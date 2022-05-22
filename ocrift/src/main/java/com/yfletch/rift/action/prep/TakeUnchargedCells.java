package com.yfletch.rift.action.prep;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;

public class TakeUnchargedCells extends ObjectAction<RiftContext>
{
	public TakeUnchargedCells()
	{
		super("Take-10", "Uncharged cells");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.getGameTime() < 0
			&& ctx.getItemCount(ItemID.UNCHARGED_CELL) < 10;
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingTo(ObjectID.UNCHARGED_CELLS_43732);
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.getItemCount(ItemID.UNCHARGED_CELL) == 10;
	}
}
