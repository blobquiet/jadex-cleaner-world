package quickstart.cleanerworld;

import java.util.*;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.*;
import jadex.bdiv3.runtime.*;
import jadex.bdiv3.model.MProcessableElement.ExcludeMode;
import jadex.micro.annotation.*;
import jadex.quickstart.cleanerworld.environment.*;
import jadex.quickstart.cleanerworld.gui.*;

/**
 *  BDI agent template.
 */
@Agent(type="bdi")    // This annotation makes the Java class and agent and enabled BDI features
public class CleanerBDIAgentC1
{
    //-------- fields holding agent data --------

    /** The sensor/actuator object gives access to the environment of the cleaner robot. */
    private SensorActuator    actsense    = new SensorActuator();
    
    /** Knowledge of the cleaner about itself (e.g. location and charge state). */
    @Belief
    private ICleaner    self    = actsense.getSelf();
    /** Set of the known charging stations. Managed by SensorActuator object. */
    @Belief
    private Set<IChargingstation>    stations    = new LinkedHashSet<>();

    /**
     *  The body is executed when the agent is started.
     *  @param bdifeature    Provides access to bdi specific methods
     */
    @AgentBody    // This annotation informs the Jadex platform to call this method once the agent is started
    private void    exampleBehavior(IBDIAgentFeature bdi)
    {
        // Open a window showing the agent's perceptions
        new SensorGui(actsense).setVisible(true);
        
        // Tell the sensor to update the belief sets
        actsense.manageChargingstationsIn(stations);

        //... add more setup code here
        //actsense.moveTo(Math.random(), Math.random());    // Dummy call so that the cleaner moves a little.
        
        // Create and dispatch agent goals.
        bdi.dispatchTopLevelGoal(new MaintainBatteryLoaded());
        bdi.dispatchTopLevelGoal(new PerformPatrol());        
    }

    //-------- inner classes that represent agent goals --------

    /**
     *  A goal to patrol around in the museum.
     */
    @Goal(orsuccess=false, excludemode=ExcludeMode.WhenFailed, randomselection=true, retrydelay=3000)
    //@Goal(recur=true, randomselection=true, retrydelay=3000)      // The goal annotation allows instances of a Java class to be dispatched as goals of the agent
    //If recurrent is true it will start over when a patrol round is finished. The goal processing restarts after all available plans have been executed for the goal
    class PerformPatrol {}
    
    /**
     *  A goal to recharge whenever the battery is low.
     */
    @Goal(recur=true, recurdelay=3000,deliberation=@Deliberation(inhibits=PerformPatrol.class))    // Pause patrol goal while loading battery
    //@Goal(recur=true, recurdelay=3000)
    class MaintainBatteryLoaded
    {
        @GoalMaintainCondition    // The cleaner aims to maintain the following expression, i.e. act to restore the condition, whenever it changes to false.
        boolean isBatteryLoaded()
        {
            return self.getChargestate()>=0.2; // Everything is fine as long as the charge state is above 20%, otherwise the cleaner needs to recharge.
        }
        @GoalTargetCondition    // Only stop charging, when this condition is true
        boolean isBatteryFullyLoaded()
        {
            return self.getChargestate()>=0.9; // Charge until 90%
        }
    }
    
    /**
     *  A goal to know a charging station.
     */
    @Goal
    class QueryChargingStation
    {
        // Remember the station when found
        IChargingstation    station;

        // Check if there is a station in the beliefs
        @GoalTargetCondition
        boolean isStationKnown()
        {
            station    = stations.isEmpty() ? null : stations.iterator().next();
            if (station!=null) {
            	System.out.println("As my battery is low, I decide to purshue the MaintainBatteryLoaded goal\n"
            			+ "so inside I Dispatch a subgoal to find a charging station from the ones that I know\n"
            			+ "Oh, suddently I remember a station! Actually the first one i've seen which is: "+ stations.iterator().next().getId());
            }
            else {System.out.println("My battery is low, I don't know any station :( I'm gonna die. Help!");}
            return station!=null;
        }
    }

    //-------- methods that represent plans (i.e. predefined recipes for working on certain goals) --------
    
    /**
     *  Move to charging station and load battery.
     */
    @Plan(trigger=@Trigger(goals=MaintainBatteryLoaded.class))
    private void loadBattery(IPlan plan)
    {
        System.out.println("Starting loadBattery() plan");

        // Move to first known charging station -> fails when no charging station known.
        //IChargingstation    chargingstation    = actsense.getChargingstations().iterator().next();
        //IChargingstation    chargingstation    = stations.iterator().next();
        
        // Print class of stations object to show that the LinkedHashSet has been wrapped.
        //System.out.println("Class of the belief set is: "+stations.getClass());
        
        // Dispatch a subgoal to find a charging station (from Exercise C1)
        QueryChargingStation    querygoal    = new QueryChargingStation();
        plan.dispatchSubgoal(querygoal).get();  //we wait synchronously by blocking the plan until the subgoal completes by using the get() method of the future.
        IChargingstation    chargingstation    = querygoal.station;
        System.out.println("Now that I remember, I can keep going with the loadBattery plan to MaintainBatteryLoaded until it reaches 90%\n"
        		+ "I'll then move to the "+ chargingstation.getId() +" and recharge");
        
        actsense.moveTo(chargingstation.getLocation());
        // Load until 100% (never reached, but plan is aborted when goal succeeds).
        actsense.recharge(chargingstation, 1.0);
    }
       

    /**
     *  Declare a plan for the PerformPatrol goal by using a method with @Plan and @Trigger annotation.
     */
    @Plan(trigger=@Trigger(goals=PerformPatrol.class))    // The plan annotation makes a method or class a plan. The trigger states, when the plan should considered for execution.
    private void    performPatrolPlan()
    {
        // Follow a simple path around the four corners of the museum and back to the first corner.
        System.out.println("Starting performPatrolPlan()");
        actsense.moveTo(0.1, 0.1);
        actsense.moveTo(0.1, 0.8);
        actsense.moveTo(0.8, 0.8);
        actsense.moveTo(0.8, 0.1);
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