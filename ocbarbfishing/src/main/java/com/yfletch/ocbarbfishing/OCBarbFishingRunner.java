package com.yfletch.ocbarbfishing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.ItemID;

@Singleton
public class OCBarbFishingRunner extends ActionRunner<OCBarbFishingContext>
{
	@Inject
	public OCBarbFishingRunner(OCBarbFishingContext context, EventBuilder eventBuilder)
	{
		super(context, eventBuilder);

		add(builder().prep()
				.withConditions(ctx -> {
					HashMap<String, Boolean> map = new HashMap<>();

					map.put("Not near fishing spots", ctx.isAtFishingSpots());
					map.put("Missing barbarian rod", ctx.hasItem(ItemID.BARBARIAN_ROD));
					map.put("Missing feathers", ctx.hasItem(ItemID.FEATHER));

					if (ctx.isCutEatMethod() || ctx.isHybridMethod())
					{
						map.put("Missing knife", ctx.hasItem(ItemID.KNIFE));
					}

					if (ctx.isTarDropMethod() || ctx.isHybridMethod())
					{
						map.put("Missing guam leaf", ctx.hasItem(ItemID.GUAM_LEAF));
						map.put("Missing swamp tar", ctx.hasItem(ItemID.SWAMP_TAR));
						map.put("Missing pestle and mortar", ctx.hasItem(ItemID.PESTLE_AND_MORTAR));
					}

					return map;
				})
		);

		// ticks:
		//       1          2          3
		// [click spot] [rod out] [interrupt/click spot/secondary]

		// start fishing
		add(builder().npc("Use-rod", "Fishing spot")
				.readyIf(
					// begin cycle on tick 6 if not already started
					ctx -> ctx.getTick() > 5
						// in cycle, only do actions on tick 3 if the
						// interrupt has happened
						|| (ctx.isTick(3)
						&& ctx.flag("interrupt")
						&& (ctx.flag("secondary")
						// allow skipping secondary if tar/drop and no fish to drop
						|| ctx.hasNoFish() && ctx.isTarDropMethod())
						// allow skipping seocndary if cut/eat and no food to eat
						|| ctx.hasNoFish() && !ctx.hasFood() && ctx.isCutEatMethod()))
				.workingIf(
					// prevent spam-clicking fishing spot
					ctx -> ctx.isFishing()
						|| (ctx.flag("fishing")
						// if counter is at 10 ticks, fishing spot
						// probably moved
						&& ctx.getTick() < 10)
				)
				.onDone(ctx -> System.out.println("poop"))
				.onRun(
					(ctx, event) -> {
						ctx.flag("interrupt", false);
						ctx.flag("secondary", false);
						event.builder().npc()
							.setNpc(1542)
							.setOption("Use-rod", 1)
							.onClick(
								menuEntry -> ctx.flag("fishing", true)
							)
							.override();
					}
				)
		);

		var fish = Map.of(
			"Leaping trout", ItemID.LEAPING_TROUT,
			"Leaping salmon", ItemID.LEAPING_SALMON,
			"Leaping sturgeon", ItemID.LEAPING_STURGEON
		);

		// CUT/EAT

		for (var entry : fish.entrySet())
		{
			// interrupt t3
			add(builder().item("Use Knife", entry.getKey())
					.readyIf(
						ctx -> !ctx.isTarDropMethod()
							&& ctx.isTick(2)
							&& ctx.hasItem(entry.getValue())
							&& !ctx.flag("interrupt")
					)
					.workingIf(
						ctx -> ctx.flag("interrupt")
					)
					.onRun(
						(ctx, event) -> event.builder().item()
							.use(ItemID.KNIFE)
							.on(entry.getValue())
							.onClick((menuEntry) -> {
								ctx.flag("fishing", false);
								ctx.flag("interrupt", true);
							})
							.override()
					)
			);
		}

		var food = Map.of(
			"Roe", ItemID.ROE,
			"Caviar", ItemID.CAVIAR
		);

		for (var entry : food.entrySet())
		{
			// eat roe/caviar
			add(builder().item("Eat", entry.getKey())
					.readyIf(
						ctx -> !ctx.isTarDropMethod()
							&& ctx.hasItem(entry.getValue())
							&& ctx.flag("interrupt")
							&& !ctx.flag("secondary")
					)
					.workingIf(
						ctx -> ctx.flag("secondary")
					)
					.onRun(
						(ctx, event) -> event.builder().item()
							.setItem(entry.getValue())
							.setOption("Eat", 2)
							.onClick(
								menuEntry -> ctx.flag("secondary", true)
							)
							.override()
					)
			);
		}

		// TAR/DROP

		// interrupt
		add(builder().item("Use Guam leaf", "Swamp tar")
				.readyIf(
					ctx -> !ctx.isCutEatMethod()
						&& ctx.isTick(2)
						&& !ctx.flag("interrupt")
				)
				.onRun(
					(ctx, event) -> event.builder().item()
						.use(ItemID.GUAM_LEAF)
						.on(ItemID.SWAMP_TAR)
						.onClick((menuEntry) -> {
							ctx.flag("fishing", false);
							ctx.flag("interrupt", true);
						})
						.override()
				)
		);

		for (var entry : fish.entrySet())
		{
			// drop fish
			add(builder().item("Drop", entry.getKey())
					.readyIf(
						ctx -> ctx.isTarDropMethod()
							&& ctx.hasItem(entry.getValue())
							&& ctx.flag("interrupt")
							&& !ctx.flag("secondary")
					)
					.onRun(
						(ctx, event) -> event.builder().item()
							.setItem(entry.getValue())
							.setOption("Drop", 7)
							.setLowPriority()
							.onClick(
								menuEntry -> ctx.flag("secondary", true)
							)
							.override()
					)
			);
		}

		add(builder().consume("No action"));
	}
}
