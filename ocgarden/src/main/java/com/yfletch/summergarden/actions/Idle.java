package com.yfletch.summergarden.actions;

import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.Action;
import com.yfletch.summergarden.util.action.ActionContext;
import net.runelite.client.ui.overlay.components.LineComponent;

public class Idle implements Action
{
	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		return LineComponent.builder().left("Idle").build();
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isInGarden()
			|| context.getState().isInHouse()
			|| context.getState().isInAlKharid();
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return false;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.consume();
	}

	@Override
	public void done(ActionContext context)
	{

	}
}
