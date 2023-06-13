package com.yfletch.ocbwans;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.util.NpcHelper;
import com.yfletch.occore.util.ObjectHelper;
import com.yfletch.occore.v2.CoreContext;
import java.util.Arrays;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;

@Singleton
public class BwansContext extends CoreContext
{
	@Inject private Client client;
	@Inject private ObjectHelper objectHelper;
	@Inject private NpcHelper npcHelper;

	public TileObject getNearestRange()
	{
		return objectHelper.getNearest(
			obj -> obj.getName().contains("Range") || obj.getName().contains("Fire")
		);
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

	public Widget getMakeButton()
	{
		final var cooked = client.getWidget(
			WidgetID.MULTISKILL_MENU_GROUP_ID,
			15
		);

		return cooked != null
			? cooked
			// poison
			: client.getWidget(WidgetID.MULTISKILL_MENU_GROUP_ID, 14);
	}

	public boolean isSkillInterfaceOpen()
	{
		return getMakeButton() != null;
	}
}
