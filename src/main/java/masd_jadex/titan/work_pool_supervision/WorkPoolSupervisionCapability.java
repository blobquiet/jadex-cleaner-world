package masd_jadex.titan.work_pool_supervision;

import jadex.application.EnvironmentService;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.ICapability;
import jadex.commons.future.ITerminableFuture;
import jadex.commons.future.ITerminationCommand;
import jadex.commons.future.TerminableFuture;
import jadex.commons.future.TerminationCommand;
import jadex.extension.envsupport.environment.AbstractEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.IVector2;
import jadex.micro.annotation.Agent;
import masd_jadex.titan.model.MiningSiteInfo;
import masd_jadex.titan.model.MiningSlotAssignment;

import java.util.*;

@Capability
public class WorkPoolSupervisionCapability implements IWorkPoolSupervision
{
    protected static final int LOW_WORK_POOL_THRESHOLD = 2;
    protected static final int OK_WORK_POOL_THRESHOLD = 10;

    public static class MiningSite {
        private static int reservationIdCounter = 0;
        public static int getNextReservationId() {
            reservationIdCounter += 1;
            return reservationIdCounter;
        }

        protected int id;
        protected int numSlots;
        protected IVector2 position;
        protected boolean depleted = false;
        protected Set<Integer> reserved = new HashSet<>();
        protected Set<Integer> taken = new HashSet<>();

        public int numNonReservedSlots() { return numSlots - reserved.size() - taken.size(); }
    }

    @Agent
    protected ICapability capability;

    @Belief
    protected final List<MiningSite> workPool = new LinkedList<>();

    @Goal(recur=true, recurdelay=3000)
    public class AcquireMiningSitesGoal
    {
        @GoalMaintainCondition
        boolean isWorkPoolLow() {
            //noinspection ResultOfMethodCallIgnored
            workPool.size(); // just there to let Jadex know that we read the work pool in the function call below
            return countFreeSlots() > LOW_WORK_POOL_THRESHOLD;
        }

        @GoalTargetCondition
        boolean isWorkPoolOk() {
            //noinspection ResultOfMethodCallIgnored
            workPool.size(); // just there to let Jadex know that we read the work pool in the function call below
            return countFreeSlots() > OK_WORK_POOL_THRESHOLD;
        }

        private int countFreeSlots() {
            int freeSlots = 0;
            synchronized (workPool) {
                for (MiningSite site : workPool) {
                    if (site.depleted)
                        continue;
                    freeSlots += site.numNonReservedSlots();
                }
            }

            return freeSlots;
        }
    }

    boolean startValuesRead = false;
    @Plan(trigger=@Trigger(goals=AcquireMiningSitesGoal.class))
    protected void aquireMiningSitesPlan()
    {
        System.out.println("We have too few mining sites, we have to find some more!");
        // Hack for mining sites discovered already at startup

        if (!startValuesRead) {
            for (ISpaceObject obj : ((AbstractEnvironmentSpace) EnvironmentService.getSpace(capability.getAgent(), "titan").get()).getSpaceObjectsByType("MiningSite")) {
                if ((Boolean) obj.getProperty("discovered")) {
                    MiningSite site = new MiningSite();
                    site.position = ((IVector2) obj.getProperty("position"));
                    site.depleted = (Boolean) obj.getProperty("depleted");
                    site.id = (Integer) obj.getProperty("id");
                    site.numSlots = (Integer) obj.getProperty("num_slots");
                    synchronized (workPool) {
                        workPool.add(site);
                    }
                }
            }

            startValuesRead = true;
        }
        // TODO: create scout agents and tell them to explore
    }

    @Override
    public ITerminableFuture<MiningSlotAssignment> requestMiningSlot(IVector2 agentPosition) {
        TerminableFuture<MiningSlotAssignment> res = new TerminableFuture<>();
        // This command gets called if the caller decides to terminate the future and not wait anymore for the result
        ITerminationCommand termCmd = new TerminationCommand() {
            @Override
            public void terminated(Exception reason) {
                System.out.println("Miner aborted request for work because I was not able to provide work in time!");
                // todo: implement termination of slot assignment
            }
        };

        res.setTerminationCommand(termCmd);

        synchronized (workPool)
        {
            MiningSlotAssignment assignment = null;
            for (MiningSite site : workPool) {
                if (site.depleted)
                    continue;

                if (site.numNonReservedSlots() > 0) {
                    assignment = new MiningSlotAssignment();
                    assignment.miningSitePosition = site.position;
                    assignment.miningSiteId = site.id;
                    int reservationId = MiningSite.getNextReservationId();
                    site.reserved.add(reservationId);
                    assignment.slotReservationId = reservationId;
                    break;
                }
            }

            // returns null if no free mining slots available
            // else returns valid mining slot assignment
            res.setResult(assignment);
        }

        return res;
    }

    @Override
    public void takeMiningSlot(int slotReservationId) {
        boolean reservationFound = false;
        synchronized (workPool)
        {
            for (MiningSite site : workPool) {
                reservationFound = site.reserved.contains(slotReservationId);
                if (reservationFound) {
                    site.reserved.remove(slotReservationId);
                    site.taken.add(slotReservationId);
                    break;
                }
            }
        }

        if (!reservationFound) {
            System.out.println("Someone took a mining slot that no reservation could be found for.");
        }
    }

    @Override
    public void freeMiningSlot(int slotReservationId) {
        boolean reservationFound = false;
        synchronized (workPool)
        {
            for (MiningSite site : workPool) {
                reservationFound = site.taken.contains(slotReservationId);
                if (reservationFound) {
                    site.taken.remove(slotReservationId);
                    break;
                }
            }
        }

        if (!reservationFound) {
            System.out.println("Someone freed a mining slot that no reservation could be found for.");
        }
    }

    @Override
    public void foundMiningSite(MiningSiteInfo info) {
        MiningSite miningSite = new MiningSite();
        miningSite.id = info.id;
        miningSite.numSlots = info.numSlots;
        miningSite.position = info.position;
        synchronized (workPool) {
            for (MiningSite site : workPool) {
                if (site.id == info.id) {
                    System.out.println("Some rediscovered the same mining site as someone else. Ignoring.");
                    return;
                }
            }

            workPool.add(miningSite);
        }
    }

    @Override
    public void miningSiteDepleted(IVector2 miningSitePosition) {
        synchronized (workPool) {
            MiningSite miningSite = null;
            double minDist = Double.MAX_VALUE;
            for (MiningSite site : workPool) {
                if (miningSite == null) {
                    minDist = site.position.copy().subtract(miningSitePosition).getSquaredLength().getAsDouble();
                    miningSite = site;
                    continue;
                }

                double d = miningSite.position.copy().subtract(miningSitePosition).getSquaredLength().getAsDouble();
                if( d < minDist) {
                    miningSite = site;
                    minDist = d;
                }
            }

            if (miningSite == null) {
                System.out.println("Someone said a mining site is depleted, but there are no mining sites known yet!");
                return;
            } else if (minDist > 0.001) {
                System.out.println("Someone said a mining site is depleted, but doesn't seem to know its location! We ignore it.");
                return;
            }

            miningSite.depleted = true;
        }


    }
}
