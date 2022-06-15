package com.yfletch.ocbloods;

import com.google.inject.Provides;
import com.yfletch.ocbloods.overlay.ActionOverlay;
import com.yfletch.ocbloods.overlay.DebugOverlay;
import com.yfletch.ocbloods.overlay.PouchOverlay;
import com.yfletch.ocbloods.overlay.StatisticsOverlay;
import com.yfletch.ocbloods.util.Statistics;
import com.yfletch.occore.ActionRunner;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemID;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

@Extension
@Slf4j
@PluginDescriptor(
	name = "OC Bloods [alpha]",
	enabledByDefault = false,
	description = "One-click Blood Runecrafting"
)
public class OCBloodsPlugin extends Plugin
{
	@Inject
	private OCBloodsConfig config;

	@Inject
	private Statistics statistics;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ClientThread clientThread;

	// overlays
	private ActionOverlay actionOverlay;

	@Inject
	private PouchOverlay pouchOverlay;

	@Inject
	private StatisticsOverlay statisticsOverlay;

	@Inject
	private DebugOverlay debugOverlay;

	@Inject
	private OverlayManager overlayManager;
	//

	// action runner
	@Inject
	private OCBloodsRunnerFactory runnerFactory;

	@Inject
	private OCBloodsContext context;

	private ActionRunner<OCBloodsContext> runner;
	//

	@Override
	protected void startUp()
	{
		runner = runnerFactory.create();
		actionOverlay = new ActionOverlay(runner);
		debugOverlay.setRunner(runner);

		overlayManager.add(actionOverlay);
		overlayManager.add(pouchOverlay);
		overlayManager.add(statisticsOverlay);
		overlayManager.add(debugOverlay);

		clientThread.invokeLater(() -> {
			int price = itemManager.getItemPrice(ItemID.BLOOD_RUNE);
			log.info("Price of blood runes: " + price + "gp");
			if (price > 0)
			{
				statistics.setRunePrice(price);
			}
		});
	}

	@Override
	protected void shutDown()
	{
		runner = null;
		overlayManager.remove(actionOverlay);
		overlayManager.remove(pouchOverlay);
		overlayManager.remove(statisticsOverlay);
		overlayManager.remove(debugOverlay);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		runner.tick();
		context.tick();
		statistics.tick();
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (config.ocEnabled())
		{
			runner.run(event);
		}

		// handle pouch state
		String target = event.getMenuTarget();
		if (target.contains("Colossal pouch"))
		{
			if (event.getId() == 2
				|| event.getId() == 9
				|| event.getMenuOption().contains("Fill"))
			{
				context.fillPouch();
			}
			else if (event.getId() == 3
				|| event.getMenuOption().contains("Empty"))
			{
				context.emptyPouch();
			}
		}

		runner.tick();
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() == InventoryID.INVENTORY.getId())
		{
			statistics.onItemContainerChanged(event);
		}

		if (event.getContainerId() == InventoryID.INVENTORY.getId()
			|| event.getContainerId() == InventoryID.BANK.getId())
		{
			context.onInventoryChanged();
		}
	}

	@Subscribe
	public void onOverlayMenuClicked(OverlayMenuClicked event)
	{
		if (event.getOverlay() == actionOverlay)
		{
			String option = event.getEntry().getOption();
			switch (option)
			{
				case ActionOverlay.ACTION_SKIP_REPAIR:
					context.repairPouch();
					break;
			}
		}
	}

	@Provides
	OCBloodsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OCBloodsConfig.class);
	}
}
