package enoki.occonstruction;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionContext;
import com.yfletch.occore.util.NpcHelper;
import com.yfletch.occore.util.ObjectHelper;
import net.runelite.api.Client;
import net.runelite.client.game.ItemManager;

/*
 * Notes:
 * Phials ID = 1614
 * Portal Game Object = 15478
 * Larder Space = 15403
 * Oak Larder = 13566
 */


@Singleton
public class Context extends ActionContext
{
	@Inject private Client client;
	@Inject private ItemManager itemManager;
	@Inject private ObjectHelper objectHelper;
	@Inject private NpcHelper npcHelper;
	@Inject private Config config;
}
