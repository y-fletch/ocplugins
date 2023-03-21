package com.yfletch.ocbarbfishing.action;

import com.yfletch.ocbarbfishing.OCBarbFishingContext;
import com.yfletch.occore.action.ItemAction;
import com.yfletch.occore.event.WrappedEvent;
import java.util.Set;
import net.runelite.api.ItemID;

public class UseKnifeOnFish extends ItemAction<OCBarbFishingContext>
{
	public UseKnifeOnFish()
	{
		super("Use Knife ->", "Leaping Salmon/Sturgeon/Trout");
	}

	@Override
	public boolean isReady(OCBarbFishingContext ctx)
	{
		return ctx.hasFish() && ctx.isTick(1);
	}

	@Override
	public boolean isDone(OCBarbFishingContext ctx)
	{
		return !ctx.hasFish() || ctx.isTick(2);
	}

	@Override
	public void run(OCBarbFishingContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.use(ItemID.KNIFE)
			.on(Set.of(
				ItemID.LEAPING_TROUT,
				ItemID.LEAPING_SALMON,
				ItemID.LEAPING_STURGEON
			))
			.override();
	}
}
