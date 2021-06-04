package masd_jadex.titan.Exploration;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Double;
import masd_jadex.titan.locomotion.LocomotionCapability;

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

        return ret;
    }
}
