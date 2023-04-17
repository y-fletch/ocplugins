package com.yfletch.ocbankskills;

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
import net.runelite.api.util.Text;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.game.ItemManager;

@Singleton
public class Context extends ActionContext
{
	@Inject private Client client;
	@Inject private ItemManager itemManager;
	@Inject private ObjectHelper objectHelper;
	@Inject private NpcHelper npcHelper;

	@Inject private Config config;

	public boolean isAnimating()
	{
		if (flag("animating"))
		{
			return true;
		}

		if (getPlayer().isAnimating())
		{
			flag("animating", true, 5);
			return true;
		}

		return false;
	}

	public boolean hasPrimary()
	{
		return hasItem(getPrimaryItemId());
	}

	public boolean hasSecondary()
	{
		return hasItem(getSecondaryItemId());
	}

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
			obj -> obj.getActions() != null && Arrays.asList(obj.getActions()).contains("Bank")
				|| obj.getName().contains("Bank")
		);
	}

	public NPC getNearestBankNPC()
	{
		// get nearest npc that has a "Bank" option
		return npcHelper.getNearest(
			npc -> npc.getActions() != null && Arrays.asList(npc.getActions()).contains("Bank")
		);
	}

	public int getMakeOption()
	{
		return config.makeOption();
	}

	public Widget getMakeButton()
	{
		return client.getWidget(
			WidgetID.MULTISKILL_MENU_GROUP_ID,
			13 + getMakeOption()
		);
	}

	public String getMakeTarget()
	{
		final var makeButton = getMakeButton();
		if (makeButton == null)
		{
			return null;
		}

		return Text.removeTags(makeButton.getName());
	}

	public boolean isSkillInterfaceOpen()
	{
		return getMakeButton() != null;
	}
}
