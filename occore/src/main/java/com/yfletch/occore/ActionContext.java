package com.yfletch.occore;

import com.google.inject.Inject;
import com.yfletch.occore.util.ObjectHelper;
import com.yfletch.occore.util.RegionPoint;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

/**
 * Contains context (game state, configs) to pass to
 * the actions to help determine when they should run.
 * <p>
 * Extend this class and add your own getters.
 */
public class ActionContext
{
	@Inject
	private Client client;

	@Inject
	private ObjectHelper objectHelper;

	private final Map<String, Boolean> flags = new HashMap<>();
	private final Map<String, Integer> ephemeral = new HashMap<>();

	@Setter(AccessLevel.PACKAGE)
	@Getter
	private String usingItemName;

	/**
	 * Persist a flag in context.
	 */
	public void flag(String key, Boolean value)
	{
		flags.put(key, value);
		ephemeral.remove(key);
	}

	/**
	 * Persist an ephemeral flag in context, that will be
	 * cleared after the set amount of ticks.
	 */
	public void flag(String key, Boolean value, int ticks)
	{
		flag(key, value);
		ephemeral.put(key, ticks);
	}

	/**
	 * Get a flag's value. Defaults to false if the flag
	 * isn't found.
	 */
	public Boolean flag(String key)
	{
		return flags.getOrDefault(key, false);
	}

	/**
	 * Decay ephemeral flags each tick.
	 */
	public void tick()
	{
		for (Map.Entry<String, Integer> entry : ephemeral.entrySet())
		{
			if (entry.getValue() < 1)
			{
				ephemeral.remove(entry.getKey());
				flags.remove(entry.getKey());
			}
			else
			{
				ephemeral.put(entry.getKey(), entry.getValue() - 1);
			}
		}
	}

	/**
	 * Clear a flag if it exists
	 */
	public void clearFlag(String key)
	{
		flags.remove(key);
		ephemeral.remove(key);
	}

	/**
	 * Clear all flags. Should only be necessary
	 * for plugin state resetting.
	 */
	public void clearFlags()
	{
		ephemeral.clear();
		flags.clear();
	}

	/**
	 * Get flags with their ephemeral tick time, for use
	 * in debug overlays
	 */
	public Map<String, String> getDebugFlags()
	{
		Map<String, String> debugFlags = new HashMap<>();
		for (Map.Entry<String, Boolean> entry : flags.entrySet())
		{
			String ticksLeft = "";
			if (ephemeral.containsKey(entry.getKey()))
			{
				ticksLeft = " (" + ephemeral.get(entry.getKey()) + ")";
			}
			debugFlags.put(entry.getKey(), entry.getValue() + ticksLeft);
		}

		return debugFlags;
	}

	/**
	 * Get current player
	 */
	public Player getPlayer()
	{
		return client.getLocalPlayer();
	}

	/**
	 * Get current player location
	 */
	public WorldPoint getPlayerLocation()
	{
		return getPlayer().getWorldLocation();
	}

	/**
	 * Get current player destination location
	 */
	public WorldPoint getDestinationLocation()
	{
		LocalPoint location = client.getLocalDestinationLocation();
		return location != null ? WorldPoint.fromLocal(client, location) : null;
	}

	/**
	 * Check if player is at position
	 */
	public boolean isAt(WorldPoint worldPoint)
	{
		return getPlayerLocation().equals(worldPoint);
	}

	/**
	 * Check if player is at position
	 */
	public boolean isAt(RegionPoint regionPoint)
	{
		return getPlayerLocation().equals(regionPoint.toWorld());
	}

	/**
	 * Check if player is pathing to the nearest object
	 * matching the given id
	 */
	public boolean isPathingTo(int objectId)
	{
		return isPathingTo(objectHelper.getNearest(objectId));
	}

	/**
	 * Check if the player is pathing to the object
	 */
	public boolean isPathingTo(TileObject object)
	{
		WorldPoint dest = getDestinationLocation();
		return object != null
			&& dest != null
			&& objectHelper.isBeside(dest, object);
	}

	/**
	 * Check if the player is pathing to the given position
	 */
	public boolean isPathingTo(WorldPoint worldPoint)
	{
		WorldPoint destination = getDestinationLocation();
		return destination != null && destination.equals(worldPoint);
	}
}
