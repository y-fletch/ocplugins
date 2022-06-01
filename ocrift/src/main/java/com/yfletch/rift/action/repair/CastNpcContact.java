package com.yfletch.rift.action.repair;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.InterfaceAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.WidgetInfo;

public class CastNpcContact extends InterfaceAction<RiftContext>
{
	public CastNpcContact()
	{
		super("Dark Mage", "NPC Contact");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.isPregame()
			&& ctx.getConfig().repairPouches()
			&& !ctx.isDialogOpen()
			&& ctx.getGameTime() <= -20
			&& (ctx.hasDegradedPouch()
			|| ctx.hasNearlyDegradedPouch());
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isNpcContacting();
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.dialogNpcTextContains("What do you want?");
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideInterfaceAction("Dark Mage", MenuAction.CC_OP, WidgetInfo.SPELL_NPC_CONTACT.getId(), 2);
	}
}
