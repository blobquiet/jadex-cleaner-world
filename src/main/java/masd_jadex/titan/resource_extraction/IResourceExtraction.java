package masd_jadex.titan.resource_extraction;

import jadex.extension.envsupport.math.IVector2;

public interface IResourceExtraction {
    void assignMiningSlot(IVector2 miningSitePosition, int slotReservationId);
    void denyMiningSlot();
}
