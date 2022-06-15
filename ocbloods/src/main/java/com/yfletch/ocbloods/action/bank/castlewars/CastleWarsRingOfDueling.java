package com.yfletch.ocbloods.action.bank.castlewars;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ItemAction;
import com.yfletch.occore.event.WrappedEvent;
import java.util.List;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.WidgetInfo;

public class CastleWarsRingOfDueling extends ItemAction<OCBloodsContext>
{
	public CastleWarsRingOfDueling()
	{
		super("Castle Wars", "Ring of dueling");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.hasItem(ItemID.BLOOD_RUNE)
			&& ctx.getOptimisticEssenceCount() == 0
			&& ctx.pouchIsEmpty()
			&& ctx.isInBloodAltar();
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.flag("rod-teleported");
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !ctx.isInBloodAltar();
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.setInventory(WidgetInfo.EQUIPMENT_RING)
			.setOption("Castle Wars", 3)
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
		ctx.flag("rod-teleported", true, 10);
	}
}
