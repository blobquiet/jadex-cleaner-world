package masd_jadex.titan.work_pool_supervision;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.OnStart;
import jadex.extension.envsupport.math.IVector2;
import jadex.micro.annotation.*;
import masd_jadex.titan.model.MiningSiteInfo;

@ProvidedServices({
        @ProvidedService(name="work_pool_supervision", type=IWorkPoolSupervision.class, implementation=@Implementation(WorkPoolSupervisionCapability.class))
})
@Agent(type= BDIAgentFactory.TYPE)
public class Supervisor
{

    @AgentFeature
    IBDIAgentFeature bdi;

    @Agent
    IInternalAccess agent;

    @Capability
    WorkPoolSupervisionCapability workPoolSuperVision = (WorkPoolSupervisionCapability) agent.getProvidedService(IWorkPoolSupervision.class);


    @OnStart
    public void body()
    {
        bdi.dispatchTopLevelGoal(workPoolSuperVision.new AcquireMiningSitesGoal());
    }

}
