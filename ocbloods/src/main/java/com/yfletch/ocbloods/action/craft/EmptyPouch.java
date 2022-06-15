package com.yfletch.ocbloods.action.craft;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ItemAction;
import com.yfletch.occore.event.WrappedEvent;
import net.runelite.api.ItemID;

public class EmptyPouch extends ItemAction<OCBloodsContext>
{
	public EmptyPouch()
	{
		super("Empty", "Colossal pouch");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isInBloodAltar()
			&& ctx.getOptimisticEssenceCount() == 0
			&& !ctx.pouchIsEmpty();
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.pouchIsEmpty()
			|| ctx.getOptimisticEssenceCount() > 0;
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.setOption("Empty", 3)
			.setItem(ItemID.COLOSSAL_POUCH)
			.override();
	}
}
