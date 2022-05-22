package com.yfletch.summergarden.actions.restock.bank;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ObjectAction;

public class OpenPalaceDoor extends ObjectAction
{
	public OpenPalaceDoor()
	{
		super("Large door", Consts.OPEN);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return !context.getConfig().usePalaceShortcut()
			&& context.getState().isInAlKharidPalace()
			&& context.getObjectManager().isVisible(Consts.PALACE_DOOR_CLOSED);
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.objectAction(Consts.OPEN, Consts.PALACE_DOOR_CLOSED, 53, 55);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return !context.getObjectManager().isVisible(Consts.PALACE_DOOR_CLOSED);
	}
}
