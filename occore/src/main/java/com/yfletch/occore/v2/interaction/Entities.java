package com.yfletch.occore.v2.interaction;

import static com.yfletch.occore.v2.util.Util.getSpellByName;
import static com.yfletch.occore.v2.util.Util.matching;
import static com.yfletch.occore.v2.util.Util.nameMatching;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Getter;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.unethicalite.api.EntityNameable;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.SceneEntity;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileItems;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.Spell;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;

/**
 *
 */
public class Entities
{
	@Getter
	private final static Set<Item> interactedItems = new HashSet<>();

	public static void clearInteracted()
	{
		interactedItems.clear();
	}

	public static void markInteracted(Item item)
	{
		interactedItems.add(item);
	}

	private static <T extends EntityNameable> Predicate<T> nameMatches(Predicate<String> predicate)
	{
		return e -> predicate.test(e.getName());
	}

	private static Item getNextItem(Item.Type type, int... ids)
	{
		if (Bank.isOpen() && type == Item.Type.INVENTORY)
		{
			type = Item.Type.BANK_INVENTORY;
		}

		final Predicate<Item> filter = (item -> Predicates.ids(ids).test(item)
			&& !interactedItems.contains(item));

		switch (type)
		{
			case EQUIPMENT:
				return Equipment.getFirst(filter);
			case BANK:
				return Bank.getFirst(filter);
			case BANK_INVENTORY:
				return Bank.Inventory.getFirst(filter);
			case INVENTORY:
			default:
				return Inventory.getFirst(filter);
		}
	}

	private static Item getNextItem(Item.Type type, Predicate<String> predicate)
	{
		if (Bank.isOpen() && type == Item.Type.INVENTORY)
		{
			type = Item.Type.BANK_INVENTORY;
		}

		final Predicate<Item> filter = (item -> nameMatches(predicate).test(item)
			&& !interactedItems.contains(item));

		switch (type)
		{
			case EQUIPMENT:
				return Equipment.getFirst(filter);
			case BANK:
				return Bank.getFirst(filter);
			case BANK_INVENTORY:
				return Bank.Inventory.getFirst(filter);
			case INVENTORY:
			default:
				return Inventory.getFirst(filter);
		}
	}

	private static Item getLastItem(Predicate<String> predicate)
	{
		final var all = Inventory.getAll(nameMatches(predicate));
		return all.get(all.size() - 1);
	}

	private static Item getLastItem(int... ids)
	{
		final var all = Inventory.getAll(ids);
		return all.get(all.size() - 1);
	}

	private static List<Widget> getAllWidgets(int groupId)
	{
		return Widgets.get(groupId).stream().flatMap(w -> getFlatChildren(w).stream())
			.collect(Collectors.toList());
	}

	private static List<Widget> getFlatChildren(Widget widget)
	{
		final var list = new ArrayList<Widget>();
		list.add(widget);
		if (widget.getChildren() != null)
		{
			list.addAll(
				Arrays.stream(widget.getChildren())
					.flatMap(w -> getFlatChildren(w).stream())
					.collect(Collectors.toList())
			);
		}

		return list;
	}

	/**
	 * Wrap an interactable so execution of its actions can
	 * be controlled by the plugin runner.
	 */
	public static <T extends Interactable> DeferredInteractable<T> of(T interactable)
	{
		return new DeferredInteractable<>(interactable);
	}

	/**
	 * Wrap an interactable so execution of its actions can
	 * be controlled by the plugin runner.
	 */
	public static DeferredInteractableItem of(Item item)
	{
		return new DeferredInteractableItem(item);
	}

	/**
	 * Wrap an interactable so execution of its actions can
	 * be controlled by the plugin runner.
	 */
	public static DeferredInteractableItem of(Item item, Item.Type type)
	{
		return new DeferredInteractableItem(item, type);
	}

	/**
	 * Get an NPC
	 */
	public static DeferredInteractable<NPC> npc(Predicate<String> predicate)
	{
		return of(NPCs.getNearest(nameMatches(predicate)));
	}

	/**
	 * Get an NPC
	 */
	public static DeferredInteractable<NPC> npc(String... names)
	{
		return npc(matching(names));
	}

	/**
	 * Get an NPC
	 */
	public static DeferredInteractable<NPC> npc(int... ids)
	{
		return of(NPCs.getNearest(ids));
	}

	/**
	 * Get a TileObject
	 */
	public static DeferredInteractable<TileObject> object(Predicate<String> predicate)
	{
		return of(TileObjects.getNearest(nameMatches(predicate)));
	}

	/**
	 * Get a TileObject
	 */
	public static DeferredInteractable<TileObject> object(String... names)
	{
		return object(matching(names));
	}

	/**
	 * Get a TileObject
	 */
	public static DeferredInteractable<TileObject> object(int... ids)
	{
		return of(TileObjects.getNearest(ids));
	}

	/**
	 * Get a TileItem
	 */
	public static DeferredInteractable<TileItem> tileItem(Predicate<String> predicate)
	{
		return of(TileItems.getNearest(nameMatches(predicate)));
	}

	/**
	 * Get a TileItem
	 */
	public static DeferredInteractable<TileItem> tileItem(String... names)
	{
		return tileItem(matching(names));
	}

	/**
	 * Get a TileItem
	 */
	public static DeferredInteractable<TileItem> tileItem(int... ids)
	{
		return of(TileItems.getNearest(ids));
	}

	/**
	 * Get a widget
	 * <p>
	 * If this fails to find a widget, a groupId is probably also required
	 * with Entities.widget(groupId, predicate)
	 * <p>
	 * Note: this also tests widget actions for the predicate
	 */
	public static DeferredInteractable<Widget> widget(Predicate<String> predicate)
	{
		return of(
			Arrays.stream(Static.getClient().getWidgets())
				.filter(Objects::nonNull)
				.flatMap(Arrays::stream)
				.filter(
					w -> predicate.test(w.getName())
						|| predicate.test(w.getText())
						|| w.getActions() != null
						&& Arrays.stream(w.getActions()).anyMatch(predicate)
				)
				.findFirst().orElse(null)
		);
	}

	/**
	 * Get a widget
	 * <p>
	 * If this fails to find a widget, a groupId is probably also required
	 * with Entities.widget(groupId, predicate)
	 * <p>
	 * Note: this also searches widget actions for the target strings
	 */
	public static DeferredInteractable<Widget> widget(String... names)
	{
		return widget(matching(names));
	}

	/**
	 * Get a widget
	 * <p>
	 * Note: this also tests widget actions for the predicate
	 */
	public static DeferredInteractable<Widget> widget(int groupId, Predicate<String> predicate)
	{
		return of(
			getAllWidgets(groupId).stream()
				.filter(
					w -> predicate.test(w.getName())
						|| predicate.test(w.getText())
						|| w.getActions() != null
						&& Arrays.stream(w.getActions()).anyMatch(predicate)
				)
				.findFirst().orElse(null)
		);
	}

	/**
	 * Get a widget
	 * <p>
	 * Note: this also searches widget actions for the target strings
	 */
	public static DeferredInteractable<Widget> widget(int groupId, String... names)
	{
		return widget(groupId, matching(names));
	}

	/**
	 * Get a widget
	 */
	public static DeferredInteractable<Widget> widget(WidgetInfo widgetInfo)
	{
		return of(Widgets.get(widgetInfo));
	}

	/**
	 * Get a dialog option
	 */
	public static DeferredInteractable<Widget> dialog(Predicate<String> predicate)
	{
		final var widget = Dialog.getOptions().stream()
			.filter(w -> predicate.test(w.getText()))
			.findFirst().orElse(null);
		return of(widget);
	}

	/**
	 * Get a dialog option
	 */
	public static DeferredInteractable<Widget> dialog(String... names)
	{
		return dialog(matching(names));
	}

	/**
	 * Get a dialog option
	 */
	public static DeferredInteractable<Widget> continueDialog()
	{
		return widget("Click here to continue");
	}

	/**
	 * Get a spell
	 */
	public static DeferredInteractableSpell spell(Spell spell)
	{
		return new DeferredInteractableSpell(spell);
	}

	/**
	 * Get a spell by name
	 */
	public static DeferredInteractableSpell spell(String spellName)
	{
		return new DeferredInteractableSpell(getSpellByName(spellName));
	}

	/**
	 * Get an item
	 */
	public static DeferredInteractableItem item(Item.Type type, Predicate<String> predicate)
	{
		return of(getNextItem(type, predicate));
	}

	/**
	 * Get an item
	 */
	public static DeferredInteractableItem item(Predicate<String> predicate)
	{
		return item(Item.Type.INVENTORY, predicate);
	}

	/**
	 * Get an item
	 */
	public static DeferredInteractableItem item(Item.Type type, String... names)
	{
		return item(type, matching(names));
	}

	/**
	 * Get an item
	 */
	public static DeferredInteractableItem item(String... names)
	{
		return item(Item.Type.INVENTORY, names);
	}

	/**
	 * Get an item
	 */
	public static DeferredInteractableItem item(Item.Type type, int... ids)
	{
		return of(getNextItem(type, ids));
	}

	/**
	 * Get an item
	 */
	public static DeferredInteractableItem item(int... ids)
	{
		return item(Item.Type.INVENTORY, ids);
	}

	/**
	 * Get the last of this item in the inventory
	 */
	public static DeferredInteractableItem lastItem(Predicate<String> predicate)
	{
		return of(getLastItem(predicate));
	}

	/**
	 * Get the last of this item in the inventory
	 */
	public static DeferredInteractableItem lastItem(String... names)
	{
		return lastItem(matching(names));
	}

	/**
	 * Get the last of this item in the inventory
	 */
	public static DeferredInteractableItem lastItem(int... ids)
	{
		return of(getLastItem(ids));
	}

	/**
	 * Get an equipped item
	 */
	public static DeferredInteractableItem equipment(Predicate<String> predicate)
	{
		return item(Item.Type.EQUIPMENT, predicate);
	}

	/**
	 * Get an equipped item
	 */
	public static DeferredInteractableItem equipment(String... names)
	{
		return item(Item.Type.EQUIPMENT, names);
	}

	/**
	 * Get an equipped item
	 */
	public static DeferredInteractableItem equipment(int... ids)
	{
		return item(Item.Type.EQUIPMENT, ids);
	}

	/**
	 * Get a banked item
	 */
	public static DeferredInteractableItem banked(Predicate<String> predicate)
	{
		return item(Item.Type.BANK, predicate);
	}

	/**
	 * Get a banked item
	 */
	public static DeferredInteractableItem banked(String... names)
	{
		return item(Item.Type.BANK, names);
	}

	/**
	 * Get a banked item
	 */
	public static DeferredInteractableItem banked(int... ids)
	{
		return item(Item.Type.BANK, ids);
	}

	/**
	 * Get an NPC or object
	 */
	public static DeferredInteractable<?> entity(Predicate<SceneEntity> predicate)
	{
		return of(
			Optional
				.<Interactable>ofNullable(NPCs.getNearest(predicate::test))
				.orElse(TileObjects.getNearest(predicate::test))
		);
	}

	/**
	 * Get an NPC or object
	 */
	public static DeferredInteractable<?> entity(String... names)
	{
		return entity(nameMatching(names));
	}
}
