package com.yfletch.ocgranite;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;
import com.yfletch.ocgranite.action.Prep;
import java.util.Map;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.WidgetInfo;

@Singleton
public class Runner extends ActionRunner<Context>
{
	private final static Map<String, Integer> GRANITE = Map.of(
		"Granite (5kg)", ItemID.GRANITE_5KG,
		"Granite (2kg)", ItemID.GRANITE_2KG,
		"Granite (500g)", ItemID.GRANITE_500G
	);

	private final static Map<String, Integer> HERBS = Map.of(
		"Guam leaf", ItemID.GUAM_LEAF,
		"Harralander", ItemID.HARRALANDER,
		"Marrentill", ItemID.MARRENTILL,
		"Tarromin", ItemID.TARROMIN
	);

	@Inject
	public Runner(Context context, EventBuilder eventBuilder)
	{
		super(context, eventBuilder);
	}

	@Override
	public void setup(Context context)
	{
		add(new Prep());

		// cast humidify
		add(builder().widget("Cast", "Humidify")
				.readyIf(ctx -> ctx.useHumidify()
					&& !ctx.hasItem(
					ItemID.WATERSKIN1,
					ItemID.WATERSKIN2,
					ItemID.WATERSKIN3,
					ItemID.WATERSKIN4
				))
				.workingIf(Context::isCastingHumidify)
				.doneIf(ctx -> ctx.hasItem(ItemID.WATERSKIN4))
				.onRun(
					(ctx, event) -> event.builder().widget()
						.setOption("Cast", 1)
						.setWidget(WidgetInfo.SPELL_HUMIDIFY)
						.override()
				)
		);

		// pre-mine first rock, just in case we stopped interacting
		// while dropping items
		add(builder().object("Mine", "Granite rocks (1)")
				.readyIf(ctx -> ctx.flag("stopped-interacting"))
				.doneIf(ctx -> !ctx.flag("mining"))
				.blockExtraClicks()
				.onRun(
					(ctx, event) -> {
						final var rock = ctx.getNextRock();
						event.builder().object()
							.setOption("Mine", 1)
							.setObject(rock)
							.onClick(menuEntry -> {
								ctx.setPreviousRock(rock);
							})
							.override();
					}
				)
		);

		// drop all granite
		for (final var entry : GRANITE.entrySet())
		{
			add(builder().item("Drop", entry.getKey())
					.readyIf(ctx -> ctx.hasItem(entry.getValue())
						&& (ctx.flag("mining-first-rock")
						|| ctx.getFreeInventorySlots() == 0))
					.onRun(
						(ctx, event) -> {
							var n = ctx.getNextDropIndex(entry.getKey());
							event.builder().item()
								.setOption("Drop", 7)
								.setLowPriority()
								.setItem(entry.getValue(), n)
								.onClick(menuEntry -> {
									ctx.flag("stopped-interacting", true, 2);
									ctx.onDrop(entry.getKey(), n);
								})
								.override();
						}
					)
			);
		}

		// tick manip
		for (final var entry : HERBS.entrySet())
		{
			add(builder().item("Use " + entry.getKey() + " ->", "Swamp tar")
					.readyIf(ctx -> !ctx.flag("mining")
						&& ctx.hasItem(entry.getValue()))
					.onceUntil(ctx -> ctx.flag("mining"))
					.onRun(
						(ctx, event) -> event.builder().item()
							.use(entry.getValue())
							.on(ItemID.SWAMP_TAR)
							.onClick(menuEntry -> {
								ctx.flag("mining", true, 2);
							})
							.override()
					)
			);
		}

		// click rock
		add(builder().object("Mine", "Granite rocks")
				.readyIf(ctx -> ctx.flag("mining"))
				.doneIf(ctx -> !ctx.flag("mining"))
				.blockExtraClicks()
				.onRun(
					(ctx, event) -> {
						final var rock = ctx.getNextRock();
						event.builder().object()
							.setOption("Mine", 1)
							.setObject(rock)
							.onClick(menuEntry -> {
								if (ctx.isFirstRock(rock))
								{
									ctx.flag("mining-first-rock", true, 2);
								}
								else
								{
									ctx.setPreviousRock(rock);
								}
							})
							.override();
					}
				)
		);
	}
}
