package com.yfletch.rift.action.pregame;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.InterfaceAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.WidgetInfo;

public class UseSpecialAttack extends InterfaceAction<RiftContext>
{
	public UseSpecialAttack()
	{
		super("Use", "Special Attack");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		if (!ctx.hasAnyEquipped(
				ItemID.DRAGON_PICKAXE,
				ItemID.DRAGON_PICKAXE_OR,
				ItemID.DRAGON_PICKAXE_12797,
				ItemID.DRAGON_PICKAXE_OR_25376,
				ItemID.INFERNAL_PICKAXE,
				ItemID.INFERNAL_PICKAXE_OR)
		) return false;

		return (ctx.getGameTime() < 0
			&& ctx.getGameTime() > -5
			&& ctx.isInLargeMine()
			&& ctx.getSpecialEnergy() == 100)
			|| (ctx.isInHugeMine()
			&& ctx.getSpecialEnergy() == 100
			&& ctx.getGuardianPower() < 50);
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.getSpecialEnergy() == 0;
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideInterfaceAction("Use", MenuAction.CC_OP, WidgetInfo.MINIMAP_SPEC_CLICKBOX.getId());
	}
}
