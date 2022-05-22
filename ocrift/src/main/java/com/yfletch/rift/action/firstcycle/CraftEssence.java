package com.yfletch.rift.action.firstcycle;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;

public class CraftEssence extends ObjectAction<RiftContext>
{
	public CraftEssence()
	{
		super("Work-at", "Workbench");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.getGameTime() > 85
			&& ctx.getItemCount(ItemID.GUARDIAN_ESSENCE) == 0;
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingTo(ObjectID.WORKBENCH_43754)
			&& ctx.isCraftingEssence();
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.getItemCount(ItemID.GUARDIAN_ESSENCE) == 19;
	}
}
