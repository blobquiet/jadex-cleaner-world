package masd_jadex.titan.Exploration;

import jadex.application.EnvironmentService;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.runtime.ICapability;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.component.IMessageHandler;
import jadex.bridge.component.IMsgHeader;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.types.security.ISecurityInfo;
import jadex.extension.envsupport.environment.AbstractEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import masd_jadex.titan.agents.Scout;
import masd_jadex.titan.model.MiningSiteInfo;
import masd_jadex.titan.tasks.MiningSiteTask;
import masd_jadex.titan.work_pool_supervision.IWorkPoolSupervision;
import masd_jadex.titan.work_pool_supervision.WorkPoolSupervisionCapability;

@Capability
@RequiredServices(@RequiredService(type= IWorkPoolSupervision.class))
@Plans({
        @Plan(trigger=@Trigger(goals={ExploreGoal.class,}), body=@Body(ExplorePlan.class)),
})
public class ExplorationCapability {

    @Agent
    protected ICapability capability;

    Object exploreGoal = null;

    public IWorkPoolSupervision getWorkPoolSupervision() {
        return capability.getAgent().getFeature(IRequiredServicesFeature.class).getService(IWorkPoolSupervision.class).get();
    }

    protected AbstractEnvironmentSpace env = (AbstractEnvironmentSpace) EnvironmentService.getSpace(capability.getAgent(), "titan").get();

    protected ISpaceObject avatar = env.getAvatar(capability.getAgent().getDescription(), capability.getAgent().getModel().getFullName());

    public void init(){
        capability.getAgent().getFeature(IMessageFeature.class).addMessageHandler(new IMessageHandler() {

            @Override
            // always handle message
            public boolean isHandling(ISecurityInfo secinfos, IMsgHeader header, Object msg) { return true; }

            @Override
            // never remove the message handler
            public boolean isRemove() { return false; }

            @Override
            public void handleMessage(ISecurityInfo secinfos, IMsgHeader header, Object msg) {
                if ((Boolean)msg && exploreGoal == null) {
                    startExplorationMsg();
                } else if (!(Boolean)msg && exploreGoal != null) {
                    stopExplorationMsg();
                }
            }
        });

        // establish agent acquaintance
        getWorkPoolSupervision().subscribeScout(capability.getAgent().getId());
    }

    void startExplorationMsg() {
        exploreGoal = new ExploreGoal();
        capability.getAgent().getFeature(IBDIAgentFeature.class).dispatchTopLevelGoal(exploreGoal);
    }

    void stopExplorationMsg() {
        capability.getAgent().getFeature(IBDIAgentFeature.class).dropGoal(exploreGoal);
        exploreGoal = null;
    }

    public void miningSiteDiscovered(ISpaceObject miningSite)
    {
        MiningSiteInfo info = new MiningSiteInfo();
        info.id = (Integer) miningSite.getProperty(MiningSiteTask.PROPERTY_ID);
        info.position = (IVector2) miningSite.getProperty(Space2D.PROPERTY_POSITION);
        info.numSlots = (Integer) miningSite.getProperty(MiningSiteTask.PROPERTY_NUM_SLOTS);
        getWorkPoolSupervision().foundMiningSite(info);
    }

}
