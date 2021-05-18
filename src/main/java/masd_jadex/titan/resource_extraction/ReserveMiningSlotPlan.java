package masd_jadex.titan.resource_extraction;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.math.IVector2;
import masd_jadex.titan.capabilities.LocomotionCapability;
import masd_jadex.titan.work_pool_supervision.IWorkPoolSupervision;

@Plan
public class ReserveMiningSlotPlan {

    @PlanCapability
    protected ResourceExtractionCapability capability;

    @PlanCapability
    protected LocomotionCapability locomotionCapability;

    @PlanAPI
    protected IPlan rplan;

    @PlanReason
    protected ReserveMiningSlotGoal goal;

    public ReserveMiningSlotPlan() { }

    @PlanBody
    public IFuture<Void> body()
    {
        final Future<Void> ret = new Future<Void>();

        IWorkPoolSupervision workPoolSupervision = capability.getWorkPoolSupervision();
        workPoolSupervision.requestMiningSlot((IVector2)locomotionCapability.getAvatar().getProperty("position"));
        // TODO: somehow wait on a change event of this believe? Not sure how you wait for an answer:
        //resourceExtractionCapability.slotReservationId


//        IFuture<LocomotionCapability.MoveGoal> fut = rplan.dispatchSubgoal(capa.new AchieveMoveTo(waste.getLocation()));
//        fut.addResultListener(new ExceptionDelegationResultListener<CleanerAgent.AchieveMoveTo, Void>(ret)
//        {
//            public void customResultAvailable(AchieveMoveTo amt)
//            {
//                IFuture<PickupWasteAction> fut = rplan.dispatchSubgoal(capa.new PickupWasteAction(waste));
//                fut.addResultListener(new ExceptionDelegationResultListener<CleanerAgent.PickupWasteAction, Void>(ret)
//                {
//                    public void customResultAvailable(PickupWasteAction pwa)
//                    {
//                        capa.setCarriedwaste(waste);
////						System.out.println("carried waste set to: "+waste+rplan.getId()+" "+((IGoal)rplan.getReason()).getId());
//                        capa.getWastes().remove(waste);
//                        ret.setResult(null);
//                    }
//                });
//            }
//        });

        return ret;
    }
}
