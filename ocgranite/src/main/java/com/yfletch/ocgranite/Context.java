package com.yfletch.ocgranite;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionContext;
import com.yfletch.occore.util.ObjectHelper;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

@Slf4j
@Singleton
public class Context extends ActionContext
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
	private Config config;

	@Setter
	@Getter
	private TileObject previousRock;

	public boolean isInMine()
	{
		return isInZone(
			new WorldPoint(3166, 2908, 0),
			new WorldPoint(3167, 2910, 0)
		);
	}

	public boolean useHumidify()
	{
		return config.useHumidify();
	}

	public boolean isCastingHumidify()
	{
		return client.getLocalPlayer().getAnimation() == 6294;
	}

	private List<TileObject> getRocks()
	{
		final var all = objectHelper
			.where(
				ObjectID.GRANITE_ROCKS,
				obj -> WorldPoint.isInZone(
					new WorldPoint(3165, 2908, 0),
					new WorldPoint(3167, 2911, 0),
					obj.getWorldLocation()
				)
			);
		// sort by y value
		all.sort(Comparator.comparingInt(TileObject::getY));
		return all;
	}

	public TileObject getNextRock()
	{
		final var rocks = getRocks();
		if (previousRock == null)
		{
			return rocks.get(0);
		}

		return rocks.stream()
			.filter(o -> o.getY() > previousRock.getY())
			.findFirst().orElse(rocks.get(0));
	}

	public boolean isFirstRock(TileObject rock)
	{
		return rock.getWorldLocation().equals(
			new WorldPoint(3165, 2908, 0)
		);
	}

	public void onGraniteMined()
	{
		flag("mining", false);
	}

	public void onDrop(String item, int n)
	{
		final var kg = item.split(" ", 2)[1];
		flag("drop-" + kg + "-" + n, true, 1);
	}

	public int getNextDropIndex(String itemName)
	{
		final var inventory = client.getItemContainer(InventoryID.INVENTORY);
		if (inventory == null) return 0;

		final var kg = itemName.split(" ", 2)[1];
		var n = 0;
		for (final var item : inventory.getItems())
		{
			if (item.getName().contains(kg))
			{
				if (!flag("drop-" + kg + "-" + n))
				{
					return n;
				}
				n++;
			}
		}

		return 0;
	}
}
