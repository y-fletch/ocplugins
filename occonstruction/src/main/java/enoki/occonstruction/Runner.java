package enoki.occonstruction;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;
import enoki.occonstruction.config.Buildable;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.unethicalite.client.Static;
import net.unethicalite.api.entities.NPCs;

@Slf4j
@Singleton
public class Runner extends ActionRunner<Context>
{
	@Inject private Client client;

	private static final int CLICK_YES = 14352385;

	@Inject
	public Runner(Context context, EventBuilder eventBuilder)
	{
		super(context, eventBuilder);
	}

	@Override
	public void setup(Context context)
	{
		// can only get item compositions on client thread
		if (!Static.getClient().isClientThread())
		{
			return;
		}

//		add(builder().prep().withConditions(ctx -> Map.of(
//				"You need a hammer", ctx.hasHammer(),
//				"You need a saw", ctx.hasSaw(),
//				"You need some coins", ctx.hasCoins(),
//				"Unable to find portal", ctx.getNearestEntryPortal() != null
//						|| ctx.getNearestExitPortal() != null,
//				"You need noted planks", !ctx.useServant()
//						&& ctx.getPlankCount() > 0
//		)));

		/*
		 * No butler route
		 */

		add(builder().object("Enter", "Portal")
				.readyIf(ctx -> ctx.inHome() && !ctx.hasPlanks())
				.doneIf(ctx -> !ctx.inHome())
				.workingIf(ctx -> ctx.isPathingTo(ctx.getNearestPortal()))
				.onRun(
						(ctx, event) -> event.builder().object()
									.setOption("Enter", 1)
									.setObject(ctx.getNearestPortal())
									.override()
				));

		add(builder().npc("Use plank", "Phials")
				.readyIf(ctx -> !ctx.hasPlanks() && ctx.getPhials() != null)
				.doneIf(Context::isDialogOpen)
				.workingIf(ctx -> ctx.flag("phials"))
				.onRun(
						(ctx, event) -> {
							event.builder().npc()
									.use(ItemID.OAK_PLANK + 1)
									.on(NpcID.PHIALS)
									.override();
//							ctx.flag("phials", true, 10);
						}
				));

		add(builder().widget("Exchange All")
				.readyIf(ctx -> !ctx.inHome() && ctx.isDialogOpen())
				.doneIf(ctx -> !ctx.isDialogOpen())
				.onRun((ctx, event) -> event.builder().widget()
						.setDialogOption("Exchange All")
						.override()
				)
		);

		add(builder().object("Build mode", "Portal")
				.readyIf(ctx -> !ctx.inHome())
				.doneIf(Context::inHome)
				.workingIf(ctx -> ctx.isPathingTo(ctx.getNearestPortal()))
				.onRun(
						(ctx, event) -> event.builder().object()
								.setOption("Build", 3)
								.setObject(ctx.getNearestPortal())
								.override()
				));

		/*
		 * Demon Butler
		 *
		 * There should not be any need to walk next to the buildable first as
		 * they always reappear beside the player after the trip.
		 *   1. Send them away
		 *   2. Build some stuff
		 *   3. They appear beside you
		 *   4. Rinse and repeat
		 */

//		add(builder().widget("Demon Butler: Open summon menu")
//				.readyIf(ctx -> ctx.useServant() && ctx.getServant() != null)
//				.doneIf(Context::isHouseOptionsOpen)
//				.onRun(
//						(ctx, event) -> event.builder().widget()
//								.setWidget(7602250)
//								.setOption("View House Options", 1)
//								.override()
//				)
//		);

//		add(builder().widget("Demon Butler: Summon")
//				.readyIf(ctx -> ctx.useServant()
//						&& ctx.getServant() != null
//						&& ctx.isHouseOptionsOpen())
//				.doneIf(Context::isDialogOpen)
//				.onRun(
//						(ctx, event) -> event.builder().widget()
//								.setWidget(24248342)
//								.setOption("Call Servant", 1)
//								.override()
//				)
//		);

// 		add(builder().widget("Talk: Repeat last task")
//				.readyIf(ctx -> ctx.useServant() && ctx.getServant() != null)
//				.doneIf()
//				.onRun(
//						// TODO: Talk to Butler
//						(ctx, event) -> event.builder().npc()
//				)
//		);

//		add(builder().widget("Talk: Go to bank")
//				.readyIf(ctx -> ctx.useServant() && ctx.getServant() != null)
//				.doneIf()
//				.onRun(
//						// TODO: Talk to Butler
//						(ctx, event) -> event.builder().npc()
//				)
//		);

//		add(builder().widget("Talk: Make a withdrawal")
//				.readyIf(ctx -> ctx.useServant() && ctx.getServant() != null)
//				.doneIf()
//				.onRun(
//						// TODO: Talk to Butler
//						(ctx, event) -> event.builder().npc()
//				)
//		);

//		add(builder().widget("Talk: Ask for planks")
//				.readyIf(ctx -> ctx.useServant() && ctx.getServant() != null)
//				.doneIf()
//				.onRun(
//						// TODO: Talk to Butler
//						(ctx, event) -> event.builder().npc()
//				)
//		);

//		add(builder().widget("Talk: Set amount")
//				.readyIf(ctx -> ctx.useServant() && ctx.getServant() != null)
//				.doneIf(ctx -> ctx.getServant() == null)
//				.onRun(
//						// TODO: Talk to Butler
//						(ctx, event) -> event.builder().npc()
//				)
//		);

		/*
		 * Main route
		 */

		add(builder().object("Build", "Larder space")
				.readyIf(ctx -> ctx.inBuildMode()
						&& !ctx.isNextToRemovable()
						&& ctx.getNearestBuildable() != null)
				.doneIf(Context::isCreationWidgetOpen)
				.workingIf(ctx -> ctx.isPathingTo(ctx.getNearestPortal()) || ctx.flag("building"))
				.onRun(
						(ctx, event) -> event.builder()
								.object()
								.setObject(ObjectID.LARDER_SPACE)
								.setOption("Build", 5)
								.override()
				));

		add(builder().widget("Select \"Oak Larder\"")
				.readyIf(ctx -> ctx.inBuildMode() && ctx.isCreationWidgetOpen())
				.doneIf(ctx -> !ctx.isCreationWidgetOpen())
				.onRun(
						(ctx, event) -> event.builder().widget()
								.setWidget(ctx.getMakeButton())
								.setOption("Build", 1)
								.onClick(e -> ctx.flag("building", true, 3))
								.override()
				));

		add(builder().object("Remove", "Larder")
				.readyIf(ctx -> ctx.inBuildMode() && ctx.getNearestRemovable() != null && !ctx.isDialogOpen())
				.doneIf(Context::isDialogOpen)
				.workingIf(ctx -> ctx.isPathingTo(ObjectID.LARDER_13566) || ctx.flag("removing"))
				.onRun(
						(ctx, event) -> event.builder().object()
								.setObject(ObjectID.LARDER_13566)
								.setOption("Remove", 5)
								.onClick(e -> ctx.flag("removing", true, 2))
								.override()
				));

		add(builder().widget("Confirm")
				.readyIf(Context::isDialogOpen)
				.doneIf(ctx -> !ctx.isDialogOpen())
				.onRun(
						(ctx, event) -> event.builder().widget()
								.setDialogOption(CLICK_YES)
								.setWidget(219, 1, 1)
								.override()
				));

		add(builder().consume("Nothing").readyIf(ctx -> true));
	}
}
