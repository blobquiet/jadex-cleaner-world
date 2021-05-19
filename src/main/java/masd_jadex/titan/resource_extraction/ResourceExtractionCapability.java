package masd_jadex.titan.resource_extraction;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.ICapability;
import jadex.bridge.service.ServiceScope;
import jadex.extension.envsupport.math.IVector2;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
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

    @AgentFeature
    protected IWorkPoolSupervision workPoolSupervision;

    @Belief
    public Integer slotReservationId = null;
    @Belief
    public IVector2 miningSitePosition = null;

    public IWorkPoolSupervision getWorkPoolSupervision()
    {
        return workPoolSupervision;
    }

    @Override
    public void assignMiningSlot(IVector2 miningSitePosition, int slotReservationId) {
        this.miningSitePosition = miningSitePosition;
        this.slotReservationId = slotReservationId;
    }

    @Override
    public void denyMiningSlot() {

    }
}
