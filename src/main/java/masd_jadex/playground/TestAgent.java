package masd_jadex.playground;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.features.IExternalBDIAgentFeature;
import jadex.bdiv3.runtime.IPlan;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.threadpool.IThreadPoolService;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import masd_jadex.cleanerworld.CleanerBDIAgentC1;

@Agent(type= BDIAgentFactory.TYPE)
@RequiredServices(@RequiredService(name="baloo", type=IThreadPoolService.class, scope=ServiceScope.PLATFORM))
public class TestAgent {
    @Belief
    boolean fulfilled;

    @Belief
    boolean isFulfilled(){
        return fulfilled;
    }

    @AgentFeature
    protected IRequiredServicesFeature services;

    @OnStart
    private void OnStart(IBDIAgentFeature bdi)
    {
        Goal1 g = new Goal1();
        bdi.dispatchTopLevelGoal(g);
        bdi.dispatchTopLevelGoal(new Goal2());

        fulfilled = false;

    }

    @Goal(deliberation=@Deliberation(inhibits=Goal2.class))
    //@Goal(recur=true, recurdelay=3000,deliberation=@Deliberation(inhibits= CleanerBDIAgentC1.PerformPatrol.class))    // Pause patrol goal while loading battery
    public class Goal1
    {
        //@GoalInhibit()
        @GoalTargetCondition()
        boolean isfulfilled()
        {
            return fulfilled;
        }
    }

    @Plan(trigger=@Trigger(goals=Goal1.class))
    private IFuture<Void> Plan1 (IPlan plan)
    {
        Future<Void> result = new Future<Void>();

        IThreadPoolService tps = services.getLocalService("baloo");
        tps.execute(()->{
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fulfilled = true;
            result.setResult((Void)null);
        });
        return result;
    }

    @Goal(deliberation=@Deliberation(inhibits=TestAgent.Goal1.class))
    class Goal2
    {

    }
}
