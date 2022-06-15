package com.yfletch.occore.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

public class ItemEvent extends EventOverride
{
	private final Client client;
	private WidgetInfo inventoryType = WidgetInfo.INVENTORY;

	ItemEvent(Client client)
	{
		this.client = client;

		// most common
		setType(MenuAction.CC_OP);
		setParam1(inventoryType.getId());
	}

	@Override
	protected void validate()
	{
		if (getIdentifier() < 0)
		{
			throw new RuntimeException("ItemEvent: Missing identifier");
		}

		super.validate();
	}

	private Widget getItem(Collection<Integer> ids)
	{
		List<Widget> matches = getItems(ids);
		return matches.size() != 0 ? matches.get(0) : null;
	}

	private ArrayList<Widget> getItems(Collection<Integer> ids)
	{
		client.runScript(6009, 9764864, 28, 1, -1);
		Widget inventoryWidget = client.getWidget(inventoryType);
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

	/**
	 * Target the first item in the inventory matching one of the
	 * given IDs.
	 */
	public ItemEvent setItems(Collection<Integer> itemIds)
	{
		Widget itemWidget = getItem(itemIds);
		if (itemWidget == null)
		{
			throw new RuntimeException("Could not find items in inventory: " + itemIds);
		}

		setTarget(itemWidget.getName());
		setParam0(isEquipment() ? -1 : itemWidget.getIndex());

		return this;
	}

	/**
	 * Target the first item in the inventory matching the given ID.
	 */
	public ItemEvent setItem(int itemId)
	{
		return setItems(List.of(itemId));
	}

	/**
	 * Set option name and index (one-based)
	 */
	public ItemEvent setOption(String option, int optionId)
	{
		setOption(option);
		setIdentifier(optionId);

		if ((inventoryType == WidgetInfo.BANK_ITEM_CONTAINER
			|| inventoryType == WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER)
			&& optionId > 5)
		{
			setLowPriority();
		}

		return this;
	}

	/**
	 * Shortcut for `setType(MenuAction.CC_OP_LOW_PRIORITY)`.
	 * MenuOptions seem to be low priority if their index is greater
	 * than 5 (and only for certain inventories). This is set automatically
	 * for relevant inventories.
	 */
	public ItemEvent setLowPriority()
	{
		setType(MenuAction.CC_OP_LOW_PRIORITY);
		return this;
	}

	/**
	 * Set the target inventory to the given widget. Must be called before
	 * `setItem` in order for the index to be set correctly. Defaults to the
	 * player's regular inventory.
	 * <p>
	 * `withdraw()` and `deposit()` are common shortcuts for those
	 * respective inventories. Use this directly for equipment slots like
	 * `WidgetInfo.EQUIPMENT_AMMO` etc
	 */
	public ItemEvent setInventory(WidgetInfo widgetInfo)
	{
		if (getTarget() != null)
		{
			throw new RuntimeException("setInventory must be called before setItem");
		}

		inventoryType = widgetInfo;
		setParam1(widgetInfo.getId());
		return this;
	}

	/**
	 * Shortcut for `setInventory(WidgetInfo.BANK_ITEM_CONTAINER)`.
	 * Set the target inventory to the given widget. Must be called before
	 * `setItem` in order for the index to be set correctly.
	 */
	public ItemEvent withdraw()
	{
		return setInventory(WidgetInfo.BANK_ITEM_CONTAINER);
	}
	
	/**
	 * Shortcut for `setInventory(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER)`.
	 * Set the target inventory to the given widget. Must be called before
	 * `setItem` in order for the index to be set correctly.
	 */
	public ItemEvent deposit()
	{
		return setInventory(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
	}

	private boolean isEquipment()
	{
		return inventoryType == WidgetInfo.EQUIPMENT_AMMO
			|| inventoryType == WidgetInfo.EQUIPMENT_HELMET
			|| inventoryType == WidgetInfo.EQUIPMENT_CAPE
			|| inventoryType == WidgetInfo.EQUIPMENT_AMULET
			|| inventoryType == WidgetInfo.EQUIPMENT_WEAPON
			|| inventoryType == WidgetInfo.EQUIPMENT_BODY
			|| inventoryType == WidgetInfo.EQUIPMENT_SHIELD
			|| inventoryType == WidgetInfo.EQUIPMENT_LEGS
			|| inventoryType == WidgetInfo.EQUIPMENT_GLOVES
			|| inventoryType == WidgetInfo.EQUIPMENT_BOOTS
			|| inventoryType == WidgetInfo.EQUIPMENT_RING;
	}
}
