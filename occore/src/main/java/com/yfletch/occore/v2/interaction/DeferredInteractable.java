package com.yfletch.occore.v2.interaction;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.commons.Predicates;

@AllArgsConstructor
public class DeferredInteractable
{
	/**
	 * The interactable entity to act upon.
	 * <p>
	 * If null, the interaction will fail silently
	 * and produce appropriate errors.
	 */
	@Nullable
	private final Interactable interactable;

	public Interactable unwrap()
	{
		return interactable;
	}

	/**
	 * Interact with the first action matching the predicate
	 */
	public DeferredInteraction interact(Predicate<String> predicate)
	{
		if (interactable == null) return null;
		final var actions = interactable.getActions();
		if (actions == null) return null;

		for (var i = 0; i < actions.length; i++)
		{
			if (predicate.test(actions[i]))
			{
				return interact(i);
			}
		}

		return null;
	}

	/**
	 * Interact with the first action matching the given values
	 */
	public DeferredInteraction interact(String... actions)
	{
		return interact(Predicates.texts(actions));
	}

	/**
	 * Interact with the nth action
	 */
	public DeferredInteraction interact(int index)
	{
		if (interactable == null) return null;
		return new DeferredInteraction(interactable, index);
	}

	/**
	 * Interact with the first action
	 */
	public DeferredInteraction interact()
	{
		return interact(0);
	}
}
