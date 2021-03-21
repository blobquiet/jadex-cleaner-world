package masd_jadex.bdiv3_tutorial.b2;

import java.util.HashMap;
import java.util.Map;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.Plans;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.Description;

/**
 *  The translation agent B2.
 *  
 *  Declare and activate an inline plan (declared as inner class).
 */
@Agent(type=BDIAgentFactory.TYPE)
@Description("The translation agent B2. <br>  Declare and activate an inline plan (declared as inner class).")
@Plans(@Plan(body=@Body(TranslationBDI.TranslationPlan.class)))
public class TranslationBDI
{
	/** The bdi api. */
	@AgentFeature
	protected IBDIAgentFeature bdi;
	
	/**
	 *  The agent body.
	 */
	@OnStart
	public void body()
	{
		bdi.adoptPlan(new TranslationPlan());
	}
	
	/**
	 *  Inline translation plan.
	 */
	@Plan
	public class TranslationPlan
	{
		/** The wordtable. */
		protected Map<String, String> wordtable;

		/**
		 *  Create a new TranslationPlan.
		 */
		public TranslationPlan()
		{
			this.wordtable = new HashMap<String, String>();
			this.wordtable.put("coffee", "Kaffee");
			this.wordtable.put("milk", "Milch");
			this.wordtable.put("cow", "Kuh");
			this.wordtable.put("cat", "Katze");
			this.wordtable.put("dog", "Hund");
		}
		
		/**
		 *  Plan body invoke once when plan is activated. 
		 */
		@PlanBody
		public void translateEnglishGerman()
		{
			String eword = "dog";
			String gword = wordtable.get(eword);
			System.out.println("Translated: "+eword+" - "+gword);
		}
	}
}

