package com.yfletch.ocbloods.action.bank;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ItemAction;
import com.yfletch.occore.event.WrappedEvent;
import net.runelite.api.ItemID;

public class DepositPieDish extends ItemAction<OCBloodsContext>
{
	public DepositPieDish()
	{
		super("Deposit-All", "Pie dish");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isBankOpen()
			&& ctx.hasItem(ItemID.PIE_DISH);
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !ctx.hasItem(ItemID.PIE_DISH);
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.deposit()
			.setItem(ItemID.PIE_DISH)
			.setOption("Deposit-1", 2)
			.override();
	}
}
