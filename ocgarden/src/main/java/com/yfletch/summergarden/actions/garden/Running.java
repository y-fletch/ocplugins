package com.yfletch.summergarden.actions.garden;

import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.Action;
import com.yfletch.summergarden.util.action.ActionContext;
import net.runelite.client.ui.overlay.components.LineComponent;

public class Running implements Action
{
	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		return LineComponent.builder().left("Running...").build();
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isRunning()
			|| context.getState().isAlmostAtGate()
			|| context.getState().isInSummerGarden();
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
