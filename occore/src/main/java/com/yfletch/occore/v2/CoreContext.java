package com.yfletch.occore.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CoreContext
{
	private final Map<String, Boolean> flags = new HashMap<>();
	private final Map<String, Integer> ephemeralFlags = new HashMap<>();
	private final Map<String, String> args = new HashMap<>();
	private final Map<String, Integer> ephemeralArgs = new HashMap<>();


	/**
	 * Persist a flag in context.
	 */
	public void flag(String key, Boolean value)
	{
		flags.put(key, value);
		ephemeralFlags.remove(key);
	}

	/**
	 * Persist an ephemeral flag in context, that will be
	 * cleared after the set amount of ticks.
	 */
	public void flag(String key, Boolean value, int ticks)
	{
		flag(key, value);
		ephemeralFlags.put(key, ticks);
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
	 * Persist an arg in context.
	 */
	public void arg(String key, String value)
	{
		args.put(key, value);
		ephemeralArgs.remove(key);
	}

	/**
	 * Persist an ephemeral arg in context, that will be
	 * cleared after the set amount of ticks.
	 */
	public void arg(String key, String value, int ticks)
	{
		arg(key, value);
		ephemeralArgs.put(key, ticks);
	}

	/**
	 * Get a arg's value. Defaults to empty string if the arg
	 * isn't found.
	 */
	public String arg(String key)
	{
		return args.getOrDefault(key, "");
	}

	public void tick()
	{
		tick(false);
	}

	/**
	 * Decay ephemeral flags each tick.
	 */
	public void tick(boolean isGameTick)
	{
		if (!isGameTick)
		{
			return;
		}

		for (Map.Entry<String, Integer> entry : new ArrayList<>(ephemeralFlags.entrySet()))
		{
			if (entry.getValue() < 1)
			{
				ephemeralFlags.remove(entry.getKey());
				flags.remove(entry.getKey());
			}
			else
			{
				ephemeralFlags.put(entry.getKey(), entry.getValue() - 1);
			}
		}

		for (Map.Entry<String, Integer> entry : new ArrayList<>(ephemeralArgs.entrySet()))
		{
			if (entry.getValue() < 1)
			{
				ephemeralArgs.remove(entry.getKey());
				args.remove(entry.getKey());
			}
			else
			{
				ephemeralArgs.put(entry.getKey(), entry.getValue() - 1);
			}
		}
	}

	/**
	 * Clear a flag or arg if it exists
	 */
	public void clear(String key)
	{
		flags.remove(key);
		ephemeralFlags.remove(key);

		args.remove(key);
		ephemeralArgs.remove(key);
	}

	/**
	 * Clear all flags and args. Should only
	 * be necessary for plugin state resetting.
	 */
	public void clear()
	{
		ephemeralFlags.clear();
		flags.clear();
	}

	/**
	 * Get flags with their ephemeral tick time, for use
	 * in debug overlays. Can also be extended.
	 */
	public Map<String, String> getDebugMap()
	{
		final var lines = new HashMap<String, String>();
		for (Map.Entry<String, Boolean> entry : flags.entrySet())
		{
			var ticksLeft = "";
			if (ephemeralFlags.containsKey(entry.getKey()))
			{
				ticksLeft = " (" + ephemeralFlags.get(entry.getKey()) + ")";
			}

			lines.put(entry.getKey(), entry.getValue() + ticksLeft);
		}

		for (Map.Entry<String, String> entry : args.entrySet())
		{
			var ticksLeft = "";
			if (ephemeralArgs.containsKey(entry.getKey()))
			{
				ticksLeft = " (" + ephemeralArgs.get(entry.getKey()) + ")";
			}

			lines.put(entry.getKey(), entry.getValue() + ticksLeft);
		}

		return lines;
	}
}
