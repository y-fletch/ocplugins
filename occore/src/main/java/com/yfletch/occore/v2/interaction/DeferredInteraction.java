package com.yfletch.occore.v2.interaction;

import com.yfletch.occore.v2.util.TextColor;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.runelite.api.Item;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.WidgetInfo;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.items.Bank;
import net.unethicalite.client.Static;

/**
 * Represents a deferred interaction with an NPC, TileObject
 * or Widget
 */
@RequiredArgsConstructor
public class DeferredInteraction<T extends Interactable>
{
	@Nonnull
	protected final T interactable;
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

//	public DeferredInteraction(@Nonnull T interactable, int actionIndex)
//	{
//		this.interactable = interactable;
//		this.actionIndex = actionIndex;
//	}

	public final String getTooltip()
	{
		return getMenuOption() + " " + getMenuTarget();
	}

	public void execute()
	{
		if (interactable instanceof Item)
		{
			final var item = (Item) interactable;
			final var offset = item.getType() == Item.Type.BANK
				|| item.getType() == Item.Type.BANK_INVENTORY
				? 1 : 0;

			interactable.interact(actionIndex + offset);

			if (item.getType() != Item.Type.BANK && !item.isStackable())
			{
				Entities.markInteracted(item);
			}
		}
		else
		{
			interactable.interact(actionIndex);
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
		// force the widget ID of the item, just in case
		// it has been changed by & to another inventory type
		if (interactable instanceof Item)
		{
			final var item = (Item) interactable;
			if (Bank.isOpen() && item.getType() == Item.Type.INVENTORY)
			{
				item.setWidgetId(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId());
			}
		}

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
