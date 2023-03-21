package com.yfletch.ocbarbfishing;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.ocbarbfishing.overlay.ActionOverlay;
import com.yfletch.ocbarbfishing.overlay.DebugOverlay;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.AnimationChanged;
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
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private OCBarbFishingConfig config;

	@Inject
	private OCBarbFishingContext context;

	@Inject
	private ActionOverlay actionOverlay;

	@Inject
	private DebugOverlay debugOverlay;

	@Inject
	private OCBarbFishingRunner runner;

	@Override
	protected void startUp()
	{
		overlayManager.add(actionOverlay);
		overlayManager.add(debugOverlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(actionOverlay);
		overlayManager.remove(debugOverlay);
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
		runner.tick();

		if (config.ocEnabled())
		{
			runner.run(event);
		}
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		if (event.getActor().equals(client.getLocalPlayer())
			&& event.getActor().getAnimation() == 9349)
		{
			context.beginFishing();
		}
	}

	@Provides
	OCBarbFishingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OCBarbFishingConfig.class);
	}
}
