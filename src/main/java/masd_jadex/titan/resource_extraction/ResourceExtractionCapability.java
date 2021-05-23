package masd_jadex.titan.resource_extraction;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.ICapability;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.future.Future;
import jadex.extension.envsupport.math.IVector2;
import jadex.micro.annotation.*;
import masd_jadex.titan.work_pool_supervision.IWorkPoolSupervision;

@Capability
@RequiredServices(@RequiredService(name="work_pool_supervision", type=IWorkPoolSupervision.class, scope= ServiceScope.APPLICATION))
@Plans({
        @Plan(trigger=@Trigger(goals=ReserveMiningSlotGoal.class), body=@Body(ReserveMiningSlotPlan.class)),
})
public class ResourceExtractionCapability implements IResourceExtraction
{
    @Agent
    protected ICapability capability;

    public IWorkPoolSupervision getWorkPoolSupervision()
    {
        return (IWorkPoolSupervision) capability.getAgent().getFeature(IRequiredServicesFeature.class).getService("work_pool_supervision").get();
    }

    // slot for receiving the answer from the work pool supervision role
    protected Future<ReserveMiningSlotGoal.AssignMiningSlotMsgData> pendingAssignMiningSlotMsg = null;

    public void receiveAssignMiningSlotMsg(Future<ReserveMiningSlotGoal.AssignMiningSlotMsgData> fut) throws IllegalStateException {
        if (pendingAssignMiningSlotMsg != null) {
            throw new IllegalStateException("Cannot listen for AssignMiningSlotMsg because we are already listening!");
        }

        pendingAssignMiningSlotMsg = fut;
    }

    @Override
    public void assignMiningSlot(IVector2 miningSitePosition, int slotReservationId) {
        ReserveMiningSlotGoal.AssignMiningSlotMsgData result = new ReserveMiningSlotGoal.AssignMiningSlotMsgData();
        result.miningSitePostion = miningSitePosition;
        result.slotReservationId = slotReservationId;
        pendingAssignMiningSlotMsg.setResult(result);
        pendingAssignMiningSlotMsg = null;
    }

    @Override
    public void denyMiningSlot() {
        if (pendingAssignMiningSlotMsg == null) {
            throw new IllegalStateException("Cannot receive AssignMiningSlotMsg because we are are not listening for it!");
        }

        pendingAssignMiningSlotMsg.setResult(null);
        pendingAssignMiningSlotMsg = null;
    }
}
