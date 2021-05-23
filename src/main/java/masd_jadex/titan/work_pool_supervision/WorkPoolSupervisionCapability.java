package masd_jadex.titan.work_pool_supervision;

import jadex.bdiv3.annotation.*;
import jadex.extension.envsupport.math.IVector2;
import masd_jadex.titan.model.MiningSiteInfo;

import java.util.LinkedList;
import java.util.List;

@Capability
@Plans({
        @Plan(trigger=@Trigger(goals={WorkPoolSupervisionCapability.AcquireMiningSitesGoal.class,}), body=@Body(WorkPoolSupervisionCapability.class)),
})
public class WorkPoolSupervisionCapability implements IWorkPoolSupervision
{
    public static final int LOW_WORK_POOL_THRESHOLD = 2;
    public static final int Ok_WORK_POOL_THRESHOLD = 10;

    public static class MiningSite {
        protected int id;
        protected int numSlots;
        protected IVector2 position;
    }

    protected List<MiningSite> workPool = new LinkedList<>();

    @Goal(unique=true, rebuild=true, recurdelay=3000)
    public class AcquireMiningSitesGoal
    {
        @GoalMaintainCondition
        boolean isWorkPoolLow() {
            return countFreeSlots() < LOW_WORK_POOL_THRESHOLD;
        }

        @GoalTargetCondition
        boolean isWorkPoolOk() {
            return countFreeSlots() > Ok_WORK_POOL_THRESHOLD;
        }

        private int countFreeSlots() {
            int freeSlots = 0;
            for (MiningSite site : workPool)
            {
                freeSlots += site.numSlots;
            }
            return freeSlots;
        }
    }

    @Plan(trigger=@Trigger(goals=AcquireMiningSitesGoal.class))
    protected void aquireMiningSitesPlan()
    {
        // TODO: create scout agents and tell them to explore
    }

    @Override
    public void requestMiningSlot(IVector2 agentPosition) {

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
