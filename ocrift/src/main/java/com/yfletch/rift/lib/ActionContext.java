package com.yfletch.rift.lib;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Contains context (game state, configs) to pass to
 * the actions to help determine when they should run.
 * <p>
 * Extend this class and add your own getters.
 */
@Getter
public class ActionContext
{
	@Getter
	private final Map<String, Boolean> flags = new HashMap<>();

	@Setter
	private String usingItemName;

	public void flag(String key, Boolean value)
	{
		flags.put(key, value);
	}

	public Boolean flag(String key)
	{
		return flags.getOrDefault(key, false);
	}

	public void clearFlags()
	{
		flags.clear();
	}
}
