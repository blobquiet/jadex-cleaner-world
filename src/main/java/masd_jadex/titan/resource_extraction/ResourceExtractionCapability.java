package masd_jadex.titan.resource_extraction;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.ICapability;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.*;
import masd_jadex.titan.work_pool_supervision.IWorkPoolSupervision;

@Capability
@RequiredServices(@RequiredService(type=IWorkPoolSupervision.class))
@Plans({
        @Plan(trigger=@Trigger(goals=ReserveMiningSlotGoal.class), body=@Body(ReserveMiningSlotPlan.class)),
})
public class ResourceExtractionCapability {
    @Agent
    protected ICapability capability;

    public IWorkPoolSupervision getWorkPoolSupervision() {
        return (IWorkPoolSupervision) capability.getAgent().getFeature(IRequiredServicesFeature.class).getService(IWorkPoolSupervision.class).get();
    }

    public void notifyMiningSlotTaken(int reservationId) {
        capability.getAgent().scheduleStep(new IComponentStep<Object>() {

            @Override
            public IFuture<Object> execute(IInternalAccess ia) {
                getWorkPoolSupervision().takeMiningSlot(reservationId);
                return new Future<Object>((Object)null);
            }
        }).get();

    }

    public void notifyMiningSlotFreed(int reservationId) {
        capability.getAgent().scheduleStep(new IComponentStep<Object>() {

            @Override
            public IFuture<Object> execute(IInternalAccess ia) {
                getWorkPoolSupervision().freeMiningSlot(reservationId);
                return new Future<Object>((Object)null);
            }
        }).get();
    }

}
