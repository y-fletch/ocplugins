package com.yfletch.ocbloods.action.bank.repair;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.ocbloods.lib.ItemAction;
import com.yfletch.ocbloods.lib.event.WrappedEvent;
import net.runelite.api.ItemID;

public class WithdrawRunePouch extends ItemAction<OCBloodsContext>
{
	public WithdrawRunePouch()
	{
		super("Withdraw-1", "Rune pouch");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isBankOpen()
			&& ctx.pouchNeedsRepair()
			&& ctx.getConfig().repairPouch();
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.flag("rp-withdrawn");
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.hasItem(ItemID.RUNE_POUCH);
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.withdraw()
			.setOption("Withdraw-1", 1)
			.setItem(ItemID.RUNE_POUCH)
			.override();
		ctx.flag("rp-withdrawn", true, 2);
	}
}
