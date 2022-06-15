package com.yfletch.ocbloods.action.travel.caves;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.ItemAction;
import com.yfletch.occore.event.WrappedEvent;
import java.util.List;
import net.runelite.api.ItemID;

public class EatSummerPie extends ItemAction<OCBloodsContext>
{
	public EatSummerPie()
	{
		super("Eat", "Summer pie");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return (ctx.isInTunnel3() || ctx.isInTunnel4())
			&& ctx.getAgilityLevel() < 93
			&& (ctx.hasItem(ItemID.SUMMER_PIE)
			|| ctx.hasItem(ItemID.HALF_A_SUMMER_PIE));
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.flag("ate-pie")
			|| ctx.getAgilityLevel() >= 93;
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().item()
			.setOption("Eat", 2)
			.setItems(List.of(
				ItemID.SUMMER_PIE,
				ItemID.HALF_A_SUMMER_PIE
			))
			.override();

		ctx.flag("ate-pie", true, 10);
	}
}
