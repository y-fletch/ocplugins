package com.yfletch.occore.v2.interaction;

import net.runelite.api.Item;
import net.unethicalite.api.Interactable;

public class DeferredInteractableItem extends DeferredInteractable<Item>
{
	private final Item item;

	public DeferredInteractableItem(Item item)
	{
		super(item);
		this.item = item;
	}

	public DeferredInteractableItem(Item item, Item.Type type)
	{
		super(item);
		this.item = item;
	}

	@Override
	public Item unwrap()
	{
		return item;
	}

	@Override
	public DeferredInteraction<Item> interact(int index)
	{
		return super.interact(index);
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
	 * Withdraw an amount from the bank. n should be 1, 5, or 10
	 */
	public DeferredInteraction<Item> withdraw(int n)
	{
		assert item.getType() == Item.Type.BANK;
		return interact("Withdraw-" + n);
	}

	/**
	 * Withdraw the custom amount from the bank
	 */
	public DeferredInteraction<Item> withdrawX()
	{
		assert item.getType() == Item.Type.BANK;
		return interact(4);
	}

	/**
	 * Withdraw all of this item from the bank
	 */
	public DeferredInteraction<Item> withdrawAll()
	{
		assert item.getType() == Item.Type.BANK;
		return interact("Withdraw-All");
	}

	/**
	 * Deposit an amount into the bank. n should be 1, 5, or 10
	 */
	public DeferredInteraction<Item> deposit(int n)
	{
		assert item.getType() == Item.Type.BANK_INVENTORY;
		return interact("Deposit-" + n);
	}

	/**
	 * Deposit the custom amount into the bank
	 */
	public DeferredInteraction<Item> depositX()
	{
		assert item.getType() == Item.Type.BANK_INVENTORY;
		return interact(5);
	}

	/**
	 * Deposit all of this item into the bank
	 */
	public DeferredInteraction<Item> depositAll()
	{
		assert item.getType() == Item.Type.BANK_INVENTORY;
		return interact("Deposit-All");
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
		if (item == null || target == null)
		{
			return null;
		}

		return new DeferredItemInteraction(item, target);
	}
}
