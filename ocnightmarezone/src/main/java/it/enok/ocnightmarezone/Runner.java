package it.enok.ocnightmarezone;

import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;
import it.enok.ocnightmarezone.config.ItemOption;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;

import java.util.Map;

@Singleton
public class Runner extends ActionRunner<Context>
{
	private static final int[] absorptionPotions = new int[] {
			ItemID.ABSORPTION_1,
			ItemID.ABSORPTION_2,
			ItemID.ABSORPTION_3,
			ItemID.ABSORPTION_4
	};

	private static final int TICK_FOOD_EAT = 3;
	private static final int TICK_POTION_ABSORB = 3;
	private static final int TICK_POTION_COMBAT = 9;

	private static final int TICK_OVERLOAD_POTION = 12;

	@Inject
	public Runner(Context context, EventBuilder eventBuilder)
	{
		super(context, eventBuilder);
	}

	@Override
	public void setup(Context context)
	{
		add(builder().prep().withConditions(ctx -> Map.of(
				"Need damaging item", !ctx.usingDamageItem() || ctx.hasItem(ctx.getDamageItemId()),
				"Need absorption potions", !ctx.useAbsorptionPotion()
						|| ctx.hasItem(absorptionPotions)
						|| !ctx.hasItem(absorptionPotions) && ctx.inInstancedRegion(),
				"Need combat potions", !ctx.usingPotionOption()
						|| ctx.hasItem(ctx.getPotionOptionIds())
						|| !ctx.hasItem(ctx.getPotionOptionIds()) && ctx.inInstancedRegion(),
				"Not in NMZ", ctx.inInstancedRegion()
		)));

		/*
		 * Combat boosts
		 */

		add(builder().item("Drink", context.getPotionOptionLabel())
				.readyIf(ctx -> ctx.usingPotionOption()
						&& ctx.drinkCombatPotion()
						&& ctx.hasItem(ctx.getPotionOptionIds()))
				.doneIf(ctx -> !ctx.drinkCombatPotion())
				.onRun(
						(ctx, event) -> {
							// Overload potions do 50 damage over 7 seconds and
							// any damage during that time will kill the player
							if (ctx.usingOverloadPotion())
							{
								ctx.flag("overloadPotion", true, TICK_OVERLOAD_POTION);
							}
							event.builder().item()
									.setOption("Drink", 2)
									.setItems(Ints.asList(ctx.getPotionOptionIds()))
									.setType(MenuAction.CC_OP)
									.onClick(e -> ctx.flag("combatPotion", true, TICK_POTION_COMBAT))
									.override();
						}
				)
		);

		/*
		 * Absorption
		 */

		add(builder().item("Drink", "Absorption")
				.readyIf(ctx -> ctx.useAbsorptionPotion()
						&& ctx.drinkAbsorptionPotion()
						&& ctx.hasItem(absorptionPotions))
				.doneIf(ctx -> !ctx.drinkAbsorptionPotion())
				.onRun(
						(ctx, event) -> event.builder().item()
								.setOption("Drink", 2)
								.setItems(Ints.asList(absorptionPotions))
								.setType(MenuAction.CC_OP)
								.onClick(e -> ctx.flag("drinkAbsorption", true, TICK_POTION_ABSORB))
								.override()

				)
		);

		/*
		 * Damage Item
		 */

		add(builder().item("Guzzle", ItemOption.ROCK_CAKE.getLabel())
				.readyIf(ctx -> ctx.usingRockCake()
						&& ctx.getCurrentPlayerHealth() >= 2
						&& !ctx.flag("overloadPotion"))
				.doneIf(ctx -> ctx.getCurrentPlayerHealth() == 1)
				.onRun(
						(ctx, event) -> event.builder().item()
								.setOption("Guzzle", 4)
								.setItem(ItemOption.ROCK_CAKE.getItemId())
								.setType(MenuAction.CC_OP)
								.onClick(e -> ctx.flag("eat", true, TICK_FOOD_EAT))
								.override()
				)
		);

		add(builder().item("Feel", ItemOption.LOCATOR_ORB.getLabel())
				.readyIf(ctx -> ctx.usingLocatorOrb()
						&& ctx.getCurrentPlayerHealth() >= 2
						&& !ctx.flag("overloadPotion"))
				.doneIf(ctx -> ctx.getCurrentPlayerHealth() == 1)
				.onRun(
						(ctx, event) -> event.builder().item()
								.setOption("Feel", 2)
								.setItem(ItemOption.LOCATOR_ORB.getItemId())
								.setType(MenuAction.CC_OP)
								.onClick(e -> ctx.flag("eat", true, TICK_FOOD_EAT))
								.override()
				)
		);

		/*
		 * TODO: Buffs
		 *   - Recurrent damage - adds an additional hit (lasts 45s)
		 *   - Zapper - damages nearby monsters over time (lasts 60s)
		 *   - power surge: fills the special attack bar and regenerates (lasts 45s)
		 *   - Ultimate force - instantly kills all bosses (does not count points)
		 *
		 * Probably going to put this under a "points mode" that will focus entirely on getting points rather than
		 * max xp.
		 */

		/*
		 * TODO: Priority focus
		 *
		 * Have a priority system that focuses on specific NPCs when they spawn. There might have to be two different
		 * queues: One for idling in the middle of the room, second for chasing down NPCs when they spawn.
		 */

		/*
		 * TODO: Move to center
		 *
		 * In order to maintain constant attacks, it is advisable to stay in the center of the room so all NPCs have to
		 * walk the minimum distance to reach the player. That being said, this could interfere with the priority focus.
		 */

		add(builder().consume("Wait").readyIf(ctx -> true));
	}
}
