package masd_jadex.titan.agents;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.PlanFinishedTaskCondition;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.IVector2;
import masd_jadex.titan.locomotion.LocomotionCapability;
import masd_jadex.titan.model.MiningSlotAssignment;
import masd_jadex.titan.resource_extraction.ReserveMiningSlotGoal;
import masd_jadex.titan.resource_extraction.ResourceExtractionCapability;
import masd_jadex.titan.tasks.TakeMiningSlotTask;

import java.util.HashMap;
import java.util.Map;

@Plan
public class ProduceOrePlan
{
    @PlanCapability
    protected Miner miner;

    @PlanAPI
    protected IPlan rplan;

    @PlanReason
    protected Miner.ProduceOreGoal goal;

    public ProduceOrePlan() { }

    @PlanBody
    public IFuture<Void> body()
    {
        final Future<Void> ret = new Future<Void>();
        reserveMiningSlot(ret);
        return ret;
    }

    private void reserveMiningSlot(Future<Void> ret) {
        IFuture<MiningSlotAssignment> fut = rplan.dispatchSubgoal(new ReserveMiningSlotGoal(
                (IVector2) miner.locomotion.getAvatar().getProperty("position")
        ));
        fut.addResultListener(
                // If an exception occurs (e.g. plan failure, then the ret Future gets called with it right away
                new ExceptionDelegationResultListener<MiningSlotAssignment, Void>(ret)
                {
                    @Override
                    public void customResultAvailable(MiningSlotAssignment result) throws PlanFailureException {
                        if (result == null) {
                            // if we didn't get some work assigned, then we fail the plan to retry later
                            throw new PlanFailureException("Produce Ore Plan did not receive a mining slot. Failing plan.");
                        }

                        moveToMiningSite(ret, result.slotReservationId, result.miningSitePosition, result.miningSiteId);
                    }
                }
            );
    }

    private void moveToMiningSite(Future<Void> ret, int slotReservationId, IVector2 miningSitePosition, int miningSiteId)
    {
        IFuture<LocomotionCapability.MoveGoal> fut = rplan.dispatchSubgoal(new LocomotionCapability.MoveGoal(miningSitePosition));
        fut.addResultListener(
                new ExceptionDelegationResultListener<LocomotionCapability.MoveGoal, Void>(ret)
                {
                    @Override
                    public void customResultAvailable(LocomotionCapability.MoveGoal result) {
                        takeMiningSlot(ret, slotReservationId, miningSiteId);
                    }
                }
        );
    }

    private void takeMiningSlot(Future<Void> ret, int slotReservationId, int miningSiteId) {
        // take mining slot on space object
        Map<String, Object> props = new HashMap<String, Object>();
        // plan finished condition aborts object tasks in space if the plan somehow finishes in the meantime
        props.put(AbstractTask.PROPERTY_CONDITION, new PlanFinishedTaskCondition(rplan));
        props.put(TakeMiningSlotTask.PROPERTY_ID, miningSiteId);
        IEnvironmentSpace space = miner.locomotion.getEnvironment();
        ISpaceObject avatar = miner.locomotion.getAvatar();
        Object takeSlotTaskId = space.createObjectTask(TakeMiningSlotTask.TASK_TYPENAME, props, avatar.getId());
        //todo: try without undone, not completely sure for what it is
        space.addTaskListener(takeSlotTaskId, avatar.getId(), new ExceptionDelegationResultListener<Object, Void>(ret, true) {
            @Override
            public void customResultAvailable(Object finishedTaskId) throws Exception {
                // inform supervisor about occupied slot
                ResourceExtractionCapability resourceExtraction = miner.getResourceExtraction();
                resourceExtraction.notifyMiningSlotTaken(slotReservationId);

                drillForOre(ret, slotReservationId);
            }
        });
    }

    private void drillForOre(Future<Void> ret, int slotReservationId) {
        // TODO: exectute drillForOre task on space object (add task to xml and implement energy reduction on agent)
        // TODO: how do we spawn an ore instance in the space: locomotionCapability.getEnvironment().componentAdded();
        // TODO: check how percepts are implemented and stop when the Mining site is depleted

        // todo: create and execute freeMiningSlot task
        miner.getResourceExtraction().notifyMiningSlotFreed(slotReservationId);
        ret.setResult(null);
    }

    @PlanAborted
    public void aborted()
    {
        System.out.println("ProduceOrePlan aborted.");
        cleanup();
    }

    @PlanFailed
    public void failed(Exception e)
    {
        System.out.println("ProduceOrePlan failed with exception: " + e.toString());
        cleanup();
    }

    protected void cleanup() {
        // Todo: free mining slot
        //miner.getResourceExtraction().notifyMiningSlotFreed(slotReservationId);
    }

}
