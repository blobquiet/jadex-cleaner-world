package masd_jadex.titan.resource_extraction;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bridge.TimeoutResultListener;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.ITerminableFuture;
import masd_jadex.titan.model.MiningSlotAssignment;
import masd_jadex.titan.work_pool_supervision.IWorkPoolSupervision;

@Plan
public class ReserveMiningSlotPlan {

    @PlanCapability
    protected ResourceExtractionCapability capability;

    @PlanAPI
    protected IPlan rplan;

    @PlanReason
    protected ReserveMiningSlotGoal goal;

    public ReserveMiningSlotPlan() { }

    @PlanBody
    public IFuture<Void> body()
    {
        final Future<Void> ret = new Future<Void>();

        IWorkPoolSupervision workPoolSupervision = capability.getWorkPoolSupervision();
        ITerminableFuture<MiningSlotAssignment> miningSlotFuture = workPoolSupervision.requestMiningSlot(goal.requestPosition);

        miningSlotFuture.addResultListener(
                new TimeoutResultListener<MiningSlotAssignment>(3000, capability.capability.getAgent().getExternalAccess(),
                        new ExceptionDelegationResultListener<MiningSlotAssignment, Void>(ret) {
                            @Override
                            public void customResultAvailable(MiningSlotAssignment result) throws PlanFailureException {
                                if (result == null) {
                                    throw new PlanFailureException("Failed to reserve a mining slot. Assignment denied by work pool supervision.");
                                } else {
                                    goal.setMiningSlotAssignment(result);
                                    ret.setResult(null);
                                }
                            }
                        })
        );
        return ret;
    }

}
