package com.yfletch.occore.v2.interaction;

import com.google.common.primitives.Ints;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
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
@Getter(AccessLevel.PACKAGE)
public class Interaction
{
	private final static String COL_BLANK = "<col=ffffff>";
	private final static String COL_ITEM = "<col=ff9040>";
	private final static String COL_OBJECT = "<col=ffff>";
	private final static String COL_NPC = "<col=ffff00>";
	private final static String COL_SPELL = "<col=00ff00>";
	private final static String COL_END = "</col>";

	private WidgetInfo inventoryType = WidgetInfo.INVENTORY;

	private Spell spell;
	private StaticItem using;
	private Interactable interactable;
	private boolean isDialog = false;

	private Predicate<String> optionFilter = null;
	private int optionIndex = -1;

	@Setter
	@Accessors(fluent = true,
			   chain = true)
	private int repeat = 1;

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

	@Getter
	private final InteractionExecutor executor;

	public Interaction()
	{
		executor = new InteractionExecutor(this);
	}

	private String join(int... ids)
	{
		return Ints.asList(ids).stream()
			.map(id -> "" + id).collect(Collectors.joining(", "));
	}

	private StaticItem getNextItem(int... ids)
	{
		final var idList = Ints.asList(ids);
		final Predicate<Item> filter = item -> idList.contains(item.getId())
			&& !InteractionExecutor.getItemsInteracted().contains(item);

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

	/**
	 * Specify the inventory for this interaction to search for items
	 * within. Should be called before any item references in the chain.
	 * <p>
	 * e.g `from(<bank_inventory>).eat(<item>)`
	 * NOT `eat(<item>).from(<bank_inventory>)`
	 */
	public Interaction from(WidgetInfo widgetInfo)
	{
		this.inventoryType = widgetInfo;
		return this;
	}

	/**
	 * Specify that this interaction in within the equipment inventory.
	 */
	public Interaction fromEquipment()
	{
		return from(WidgetInfo.EQUIPMENT);
	}

	/**
	 * Specify that this interaction in within the bank.
	 */
	public Interaction fromBank()
	{
		return from(WidgetInfo.BANK_ITEM_CONTAINER);
	}

	/**
	 * Specify that this interaction in within the bank inventory.
	 * Required for interacting with the inventory while the bank is open,
	 * as it's a slightly different widget.
	 */
	public Interaction fromBankInventory()
	{
		return from(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
	}

	/**
	 * "Use" an item on a target.
	 * <p>
	 * Should be followed with an "on" call to specify
	 * the target.
	 * <p>
	 * e.g. `use(ranarrSeed)`
	 */
	public Interaction use(StaticItem item)
	{
		using = item;
		return this;
	}

	/**
	 * "Use" an item on a target.
	 * <p>
	 * Should be followed with an "on" call to specify
	 * the target.
	 * <p>
	 * e.g. `use(ranarrSeed)`
	 */
	public Interaction use(Item item)
	{
		return use(new StaticItem(item));
	}

	/**
	 * "Use" the first item matching the id(s) in the current inventory
	 * on a target.
	 * <p>
	 * Should be followed with an "on" call to specify
	 * the target.
	 * <p>
	 * e.g. `use(ItemID.RANARR_SEED)`
	 */
	public Interaction use(int... ids)
	{
		return use(getNextItem(ids));
	}

	/**
	 * Cast a spell.
	 * <p>
	 * Follow with an "on" call to cast this spell
	 * on another item, object or NPC.
	 * <p>
	 * e.g. `cast(Spell.HUMIDIFY)`
	 */
	public Interaction cast(Spell spell)
	{
		this.spell = spell;
		return this;
	}

	/**
	 * Cast a spell with the specified option.
	 * <p>
	 * Follow with an "on" call to cast this spell
	 * on another item, object or NPC.
	 * <p>
	 * e.g. `cast("Dark Mage", Spell.NPC_CONTACT)`
	 */
	public Interaction cast(String action, Spell spell)
	{
		click(action);
		this.spell = spell;
		return this;
	}

	/**
	 * Click the first option on this interactable.
	 * <p>
	 * This one is a shortcut primarily for widgets. It
	 * doesn't need to be followed with an "on" call.
	 * <p>
	 * e.g. `click(depositInventory)`
	 */
	public Interaction click(Interactable interactable)
	{
		return click(0).on(interactable);
	}

	/**
	 * Click the option at this id in the target menu.
	 * <p>
	 * Should be followed by an "on" call to specify
	 * the target. Remember this is one-based, so the
	 * first option is option 1.
	 * <p>
	 * e.g. `click(1).onItem(ItemID.GRANITE_500G)`
	 */
	public Interaction click(int optionNumber)
	{
		this.optionIndex = optionNumber - 1;
		return this;
	}

	/**
	 * Click the first matching option in the target menu.
	 * <p>
	 * Should be followed by an "on" call to specify
	 * the target.
	 * <p>
	 * e.g. `click("Bury").onItem(ItemID.DRAGON_BONES)`
	 */
	public Interaction click(String... options)
	{
		return click(Predicates.texts(options));
	}

	/**
	 * Click the first option matching the predicate in
	 * the target menu.
	 * <p>
	 * Should be followed by an "on" call to specify
	 * the target.
	 * <p>
	 * e.g. `click(o -> o.contains("Teleport")).on(nextJewellery)`
	 */
	public Interaction click(Predicate<String> isOption)
	{
		this.optionFilter = isOption;
		return this;
	}

	/**
	 * Click the first option on this widget.
	 * <p>
	 * This one is a shortcut for widgets. It doesn't
	 * need to be followed with an "on" call.
	 * <p>
	 * e.g. `click(WidgetInfo.MINIMAP_SPEC_CLICKBOX)`
	 */
	public Interaction click(WidgetInfo widgetInfo)
	{
		return on(Widgets.get(widgetInfo));
	}

	/**
	 * Set the target for this interaction.
	 * <p>
	 * e.g. `click("Attack").on(context.getNextGoblin())`
	 */
	public Interaction on(Interactable interactable)
	{
		this.interactable = interactable;
		return this;
	}

	/**
	 * Set the target widget for this interaction.
	 * <p>
	 * e.g. `click("Activate").on(WidgetInfo.MINIMAP_PRAYER_CLICKBOX)`
	 */
	public Interaction on(WidgetInfo widgetInfo)
	{
		return on(Widgets.get(widgetInfo));
	}

	/**
	 * Set the target to the first NPC, object, or item matching the predicate.
	 * <p>
	 * Does a lot of searching - may be slow. Consider other methods if target
	 * is known before runtime.
	 * <p>
	 * e.g. `click("Use", "Bank").on(name -> name.contains("Bank"))`
	 */
	public Interaction on(Predicate<String> predicate)
	{
		// search NPCs
		final var npc = NPCs.getNearest(o -> predicate.test(o.getName()));
		if (npc != null)
		{
			return on(npc);
		}

		final var object = TileObjects.getNearest(o -> predicate.test(o.getName()));
		if (object != null)
		{
			return on(object);
		}

		final var item = Inventory.getFirst(o -> predicate.test(o.getName()));
		if (item != null)
		{
			return on(item);
		}

		// do nothing - error will be found and propagated
		// later on when getTarget() is called
		return this;
	}

	/**
	 * Set the target to the first NPC, object, or item with a name matching
	 * one of the given options.
	 * <p>
	 * Does a lot of searching - may be slow. Consider other methods if target
	 * is known before runtime.
	 * <p>
	 * e.g. `click("Use").on("Bank chest")`
	 */
	public Interaction on(String... name)
	{
		return on(Predicates.texts(name));
	}

	/**
	 * Set the target object for this interaction by ID.
	 * <p>
	 * e.g. `click("Mine").on(ObjectID.RUNE_ROCK)`
	 */
	public Interaction onObject(int... ids)
	{
		final var object = TileObjects.getNearest(ids);
		if (object == null)
		{
			throw new IllegalStateException("Failed to find object with ID: " + join(ids));
		}

		return on(object);
	}

	/**
	 * Set the target NPC for this interaction by ID.
	 * <p>
	 * e.g. `click("Attack").on(NpcID.ZEBAK)`
	 */
	public Interaction onNPC(int... ids)
	{
		final var npc = NPCs.getNearest(ids);
		if (npc == null)
		{
			throw new IllegalStateException("Failed to find NPC with ID: " + join(ids));
		}

		return on(npc);
	}

	/**
	 * Set the target item for this interaction by ID.
	 * <p>
	 * e.g. `click("Clean").on(ObjectID.GRIMY_KWUARM)`
	 */
	public Interaction onItem(int... ids)
	{
		return on(getNextItem(ids));
	}

	/**
	 * Equip the target item.
	 * <p>
	 * e.g. `equip(meleeWeapon)`
	 */
	public Interaction equip(StaticItem item)
	{
		return click("Wield", "Wear").on(item);
	}

	/**
	 * Equip the target item.
	 * <p>
	 * e.g. `equip(meleeWeapon)`
	 */
	public Interaction equip(Item item)
	{
		return click("Wield", "Wear").on(item);
	}

	/**
	 * Equip the first target item matching the ids given.
	 * <p>
	 * e.g. `equip(ItemID.BUNNY_TOP)`
	 */
	public Interaction equip(int... ids)
	{
		return equip(getNextItem(ids));
	}

	/**
	 * Drop the target item.
	 * <p>
	 * e.g. `drop(nextGranite)`
	 */
	public Interaction drop(StaticItem item)
	{
		return click("Drop", "Destroy").on(item);
	}

	/**
	 * Drop the target item.
	 * <p>
	 * e.g. `drop(nextGranite)`
	 */
	public Interaction drop(Item item)
	{
		return click("Drop", "Destroy").on(item);
	}

	/**
	 * Drop the first target item matching the ids given.
	 * <p>
	 * e.g. `drop(ItemID.LEAPING_STURGEON)`
	 */
	public Interaction drop(int... ids)
	{
		return drop(getNextItem(ids));
	}

	/**
	 * Unequip the target item.
	 * <p>
	 * e.g. `remove(meleeChestplate)`
	 */
	public Interaction remove(Item item)
	{
		return from(WidgetInfo.EQUIPMENT).click("Remove").on(item);
	}

	/**
	 * Unequip the first target item matching the ids given.
	 * <p>
	 * e.g. `unequip(ItemID.TORVA_PLATEBODY)`
	 */
	public Interaction remove(int... ids)
	{
		return fromEquipment().click("Remove").on(getNextItem(ids));
	}

	/**
	 * Withdraw the specified amount (1, 5, or 10) of the item
	 * from the bank.
	 * <p>
	 * Can use the customised option, but can't set it.
	 * <p>
	 * e.g. `withdraw(1, ItemID.PURE_ESSENCE)`
	 */
	public Interaction withdraw(int n, Item item)
	{
		return fromBank().click("Withdraw-" + n).on(item);
	}

	/**
	 * Withdraw the specified amount (1, 5, or 10) of the first
	 * item matching the ids given from the bank.
	 * <p>
	 * Can use the customised option, but can't set it.
	 * <p>
	 * e.g. `withdraw(1, ItemID.PURE_ESSENCE, ItemID.RUNE_ESSENCE)`
	 */
	public Interaction withdraw(int n, int... ids)
	{
		return fromBank().click("Withdraw-" + n).on(getNextItem(ids));
	}

	/**
	 * Withdraw the current custom amount of the item from
	 * the bank.
	 * <p>
	 * e.g. `withdrawX(primaryItem)`
	 */
	public Interaction withdrawX(Item item)
	{
		return fromBank().click(5).on(item);
	}

	/**
	 * Withdraw the current custom amount of the first item
	 * matching the ids given from the bank.
	 * <p>
	 * e.g. `withdrawX(ItemID.BOW_STRING)`
	 */
	public Interaction withdrawX(int... ids)
	{
		return fromBank().click(5).on(getNextItem(ids));
	}

	/**
	 * Withdraw all of the item from the bank.
	 * <p>
	 * e.g. `withdrawAll(primaryItem)`
	 */
	public Interaction withdrawAll(Item item)
	{
		return fromBank().click("Withdraw-All").on(item);
	}

	/**
	 * Withdraw all of the first item matching the
	 * ids given from the bank.
	 * <p>
	 * e.g. `withdrawAll(ItemID.PURE_ESSENCE)`
	 */
	public Interaction withdrawAll(int... ids)
	{
		return fromBank().click("Withdraw-All").on(getNextItem(ids));
	}

	/**
	 * Deposit the specified amount (1, 5, or 10) of the item
	 * into the bank.
	 * <p>
	 * Can use the customised option, but can't set it.
	 * <p>
	 * e.g. `deposit(1, productItem)`
	 */
	public Interaction deposit(int n, Item item)
	{
		return fromBankInventory().click("Deposit-" + n).on(item);
	}

	/**
	 * Deposit the specified amount (1, 5, or 10) of the first
	 * item matching the ids given into the bank.
	 * <p>
	 * Can use the customised option, but can't set it.
	 * <p>
	 * e.g. `deposit(1, ItemID.SQUIRK_JUICE)`
	 */
	public Interaction deposit(int n, int... ids)
	{
		return fromBankInventory().click("Deposit-" + n).on(getNextItem(ids));
	}

	/**
	 * Deposit the current custom amount of the item into
	 * the bank.
	 * <p>
	 * e.g. `depositX(primaryItem)`
	 */
	public Interaction depositX(Item item)
	{
		return fromBankInventory().click(6).on(item);
	}

	/**
	 * Deposit the current custom amount of the first item
	 * matching the ids given into the bank.
	 * <p>
	 * e.g. `depositX(ItemID.MAGIC_SHORTBOW)`
	 */
	public Interaction depositX(int... ids)
	{
		return fromBankInventory().click(6).on(getNextItem(ids));
	}

	/**
	 * Deposit all of the item into the bank.
	 * <p>
	 * e.g. `withdrawAll(primaryItem)`
	 */
	public Interaction depositAll(Item item)
	{
		return fromBankInventory().click("Deposit-All").on(item);
	}

	/**
	 * Deposit all of the first item matching the
	 * ids given into the bank.
	 * <p>
	 * e.g. `depositAll(ItemID.SQUIRK_JUICE)`
	 */
	public Interaction depositAll(int... ids)
	{
		return fromBankInventory().click("Deposit-All").on(getNextItem(ids));
	}

	/**
	 * Click the first dialog option matching the predicate given.
	 * <p>
	 * No "click" call is required.
	 * <p>
	 * e.g. `dialog(o -> o.contains("bank notes"))`
	 */
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

	/**
	 * Click the first dialog option matching one of the
	 * options given.
	 * <p>
	 * No "click" call is required.
	 * <p>
	 * e.g. `dialog("Something else...", "Continue")`
	 */
	public Interaction dialog(String... option)
	{
		return dialog(Predicates.texts(option));
	}

	/**
	 * Continue through player or NPC dialog.
	 * <p>
	 * No "click" call is required.
	 */
	public Interaction continueDialog()
	{
		return dialog("Continue");
	}

	boolean isSpellCast()
	{
		return spell != null;
	}

	boolean hasTarget()
	{
		return interactable != null;
	}

	boolean isUseItem()
	{
		return using != null;
	}

	boolean isWidgetTarget()
	{
		return interactable instanceof Widget;
	}

	boolean isItemTarget()
	{
		return interactable instanceof StaticItem;
	}

	boolean isTileObjectTarget()
	{
		return interactable instanceof TileObject;
	}

	boolean isGameObjectTarget()
	{
		return interactable instanceof GameObject;
	}

	boolean isNpcTarget()
	{
		return interactable instanceof NPC;
	}

	Widget getWidget()
	{
		if (!isWidgetTarget())
		{
			throw new IllegalStateException("Target is not a widget");
		}

		return (Widget) interactable;
	}

	StaticItem getItem()
	{
		if (!isItemTarget())
		{
			throw new IllegalStateException("Target is not an item");
		}

		return (StaticItem) interactable;
	}

	TileObject getTileObject()
	{
		if (!isTileObjectTarget())
		{
			throw new IllegalStateException("Target is not a tile object");
		}

		return (TileObject) interactable;
	}

	GameObject getGameObject()
	{
		if (!isGameObjectTarget())
		{
			throw new IllegalStateException("Target is not a game object");
		}

		return (GameObject) interactable;
	}

	NPC getNpc()
	{
		if (!isNpcTarget())
		{
			throw new IllegalStateException("Target is not an NPC");
		}

		return (NPC) interactable;
	}
}
