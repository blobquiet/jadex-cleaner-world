package masd_jadex.titan.resource_extraction;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import masd_jadex.titan.work_pool_supervision.IWorkPoolSupervision;

@Plan
public class ReserveMiningSlotPlan {

    @PlanCapability
    protected ResourceExtractionCapability resourceExtraction;

    @PlanAPI
    protected IPlan rplan;

    @PlanReason
    protected ReserveMiningSlotGoal goal;

    public ReserveMiningSlotPlan() { }

    @PlanBody
    public IFuture<Void> body()
    {
        final Future<Void> ret = new Future<Void>();

        Future<ReserveMiningSlotGoal.AssignMiningSlotMsgData> assignMiningSlotMsgData = new Future<>();
        assignMiningSlotMsgData.addResultListener(new ExceptionDelegationResultListener<ReserveMiningSlotGoal.AssignMiningSlotMsgData, Void>(ret) {
            @Override
            public void customResultAvailable(ReserveMiningSlotGoal.AssignMiningSlotMsgData result) throws Exception {
                if (result == null) {
                    throw new PlanFailureException("Failed to reserve a mining slot. Assignment denied by work pool supervision.");
                } else {
                    goal.assignMiningSlotMsgData = result;
                    ret.setResult(null);
                }
            }
        });
        IWorkPoolSupervision workPoolSupervision = resourceExtraction.getWorkPoolSupervision();
        workPoolSupervision.requestMiningSlot(goal.requestPosition);

        return ret;
    }
}
