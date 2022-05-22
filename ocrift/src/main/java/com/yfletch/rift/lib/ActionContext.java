package com.yfletch.rift.lib;

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
	private final Map<String, String> flags = new HashMap<>();

	@Setter
	private String usingItemName;
}
