package com.yfletch.ocbloods.action.craft;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.ocbloods.lib.ItemAction;
import com.yfletch.ocbloods.lib.event.WrappedEvent;
import net.runelite.api.ItemID;

public class ActivateBloodEssence extends ItemAction<OCBloodsContext>
{
	public ActivateBloodEssence()
	{
		super("Activate", "Blood essence");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.getConfig().useBloodEssence()
			&& ctx.isInBloodAltar()
			&& !ctx.hasItem(ItemID.BLOOD_ESSENCE_ACTIVE)
			&& ctx.hasItem(ItemID.BLOOD_ESSENCE);
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.hasItem(ItemID.BLOOD_ESSENCE_ACTIVE);
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.setOption("Activate", 2)
			.setItem(ItemID.BLOOD_ESSENCE)
			.override();
	}
}
