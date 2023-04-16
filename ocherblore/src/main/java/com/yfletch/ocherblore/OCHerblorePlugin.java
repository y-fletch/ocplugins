package com.yfletch.ocherblore;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.ocherblore.overlay.ActionOverlay;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Herblore",
	description = "One-click herblore",
	enabledByDefault = false
)
public class OCHerblorePlugin extends Plugin
{
	@Inject
	private ClientThread clientThread;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ActionOverlay actionOverlay;

	@Inject
	private OCHerbloreContext context;

	@Inject
	private OCHerbloreRunner runner;

	@Inject
	private OCHerbloreConfig config;

	@Override
	protected void startUp()
	{
		overlayManager.add(actionOverlay);
		clientThread.invokeLater(() -> runner.refresh());
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

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("oc-herblore"))
		{
			clientThread.invokeLater(() -> runner.refresh());
		}
	}

	@Provides
	public OCHerbloreConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OCHerbloreConfig.class);
	}
}
