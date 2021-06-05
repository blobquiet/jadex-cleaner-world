package masd_jadex.titan.agents;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Capability;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.OnStart;
import jadex.extension.envsupport.environment.AbstractEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import masd_jadex.titan.Exploration.ExplorationCapability;

@Agent(type= BDIAgentFactory.TYPE)
public class Scout extends MobileAgent {
    @Agent
    IInternalAccess agent;

    @AgentFeature
    IBDIAgentFeature bdi;

    @Capability
    ExplorationCapability exploration = new ExplorationCapability();

    @OnStart
    void body() {
        exploration.init();
    }

    protected ISpaceObject miningSiteDiscoveredPercept = null;

    @Belief
    public void setMiningSiteDiscoveredPercept(ISpaceObject obj) {
        miningSiteDiscoveredPercept = obj;
    }

    @Belief
    public ISpaceObject getMiningSiteDiscoveredPercept() {
        return miningSiteDiscoveredPercept;
    }

    @Plan(trigger=@Trigger(factchanged="miningSiteDiscoveredPercept"))
    protected void reactToCurrentMiningSiteDepletedPercept() {
        if (miningSiteDiscoveredPercept != null) {
            exploration.miningSiteDiscovered(miningSiteDiscoveredPercept);
        }
    }

    public AbstractEnvironmentSpace getEnv() {return locomotion.getEnvironment();};
}
