package com.yfletch.occore.v2;

import com.google.inject.Inject;
import com.yfletch.occore.v2.interaction.DeferredInteraction;
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
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.client.Static;
import net.unethicalite.client.config.UnethicaliteConfig;
import net.unethicalite.client.managers.interaction.InteractMethod;

@Slf4j
public abstract class RunnerPlugin<TContext extends CoreContext> extends Plugin
{
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
	protected final DynamicRule<TContext> action()
	{
		final var rule = new DynamicRule<TContext>();
		rules.add(rule);
		return rule.name("Unknown");
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

	private void execute()
	{
		if (!config.enabled() || currentRule == null)
		{
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
			final var interaction = getInteraction(currentRule);
			if (interaction != null && currentRule.canExecute())
			{
				interaction.execute();
				currentRule.useRepeat();
			}
		}
	}

	@Nullable
	private DeferredInteraction<?> getInteraction(Rule<TContext> rule)
	{
		nextInteraction = rule.run(context);
		if (nextInteraction == null)
		{
			// fallback to rule message
			messages = rule.messages(context);
			if (messages == null)
			{
				messages = List.of(TextColor.DANGER + "Failed to generate interaction");
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
		getInteraction(rule);

		// use new max delay
		context.setInteractionDelay(rule.maxDelay());

		currentRule = rule;
	}

	/**
	 * Determine the next rule to move to
	 */
	private void process()
	{
		// clear rule if it no longer passes
		if (currentRule != null && !passes(currentRule))
		{
			currentRule = null;
			nextInteraction = null;
			messages = null;
		}

		// find new rule to apply
		if (currentRule == null)
		{
			for (final var rule : rules)
			{
				if (passes(rule))
				{
					enable(rule);
					return;
				}
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

		if (processOnGameTick)
		{
			process();
		}

		execute();

		final var queued = Static.getClient().getQueuedMenu();
		if (queued != null)
		{
			RunnerUtil.logDebug("Q", queued.toEntry(Static.getClient()));
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (config.debugRawMenuEntries())
		{
			RunnerUtil.logDebug("RAW", event.getMenuEntry());
		}

		context.tick(false);

		if (processOnMouseClick)
		{
			process();
		}

		execute();

		if (unethicaliteConfig.interactMethod() == InteractMethod.MOUSE_FORWARDING)
		{
			final var queued = Static.getClient().getQueuedMenu();
			if (queued != null)
			{
				event.setMenuEntry(queued.toEntry(Static.getClient()));
				RunnerUtil.logDebug("OC", event.getMenuEntry());
			}
		}
	}
}
