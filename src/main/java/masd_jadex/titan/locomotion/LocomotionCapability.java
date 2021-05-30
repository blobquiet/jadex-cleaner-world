package masd_jadex.titan.locomotion;


import jadex.application.EnvironmentService;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.ICapability;
import jadex.extension.envsupport.environment.AbstractEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.IVector2;
import jadex.micro.annotation.Agent;


@Capability
@Plans({
        @Plan(trigger=@Trigger(goals={LocomotionCapability.MoveGoal.class,}), body=@Body(GoToTargetPlan.class)),
})
public class LocomotionCapability {
    @Agent
    protected ICapability capability;

    protected AbstractEnvironmentSpace env = (AbstractEnvironmentSpace) EnvironmentService.getSpace(capability.getAgent(), "titan").get();

    protected ISpaceObject avatar = env.getAvatar(capability.getAgent().getDescription(), capability.getAgent().getModel().getFullName());

    @Goal
    public static class MoveGoal implements IDestinationGoal {
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
}
