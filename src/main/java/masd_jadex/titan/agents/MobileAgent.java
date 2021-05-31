package masd_jadex.titan.agents;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Capability;
import jadex.bridge.IInternalAccess;
import jadex.micro.annotation.Agent;
import masd_jadex.titan.locomotion.LocomotionCapability;

@Agent(type= BDIAgentFactory.TYPE)
public class MobileAgent
{
    @Agent
    protected IInternalAccess agent;

    /** The customer capability. */
    @Capability
    protected LocomotionCapability locomotion = new LocomotionCapability();

    public LocomotionCapability getLocomotion() {
        return locomotion;
    }

}
