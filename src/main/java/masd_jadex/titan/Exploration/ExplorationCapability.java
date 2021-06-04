package masd_jadex.titan.Exploration;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.runtime.ICapability;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.component.IMessageHandler;
import jadex.bridge.component.IMsgHeader;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.types.security.ISecurityInfo;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import masd_jadex.titan.work_pool_supervision.IWorkPoolSupervision;

@Capability
@RequiredServices(@RequiredService(type= IWorkPoolSupervision.class))
@Plans({
        @Plan(trigger=@Trigger(goals={ExploreGoal.class,}), body=@Body(ExplorePlan.class)),
})
public class ExplorationCapability {

    @Agent
    protected ICapability capability;

    Object exploreGoal = null;

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
        IWorkPoolSupervision supervisor = (IWorkPoolSupervision)capability.getAgent().getFeature(IRequiredServicesFeature.class).getService(IWorkPoolSupervision.class).get();
        supervisor.subscribeScout(capability.getAgent().getId());
    }

    void startExplorationMsg() {
        exploreGoal = new ExploreGoal();
        capability.getAgent().getFeature(IBDIAgentFeature.class).dispatchTopLevelGoal(exploreGoal);
    }

    void stopExplorationMsg() {
        capability.getAgent().getFeature(IBDIAgentFeature.class).dropGoal(exploreGoal);
    }
}
