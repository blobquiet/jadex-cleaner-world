1. Things you should analyze:


A. Elements in the language/formalism
\* Describe which are the main abstractions that the language provides to define the agent behaviour? (eg: propositions, actions, events, intentions, goals, tasks, plans ....)

B. Operational Semantics (how the language works)
\* Describe the steps of the Reasoning cycle (Deliberation, Means-end Reasoning... )

\* Answer the following questions:
 \- Is there any Belief revision?
 \- Is there any Goal/Intention revision?
 \- Is there any Plan/Task revision?
 \- Is there any special events' handling (i.e. emergency handling)? Is there any way to make agents react to sudden changes in the environment?
 \- Is there any Meta-Level reasoning? (reasoning over the reasioning cycle).

C. Formal Semantics (theoretical model)
\* Is there any theoretical model connected to the programming language?
\* Which are their elements? (eg, propositions, actions, events, tasks, plans ....) Are those the same as in the implementation?

D. Connecting with the environment:
\* Is there any agent platform?
\* Do messages follow FIPA specifications?
\* Which component manages the message passing between agents?
\* Can ontologies be included in messages?

E. Tools
\* Are there any available tools?

F. Execution examples
\* Download the software from the web and test it.
\* Search for examples about implemented agents (eg: the examples coming with the language) or, if you prefer, do your own tiny examples.

G. (Optional) Connecting with other alternatives
\* If you find any information about the origins for the formalism/language, if you find comparisons already made with other alternatives, you can include them.

------

**The programming of active components** is based on general features that all active components share (black box view) and those features that are specific for a concrete type of active:

- BDI (belief-desire-intention) Agent:

  is a well-know agent architecture that facilitates describing behavior with goals, plans and beliefs. The idea is to clearly distinguish between what is to achieved (goals) and how it is achieved (plans).

  - This separation helps for different reasons

    -  On the one hand, it helps to design complex behaviors in an understandable way and 
    - on the other hand the behavior of the agent becomes more understandable as its current (and past) goal and plan executions can be seen. 

    Despite, it may sound unfamiliar using mentalistic concepts like goals and plans for programming, the programming is straight forward and relies on XML and Java only, i.e. no new programming language has to be learned.

  - Since BDI V3 it is possible to program BDI agents completely in Java using annotations to specify beliefs, goals and plans. Please note, that we strongly recommend using BDI V3. It is much easier to program and faster than V2.

- BPMN workflow

##### Jadex BDIV3

is an agent-oriented reasoning engine for writing rational agents in Java. 

Jadex represents a conservative approach towards agent-orientation for several reasons. 

- One main aspect is that no new programming language is introduced. Instead, Jadex agents can be programmed in the state-of-the art object-oriented integrated development environments (IDEs) such as [eclipse](http://www.eclipse.org/). 
- The other important aspect concerns the middleware independence of Jadex. As Jadex BDIV3 is loosely coupled with its underlying middleware, Jadex can be used in very different scenarios on top of agent platforms as well as enterprise systems such as J2EE.

Similar to the paradigm shift towards object-orientation **agents represent a new conceptual level of abstraction extending well-known and accepted object-oriented practices**. Agent-oriented programs add the explicit concept of autonomous actors to the world of passive objects.

In this respect **agents represent active components** with individual reasoning capabilities.

- This means that agents can exhibit reactive behavior (responding to external events) 
- as well as pro-active behavior (motivated by the agents own goals).

The name indicates Jadex BDIV3 it is the third version of the Jadex BDI kernel.

- **The V1** kernel version was based on XML and Java and introduced an goal-oriented reasoning mechanism embraces the full BDI reasoning cycle including the selection of goals to pursue (goal deliberation) and the realization phase in which different plans can be tried out to achieve a goal.
- In **BDI kernel V2** the programming model was kept the same but the engine itself was completely rebuilt based on a RETE rule engine operating on BDI rules.
- Finally, **in V3** the main objective was to create a new programming model that allows fast prototyping and hides as much of the framework as possible. Thus, in the new **V3 kernel BDI agents are written in Java** only (no XMLs any more) and annotations are used to designate BDI elements.
  - Another important aspect is the much stronger integration of **BDI and object oriented concepts** in the new kernel, i.e. **it becomes much simpler to program BDI agent having a solid background on object-oriented concepts** (supporting e.g. inheritance, POJO programming, dependency injection).

#### BDI model of jadex	 

To realize rational agents, numerous deliberative agent architectures exist (e.g. BDI [Bratman 87](https://www.press.uchicago.edu/ucp/books/book/distributed/I/bo3629095.html)), 

Regarding the theoretical foundation and the number of implemented and successfully applied systems, the most interesting and widespread agent architecture is the Belief-Desire-Intention BDI architecture, introduced by Bratman as a philosophical model for describing rational agents [Bratman 87](https://www.press.uchicago.edu/ucp/books/book/distributed/I/bo3629095.html).

**It consists of the concepts of *belief*, *desire*, and *intention* as mental attitudes, that generate human action.** Beliefs capture *informational* attitudes, desires *motivational* attitudes, and intentions *deliberative* attitudes of agents. [Rao and Georgeff 95](https://www.aaai.org/Library/ICMAS/1995/icmas95-042.php) have adopted this model and transformed it into a formal theory and an execution model for software agents, based on the notion of beliefs, goals, and plans.

**Jadex** facilitates using the BDI model in the context mainstream programming, **by introducing beliefs, goals and plans as first class objects,** that can be created and manipulated inside the agent. In Jadex, agents have beliefs, which can be any kind of Java object and are stored in a belief base. Goals represent the concrete motivations (e.g. states to be achieved) that influence an agent's behavior. To achieve its goals the agent executes plans, which are procedural recipes coded in Java. The abstract architecture of a Jadex agent is depicted in the following Figure 1.

![jadexabstractarchitecture.png](https://github.com/actoron/jadex/blob/master/docs/guides/bdiv3/jadexabstractarchitecture.png?raw=true)

#### Questions:

How a model so old can still has its validity in the new times?

### Resources

Examples https://download.actoron.com/docs/releases/latest/jadex-mkdocs/guides/bdiv3/01%20Introduction/

Guides https://github.com/actoron/jadex/blob/master/docs/guides/bdiv3/01%20Introduction.md

https://github.com/actoron/jadex/tree/master/applications/bdiv3/src/main/java/jadex/bdiv3

The main concepts of Jadex are beliefs, goals and plans. and it introduces them as class objects

Agents  The first which is means-end reasoning is about the agent reacting to incoming messages, internal events and goals by select-ing and executing plans. The second is about the agent continuouslydeliberating its current goals, to then decide from a subset of goals,the ones that need to be pursued 

------

http://jasss.soc.surrey.ac.uk/18/1/11.html

DI Agent System ([Braubach & Pokahr 2013](http://jasss.soc.surrey.ac.uk/18/1/11.html#braubach2013)) follows the Belief Desire Intention (BDI) model and facilitates easy intelligent agent construction with sound software engineering foundations. It allows for programming intelligent software agents in XML and Java. The Jadex research project is conducted by the Distributed Systems and Information Systems Group at the University of Hamburg. The developed software framework is available under GNUs LGPL license, and is continuously evolving. Jadex has been put into practice in the context of several research, teaching, and industrial application scenarios some of which are described in its website. It has been used to build applications in different domains such as simulation, scheduling, and mobile computing. For example, Jadex was used to develop a multi-agent application for negotiation of treatment schedules in hospitals ([Braubach et al. 2014](http://jasss.soc.surrey.ac.uk/18/1/11.html#braubach2014)). 

http://pdfs.semanticscholar.org/ad7f/55a680728ee0eecfb73afda72140a75046cd.pdf

------

http://jasss.soc.surrey.ac.uk/18/1/11.html

https://download.actoron.com/docs/releases/jadex-0.94x/examples/jadex/examples/cleanerworld/multi/cleanermobile/Cleaner.agent.html



https://download.actoron.com/docs/releases/latest/jadex-mkdocs/guides/ac/01%20Introduction/

https://download.actoron.com/docs/releases/latest/jadex-mkdocs/guides/ac/02%20Active%20Components/

https://www.activecomponents.org/#/docs/overview

https://download.actoron.com/docs/releases/latest/jadex-mkdocs/getting-started/getting-started/

https://download.actoron.com/docs/releases/latest/jadex-mkdocs/tutorials/bdiv3/01%20Introduction/

https://github.com/actoron/jadex/blob/master/docs/guides/bdiv3/01%20Introduction.md



*The Jadex platform provides the basic services for running components. In addition, the platform exposes several configuration options as arguments. Further platform start parameters,*

- ### XML Components

- ### Micro Agents

- ### BDI Agents:

  - BDI (belief-desire-intention) is a well-know agent architecture that facilitates describing behavior with goals, plans and beliefs. The idea is to clearly distinguish between what is to achieved (goals) and how it is achieved (plans). This separation helps for different reasons. On the one hand, it helps to design complex behaviors in an understandable way and on the other hand the behavior of the agent becomes more understandable as its current (and past) goal and plan executions can be seen. Despite, it may sound unfamiliar using mentalistic concepts like goals and plans for programming, the programming is straight forward and relies on XML and Java only, i.e. no new programming language has to be learned. Moreover, since BDI V3 it is possible to program BDI agents completely in Java using annotations to specify beliefs, goals and plans. Please note, that we strongly recommend using BDI V3. It is much easier to program and faster than V2.

- ### BPMN Workflows

- ### GPMN Workflows

- ### Virtual Environments



## Building and Extending Jadex

Jadex is a modular software system that relies on the Maven build and deployment system. Extending Jadex is possible in many different areas, e.g. it is easily possible to add new types of active components by implementing a new kernel or create a new JCC plugin.

## Supported Runtime Environments

Jadex is also available as Android version, so that the platform can be run on mobile phones and tables.



------

Built upon the foundations of software engineering it provides a natural abstraction layer for developing agent oriented systems 

A Java based software framework designed for the creation of goal oriented agents following the BDI architectural model. JADEX itself is a reasoning engine developed to simplify the development of adaptive agents for traditional software engineers. 

------

### Concepts

Rationality means that the agent will always perform the most promising actions (based on the knowledge about itself and the world)  o achieve its objectives

- In Jadex, agents have **beliefs**, which can be any kind of Java object and are stored in a beliefbase.
- **Goals** represent the concrete motivations (e.g. states to be achieved) that influence an agent's behavior.
- To achieve its goals the agent executes **plans**, which are procedural recipes coded in Java.
- beliefs, goals and plans.

Reasoning in Jadex is a process consisting of two interleaved components. On the one hand, the agent reacts to incoming messages, internal events and goals by **selecting and executing plans** (**means-end reasoning**)

On the other hand, the agent continuously **deliberates** about its **current goals**, **to decide about a consistent subset,** which should be pursued.



The main concepts of Jadex are beliefs, goals and plans. The beliefs, goals and plans of the agent are defined by the programmer and prescribe the behavior of the agent. 

###### Using Beliefs  https://download.actoron.com/docs/releases/latest/jadex-mkdocs/tutorials/bdiv3/04%20Using%20Beliefs/





------

##### Relative to the examples:

## the Cleanerworld Environment

https://github.com/actoron/jadex/tree/master/docs/tutorials/quickstart-bdi

The `Main` class starts a Jadex platform with an initial agent and also opens a GUI of the cleanerworld. You can use the mouse to place or remove *waste* objects directly in the environment view

In the `Main` class, the agent is started with the line:

```java
        conf.addComponent("quickstart/cleanerworld/SimpleCleanerAgent.class");
```

You can change this to start your own agents and/or duplicate the line to start multiple agents at once. Further, you can use the `CLOCK_SPEED` setting to change the progress of time in the environment and thus make the cleaner move faster or slower:

```java
    /** Use higher values (e.g. 2.0) for faster cleaner movement and lower values (e.g. 0.5) for slower movement. */
    protected static double    CLOCK_SPEED    = 1;
```

The project contains a `SimpleCleanerAgent` that moves around in the Cleanerworld. It uses the `SensorActuator` object that is available in the example project for accessing the environment. Each agent has its local view of the environment, which is based on a limited vision range as indicated by the semi-transparent circle around the cleaner:

![img](https://github.com/actoron/jadex/raw/master/docs/tutorials/quickstart-bdi/view-cleaner.png)

The sensor / actuator gives access to the perceived environment and provides operations to manipulate the environment. Each cleaner agent should create its own sensor/actuator. t remembers seen objects and also notices their disappearence when in vision range.

Try placing/removing waste around the moving cleaner to understand the difference between the global world view and the cleaners local (**incomplete** and possibly incorrect) knowledge.



# Goals and Plans

This exercise introduces the BDI model and shows how to generate agent actions by using the goal and plan concepts of Jadex. Specifically, you will learn how to add goals and plans to an agent and how to control the plan selection and plan execution processes with goal flags.

# Quick Introduction to the BDI Model

The belief-desire-intention (BDI) model of agency is based on Stanford philosopher [Michael E. Bratman](https://philosophy.stanford.edu/people/michael-e-bratman)'s seminal work [Intention, Plans, and Practical Reason](https://www.press.uchicago.edu/ucp/books/book/distributed/I/bo3629095.html). An (overly simplified) description of one main idea is that humans are resource-bounded agents in the sense that they don't calculate the utility of every possible course of action in every second of their existence

Instead, it is rational for agents, to stick to a once-chosen plan, without questioning its actions in every step. Only when a problem occurs with a chosen plan, the agent will rethink its choice and maybe form another plan.

The so called *means-end reasoning* process is at the heart of this model. For every desire, the agent will form intentions how to satisfy it. The selection of suitable desires is in turn based on the current beliefs of an agent. 

In **software implementations of the BDI model (such as Jadex), desires and intentions are replaced by more concrete notions of goals and plans.** 

- Goals can be stated, e.g., as a boolean expression that represents a world state to be achieved.

- Plans are procedural recipes of actions (e.g. code to be executed for achieving a goal).

-  Instead of abstract desires, a software agent in Jadex has a dynamic set of concrete goals to be pursued. 

- Instead of forming arbitrary intentions, a Jadex agent selects existing plans from its so called *plan library* in response to the currently active goals.

- The means-end reasoning process is then the decision logic for finding an appropriate plan (*means*) for a given goal (*end*).

### `@Agent` Annotation

###### **BDI agent (`type="bdi"`). The second part is necessary to enable BDI features, such as the automatic processing of goals and selection and execution of plans.**



### Agent Setup

Unlike the simple cleaner, we stored the `actsense` object in a field instead of a local variable. The reason is that we will add more methods and inner classes later for goals and plans and want to access the `actsense` object from all of these.

`IBDIAgentFeature bdi` parameter of the method. It provides access to the BDI features of Jadex. We will use it later, e.g. to add goals to the agent.

### The `@Goal` Annotation and the `PerformPatrol`

the `PerformPatrol` class is only a template for a goal, just like any Java class is only a template for an object. We need to instatiate the goal class and tell the agent to pursue this newly created goal object. 

the `PerformPatrol` class to represent the goal object. Having Java classes for goals allows treating them as instances like any other object in Java. Thus, goals can be created, passed to the agent for processing, and the agent can keep track of its goals as a collection of objects.

### The `@Plan` Annotation and the `performPatrolPlan()` Method

According to the BDI model as implemented in Jadex, the agent should pursue its goals by selecting appropriate plans from its *plan library*. Plans are procedural recipes that can be naturally specified as simple Java methods. To add a method to the plan library you have to tell Jadex to treat this method as a plan. This is done by the `@Plan` annotation.

In addition to marking a method with `@Plan` you also need to tell Jadex the situations, when this plan should be selected. Most prominently, you have to state some triggering event or goal for which the agent should consider the method as a suitable plan

### The `@Trigger` Annotation

So we need to tell the agent to consider the `performPatrolPlan()` method as an applicable plan for the `PerformPatrol` goal

### The `IBDIAgentFeature` and the `dispatchTopLevelGoal()`Method

The `IBDIAgentFeature` class provides methods to access the BDI functionality of a Jadex agent.  We can use it to add a goal to the agent with the `dispatchTopLevelGoal()` method. All we need is an object to represent the goal.

## Exercise A2: Execute the Goal Periodically

1. We create the goal and add it with `dispatchTopLevelGoal()`.
2. The Jadex framework selects the `exampleBehavior()` as a suitable plan and executes the method.
3. Our plan implementation in that method causes the agent to move to the specified locations on after another and finally the plan method returns.
4. The Jadex framework notices that the plan is finished and considers the goal processing to be complete.
5. In Exercise A1 the goal would now be dropped because processing is finished. Due to the `recur=true` flag, in this exercise, the goal processing restarts at step 2.

## Multiple Plans for a Goal

One advantage of the BDI model is the clean separation between *what* an agent should achieve (goals) and *how* it can achieve it (plans).

Often, there are many different ways to achieve the same result. In BDI agents this is naturally reflected by the possibility to have many plans that all are suitable candidates for pursuing the same goal.

### The `orsuccess` Flag for Goals

One option for telling the agent to look for other plans after a plan is completed is stating that not only *one*, but *all* available plans should be executed. orsuccess flag that allows changing the processing semantics from *OR* (= only one of many plans needs to be executed) to *AND* (= all of the available plans need to be executed)

## Means-end Reasoning Flags

we already introduced one flag for controlling the means-end reasoning (a.k.a. goal processing, a.k.a. plan selection) the flags for 

```java
@Goal(recur=true, orsuccess=false, retrydelay=3000)    // Goal flags: variation 1
```

https://download.actoron.com/docs/javadoc/jadex-4.0.168/index.html?jadex/bdiv3/annotation/Goal.html

# Beliefs and Goal Conditions

 introduces beliefs that let an agent automatically perceive and react to changes. It also shows how to use goal conditions for controlling goal processing behavior based on belief values and belief changes.



The cleaner moved around for some time and when the charge state droppen below 20%, the cleaner tried to start the battery loading plan. The only problem was, that it was still performing a patrol round thus trying to move to two directions at once. This caused the above error. In the next exercise we will fix that.

### The `@GoalMaintainCondition` and Declarative Goals

`MaintainBatteryLoaded` class had a method `isBatteryLoaded()` to check, if there is still enough battery. The `@GoalMaintainCondition` tells Jadex to monitor the result of this method. 

```java
@GoalMaintainCondition    // The cleaner aims to maintain the following expression, i.e. act to restore the condition, whenever it changes to false.
        boolean isBatteryLoaded()
        {
            return self.getChargestate()>=0.2; // Everything is fine as long as the charge state is above 20%, otherwise the cleaner needs to recharge.
        }
```

Thanks to the maintain condition, the goal becomes ***declarative***, which means that the agent can check (with the boolean expression implemented in the method) if the goal is currently succeeded or not. 

This is quite different to the perform patrol goal, which is only ***procedural***, meaning that the agent will always execute plans for the perform patrol goal and always consider it succeeded after successful plan execution.

Declarative goals capture the desired state already in their specification and are thus more decoupled from their plans. E.g. when the condition is `true`, no plan needs to be executed, whereas a procedural goal will always lead to plan execution

Also, when the condition is `false` and stays `false` even after plan execution, the agent will look for other plans to pursue the goal, whereas a procedural goal is always considered to have succeeded after plans (i.e. procedures) are completed.



### The `@Belief` Annotation

So why did we need the `@Belief` annotation?  The Jadex framework does not check every goal condition for every step of every agent all the time (this would lead to really poor perfomance). Instead it waits for interesting events to happen and only then selectively checks the affected conditions of the affected goals of the affected agents. Thanks to this, you can run Jadex programs with a lot of agents that can have a lot of goals with a lot of conditions without the need for a supercomputer.



The drawback is, that Jadex needs to know for which events to look. Thankfully, Jadex analyzes your code and is able to deduce many events automatically as long as your condition code refers to fields marked as `@Belief`

Therefore having the annotation at the `self` field allows Jadex to see that the maintain condition, that also refers to this field, should be re-checked whenever the `self` belief value changes.

Actually the value of the `self` field does not change. The field holds a reference to an object of type `ICleaner`. The reference (`self` field) itself does not change, but an attribute of the referenced cleaner object. This change is published as a *bean property change*.



## Using Deliberation Settings for Managing Conflicting Goals

We somehow want to tell the cleaner to prioritize the latter and stop executing plans for the former until the battery is loaded again. This is actually quite simple with Jadex. Change the `@Goal` annotation of the `MaintainBatteryLoaded` class to the following code

### The `@Deliberation` Annotation and the `inhibits` Setting

deliberation settings that can be added to `@Goal` annotations. Deliberation means that the agent keeps track of its current goals and decides (i.e. *deliberates*) if some of them need to be suspended in favor of other more important goals

When the more important goals are completed, the agent can resume the previously suspended less important goals.

In Jadex, deliberation criteria can be specified by so called *inhibition arcs*, which can be seen as pointers from the more important goals to the less important goals. As long as such a more important goal is actively processed (i.e. plans are executed for the goal), all other goals connected by the inhibition arcs are *inhibited*, i.e. prevented from executing plans.

the maintain battery loaded goal now inhibits the perform patrol goal. As a result, any active patrol plan will be aborted as soon as the maintain battery loaded goal becomes active. Thus now the load battery plan should succeed, because the patrol plan cannot interfere anymore:

![img](https://github.com/actoron/jadex/raw/master/docs/tutorials/quickstart-bdi/deliberation-inhibits.svg)

​                                                                    *Inhibition arcs explained*

##  Separate Maintain and Target Conditions

As soon as it reaches 20% the goal condition is satisfied again. The maintain battery loaded goal is a declarative goal due to its maintain condition. Therefore the agent notices the condition to be true again and thus stops executing the `loadBattery()` method. Anyways, executing a plan when the goal is already achieved would be a waste of time, right?

Well, in general: yes. But in this case we want the cleaner to load the battery some more before resuming the patrol round

```java
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
```

# Plans with Subgoals

 A subgoal is a goal, that exists in the context of a plan. For the subgoal, the agent may again execute other plans, thus forming a hierarchy of goals and plans called the *goal plan tree*.

To achieve a goal, *only one* of the available plans needs to work (*OR* decomposition, e.g. "throw pancake" *or* "flip with spatula"). For a plan to complete, *all* subgoals of the plan need to be successful (*AND* decomposition, e.g. "pancake mix ready" *and* "pancake flipped").

### The `stations` Belief Set

The `LinkedHashSet` as well as other Java collection classes do not directly support monitoring their contents. For that reason, Jadex wraps the object into a collection that supports monitoring. Jadex can do this for lists (`java.util.List`), sets (`java.util.Set`) and maps (`java.util.Map`).

```java
 /** Set of the known charging stations. Managed by SensorActuator object. */
    @Belief
    private Set<IChargingstation>    stations    = new LinkedHashSet<>();
```

The `SensorActuator` object provides this method to simplify using belief sets for the perceptions from the environment 

Similar methods are available for waste, waste bins, and other cleaners. The sensor also updates the belief sets on the disappearance of objects: E.g. when the the cleaner is in vision range of a location where previously a waste object was detected, but the waste object is no longer there, the sensor will remove the waste object from the set that has been provided by the `manageWastesIn()` method.

## A Subgoal for Knowing Charging Stations

the `QueryChargingStation` also defines a field for remembering a charging station, once it has been found. As a goal is still just a Java class, you can use all the Java features you like, e.g. add fields and constructors, extend other classes or implement interfaces etc.

### The `IPlan` Parameter and the `dispatchSubgoal()` Method

ere we use the `dispatchSubgoal()` method to attach a newly created `QueryChargingStation` object as a subgoal to the plan.

By default, Jadex would process the subgoals of a plan in parallel to the plan itself. The result of the `dispatchSubgoal()` call is therefore a [future](https://www.activecomponents.org/forward.html?type=javadoc&path=/index.html?jadex/commons/future/IFuture.html) object that allows various ways of synchronously and asynchronously waiting for the subgoal being processed. Our load battery plan needs a charging station before it can continue, so we wait synchronously by blocking the plan until the subgoal completes by using the `get()` method of the future.

##  A Plan for Finding a Charging Station

- The agent performs patrol rounds as long as there is enough battery.

- When the battery is below the threshold in the maintain condition (i.e. the

   

  ```
  isBatteryLoaded()
  ```

   

  method), the agent will stop its patrol plan and

  - move to a charging station if known (as implemented in the `loadBattery()` plan method),
  - execute a plan to find a charging station if not known (in reaction to the subgoal posted in the `loadBattery()` plan method).

# Goals with Multiple Instances

handle goals using the concepts *type* and *instance* that you may know from object-orientation. In the previous exercises, our agents always used a single instance of each goal that was created at agent startup with

```java
bdi.dispatchTopLevelGoal(new <goalclass>())`, e.g. `bdi.dispatchTopLevelGoal(new PerformPatrol())
```

When considering the goal to pick up any known piece of waste, we can follow a different approach: instead of a single global goal to pick up *all* pieces of waste, we can create individual goal instances for each specific piece of waste.



This *one goal for one piece of waste* approach might look a bit more complex at first

Yet, it has an important advantage as it makes the choices of the agent more explicit.

 **If we would combine all waste pieces in a global *PickUpAllWaste* goal, we would implement the decision logic (which piece to pick up first?) into procedural plans. Having a separate goal instance for each piece of waste allows us to describe the choice using goal deliberation and keep our procedural plans simple.**

## A Goal Instance for each Piece of Waste





### Questions about Means-end Reasoning Flags

1. The recur delay only applies after all plans have been executed, the retry delay appears between the plans.
2. With the or-success flag removed, only one plan is executed. Due to the random selection flag, one of the three plans is chosen randomly for each goal processing cycle.
3. The retry delay has no meaning without the or-success, because only one plan is executed and no retry happens. You can instead specify a recur delay, to add some waiting time before executing the next randomly selected plan.
4. With or-success set to false, the *AND* semantics is enabled meaning that the agent continues executing plans as long as there are plans in the APL. With the exclude mode *when-failed*, all of the patrol plans remain in the APL even after they have been executed, and thus can be selected again and again. Random selection causes the cleaner to select a random plan from the APL instead of the first. Without random selection, only the first plan would be executed over and over. Finally, the retry delay halts the cleaner after each execution of a plan.
5. All three plans get executed in parallel and try moving the cleaner to different locations at once. One of the plan "wins" and is allowed to execute its `moveTo()` action, while the other two are stopped with an error message. Therefore, only one of the patrol rounds is actually performed.