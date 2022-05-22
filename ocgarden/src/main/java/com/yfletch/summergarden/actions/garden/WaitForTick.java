package com.yfletch.summergarden.actions.garden;

import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.Action;
import com.yfletch.summergarden.util.action.ActionContext;
import net.runelite.client.ui.overlay.components.LineComponent;

public class WaitForTick implements Action
{
	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		return LineComponent.builder().left("Waiting").right("" + context.getCollisionDetector().getTicksUntilStart()).build();
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isOnStartingTile();
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getCollisionDetector().getTicksUntilStart() == 0;
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
