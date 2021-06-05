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
import masd_jadex.titan.agents.Scout;

public class PerceiveMiningSlotsTask extends AbstractTask implements MiningSiteTask
{
    public static final String TASK_TYPENAME = "perceiveMiningSlots";

    /**
     *  Executes the task.
     *  @param space The environment in which the task is executing.
     *  @param avatar The object that is executing the task.
     *  @param progress The time that has passed according to the environment executor.
     */
    public void execute(IEnvironmentSpace space, ISpaceObject avatar, long progress, IClockService clock) {
        ISpaceObject[] miningSites = space.getSpaceObjectsByType(MiningSiteTask.OBJECT_TYPENAME);
        IVector2 position = (IVector2)avatar.getProperty(Space2D.PROPERTY_POSITION);
        float vision_range = ((Number)avatar.getProperty("vision_range")).floatValue();


        for (ISpaceObject miningSite : miningSites) {
            IVector2 miningSitePos = (IVector2)miningSite.getProperty(Space2D.PROPERTY_POSITION);
            float distance = ((Space2D)space).getDistance(position, miningSitePos).getAsFloat();
            if (distance < vision_range) {
                if (!(Boolean)miningSite.getProperty(MiningSiteTask.PROPERTY_DISCOVERED)) {
                    miningSite.setProperty(MiningSiteTask.PROPERTY_DISCOVERED, true);

                    ((IExternalAccess) getProperty(PROPERTY_AGENT_REF)).scheduleStep(new IComponentStep<Void>() {
                        @Override
                        public IFuture<Void> execute(IInternalAccess ia) {
                            Scout m = (Scout) ia.getFeature(IPojoComponentFeature.class).getPojoAgent();
                            m.setMiningSiteDiscoveredPercept(miningSite);
                            return Future.DONE;
                        }
                    });
                }
            }
        }
    }


}
