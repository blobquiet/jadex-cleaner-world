package masd_jadex.titan.capabilities;

import java.util.HashMap;
import java.util.Map;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.PlanFinishedTaskCondition;
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import masd_jadex.titan.tasks.MoveTask;


@Plan
public class GoToTargetPlan
{

    @PlanCapability
    protected LocomotionCapability capability;

    @PlanAPI
    protected IPlan rplan;

    @PlanReason
    protected IDestinationGoal goal;

    @PlanBody
    public IFuture<Void> body()
    {
        ISpaceObject avatar	= capability.getAvatar();
        Object dest = goal.getDestination();

        Map<String, Object> props = new HashMap<String, Object>();
        props.put(MoveTask.PROPERTY_DESTINATION, dest);
        props.put(AbstractTask.PROPERTY_CONDITION, new PlanFinishedTaskCondition(rplan));
        IEnvironmentSpace space = capability.getEnvironment();

        Future<Void> fut = new Future<Void>();
        Object moveTaskId = space.createObjectTask(MoveTask.PROPERTY_TYPENAME, props, avatar.getId());
        space.addTaskListener(moveTaskId, avatar.getId(), new DelegationResultListener<Void>(fut, true));
        fut.get();
        return IFuture.DONE;
    }
}