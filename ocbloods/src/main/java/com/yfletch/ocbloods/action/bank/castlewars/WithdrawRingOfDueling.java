package com.yfletch.ocbloods.action.bank.castlewars;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ItemAction;
import com.yfletch.occore.event.WrappedEvent;
import java.util.List;
import net.runelite.api.ItemID;

public class WithdrawRingOfDueling extends ItemAction<OCBloodsContext>
{
	public WithdrawRingOfDueling()
	{
		super("Withdraw-1", "Ring of dueling");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isBankOpen()
			&& !(ctx.hasItemOrHasEquipped(ItemID.RING_OF_DUELING1)
			|| ctx.hasItemOrHasEquipped(ItemID.RING_OF_DUELING2)
			|| ctx.hasItemOrHasEquipped(ItemID.RING_OF_DUELING3)
			|| ctx.hasItemOrHasEquipped(ItemID.RING_OF_DUELING4)
			|| ctx.hasItemOrHasEquipped(ItemID.RING_OF_DUELING5)
			|| ctx.hasItemOrHasEquipped(ItemID.RING_OF_DUELING6)
			|| ctx.hasItemOrHasEquipped(ItemID.RING_OF_DUELING7)
			|| ctx.hasItemOrHasEquipped(ItemID.RING_OF_DUELING8));
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.flag("rod-withdrawn");
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.hasItem(ItemID.RING_OF_DUELING1)
			|| ctx.hasItem(ItemID.RING_OF_DUELING2)
			|| ctx.hasItem(ItemID.RING_OF_DUELING3)
			|| ctx.hasItem(ItemID.RING_OF_DUELING4)
			|| ctx.hasItem(ItemID.RING_OF_DUELING5)
			|| ctx.hasItem(ItemID.RING_OF_DUELING6)
			|| ctx.hasItem(ItemID.RING_OF_DUELING7)
			|| ctx.hasItem(ItemID.RING_OF_DUELING8);
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.withdraw()
			.setOption("Withdraw-1", 1)
			.setItems(List.of(
				ItemID.RING_OF_DUELING1,
				ItemID.RING_OF_DUELING2,
				ItemID.RING_OF_DUELING3,
				ItemID.RING_OF_DUELING4,
				ItemID.RING_OF_DUELING5,
				ItemID.RING_OF_DUELING6,
				ItemID.RING_OF_DUELING7,
				ItemID.RING_OF_DUELING8
			))
			.override();

		ctx.flag("rod-withdrawn", true, 2);
	}
}
