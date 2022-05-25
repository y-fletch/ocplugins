package com.yfletch.rift.helper;

import com.yfletch.rift.Cell;
import com.yfletch.rift.RiftContext;
import com.yfletch.rift.Rune;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

@Slf4j
public class CellTileDecider
{
	private static final WorldPoint CENTER = new WorldPoint(3615, 9510, 0);

	private final RiftContext context;
	private final RuneDecider runeDecider;

	public CellTileDecider(RiftContext context)
	{
		this.context = context;
		this.runeDecider = new RuneDecider(context);
	}

	public TileObject pick()
	{
		List<TileObject> inactive = context.getObjectHelper().getAll(Cell.UNCHARGED.getObjectId());
		if (!inactive.isEmpty())
		{
			return getClosest(inactive, CENTER);
		}

		List<TileObject> tiles = context.getObjectHelper().getAll(
			Cell.WEAK.getObjectId(),
			Cell.MEDIUM.getObjectId(),
			Cell.STRONG.getObjectId(),
			Cell.OVERCHARGED.getObjectId()
		);

		WorldPoint target = CENTER;
		Rune targetRune = runeDecider.pick();
		if (targetRune != null)
		{
			TileObject guardian = context.getObjectHelper().getNearest(targetRune.getGuardianId());
			if (guardian != null)
			{
				target = guardian.getWorldLocation();
			}
		}

		TileObject closestHealable = getClosest(
			tiles.stream().filter(this::canHeal)
				.collect(Collectors.toList()), target);

		return closestHealable != null
			? closestHealable
			: getClosest(tiles, target);
	}

	private TileObject getClosest(List<TileObject> objects, WorldPoint target)
	{
		TileObject closest = null;
		for (TileObject object : objects)
		{
			if (closest == null
				|| object.getWorldLocation().distanceTo(target) < closest.getWorldLocation().distanceTo(target))
			{
				closest = object;
			}
		}

		return closest;
	}

	private boolean canHeal(TileObject tile)
	{
		NPC npc = context.getNpcHelper().getNearestTo(new int[]{11424, 11425}, tile);

		// not the exact health amount, but it's enough to figure out
		// which has the lowest HP
		return !(npc == null || npc.getHealthRatio() == 100);
	}
}
