package com.yfletch.summergarden.actions.restock.bank;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ItemAction;
import net.runelite.api.MenuAction;

public class WithdrawStamina4 extends ItemAction
{
	public WithdrawStamina4()
	{
		super("Stamina potion(4)", Consts.WITHDRAW_1);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isBankOpen()
			&& context.getState().getInventoryCount(Consts.STAMINA_4) < context.getConfig().withdrawStaminas();
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.withdrawItemAction(1, MenuAction.CC_OP, Consts.STAMINA_4);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().getInventoryCount(Consts.STAMINA_4) >= context.getConfig().withdrawStaminas();
	}
}
