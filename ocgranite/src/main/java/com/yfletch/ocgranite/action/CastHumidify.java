package com.yfletch.ocgranite.action;

import com.yfletch.occore.action.WidgetAction;
import com.yfletch.occore.event.WrappedEvent;
import com.yfletch.ocgranite.OCGraniteContext;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.WidgetInfo;

public class CastHumidify extends WidgetAction<OCGraniteContext>
{
	public CastHumidify()
	{
		super("Cast", "Humidify");
	}

	@Override
	public boolean isReady(OCGraniteContext ctx)
	{
		return ctx.useHumidify()
			&& !ctx.hasItem(
			ItemID.WATERSKIN1,
			ItemID.WATERSKIN2,
			ItemID.WATERSKIN3,
			ItemID.WATERSKIN4
		);
	}

	@Override
	public boolean isWorking(OCGraniteContext ctx)
	{
		return ctx.isCastingHumidify();
	}

	@Override
	public boolean isDone(OCGraniteContext ctx)
	{
		return ctx.hasItem(ItemID.WATERSKIN4);
	}

	@Override
	public void run(OCGraniteContext ctx, WrappedEvent event)
	{
		event.builder().widget()
			.setOption("Cast", 1)
			.setWidget(WidgetInfo.SPELL_HUMIDIFY)
			.override();
	}
}
