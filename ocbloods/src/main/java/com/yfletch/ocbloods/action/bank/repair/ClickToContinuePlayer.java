package com.yfletch.ocbloods.action.bank.repair;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.ocbloods.lib.InterfaceAction;
import com.yfletch.ocbloods.lib.event.WrappedEvent;

public class ClickToContinuePlayer extends InterfaceAction<OCBloodsContext>
{
	private static final int CLICK_TO_CONTINUE = 14221317;

	public ClickToContinuePlayer()
	{
		super("Continue", null);
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.dialogPlayerTextContains("Can you repair my pouches?");
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !ctx.dialogPlayerTextContains("Can you repair my pouches?");
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().widget()
			.setDialogOption(CLICK_TO_CONTINUE)
			.override();
	}

	@Override
	public void done(OCBloodsContext ctx)
	{
		ctx.repairPouch();
	}
}
