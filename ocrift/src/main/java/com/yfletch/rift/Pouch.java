package com.yfletch.rift;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@Getter
@AllArgsConstructor
public enum Pouch
{
	SMALL("Small pouch", ItemID.SMALL_POUCH, 3, ItemID.SMALL_POUCH, 3),
	MEDIUM("Medium pouch", ItemID.MEDIUM_POUCH, 6, ItemID.MEDIUM_POUCH_5511, 3),
	LARGE("Large pouch", ItemID.LARGE_POUCH, 9, ItemID.LARGE_POUCH_5513, 7),
	GIANT("Giant pouch", ItemID.GIANT_POUCH, 12, ItemID.GIANT_POUCH_5515, 9),
	COLOSSAL("Colossal pouch", ItemID.COLOSSAL_POUCH, 40, ItemID.COLOSSAL_POUCH_26786, 35);

	private String itemName;
	private int itemId;
	private int capacity;
	private int degradedItemId;
	private int degradedCapacity;
}
