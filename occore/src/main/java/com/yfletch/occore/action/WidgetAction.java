package com.yfletch.occore.action;

import com.yfletch.occore.ActionContext;
import java.awt.Color;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;

/**
 * Represents an action on a widget or part of the game interface.
 */
@AllArgsConstructor
public class WidgetAction<T extends ActionContext> extends Action<T>
{
	private String action;
	@Nullable
	private String widgetName;

	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		LineComponent.LineComponentBuilder builder = LineComponent.builder()
			.left(action).leftColor(Color.WHITE);

		if (widgetName != null)
		{
			builder
				.right(widgetName)
				// green for magic spells
				.rightColor(action.equals("Cast") ? Color.GREEN : Color.ORANGE);
		}

		return builder.build();
	}

}
