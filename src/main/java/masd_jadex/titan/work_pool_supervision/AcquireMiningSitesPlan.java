package masd_jadex.titan.work_pool_supervision;

import jadex.application.EnvironmentService;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.fipa.FipaMessage;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.AbstractEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.math.IVector2;
import masd_jadex.titan.model.MiningSiteInfo;
import masd_jadex.titan.tasks.MiningSiteTask;

@Plan
public class AcquireMiningSitesPlan {

    @PlanCapability
    protected WorkPoolSupervisionCapability capability;

    @PlanAPI
    protected IPlan rplan;

    @PlanReason
    protected WorkPoolSupervisionCapability.AcquireMiningSitesGoal goal;

    boolean startValuesRead = false;

    class CheckPlanComplete implements IComponentStep<Void>{
        Future<Void> ret;

        CheckPlanComplete(Future<Void> ret) { this.ret = ret; }

        @Override
        public IFuture<Void> execute(IInternalAccess ia) {
            if (!goal.isWorkPoolOk())
                capability.capability.getAgent().scheduleStep(new CheckPlanComplete(ret));
            else
                ret.setResult(null);

            return ret;
        }
    }

    @PlanBody
    public IFuture<Void> body() {
        Future<Void> ret = new Future<>();
        System.out.println("We have too few mining sites, we have to find some more!");
        // Hack for mining sites discovered already at startup
        if (!startValuesRead) {
            for (ISpaceObject obj : ((AbstractEnvironmentSpace) EnvironmentService.getSpace(capability.capability.getAgent(), "titan").get()).getSpaceObjectsByType("MiningSite")) {
                if ((Boolean)obj.getProperty(MiningSiteTask.PROPERTY_DISCOVERED)) {
                    MiningSiteInfo info = new MiningSiteInfo();
                    info.id = (Integer) obj.getProperty("id");
                    info.position = (IVector2) obj.getProperty("position");
                    info.numSlots =  (Integer) obj.getProperty("num_slots");
                    capability.foundMiningSite(info);
                }
            }

            startValuesRead = true;
        }

        for (IComponentIdentifier id : capability.knownScouts) {
            // most simple message ever
            capability.capability.getAgent().getFeature(IMessageFeature.class).sendMessage(true, id);
        }

        capability.capability.getAgent().scheduleStep(new CheckPlanComplete(ret));
        return ret;
    }

    @PlanAborted
    @PlanFailed
    @PlanPassed
    void cleanup() {
        for (IComponentIdentifier id : capability.knownScouts) {
            FipaMessage msg = new FipaMessage();
            // most simple message ever
            capability.capability.getAgent().getFeature(IMessageFeature.class).sendMessage(false, id);
        }
    }
}
