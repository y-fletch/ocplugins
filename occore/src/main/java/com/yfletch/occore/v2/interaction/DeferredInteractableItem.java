package com.yfletch.occore.v2.interaction;

import net.runelite.api.Item;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.items.Bank;

public class DeferredInteractableItem extends DeferredInteractable<Item>
{
	private final Item item;
	private final Item.Type type;

	public DeferredInteractableItem(Item item)
	{
		super(item);
		this.item = item;
		this.type = Bank.isOpen()
			? Item.Type.BANK_INVENTORY
			: Item.Type.INVENTORY;
	}

	public DeferredInteractableItem(Item item, Item.Type type)
	{
		super(item);
		this.item = item;
		this.type = type == Item.Type.INVENTORY && Bank.isOpen()
			? Item.Type.BANK_INVENTORY
			: type;
	}

	@Override
	public Item unwrap()
	{
		return item;
	}

	/**
	 * Drop the item
	 */
	public DeferredInteraction<Item> drop()
	{
		return interact("Drop");
	}

	/**
	 * Equip the item
	 */
	public DeferredInteraction<Item> equip()
	{
		return interact("Wear", "Wield");
	}

	/**
	 * Remove the item
	 */
	public DeferredInteraction<Item> remove()
	{
		return interact("Remove");
	}

	/**
	 * Use the item on a target
	 */
	public DeferredItemInteraction useOn(DeferredInteractable<?> target)
	{
		return useOn(target.unwrap());
	}

	/**
	 * Use the item on a target
	 */
	public DeferredItemInteraction useOn(Interactable target)
	{
		if (target == null)
		{
			return null;
		}

		return new DeferredItemInteraction(item, target);
	}
}
