package masd_jadex.titan.resource_extraction;

import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalParameter;
import jadex.bdiv3.annotation.GoalResult;
import jadex.extension.envsupport.math.IVector2;
import masd_jadex.titan.model.MiningSlotAssignment;

@Goal
public class ReserveMiningSlotGoal
{
    @GoalParameter
    protected IVector2 requestPosition;

    @GoalResult
    protected MiningSlotAssignment slotAssignment;


    public ReserveMiningSlotGoal(IVector2 requestPosition) {
        this.requestPosition = requestPosition;
    }

    public MiningSlotAssignment getMiningSlotAssignment() {
        return slotAssignment;
    }
    public void setMiningSlotAssignment(MiningSlotAssignment assignment) {
        assert slotAssignment == null;
        assert assignment != null;
        slotAssignment = assignment;
    }
}
