package com.yfletch.summergarden;


import com.google.inject.Provides;
import com.yfletch.summergarden.util.ObjectManager;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.WallObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import javax.inject.Inject;
import java.awt.Color;
import org.pf4j.Extension;

@Extension
@Slf4j
@PluginDescriptor(
	name = "OC Summer Garden",
	enabledByDefault = false,
	description = "One-click summer garden"
)
public class SummerGardenPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private SummerGardenOverlay overlay;

	@Inject
	private ActionOverlay actionOverlay;

	@Inject
	private StatsOverlay debugOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ElementalCollisionDetector collisionDetector;

	@Inject
	private InfoBoxManager infoBoxManager;

	@Inject
	private ItemManager itemManager;

	@Inject
	private SummerGardenConfig config;

	@Inject
	private ObjectManager objectManager;

	@Inject
	private ActionRunner runner;

	@Inject
	private State state;

	@Inject
	private Stats stats;

	private final Pattern coloredItemPattern = Pattern.compile("<.+>(.+)<.+>");

	private boolean overlayEnabled = false;
	private InfoBox countdownTimerInfoBox;
	private static final WorldPoint GARDEN = new WorldPoint(2915, 5490, 0);

	@Override
	protected void startUp() throws Exception
	{
		enableOverlay();
		enableCountdownTimerInfoBox();
		collisionDetector.setGateStart(true);
	}

	@Override
	protected void shutDown() throws Exception
	{
		disableOverlay();
		disableCountdownTimerInfoBox();
	}

	private void enableOverlay()
	{
		if (overlayEnabled)
		{
			return;
		}

		overlayEnabled = true;
		overlayManager.add(overlay);
		overlayManager.add(actionOverlay);
		overlayManager.add(debugOverlay);
	}

	private void disableOverlay()
	{
		if (overlayEnabled)
		{
			overlayManager.remove(overlay);
			overlayManager.remove(actionOverlay);
			overlayManager.remove(debugOverlay);
		}
		overlayEnabled = false;
	}

	private void enableCountdownTimerInfoBox()
	{
		if (countdownTimerInfoBox == null)
		{
			countdownTimerInfoBox = new InfoBox(itemManager.getImage(Consts.SUMMER_SQIRK), this)
			{
				@Override
				public String getText()
				{
					return "" + collisionDetector.getTicksUntilStart();
				}

				@Override
				public Color getTextColor()
				{
					return null;
				}
			};
			infoBoxManager.addInfoBox(countdownTimerInfoBox);
		}
	}

	private void disableCountdownTimerInfoBox()
	{
		infoBoxManager.removeInfoBox(countdownTimerInfoBox);
		countdownTimerInfoBox = null;
	}

	@Subscribe
	public void onGameTick(GameTick e)
	{
		Player p = client.getLocalPlayer();
		if (p == null)
		{
			return;
		}

		stats.tick();

		if (p.getWorldLocation().distanceTo2D(GARDEN) < 50)
		{
			enableCountdownTimerInfoBox();
			client.getNpcs()
				.stream()
				.filter(ElementalCollisionDetector::isSummerElemental)
				.forEach(npc -> collisionDetector.updatePosition(npc, client.getTickCount()));
			collisionDetector.updateCountdownTimer(client.getTickCount());
		}
		else
		{
			disableCountdownTimerInfoBox();
		}

		runner.onTick();
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		GameObject gameObject = event.getGameObject();
		objectManager.add(gameObject);
	}

	@Subscribe
	public void onWallObjectSpawned(WallObjectSpawned event)
	{
		WallObject gameObject = event.getWallObject();
		objectManager.add(gameObject);
	}

	@Subscribe
	public void onDecorativeObjectSpawned(DecorativeObjectSpawned event)
	{
		DecorativeObject gameObject = event.getDecorativeObject();
		objectManager.add(gameObject);
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		GameObject gameObject = event.getGameObject();
		objectManager.hide(gameObject);
	}

	@Subscribe
	public void onWallObjectDespawned(WallObjectDespawned event)
	{
		WallObject gameObject = event.getWallObject();
		objectManager.hide(gameObject);
	}


	@Subscribe
	public void onDecorativeObjectDespawned(DecorativeObjectDespawned event)
	{
		DecorativeObject gameObject = event.getDecorativeObject();
		objectManager.hide(gameObject);
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		runner.run(event);
//		debugEntries();

		state.setUsingItemName(null);

		// "use"
		if (event.getMenuAction() == MenuAction.WIDGET_TARGET)
		{
			String target = event.getMenuTarget();
			if (!target.contains("->"))
			{
				target = target.replaceAll("</?col(?:=\\w{6})?>", "");
				state.setUsingItemName(target);
			}
		}
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		Actor actor = event.getActor();
		if (actor == null || actor.getName() == null)
		{
			return;
		}

		if (actor.getName().equals(client.getLocalPlayer().getName()) && actor.getAnimation() == Consts.SUCCESSFUL_PICK_ANIM)
		{
			stats.incFruitPicked();
		}

		if (actor.getName().equals("Summer Elemental") && actor.getAnimation() == Consts.ELEMENTAL_TELEPORT_ANIM)
		{
			stats.incFails();
		}
	}

	public void debugEntries()
	{
		MenuEntry[] entries = client.getMenuEntries();

		for (MenuEntry entry : entries)
		{
			System.out.println("Entry: \n\tOption: " + entry.getOption() + "\n\tTarget: " + entry.getTarget() + "\n\tIdentifier: " + entry.getIdentifier() + "\n\tOpcode: " + entry.getOpcode() + "\n\tParam0: " + entry.getParam0() + "\n\tParam1: " + entry.getParam1());
		}
	}

	@Provides
	SummerGardenConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SummerGardenConfig.class);
	}
}
