package masd_jadex.cleanerworld;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.*;
import jadex.micro.annotation.*;
import jadex.quickstart.cleanerworld.environment.*;
import jadex.quickstart.cleanerworld.gui.*;

/**
 *  BDI agent template.
 */
@Agent(type="bdi")    // This annotation makes the Java class and agent and enabled BDI features
public class CleanerBDIAgentA3
{
    //-------- fields holding agent data --------

    /** The sensor/actuator object gives access to the environment of the cleaner robot. */
    private SensorActuator    actsense    = new SensorActuator();


    /**
     *  The body is executed when the agent is started.
     *  @param bdifeature    Provides access to bdi specific methods
     */
    @AgentBody    // This annotation informs the Jadex platform to call this method once the agent is started
    private void    exampleBehavior(IBDIAgentFeature bdi)
    {
        // Open a window showing the agent's perceptions
        new SensorGui(actsense).setVisible(true);

        //... add more setup code here
        //actsense.moveTo(Math.random(), Math.random());    // Dummy call so that the cleaner moves a little.
        
        // Create and dispatch a goal.
        bdi.dispatchTopLevelGoal(new PerformPatrol());
    }

    //-------- inner classes that represent agent goals --------

    /**
     *  A goal to patrol around in the museum.
     */
    @Goal(recur=true, orsuccess=false)    // The goal annotation allows instances of a Java class to be dispatched as goals of the agent
    //If recurrent is true it will start over when a patrol round is finished. The goal processing restarts after all available plans have been executed for the goal
    class PerformPatrol {}

    //-------- methods that represent plans (i.e. predefined recipes for working on certain goals) --------

    /**
     *  Declare a plan for the PerformPatrol goal by using a method with @Plan and @Trigger annotation.
     */
    @Plan(trigger=@Trigger(goals=PerformPatrol.class))    // The plan annotation makes a method or class a plan. The trigger states, when the plan should considered for execution.
    private void    performPatrolPlan()
    {
        // Follow a simple path around the four corners of the museum and back to the first corner.
        System.out.println("Starting performPatrolPlan()");
        actsense.moveTo(0.1, 0.1);
        actsense.moveTo(0.1, 0.9);
        actsense.moveTo(0.9, 0.9);
        actsense.moveTo(0.9, 0.1);
        actsense.moveTo(0.1, 0.1);
    }
    
    /**
     *  Declare a second plan for the PerformPatrol goal.
     */
    @Plan(trigger=@Trigger(goals=PerformPatrol.class))
    private void    performPatrolPlan2()
    {
        // Follow another path around the middle of the museum.
        System.out.println("Starting performPatrolPlan2()");

        // Fill in moveTo() commands, e.g. according to the figure
        actsense.moveTo(0.3, 0.3);
        actsense.moveTo(0.3, 0.7);
        actsense.moveTo(0.7, 0.7);
        actsense.moveTo(0.7, 0.3);
        actsense.moveTo(0.3, 0.3);
    }

    /**
     *  Declare a third plan for the PerformPatrol goal.
     */
    @Plan(trigger=@Trigger(goals=PerformPatrol.class))
    private void    performPatrolPlan3()
    {
        // Follow another path around the middle of the museum.
        System.out.println("Starting performPatrolPlan3()");

        // Fill in moveTo() commands, e.g. according to the figure
        actsense.moveTo(0.3, 0.3);        
        actsense.moveTo(0.7, 0.7);
        actsense.moveTo(0.3, 0.7);
        actsense.moveTo(0.7, 0.3);
        actsense.moveTo(0.3, 0.3);
    }


    //... BDI goals and plans will be added here
}