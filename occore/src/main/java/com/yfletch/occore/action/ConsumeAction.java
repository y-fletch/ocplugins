package com.yfletch.occore.action;

import com.yfletch.occore.ActionContext;
import com.yfletch.occore.event.WrappedEvent;
import java.awt.Color;
import lombok.AllArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;

/**
 * Consume the action / noop
 */
@AllArgsConstructor
public class ConsumeAction<T extends ActionContext> extends Action<T>
{
	private String name;

	@Override
	public String getName()
	{
		return name + " (consume)";
	}

	@Override
	public LineComponent getDisplayLine(T ctx)
	{
		return LineComponent.builder()
			.left(name).leftColor(Color.WHITE)
			.right("(consume)").rightColor(Color.ORANGE)
			.build();
	}

	@Override
	public void run(T ctx, WrappedEvent event)
	{
		event.consume();
	}
}
