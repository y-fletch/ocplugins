package com.yfletch.rift.action.cycle.common;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ItemAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;

public class DropRunes extends ItemAction<RiftContext>
{
	public DropRunes()
	{
		super("Drop", "Rune");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return !ctx.isOutsideRift()
			&& ctx.hasGuardianStones()
			&& ctx.hasDroppableRunes();
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return !ctx.hasDroppableRunes();
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideItemAction(7, MenuAction.CC_OP_LOW_PRIORITY, ctx.getDroppableRune());
	}
}
