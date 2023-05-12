package com.yfletch.occore.v2.rule;

import com.yfletch.occore.v2.CoreContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.runelite.api.coords.WorldArea;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class RequirementRule<TContext extends CoreContext> implements Rule<TContext>
{
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
		requirements.put("<col=ffffff>" + error, predicate);
		return this;
	}

	/**
	 * Display the error if the predicate evaluates to TRUE
	 */
	public RequirementRule<TContext> mustNot(Predicate<TContext> predicate, String error)
	{
		requirements.put("<col=ffffff>" + error, predicate.negate());
		return this;
	}

	/**
	 * Require all item IDs to be in the player's inventory, bank
	 * or equipped
	 */
	public RequirementRule<TContext> mustHave(int... ids)
	{
		for (final var id : ids)
		{
			final var name = getItemName(id);
			requirements.put(
				"<col=ffffff>Must have <col=ff9040>" + name,
				c -> Inventory.contains(id) || Bank.contains(id) || Equipment.contains(id)
			);
		}

		return this;
	}

	/**
	 * Require all item IDs to be in the player's inventory
	 */
	public RequirementRule<TContext> mustHaveInInventory(int... ids)
	{
		for (final var id : ids)
		{
			final var name = getItemName(id);
			requirements.put(
				"<col=ffffff>Must have <col=ff9040>" + name,
				c -> Inventory.contains(id)
			);
		}

		return this;
	}

	/**
	 * Require all item IDs to be in the player's equipment
	 */
	public RequirementRule<TContext> mustHaveEquipped(int... ids)
	{
		for (final var id : ids)
		{
			final var name = getItemName(id);
			requirements.put(
				"<col=ffffff>Must have <col=ff9040>" + name + " <col=ffffff>equipped",
				c -> Equipment.contains(id)
			);
		}

		return this;
	}

	/**
	 * Require all item IDs to be in the player's bank
	 */
	public RequirementRule<TContext> mustHaveBanked(int... ids)
	{
		for (final var id : ids)
		{
			final var name = getItemName(id);
			requirements.put(
				"<col=ffffff>Must have <col=ff9040>" + name + " <col=ffffff>in bank",
				c -> Bank.contains(id)
			);
		}

		return this;
	}

	/**
	 * Require the player to be in a specific, named area
	 */
	public RequirementRule<TContext> mustBeIn(String name, WorldArea worldArea)
	{
		return must(
			c -> worldArea.contains(Static.getClient().getLocalPlayer().getWorldLocation()),
			"<col=ffffff>Must be in <col=ffff>" + name
		);
	}

	/**
	 * Require the player to be in a specific, named area. Just says "near"
	 * instead of "in" in the message.
	 */
	public RequirementRule<TContext> mustBeNear(String name, WorldArea worldArea)
	{
		return must(
			c -> worldArea.contains(Static.getClient().getLocalPlayer().getWorldLocation()),
			"<col=ffffff>Must be near <col=ffff>" + name
		);
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
		messages.add("<col=ff0000>Failed requirements:");
		for (final var entry : requirements.entrySet())
		{
			if (!entry.getValue().test(ctx))
			{
				messages.add("<col=ff0000>- " + entry.getKey());
			}
		}

		return messages;
	}
}
