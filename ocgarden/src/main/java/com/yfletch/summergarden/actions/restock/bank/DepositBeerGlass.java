package com.yfletch.summergarden.actions.restock.bank;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ItemAction;
import net.runelite.api.MenuAction;

public class DepositBeerGlass extends ItemAction
{
	public DepositBeerGlass()
	{
		super("Beer glass", Consts.DEPOSIT_1);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isBankOpen()
			&& context.getState().getFreeInventorySlots() < 2;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.depositItemAction(2, MenuAction.CC_OP, Consts.EMPTY_BEER_GLASS);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().getFreeInventorySlots() >= 2;
	}
}