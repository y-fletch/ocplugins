package com.yfletch.summergarden.actions.restock.bank;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ItemAction;
import net.runelite.api.MenuAction;

public class WithdrawBeerGlass extends ItemAction
{
	public WithdrawBeerGlass()
	{
		super("Beer glass", Consts.WITHDRAW_ALL);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isBankOpen()
			&& context.getState().getInventoryCount(Consts.EMPTY_BEER_GLASS) == 0;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.withdrawItemAction(7, MenuAction.CC_OP_LOW_PRIORITY, Consts.EMPTY_BEER_GLASS);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().getInventoryCount(Consts.EMPTY_BEER_GLASS) > 0;
	}
}
