package com.yfletch.occore.v2.interaction;

import static com.yfletch.occore.v2.interaction.Entities.matching;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.unethicalite.api.Interactable;

@Slf4j
@AllArgsConstructor
public class DeferredInteractable<T extends Interactable>
{
	/**
	 * The interactable entity to act upon.
	 * <p>
	 * If null, the interaction will fail silently
	 * and produce appropriate errors.
	 */
	@Nullable
	private final T interactable;

	public T unwrap()
	{
		return interactable;
	}

	/**
	 * Interact with the first action matching the predicate
	 */
	public DeferredInteraction<T> interact(Predicate<String> predicate)
	{
		if (interactable == null)
		{
			log.info("Null interactable");
			return null;
		}

		final var actions = interactable.getActions();
		if (actions == null)
		{
			log.info("Null actions");
			return null;
		}

		for (var i = 0; i < actions.length; i++)
		{
			if (predicate.test(actions[i]))
			{
				return interact(i);
			}
		}

		log.info("No matching actions");
		return null;
	}

	/**
	 * Interact with the first action matching the given values
	 */
	public DeferredInteraction<T> interact(String... actions)
	{
		return interact(matching(actions));
	}

	/**
	 * Interact with the nth action
	 */
	public DeferredInteraction<T> interact(int index)
	{
		if (interactable == null)
		{
			log.info("Interactable was null");
			return null;
		}

		return new DeferredInteraction<>(interactable, index);
	}

	/**
	 * Interact with the first action
	 */
	public DeferredInteraction<T> interact()
	{
		return interact(0);
	}
}
