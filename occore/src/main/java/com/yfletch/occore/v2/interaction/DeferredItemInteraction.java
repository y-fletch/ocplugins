package com.yfletch.occore.v2.interaction;

import com.yfletch.occore.v2.util.TextColor;
import net.runelite.api.Item;
import net.unethicalite.api.Interactable;

public class DeferredItemInteraction extends DeferredInteraction<Item>
{
	private final Interactable target;

	public DeferredItemInteraction(Item item, Interactable target)
	{
		super(item, 0);
		this.target = target;
	}

	@Override
	public void execute()
	{
		if (target != null)
		{
			interactable.useOn(target);
			return;
		}

		super.execute();
	}

	@Override
	public String getActionText()
	{
		if (target != null)
		{
			return TextColor.WHITE + "Use";
		}

		return super.getActionText();
	}

	@Override
	public String getTargetText()
	{
		if (target != null)
		{
			return TextColor.ITEM + interactable.getName() + TextColor.END
				+ TextColor.WHITE + " -> " + getTargetText(target);
		}

		return super.getTargetText();
	}
}
