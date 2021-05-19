package masd_jadex.titan.agents;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

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

    @Goal
    public class ProduceOreGoal
    {

    }


}
