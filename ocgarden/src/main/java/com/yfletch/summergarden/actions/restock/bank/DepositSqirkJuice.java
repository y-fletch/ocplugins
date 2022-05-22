package com.yfletch.summergarden.actions.restock.bank;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ItemAction;
import net.runelite.api.MenuAction;

public class DepositSqirkJuice extends ItemAction
{
	public DepositSqirkJuice()
	{
		super("Summer sq'irkjuice", Consts.DEPOSIT_ALL);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isBankOpen()
			&& context.getState().getInventoryCount(Consts.SUMMER_SQIRK_JUICE) > 0;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.depositItemAction(8, MenuAction.CC_OP_LOW_PRIORITY, Consts.SUMMER_SQIRK_JUICE);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().getInventoryCount(Consts.SUMMER_SQIRK_JUICE) == 0;
	}
}
