package com.yfletch.ocbloods.action.bank.castlewars;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.ocbloods.lib.ItemAction;
import com.yfletch.ocbloods.lib.event.WrappedEvent;
import java.util.List;
import net.runelite.api.ItemID;

public class WearRingOfDueling extends ItemAction<OCBloodsContext>
{
	public WearRingOfDueling()
	{
		super("Wear", "Ring of dueling");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isBankOpen()
			&& (ctx.hasItem(ItemID.RING_OF_DUELING1)
			|| ctx.hasItem(ItemID.RING_OF_DUELING2)
			|| ctx.hasItem(ItemID.RING_OF_DUELING3)
			|| ctx.hasItem(ItemID.RING_OF_DUELING4)
			|| ctx.hasItem(ItemID.RING_OF_DUELING5)
			|| ctx.hasItem(ItemID.RING_OF_DUELING6)
			|| ctx.hasItem(ItemID.RING_OF_DUELING7)
			|| ctx.hasItem(ItemID.RING_OF_DUELING8));
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !(ctx.hasItem(ItemID.RING_OF_DUELING1)
			|| ctx.hasItem(ItemID.RING_OF_DUELING2)
			|| ctx.hasItem(ItemID.RING_OF_DUELING3)
			|| ctx.hasItem(ItemID.RING_OF_DUELING4)
			|| ctx.hasItem(ItemID.RING_OF_DUELING5)
			|| ctx.hasItem(ItemID.RING_OF_DUELING6)
			|| ctx.hasItem(ItemID.RING_OF_DUELING7)
			|| ctx.hasItem(ItemID.RING_OF_DUELING8));
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.deposit()
			.setOption("Wear", 9)
			.setLowPriority()
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
	}
}
