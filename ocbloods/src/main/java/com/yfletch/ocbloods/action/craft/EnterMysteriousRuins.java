package com.yfletch.ocbloods.action.craft;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.ocbloods.lib.ObjectAction;
import com.yfletch.ocbloods.lib.event.WrappedEvent;

public class EnterMysteriousRuins extends ObjectAction<OCBloodsContext>
{
	public EnterMysteriousRuins()
	{
		super("Enter", "Mysterious ruins");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isInBloodCrypt();
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.isPathingTo(25380);
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !ctx.isInBloodCrypt();
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().object()
			.setOption("Enter", 1)
			.setObject(25380)
			.override();
	}
}
