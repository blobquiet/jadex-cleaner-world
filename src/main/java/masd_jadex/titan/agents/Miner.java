package masd_jadex.titan.agents;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import masd_jadex.titan.resource_extraction.ProduceOreGoal;

@Agent(type= BDIAgentFactory.TYPE)
public class Miner extends MobileAgent
{

    @AgentFeature
    IBDIAgentFeature bdi;

    @OnStart
    public void body()
    {
        bdi.dispatchTopLevelGoal(new ProduceOreGoal());
    }


}