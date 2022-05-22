package com.yfletch.summergarden.actions.restock.bank.stamina;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ItemAction;
import net.runelite.api.MenuAction;

public class DepositVial extends ItemAction
{
	public DepositVial()
	{
		super("Vial", Consts.DEPOSIT_ALL);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isBankOpen()
			&& context.getState().getInventoryCount(Consts.VIAL) > 0;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.depositItemAction(8, MenuAction.CC_OP_LOW_PRIORITY, Consts.VIAL);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().getInventoryCount(Consts.VIAL) == 0;
	}
}
