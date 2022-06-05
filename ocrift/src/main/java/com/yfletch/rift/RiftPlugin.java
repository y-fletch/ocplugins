package com.yfletch.rift;

import com.google.inject.Provides;
import com.yfletch.rift.action.EmptyPouch;
import com.yfletch.rift.action.EquipRobeTop;
import com.yfletch.rift.action.EquipVarrockArmour;
import com.yfletch.rift.action.FillPouch;
import com.yfletch.rift.action.Nothing;
import com.yfletch.rift.action.cycle.common.CraftRunes;
import com.yfletch.rift.action.cycle.common.DepositRunes;
import com.yfletch.rift.action.cycle.common.DropRunes;
import com.yfletch.rift.action.cycle.common.EnterAltar;
import com.yfletch.rift.action.cycle.common.ExitAltar;
import com.yfletch.rift.action.cycle.common.PlaceCell;
import com.yfletch.rift.action.cycle.common.PowerUpGuardian;
import com.yfletch.rift.action.cycle.craft.MineGuardianParts;
import com.yfletch.rift.action.cycle.mine.ExitPortal;
import com.yfletch.rift.action.cycle.mine.MineHugeRemains;
import com.yfletch.rift.action.cycle.mine.EnterPortal;
import com.yfletch.rift.action.cycle.start.ClimbUpRubble;
import com.yfletch.rift.action.cycle.craft.WorkAtWorkbench;
import com.yfletch.rift.action.cycle.start.MineLargeRemains;
import com.yfletch.rift.action.postgame.DropCell;
import com.yfletch.rift.action.pregame.UseSpecialAttack;
import com.yfletch.rift.action.pregame.ClimbDownRubble;
import com.yfletch.rift.action.pregame.TakeUnchargedCells;
import com.yfletch.rift.action.pregame.TakeWeakCell;
import com.yfletch.rift.action.pregame.WalkToLargeRemains;
import com.yfletch.rift.action.repair.CastNpcContact;
import com.yfletch.rift.action.repair.ClickToContinueNPC;
import com.yfletch.rift.action.repair.ClickToContinuePlayer;
import com.yfletch.rift.action.repair.RepairPouches;
import com.yfletch.rift.enums.Pouch;
import com.yfletch.rift.helper.PouchSolver;
import com.yfletch.rift.lib.ActionRunner;
import com.yfletch.rift.overlay.DebugOverlay;
import com.yfletch.rift.overlay.DestinationOverlay;
import com.yfletch.rift.overlay.PouchUseOverlay;
import com.yfletch.rift.overlay.RiftActionOverlay;
import com.yfletch.rift.overlay.StatisticsOverlay;
import com.yfletch.rift.util.MenuEntryProvider;
import com.yfletch.rift.util.Statistics;
import java.util.Arrays;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

@Extension
@Slf4j
@PluginDescriptor(
	name = "OC Rift [alpha]",
	enabledByDefault = false,
	description = "One-click Guardians of the Rift"
)
public class RiftPlugin extends Plugin
{
	// overlays
	@Inject
	private DebugOverlay debugOverlay;

	@Inject
	private DestinationOverlay destinationOverlay;

	@Inject
	private PouchUseOverlay pouchUseOverlay;

	@Inject
	private StatisticsOverlay statisticsOverlay;

	@Inject
	private OverlayManager overlayManager;

	private RiftActionOverlay actionOverlay;
	// end overlays

	// action runner
	@Inject
	private RiftContext context;

	@Inject
	private MenuEntryProvider menuEntryProvider;

	private ActionRunner<RiftContext> runner;
	// end action runner

	// misc
	@Inject
	private Client client;

	@Inject
	private RiftConfig config;

	@Inject
	private Statistics statistics;
	// end misc

	@Override
	protected void startUp()
	{
		runner = new ActionRunner<>(context, menuEntryProvider);
		runner.add(new CastNpcContact());
		runner.add(new ClickToContinueNPC());
		runner.add(new RepairPouches());
		runner.add(new ClickToContinuePlayer());

		runner.add(new DropCell());
		runner.add(new TakeWeakCell());
		runner.add(new TakeUnchargedCells());
		runner.add(new ClimbDownRubble());
		runner.add(new WalkToLargeRemains());
		runner.add(new UseSpecialAttack());
		runner.add(new EquipVarrockArmour());
		runner.add(new MineLargeRemains());
		runner.add(new ClimbUpRubble());
		Arrays.stream(Pouch.values())
			.forEach(p -> runner.add(new FillPouch(p)));
		runner.add(new WorkAtWorkbench());
		runner.add(new PlaceCell());
		runner.add(new EnterAltar());
		Arrays.stream(Pouch.values())
			.forEach(p -> runner.add(new EmptyPouch(p)));
		runner.add(new EquipRobeTop());
		runner.add(new CraftRunes());
		runner.add(new ExitAltar());
		runner.add(new DropRunes());
		runner.add(new DepositRunes());
		runner.add(new PowerUpGuardian());
		runner.add(new MineGuardianParts());
		runner.add(new EnterPortal());
		runner.add(new MineHugeRemains());
		runner.add(new ExitPortal());
		runner.add(new Nothing());

		actionOverlay = new RiftActionOverlay(runner);
		overlayManager.add(actionOverlay);
		overlayManager.add(debugOverlay);
		overlayManager.add(pouchUseOverlay);
		overlayManager.add(statisticsOverlay);
//		overlayManager.add(destinationOverlay);

		PouchSolver solver = new PouchSolver(context);
		solver.getNextUnfilledPouch();
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(actionOverlay);
		overlayManager.remove(debugOverlay);
		overlayManager.remove(pouchUseOverlay);
		overlayManager.remove(statisticsOverlay);
//		overlayManager.remove(destinationOverlay);
		actionOverlay = null;
		runner = null;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (runner == null)
		{
			log.error("Got a game tick with no runner created!");
			return;
		}

		context.setGameTime(context.getGameTime() + 0.6);

		// default to negative game time when entering
		if (context.isInLobbyArea())
		{
			context.reset();
		}

		runner.tick();

		if (!context.isInLobbyArea())
		{
			statistics.tick();
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (runner == null)
		{
			log.error("Got a menu option click with no runner created!");
		}

		if (config.ocEnabled() && !context.isInLobbyArea())
		{
			runner.run(event);
		}

		// handle pouch state
		String target = event.getMenuTarget();
		if (target.contains(" pouch"))
		{
			for (Pouch pouch : Pouch.values())
			{
				if (!target.contains(pouch.getItemName()))
				{
					continue;
				}

				if (event.getId() == 2)
				{
					context.fillPouch(pouch);
				}
				else if (event.getId() == 3)
				{
					context.emptyPouch(pouch);
				}
			}
		}

		runner.tick();
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.SPAM
			&& event.getType() != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}

		String message = event.getMessage();
		if (message.contains("The rift becomes active!"))
		{
		}
		else if (message.contains("The rift will become active in 30 seconds."))
		{
			context.reset();
			context.setGameTime(-30);
		}
		else if (message.contains("The rift will become active in 10 seconds."))
		{
			context.reset();
			context.setGameTime(-10);
		}
		else if (message.contains("The rift will become active in 5 seconds."))
		{
			context.reset();
			context.setGameTime(-5);
		}
		else if (message.contains("The Portal Guardians will keep their rifts open for another 30 seconds."))
		{
			context.reset();
			context.setGameTime(-60);
		}
		else if (message.contains("The Great Guardian successfully closed the rift!"))
		{
			context.reset();
			context.setGameTime(-60);
		}

		statistics.onChatMessage(event);
	}

	@Subscribe
	public void onOverlayMenuClicked(OverlayMenuClicked event)
	{
		if (event.getOverlay() == actionOverlay)
		{
			String option = event.getEntry().getOption();
			switch (option)
			{
				case RiftActionOverlay.DEBUG_SET_N60:
					context.setGameTime(-60);
					break;
				case RiftActionOverlay.DEBUG_SET_0:
					context.setGameTime(0);
					break;
				case RiftActionOverlay.DEBUG_SET_120:
					context.setGameTime(120);
					break;
				case RiftActionOverlay.DEBUG_CLEAR_POUCHES:
					context.clearPouches();
					break;
				case RiftActionOverlay.DEBUG_CLEAR_FLAGS:
					context.clearFlags();
					break;
				case RiftActionOverlay.DEBUG_SKIP_REPAIR:
					context.repairPouches();
					break;
				case StatisticsOverlay.CLEAR_STATISTICS:
					statistics.reset();
					break;
			}
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		statistics.onItemContainerChanged(event);

		if (event.getContainerId() == InventoryID.INVENTORY.getId())
		{
			context.onInventoryChanged();
		}
	}

	@Provides
	RiftConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(RiftConfig.class);
	}
}
