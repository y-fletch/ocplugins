package com.yfletch.occore.v2.interaction;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import net.runelite.api.Item;
import net.runelite.api.widgets.WidgetInfo;
import net.unethicalite.api.EntityNameable;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.Spell;
import net.unethicalite.api.widgets.Widgets;

/**
 *
 */
public class Entities
{
	private static <T extends EntityNameable> Predicate<T> nameMatches(Predicate<String> predicate)
	{
		return e -> predicate.test(e.getName());
	}

	private static Item getNextItem(Item.Type type, int... ids)
	{
		switch (type)
		{
			case EQUIPMENT:
				return Equipment.getFirst(ids);
			case BANK:
				return Bank.getFirst(ids);
			case BANK_INVENTORY:
				return Bank.Inventory.getFirst(ids);
			case INVENTORY:
			default:
				return Inventory.getFirst(ids);
		}
	}

	private static Item getNextItem(Item.Type type, Predicate<String> predicate)
	{
		switch (type)
		{
			case EQUIPMENT:
				return Equipment.getFirst(nameMatches(predicate));
			case BANK:
				return Bank.getFirst(nameMatches(predicate));
			case BANK_INVENTORY:
				return Bank.Inventory.getFirst(nameMatches(predicate));
			case INVENTORY:
			default:
				return Inventory.getFirst(nameMatches(predicate));
		}
	}

	/**
	 * Shortcut for lowercase contains
	 */
	public static Predicate<String> containing(String text)
	{
		return s -> s.toLowerCase().contains(text);
	}

	/**
	 * Wrap an interactable so execution of its actions can
	 * be controlled by the plugin runner.
	 */
	public static DeferredInteractable of(Interactable interactable)
	{
		return new DeferredInteractable(interactable);
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
	public static DeferredInteractable npc(Predicate<String> predicate)
	{
		return of(NPCs.getNearest(nameMatches(predicate)));
	}

	/**
	 * Get an NPC
	 */
	public static DeferredInteractable npc(String... names)
	{
		return npc(Predicates.texts(names));
	}

	/**
	 * Get an NPC
	 */
	public static DeferredInteractable npc(int... ids)
	{
		return of(NPCs.getNearest(ids));
	}

	/**
	 * Get a TileObject
	 */
	public static DeferredInteractable object(Predicate<String> predicate)
	{
		return of(TileObjects.getNearest(nameMatches(predicate)));
	}

	/**
	 * Get a TileObject
	 */
	public static DeferredInteractable object(String... names)
	{
		return object(Predicates.texts(names));
	}

	/**
	 * Get a TileObject
	 */
	public static DeferredInteractable object(int... ids)
	{
		return of(TileObjects.getNearest(ids));
	}

	/**
	 * Get a widget
	 * <p>
	 * Note: this also tests widget actions for the predicate
	 */
	public static DeferredInteractable widget(Predicate<String> predicate)
	{
		return of(
			Widgets.query()
				.filter(
					w -> predicate.test(w.getName())
						|| predicate.test(w.getText())
						|| w.getActions() != null
						&& Arrays.stream(w.getActions()).anyMatch(predicate)
				)
				.results()
				.first()
		);
	}

	/**
	 * Get a widget
	 * <p>
	 * Note: this also searches widget actions for the target strings
	 */
	public static DeferredInteractable widget(String... names)
	{
		return widget(Predicates.texts(names));
	}

	/**
	 * Get a widget
	 */
	public static DeferredInteractable widget(WidgetInfo widgetInfo)
	{
		return of(Widgets.get(widgetInfo));
	}

	/**
	 * Get a spell
	 */
	public static DeferredInteractableSpell spell(Spell spell)
	{
		return new DeferredInteractableSpell(spell);
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
		return item(type, Predicates.texts(names));
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
	 * Get an NPC or object
	 */
	public static DeferredInteractable entity(Predicate<String> predicate)
	{
		return of(
			Optional
				.<Interactable>ofNullable(NPCs.getNearest(nameMatches(predicate)))
				.orElse(TileObjects.getNearest(nameMatches(predicate)))
		);
	}
}
