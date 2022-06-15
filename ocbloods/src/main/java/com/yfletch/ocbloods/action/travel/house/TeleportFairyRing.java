package com.yfletch.ocbloods.action.travel.house;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ObjectAction;
import com.yfletch.occore.event.WrappedEvent;

public class TeleportFairyRing extends ObjectAction<OCBloodsContext>
{
	public TeleportFairyRing()
	{
		super("Last-destination (DLS)", "Fairy ring");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isInInstance();
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.isPathingTo(29228);
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !ctx.isInInstance();
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().object()
			.setOption("Last-destination (DLS)", 3)
			.setObject(29228)
			.override();
	}
}
