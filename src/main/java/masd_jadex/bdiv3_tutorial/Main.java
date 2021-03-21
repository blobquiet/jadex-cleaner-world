package masd_jadex.bdiv3_tutorial;

import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.base.IPlatformConfiguration;
import jadex.bridge.IExternalAccess;


public class Main {
    public static void main(String[] args) {
        IPlatformConfiguration config = PlatformConfigurationHandler.getDefault();
        config.setGui(true);
        config.addComponent("masd_jadex.bdiv3_tutorial.c4.ClockBDI.class");

        IExternalAccess platform = Starter.createPlatform(config).get();
    }
}
