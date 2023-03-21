package com.yfletch.ocbarbfishing.action;

import com.yfletch.ocbarbfishing.OCBarbFishingContext;
import com.yfletch.occore.action.NpcAction;
import com.yfletch.occore.event.WrappedEvent;

public class UseRodFishingSpot extends NpcAction<OCBarbFishingContext>
{
	public UseRodFishingSpot()
	{
		super("Use-rod", "Fishing spot");
	}

	@Override
	public boolean isReady(OCBarbFishingContext ctx)
	{
		return ctx.isTick(0);
	}

	@Override
	public boolean isWorking(OCBarbFishingContext ctx)
	{
		return ctx.isFishing();
	}

	@Override
	public boolean isDone(OCBarbFishingContext ctx)
	{
		return ctx.isTick(1);
	}

	@Override
	public void run(OCBarbFishingContext ctx, WrappedEvent event)
	{
		event.builder().npc()
			.setNpc(1542)
			.setOption("Use-rod", 1)
			.override();
	}
}
