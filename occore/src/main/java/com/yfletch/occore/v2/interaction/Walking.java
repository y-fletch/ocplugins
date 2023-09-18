package com.yfletch.occore.v2.interaction;

import static com.yfletch.occore.v2.util.Util.nameMatching;
import static com.yfletch.occore.v2.util.Util.offset;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.movement.pathfinder.Pathfinder;
import net.unethicalite.api.movement.pathfinder.Walker;
import net.unethicalite.client.Static;

@Slf4j
public class Walking
{
	private static final int STEPS_PER_CLICK = 21;

	private static List<WorldPoint> lastPathfindResult;
	private static WorldPoint lastPathfindDestination;

	public static DeferredWalkInteraction walk(WorldPoint target)
	{
		return new DeferredWalkInteraction(target);
	}

	public static DeferredWalkInteraction walk(int x, int y)
	{
		final var current = Static.getClient().getLocalPlayer().getWorldLocation();
		return new DeferredWalkInteraction(offset(current, x, y));
	}

	/**
	 * Automatically path-find to the target point.
	 * Will traverse obstacles and open doors,
	 * The action containing this must be marked with
	 * .many(), .skipIfNull(), and optionally .oncePerTick()
	 */
	public static DeferredInteraction walkPathTo(WorldPoint target)
	{
		final var currentLocation = Static.getClient().getLocalPlayer().getWorldLocation();

		if (currentLocation.distanceTo(target) < 5)
		{
			Static.getEntityRenderer().setCurrentPath(null);
			return null;
		}

		final var fullPath = runPathfinder(currentLocation, target);
		final var remainingPath = Walker.remainingPath(fullPath);

		Static.getEntityRenderer().setCurrentPath(remainingPath);

		final var nextObstacleInteraction = getNextObstacleInteraction(remainingPath);
		if (nextObstacleInteraction != null)
		{
			return nextObstacleInteraction;
		}

		if (!remainingPath.isEmpty() && remainingPath.size() > 5)
		{
			final var lastIdx = remainingPath.size() - 1;
			final var nextIdx = Math.min(lastIdx, STEPS_PER_CLICK);

			return walk(remainingPath.get(nextIdx));
		}

		return null;
	}

	/**
	 * Automatically path-find to the target interactable.
	 * Will traverse obstacles and open doors,
	 * The action containing this must be marked with
	 * .many(), .skipIfNull(), and optionally .oncePerTick()
	 */
	public static DeferredInteraction walkPathTo(DeferredInteractable<?> interactable)
	{
		if (!interactable.exists())
		{
			return null;
		}

		final var target = interactable.unwrap();
		if (target instanceof NPC)
		{
			return walkPathTo(((NPC) target).getWorldLocation());
		}

		if (target instanceof GameObject)
		{
			return walkPathTo(((GameObject) target).getWorldLocation());
		}

		if (target instanceof GroundObject)
		{
			return walkPathTo(((GroundObject) target).getWorldLocation());
		}

		return null;
	}

	private static DeferredInteraction getNextObstacleInteraction(List<WorldPoint> path)
	{
		for (var point : path)
		{
			final var door = TileObjects.getFirstAt(point, nameMatching("Door"));
			if (door != null && door.hasAction("Open"))
			{
				return Entities.of(door).interact("Open");
			}
		}

		return null;
	}

	private static List<WorldPoint> runPathfinder(WorldPoint start, WorldPoint destination)
	{
		if (lastPathfindDestination == null || !lastPathfindDestination.equals(destination))
		{
			log.info("Recalculating path...");

			lastPathfindResult = new Pathfinder(
				Static.getGlobalCollisionMap(),
				new HashMap<>(),
				List.of(start),
				destination,
				false
			).find();
			lastPathfindDestination = destination;
		}

		return lastPathfindResult;
	}
}
