package masd_jadex.titan.agents;

import jadex.application.EnvironmentService;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Capability;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.ITerminableFuture;
import jadex.extension.envsupport.math.IVector2;
import jadex.micro.annotation.*;
import masd_jadex.titan.model.MiningSiteInfo;
import masd_jadex.titan.model.MiningSlotAssignment;
import masd_jadex.titan.work_pool_supervision.IWorkPoolSupervision;
import masd_jadex.titan.work_pool_supervision.WorkPoolSupervisionCapability;


@Agent(type= BDIAgentFactory.TYPE)
@Service
@ProvidedServices(@ProvidedService(type=IWorkPoolSupervision.class, scope=ServiceScope.PLATFORM))
public class Supervisor implements IWorkPoolSupervision
{
    @Agent
    IInternalAccess agent;

    @AgentFeature
    IBDIAgentFeature bdi;

    @Capability
    WorkPoolSupervisionCapability workPoolSupervisionCapability = new WorkPoolSupervisionCapability();

    @OnStart
    public void body()
    {
        bdi.dispatchTopLevelGoal(workPoolSupervisionCapability.new AcquireMiningSitesGoal());
    }

    @Override
    public ITerminableFuture<MiningSlotAssignment> requestMiningSlot(IVector2 agentPosition) {
        return workPoolSupervisionCapability.requestMiningSlot(agentPosition);
    }

    @Override
    public void takeMiningSlot(int slotReservationId) {
        workPoolSupervisionCapability.takeMiningSlot(slotReservationId);
    }

    @Override
    public void freeMiningSlot(int slotReservationId) {
        workPoolSupervisionCapability.freeMiningSlot(slotReservationId);
    }

    @Override
    public void foundMiningSite(MiningSiteInfo info) {
        workPoolSupervisionCapability.foundMiningSite(info);
    }

    @Override
    public void miningSiteDepleted(IVector2 miningSitePosition) {
        workPoolSupervisionCapability.miningSiteDepleted(miningSitePosition);
    }
}
