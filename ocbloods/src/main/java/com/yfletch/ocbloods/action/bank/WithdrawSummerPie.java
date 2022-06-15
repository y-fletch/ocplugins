package com.yfletch.ocbloods.action.bank;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.ocbloods.lib.ItemAction;
import com.yfletch.ocbloods.lib.event.WrappedEvent;
import net.runelite.api.ItemID;

public class WithdrawSummerPie extends ItemAction<OCBloodsContext>
{
	public WithdrawSummerPie()
	{
		super("Withdraw-1", "Summer pie");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.getConfig().useSummerPies()
			&& ctx.isBankOpen()
			&& !(ctx.hasItem(ItemID.SUMMER_PIE)
			|| ctx.hasItem(ItemID.HALF_A_SUMMER_PIE));
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.hasItem(ItemID.SUMMER_PIE)
			|| ctx.hasItem(ItemID.HALF_A_SUMMER_PIE)
			|| ctx.flag("pie-withdrawn");
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.withdraw()
			.setOption("Withdraw-1", 1)
			.setItem(
				ctx.hasBanked(ItemID.HALF_A_SUMMER_PIE)
					? ItemID.HALF_A_SUMMER_PIE
					: ItemID.SUMMER_PIE
			)
			.override();
		ctx.flag("pie-withdrawn", true, 2);
	}
}
