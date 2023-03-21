package com.yfletch.ocbarbfishing;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.ocbarbfishing.overlay.ActionOverlay;
import com.yfletch.occore.ActionRunner;
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
	name = "OC Barb Fishing [alpha]",
	description = "One-click Barbarian Fishing"
)
public class OCBarbFishingPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private OCBarbFishingConfig config;

	@Inject
	private OCBarbFishingContext context;

	@Inject
	private OCBarbFishingRunnerFactory runnerFactory;

	private ActionOverlay actionOverlay;
	private ActionRunner<OCBarbFishingContext> runner;

	@Override
	protected void startUp()
	{
		runner = runnerFactory.create();
		actionOverlay = new ActionOverlay(runner);
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
		runner.tick();
		context.tick();
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (config.ocEnabled())
		{
			runner.run(event);
		}
	}

	@Provides
	OCBarbFishingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OCBarbFishingConfig.class);
	}
}
