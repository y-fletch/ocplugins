package com.yfletch.rift.action.pregame;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
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

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideObjectAction(
			"Take-10",
			MenuAction.GAME_OBJECT_FOURTH_OPTION,
			ObjectID.UNCHARGED_CELLS_43732
		);
	}
}
