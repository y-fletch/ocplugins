package com.yfletch.ocsepulchre;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.ActionRunner;
import com.yfletch.ocsepulchre.overlay.ActionOverlay;
import com.yfletch.ocsepulchre.overlay.DebugOverlay;
import com.yfletch.ocsepulchre.overlay.ObstacleOverlay;
import com.yfletch.ocsepulchre.overlay.TileOverlay;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

@Extension
@Slf4j
@PluginDescriptor(
	name = "OC Sepulchre [alpha]",
	enabledByDefault = false,
	description = "One-click Hallowed Sepulchre"
)
public class OCSepulchrePlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private OCSepulchreConfig config;

	@Inject
	private OCSepulchreContext context;

	@Inject
	private OCSepulchreRunnerFactory runnerFactory;

	private ActionOverlay actionOverlay;
	private TileOverlay tileOverlay;
	@Inject
	private ObstacleOverlay obstacleOverlay;
	@Inject
	private DebugOverlay debugOverlay;

	private final Pattern FLOOR_MATCH_PATTERN = Pattern.compile("^Floor (\\d) time");

	private ActionRunner<OCSepulchreContext> runner;

	@Override
	protected void startUp()
	{
		runner = runnerFactory.create();
		actionOverlay = new ActionOverlay(runner);
		tileOverlay = new TileOverlay(runner);
		overlayManager.add(actionOverlay);
		overlayManager.add(tileOverlay);
		overlayManager.add(obstacleOverlay);
		overlayManager.add(debugOverlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(actionOverlay);
		overlayManager.remove(tileOverlay);
		overlayManager.remove(obstacleOverlay);
		overlayManager.remove(debugOverlay);
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

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.SPAM
			&& event.getType() != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}

		final String message = event.getMessage();
		final Matcher matcher = FLOOR_MATCH_PATTERN.matcher(message);

		if (matcher.find())
		{
			int previousFloor = Integer.parseInt(matcher.group(1));
			context.setFloor(previousFloor < 5
								 ? previousFloor + 1
								 : 0);
		}
	}

	@Provides
	OCSepulchreConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OCSepulchreConfig.class);
	}
}
