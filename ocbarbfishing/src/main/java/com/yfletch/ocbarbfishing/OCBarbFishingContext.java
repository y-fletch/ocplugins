package com.yfletch.ocbarbfishing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.ocbarbfishing.util.Const;
import com.yfletch.ocbarbfishing.util.Method;
import com.yfletch.occore.ActionContext;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;

@Singleton
public class OCBarbFishingContext extends ActionContext
{
	@Inject
	private Client client;

	@Inject
	private OCBarbFishingConfig config;

	@Getter
	private int tick = 0;

	@Override
	public void tick()
	{
		super.tick();

		tick++;
	}

	public boolean isTick(int t)
	{
		return tick == t;
	}

	public boolean isAtFishingSpots()
	{
		return isInZone(
			new WorldPoint(2493, 3490, 0),
			new WorldPoint(2509, 3520, 0)
		);
	}

	public boolean hasNoFish()
	{
		return !hasItem(ItemID.LEAPING_TROUT)
			&& !hasItem(ItemID.LEAPING_SALMON)
			&& !hasItem(ItemID.LEAPING_STURGEON);
	}

	public boolean hasFood()
	{
		return hasItem(ItemID.ROE)
			|| hasItem(ItemID.CAVIAR);
	}

	public boolean isFishing()
	{
		return client.getLocalPlayer().getAnimation() == Const.FISHING_ANIMATION;
	}

	public void beginFishing()
	{
		tick = 0;
	}

	public boolean isTarDropMethod()
	{
		return config.method() == Method.TAR_DROP;
	}

	public boolean isCutEatMethod()
	{
		return config.method() == Method.CUT_EAT;
	}

	public boolean isHybridMethod()
	{
		return config.method() == Method.TAR_THEN_CUT_EAT;
	}
}
