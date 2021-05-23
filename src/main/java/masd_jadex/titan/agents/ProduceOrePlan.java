package masd_jadex.titan.agents;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.math.IVector2;
import masd_jadex.titan.capabilities.LocomotionCapability;
import masd_jadex.titan.resource_extraction.ReserveMiningSlotGoal;
import masd_jadex.titan.resource_extraction.ResourceExtractionCapability;

@Plan
public class ProduceOrePlan
{
    @PlanCapability
    protected Miner miner;

    @PlanAPI
    protected IPlan rplan;

    @PlanReason
    protected Miner.ProduceOreGoal goal;

    public ProduceOrePlan() { }

    @PlanBody
    public IFuture<Void> body()
    {
        final Future<Void> ret = new Future<Void>();
        reserveMiningSlot(ret);
        return ret;
    }

    private void reserveMiningSlot(Future<Void> ret) {
        IFuture<ReserveMiningSlotGoal> fut = rplan.dispatchSubgoal(new ReserveMiningSlotGoal(
                (IVector2) miner.locomotion.getAvatar().getProperty("position")
        ));
        fut.addResultListener(
                // If an exception occurs (e.g. plan failure, then the ret Future gets called with it right away
                new ExceptionDelegationResultListener<ReserveMiningSlotGoal, Void>(ret)
                {
                    @Override
                    public void customResultAvailable(ReserveMiningSlotGoal result) {
                        ReserveMiningSlotGoal.AssignMiningSlotMsgData data = result.getAssignMiningSlotMsgData();
                        moveToMiningSite(ret, data.slotReservationId, data.miningSitePostion);
                    }
                }
            );
    }

    private void moveToMiningSite(Future<Void> ret, int slotReservationId, IVector2 miningSitePosition)
    {
        IFuture<LocomotionCapability.MoveGoal> fut = rplan.dispatchSubgoal(new LocomotionCapability.MoveGoal(miningSitePosition));
        fut.addResultListener(
                new ExceptionDelegationResultListener<LocomotionCapability.MoveGoal, Void>(ret)
                {
                    @Override
                    public void customResultAvailable(LocomotionCapability.MoveGoal result) throws Exception {
                        ResourceExtractionCapability resourceExtraction = miner.getResourceExtraction();
                        // TODO: execute takeMiningSlot on space object (add freeSlots variable in xml and add a Task that decrements it or fails if 0)
                        resourceExtraction.getWorkPoolSupervision().takeMiningSlot(slotReservationId);
                        // TODO: exectute drillForOre task on space object (add task to xml and implement energy reduction on agent)
                        // TODO: how do we spawn an ore instance in the space: locomotionCapability.getEnvironment().componentAdded();
                        // TODO: check how percepts are implemented and stop when the Mining site is depleted
                        resourceExtraction.getWorkPoolSupervision().freeMiningSlot(slotReservationId);
                        ret.setResult(null);
                    }
                }
        );
    }

    @PlanAborted
    public void aborted()
    {

    }

    @PlanFailed
    public void failed(Exception e)
    {

    }

}
