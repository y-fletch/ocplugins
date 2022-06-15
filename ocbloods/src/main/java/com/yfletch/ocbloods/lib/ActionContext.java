package com.yfletch.ocbloods.lib;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Contains context (game state, configs) to pass to
 * the actions to help determine when they should run.
 * <p>
 * Extend this class and add your own getters.
 */
@Getter
public class ActionContext
{
	private final Map<String, Boolean> flags = new HashMap<>();
	private final Map<String, Integer> ephemeral = new HashMap<>();

	@Setter
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

	public void clearFlags()
	{
		ephemeral.clear();
		flags.clear();
	}

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
}
