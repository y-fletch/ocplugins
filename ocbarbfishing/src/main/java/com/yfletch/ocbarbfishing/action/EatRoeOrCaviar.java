package com.yfletch.ocbarbfishing.action;

import com.yfletch.ocbarbfishing.OCBarbFishingContext;
import com.yfletch.occore.action.ItemAction;
import com.yfletch.occore.event.WrappedEvent;
import java.util.Set;
import net.runelite.api.ItemID;

public class EatRoeOrCaviar extends ItemAction<OCBarbFishingContext>
{
	public EatRoeOrCaviar()
	{
		super("Eat", "Roe/Caviar");
	}

	@Override
	public boolean isReady(OCBarbFishingContext ctx)
	{
		return ctx.hasFood()
			&& ctx.isTick(2);
	}

	@Override
	public boolean isDone(OCBarbFishingContext ctx)
	{
		return !ctx.hasFood()
			|| ctx.isTick(0);
	}

	@Override
	public void run(OCBarbFishingContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.setItems(Set.of(ItemID.ROE, ItemID.CAVIAR))
			.setOption("Eat", 1)
			.override();
	}
}
