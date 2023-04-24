package it.enok.ocnightmarezone;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionContext;
import com.yfletch.occore.util.NpcHelper;
import com.yfletch.occore.util.ObjectHelper;
import net.runelite.api.Client;

@Singleton
public class Context extends ActionContext
{
	@Inject
	private Client client;
	
	@Inject
	private ObjectHelper objectHelper;
	
	@Inject
	private NpcHelper npcHelper;
}
