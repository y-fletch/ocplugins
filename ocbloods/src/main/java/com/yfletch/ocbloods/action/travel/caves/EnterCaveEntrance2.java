package com.yfletch.ocbloods.action.travel.caves;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ObjectAction;
import com.yfletch.occore.event.WrappedEvent;

public class EnterCaveEntrance2 extends ObjectAction<OCBloodsContext>
{
	// 5046

	public EnterCaveEntrance2()
	{
		super("Enter", "Cave entrance");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isInTunnel2();
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.isPathingTo(5046);
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !ctx.isInTunnel2();
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().object()
			.setOption("Enter", 1)
			.setObject(5046)
			.override();
	}
}
