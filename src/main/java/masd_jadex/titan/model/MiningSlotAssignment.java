package masd_jadex.titan.model;

import jadex.commons.transformation.annotations.IncludeFields;
import jadex.extension.envsupport.math.IVector2;

@IncludeFields
public class MiningSlotAssignment {
    public int slotReservationId;
    public int miningSiteId;
    public IVector2 miningSitePosition;
}
