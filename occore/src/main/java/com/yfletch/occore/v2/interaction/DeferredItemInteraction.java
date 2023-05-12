package com.yfletch.occore.v2.interaction;

import com.yfletch.occore.v2.util.TextColor;
import net.runelite.api.Item;
import net.unethicalite.api.Interactable;

public class DeferredItemInteraction extends DeferredInteraction
{
	private final Item item;
	private final Interactable target;

	public DeferredItemInteraction(Item item, int actionIndex)
	{
		super(item, actionIndex);
		this.item = item;
		target = null;
	}

	public DeferredItemInteraction(Item item, Interactable target)
	{
		super(item, 0);
		this.item = item;
		this.target = target;
	}

	@Override
	public void execute()
	{
		if (target != null)
		{
			item.useOn(target);
			return;
		}

		super.execute();
	}

	@Override
	public String getTarget()
	{
		if (target != null)
		{
			return TextColor.ITEM + item.getName() + TextColor.END
				+ TextColor.WHITE + " -> " + super.getTarget();
		}

		return super.getTarget();
	}
}
