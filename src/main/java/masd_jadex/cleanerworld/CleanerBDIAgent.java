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
public class CleanerBDIAgent
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
    /** Set of the known waste items. Managed by SensorActuator object. */
    @Belief
    private Set<IWaste>    wastes    = new LinkedHashSet<>();
    /** Set of the known waste items. Managed by SensorActuator object. */
    @Belief
    private Set<IWastebin>    wastebins    = new LinkedHashSet<>();

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
        
        // Tell the sensor to update the belief sets
        actsense.manageWastesIn(wastes);
        
        // Tell the sensor to update the belief sets
        actsense.manageWastebinsIn(wastebins);

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
    @Goal(orsuccess=false, excludemode=ExcludeMode.WhenFailed, randomselection=true, retrydelay=2000)
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
    //@Goal(deliberation=@Deliberation(inhibits=PerformPatrol.class))
    @Goal(excludemode=ExcludeMode.Never)
    class QueryChargingStation
    {
        // Remember the station when found
        IChargingstation    station;

        // Check if there is a station in the beliefs
        @GoalTargetCondition
        boolean isStationKnown()
        {
            station    = stations.isEmpty() ? null : stations.iterator().next();
            /*
            if (station!=null) {
            	System.out.println("My battery is low, but I know a station! Actually the one that comes to mind is the first one I've seen: "+ stations.iterator().next().getId());
            }
            else System.out.println("My battery is still low, but don't know any station yet!");*/
            return station!=null;
        }
    }
    
    /**
     *  A goal to drop the waste.
     */
    @Goal(recur=true, recurdelay=7000,deliberation=@Deliberation(inhibits=PerformPatrol.class))    // Pause patrol goal while loading battery
    class AchieveDropWaste
    {
    	@GoalMaintainCondition    // The cleaner aims to maintain the following expression, i.e. act to restore the condition, whenever it changes to false.
        boolean isCarried()
        {
            return self.getCarriedWaste()!=null;
        }
        @GoalTargetCondition    // Only stop, when this condition is true
        boolean isDropped()
        {
            return self.getCarriedWaste()==null;
        }
    }
    
    /**
	 *  
	 */
	@Goal(excludemode=ExcludeMode.Never)
	public class QueryWastebin
	{
        // Remember the wastebins
        IWastebin    wastebin;

        // Check if there is a wastebin in the beliefs
        @GoalTargetCondition
        boolean isWastebinKnown()
        {
        	wastebin = wastebins.isEmpty() ? null : wastebins.iterator().next();
            return wastebin!=null;
        }
    }
    
    
    
    /**
     *  A goal to cleanup waste.
     */
    @Goal(recur=true, recurdelay=7000,deliberation=@Deliberation(inhibits=PerformPatrol.class))    // Pause patrol goal while loading battery
    class AchieveCleanupWaste
    {
        // Remember the waste item to clean up
        IWaste    waste;

        // Create a new goal instance for each new waste item
        @GoalCreationCondition(factadded="wastes")
        public AchieveCleanupWaste(IWaste waste)
        {
            System.out.println("Created achieve cleanup goal for "+waste);
            this.waste = waste;
        }

    }
    
    
    

    //-------- methods that represent plans (i.e. predefined recipes for working on certain goals) --------
    
    @Plan(trigger=@Trigger(goals=AchieveCleanupWaste.class))
    private void pickUpWaste(IPlan plan)
    {
    	System.out.println("Starting pickUpWaste() plan");
    	IWaste    waste    = wastes.iterator().next();
    	actsense.moveTo(waste.getLocation());
    	actsense.pickUpWaste(waste);
    	// Dispatch a subgoal to find a wastebin
    	QueryWastebin    querygoal    = new QueryWastebin();
        plan.dispatchSubgoal(querygoal).get();  //we wait synchronously by blocking the plan until the subgoal completes by using the get() method of the future.
    	
        IWastebin    wastebin    = wastebins.iterator().next();
    	actsense.moveTo(wastebin.getLocation());
    	actsense.dropWasteInWastebin(waste, wastebin);
    }
    
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
        //System.out.println("I procceed to move to the station");        
        actsense.moveTo(chargingstation.getLocation());
        //System.out.println("I procceed to recharge the battery up to 90%");
    	actsense.recharge(chargingstation, 1.0);
    	
    }
    
    /**
     *  Declare a plan for the QueryChargingStation goal by using a method with @Plan and @Trigger annotation.
     */
    @Plan(trigger=@Trigger(goals= {QueryChargingStation.class,QueryWastebin.class}))    // The plan annotation makes a method or class a plan. The trigger states, when the plan should considered for execution.
    private void    MoveAround()
    {
    	// This will only trigger if the the QueryChargingStation is not succeed, that happens only when there's no stations in the belief-set 
    	actsense.moveTo(Math.random(), Math.random());
    }
       

    /**
     *  Declare a plan for the PerformPatrol goal by using a method with @Plan and @Trigger annotation.
     */
    @Plan(trigger=@Trigger(goals=PerformPatrol.class))    // The plan annotation makes a method or class a plan. The trigger states, when the plan should considered for execution.
    private void    performPatrolPlan()
    {
        // Follow a simple path around the four corners of the museum and back to the first corner.
        System.out.println("Starting performPatrolPlan()");
        
        /*
        actsense.moveTo(0.1, 0.1);
        actsense.moveTo(0.1, 0.8);
        actsense.moveTo(0.8, 0.8);
        actsense.moveTo(0.8, 0.1);
        actsense.moveTo(0.1, 0.1);*/
        
        
        actsense.moveTo(0.5, 0.5);
        actsense.moveTo(0.5, 0.6);
        actsense.moveTo(0.6, 0.6);
        actsense.moveTo(0.6, 0.5);
        actsense.moveTo(0.5, 0.5);
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