package masd_jadex.titan.work_pool_supervision;

import jadex.bridge.service.annotation.Service;
import jadex.commons.future.ITerminableFuture;
import jadex.extension.envsupport.math.IVector2;
import masd_jadex.titan.model.MiningSiteInfo;
import masd_jadex.titan.model.MiningSlotAssignment;

@Service
public interface IWorkPoolSupervision
{
    ITerminableFuture<MiningSlotAssignment> requestMiningSlot(IVector2 agentPosition);
    void takeMiningSlot(int slotReservationId);
    void freeMiningSlot(int slotReservationId);

    void foundMiningSite(MiningSiteInfo info);
    void miningSiteDepleted(IVector2 miningSitePosition);
}
