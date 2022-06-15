package com.yfletch.ocbloods.action.travel.caves;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.ocbloods.lib.ObjectAction;
import com.yfletch.ocbloods.lib.event.WrappedEvent;

public class EnterCaveEntrance1 extends ObjectAction<OCBloodsContext>
{
	public EnterCaveEntrance1()
	{
		super("Enter", "Cave entrance");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isInTunnel1();
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.isPathingTo(16308);
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !ctx.isInTunnel1();
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().object()
			.setOption("Enter", 1)
			.setObject(16308)
			.override();
	}
}
