package masd_jadex.titan.agents;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import masd_jadex.titan.locomotion.LocomotionCapability;
import masd_jadex.titan.resource_extraction.ResourceExtractionCapability;

@Agent(type= BDIAgentFactory.TYPE)
@Plans({
        @Plan(trigger=@Trigger(goals={Miner.ProduceOreGoal.class, }), body=@Body(ProduceOrePlan.class)),
})
public class Miner extends MobileAgent
{
    @Agent
    IInternalAccess agent;

    @AgentFeature
    IBDIAgentFeature bdi;

    @Capability
    protected ResourceExtractionCapability resourceExtraction = new ResourceExtractionCapability();

    ResourceExtractionCapability getResourceExtraction()
    {
        return resourceExtraction;
    }

    @OnStart
    public void body()
    {
        bdi.dispatchTopLevelGoal(new ProduceOreGoal());
    }

    @Goal(recur=true, recurdelay=3000)
    public static class ProduceOreGoal
    {

    }


}
