package com.yfletch.occore.v2.interaction;

import static com.yfletch.occore.v2.util.Util.matching;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.magic.Spell;
import net.unethicalite.api.widgets.Widgets;

@AllArgsConstructor
public class DeferredInteractableSpell
{
	private final Spell spell;

	public DeferredSpellInteraction cast()
	{
		return cast(0);
	}

	public DeferredSpellInteraction cast(int index)
	{
		if (Widgets.get(spell.getWidget()) == null)
		{
			return null;
		}

		return new DeferredSpellInteraction(spell, index);
	}

	public DeferredSpellInteraction cast(Predicate<String> predicate)
	{
		final var widget = Widgets.get(spell.getWidget());
		if (widget == null)
		{
			return null;
		}

		final var actions = widget.getActions();
		if (actions == null)
		{
			return null;
		}

		for (var i = 0; i < actions.length; i++)
		{
			if (predicate.test(actions[i]))
			{
				return cast(i);
			}
		}

		return null;
	}

	public DeferredSpellInteraction cast(String... actions)
	{
		return cast(matching(actions));
	}

	public DeferredSpellInteraction castOn(Interactable target)
	{
		if (target == null)
		{
			return null;
		}

		return new DeferredSpellInteraction(spell, target);
	}

	public DeferredSpellInteraction castOn(DeferredInteractable<?> target)
	{
		return castOn(target.unwrap());
	}
}
