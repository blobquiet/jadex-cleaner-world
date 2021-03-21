package masd_jadex.cleanerworld;

import jadex.bdiv3.features.*;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.*;
import jadex.quickstart.cleanerworld.environment.*;
import jadex.quickstart.cleanerworld.gui.*;

/**
 *  BDI agent template.
 */
@Agent(type="bdi")    // This annotation makes the Java class and agent and enabled BDI features
public class CleanerBDIAgentA0
{
    //-------- fields holding agent data --------

    /** The sensor/actuator object gives access to the environment of the cleaner robot. */
    private SensorActuator    actsense    = new SensorActuator();


    /**
     *  The body is executed when the agent is started.
     *  @param bdi    Provides access to bdi specific methods
     */
    @OnStart    // This annotation informs the Jadex platform to call this method once the agent is started
    private void    exampleBehavior(IBDIAgentFeature bdi)
    {
        // Open a window showing the agent's perceptions
        new SensorGui(actsense).setVisible(true);

        //... add more setup code here
        actsense.moveTo(Math.random(), Math.random());    // Dummy call so that the cleaner moves a little.
    }

    //-------- additional BDI agent code --------

    //... BDI goals and plans will be added here
}