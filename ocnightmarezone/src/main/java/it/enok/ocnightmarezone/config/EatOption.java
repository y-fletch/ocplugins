package it.enok.ocnightmarezone.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;


@Getter
@RequiredArgsConstructor
public enum EatOption
{
	LOCATOR_ORB("Locator Orb", ItemID.LOCATOR_ORB),
	ROCK_CAKE("Rock Cake", ItemID.DWARVEN_ROCK_CAKE_7510),
	NONE("None", -1);

	private final String label;
	private final int itemId;

	@Override
	public String toString()
	{
		return label;
	}
}