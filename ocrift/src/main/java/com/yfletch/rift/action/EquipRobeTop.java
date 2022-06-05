package com.yfletch.rift.action;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ItemAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;

public class EquipRobeTop extends ItemAction<RiftContext>
{
	public EquipRobeTop()
	{
		super("Wear", "Robe top of the eye");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		if (!ctx.getConfig().switchBody()
			|| ctx.hasEqupped(ItemID.ROBE_TOP_OF_THE_EYE)
			|| ctx.hasEqupped(ItemID.ROBE_TOP_OF_THE_EYE_BLUE)
			|| ctx.hasEqupped(ItemID.ROBE_TOP_OF_THE_EYE_GREEN)
			|| ctx.hasEqupped(ItemID.ROBE_TOP_OF_THE_EYE_RED))
		{
			return false;
		}

		// CraftRunes
		return ctx.isOutsideRift()
			&& ctx.getOptimisticEssenceCount() > 0;
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.hasEqupped(ItemID.ROBE_TOP_OF_THE_EYE)
			|| ctx.hasEqupped(ItemID.ROBE_TOP_OF_THE_EYE_BLUE)
			|| ctx.hasEqupped(ItemID.ROBE_TOP_OF_THE_EYE_GREEN)
			|| ctx.hasEqupped(ItemID.ROBE_TOP_OF_THE_EYE_RED);
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		if (ctx.hasItem(ItemID.ROBE_TOP_OF_THE_EYE))
		{
			event.overrideItemAction(3, MenuAction.CC_OP, ItemID.ROBE_TOP_OF_THE_EYE);
		}

		if (ctx.hasItem(ItemID.ROBE_TOP_OF_THE_EYE_BLUE))
		{
			event.overrideItemAction(3, MenuAction.CC_OP, ItemID.ROBE_TOP_OF_THE_EYE_BLUE);
		}

		if (ctx.hasItem(ItemID.ROBE_TOP_OF_THE_EYE_GREEN))
		{
			event.overrideItemAction(3, MenuAction.CC_OP, ItemID.ROBE_TOP_OF_THE_EYE_GREEN);
		}

		if (ctx.hasItem(ItemID.ROBE_TOP_OF_THE_EYE_RED))
		{
			event.overrideItemAction(3, MenuAction.CC_OP, ItemID.ROBE_TOP_OF_THE_EYE_RED);
		}
	}
}
