package com.yfletch.rift;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;

@Getter
@AllArgsConstructor
public enum Cell
{
	UNCHARGED("Inactive", ItemID.UNCHARGED_CELL, ObjectID.INACTIVE_CELL_TILE_43739),
	WEAK("Weak", ItemID.WEAK_CELL, ObjectID.WEAK_CELL_TILE),
	MEDIUM("Medium", ItemID.MEDIUM_CELL, ObjectID.MEDIUM_CELL_TILE),
	STRONG("Strong", ItemID.STRONG_CELL, ObjectID.STRONG_CELL_TILE),
	OVERCHARGED("Overcharged", ItemID.OVERCHARGED_CELL, ObjectID.OVERPOWERED_CELL_TILE);

	private final String name;
	private final int itemId;
	private final int objectId;

	public boolean isBetterThan(Cell other)
	{
		return itemId > other.itemId;
	}
}
