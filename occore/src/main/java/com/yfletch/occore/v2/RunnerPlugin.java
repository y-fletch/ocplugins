package com.yfletch.occore.v2;

import com.google.inject.Inject;
import com.yfletch.occore.v2.overlay.CoreActionOverlay;
import com.yfletch.occore.v2.overlay.CoreDebugOverlay;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.unethicalite.client.Static;

@Slf4j
public abstract class RunnerPlugin<TContext extends CoreContext> extends Plugin
{
	@Inject private ConfigManager configManager;
	@Inject private KeyManager keyManager;
	@Inject private OverlayManager overlayManager;

	@Getter
	private final List<Rule<TContext>> rules = new ArrayList<>();
	private Rule<TContext> currentRule = null;

	@Getter
	private Interaction nextInteraction = null;

	@Getter
	@Accessors(fluent = true)
	private boolean isConsuming = false;

	private CoreActionOverlay actionOverlay;
	private CoreDebugOverlay debugOverlay;

	@Setter
	private TContext context;

	@Setter
	private CoreConfig config;

	@Setter
	private String configGroup;

	@Setter
	@Accessors(fluent = true)
	private boolean processOnGameTick = true;

	@Setter
	@Accessors(fluent = true)
	private boolean processOnMouseClick = true;

	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.quickToggleKeybind())
	{
		@Override
		public void hotkeyPressed()
		{
			configManager.setConfiguration(configGroup, "enabled", !config.enabled());
		}
	};

	public boolean enabled()
	{
		return config.enabled();
	}

	/**
	 * Setup this plugin by adding rules here
	 */
	public abstract void setup();

	/**
	 * Create, add and return a new rule instance that can be customised similar to a builder
	 */
	protected final DynamicRule<TContext> createRule()
	{
		final var rule = new DynamicRule<TContext>();
		rules.add(rule);
		return rule;
	}

	protected final Interaction interact()
	{
		return new Interaction();
	}

	/**
	 * Determine the next rule to move to
	 */
	private void process()
	{
		isConsuming = false;

		if (currentRule != null)
		{
			if (!currentRule.passes(context) || currentRule.continues(context))
			{
				currentRule = null;
				nextInteraction = null;
			}
			else if (config.pluginMode() == PluginMode.ONE_CLICK
				&& currentRule.consumes(context))
			{
				isConsuming = true;
				Interaction.consumeNext();
			}

			if (currentRule != null)
			{
				// update next interaction
				nextInteraction = currentRule.run(context);
			}
		}

		if (currentRule == null)
		{
			for (final var rule : rules)
			{
				if (rule.passes(context))
				{
					currentRule = rule;
					nextInteraction = currentRule.run(context);

					// immediately perform actions on rule change,
					// unless we're in one-click mode
					if (config.enabled()
						&& config.pluginMode() != PluginMode.ONE_CLICK)
					{
						// TODO: random delay
						nextInteraction.execute();
						// process again, just in case we can do something
						// else straight away
						process();
					}

					return;
				}
			}
		}
	}

	private void createOverlays()
	{
		actionOverlay = new CoreActionOverlay(this);
		debugOverlay = new CoreDebugOverlay(this, context);
	}

	@Override
	protected void startUp()
	{
		createOverlays();
		setup();

		if (config.showActionOverlay())
		{
			overlayManager.add(actionOverlay);
		}

		if (config.showDebugOverlay())
		{
			overlayManager.add(debugOverlay);
		}

		if (config.quickToggleKeybind() != null)
		{
			keyManager.registerKeyListener(hotkeyListener);
		}
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(actionOverlay);
		overlayManager.remove(debugOverlay);
		keyManager.unregisterKeyListener(hotkeyListener);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		Interaction.tick();

		context.tick(true);

		if (processOnGameTick)
		{
			process();
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (config.debugRawMenuEntries())
		{
			final var debug = "[RAW] option=" + event.getMenuOption()
				+ " target=" + event.getMenuTarget()
				+ " id=" + event.getId()
				+ " action=" + event.getMenuAction()
				+ " p0=" + event.getParam0()
				+ " p1=" + event.getParam1();
			log.info(debug);
			Static.getClient().addChatMessage(
				ChatMessageType.GAMEMESSAGE,
				"Bob",
				debug,
				null
			);
		}

		context.tick(false);

		if (processOnMouseClick)
		{
			process();
		}

		if (nextInteraction != null
			&& config.enabled()
			&& config.pluginMode() == PluginMode.ONE_CLICK)
		{
			// TODO: random delay
			if (config.enabled() && nextInteraction != null)
			{
				nextInteraction.execute(event);

				if (config.debugOCMenuEntries())
				{
					final var debug = "[OC] option=" + event.getMenuOption()
						+ " target=" + event.getMenuTarget()
						+ " id=" + event.getId()
						+ " action=" + event.getMenuAction()
						+ " p0=" + event.getParam0()
						+ " p1=" + event.getParam1();
					log.info(debug);
					Static.getClient().addChatMessage(
						ChatMessageType.GAMEMESSAGE,
						"Bob",
						debug,
						null
					);
				}
			}
		}
	}
}
