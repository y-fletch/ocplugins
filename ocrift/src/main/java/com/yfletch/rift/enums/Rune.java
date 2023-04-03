package com.yfletch.rift.enums;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.Quest;

@Getter
@AllArgsConstructor
public enum Rune
{
	AIR("Air", ItemID.AIR_RUNE, ItemID.PORTAL_TALISMAN_AIR, Cell.WEAK, ObjectID.GUARDIAN_OF_AIR, false, 1, 4353, null),
	WATER("Water", ItemID.WATER_RUNE, ItemID.PORTAL_TALISMAN_WATER, Cell.MEDIUM, ObjectID.GUARDIAN_OF_WATER, false, 5, 4355, null),
	EARTH("Earth", ItemID.EARTH_RUNE, ItemID.PORTAL_TALISMAN_EARTH, Cell.STRONG, ObjectID.GUARDIAN_OF_EARTH, false, 9, 4356, null),
	FIRE("Fire", ItemID.FIRE_RUNE, ItemID.PORTAL_TALISMAN_FIRE, Cell.OVERCHARGED, ObjectID.GUARDIAN_OF_FIRE, false, 14, 4357, null),
	MIND("Mind", ItemID.MIND_RUNE, ItemID.PORTAL_TALISMAN_MIND, Cell.WEAK, ObjectID.GUARDIAN_OF_MIND, true, 2, 4354, null),
	BODY("Body", ItemID.BODY_RUNE, ItemID.PORTAL_TALISMAN_BODY, Cell.WEAK, ObjectID.GUARDIAN_OF_BODY, true, 20, 4358, null),
	COSMIC("Cosmic", ItemID.COSMIC_RUNE, ItemID.PORTAL_TALISMAN_COSMIC, Cell.MEDIUM, ObjectID.GUARDIAN_OF_COSMIC, true, 27, 4359, null),
	CHAOS("Chaos", ItemID.CHAOS_RUNE, ItemID.PORTAL_TALISMAN_CHAOS, Cell.MEDIUM, ObjectID.GUARDIAN_OF_CHAOS, true, 35, 4360, null),
	NATURE("Nature", ItemID.NATURE_RUNE, ItemID.PORTAL_TALISMAN_NATURE, Cell.STRONG, ObjectID.GUARDIAN_OF_NATURE, true, 44, 4361, null),
	LAW("Law", ItemID.LAW_RUNE, ItemID.PORTAL_TALISMAN_LAW, Cell.STRONG, ObjectID.GUARDIAN_OF_LAW, true, 54, 4362, null),
	DEATH("Death", ItemID.DEATH_RUNE, ItemID.PORTAL_TALISMAN_DEATH, Cell.OVERCHARGED, ObjectID.GUARDIAN_OF_DEATH, true, 65, 4363, Quest.MOURNINGS_END_PART_II),
	BLOOD("Blood", ItemID.BLOOD_RUNE, ItemID.PORTAL_TALISMAN_BLOOD, Cell.OVERCHARGED, ObjectID.GUARDIAN_OF_BLOOD, true, 77, 4364, Quest.SINS_OF_THE_FATHER);

	private final String name;
	private final int itemId;
	private final int talismanId;
	private final Cell cell;
	private final int guardianId;
	private final boolean catalytic;
	private final int requiredLevel;
	private final int spriteId;
	private final Quest requiredQuest;

	public boolean isBetterThan(Rune rune)
	{
		return cell.isBetterThan(rune.cell);
	}

	public static Rune getByItemId(int itemId)
	{
		return Arrays.stream(Rune.values())
			.filter(rune -> rune.itemId == itemId)
			.findFirst()
			.orElse(null);
	}
}
