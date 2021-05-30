package masd_jadex.titan.resource_extraction;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.ICapability;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.component.IRequiredServicesFeature;
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
}
