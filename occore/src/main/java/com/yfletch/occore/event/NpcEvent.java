package com.yfletch.occore.event;

import com.yfletch.occore.util.NpcHelper;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class NpcEvent extends EventOverride
{
	private final Client client;
	private final NpcHelper npcHelper;

	private Widget usedItemWidget;

	NpcEvent(Client client, NpcHelper npcHelper)
	{
		this.client = client;
		this.npcHelper = npcHelper;
		setParam0(0);
		setParam1(0);
	}

	/**
	 * Set option name and index (one-based)
	 */
	public NpcEvent setOption(String option, int index)
	{
		setOption(option);

		switch (index)
		{
			case 1:
				setType(MenuAction.NPC_FIRST_OPTION);
				break;
			case 2:
				setType(MenuAction.NPC_SECOND_OPTION);
				break;
			case 3:
				setType(MenuAction.NPC_THIRD_OPTION);
				break;
			case 4:
				setType(MenuAction.NPC_FOURTH_OPTION);
				break;
			case 5:
				setType(MenuAction.NPC_FIFTH_OPTION);
				break;
		}

		return this;
	}

	/**
	 * Target the specific NPC
	 */
	public NpcEvent setNpc(NPC npc)
	{
		setTarget(npc.getName());
		setIdentifier(npc.getIndex());

		return this;
	}

	/**
	 * Target the nearest NPC to the player matching the given ID
	 */
	public NpcEvent setNpc(int npcId)
	{
		NPC npc = npcHelper.getNearest(npcId);
		return npc != null ? setNpc(npc) : this;
	}

	private ArrayList<Widget> getItems(Collection<Integer> ids)
	{
		client.runScript(6009, 9764864, 28, 1, -1);
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		ArrayList<Widget> matchedItems = new ArrayList<>();

		if (inventoryWidget != null && inventoryWidget.getDynamicChildren() != null)
		{
			Widget[] items = inventoryWidget.getDynamicChildren();
			for (Widget item : items)
			{
				if (ids.contains(item.getItemId()))
				{
					matchedItems.add(item);
				}
			}
		}
		return matchedItems;
	}

	private Widget getItem(Collection<Integer> ids)
	{
		List<Widget> matches = getItems(ids);
		return matches.size() != 0 ? matches.get(matches.size() - 1) : null;
	}

	public NpcEvent use(int itemId)
	{
		usedItemWidget = getItem(Set.of(itemId));
		if (usedItemWidget == null)
		{
			throw new RuntimeException("Could not find use item in inventory: " + itemId);
		}
		setType(MenuAction.WIDGET_TARGET_ON_NPC);
		return this;
	}

	public NpcEvent on(NPC npc)
	{
		setOption("Use");
		setNpc(npc);
		return this;
	}

	public NpcEvent on(int npcId)
	{
		setOption("Use");
		setNpc(npcId);
		return this;
	}

	@Override
	public void override()
	{
		if (usedItemWidget != null)
		{
			setTarget(usedItemWidget.getName() + "<col=ffffff> -> " + getTarget());

			client.setSelectedSpellWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedSpellChildIndex(usedItemWidget.getIndex());
			client.setSelectedSpellItemId(usedItemWidget.getItemId());
			client.setSpellSelected(true);
		}
		super.override();
	}
}
