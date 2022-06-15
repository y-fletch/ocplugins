package com.yfletch.ocbloods.action.bank.castlewars;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.ocbloods.lib.ObjectAction;
import com.yfletch.ocbloods.lib.event.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;

public class UseCastleWarsBank extends ObjectAction<OCBloodsContext>
{
	public UseCastleWarsBank()
	{
		super("Use", "Bank chest");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.isInCastleWars()
			&& !ctx.isBankOpen()
			&& (ctx.hasItem(ItemID.BLOOD_RUNE)
			|| ctx.hasItem(ItemID.RUNE_POUCH));
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.isPathingTo(ObjectID.BANK_CHEST_4483);
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.isBankOpen();
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().object()
			.setObject(ObjectID.BANK_CHEST_4483)
			.setOption("Use", 1)
			.override();
	}
}
