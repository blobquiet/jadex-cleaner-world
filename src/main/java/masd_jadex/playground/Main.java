package masd_jadex.playground;

import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.extension.envsupport.*;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClock;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.threadpool.IThreadPoolService;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.quickstart.cleanerworld.gui.EnvironmentGui;

import javax.swing.*;
import java.util.logging.Level;


public class Main {
    public static void main(String[] args) {
        // Start from minimal configuration
        IPlatformConfiguration	conf	= PlatformConfigurationHandler.getMinimal();
        conf.setGui(true);

        // Set logging level to provider better debugging output for agents.
        conf.setLoggingLevel(Level.WARNING);
        // Add BDI kernel (required when running BDI agents)
        conf.setValue("kernel_bdi", true);

        // Add your cleaner agent(s)
        conf.addComponent("masd_jadex/playground/TestAgent.class");

        IExternalAccess acc = Starter.createPlatform(conf).get();
    }
}
