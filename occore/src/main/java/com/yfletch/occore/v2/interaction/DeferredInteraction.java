package com.yfletch.occore.v2.interaction;

import com.yfletch.occore.v2.util.TextColor;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.runelite.api.Item;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.unethicalite.api.Interactable;
import net.unethicalite.client.Static;

/**
 * Represents a deferred interaction with an NPC, TileObject
 * or Widget
 */
@RequiredArgsConstructor
public class DeferredInteraction<T extends Interactable>
{
	@Nonnull
	protected T interactable;
	private final int actionIndex;

	/**
	 * Callback to run after _each_ execution
	 * of this interaction
	 */
	@Setter
	@Accessors(fluent = true,
			   chain = true)
	private Runnable then;

	/**
	 * Callback to run after this interaction
	 * is complete
	 */
	@Setter
	@Accessors(fluent = true,
			   chain = true)
	private Runnable after;

	public final String getTooltip()
	{
		return getMenuOption() + " " + getMenuTarget();
	}

	public void execute()
	{
		interactable.interact(actionIndex);

		if (interactable instanceof Item)
		{
			final var item = (Item) interactable;
			if (item.getType() != Item.Type.BANK)
			{
				Entities.markInteracted(item);
			}
		}
	}

	/**
	 * Run anything that needs to be done before the
	 * menu entry is clicked (OC only)
	 */
	public void prepare()
	{
		if (interactable instanceof Item)
		{
			final var item = (Item) interactable;
			if (item.getType() != Item.Type.BANK)
			{
				Entities.markInteracted(item);
			}
		}
	}

	public MenuEntry createMenuEntry()
	{
		return Static.getClient().createMenuEntry(-1)
			.setOption(getMenuOption())
			.setTarget(getMenuTarget())
			.setType(getMenuType())
			.setIdentifier(getMenuIdentifier())
			.setParam0(getMenuParam0())
			.setParam1(getMenuParam1());
	}

	public String getMenuOption()
	{
		if (MenuEntryUtil.isDialogOption(interactable))
		{
			return TextColor.WHITE + "Continue" + TextColor.END;
		}

		final var actions = interactable.getActions();
		if (actions == null || actions.length <= actionIndex)
		{
			return TextColor.DANGER + "Unknown option" + TextColor.END;
		}

		return TextColor.WHITE + actions[actionIndex] + TextColor.END;
	}

	public String getMenuTarget()
	{
		return MenuEntryUtil.getTarget(interactable);
	}

	protected MenuAction getMenuType()
	{
		return MenuEntryUtil.getType(interactable, actionIndex);
	}

	protected int getMenuIdentifier()
	{
		return MenuEntryUtil.getIdentifier(interactable, actionIndex);
	}

	protected int getMenuParam0()
	{
		return MenuEntryUtil.getParam0(interactable);
	}

	protected int getMenuParam1()
	{
		return MenuEntryUtil.getParam1(interactable);
	}
}
