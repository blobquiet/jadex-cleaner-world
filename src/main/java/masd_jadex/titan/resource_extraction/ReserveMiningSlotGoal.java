package masd_jadex.titan.resource_extraction;

import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalResult;
import jadex.extension.envsupport.math.IVector2;

@Goal
public class ReserveMiningSlotGoal
{
    @GoalResult
    protected int slotReservationId;
    @GoalResult
    protected IVector2 miningSitePosition;

    public ReserveMiningSlotGoal() { }

    public int getSlotReservationId() {
        return slotReservationId;
    }
    public IVector2 getMiningSitePosition() {
        return miningSitePosition;
    }
}
