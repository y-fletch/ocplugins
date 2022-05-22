package com.yfletch.rift.action;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.Action;
import net.runelite.client.ui.overlay.components.LineComponent;

public class Unknown extends Action<RiftContext>
{
	@Override
	public LineComponent getDisplayLine(RiftContext ctx)
	{
		return LineComponent.builder().left("Unknown").build();
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return true;
	}
}
