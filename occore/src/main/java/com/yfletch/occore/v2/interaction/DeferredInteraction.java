package com.yfletch.occore.v2.interaction;

import com.yfletch.occore.v2.util.TextColor;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.Interactable;

@RequiredArgsConstructor
public class DeferredInteraction<T extends Interactable>
{
	@Nonnull
	protected T interactable;
	private final int actionIndex;

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

	public void execute()
	{
		interactable.interact(actionIndex);
	}

	public final String getTooltip()
	{
		return getActionText() + " " + getTargetText();
	}

	public String getActionText()
	{
		final var actions = interactable.getActions();
		if (actions == null || actions.length <= actionIndex)
		{
			return TextColor.DANGER + "Unknown action";
		}

		return TextColor.WHITE + actions[actionIndex];
	}

	public String getTargetText()
	{
		return getTargetText(interactable);
	}

	protected String getTargetText(Interactable interactable)
	{
		if (interactable instanceof NPC)
		{
			return TextColor.NPC + ((NPC) interactable).getName();
		}

		if (interactable instanceof TileObject)
		{
			return TextColor.OBJECT + ((TileObject) interactable).getName();
		}

		if (interactable instanceof Widget)
		{
			return TextColor.WHITE + ((Widget) interactable).getName();
		}

		// all other types of targets are handled by subclasses
		return TextColor.DANGER + "Unknown target";
	}
}
