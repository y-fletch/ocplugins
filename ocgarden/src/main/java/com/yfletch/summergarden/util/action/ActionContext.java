package com.yfletch.summergarden.util.action;

import com.yfletch.summergarden.ElementalCollisionDetector;
import com.yfletch.summergarden.State;
import com.yfletch.summergarden.SummerGardenConfig;
import com.yfletch.summergarden.util.ObjectManager;
import javax.inject.Inject;
import lombok.Getter;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ActionContext
{
	private final Map<String, String> ctx = new HashMap<>();

	@Inject
	private State state;

	@Inject
	private ElementalCollisionDetector collisionDetector;

	@Inject
	private ObjectManager objectManager;

	@Inject
	private SummerGardenConfig config;

	public void set(String key, String value)
	{
		this.ctx.put(key, value);
	}

	@Nullable
	public String set(String key)
	{
		if (!this.ctx.containsKey(key))
		{
			return null;
		}
		return this.ctx.get(key);
	}

	public void setInt(String key, int value)
	{
		this.set(key, "" + value);
	}

	@Nullable
	public Integer getInt(String key)
	{
		if (!this.ctx.containsKey(key))
		{
			return null;
		}
		return Integer.getInteger(this.set(key));
	}

	public void setFlag(String key, boolean value)
	{
		this.set(key, value ? "true" : "false");
	}

	public Boolean getFlag(String key)
	{
		if (!this.ctx.containsKey(key))
		{
			return false;
		}
		return this.set(key).equals("true");
	}
}
