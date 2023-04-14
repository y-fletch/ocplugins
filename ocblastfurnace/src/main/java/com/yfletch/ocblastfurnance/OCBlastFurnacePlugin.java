package com.yfletch.ocblastfurnance;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.ocblastfurnance.overlay.ActionOverlay;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Blast Furnace [alpha]",
	description = "One-click Blast Furnace"
)
public class OCBlastFurnacePlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ActionOverlay actionOverlay;

	@Inject
	private OCBlastFurnaceContext context;

	@Inject
	private OCBlastFurnaceRunner runner;

	@Inject
	private OCBlastFurnaceConfig config;

	@Override
	protected void startUp()
	{
		overlayManager.add(actionOverlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(actionOverlay);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		context.tick();
		runner.tick();
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		log.info(event.getMenuEntry().toString());

		runner.tick();

		if (config.ocEnabled())
		{
			runner.run(event);
		}
	}

	@Provides
	public OCBlastFurnaceConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OCBlastFurnaceConfig.class);
	}
}
