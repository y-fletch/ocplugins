package it.enok.ocnightmarezone.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;


@Getter
@RequiredArgsConstructor
public enum Eat
{
	ROCK_CAKE("Rock Cake", ItemID.ROCK_CAKE),
	LOCATOR_ORB("Locator Orb", ItemID.LOCATOR_ORB);

	private final String label;
	private final int itemId;

	@Override
	public String toString()
	{
		return label;
	}
}