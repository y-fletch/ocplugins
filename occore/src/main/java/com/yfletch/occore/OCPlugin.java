package com.yfletch.occore;

import com.google.inject.Inject;
import com.yfletch.occore.overlay.ActionOverlay;
import lombok.Setter;
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
		this.context = context;
		this.runner = runner;
		actionOverlay = new ActionOverlay(getName(), runner, config);
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
		if (actionOverlay != null)
		{
			overlayManager.add(actionOverlay);
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

		if (config.quickToggleKeybind() != null)
		{
			keyManager.unregisterKeyListener(hotkeyListener);
		}
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
	public void onConfigChanged(ConfigChanged event)
	{
		if (refreshOnConfigChange
			&& event.getGroup().equals(configGroup))
		{
			clientThread.invokeLater(() -> runner.refresh());
		}
	}
}
