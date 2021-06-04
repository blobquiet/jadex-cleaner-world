package masd_jadex.titan.tasks;

public interface MiningSiteTask {
    String OBJECT_TYPENAME = "MiningSite";
    String PROPERTY_ID	= "id";
    String PROPERTY_DISCOVERED = "discovered";
    String PROPERTY_NUM_SLOTS = "num_slots";
    String PROPERTY_REMAINING_ORE = "remaining_ore";
    String PROPERTY_DEPLETED = "depleted";
    String PROPERTY_ACTIVE_MINERS = "active_miners";
    String PROPERTY_AGENT_REF = "agentRef";
}
