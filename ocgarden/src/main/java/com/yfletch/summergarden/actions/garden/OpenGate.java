package com.yfletch.summergarden.actions.garden;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ObjectAction;

public class OpenGate extends ObjectAction
{
	public OpenGate()
	{
		super("Gate", Consts.OPEN);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isInGardenCenter();
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.objectAction(Consts.OPEN, Consts.GATE);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().isRunning();
	}
}
