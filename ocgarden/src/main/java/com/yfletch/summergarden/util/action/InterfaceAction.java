package com.yfletch.summergarden.util.action;

import com.yfletch.summergarden.util.WrappedEvent;
import lombok.AllArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.annotation.Nullable;
import java.awt.*;
import org.apache.commons.lang3.NotImplementedException;

@AllArgsConstructor
public class InterfaceAction implements Action
{
	@Nullable
	private String interfaceName;

	/**
	 * AKA Menu option
	 */
	private String actionName;

	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		LineComponent.LineComponentBuilder builder = LineComponent.builder()
			.left(actionName).leftColor(Color.WHITE);

		if (interfaceName != null)
		{
			builder
				.right(interfaceName)
				// green for magic spells
				.rightColor(actionName.equals("Cast") ? Color.GREEN : Color.ORANGE);
		}

		return builder.build();
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		throw new NotImplementedException();
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return false;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		throw new NotImplementedException();
	}

	@Override
	public void done(ActionContext context)
	{

	}
}
