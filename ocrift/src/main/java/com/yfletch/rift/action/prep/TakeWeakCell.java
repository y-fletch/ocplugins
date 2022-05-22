package com.yfletch.rift.action.prep;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;

public class TakeWeakCell extends ObjectAction<RiftContext>
{
	public TakeWeakCell()
	{
		super("Take", "Weak cells");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.getGameTime() < 0
			&& ctx.getItemCount(ItemID.WEAK_CELL) == 0;
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingTo(ObjectID.WEAK_CELLS);
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.getItemCount(ItemID.WEAK_CELL) > 0;
	}
}
