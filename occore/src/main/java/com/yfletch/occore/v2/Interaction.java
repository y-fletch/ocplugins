package com.yfletch.occore.v2;

import com.google.common.primitives.Ints;
import com.yfletch.occore.v2.util.StaticItem;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.Item;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.Magic;
import net.unethicalite.api.magic.Spell;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Widgets;

/**
 * An interaction with the game that can be executed
 * at a later time.
 * <p>
 * Examples:
 * <p>
 * Inventory:
 * drop(ItemID.GRANITE_500G)
 * equip(ItemID.DRAGON_PICKAXE)
 * unequip(ItemID.WIZARD_BOOTS)
 * use(ItemID.GUAM_LEAF).onItem(ItemID.SWAMP_TAR)
 * from(InventoryID.EQUIPMENT).use("Castle Wars").on(getNextDuelingRing())
 * <p>
 * Bank:
 * withdraw(5, ItemID.PURE_ESSENCE)
 * withdrawX(ItemID.PURE_ESSENCE)
 * withdrawAll(ItemID.PURE_ESSENCE)
 * deposit(5, ItemID.FIRE_RUNE)
 * depositX(ItemID.FIRE_RUNE)
 * depositAll(ItemID.FIRE_RUNE)
 * <p>
 * World interactions:
 * click("Bank", "Use").onObject(ObjectID.BANK_CHEST)
 * use(ItemID.GRIMY_KWUARM).on(toolLeprechaun)
 * <p>
 * Widgets:
 * click(WidgetInfo.BANK_DEPOSIT_INVENTORY)
 * click("View tab").on(getTabWidget(6))
 * click("Activate").onWidget(Prayer.PIETY.getWidget())
 * <p>
 * Chat options:
 * x continueDialog()
 * x dialog("Something else...")
 * <p>
 * Spells:
 * cast(Spell.CRUMBLE_UNDEAD).onNPC(NpcID.ZOMBIFIED_SPAWN)
 * cast(Spell.HIGH_LEVEL_ALCHEMY).on(getAlchItem())
 * cast(Spell.HUMIDIFY)
 * cast("Dark Mage", Spell.NPC_CONTACT)
 */
@Slf4j
public class Interaction
{
	private final static String COL_BLANK = "<col=ffffff>";
	private final static String COL_ITEM = "<col=ff9040>";
	private final static String COL_OBJECT = "<col=ffff>";
	private final static String COL_NPC = "<col=ffff00>";
	private final static String COL_SPELL = "<col=00ff00>";
	private final static String COL_END = "</col>";

	private static boolean consumeNext = false;

	public static void consumeNext()
	{
		consumeNext = true;
	}

	private static final Set<Item> itemsInteracted = new HashSet<>();

	private WidgetInfo inventoryType = WidgetInfo.INVENTORY;

	private Spell spell;
	private StaticItem using;
	private Interactable interactable;
	private boolean isDialog = false;
	private boolean isNoop = false;

	private Predicate<String> actionFilter = null;
	private int actionIndex = -1;

	public static void tick()
	{
		itemsInteracted.clear();
	}

	private String join(int... ids)
	{
		return Ints.asList(ids).stream()
			.map(id -> "" + id).collect(Collectors.joining(", "));
	}

	private StaticItem getNextItem(StaticItem item)
	{
		return getNextItem(item.getItem().getId());
	}

	private StaticItem getNextItem(int... ids)
	{
		final var idList = Ints.asList(ids);
		final Predicate<Item> filter = item -> idList.contains(item.getId())
			&& !itemsInteracted.contains(item);

		Item item = null;
		switch (inventoryType)
		{
			case BANK_ITEM_CONTAINER:
				item = Bank.getFirst(filter);
				break;
			case INVENTORY:
				item = Inventory.getFirst(filter);
				break;
			case EQUIPMENT:
				item = Equipment.getFirst(filter);
				break;
			case BANK_INVENTORY_ITEMS_CONTAINER:
				item = Bank.Inventory.getFirst(filter);
				break;
		}

		if (item == null)
		{
			throw new IllegalArgumentException(
				"Failed to find item with ID: " + join(ids)
			);
		}

		return new StaticItem(item);
	}

	public Interaction from(WidgetInfo widgetInfo)
	{
		this.inventoryType = widgetInfo;
		return this;
	}

	public Interaction fromEquipment()
	{
		return from(WidgetInfo.EQUIPMENT);
	}

	public Interaction fromBank()
	{
		return from(WidgetInfo.BANK_ITEM_CONTAINER);
	}

	public Interaction fromBankInventory()
	{
		return from(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
	}

	public Interaction use(StaticItem item)
	{
		using = item;
		return this;
	}

	public Interaction use(Item item)
	{
		return use(new StaticItem(item));
	}

	public Interaction use(int... ids)
	{
		return use(getNextItem(ids));
	}

	public Interaction cast(Spell spell)
	{
		this.spell = spell;
		return this;
	}

	public Interaction cast(String action, Spell spell)
	{
		click(action);
		this.spell = spell;
		return this;
	}

	public Interaction click(Interactable interactable)
	{
		return click(0).on(interactable);
	}

	public Interaction click(int optionNumber)
	{
		this.actionIndex = optionNumber - 1;
		return this;
	}

	public Interaction click(String... options)
	{
		return click(Predicates.texts(options));
	}

	public Interaction click(Predicate<String> isOption)
	{
		this.actionFilter = isOption;
		return this;
	}

	public Interaction click(WidgetInfo widgetInfo)
	{
		return on(Widgets.get(widgetInfo));
	}

	public Interaction on(Interactable interactable)
	{
		this.interactable = interactable;
		return this;
	}

	public Interaction on(WidgetInfo widgetInfo)
	{
		return on(Widgets.get(widgetInfo));
	}

	public Interaction onObject(int... ids)
	{
		final var object = TileObjects.getNearest(ids);
		if (object == null)
		{
			throw new IllegalStateException("Failed to find object with ID: " + join(ids));
		}

		return on(object);
	}

	public Interaction onNPC(int... ids)
	{
		final var npc = NPCs.getNearest(ids);
		if (npc == null)
		{
			throw new IllegalStateException("Failed to find NPC with ID: " + join(ids));
		}

		return on(npc);
	}

	public Interaction onItem(int... ids)
	{
		return on(getNextItem(ids));
	}

	public Interaction equip(StaticItem item)
	{
		return click("Wield", "Wear").on(item);
	}

	public Interaction equip(Item item)
	{
		return click("Wield", "Wear").on(item);
	}

	public Interaction equip(int... ids)
	{
		return equip(getNextItem(ids));
	}

	public Interaction drop(StaticItem item)
	{
		return click("Drop", "Destroy").on(item);
	}

	public Interaction drop(Item item)
	{
		return click("Drop", "Destroy").on(item);
	}

	public Interaction drop(int... ids)
	{
		return drop(getNextItem(ids));
	}

	public Interaction remove(Item item)
	{
		return from(WidgetInfo.EQUIPMENT).click("Remove").on(item);
	}

	public Interaction remove(int... ids)
	{
		return fromEquipment().click("Remove").on(getNextItem(ids));
	}

	public Interaction withdraw(int n, Item item)
	{
		return fromBank().click("Withdraw-" + n).on(item);
	}

	public Interaction withdraw(int n, int... ids)
	{
		return fromBank().click("Withdraw-" + n).on(getNextItem(ids));
	}

	public Interaction withdrawX(Item item)
	{
		return fromBank().click(5).on(item);
	}

	public Interaction withdrawX(int... ids)
	{
		return fromBank().click(5).on(getNextItem(ids));
	}

	public Interaction withdrawAll(Item item)
	{
		return fromBank().click("Withdraw-All").on(item);
	}

	public Interaction withdrawAll(int... ids)
	{
		return fromBank().click("Withdraw-All").on(getNextItem(ids));
	}

	public Interaction deposit(int n, Item item)
	{
		return fromBankInventory().click("Deposit-" + n).on(item);
	}

	public Interaction deposit(int n, int... ids)
	{
		return fromBankInventory().click("Deposit-" + n).on(getNextItem(ids));
	}

	public Interaction depositX(Item item)
	{
		return fromBankInventory().click(6).on(item);
	}

	public Interaction depositX(int... ids)
	{
		return fromBankInventory().click(6).on(getNextItem(ids));
	}

	public Interaction depositAll(Item item)
	{
		return fromBankInventory().click("Deposit-All").on(item);
	}

	public Interaction depositAll(int... ids)
	{
		return fromBankInventory().click("Deposit-All").on(getNextItem(ids));
	}

	public Interaction dialog(Predicate<String> option)
	{
		isDialog = true;
		for (Widget widget : Dialog.getOptions())
		{
			if (option.test(widget.getText()))
			{
				return on(widget);
			}
		}

		throw new IllegalArgumentException("No dialog option found");
	}

	public Interaction dialog(String... option)
	{
		return dialog(Predicates.texts(option));
	}

	public Interaction continueDialog()
	{
		return dialog("Continue");
	}

	/**
	 * Execute using Devious API
	 */
	public void execute()
	{
		if (isNoop)
		{
			return;
		}

		if (using != null)
		{
			using.getItem().useOn(interactable);
			return;
		}

		if (spell != null)
		{
			if (interactable != null)
			{
				spell.castOn(interactable);
				return;
			}

			spell.cast();
			return;
		}

		if (interactable != null)
		{
			interactable.interact(getActionIndex());
			if (interactable instanceof StaticItem)
			{
				interactable = getNextItem((StaticItem) interactable);
				log.info("Next item slot: " + ((StaticItem) interactable).getItem().getSlot());
			}

			return;
		}

		throw new IllegalStateException("No execution strategy found for interaction");
	}

	/**
	 * Execute by forwarding click event
	 */
	public void execute(MenuOptionClicked event)
	{
		if (isNoop)
		{
			return;
		}

		if (consumeNext)
		{
			consumeNext = false;
			return;
		}

		if (using != null)
		{
			using.use();
		}

		if (spell != null)
		{
			Magic.selectSpell(spell);
		}

		event.setMenuOption(getOption());
		event.setMenuTarget(getTarget());
		event.setMenuAction(getAction());
		event.setId(getId());
		event.setParam0(getParam0());
		event.setParam1(getParam1());

		// move to the next item immediately if interacting
		// with an inventory item. this allows for quick dropping etc
		// TODO: only move if not stackable
		if (interactable instanceof StaticItem
			&& (inventoryType == WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER
			|| inventoryType == WidgetInfo.INVENTORY))
		{
			final var staticItem = (StaticItem) interactable;
			itemsInteracted.add(staticItem.getItem());
			log.info("Current item slot: " + staticItem.getItem().getSlot());
			try
			{
				interactable = getNextItem(staticItem);
				log.info("Next item slot: " + ((StaticItem) interactable).getItem().getSlot());
			}
			catch (IllegalArgumentException e)
			{
				log.info("Caught: setting noop");
				// nothing in the next slot - nothing else
				// for this interaction to do
				isNoop = true;
			}
		}
	}

	public int getActionIndex()
	{
		// actionIndex was set during construction
		if (actionIndex != -1)
		{
			return actionIndex;
		}

		// no index when using an item
		// or on dialogs
		if (using != null || isDialog)
		{
			return -1;
		}

		// casting a normal spell,
		// casting a spell on an interactable,
		// or casting a spell alternative
		if (spell != null)
		{
			// no index when using a spell on something
			if (interactable != null)
			{
				return -1;
			}

			// not an alternative - "cast" will always be #1
			if (actionFilter == null)
			{
				return 0;
			}

			// find the index of the alternative
			final var actions = Widgets.get(spell.getWidget()).getActions();
			if (actions == null)
			{
				throw new IllegalStateException("Spell widget does not have actions");
			}

			for (var i = 0; i < actions.length; i++)
			{
				if (actionFilter.test(actions[i]))
				{
					return i;
				}
			}

			throw new IllegalStateException("Could not find casting action index");
		}

		if (interactable != null)
		{
			if (actionFilter == null)
			{
				// use first option for widgets
				if (interactable instanceof Widget)
				{
					return 0;
				}

				throw new IllegalStateException("No action filter provided");
			}

			final var actions = interactable.getActions();
			for (var i = 0; i < actions.length; i++)
			{
				if (actionFilter.test(actions[i]))
				{
					return i;
				}
			}

			throw new IllegalStateException("No actions matched filter");
		}

		throw new IllegalStateException("No candidates for determining action index");
	}

	public String getOption()
	{
		if (isDialog)
		{
			return "Continue";
		}

		if (spell != null)
		{
			final var widget = Widgets.get(spell.getWidget());

			return actionFilter == null
				? "Cast"
				: Arrays.stream(Objects.requireNonNull(widget.getActions()))
				.filter(actionFilter)
				.findFirst().orElse("???");
		}

		if (interactable == null)
		{
			throw new IllegalStateException();
		}

		if (using != null)
		{
			return "Use";
		}

		if (interactable.getActions() == null)
		{
			throw new IllegalStateException("Interactable does not have actions");
		}

		if (actionIndex != -1)
		{
			return interactable.getActions()[actionIndex];
		}

		if (actionFilter == null)
		{
			return interactable.getActions()[0];
		}

		return Arrays.stream(interactable.getActions())
			.filter(actionFilter)
			.findFirst().orElse("???");
	}

	public String getTarget()
	{
		if (isDialog)
		{
			return "(" + ((Widget) interactable).getText() + ")";
		}

		if (interactable == null)
		{
			if (spell != null)
			{
				return COL_SPELL + Widgets.get(spell.getWidget()).getName();
			}

			throw new IllegalArgumentException();
		}

		var target = "";
		if (interactable instanceof NPC)
		{
			target = COL_NPC + ((NPC) interactable).getTransformedName();
		}

		if (interactable instanceof GameObject)
		{
			target = COL_OBJECT + ((GameObject) interactable).getName();
		}

		if (interactable instanceof TileObject)
		{
			target = COL_OBJECT + ((TileObject) interactable).getName();
		}

		if (interactable instanceof Widget)
		{
			target = COL_ITEM + ((Widget) interactable).getName();
		}

		if (interactable instanceof StaticItem)
		{
			target = COL_ITEM + ((StaticItem) interactable).getItem().getName();
		}

		if (using != null)
		{
			target = COL_ITEM + using.getItem().getName() + COL_END + COL_BLANK + " -> " + target;
		}

		if (spell != null)
		{
			target = COL_SPELL + Widgets.get(spell.getWidget()).getName() + COL_END + COL_BLANK + " -> " + target;
		}

		return target;
	}

	public MenuAction getAction()
	{
		final var index = getActionIndex();

		// special cases
		if (index == -1)
		{
			if (isDialog)
			{
				return MenuAction.WIDGET_CONTINUE;
			}

			if (interactable != null)
			{
				if (interactable instanceof NPC)
				{
					return MenuAction.WIDGET_TARGET_ON_NPC;
				}
				else if (interactable instanceof TileObject)
				{
					return MenuAction.WIDGET_TARGET_ON_GAME_OBJECT;
				}
				else if (interactable instanceof Player)
				{
					return MenuAction.WIDGET_TARGET_ON_PLAYER;
				}
				else if (interactable instanceof Widget || interactable instanceof StaticItem)
				{
					return MenuAction.WIDGET_TARGET_ON_WIDGET;
				}
			}

			throw new IllegalStateException("Invalid action index");
		}

		if (interactable instanceof NPC)
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

		if (interactable instanceof TileObject)
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

	public int getId()
	{
		if (interactable != null)
		{
			// object: object id
			if (interactable instanceof TileObject)
			{
				return ((TileObject) interactable).getId();
			}

			// npc: npc index
			if (interactable instanceof NPC)
			{
				return ((NPC) interactable).getIndex();
			}
		}

		// item / widget / spell / dialog: option id (1-indexed)
		final var index = getActionIndex();
		return index + 1;
	}

	public int getParam0()
	{
		// item: slot index (except equipment)
		// item -> item: 2nd item slot index
		if (interactable instanceof StaticItem)
		{
			if (inventoryType == WidgetInfo.EQUIPMENT)
			{
				return -1;
			}

			return ((StaticItem) interactable).getItem().getSlot();
		}

		// object: object scene coords
		if (interactable instanceof TileObject)
		{
			return getSceneLocation().getX();
		}

		// widget: index
		if (interactable instanceof Widget)
		{
			return ((Widget) interactable).getIndex();
		}

		// npc: 0
		if (interactable instanceof NPC)
		{
			return 0;
		}

		// spell: -1
		if (spell != null)
		{
			return -1;
		}

		// default 0
		return 0;
	}

	public int getParam1()
	{
		// object: object scene coords
		if (interactable instanceof TileObject)
		{
			return getSceneLocation().getY();
		}

		// widget: id / parent id
		if (interactable instanceof Widget)
		{
			return ((Widget) interactable).getId();
		}

		// single spell: widget id
		if (spell != null && interactable == null)
		{
			return spell.getWidget().getPackedId();
		}

		if (interactable instanceof StaticItem)
		{
			return ((StaticItem) interactable).getWidgetId();
		}

		// npc: 0
		return 0;
	}

	public Point getSceneLocation()
	{
		if (interactable instanceof GameObject)
		{
			return ((GameObject) interactable).getSceneMinLocation();
		}

		if (interactable instanceof TileObject)
		{
			final var local = ((TileObject) interactable).getLocalLocation();
			return new Point(local.getSceneX(), local.getSceneY());
		}

		return null;
	}
}
