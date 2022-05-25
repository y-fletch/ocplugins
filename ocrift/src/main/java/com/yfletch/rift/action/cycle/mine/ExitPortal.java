package com.yfletch.rift.action.cycle.mine;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;

public class ExitPortal extends ObjectAction<RiftContext>
{
	// object id SHOULD be ObjectID.PORTAL_43730
	// but for some reason this is the ID clicked when
	// normally clicking the portal?
	private final int PORTAL = 38044;

	public ExitPortal()
	{
		super("Enter", "Portal");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.isInHugeMine()
			&& ctx.areAllPouchesFull()
			&& ctx.getFreeInventorySlots() == 0;
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingTo(PORTAL);
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return !ctx.isInHugeMine();
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideObjectAction("Enter", MenuAction.GAME_OBJECT_FIRST_OPTION, PORTAL);
		ctx.flag("mining", false);
	}
}
