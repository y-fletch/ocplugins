package com.yfletch.occore.v2.interaction;

import net.runelite.api.Item;
import net.unethicalite.api.Interactable;

public class DeferredInteractableItem extends DeferredInteractable
{
	private final Item item;
	private final Item.Type type;

	public DeferredInteractableItem(Item item)
	{
		super(item);
		this.item = item;
		this.type = Item.Type.INVENTORY;
	}

	public DeferredInteractableItem(Item item, Item.Type type)
	{
		super(item);
		this.item = item;
		this.type = type;
	}

	/**
	 * Use the item on a target
	 */
	public DeferredItemInteraction useOn(DeferredInteractable target)
	{
		return useOn(target.unwrap());
	}

	/**
	 * Use the item on a target
	 */
	public DeferredItemInteraction useOn(Interactable target)
	{
		return new DeferredItemInteraction(item, target);
	}
}
