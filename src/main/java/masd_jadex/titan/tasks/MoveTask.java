package masd_jadex.titan.tasks;

import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;


public class MoveTask extends AbstractTask
{

    public static final String TASK_TYPENAME = "move";
    public static final String	PROPERTY_DESTINATION = "destination";

    public static final String	PROPERTY_SPEED	= "speed"; // m/s
    public static final String	PROPERTY_VISION	= "vision"; // meters
    public static final String	PROPERTY_TARGETRADIUS = "targetradius"; // meters


    /**
     *  Executes the task.
     *  @param space The environment in which the task is executing.
     *  @param avatar The object that is executing the task.
     *  @param progress The time that has passed according to the environment executor.
     */
    public void execute(IEnvironmentSpace space, ISpaceObject avatar, long progress, IClockService clock)
    {
        double speed = ((Number)avatar.getProperty(PROPERTY_SPEED)).doubleValue();
        double maxDist = progress * speed * 0.001;
        IVector2 position = (IVector2)avatar.getProperty(Space2D.PROPERTY_POSITION);

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
            newPosition = destination;
        }

        ((Space2D)space).setPosition(avatar.getId(), newPosition);

        if(newPosition==destination) {
            setFinished(space, avatar, true);
        }
    }

}
