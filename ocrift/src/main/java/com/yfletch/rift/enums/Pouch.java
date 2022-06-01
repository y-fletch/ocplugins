package com.yfletch.rift.enums;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@Getter
@AllArgsConstructor
public enum Pouch
{
	SMALL("Small pouch", ItemID.SMALL_POUCH, 3, Integer.MAX_VALUE, 3, -1),
	MEDIUM("Medium pouch", ItemID.MEDIUM_POUCH, 6, ItemID.MEDIUM_POUCH_5511, 3, 44),
	LARGE("Large pouch", ItemID.LARGE_POUCH, 9, ItemID.LARGE_POUCH_5513, 7, 31),
	GIANT("Giant pouch", ItemID.GIANT_POUCH, 12, ItemID.GIANT_POUCH_5515, 9, 11),
	COLOSSAL("Colossal pouch", ItemID.COLOSSAL_POUCH, 40, ItemID.COLOSSAL_POUCH_26786, 35, 8);

	private final String itemName;
	private final int itemId;
	private final int capacity;
	private final int degradedItemId;
	private final int degradedCapacity;
	private final int uses;

	public static Pouch getById(int id)
	{
		return Arrays.stream(Pouch.values())
			.filter(pouch -> pouch.getItemId() == id || pouch.getDegradedItemId() == id)
			.findFirst()
			.orElse(null);
	}
}
