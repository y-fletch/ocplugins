package com.yfletch.rift.action.cycle.common;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectID;

public class DepositRunes extends ObjectAction<RiftContext>
{
	public DepositRunes()
	{
		super("Deposit-runes", "Deposit pool");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return !ctx.isOutsideRift()
			&& ctx.hasRunes();
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingTo(ObjectID.DEPOSIT_POOL);
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return !ctx.hasRunes();
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideObjectAction("Deposit-runes", MenuAction.GAME_OBJECT_FIRST_OPTION, ObjectID.DEPOSIT_POOL);
	}
}
