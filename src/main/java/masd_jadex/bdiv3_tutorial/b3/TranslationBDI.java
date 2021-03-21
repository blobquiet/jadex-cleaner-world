package masd_jadex.bdiv3_tutorial.b3;

import java.util.HashMap;
import java.util.Map;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.Description;

/**
 *  The translation agent B3.
 *  
 *  Declare and activate an inline plan (declared as method).
 */
@Agent(type=BDIAgentFactory.TYPE)
@Description("The translation agent B3. <br>  Declare and activate an inline plan (declared as method).")
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
		bdi.adoptPlan("translateEnglishGerman");
	}
	
	/**
	 *  Translate an English word to German.
	 */
	@Plan
	public void translateEnglishGerman()
	{
		String eword = "dog";
		String gword = wordtable.get(eword);
		System.out.println("Translated: "+eword+" - "+gword);
	}
}

