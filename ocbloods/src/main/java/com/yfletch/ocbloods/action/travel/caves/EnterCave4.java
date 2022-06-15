package com.yfletch.ocbloods.action.travel.caves;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ObjectAction;
import com.yfletch.occore.event.WrappedEvent;

public class EnterCave4 extends ObjectAction<OCBloodsContext>
{
	public EnterCave4()
	{
		super("Enter", "Cave");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isInTunnel4();
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !ctx.isInTunnel4();
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().object()
			.setOption("Enter", 1)
			.setObject(43762)
			.override();
	}
}
