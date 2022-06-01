package com.yfletch.rift.action.repair;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.InterfaceAction;
import com.yfletch.rift.lib.WrappedEvent;

public class RepairPouches extends InterfaceAction<RiftContext>
{
	public RepairPouches()
	{
		super("Continue", null);
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.getDialogOptionIndex("Can you repair my pouches?") > 0;
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.dialogPlayerTextContains("Can you repair my pouches?");
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideDialogOptionAction("Can you repair my pouches?");
	}
}
