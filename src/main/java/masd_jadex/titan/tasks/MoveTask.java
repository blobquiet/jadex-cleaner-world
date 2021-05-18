package masd_jadex.titan.tasks;

import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;



public class MoveTask extends AbstractTask
{

    public static final String	PROPERTY_TYPENAME = "move";
    public static final String	PROPERTY_DESTINATION = "destination";

    public static final String	PROPERTY_SPEED	= "speed"; // m/s
    public static final String	PROPERTY_VISION	= "vision"; // meters
    public static final String	PROPERTY_TARGETRADIUS = "targetradius"; // meters



    /**
     *  Executes the task.
     *  Handles exceptions. Subclasses should implement doExecute() instead.
     *  @param space	The environment in which the task is executing.
     *  @param obj	The object that is executing the task.
     *  @param progress	The time that has passed according to the environment executor.
     */
    public void execute(IEnvironmentSpace space, ISpaceObject obj, long progress, IClockService clock)
    {
        double speed = ((Number)obj.getProperty(PROPERTY_SPEED)).doubleValue();
        double maxDist = progress * speed * 0.001;
        IVector2 position = (IVector2)obj.getProperty(Space2D.PROPERTY_POSITION);

        double targetRadius = (Double)space.getProperty(PROPERTY_TARGETRADIUS);
        IVector2 destination = (IVector2)getProperty(PROPERTY_DESTINATION);
        double distance = ((Space2D)space).getDistance(position, destination).getAsDouble();
        IVector2 newPosition;
        if(distance > targetRadius)
        {
            newPosition = distance <= maxDist ? destination :
                destination.copy().subtract(position).normalize().multiply(maxDist).add(position);
        }
        else
        {
            newPosition = position;
        }

        ((Space2D)space).setPosition(obj.getId(), newPosition);

        // processVision(space, obj, agent);

        if(newPosition==destination) {
            setFinished(space, obj, true);
        }
    }

//    protected static void processVision(IEnvironmentSpace space, ISpaceObject obj, IExternalAccess agent)
//    {
//        // Process vision at new location.
//        double	vision	= ((Number)obj.getProperty(PROPERTY_VISION)).doubleValue();
//        final Set objects	= ((Space2D)space).getNearObjects((IVector2)obj.getProperty(Space2D.PROPERTY_POSITION), new Vector1Double(vision));
//        if(objects!=null)
//        {
//            agent.scheduleStep(new IComponentStep<Void>()
//            {
//                @Classname("add")
//                public IFuture<Void> execute(IInternalAccess ia)
//                {
//                    BaseAgent ba = (BaseAgent)ia.getFeature(IPojoComponentFeature.class).getPojoAgent();
//                    for(Iterator<ISpaceObject> it=objects.iterator(); it.hasNext(); )
//                    {
//                        final ISpaceObject so = it.next();
//                        if(so.getType().equals("target"))
//                        {
//                            ba.getMoveCapa().addTarget(so);
//                        }
//                    }
//                    return IFuture.DONE;
//                }
//            });
//        }
//    }
}
