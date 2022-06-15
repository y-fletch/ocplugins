package com.yfletch.ocbloods.action.bank.repair;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.ocbloods.lib.InterfaceAction;
import com.yfletch.ocbloods.lib.event.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.WidgetInfo;

public class CastNpcContact extends InterfaceAction<OCBloodsContext>
{
	public CastNpcContact()
	{
		super("Dark Mage", "NPC Contact");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.pouchNeedsRepair()
			&& ctx.hasItem(ItemID.RUNE_POUCH);
	}

	@Override
	public boolean isWorking(OCBloodsContext ctx)
	{
		return ctx.isNpcContacting();
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return ctx.dialogNpcTextContains("What do you want?")
			|| !ctx.pouchNeedsRepair();
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().widget()
			.setOption("Dark Mage", 2)
			.setWidget(WidgetInfo.SPELL_NPC_CONTACT)
			.override();
	}
}
