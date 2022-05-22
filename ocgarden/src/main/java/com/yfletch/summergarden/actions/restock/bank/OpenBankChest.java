package com.yfletch.summergarden.actions.restock.bank;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ObjectAction;
import net.runelite.api.MenuAction;

public class OpenBankChest extends ObjectAction
{
	public OpenBankChest()
	{
		super("Bank chest", Consts.USE);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isInAlKharidPalace();
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.objectAction(Consts.BANK, MenuAction.GAME_OBJECT_FIRST_OPTION, Consts.SHANTAY_BANK_CHEST);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().isRunning();
	}
}
