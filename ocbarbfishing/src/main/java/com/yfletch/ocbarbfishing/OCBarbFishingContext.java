package com.yfletch.ocbarbfishing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionContext;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldPoint;

@Singleton
public class OCBarbFishingContext extends ActionContext
{
	@Inject
	private Client client;

	private int tick;

	@Override
	public void tick()
	{
		super.tick();

		if (!hasFish() && !hasFood())
		{
			tick = 0;
			return;
		}

		tick++;
		if (tick >= 3)
		{
			tick = 0;
		}
	}

	public boolean isTick(int t)
	{
		return tick == t;
	}

	public boolean hasBarbarianRod()
	{
		return hasItem(ItemID.BARBARIAN_ROD);
	}

	public boolean hasFeathers()
	{
		return hasItem(ItemID.FEATHER);
	}

	public boolean hasKnife()
	{
		return hasItem(ItemID.KNIFE);
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
}
