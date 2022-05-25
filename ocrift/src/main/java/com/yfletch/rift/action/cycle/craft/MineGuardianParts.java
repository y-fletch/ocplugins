package com.yfletch.rift.action.cycle.craft;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;

// A.K.A "Wait for portal"
public class MineGuardianParts extends ObjectAction<RiftContext>
{
	public MineGuardianParts()
	{
		super("Mine", "Guardian parts");
	}

	private TileObject getTarget(RiftContext ctx)
	{
		return ctx.getObjectHelper().getNearest(
			ObjectID.GUARDIAN_PARTS,
			ctx.getObjectHelper().getNearest(ObjectID.UNCHARGED_CELLS_43732)
		);
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return !ctx.isInHugeMine()
			&& ctx.getHugeEssencePortal() == null
			&& ctx.flag("mine-cycle");
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingTo(getTarget(ctx)) || ctx.isMining();
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.getHugeEssencePortal() != null;
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideObjectAction(
			"Mine",
			MenuAction.GAME_OBJECT_FIRST_OPTION,
			getTarget(ctx)
		);
	}
}
