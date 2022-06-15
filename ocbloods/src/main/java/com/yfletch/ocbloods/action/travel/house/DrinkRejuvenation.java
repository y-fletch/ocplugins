package com.yfletch.ocbloods.action.travel.house;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ObjectAction;
import com.yfletch.occore.event.WrappedEvent;

public class DrinkRejuvenation extends ObjectAction<OCBloodsContext>
{
	public DrinkRejuvenation()
	{
		super("Drink", "Pool of Rejuvenation");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isInInstance()
			&& ctx.getRunEnergy() < ctx.getConfig().restoreRunThreshold()
			&& !ctx.isNextTo(ctx.getPoolOfRejuvenation());
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.isPathingTo(ctx.getPoolOfRejuvenation())
			|| ctx.flag("drink-pool");
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.getRunEnergy() >= 100;
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().object()
			.setOption("Drink", 1)
			.setObject(ctx.getPoolOfRejuvenation())
			.override();

		ctx.flag("drink-pool", true, 4);
	}
}
