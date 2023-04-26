package it.enok.ocnightmarezone.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;


@Getter
@RequiredArgsConstructor
public enum PotionOption
{
	NONE("None", new int[] {}),
	SUPER_COMBAT_POTION("Super Combat", new int[] {
			ItemID.SUPER_COMBAT_POTION1,
			ItemID.SUPER_COMBAT_POTION2,
			ItemID.SUPER_COMBAT_POTION3,
			ItemID.SUPER_COMBAT_POTION4,
	}),
	OVERLOAD_POTION("Overload", new int[] {
			ItemID.OVERLOAD_1,
			ItemID.OVERLOAD_2,
			ItemID.OVERLOAD_3,
			ItemID.OVERLOAD_4,
	});

	private final String label;
	private final int[] itemIds;

	@Override
	public String toString()
	{
		return label;
	}
}