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
	public DeferredEntityInteraction<Item> interact(int index)
	{
		return super.interact(index);
	}

	/**
	 * Drop the item
	 */
	public DeferredEntityInteraction<Item> drop()
	{
		return interact("Drop");
	}

	/**
	 * Equip the item
	 */
	public DeferredEntityInteraction<Item> equip()
	{
		return interact("Wear", "Wield");
	}

	/**
	 * Remove the item
	 */
	public DeferredEntityInteraction<Item> remove()
	{
		return interact("Remove");
	}

	/**
	 * Withdraw an amount from the bank. n should be 1, 5, or 10
	 */
	public DeferredEntityInteraction<Item> withdraw(int n)
	{
		if (item == null)
		{
			return null;
		}

		assert item.getType() == Item.Type.BANK;
		return interact("Withdraw-" + n);
	}

	/**
	 * Withdraw the custom amount from the bank
	 */
	public DeferredEntityInteraction<Item> withdrawX()
	{
		if (item == null)
		{
			return null;
		}

		assert item.getType() == Item.Type.BANK;
		return interact(4);
	}

	/**
	 * Withdraw all of this item from the bank
	 */
	public DeferredEntityInteraction<Item> withdrawAll()
	{
		if (item == null)
		{
			return null;
		}

		assert item.getType() == Item.Type.BANK;
		return interact("Withdraw-All");
	}

	/**
	 * Deposit an amount into the bank. n should be 1, 5, or 10
	 */
	public DeferredEntityInteraction<Item> deposit(int n)
	{
		if (item == null)
		{
			return null;
		}

		assert item.getType() == Item.Type.BANK_INVENTORY;
		return interact("Deposit-" + n);
	}

	/**
	 * Deposit the custom amount into the bank
	 */
	public DeferredEntityInteraction<Item> depositX()
	{
		if (item == null)
		{
			return null;
		}

		assert item.getType() == Item.Type.BANK_INVENTORY;
		return interact(5);
	}

	/**
	 * Deposit all of this item into the bank
	 */
	public DeferredEntityInteraction<Item> depositAll()
	{
		if (item == null)
		{
			return null;
		}

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
