package com.yfletch.ocbloods.action.bank;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ItemAction;
import com.yfletch.occore.event.WrappedEvent;
import net.runelite.api.ItemID;

public class WithdrawPureEssence extends ItemAction<OCBloodsContext>
{
	public WithdrawPureEssence()
	{
		super("Withdraw-All", "Pure essence");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isBankOpen()
			&& ctx.getOptimisticFreeSlots() > 0;
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.getOptimisticFreeSlots() == 0;
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.withdraw()
			.setOption("Withdraw-All", 7)
			.setItem(ItemID.PURE_ESSENCE)
			.override();
	}
}
