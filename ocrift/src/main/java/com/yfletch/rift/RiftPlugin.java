package com.yfletch.rift;

import com.google.inject.Provides;
import com.yfletch.rift.action.Unknown;
import com.yfletch.rift.action.firstcycle.ClimbUpRubble;
import com.yfletch.rift.action.firstcycle.CraftEssence;
import com.yfletch.rift.action.firstcycle.MineLargeRemains;
import com.yfletch.rift.action.prep.UseSpecialAttack;
import com.yfletch.rift.action.prep.ClimbDownRubble;
import com.yfletch.rift.action.prep.TakeUnchargedCells;
import com.yfletch.rift.action.prep.TakeWeakCell;
import com.yfletch.rift.action.prep.WalkToLargeRemains;
import com.yfletch.rift.lib.ActionRunner;
import com.yfletch.rift.lib.ObjectManager;
import com.yfletch.rift.lib.TrackedObject;
import com.yfletch.rift.util.MenuEntryProvider;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.ObjectID;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
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
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

@Extension
@Slf4j
@PluginDescriptor(
	name = "OC Rift",
	enabledByDefault = false,
	description = "One-click Guardians of the Rift"
)
public class RiftPlugin extends Plugin
{
	// overlays
	@Inject
	private DebugOverlay debugOverlay;

	@Inject
	private DestinationOverlay destinationOverlay;

	@Inject
	private OverlayManager overlayManager;

	private RiftActionOverlay actionOverlay;
	// end overlays

	// action runner
	@Inject
	private RiftContext context;

	@Inject
	private MenuEntryProvider menuEntryProvider;

	private ActionRunner<RiftContext> runner;
	// end action runner

	// misc
	@Inject
	private RiftConfig config;

	@Inject
	private ObjectManager objectManager;
	// end misc

	@Override
	protected void startUp()
	{
		runner = new ActionRunner<>(context, menuEntryProvider);
		runner.add(new TakeWeakCell());
		runner.add(new TakeUnchargedCells());
		runner.add(new ClimbDownRubble());
		runner.add(new WalkToLargeRemains());
		runner.add(new UseSpecialAttack());
		runner.add(new MineLargeRemains());
		runner.add(new ClimbUpRubble());
		runner.add(new CraftEssence());
		runner.add(new Unknown());

		actionOverlay = new RiftActionOverlay(runner);
		overlayManager.add(actionOverlay);
		overlayManager.add(debugOverlay);
		overlayManager.add(destinationOverlay);

		objectManager.setTrackedObjects(new TrackedObject[]{
			new TrackedObject(ObjectID.UNCHARGED_CELLS_43732),
			new TrackedObject(ObjectID.WEAK_CELLS),
			new TrackedObject(ObjectID.RUBBLE_43724),
			new TrackedObject(ObjectID.RUBBLE_43726),
			new TrackedObject(ObjectID.LARGE_GUARDIAN_REMAINS, new WorldPoint(3639, 9497, 0)),
			new TrackedObject(ObjectID.HUGE_GUARDIAN_REMAINS, new WorldPoint(3589, 9497, 0)),

			new TrackedObject(Guardian.AIR.getObjectId()),
			new TrackedObject(Guardian.WATER.getObjectId()),
			new TrackedObject(Guardian.EARTH.getObjectId()),
			new TrackedObject(Guardian.FIRE.getObjectId()),
			new TrackedObject(Guardian.MIND.getObjectId()),
			new TrackedObject(Guardian.BODY.getObjectId()),
			new TrackedObject(Guardian.COSMIC.getObjectId()),
			new TrackedObject(Guardian.CHAOS.getObjectId()),
			new TrackedObject(Guardian.NATURE.getObjectId()),
			new TrackedObject(Guardian.LAW.getObjectId()),
			new TrackedObject(Guardian.DEATH.getObjectId()),
			new TrackedObject(Guardian.BLOOD.getObjectId()),
		});
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(actionOverlay);
		overlayManager.remove(debugOverlay);
		overlayManager.remove(destinationOverlay);
		actionOverlay = null;
		runner = null;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (runner == null)
		{
			log.error("Got a game tick with no runner created!");
			return;
		}

		context.setGameTime(context.getGameTime() + 0.6);

		runner.tick();
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (runner == null)
		{
			log.error("Got a menu option click with no runner created!");
			return;
		}

		if (config.ocEnabled())
		{
			runner.run(event);
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		if (chatMessage.getType() != ChatMessageType.SPAM
			&& chatMessage.getType() != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}

		String message = chatMessage.getMessage();
		if (message.contains("The rift becomes active!"))
		{
		}
		else if (message.contains("The rift will become active in 30 seconds."))
		{
			context.setGameTime(-30);
		}
		else if (message.contains("The rift will become active in 10 seconds."))
		{
			context.setGameTime(-10);
		}
		else if (message.contains("The rift will become active in 5 seconds."))
		{
			context.setGameTime(-5);
		}
		else if (message.contains("The Portal Guardians will keep their rifts open for another 30 seconds."))
		{
			context.setGameTime(-60);
		}
	}

	// object manager tracking

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		objectManager.add(event.getGameObject());
	}

	@Subscribe
	public void onWallObjectSpawned(WallObjectSpawned event)
	{
		objectManager.add(event.getWallObject());
	}

	@Subscribe
	public void onDecorativeObjectSpawned(DecorativeObjectSpawned event)
	{
		objectManager.add(event.getDecorativeObject());
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		objectManager.hide(event.getGameObject());
	}

	@Subscribe
	public void onWallObjectDespawned(WallObjectDespawned event)
	{
		objectManager.hide(event.getWallObject());
	}

	@Subscribe
	public void onDecorativeObjectDespawned(DecorativeObjectDespawned event)
	{
		objectManager.hide(event.getDecorativeObject());
	}

	// end object manager

	@Provides
	RiftConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(RiftConfig.class);
	}
}
