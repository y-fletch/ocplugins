package com.yfletch.occore.action;

import com.yfletch.occore.ActionContext;
import java.awt.Color;
import java.util.function.Function;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;

/**
 * Represents an action on a widget or part of the game interface.
 */
@RequiredArgsConstructor
public class WidgetAction<T extends ActionContext> extends Action<T>
{
	private final String action;

	@Nullable
	private String widgetName;

	@Nullable
	private Function<T, String> widgetNameGetter;

	public WidgetAction(String action, String widgetName)
	{
		this.action = action;
		this.widgetName = widgetName;
	}

	public WidgetAction(String action, Function<T, String> widgetNameGetter)
	{
		this.action = action;
		this.widgetNameGetter = widgetNameGetter;
	}

	@Override
	public String getName()
	{
		return action + " " + (widgetName != null ? widgetName : "<none>");
	}

	@Override
	public LineComponent getDisplayLine(T context)
	{
		LineComponent.LineComponentBuilder builder = LineComponent.builder()
			.left(action).leftColor(Color.WHITE);

		final var name = widgetNameGetter != null
			? widgetNameGetter.apply(context)
			: widgetName;

		if (name != null)
		{
			builder
				.right(name)
				// green for magic spells
				.rightColor(action.equals("Cast") ? Color.GREEN : Color.ORANGE);
		}

		return builder.build();
	}

}
