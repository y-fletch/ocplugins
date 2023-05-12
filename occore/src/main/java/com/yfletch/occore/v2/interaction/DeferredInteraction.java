package com.yfletch.occore.v2.interaction;

import com.yfletch.occore.v2.util.TextColor;
import lombok.AllArgsConstructor;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.Interactable;

@AllArgsConstructor
public class DeferredInteraction
{
	private final Interactable interactable;
	private final int actionIndex;

	public void execute()
	{
		interactable.interact(actionIndex);
	}

	public final String getTooltip()
	{
		return getAction() + " " + getTarget();
	}

	public String getAction()
	{
		final var actions = interactable.getActions();
		if (actions == null || actions.length <= actionIndex)
		{
			return TextColor.DANGER + "Unknown action";
		}

		return TextColor.WHITE + actions[actionIndex];
	}

	public String getTarget()
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
