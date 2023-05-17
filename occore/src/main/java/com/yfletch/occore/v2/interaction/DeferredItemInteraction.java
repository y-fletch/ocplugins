package com.yfletch.occore.v2.interaction;

import com.yfletch.occore.v2.util.TextColor;
import net.runelite.api.Item;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.Interactable;

/**
 * Represents a deferred interaction of using an item
 * on an entity
 */
public class DeferredItemInteraction extends DeferredEntityInteraction<Item>
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
	public void prepare()
	{
		if (target != null)
		{
			interactable.use();
		}

		super.prepare();
	}

	@Override
	public MenuEntry createMenuEntry()
	{
		final var menuEntry = super.createMenuEntry();
		menuEntry.setItemId(interactable.getId());
		return menuEntry;
	}

	@Override
	public String getMenuOption()
	{
		if (target != null)
		{
			return TextColor.WHITE + "Use";
		}

		return super.getMenuOption();
	}

	@Override
	public String getMenuTarget()
	{
		if (target != null)
		{
			return TextColor.ITEM + interactable.getName() + TextColor.END
				+ TextColor.WHITE + " -> " + MenuEntryUtil.getTarget(target);
		}

		return super.getMenuTarget();
	}

	@Override
	protected MenuAction getMenuType()
	{
		if (target != null)
		{
			if (target instanceof NPC)
			{
				return MenuAction.WIDGET_TARGET_ON_NPC;
			}

			if (target instanceof TileObject)
			{
				return MenuAction.WIDGET_TARGET_ON_GAME_OBJECT;
			}

			if (target instanceof Widget || target instanceof Item)
			{
				return MenuAction.WIDGET_TARGET_ON_WIDGET;
			}
		}

		return super.getMenuType();
	}

	@Override
	protected int getMenuIdentifier()
	{
		if (target != null)
		{
			return MenuEntryUtil.getIdentifier(target);
		}

		return super.getMenuIdentifier();
	}

	@Override
	protected int getMenuParam0()
	{
		if (target != null)
		{
			return MenuEntryUtil.getParam0(target);
		}

		return super.getMenuParam0();
	}

	@Override
	protected int getMenuParam1()
	{
		if (target != null)
		{
			return MenuEntryUtil.getParam1(target);
		}

		return super.getMenuParam1();
	}
}
