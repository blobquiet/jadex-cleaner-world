package masd_jadex.titan.tasks;

import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;
import masd_jadex.titan.tasks.MoveTask;

import java.util.HashSet;
import java.util.Set;

//todo: clean what is not used in the  end
public class TakeMiningSlotTask extends AbstractTask implements MiningSiteTask
{

    public static final String TASK_TYPENAME = "takeMiningSlot";
    /**
     *  Executes the task.
     *  @param space The environment in which the task is executing.
     *  @param avatar The object that is executing the task.
     *  @param progress The time that has passed according to the environment executor.
     */
    public void execute(IEnvironmentSpace space, ISpaceObject avatar, long progress, IClockService clock) {
        ISpaceObject[] miningSites = space.getSpaceObjectsByType(OBJECT_TYPENAME);
        int miningSiteId = ((Number) getProperty(MiningSiteTask.PROPERTY_ID)).intValue();
        boolean miningSiteFound = false;
        for (ISpaceObject miningSite : miningSites) {
            if (((Number) miningSite.getProperty(MiningSiteTask.PROPERTY_ID)).intValue() == miningSiteId) {
                miningSiteFound = true;
                takeMiningSite(miningSite, avatar, space);
                break;
            }
        }

        if (!miningSiteFound) {
            throw new IllegalStateException("Someone tried to take a mining slot but no mining site with the given id exists.");
        }

        setFinished(space, avatar, true);
    }

    void takeMiningSite(ISpaceObject miningSite, ISpaceObject avatar, IEnvironmentSpace space) {
        if (!(Boolean) miningSite.getProperty(PROPERTY_DISCOVERED)) {
            throw new IllegalStateException("Someone tried to take a mining slot but the respective mining site was not yet discovered.");
        }

        IVector2 avatarPos = (IVector2)avatar.getProperty(Space2D.PROPERTY_POSITION);
        IVector2 miningSitePos = (IVector2)miningSite.getProperty(Space2D.PROPERTY_POSITION);
        double distance = ((Space2D)space).getDistance(avatarPos, miningSitePos).getAsDouble();
        double targetRadius = (Double)space.getProperty(MoveTask.PROPERTY_TARGETRADIUS);
        if (distance > targetRadius) {
            throw new IllegalStateException("Someone tried to take a mining slot but the distance to the mining site was to big.");
        }

        int num_slots = ((Number)miningSite.getProperty(PROPERTY_NUM_SLOTS)).intValue();
        assert num_slots >= 0;
        if (num_slots == 0) {
            throw new IllegalStateException("Someone tried to take a mining slot but there is no free slot available.");
        }

        if(miningSite.getProperty(PROPERTY_ACTIVE_MINERS) == null)
        {
            miningSite.setProperty(PROPERTY_ACTIVE_MINERS, new HashSet<Object>());
        }

        @SuppressWarnings("unchecked")
        Set<Object> activeMiners = (Set<Object>)miningSite.getProperty(PROPERTY_ACTIVE_MINERS);
        if (activeMiners.contains(avatar.getId())) {
            throw new IllegalStateException("Someone tried to take a mining slot but is already occupying one.");
        }

        miningSite.setProperty(PROPERTY_NUM_SLOTS, num_slots - 1);
        activeMiners.add(avatar.getId());
    }

}
