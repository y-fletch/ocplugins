package com.yfletch.occore.v2;

import com.google.inject.Inject;
import com.yfletch.occore.v2.interaction.Interaction;
import com.yfletch.occore.v2.interaction.InteractionExecutor;
import com.yfletch.occore.v2.interaction.exceptions.InteractionException;
import com.yfletch.occore.v2.overlay.CoreActionOverlay;
import com.yfletch.occore.v2.overlay.CoreDebugOverlay;
import com.yfletch.occore.v2.rule.DynamicRule;
import com.yfletch.occore.v2.rule.RequirementRule;
import com.yfletch.occore.v2.rule.Rule;
import com.yfletch.occore.v2.util.RunnerUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.unethicalite.api.commons.Rand;
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
	private InteractionExecutor nextInteraction = null;

	@Getter
	private List<String> messages = null;

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
	 * Setup this plugin by adding rules here. As this is executed on
	 * the client thread, it is possible to query items/objects etc.
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

	/**
	 * Create, add and return a new requirement rule instance that can be customised similar to a builder
	 */
	protected final RequirementRule<TContext> requirements()
	{
		final var rule = new RequirementRule<TContext>();
		rules.add(rule);
		return rule;
	}

	protected final Interaction interact()
	{
		return new Interaction();
	}

	private void revalidate()
	{
		if (currentRule == null)
		{
			nextInteraction = null;
			messages = null;
			return;
		}

		// if the current interaction is null (errored last tick), try to re-process it
		if (nextInteraction == null)
		{
			final var interaction = currentRule.run(context);
			if (interaction == null)
			{
				messages = currentRule.messages(context);
				return;
			}

			nextInteraction = interaction.getExecutor();
			messages = null;
		}

		// if the current interaction is invalid, nullify it
		try
		{
			nextInteraction.validate();
		}
		catch (InteractionException e)
		{
			nextInteraction = null;
			messages = List.of(
				"<col=ff0000>Interaction error:",
				"<col=ff0000>-<col=ffffff> " + e.getMessage()
			);
		}
	}

	private void execute(MenuOptionClicked event)
	{
		if (!config.enabled()
			|| currentRule == null
			|| nextInteraction == null
			|| nextInteraction.isComplete())
		{
			// consume erroneous / extra events in one-click mode
			if (config.enabled() && event != null)
			{
				event.consume();
			}

			return;
		}

		var delay = 0;
		if (context.getInteractionDelay() > 0)
		{
			// [0, 2)
			// the lower the interaction delay is, the more likely
			// it will execute. 4t => 1/4, 3t => 1/3, etc
			delay = Rand.nextInt(0, context.getInteractionDelay() + 1);
		}

		if (delay == 0)
		{
			// just for debugging
			context.setInteractionDelay(0);

			if (event == null)
			{
				nextInteraction.execute();
			}
			else
			{
				nextInteraction.execute(event);
			}
		}
		else if (event != null)
		{
			// consume events that were attempted during
			// the delay
			event.consume();
		}
		// process again, just in case we can do something
		// else straight away
		process();
	}

	/**
	 * Determine the next rule to move to
	 */
	private void process()
	{
		isConsuming = false;

		if (currentRule != null)
		{
			revalidate();

			if (!currentRule.passes(context) || currentRule.continues(context))
			{
				currentRule = null;
				nextInteraction = null;
			}
			else if (config.pluginApi() == PluginAPI.ONE_CLICK
				&& currentRule.consumes(context))
			{
				isConsuming = true;
				InteractionExecutor.consumeNext();
			}
		}

		if (currentRule == null)
		{
			for (final var rule : rules)
			{
				if (rule.passes(context))
				{
					currentRule = rule;

					// set the interaction delay once, when
					// the rule is first processed
					context.setInteractionDelay(currentRule.maxDelay());
					revalidate();

					// immediately perform actions on rule change
					// if we're using devious API
//					if (config.pluginApi() == PluginAPI.DEVIOUS)
//					{
//						execute(null);
//					}

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

		Static.getClientThread().invokeLater(this::setup);
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
		InteractionExecutor.tick();
		context.tick(true);

		if (processOnGameTick)
		{
			process();
		}

		if (config.pluginApi() == PluginAPI.DEVIOUS)
		{
			// if the devious interaction hasn't been executed yet,
			// attempt to run it again
			execute(null);
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (config.debugRawMenuEntries())
		{
			RunnerUtil.logDebug("RAW", event);
		}

		context.tick(false);

		if (processOnMouseClick)
		{
			process();
		}

		if (config.pluginApi() == PluginAPI.ONE_CLICK)
		{
			execute(event);

			if (config.debugOCMenuEntries())
			{
				RunnerUtil.logDebug("OC", event);
			}
		}
	}
}
