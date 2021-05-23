package masd_jadex.titan.resource_extraction;

import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalParameter;
import jadex.bdiv3.annotation.GoalResult;
import jadex.extension.envsupport.math.IVector2;

@Goal
public class ReserveMiningSlotGoal
{
    public static class AssignMiningSlotMsgData
    {
        public IVector2 miningSitePostion;
        public int slotReservationId;
    }

    @GoalParameter
    protected IVector2 requestPosition;

    @GoalResult
    protected AssignMiningSlotMsgData assignMiningSlotMsgData;


    public ReserveMiningSlotGoal(IVector2 requestPosition) {
        this.requestPosition = requestPosition;
    }

    public AssignMiningSlotMsgData getAssignMiningSlotMsgData() {
        return assignMiningSlotMsgData;
    }
}
