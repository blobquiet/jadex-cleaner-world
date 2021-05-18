package masd_jadex.titan.capabilities;


import jadex.application.EnvironmentService;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Capability;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.runtime.ICapability;
import jadex.extension.envsupport.environment.AbstractEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.IVector2;
import jadex.micro.annotation.Agent;

import java.util.ArrayList;
import java.util.List;

@Capability
//@Plans({
//        @Plan(trigger=@Trigger(goals={MovementCapability.Move.class, MovementCapability.Missionend.class}), body=@Body(MoveToLocationPlan.class)),
//})
//@RequiredServices(@RequiredService(name="clockservice", type= IClockService.class))
public class LocomotionCapability {
    @Agent
    protected ICapability capability;

    protected AbstractEnvironmentSpace env = (AbstractEnvironmentSpace) EnvironmentService.getSpace(capability.getAgent(), "masd_jadex/titan").get();

    protected ISpaceObject avatar = env.getAvatar(capability.getAgent().getDescription(), capability.getAgent().getModel().getFullName());

    @Belief
    protected List<ISpaceObject> targets = new ArrayList<ISpaceObject>();

    @Goal
    public class MoveGoal implements IDestinationGoal {
        protected IVector2 destination;

        public MoveGoal(IVector2 destination) {
            this.destination = destination;
        }

        public Object getDestination() {
            return destination;
        }
    }

    public AbstractEnvironmentSpace getEnvironment() {
        return env;
    }

    public ISpaceObject getAvatar() {
        return avatar;
    }

    public ICapability getCapability() {
        return capability;
    }

    public List<ISpaceObject> getTargets() {
        return targets;
    }


    public void addTarget(ISpaceObject target) {
        if (!targets.contains(target)) {
            targets.add(target);
        }
    }
}
