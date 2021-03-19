package hello;

import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.OnStart;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;

@Agent
public class HelloAgent {
    /**
     *  The agent says hello every five seconds.
     */
    @OnStart
    void hello(IInternalAccess me) {
        me.repeatStep(0, 5000, dummy -> {
            System.out.println("Hello, Jadex!");
            return IFuture.DONE;
        });
    }

    /**
     *  Start Jadex platform with the hello agent.
     */
    public static void main(String[] args) {
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.addComponent(HelloAgent.class);
        Starter.createPlatform(config).get();
    }
}