package com.yfletch.ocbarbfishing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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

	public boolean hasFish()
	{
		return hasItem(ItemID.LEAPING_TROUT)
			|| hasItem(ItemID.LEAPING_SALMON)
			|| hasItem(ItemID.LEAPING_STURGEON);
	}

	public boolean hasFood()
	{
		return hasItem(ItemID.ROE)
			|| hasItem(ItemID.CAVIAR);
	}

	public boolean isFishing()
	{
		return client.getLocalPlayer().getAnimation() == 9349;
	}

	public void beginFishing()
	{
		tick = 0;
	}
}
