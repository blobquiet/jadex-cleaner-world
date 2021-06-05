package masd_jadex.titan.Exploration;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.PlanFinishedTaskCondition;
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Double;
import masd_jadex.titan.agents.Scout;
import masd_jadex.titan.locomotion.LocomotionCapability;
import masd_jadex.titan.tasks.MiningSiteTask;
import masd_jadex.titan.tasks.MoveTask;
import masd_jadex.titan.tasks.PerceiveMiningSlotsTask;

import java.util.HashMap;
import java.util.Map;

@Plan
public class ExplorePlan {

    @PlanCapability
    protected ExplorationCapability capability;

    @PlanAPI
    protected IPlan rplan;

    @PlanReason
    protected ExploreGoal goal;

    @PlanBody
    public IFuture<Void> body() {
        Future<Void> ret  = new Future<>();
        // random walk
        IVector2 nextTarget = new Vector2Double(Math.random(), Math.random()).multiply(100);
        IFuture<LocomotionCapability.MoveGoal> fut = rplan.dispatchSubgoal(new LocomotionCapability.MoveGoal(nextTarget));
        fut.addResultListener(new ExceptionDelegationResultListener<LocomotionCapability.MoveGoal, Void>(ret)
        {
            public void customResultAvailable(LocomotionCapability.MoveGoal goal)
            {
                ret.setResult(null);
            }
        });

        Map<String, Object> props = new HashMap<String, Object>();
        props.put(MiningSiteTask.PROPERTY_AGENT_REF, capability.capability.getAgent().getExternalAccess());
       props.put(AbstractTask.PROPERTY_CONDITION, new PlanFinishedTaskCondition(rplan));
        IEnvironmentSpace space = capability.env;

        Object moveTaskId = space.createObjectTask(PerceiveMiningSlotsTask.TASK_TYPENAME, props, capability.avatar.getId());
        // not waiting, will abort when plan aborts
        return ret;
    }
}
