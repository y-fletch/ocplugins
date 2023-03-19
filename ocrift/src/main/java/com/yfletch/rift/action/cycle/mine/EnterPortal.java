package com.yfletch.rift.action.cycle.mine;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;

public class EnterPortal extends ObjectAction<RiftContext>
{
	public EnterPortal()
	{
		super("Enter", "Portal");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return !ctx.isOutsideRift()
			&& !ctx.hasRunes()
			&& !ctx.hasGuardianStones()
			&& !ctx.hasCell()
			&& ctx.getHugeEssencePortal() != null
			&& ctx.getGuardianPower() < 94;
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingTo(ctx.getHugeEssencePortal());
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.isInHugeMine();
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideObjectAction("Enter", MenuAction.GAME_OBJECT_FIRST_OPTION, ctx.getHugeEssencePortal());
	}
}
