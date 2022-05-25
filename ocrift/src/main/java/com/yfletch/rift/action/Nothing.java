package com.yfletch.rift.action;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.Action;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.client.ui.overlay.components.LineComponent;

public class Nothing extends Action<RiftContext>
{
	@Override
	public LineComponent getDisplayLine(RiftContext ctx)
	{
		return LineComponent.builder().left("Nothing").build();
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return true;
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return false;
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.consume();
	}
}
