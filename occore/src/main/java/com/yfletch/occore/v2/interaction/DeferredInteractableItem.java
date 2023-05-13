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
		if (item == null)
		{
			this.type = Item.Type.INVENTORY;
		}
		else
		{
			this.type = item.getType() == Item.Type.INVENTORY && Bank.isOpen()
				? Item.Type.BANK_INVENTORY
				: item.getType();
		}
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
	 * Withdraw an amount from the bank. n should be 1, 5, or 10
	 */
	public DeferredInteraction<Item> withdraw(int n)
	{
		assert type == Item.Type.BANK;
		return interact("Withdraw-" + n);
	}

	/**
	 * Withdraw the custom amount from the bank
	 */
	public DeferredInteraction<Item> withdrawX()
	{
		assert type == Item.Type.BANK;
		return interact(5);
	}

	/**
	 * Withdraw all of this item from the bank
	 */
	public DeferredInteraction<Item> withdrawAll()
	{
		assert type == Item.Type.BANK;
		return interact("Withdraw-All");
	}

	/**
	 * Deposit an amount into the bank. n should be 1, 5, or 10
	 */
	public DeferredInteraction<Item> deposit(int n)
	{
		assert type == Item.Type.BANK_INVENTORY;
		return interact("Deposit-" + n);
	}

	/**
	 * Deposit the custom amount into the bank
	 */
	public DeferredInteraction<Item> depositX()
	{
		assert type == Item.Type.BANK;
		return interact(5);
	}

	/**
	 * Deposit all of this item into the bank
	 */
	public DeferredInteraction<Item> depositAll()
	{
		assert type == Item.Type.BANK;
		return interact("Withdraw-All");
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
