package com.yfletch.rift.action.postgame;

import com.yfletch.rift.enums.Cell;
import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ItemAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;

public class DropCell extends ItemAction<RiftContext>
{
	public DropCell()
	{
		super("Drop", "Cell");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.getGameTime() < 0
			&& ctx.hasCell()
			&& !ctx.hasItem(Cell.WEAK.getItemId());
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return !ctx.hasCell();
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideItemAction(7, MenuAction.CC_OP_LOW_PRIORITY, ctx.getCell());
	}
}
