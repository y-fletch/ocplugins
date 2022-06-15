package com.yfletch.ocbloods.action.bank.repair;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ItemAction;
import com.yfletch.occore.event.WrappedEvent;
import net.runelite.api.ItemID;

public class DepositRunePouch extends ItemAction<OCBloodsContext>
{
	public DepositRunePouch()
	{
		super("Deposit-1", "Rune pouch");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isBankOpen()
			&& !ctx.pouchNeedsRepair()
			&& ctx.hasItem(ItemID.RUNE_POUCH);
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.flag("rp-deposited");
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !ctx.hasItem(ItemID.RUNE_POUCH);
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.deposit()
			.setItem(ItemID.RUNE_POUCH)
			.setOption("Deposit-1", 2)
			.override();

		ctx.flag("rp-deposited", true, 2);
	}
}
