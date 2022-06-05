package com.yfletch.rift.action;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ItemAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;

public class EquipVarrockArmour extends ItemAction<RiftContext>
{
	public EquipVarrockArmour()
	{
		super("Wear", "Varrock armour");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		if (!ctx.getConfig().switchBody()
			|| ctx.hasEqupped(ItemID.VARROCK_ARMOUR_1)
			|| ctx.hasEqupped(ItemID.VARROCK_ARMOUR_2)
			|| ctx.hasEqupped(ItemID.VARROCK_ARMOUR_3)
			|| ctx.hasEqupped(ItemID.VARROCK_ARMOUR_4))
		{
			return false;
		}

		// MineGuardianParts
		return !ctx.isInHugeMine()
			&& ctx.getHugeEssencePortal() == null
			&& ctx.flag("mine-cycle")
			&& !ctx.isOutsideRift()

			// MineLargeRemains
			|| ctx.getGameTime() >= 0
			&& ctx.getGameTime() < ctx.getExitMineTime()
			&& ctx.isInLargeMine()

			// MineHugeRemains
			|| ctx.isInHugeMine()
			&& ctx.getFreeInventorySlots() > 0;
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.hasEqupped(ItemID.VARROCK_ARMOUR_1)
			|| ctx.hasEqupped(ItemID.VARROCK_ARMOUR_2)
			|| ctx.hasEqupped(ItemID.VARROCK_ARMOUR_3)
			|| ctx.hasEqupped(ItemID.VARROCK_ARMOUR_4);
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		if (ctx.hasItem(ItemID.VARROCK_ARMOUR_1))
		{
			event.overrideItemAction(3, MenuAction.CC_OP, ItemID.VARROCK_ARMOUR_1);
		}

		if (ctx.hasItem(ItemID.VARROCK_ARMOUR_2))
		{
			event.overrideItemAction(3, MenuAction.CC_OP, ItemID.VARROCK_ARMOUR_2);
		}

		if (ctx.hasItem(ItemID.VARROCK_ARMOUR_3))
		{
			event.overrideItemAction(3, MenuAction.CC_OP, ItemID.VARROCK_ARMOUR_3);
		}

		if (ctx.hasItem(ItemID.VARROCK_ARMOUR_4))
		{
			event.overrideItemAction(3, MenuAction.CC_OP, ItemID.VARROCK_ARMOUR_4);
		}
	}
}
