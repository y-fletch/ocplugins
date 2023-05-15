package com.yfletch.occore.v2;

import com.google.inject.Inject;
import com.yfletch.occore.v2.interaction.DeferredInteraction;
import com.yfletch.occore.v2.interaction.Entities;
import com.yfletch.occore.v2.overlay.CoreDebugOverlay;
import com.yfletch.occore.v2.overlay.InteractionOverlay;
import com.yfletch.occore.v2.rule.DynamicRule;
import com.yfletch.occore.v2.rule.RequirementRule;
import com.yfletch.occore.v2.rule.Rule;
import com.yfletch.occore.v2.util.RunnerUtil;
import com.yfletch.occore.v2.util.TextColor;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.PostMenuSort;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.client.Static;
import net.unethicalite.client.config.UnethicaliteConfig;

@Slf4j
public abstract class RunnerPlugin<TContext extends CoreContext> extends Plugin
{
	/**
	 * Amount of times we will try to execute with the devious
	 * API per tick. Used for quick-dropping etc.
	 */
	private final static int MAX_ACTIONS_PER_TICK = 8;

	@Inject private ConfigManager configManager;
	@Inject private KeyManager keyManager;
	@Inject private OverlayManager overlayManager;

	@Inject private UnethicaliteConfig unethicaliteConfig;

	@Getter
	private final List<Rule<TContext>> rules = new ArrayList<>();
	private Rule<TContext> currentRule = null;

	@Getter
	private DeferredInteraction<?> nextInteraction = null;

	@Getter
	private boolean isDelaying = false;

	@Getter
	private List<String> messages = null;

	private InteractionOverlay interactionOverlay;
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

	@Setter
	@Accessors(fluent = true)
	private boolean refreshOnConfigChange = true;

	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.quickToggleKeybind())
	{
		@Override
		public void hotkeyPressed()
		{
			configManager.setConfiguration(configGroup, "enabled", !enabled());
		}
	};

	public boolean enabled()
	{
		return config.enabled();
	}

	public int getRuleRepeatsLeft()
	{
		return currentRule == null ? 1 : currentRule.repeatsLeft();
	}

	/**
	 * Clear all current rules and run setup again
	 */
	public void refresh()
	{
		rules.clear();
		setup();
	}

	/**
	 * Setup this plugin by adding rules here. As this is executed on
	 * the client thread, it is possible to query items/objects etc.
	 */
	public abstract void setup();

	/**
	 * Create, add and return a new rule instance that can be customised similar to a builder
	 */
	protected final DynamicRule<TContext> action()
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
		return rule.name("Requirements");
	}

	private void updateDelay()
	{
		if (context.getMinDelayTimer() > 0)
		{
			return;
		}

		var delay = 0;
		if (context.getDelayTimer() > 0)
		{
			// [0, 2)
			// the lower the interaction delay is, the more likely
			// it will execute. 4t => 1/4, 3t => 1/3, etc
			delay = Rand.nextInt(0, context.getDelayTimer() + 1);
		}

		if (delay == 0)
		{
			isDelaying = false;

			// just for debugging
			context.setDelayTimer(0);
		}
	}

	private boolean canExecute()
	{
		if (!enabled() || currentRule == null || !currentRule.canExecute())
		{
			return false;
		}

		return !isDelaying;
	}

	private void executeWithDeviousAPI()
	{
		if (canExecute() && config.pluginApi() == PluginAPI.DEVIOUS)
		{
			final var interaction = updateInteraction(currentRule);
			if (interaction != null)
			{
				interaction.execute();
				currentRule.callback(context);
				currentRule.useRepeat();

				if (!currentRule.canExecute())
				{
					currentRule.completeCallback(context);
				}
			}
		}
	}

	@Nullable
	private DeferredInteraction<?> updateInteraction(Rule<TContext> rule)
	{
		nextInteraction = rule.run(context);
		if (nextInteraction == null)
		{
			// fallback to rule message
			messages = rule.messages(context);
			if (messages == null)
			{
				final var ruleName = rule.name() != null
					? "\"" + rule.name() + "\""
					: "unknown rule";

				messages = List.of(
					TextColor.WHITE + "Nothing to do (no interaction)",
					TextColor.GRAY + "For " + ruleName
				);
			}
		}
		else
		{
			messages = null;
		}

		return nextInteraction;
	}

	private boolean passes(Rule<TContext> rule)
	{
		return rule.passes(context) && !rule.continues(context);
	}

	private void enable(Rule<TContext> rule)
	{
		// reset rule status
		rule.reset();

		// update interaction display
		updateInteraction(rule);

		// use new max delay
		context.setDelayTimer(rule.maxDelay());
		context.setMinDelayTimer(rule.minDelay());
		// reset delay status
		isDelaying = rule.maxDelay() > 0 || rule.minDelay() > 0;

		currentRule = rule;
	}

	/**
	 * Determine the next rule to move to
	 */
	private void process()
	{
		if (currentRule != null)
		{
			// clear rule if it no longer passes
			if (!passes(currentRule))
			{
				currentRule = null;
				nextInteraction = null;
				messages = null;
			}

			if (currentRule != null)
			{
				updateInteraction(currentRule);
			}
		}

		// find new rule to apply
		for (final var rule : rules)
		{
			if (passes(rule))
			{
				if (rule != currentRule)
				{
					enable(rule);
				}
				return;
			}
		}
	}

	private void createOverlays()
	{
		interactionOverlay = new InteractionOverlay(this);
		debugOverlay = new CoreDebugOverlay(this, context);
	}

	@Override
	protected void startUp()
	{
		createOverlays();

		if (config.showActionOverlay())
		{
			overlayManager.add(interactionOverlay);
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
		overlayManager.remove(interactionOverlay);
		overlayManager.remove(debugOverlay);
		keyManager.unregisterKeyListener(hotkeyListener);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		context.tick(true);
		Entities.clearInteracted();

		if (processOnGameTick)
		{
			process();
		}

		updateDelay();
		for (int i = 0; i < MAX_ACTIONS_PER_TICK; i++)
		{
			executeWithDeviousAPI();
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (config.debugRawMenuEntries())
		{
			RunnerUtil.log("raw", event.getMenuEntry());
		}

		context.tick(false);

		if (enabled() && config.pluginApi() == PluginAPI.ONE_CLICK_CONSUME && !canExecute())
		{
			event.consume();
			if (config.debugOCMenuEntries())
			{
				RunnerUtil.log("OC", "Consumed");
			}
		}

		if (canExecute() && config.pluginApi().isOneClick()
			&& !event.getMenuOption().startsWith("* ")
			&& nextInteraction != null)
		{
			RunnerUtil.log(
				"OC",
				TextColor.DANGER + "Failed to override menu click."
			);
			RunnerUtil.chat(
				"OC",
				TextColor.DANGER + "Please make sure the mouse cursor" +
					" is not over an item slot, or"
			);
			RunnerUtil.chat(
				"OC",
				TextColor.DANGER + "another plugin's interface."
			);
		}

		if (currentRule != null && event.getMenuOption().startsWith("* "))
		{
			if (nextInteraction != null)
			{
				nextInteraction.prepare();
			}

			currentRule.callback(context);
			currentRule.useRepeat();

			if (!currentRule.canExecute())
			{
				currentRule.completeCallback(context);
			}

			RunnerUtil.log("OC", event.getMenuEntry());
		}

		if (processOnMouseClick)
		{
			process();
		}

		executeWithDeviousAPI();
	}

	@Subscribe
	public void onPostMenuSort(PostMenuSort event)
	{
		if (config.pluginApi().isOneClick() && canExecute())
		{
			if (nextInteraction != null)
			{
				// add the one-click entry to the top
				final var entry = nextInteraction.createMenuEntry();
				entry.setOption("* " + entry.getOption());
				entry.setForceLeftClick(true);
			}
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(configGroup))
		{
			if (refreshOnConfigChange)
			{
				Static.getClientThread().invokeLater(this::refresh);
			}

			if (event.getKey().equals("showActionOverlay"))
			{
				if (event.getNewValue().equals("true"))
				{
					overlayManager.add(interactionOverlay);
				}
				else
				{
					overlayManager.remove(interactionOverlay);
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
