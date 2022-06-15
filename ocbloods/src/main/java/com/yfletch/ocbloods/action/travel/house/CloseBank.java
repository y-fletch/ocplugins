package com.yfletch.ocbloods.action.travel.house;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.ocbloods.lib.InterfaceAction;
import com.yfletch.ocbloods.lib.event.WrappedEvent;

public class CloseBank extends InterfaceAction<OCBloodsContext>
{
	public CloseBank()
	{
		super("Close", "");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isInBankingArea()
			&& ctx.isBankOpen()
			&& ctx.getOptimisticFreeSlots() == 0;
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.flag("bank-closed");
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !ctx.isBankOpen();
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().widget()
			.setOption("Close", 1)
			.setWidget(12, 2)
			.setChild(11)
			.override();
		ctx.flag("bank-closed", true, 2);
	}
}
