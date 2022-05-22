package com.yfletch.summergarden.actions.garden;

import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.InterfaceAction;
import net.runelite.api.widgets.WidgetInfo;

public class EnableRun extends InterfaceAction
{
	public EnableRun()
	{
		super(null, "Toggle Run");
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return !context.getState().isRunEnabled();
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.interfaceAction("Toggle Run", WidgetInfo.MINIMAP_TOGGLE_RUN_ORB);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().isRunEnabled();
	}
}
