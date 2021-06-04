package masd_jadex.titan.tasks;

import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;

import java.util.HashSet;
import java.util.Set;

//todo: clean what is not used in the  end
public class FreeMiningSlotTask extends AbstractTask implements MiningSiteTask
{

    public static final String TASK_TYPENAME = "freeMiningSlot";

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
                freeMiningSite(miningSite, avatar, space);
                break;
            }
        }

        if (!miningSiteFound) {
            throw new IllegalStateException("Someone tried to free a mining slot but no mining site with the given id exists.");
        }

        setFinished(space, avatar, true);
    }

    void freeMiningSite(ISpaceObject miningSite, ISpaceObject avatar, IEnvironmentSpace space) {
        if (!(Boolean) miningSite.getProperty(PROPERTY_DISCOVERED)) {
            throw new IllegalStateException("Someone tried to free a mining slot but the respective mining site was not yet discovered.");
        }

        boolean agentFound = false;
        @SuppressWarnings("unchecked")
        Set<Object> activeMiners = (Set<Object>)miningSite.getProperty(PROPERTY_ACTIVE_MINERS);
        if(activeMiners != null)
        {
            agentFound = activeMiners.remove(avatar.getId());
        }

        if (!agentFound) {
            System.out.println("Someone tried to free a mining slot but non was occupied.");
        }
    }

}
