package masd_jadex.titan.agents;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.runtime.ChangeEvent;
import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.PlanFinishedTaskCondition;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.OnStart;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.IVector2;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.rules.eca.ChangeInfo;
import masd_jadex.titan.locomotion.LocomotionCapability;
import masd_jadex.titan.model.MiningSlotAssignment;
import masd_jadex.titan.resource_extraction.ReserveMiningSlotGoal;
import masd_jadex.titan.resource_extraction.ResourceExtractionCapability;
import masd_jadex.titan.tasks.DrillForOreTask;
import masd_jadex.titan.tasks.FreeMiningSlotTask;
import masd_jadex.titan.tasks.MiningSiteTask;
import masd_jadex.titan.tasks.TakeMiningSlotTask;

import java.util.HashMap;
import java.util.Map;

@Agent(type= BDIAgentFactory.TYPE)
public class Miner extends MobileAgent
{
    @Agent
    IInternalAccess agent;

    @AgentFeature
    IBDIAgentFeature bdi;

    @Belief
    protected int currentMiningSiteId = -1;
    protected int currentReservationId = -1;
    protected int oldReservationId = -1;
    protected IVector2 currentMiningsitePos = null;

    protected boolean currentMiningSiteDepletedPercept = false;

    @Belief
    public void setCurrentMiningSiteDepletedPercept(boolean isDepleted) {
        currentMiningSiteDepletedPercept = isDepleted;
    }

    @Belief
    public boolean getCurrentMiningSiteDepletedPercept() {
        return currentMiningSiteDepletedPercept;
    }

    @Capability
    protected ResourceExtractionCapability resourceExtraction = new ResourceExtractionCapability();

    ResourceExtractionCapability getResourceExtraction()
    {
        return resourceExtraction;
    }

    @OnStart
    public void body()
    {
        bdi.dispatchTopLevelGoal(new ProduceOreGoal());
    }

    @Goal(recur=true, recurdelay=3000)
    public static class ProduceOreGoal
    {

    }

    @Plan(trigger=@Trigger(factchanged="currentMiningSiteDepletedPercept"))
    protected void reactToCurrentMiningSiteDepletedPercept() {
        if (currentMiningsitePos != null) {
            resourceExtraction.getWorkPoolSupervision().miningSiteDepleted(currentMiningsitePos);
        }
    }

    @Plan(trigger=@Trigger(factchanged="currentMiningSiteId"))
    protected class ReactToMiningSiteIdChangePlan
    {
        @PlanAPI
        protected IPlan rplan;

        @PlanBody
        public IFuture<Void> reactToMiningSiteIdChange(ChangeEvent<ChangeInfo<Integer>> event) {
            Future<Void> ret = new Future<>();
            Integer previousInt = event.getValue().getOldValue();
            Integer currentInt = event.getValue().getValue();
            int previous = previousInt == null ? -1 : previousInt;
            int current = currentInt == null ? -1 : currentInt;
            if (current != previous) {
                if (previous != -1) {
                    // we freed some mining slot
                    Map<String, Object> props = new HashMap<String, Object>();
                    // plan finished condition aborts object tasks in space if the plan somehow finishes in the meantime
                    props.put(AbstractTask.PROPERTY_CONDITION, new PlanFinishedTaskCondition(rplan));
                    props.put(MiningSiteTask.PROPERTY_ID, previous);
                    IEnvironmentSpace space = locomotion.getEnvironment();
                    ISpaceObject avatar = locomotion.getAvatar();
                    Object takeSlotTaskId = space.createObjectTask(FreeMiningSlotTask.TASK_TYPENAME, props, avatar.getId());
                    space.addTaskListener(takeSlotTaskId, avatar.getId(), new ExceptionDelegationResultListener<Object, Void>(ret, true) {
                        @Override
                        public void customResultAvailable(Object finishedTaskId) throws Exception {
                            // inform supervisor about freed slot
                            ResourceExtractionCapability resourceExtraction = getResourceExtraction();
                            resourceExtraction.notifyMiningSlotFreed(oldReservationId);
                            ret.setResult(null);
                        }
                    });
                }

                if (current != -1) {
                    // we are taking a mining slot
                    Map<String, Object> props = new HashMap<String, Object>();
                    // plan finished condition aborts object tasks in space if the plan somehow finishes in the meantime
                    props.put(AbstractTask.PROPERTY_CONDITION, new PlanFinishedTaskCondition(rplan));
                    props.put(MiningSiteTask.PROPERTY_ID, current);
                    IEnvironmentSpace space = locomotion.getEnvironment();
                    ISpaceObject avatar = locomotion.getAvatar();
                    Object takeSlotTaskId = space.createObjectTask(TakeMiningSlotTask.TASK_TYPENAME, props, avatar.getId());
                    space.addTaskListener(takeSlotTaskId, avatar.getId(), new ExceptionDelegationResultListener<Object, Void>(ret, true) {
                        @Override
                        public void customResultAvailable(Object finishedTaskId) throws Exception {
                            // inform supervisor about taken slot
                            ResourceExtractionCapability resourceExtraction = getResourceExtraction();
                            resourceExtraction.notifyMiningSlotTaken(currentReservationId);
                            oldReservationId = currentReservationId;
                            ret.setResult(null);
                        }
                    });
                }
            } else {
                return Future.DONE;
            }

            return ret;
        }
    }

    @Plan(trigger = @Trigger(goals=ProduceOreGoal.class))
    protected class ProduceOrePlan
    {
        @PlanCapability
        protected Miner miner;

        @PlanAPI
        protected IPlan rplan;

        @PlanReason
        protected Miner.ProduceOreGoal goal;

        public ProduceOrePlan() { }

        // stop when the mining becomes depleted
        @PlanContextCondition(beliefs="currentMiningSiteDepletedPercept")
        public boolean checkCondition()
        {
            return !currentMiningSiteDepletedPercept;
        }

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
                            // now we are there and we can trigger the believe change listener
                            currentReservationId = slotReservationId;
                            currentMiningSiteId = miningSiteId;
                            currentMiningsitePos = miningSitePosition;
                            agent.waitForDelay(400, new IComponentStep<Void>() {
                                @Override
                                public IFuture<Void> execute(IInternalAccess ia) {
                                    drillForOre(ret);
                                    return Future.DONE;
                                }
                            });
                        }
                    }
            );
        }

        private void drillForOre(Future<Void> ret) {
            Map<String, Object> props = new HashMap<String, Object>();
            props.put(AbstractTask.PROPERTY_CONDITION, new PlanFinishedTaskCondition(rplan));
            props.put(MiningSiteTask.PROPERTY_ID, currentMiningSiteId);
            props.put(MiningSiteTask.PROPERTY_AGENT_REF, agent.getExternalAccess());
            IEnvironmentSpace space = locomotion.getEnvironment();
            ISpaceObject avatar = locomotion.getAvatar();
            Object takeSlotTaskId = space.createObjectTask(DrillForOreTask.TASK_TYPENAME, props, avatar.getId());
            space.addTaskListener(takeSlotTaskId, avatar.getId(), new ExceptionDelegationResultListener<Object, Void>(ret, true) {
                @Override
                public void customResultAvailable(Object finishedTaskId) throws Exception {
                    // inform supervisor about freed slot
                    currentMiningSiteId = -1;
                    currentReservationId = -1;
                    finish(ret);
                }
            });
        }

        private void finish(Future<Void> ret) {
            agent.waitForDelay(400, new IComponentStep<Void>() {
                @Override
                public IFuture<Void> execute(IInternalAccess ia) {
                    cleanup();
                    ret.setResult(null);
                    return Future.DONE;
                }
            });
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
            currentReservationId = -1;
            currentMiningSiteId = -1;
            currentMiningSiteDepletedPercept = false;
            currentMiningsitePos = null;
        }

    }

}
