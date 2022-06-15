package com.yfletch.ocbloods.action.bank;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.ocbloods.lib.ItemAction;
import com.yfletch.ocbloods.lib.event.WrappedEvent;
import net.runelite.api.ItemID;

public class FillPouch extends ItemAction<OCBloodsContext>
{
	public FillPouch()
	{
		super("Fill", "Colossal pouch");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isBankOpen()
			&& ctx.hasItem(ItemID.PURE_ESSENCE)
			&& ctx.hasItem(ItemID.COLOSSAL_POUCH)
			&& ctx.getOptimisticFreeSlots() == 0
			&& !ctx.pouchIsFull();
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.getOptimisticFreeSlots() > 0
			|| ctx.pouchIsFull();
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.deposit()
			.setItem(ItemID.COLOSSAL_POUCH)
			.setOption("Fill", 9)
			.override();
	}
}
