package com.yfletch.summergarden.util.action;

import com.yfletch.summergarden.util.WrappedEvent;
import java.awt.Color;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;
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
		throw new NotImplementedException("Missing isReady implementation");
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return false;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		throw new NotImplementedException("Missing run i mplementation");
	}

	@Override
	public void done(ActionContext context)
	{

	}
}
