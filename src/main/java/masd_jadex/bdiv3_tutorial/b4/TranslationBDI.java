package masd_jadex.bdiv3_tutorial.b4;

import java.util.HashMap;
import java.util.Map;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.Description;

/**
 *!!! Doesn't work: org.objectweb.asm.ClassVisitor.visitNestMemberExperimental throws UnsupportedOperationException
 *  The translation agent B4.
 *
 *  Using other plan methods.
 */
@Agent(type=BDIAgentFactory.TYPE)
@Description("The translation agent B4. <br>  Using other plan methods.")
public class TranslationBDI
{
	/** The bdi api. */
	@AgentFeature
	protected IBDIAgentFeature bdi;

	/** The wordtable. */
	protected Map<String, String> wordtable;

	@OnInit
	public void init()
	{
		this.wordtable = new HashMap<String, String>();
		this.wordtable.put("coffee", "Kaffee");
		this.wordtable.put("milk", "Milch");
		this.wordtable.put("cow", "Kuh");
		this.wordtable.put("cat", "Katze");
		this.wordtable.put("dog", "Hund");
	}

	/**
	 *  The agent body.
	 */
	@OnStart
	public void body()
	{
		try
		{
			System.out.println(bdi.adoptPlan(new TranslatePlan("dog")).get());
		}
		catch(Exception e)
		{
			System.out.println("Plan exception: "+e);
		}
	}

	/**
	 *  Translate an English word to German.
	 */
	@Plan
	public class TranslatePlan
	{
		/** The German word. */
		protected String gword;

		/**
		 *  Create a new TranslatePlan.
		 */
		public TranslatePlan(String gword)
		{
			this.gword = gword;
		}

		/**
		 *  The plan body.
		 */
		@PlanBody
		public String translateEnglishGerman()
		{
//			throw new PlanFailureException();
			return wordtable.get(gword);
		}

		/**
		 *  Called when plan passed.
		 */
		@PlanPassed
		public void passed()
		{
			System.out.println("Plan finished successfully.");
		}

		/**
		 *  Called when plan is aborted.
		 */
		@PlanAborted
		public void aborted()
		{
			System.out.println("Plan aborted.");
		}

		/**
		 *  Called when plan fails.
		 */
		@PlanFailed
		public void failed(Exception e)
		{
			System.out.println("Plan failed: "+e);
		}
	}
}

