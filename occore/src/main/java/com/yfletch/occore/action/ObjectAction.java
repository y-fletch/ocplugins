package com.yfletch.occore.action;

import com.yfletch.occore.ActionContext;
import java.awt.Color;
import lombok.AllArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;

/**
 * Represents an action on a game or tile object in the world.
 */
@AllArgsConstructor
public class ObjectAction<T extends ActionContext> extends Action<T>
{
	private String action;
	private String objectName;

	@Override
	public String getName()
	{
		return action + " " + objectName;
	}

	@Override
	public LineComponent getDisplayLine(T ctx)
	{
		return LineComponent.builder()
			.left(action).leftColor(Color.WHITE)
			.right(objectName).rightColor(Color.CYAN)
			.build();
	}
}
