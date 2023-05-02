package com.yfletch.occore;

import com.google.inject.Inject;
import com.yfletch.occore.overlay.ActionOverlay;
import com.yfletch.occore.overlay.DebugOverlay;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

@Slf4j
public abstract class OCPlugin extends Plugin
{
	// injected
	@Inject protected Client client;
	@Inject protected ClientThread clientThread;
	@Inject protected KeyManager keyManager;
	@Inject protected OverlayManager overlayManager;
	@Inject protected ConfigManager configManager;

	// plugin specific
	private ActionContext context;
	private ActionRunner<?> runner;
	private OCConfig config;
	private ActionOverlay actionOverlay;
	private DebugOverlay debugOverlay;

	/**
	 * Required for quick toggle and refresh on config change to work
	 */
	@Setter
	private String configGroup = "oc-plugins";

	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.quickToggleKeybind())
	{
		@Override
		public void hotkeyPressed()
		{
			configManager.setConfiguration(configGroup, "ocEnabled", !config.ocEnabled());
		}
	};

	private boolean refreshOnConfigChange = false;

	protected void setup(ActionContext context, ActionRunner<?> runner, OCConfig config)
	{
		if (context == null || runner == null || config == null)
		{
			throw new IllegalArgumentException("OCPlugin was not injected properly");
		}

		this.context = context;
		this.runner = runner;
		actionOverlay = new ActionOverlay(getName(), runner, config);
		debugOverlay = new DebugOverlay(getName(), context);
		this.config = config;
	}

	protected void refreshOnConfigChange()
	{
		refreshOnConfigChange = true;
	}

	// RL hooks

	@Override
	protected void startUp()
	{
		if (actionOverlay != null && config.showActionOverlay())
		{
			overlayManager.add(actionOverlay);
		}

		if (debugOverlay != null && config.showDebugOverlay())
		{
			overlayManager.add(debugOverlay);
		}

		if (config.quickToggleKeybind() != null)
		{
			keyManager.registerKeyListener(hotkeyListener);
		}

		if (refreshOnConfigChange && runner != null)
		{
			clientThread.invokeLater(() -> runner.refresh());
		}
	}

	@Override
	protected void shutDown()
	{
		if (actionOverlay != null)
		{
			overlayManager.remove(actionOverlay);
		}

		if (debugOverlay != null)
		{
			overlayManager.remove(debugOverlay);
		}

		if (config.quickToggleKeybind() != null)
		{
			keyManager.unregisterKeyListener(hotkeyListener);
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		context.tick(true);
		runner.tick();
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (config.debugMenuEntries())
		{
			final var debug = "[OC] option=" + event.getMenuOption()
				+ " target=" + event.getMenuTarget()
				+ " id=" + event.getId()
				+ " action=" + event.getMenuAction()
				+ " p0=" + event.getParam0()
				+ " p1=" + event.getParam1();
			log.info(debug);
			client.addChatMessage(
				ChatMessageType.GAMEMESSAGE,
				"Bob",
				debug,
				null
			);
		}

		runner.tick();

		if (config.ocEnabled())
		{
			runner.run(event);
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(configGroup))
		{
			if (refreshOnConfigChange)
			{
				clientThread.invokeLater(() -> runner.refresh());
			}

			if (event.getKey().equals("showActionOverlay"))
			{
				if (event.getNewValue().equals("true"))
				{
					overlayManager.add(actionOverlay);
				}
				else
				{
					overlayManager.remove(actionOverlay);
				}
			}

			if (event.getKey().equals("showDebugOverlay"))
			{
				if (event.getNewValue().equals("true"))
				{
					overlayManager.add(debugOverlay);
				}
				else
				{
					overlayManager.remove(debugOverlay);
				}
			}
		}
	}
}
