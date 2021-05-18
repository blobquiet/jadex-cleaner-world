package masd_jadex.titan.work_pool_supervision;

import jadex.bridge.service.annotation.Service;
import jadex.extension.envsupport.math.IVector2;
import masd_jadex.titan.model.MiningSiteInfo;

@Service
public interface IWorkPoolSupervision
{
    void requestMiningSlot(IVector2 agentPosition);
    void takeMiningSlot(int slotReservationId);
    void freeMiningSlot(int slotReservationId);

    void foundMiningSite(MiningSiteInfo info);
    void miningSiteDepleted(IVector2 miningSitePosition);
}
