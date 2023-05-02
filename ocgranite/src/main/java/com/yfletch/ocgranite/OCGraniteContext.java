package com.yfletch.ocgranite;

import com.yfletch.occore.ActionContext;
import com.yfletch.occore.util.ObjectHelper;
import java.util.Arrays;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

@Singleton
public class OCGraniteContext extends ActionContext
{
	private static final Set<Integer> GRANITE_ITEMS = Set.of(
		ItemID.GRANITE_500G,
		ItemID.GRANITE_2KG,
		ItemID.GRANITE_5KG
	);

	@Inject
	private Client client;

	@Inject
	private ObjectHelper objectHelper;

	@Inject
	private OCGraniteConfig config;

	@Setter
	@Getter
	private TileObject previousRock;

	private Item[] previousItems;

	@Getter
	@Accessors(fluent = true)
	private boolean receivedGranite;

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

	public boolean isMining()
	{
		return OCGranitePlugin.MINING_ANIMATIONS.contains(
			client.getLocalPlayer().getAnimation()
		);
	}

	public boolean isInMine()
	{
		return isInZone(
			new WorldPoint(3166, 2908, 0),
			new WorldPoint(3167, 2910, 0)
		);
	}

	public void beginMining()
	{
		tick = 0;
		receivedGranite = false;
	}

	public boolean useHumidify()
	{
		return config.humidify();
	}

	public boolean isCastingHumidify()
	{
		return client.getLocalPlayer().getAnimation() == 6294;
	}

	public TileObject getNextRock()
	{
		return objectHelper
			.queryWhere(
				ObjectID.GRANITE_ROCKS,
				obj -> WorldPoint.isInZone(
					new WorldPoint(3165, 2908, 0),
					new WorldPoint(3167, 2911, 0),
					obj.getWorldLocation()
				) && !obj.equals(previousRock))
			.nearestTo(client.getLocalPlayer());
	}

	public void onInventoryChanged(ItemContainer inventory)
	{
		var items = inventory.getItems();
		var prevList = Arrays.asList(previousItems);

		for (var item : items)
		{
			if (GRANITE_ITEMS.contains(item.getId()) && !prevList.contains(item))
			{
				receivedGranite = true;
				System.out.println("Got granite");
			}
		}

		previousItems = inventory.getItems();
	}
}
