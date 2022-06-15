package com.yfletch.ocbloods.action.bank;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ItemAction;
import com.yfletch.occore.event.WrappedEvent;
import net.runelite.api.ItemID;

public class WithdrawBloodEssence extends ItemAction<OCBloodsContext>
{
	public WithdrawBloodEssence()
	{
		super("Withdraw-1", "Blood essence");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.getConfig().useBloodEssence()
			&& ctx.isBankOpen()
			&& !(ctx.hasItem(ItemID.BLOOD_ESSENCE_ACTIVE)
			|| ctx.hasItem(ItemID.BLOOD_ESSENCE));
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.hasItem(ItemID.BLOOD_ESSENCE)
			|| ctx.hasItem(ItemID.BLOOD_ESSENCE_ACTIVE)
			|| ctx.flag("be-withdrawn");
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.withdraw()
			.setOption("Withdraw-1", 1)
			.setItem(
				ctx.hasBanked(ItemID.BLOOD_ESSENCE_ACTIVE)
					? ItemID.BLOOD_ESSENCE_ACTIVE
					: ItemID.BLOOD_ESSENCE
			)
			.override();
		ctx.flag("be-withdrawn", true, 5);
	}
}
