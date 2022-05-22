package com.yfletch.summergarden.actions.garden;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ObjectAction;

public class GrabSqirk extends ObjectAction
{
	public GrabSqirk()
	{
		super("Sq'irk tree", Consts.PICK_FRUIT);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isOnStartingTile()
			&& context.getCollisionDetector().getTicksUntilStart() == 0;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.objectAction(Consts.PICK_FRUIT, Consts.SUMMER_SQIRK_TREE, 1);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().isRunning();
	}
}
