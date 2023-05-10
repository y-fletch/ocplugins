package com.yfletch.occore.v2.interaction;

import com.yfletch.occore.v2.interaction.exceptions.InvalidOptionException;
import com.yfletch.occore.v2.interaction.exceptions.InvalidTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Item;
import net.runelite.api.MenuAction;
import net.runelite.api.Point;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.unethicalite.api.magic.Magic;
import net.unethicalite.api.widgets.Widgets;

@Slf4j
@RequiredArgsConstructor
public class InteractionExecutor
{
	private final static String COL_BLANK = "<col=ffffff>";
	private final static String COL_ITEM = "<col=ff9040>";
	private final static String COL_OBJECT = "<col=ffff>";
	private final static String COL_NPC = "<col=ffff00>";
	private final static String COL_SPELL = "<col=00ff00>";
	private final static String COL_END = "</col>";

	@Getter
	private static final Set<Item> itemsInteracted = new HashSet<>();

	public static void tick()
	{
		itemsInteracted.clear();
	}

	private static boolean consumeNext = false;

	public static void consumeNext()
	{
		consumeNext = true;
	}

	private final Interaction interaction;
	private boolean abort = false;
	private int repeatsLeft = -1;

	public boolean isComplete()
	{
		return abort || repeatsLeft == 0;
	}

	/**
	 * Run through all of the event and option logic,
	 * so any errors encountered are thrown without actually
	 * executing the event.
	 */
	public void validate()
	{
		getOption();
		getTarget();
		getAction();
		getId();
		getParam0();
		getParam1();
	}

	/**
	 * Execute the interaction using Devious APIs
	 */
	public void execute()
	{
		if (!setupExecution())
		{
			return;
		}

		if (interaction.isUseItem())
		{
			interaction.getUsing().getWrapped().useOn(interaction.getInteractable());

			runCallbacks();
			return;
		}

		if (interaction.isSpellCast())
		{
			if (interaction.hasTarget())
			{
				interaction.getSpell().castOn(interaction.getInteractable());
			}
			else
			{
				interaction.getSpell().cast();
			}

			runCallbacks();
			return;
		}

		if (interaction.hasTarget())
		{
			var index = getOptionIndex();
			if (interaction.getInventoryType() == WidgetInfo.BANK_ITEM_CONTAINER)
			{
				// for some reason, devious removes 1 from bank item interacts
				// so... we've gotta add it back
				index++;
			}

			interaction.getInteractable().interact(index);
			moveToNextItem();

			runCallbacks();
			return;
		}

		throw new IllegalStateException("No execution strategy found for interaction");
	}

	/**
	 * Execute using the one-click API (a.k.a. mutating
	 * the click event)
	 */
	public void execute(MenuOptionClicked event)
	{
		if (!setupExecution())
		{
			return;
		}

		if (consumeNext)
		{
			consumeNext = false;
			return;
		}

		if (interaction.isUseItem())
		{
			interaction.getUsing().use();
		}

		if (interaction.isSpellCast())
		{
			Magic.selectSpell(interaction.getSpell());
		}

		event.setMenuOption(getOption());
		event.setMenuTarget(getTarget());
		event.setMenuAction(getAction());
		event.setId(getId());
		event.setParam0(getParam0());
		event.setParam1(getParam1());
		if (hasCallbacks())
		{
			event.getMenuEntry().onClick((menuEntry) -> runCallbacks());
		}

		moveToNextItem();
	}

	private boolean setupExecution()
	{
		// get amount of repeats left from interaction,
		// only on first execute
		if (repeatsLeft == -1)
		{
			repeatsLeft = interaction.repeat();
		}

		if (isComplete())
		{
			if (interaction.after() != null)
			{
				interaction.after().run();

				// clear the runnable, so it isn't ran
				// again if this gets called again
				interaction.after(null);
			}
			return false;
		}

		repeatsLeft--;
		return true;
	}

	private boolean hasCallbacks()
	{
		return interaction.then() != null || interaction.after() != null;
	}

	private void runCallbacks()
	{
		if (interaction.then() != null)
		{
			interaction.then().run();
		}

		// because run callbacks is at the end of execution,
		// if the execution is _now_ complete that means it just
		// finished - so we can run the after callback
		if (isComplete() && interaction.after() != null)
		{
			interaction.after().run();

			// clear the runnable, so it isn't ran
			// again if this gets called again
			interaction.after(null);
		}
	}

	private void moveToNextItem()
	{
		// move to the next item immediately if interacting
		// with an inventory item. this allows for quick dropping etc
		// TODO: only move if not stackable
		if (interaction.isItemTarget()
			&& (interaction.getInventoryType() == WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER
			|| interaction.getInventoryType() == WidgetInfo.INVENTORY))
		{
			final var item = interaction.getItem().getWrapped();
			itemsInteracted.add(item);

			try
			{
				interaction.onItem(item.getId());
			}
			catch (IllegalArgumentException e)
			{
				// nothing in the next slot - nothing else
				// for this interaction to do
				abort = true;
			}
		}
	}

	/**
	 * Get the index of the target option in its menu.
	 * Returns -1 for menu actions that don't require option
	 * indices. Throws if the option is invalid or can't be found.
	 */
	public int getOptionIndex()
	{
		if (interaction.getOptionIndex() != -1)
		{
			// action index set during construction
			return interaction.getOptionIndex();
		}

		if (interaction.getUsing() != null || interaction.isDialog())
		{
			// no index required when using items,
			// or when clicking dialog options
			return -1;
		}

		if (interaction.isSpellCast())
		{
			if (interaction.hasTarget())
			{
				// casting spell on _something_,
				// no index required
				return -1;
			}

			if (interaction.getOptionFilter() == null)
			{
				// shortcut to "Cast"
				return 0;
			}

			// find the index of the option
			final var actions = getSpellWidget().getActions();
			if (actions == null)
			{
				throw new InvalidOptionException(interaction, "Spell widget does not have actions");
			}

			for (var i = 0; i < actions.length; i++)
			{
				if (interaction.getOptionFilter().test(actions[i]))
				{
					return i;
				}
			}

			throw new InvalidOptionException(interaction, "Could not find casting option index");
		}

		if (interaction.hasTarget())
		{
			if (interaction.getOptionFilter() == null)
			{
				if (interaction.isWidgetTarget())
				{
					// default to the first option for widgets
					return 0;
				}

				throw new InvalidOptionException(interaction, "No option filter provided");
			}

			final var actions = interaction.getInteractable().getActions();
			for (var i = 0; i < actions.length; i++)
			{
				if (interaction.getOptionFilter().test(actions[i]))
				{
					return i;
				}
			}

			throw new InvalidOptionException(interaction, "No option matched filter");
		}

		throw new InvalidOptionException(interaction, "No candidates for determining option index");
	}

	/**
	 * Get the event option name.
	 */
	public String getOption()
	{
		if (interaction.isDialog())
		{
			return "Continue";
		}

		if (interaction.isSpellCast())
		{
			final var actions = getSpellWidget().getActions();
			return interaction.getOptionFilter() == null || actions == null
				? "Cast"
				: Arrays.stream(actions)
				.filter(interaction.getOptionFilter())
				.findFirst()
				.orElseThrow(() -> new InvalidOptionException(interaction, "No option matched filter"));
		}

		if (!interaction.hasTarget())
		{
			throw new InvalidTargetException(interaction, "Missing target");
		}

		if (interaction.isUseItem())
		{
			return "Use";
		}

		final var actions = interaction.getInteractable().getActions();
		if (actions == null)
		{
			throw new InvalidTargetException(interaction, "Target does not have actions");
		}

		final var optionIndex = getOptionIndex();
		if (optionIndex != -1)
		{
			return actions[getOptionIndex()];
		}

		throw new InvalidOptionException(interaction, "No candidates for option name");
	}

	/**
	 * Get the event target name, including any formatting colours.
	 */
	public String getTarget()
	{
		if (interaction.isDialog())
		{
			return "(" + interaction.getWidget().getText() + ")";
		}

		if (!interaction.hasTarget())
		{
			if (interaction.isSpellCast())
			{
				return COL_SPELL + getSpellWidget().getName();
			}

			throw new InvalidTargetException(interaction, "Missing target");
		}

		var target = "";
		if (interaction.isNpcTarget())
		{
			target = COL_NPC + interaction.getNpc().getTransformedName();
		}

		if (interaction.isTileObjectTarget())
		{
			target = COL_OBJECT + interaction.getTileObject().getName();
		}

		if (interaction.isWidgetTarget())
		{
			target = COL_ITEM + interaction.getWidget().getName();
		}

		if (interaction.isItemTarget())
		{
			target = COL_ITEM + interaction.getItem().getWrapped().getName();
		}

		if (interaction.isUseItem())
		{
			target = COL_ITEM + interaction.getUsing().getWrapped().getName() + COL_END + COL_BLANK + " -> " + target;
		}

		if (interaction.isSpellCast())
		{
			target = COL_SPELL + getSpellWidget().getName() + COL_END + COL_BLANK + " -> " + target;
		}

		return target;
	}

	/**
	 * Get the menu action type.
	 */
	public MenuAction getAction()
	{
		final var index = getOptionIndex();

		// special cases
		if (index == -1)
		{
			if (interaction.isDialog())
			{
				return MenuAction.WIDGET_CONTINUE;
			}

			if (interaction.hasTarget())
			{
				if (interaction.isNpcTarget())
				{
					return MenuAction.WIDGET_TARGET_ON_NPC;
				}
				else if (interaction.isTileObjectTarget())
				{
					return MenuAction.WIDGET_TARGET_ON_GAME_OBJECT;
				}
				else if (interaction.isWidgetTarget() || interaction.isItemTarget())
				{
					return MenuAction.WIDGET_TARGET_ON_WIDGET;
				}
			}

			throw new IllegalStateException("Invalid action index");
		}

		if (interaction.isNpcTarget())
		{
			switch (index)
			{
				case 0:
					return MenuAction.NPC_FIRST_OPTION;
				case 1:
					return MenuAction.NPC_SECOND_OPTION;
				case 2:
					return MenuAction.NPC_THIRD_OPTION;
				case 3:
					return MenuAction.NPC_FOURTH_OPTION;
				case 4:
					return MenuAction.NPC_FIFTH_OPTION;
			}
		}

		if (interaction.isTileObjectTarget())
		{
			switch (index)
			{
				case 0:
					return MenuAction.GAME_OBJECT_FIRST_OPTION;
				case 1:
					return MenuAction.GAME_OBJECT_SECOND_OPTION;
				case 2:
					return MenuAction.GAME_OBJECT_THIRD_OPTION;
				case 3:
					return MenuAction.GAME_OBJECT_FOURTH_OPTION;
				case 4:
					return MenuAction.GAME_OBJECT_FIFTH_OPTION;
			}
		}

		return index > 5 ? MenuAction.CC_OP_LOW_PRIORITY : MenuAction.CC_OP;
	}

	/**
	 * Get the event ID (or identifier)
	 */
	public int getId()
	{
		if (interaction.hasTarget())
		{
			// object: object id
			if (interaction.isTileObjectTarget())
			{
				return interaction.getTileObject().getId();
			}

			// npc: npc index
			if (interaction.isNpcTarget())
			{
				return interaction.getNpc().getIndex();
			}
		}

		// item / widget / spell / dialog: option id (1-indexed)
		return getOptionIndex() + 1;
	}


	public int getParam0()
	{
		// item: slot index (except equipment)
		// item -> item: 2nd item slot index
		if (interaction.isItemTarget())
		{
			if (interaction.getInventoryType() == WidgetInfo.EQUIPMENT)
			{
				return -1;
			}

			return interaction.getItem().getWrapped().getSlot();
		}

		// object: object scene coords
		if (interaction.isTileObjectTarget())
		{
			return getSceneLocation().getX();
		}

		// widget: index
		if (interaction.isWidgetTarget())
		{
			return interaction.getWidget().getIndex();
		}

		// npc: 0
		if (interaction.isNpcTarget())
		{
			return 0;
		}

		// spell: -1
		if (interaction.isSpellCast())
		{
			return -1;
		}

		// default 0
		return 0;
	}

	public int getParam1()
	{
		// object: object scene coords
		if (interaction.isTileObjectTarget())
		{
			return getSceneLocation().getY();
		}

		// widget: id / parent id
		if (interaction.isWidgetTarget())
		{
			return interaction.getWidget().getId();
		}

		// single spell: widget id
		if (interaction.isSpellCast() && !interaction.hasTarget())
		{
			return interaction.getSpell().getWidget().getPackedId();
		}

		if (interaction.isItemTarget())
		{
			return interaction.getItem().getWidgetId();
		}

		// npc: 0
		return 0;
	}

	private Widget getSpellWidget()
	{
		return Widgets.get(interaction.getSpell().getWidget());
	}

	private Point getSceneLocation()
	{
		if (interaction.isGameObjectTarget())
		{
			return interaction.getGameObject().getSceneMinLocation();
		}

		if (interaction.isTileObjectTarget())
		{
			final var local = interaction.getTileObject().getLocalLocation();
			return new Point(local.getSceneX(), local.getSceneY());
		}

		if (interaction.isNpcTarget())
		{
			final var local = interaction.getNpc().getLocalLocation();
			return new Point(local.getSceneX(), local.getSceneY());
		}

		throw new IllegalArgumentException("Target does not have a scene location");
	}
}
