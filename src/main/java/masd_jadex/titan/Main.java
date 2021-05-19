package masd_jadex.titan;

import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;

import java.util.logging.Level;


public class Main {
    public static void main(String[] args) {
        IPlatformConfiguration conf = PlatformConfigurationHandler.getDefaultNoGui();

        //conf.setGui(true);

        // Set logging level to provider better debugging output for agents.
        conf.setLoggingLevel(Level.WARNING);
        IExternalAccess platform = Starter.createPlatform(conf).get();
        CreationInfo ci = new CreationInfo().setFilename("/masd_jadex/titan/titan.application.xml");
        platform.createComponent(ci).get();
    }
}
