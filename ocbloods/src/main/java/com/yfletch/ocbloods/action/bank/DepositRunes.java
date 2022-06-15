package com.yfletch.ocbloods.action.bank;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ItemAction;
import com.yfletch.occore.event.WrappedEvent;
import net.runelite.api.ItemID;

public class DepositRunes extends ItemAction<OCBloodsContext>
{
	public DepositRunes()
	{
		super("Deposit-All", "Blood rune");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isBankOpen()
			&& ctx.hasItem(ItemID.BLOOD_RUNE);
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !ctx.hasItem(ItemID.BLOOD_RUNE);
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.deposit()
			.setItem(ItemID.BLOOD_RUNE)
			.setOption("Deposit-All", 8)
			.override();
	}
}
