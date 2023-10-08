package com.yfletch.occore.v2.rule;

import com.yfletch.occore.v2.CoreContext;
import com.yfletch.occore.v2.interaction.DeferredInteractable;
import com.yfletch.occore.v2.util.TextColor;
import static com.yfletch.occore.v2.util.Util.formatList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldArea;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.Spell;
import net.unethicalite.client.Static;
import org.apache.commons.text.WordUtils;

public class RequirementRule<TContext extends CoreContext> implements Rule<TContext>
{
	private final static String MUST_HAVE_ITEM = TextColor.WHITE + "Must have " + TextColor.ITEM;

	@Setter
	@Getter
	@Accessors(fluent = true)
	private String name;

	// if any requirement evaluates to false,
	// then the key (error) will be displayed
	@Getter
	private final Map<String, Predicate<TContext>> requirements = new HashMap<>();

	/**
	 * Specify when these requirements should apply. If this evaluates to false,
	 * no requirements will be checked in this rule.
	 */
	@Setter
	@Getter
	@Accessors(fluent = true,
			   chain = true)
	private Predicate<TContext> when;

	private String getItemName(int id)
	{
		return Static.getItemManager().getItemComposition(id).getName();
	}

	/**
	 * Display the error if the predicate evaluates FALSE
	 */
	public RequirementRule<TContext> must(Predicate<TContext> predicate, String error)
	{
		requirements.put(TextColor.WHITE + error, predicate);
		return this;
	}

	/**
	 * Display the error if the predicate evaluates to TRUE
	 */
	public RequirementRule<TContext> mustNot(Predicate<TContext> predicate, String error)
	{
		requirements.put(TextColor.WHITE + error, predicate.negate());
		return this;
	}

	/**
	 * Require all items to be in the player's inventory, bank
	 * or equipment
	 */
	public RequirementRule<TContext> mustHave(int... ids)
	{
		for (final var id : ids)
		{
			final var name = getItemName(id);
			requirements.put(
				MUST_HAVE_ITEM + name,
				c -> Inventory.contains(id) || Bank.contains(id) || Equipment.contains(id)
			);
		}

		return this;
	}

	/**
	 * Require all items to be in the player's inventory, bank
	 * or equipment
	 * <p>
	 * Warning: names are case-sensitive
	 */
	public RequirementRule<TContext> mustHave(Predicate<Item> predicate, String name)
	{
		requirements.put(
			MUST_HAVE_ITEM + name,
			c -> Inventory.contains(predicate) || Bank.contains(predicate) || Equipment.contains(predicate)
		);

		return this;
	}

	/**
	 * Require all items to be in the player's inventory, bank
	 * or equipment
	 * <p>
	 * Warning: names are case-sensitive
	 */
	public RequirementRule<TContext> mustHave(String... names)
	{
		for (final var name : names)
		{
			requirements.put(
				MUST_HAVE_ITEM + name,
				c -> Inventory.contains(name) || Bank.contains(name) || Equipment.contains(name)
			);
		}

		return this;
	}

	/**
	 * Require all items to be in the player's inventory
	 */
	public RequirementRule<TContext> mustHaveInInventory(int... ids)
	{
		for (final var id : ids)
		{
			final var name = getItemName(id);
			requirements.put(
				MUST_HAVE_ITEM + name + TextColor.WHITE + " in inventory",
				c -> Inventory.contains(id)
			);
		}

		return this;
	}

	/**
	 * Require all items to be in the player's inventory.
	 * <p>
	 * Warning: names are case-sensitive
	 */
	public RequirementRule<TContext> mustHaveInInventory(String... names)
	{
		for (final var name : names)
		{
			requirements.put(
				MUST_HAVE_ITEM + name + TextColor.WHITE + " in inventory",
				c -> Inventory.contains(name)
			);
		}

		return this;
	}

	/**
	 * Require any items in the list to be in the player's inventory.
	 * <p>
	 * Warning: names are case-sensitive
	 */
	public RequirementRule<TContext> mustHaveAnyInInventory(String... names)
	{
		requirements.put(
			MUST_HAVE_ITEM + formatList(names, "or") + TextColor.WHITE + " in inventory",
			c -> Inventory.contains(names)
		);

		return this;
	}

	/**
	 * Require all items to be in the player's inventory.
	 * <p>
	 * Warning: names are case-sensitive
	 */
	public RequirementRule<TContext> mustHaveInInventory(Predicate<Item> predicate, String name)
	{
		requirements.put(
			MUST_HAVE_ITEM + name + TextColor.WHITE + " in inventory",
			c -> Inventory.contains(predicate)
		);

		return this;
	}

	/**
	 * Require all items to be in the player's equipment
	 */
	public RequirementRule<TContext> mustHaveEquipped(int... ids)
	{
		for (final var id : ids)
		{
			final var name = getItemName(id);
			requirements.put(
				MUST_HAVE_ITEM + name + TextColor.WHITE + " equipped",
				c -> Equipment.contains(id)
			);
		}

		return this;
	}

	/**
	 * Require all items to be in the player's equipment
	 * <p>
	 * Warning: names are case-sensitive
	 */
	public RequirementRule<TContext> mustHaveEquipped(String... names)
	{
		for (final var name : names)
		{
			requirements.put(
				MUST_HAVE_ITEM + name + TextColor.WHITE + " equipped",
				c -> Equipment.contains(name)
			);
		}

		return this;
	}

	/**
	 * Require all items to be in the player's inventory or equipment
	 */
	public RequirementRule<TContext> mustHaveOnPerson(int... ids)
	{
		for (final var id : ids)
		{
			final var name = getItemName(id);
			requirements.put(
				MUST_HAVE_ITEM + name + TextColor.WHITE + " in inventory or equipped",
				c -> Inventory.contains(id) || Equipment.contains(id)
			);
		}

		return this;
	}

	/**
	 * Require all items to be in the player's inventory or equipment
	 * <p>
	 * Warning: names are case-sensitive
	 */
	public RequirementRule<TContext> mustHaveOnPerson(Predicate<Item> predicate, String name)
	{
		requirements.put(
			MUST_HAVE_ITEM + name + TextColor.WHITE + " in inventory or equipped",
			c -> Inventory.contains(predicate) || Equipment.contains(predicate)
		);

		return this;
	}

	/**
	 * Require all items to be in the player's inventory or equipment
	 * <p>
	 * Warning: names are case-sensitive
	 */
	public RequirementRule<TContext> mustHaveOnPerson(String... names)
	{
		for (final var name : names)
		{
			requirements.put(
				MUST_HAVE_ITEM + name + TextColor.WHITE + " in inventory or equipped",
				c -> Inventory.contains(name) || Equipment.contains(name)
			);
		}

		return this;
	}

	/**
	 * Require all items to be in the player's bank
	 */
	public RequirementRule<TContext> mustHaveBanked(int... ids)
	{
		for (final var id : ids)
		{
			final var name = getItemName(id);
			requirements.put(
				MUST_HAVE_ITEM + name + TextColor.WHITE + " in bank",
				c -> Bank.contains(id)
			);
		}

		return this;
	}

	/**
	 * Require all items to be in the player's bank
	 * <p>
	 * Warning: names are case sensitive
	 */
	public RequirementRule<TContext> mustHaveBanked(String... names)
	{
		for (final var name : names)
		{
			requirements.put(
				MUST_HAVE_ITEM + name + TextColor.WHITE + " in bank",
				c -> Bank.contains(name)
			);
		}

		return this;
	}

	/**
	 * Require the spell to be castable (all required runes/staves etc, correct
	 * spellbook, correct levels)
	 */
	public RequirementRule<TContext> mustBeAbleToCast(Spell spell)
	{
		requirements.put(
			TextColor.WHITE + "Must be able to cast " + TextColor.SPELL
				+ WordUtils.capitalizeFully(spell.toString().replaceAll("_", " ")),
			c -> spell.canCast()
		);

		return this;
	}

	/**
	 * Require the player to be in a specific, named area
	 */
	public RequirementRule<TContext> mustBeIn(String name, WorldArea worldArea)
	{
		return must(
			c -> worldArea.contains(Static.getClient().getLocalPlayer().getWorldLocation()),
			TextColor.WHITE + "Must be in " + TextColor.OBJECT + name
		);
	}

	/**
	 * Require the player to be in a specific, named area. Just says "near"
	 * instead of "in" in the message.
	 */
	public RequirementRule<TContext> mustBeNear(WorldArea worldArea, String name)
	{
		return must(
			c -> worldArea.contains(Static.getClient().getLocalPlayer().getWorldLocation()),
			TextColor.WHITE + "Must be near " + TextColor.OBJECT + name
		);
	}

	/**
	 * Require the interactable to not be null, and provide
	 * an overridden name to show
	 */
	public RequirementRule<TContext> mustBeNear(Supplier<?> supplier, String overrideName)
	{
		return must(
			c -> {
				final var interactable = supplier.get();
				return interactable instanceof DeferredInteractable
					? ((DeferredInteractable<?>) interactable).exists()
					: interactable != null;
			},
			TextColor.WHITE + "Must be near " + TextColor.OBJECT + overrideName
		);
	}

	/**
	 * Require the interactable to be visible to the player
	 */
	public RequirementRule<TContext> mustBeNear(Supplier<?> interactable)
	{
		final var object = interactable.get();
		if (object != null && !(object instanceof Interactable) && !(object instanceof DeferredInteractable))
		{
			throw new IllegalArgumentException(
				"mustBeNear supplier should return an Interactable or DeferredInteractable");
		}

		final var actual = object instanceof DeferredInteractable
			? ((DeferredInteractable<?>) object).unwrap()
			: (Interactable) object;
		if (actual instanceof NPC)
		{
			return mustBeNear(() -> actual, TextColor.NPC + ((NPC) actual).getName());
		}

		if (actual instanceof TileObject)
		{
			return mustBeNear(() -> actual, ((TileObject) actual).getName());
		}

		if (actual instanceof TileItem)
		{
			return mustBeNear(() -> actual, ((TileItem) actual).getName());
		}

		return mustBeNear(() -> actual, "Unknown");
	}

	@Override
	public boolean passes(TContext ctx)
	{
		// if when isn't true, then we should _not_
		// pass this rule
		if (when != null && !when.test(ctx))
		{
			return false;
		}

		// if any are false, trigger the requirement rule
		return requirements.values().stream().anyMatch(p -> !p.test(ctx));
	}

	@Override
	public List<String> messages(TContext ctx)
	{
		final var messages = new ArrayList<String>();
		messages.add(TextColor.DANGER + "Failed requirements:");
		for (final var entry : requirements.entrySet())
		{
			if (!entry.getValue().test(ctx))
			{
				messages.add(TextColor.DANGER + "- " + entry.getKey());
			}
		}

		return messages;
	}
}
