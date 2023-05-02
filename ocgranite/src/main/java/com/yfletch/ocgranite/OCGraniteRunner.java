package com.yfletch.ocgranite;

import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;
import com.yfletch.ocgranite.action.CastHumidify;
import com.yfletch.ocgranite.action.Prep;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.ItemID;

@Singleton
public class OCGraniteRunner extends ActionRunner<OCGraniteContext>
{

	@Inject
	public OCGraniteRunner(OCGraniteContext context, EventBuilder eventBuilder)
	{
		super(context, eventBuilder);

		add(new Prep());
		add(new CastHumidify());

		// tar/herb
		// click rock

		// should always be doing mixing animation

		// interrupt
		var herbs = Map.of(
			"Guam leaf", ItemID.GUAM_LEAF,
			"Harralander", ItemID.HARRALANDER,
			"Marrentill", ItemID.MARRENTILL,
			"Tarromin", ItemID.TARROMIN
		);

		for (var entry : herbs.entrySet())
		{
			add(builder().item("Use " + entry.getKey(), "Swamp tar")
				.readyIf(
					ctx -> (ctx.getTick() > 4 || ctx.receivedGranite())
						&& !ctx.flag("interrupt")
						&& ctx.hasItem(entry.getValue())
				)
				.onRun(
					(ctx, event) -> event.builder().item()
						.use(entry.getValue())
						.on(ItemID.SWAMP_TAR)
						.onClick((menuEntry) -> {
							ctx.flag("interrupt", true);
						})
						.override()
				)
			);
		}

		add(builder().object("Mine", "Granite rocks")
			.readyIf(
				ctx -> ctx.flag("interrupt")
			)
			.workingIf(
				ctx -> ctx.isPathingTo(ctx.getNextRock())
					|| ctx.getTick() <= 3
			)
			.onRun(
				(ctx, event) -> {
					var rock = ctx.getNextRock();
					event.builder().object()
						.setOption("Mine", 1)
						.setObject(rock)
						.onClick(
							menuEntry -> {
								ctx.flag("interrupt", false);
								ctx.beginMining();
								ctx.setPreviousRock(rock);
							}
						)
						.override();
				}
			)
		);

		var granite = Map.of(
			"Granite (500g)", ItemID.GRANITE_500G,
			"Granite (2kg)", ItemID.GRANITE_2KG,
			"Granite (5kg)", ItemID.GRANITE_5KG
		);

		for (var entry : granite.entrySet())
		{
			// drop granite
			add(builder().item("Drop", entry.getKey())
				.readyIf(
					ctx -> ctx.hasItem(entry.getValue())
						&& ctx.isTick(3)
				)
				.onRun(
					(ctx, event) -> event.builder().item()
						.setItem(entry.getValue())
						.setOption("Drop", 7)
						.setLowPriority()
						.override()
				)
			);
		}

		add(builder().consume("No action"));
	}
}
