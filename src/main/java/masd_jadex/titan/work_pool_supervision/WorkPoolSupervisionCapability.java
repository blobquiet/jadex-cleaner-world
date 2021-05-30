package masd_jadex.titan.work_pool_supervision;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.ICapability;
import jadex.commons.future.ITerminableFuture;
import jadex.commons.future.ITerminationCommand;
import jadex.commons.future.TerminableFuture;
import jadex.commons.future.TerminationCommand;
import jadex.extension.envsupport.math.IVector2;
import jadex.micro.annotation.Agent;
import masd_jadex.titan.model.MiningSiteInfo;
import masd_jadex.titan.model.MiningSlotAssignment;

import java.util.LinkedList;
import java.util.List;

@Capability
public class WorkPoolSupervisionCapability implements IWorkPoolSupervision
{
    protected static final int LOW_WORK_POOL_THRESHOLD = 2;
    protected static final int OK_WORK_POOL_THRESHOLD = 10;

    public static class MiningSite {
        protected int id;
        protected int numSlots;
        protected IVector2 position;
    }

    @Agent
    protected ICapability capability;

    @Belief
    protected final List<MiningSite> workPool = new LinkedList<>();

    @Goal(recur = true, recurdelay=3000)
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
                    freeSlots += site.numSlots;
                }
            }
            return freeSlots;
        }
    }

    @Plan(trigger=@Trigger(goals=AcquireMiningSitesGoal.class))
    protected void aquireMiningSitesPlan()
    {
        System.out.println("We have too few mining sites, we have to find some more!");
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

        }
        return res;
    }

    @Override
    public void takeMiningSlot(int slotReservationId) {

    }

    @Override
    public void freeMiningSlot(int slotReservationId) {

    }

    @Override
    public void foundMiningSite(MiningSiteInfo info) {

    }

    @Override
    public void miningSiteDepleted(IVector2 miningSitePosition) {

    }
}
