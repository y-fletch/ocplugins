package com.yfletch.summergarden.actions.restock.house;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ItemAction;
import net.runelite.api.MenuAction;

public class BreakTeletab extends ItemAction
{
	public BreakTeletab()
	{
		super("Teleport to house", Consts.BREAK);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isInGardenCenter()
			&& context.getState().getInventoryCount(Consts.EMPTY_BEER_GLASS) < 1;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.itemAction(2, MenuAction.CC_OP, Consts.HOUSE_TAB);
		context.setFlag("drank-from-pool", false);
		context.setFlag("in-house", true);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getFlag("in-house") || context.getState().isInHouse();
	}
}
