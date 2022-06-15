package com.yfletch.ocbloods.action.craft;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ObjectAction;
import com.yfletch.occore.event.WrappedEvent;
import net.runelite.api.ItemID;

public class CraftRuneAltar extends ObjectAction<OCBloodsContext>
{
	public CraftRuneAltar()
	{
		super("Craft-rune", "Altar");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isInBloodAltar()
			&& ctx.getOptimisticEssenceCount() > 0;
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.flag("craft-rune");
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.getOptimisticEssenceCount() == 0
			&& !ctx.hasItem(ItemID.PURE_ESSENCE)
			&& ctx.pouchIsEmpty();
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().object()
			.setOption("Craft-rune", 1)
			.setObject(43479)
			.override();
		ctx.flag("craft-rune", true, 0);
	}
}
