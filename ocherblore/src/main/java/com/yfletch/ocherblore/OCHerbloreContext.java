package com.yfletch.ocherblore;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionContext;
import com.yfletch.occore.util.NpcHelper;
import com.yfletch.occore.util.ObjectHelper;
import java.util.Arrays;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.ItemManager;

@Singleton
public class OCHerbloreContext extends ActionContext
{
	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ObjectHelper objectHelper;

	@Inject
	private NpcHelper npcHelper;

	@Inject
	private OCHerbloreConfig config;

	public int getPrimaryItemId()
	{
		return config.primary();
	}

	public int getSecondaryItemId()
	{
		return config.secondary();
	}

	public ItemComposition getPrimaryItem()
	{
		return getPrimaryItemId() > 0
			? itemManager.getItemComposition(getPrimaryItemId())
			: null;
	}

	public ItemComposition getSecondaryItem()
	{
		return getSecondaryItemId() > 0
			? itemManager.getItemComposition(getSecondaryItemId())
			: null;
	}

	public TileObject getNearestBank()
	{
		// get nearest object that has a "Bank" option
		return objectHelper.getNearest(
			obj -> Arrays.asList(obj.getActions()).contains("Bank")
				|| obj.getName().contains("Bank")
		);
	}

	public NPC getNearestBankNPC()
	{
		// get nearest npc that has a "Bank" option
		return npcHelper.getNearest(
			npc -> Arrays.asList(npc.getActions()).contains("Bank")
		);
	}

	public boolean isMixing()
	{
		final var current = getPlayerAnimation() == 363;
		if (current)
		{
			flag("mixing", true, 3);
			return true;
		}

		return flag("mixing");
	}

	public Widget getMakeButton()
	{
		return client.getWidget(17694734);
	}

	public boolean isSkillInterfaceOpen()
	{
		return getMakeButton() != null;
	}
}
