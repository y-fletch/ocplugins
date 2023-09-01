package com.yfletch.ocmonke;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import com.yfletch.occore.v2.interaction.Walking;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Dancing",
	description = "One-click dancing",
	enabledByDefault = false
)
public class OCDancingPlugin extends RunnerPlugin<DancingContext>
{
	@Inject private DancingConfig config;
	@Inject private DancingContext context;

	@Inject private Client client;
	@Inject private ConfigManager configManager;

	@Inject
	public void init(DancingConfig config, DancingContext context)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(DancingConfig.GROUP_NAME);
	}

	@Override
	public void setup()
	{
		action().name("Tile 1")
			.when(c -> c.getPlayerLocation().equals(c.getTile2()))
			.then(c -> Walking.walk(c.getTile1()))
			.delay(config.danceInterval());

		action().name("Tile 2")
			.when(c -> c.getPlayerLocation().equals(c.getTile1()))
			.then(c -> Walking.walk(c.getTile2()))
			.delay(config.danceInterval());
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (event.getOption().equals("Walk here"))
		{
			client.createMenuEntry(-1)
				.setOption("OCDancing -> mark tile 2")
				.setTarget(event.getTarget())
				.setType(MenuAction.RUNELITE)
				.onClick(e -> {
					final var target = client.getSelectedSceneTile();
					if (target != null)
					{
						final var location = target.getWorldLocation();
						configManager.setConfiguration(
							DancingConfig.GROUP_NAME,
							"tile2",
							DancingContext.worldPointToString(location)
						);
					}
				});

			client.createMenuEntry(-1)
				.setOption("OCDancing -> mark tile 1")
				.setTarget(event.getTarget())
				.setType(MenuAction.RUNELITE)
				.onClick(e -> {
					final var target = client.getSelectedSceneTile();
					if (target != null)
					{
						final var location = target.getWorldLocation();
						configManager.setConfiguration(
							DancingConfig.GROUP_NAME,
							"tile1",
							DancingContext.worldPointToString(location)
						);
					}
				});
		}
	}

	@Provides
	public DancingConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DancingConfig.class);
	}
}
