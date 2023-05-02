package com.yfletch.ocgranite;

import com.google.inject.Provides;
import com.yfletch.ocgranite.overlay.ActionOverlay;
import com.yfletch.ocgranite.overlay.DebugOverlay;
import com.yfletch.ocgranite.overlay.HighlightOverlay;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
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
	name = "OC Granite",
	description = "One-click 3t4g",
	enabledByDefault = false
)
public class OCGranitePlugin extends Plugin
{
	public static final Set<Integer> MINING_ANIMATIONS = Set.of(
		AnimationID.MINING_RUNE_PICKAXE,
		AnimationID.MINING_DRAGON_PICKAXE,
		AnimationID.MINING_DRAGON_PICKAXE_OR,
		AnimationID.MINING_DRAGON_PICKAXE_UPGRADED,
		AnimationID.MINING_DRAGON_PICKAXE_OR_TRAILBLAZER,
		AnimationID.MINING_GILDED_PICKAXE,
		AnimationID.MINING_CRYSTAL_PICKAXE,
		AnimationID.MINING_INFERNAL_PICKAXE,
		AnimationID.MINING_3A_PICKAXE
	);

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private OCGraniteRunner runner;

	@Inject
	private OCGraniteContext context;

	@Inject
	private OCGraniteConfig config;

	@Inject
	private ActionOverlay actionOverlay;

	@Inject
	private DebugOverlay debugOverlay;

	@Inject
	private HighlightOverlay highlightOverlay;

	@Override
	protected void startUp()
	{
		overlayManager.add(actionOverlay);
		overlayManager.add(debugOverlay);
		overlayManager.add(highlightOverlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(actionOverlay);
		overlayManager.remove(debugOverlay);
		overlayManager.remove(highlightOverlay);
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
			&& MINING_ANIMATIONS.contains(event.getActor().getAnimation()))
		{
			context.beginMining();
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() == InventoryID.INVENTORY.getId())
		{
			context.onInventoryChanged(event.getItemContainer());
		}
	}

	@Provides
	OCGraniteConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OCGraniteConfig.class);
	}
}
