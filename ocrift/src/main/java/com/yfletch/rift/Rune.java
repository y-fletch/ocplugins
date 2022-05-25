package com.yfletch.rift;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;

@Getter
@AllArgsConstructor
public enum Rune
{
	AIR("Air", ItemID.AIR_RUNE, Cell.WEAK, ObjectID.GUARDIAN_OF_AIR, false, 1, 4353),
	WATER("Water", ItemID.WATER_RUNE, Cell.MEDIUM, ObjectID.GUARDIAN_OF_WATER, false, 5, 4355),
	EARTH("Earth", ItemID.EARTH_RUNE, Cell.STRONG, ObjectID.GUARDIAN_OF_EARTH, false, 9, 4356),
	FIRE("Fire", ItemID.FIRE_RUNE, Cell.OVERCHARGED, ObjectID.GUARDIAN_OF_FIRE, false, 14, 4357),
	MIND("Mind", ItemID.MIND_RUNE, Cell.WEAK, ObjectID.GUARDIAN_OF_MIND, true, 2, 4354),
	BODY("Body", ItemID.BODY_RUNE, Cell.WEAK, ObjectID.GUARDIAN_OF_BODY, true, 20, 4358),
	COSMIC("Cosmic", ItemID.COSMIC_RUNE, Cell.MEDIUM, ObjectID.GUARDIAN_OF_COSMIC, true, 27, 4359),
	CHAOS("Chaos", ItemID.CHAOS_RUNE, Cell.MEDIUM, ObjectID.GUARDIAN_OF_CHAOS, true, 35, 4360),
	NATURE("Nature", ItemID.NATURE_RUNE, Cell.STRONG, ObjectID.GUARDIAN_OF_NATURE, true, 44, 4361),
	LAW("Law", ItemID.LAW_RUNE, Cell.STRONG, ObjectID.GUARDIAN_OF_LAW, true, 54, 4362),
	DEATH("Death", ItemID.DEATH_RUNE, Cell.OVERCHARGED, ObjectID.GUARDIAN_OF_DEATH, true, 65, 4363),
	BLOOD("Blood", ItemID.BLOOD_RUNE, Cell.OVERCHARGED, ObjectID.GUARDIAN_OF_BLOOD, true, 77, 4364);

	private final String name;
	private final int itemId;
	private final Cell cell;
	private final int guardianId;
	private final boolean catalytic;
	private final int requiredLevel;
	private final int spriteId;

	public boolean isBetterThan(Rune rune)
	{
		return cell.isBetterThan(rune.cell);
	}
}
