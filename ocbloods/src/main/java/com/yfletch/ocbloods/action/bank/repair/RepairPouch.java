package com.yfletch.ocbloods.action.bank.repair;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.ocbloods.lib.InterfaceAction;
import com.yfletch.ocbloods.lib.event.WrappedEvent;

public class RepairPouch extends InterfaceAction<OCBloodsContext>
{
	public RepairPouch()
	{
		super("Continue", null);
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.getDialogOptionIndex("Can you repair my pouches?") > 0;
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.dialogPlayerTextContains("Can you repair my pouches?");
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().widget()
			.setDialogOption("Can you repair my pouches?")
			.override();
	}
}
