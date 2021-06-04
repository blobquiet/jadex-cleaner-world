package masd_jadex.titan.tasks;

import jadex.bridge.IComponentStep;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IPojoComponentFeature;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Double;
import masd_jadex.titan.agents.Miner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DrillForOreTask extends AbstractTask implements MiningSiteTask
{

    public static final String TASK_TYPENAME = "drillForOre";

    private long lastProgress = 0;

    /**
     *  Executes the task.
     *  @param space The environment in which the task is executing.
     *  @param avatar The object that is executing the task.
     *  @param progress The time that has passed according to the environment executor.
     */
    public void execute(IEnvironmentSpace space, ISpaceObject avatar, long progress, IClockService clock)
    {
        lastProgress += progress;

        ISpaceObject[] miningSites = space.getSpaceObjectsByType(OBJECT_TYPENAME);
        int miningSiteId = ((Number) getProperty(MiningSiteTask.PROPERTY_ID)).intValue();
        boolean miningSiteFound = false;
        for (ISpaceObject miningSite : miningSites) {
            if (((Number) miningSite.getProperty(MiningSiteTask.PROPERTY_ID)).intValue() == miningSiteId) {
                miningSiteFound = true;
                mineOre(miningSite, avatar, space);
                break;
            }
        }

        if (!miningSiteFound) {
            throw new IllegalStateException("Someone tried to mine ore at a mining slot but no mining site with the given id exists.");
        }
    }

    void mineOre(ISpaceObject miningSite, ISpaceObject avatar, IEnvironmentSpace space) {
        if (!(Boolean) miningSite.getProperty(PROPERTY_DISCOVERED)) {
            throw new IllegalStateException("Someone tried to mine ore at a mining slot but the respective mining site was not yet discovered.");
        }

        IVector2 avatarPos = (IVector2)avatar.getProperty(Space2D.PROPERTY_POSITION);
        IVector2 miningSitePos = (IVector2)miningSite.getProperty(Space2D.PROPERTY_POSITION);
        double distance = ((Space2D)space).getDistance(avatarPos, miningSitePos).getAsDouble();
        double targetRadius = (Double)space.getProperty(MoveTask.PROPERTY_TARGETRADIUS);
        if (distance > targetRadius) {
            throw new IllegalStateException("Someone tried to mine ore at a mining slot but the distance to the mining site was to big.");
        }

        @SuppressWarnings("unchecked")
        Set<Object> activeMiners = (Set<Object>)miningSite.getProperty(PROPERTY_ACTIVE_MINERS);
        if(activeMiners == null || !activeMiners.contains(avatar.getId())) {
            throw new IllegalStateException("Someone tried to mine ore but did not occupy a mining slot at the mining site.");
        }

        tryDropOre(miningSite, avatar, space);
    }

    void tryDropOre(ISpaceObject miningSite, ISpaceObject avatar, IEnvironmentSpace space) {
        if (lastProgress > 2000) {
            lastProgress = lastProgress - 2000;
            if (!(Boolean)miningSite.getProperty(MiningSiteTask.PROPERTY_DEPLETED)) {
                Map<String, Object> props = new HashMap<String, Object>();
                IVector2 randomVec = new Vector2Double(Math.random() * 6.0, Math.random() * 6.0).subtract(3.0);
                props.put(Space2D.PROPERTY_POSITION, ((IVector2) avatar.getProperty(Space2D.PROPERTY_POSITION)).copy().add(randomVec));
                space.createSpaceObject("Ore", props, null);
                miningSite.setProperty(MiningSiteTask.PROPERTY_REMAINING_ORE, ((Integer)miningSite.getProperty(MiningSiteTask.PROPERTY_REMAINING_ORE)) - 1);
                System.out.println("Produced one Ore.");
            }
        }
        if ((Boolean)miningSite.getProperty(MiningSiteTask.PROPERTY_DEPLETED))
        {
            ((IExternalAccess)getProperty(PROPERTY_AGENT_REF)).scheduleStep(new IComponentStep<Void>() {
                @Override
                public IFuture<Void> execute(IInternalAccess ia) {
                    Miner m = (Miner)ia.getFeature(IPojoComponentFeature.class).getPojoAgent();
                    m.setCurrentMiningSiteDepletedPercept(true);
                    return Future.DONE;
                }
            });
        }
    }

}
