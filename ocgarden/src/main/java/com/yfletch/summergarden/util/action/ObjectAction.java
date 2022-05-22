package com.yfletch.summergarden.util.action;

import com.yfletch.summergarden.util.WrappedEvent;
import lombok.AllArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;

import java.awt.*;
import org.apache.commons.lang3.NotImplementedException;

@AllArgsConstructor
public class ObjectAction implements Action
{
	/**
	 * @TODO: see if this can be derived from ID
	 */
	private String objectName;

	/**
	 * AKA Menu option
	 */
	private String actionName;

	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		return LineComponent.builder()
			.left(actionName).leftColor(Color.WHITE)
			.right(objectName).rightColor(Color.CYAN)
			.build();
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
