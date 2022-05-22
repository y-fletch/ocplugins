package com.yfletch.summergarden.actions.restock.bank;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ObjectAction;

public class OpenApprenticeDoor extends ObjectAction
{
	public OpenApprenticeDoor()
	{
		super("Door", Consts.OPEN);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isInAlKharid()
			&& context.getState().getInventoryCount(Consts.EMPTY_BEER_GLASS) > 0
			&& context.getState().getFreeInventorySlots() == 2
			&& context.getObjectManager().isVisible(Consts.APPRENTICE_DOOR_CLOSED);
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.objectAction(Consts.OPEN, Consts.APPRENTICE_DOOR_CLOSED, 65, 70);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().isRunning();
	}
}
