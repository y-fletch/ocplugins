package com.yfletch.summergarden.actions;

import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.Action;
import com.yfletch.summergarden.util.action.ActionContext;
import java.awt.Color;
import net.runelite.client.ui.overlay.components.LineComponent;

public class Unknown implements Action
{
	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		return LineComponent.builder().left("Summer garden").leftColor(Color.CYAN).right("Out of range!").build();
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return true;
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return false;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{

	}

	@Override
	public void done(ActionContext context)
	{

	}
}
