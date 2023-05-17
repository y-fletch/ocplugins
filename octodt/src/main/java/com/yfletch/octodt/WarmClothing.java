package com.yfletch.octodt;

import java.util.ArrayList;
import java.util.List;
import net.runelite.api.ItemID;
import net.unethicalite.api.items.Equipment;

public class WarmClothing
{
	private final static List<Integer> ids = new ArrayList<>();

	static
	{
		ids.add(ItemID.SANTA_MASK);
		ids.add(ItemID.SANTA_JACKET);
		ids.add(ItemID.SANTA_PANTALOONS);
		ids.add(ItemID.SANTA_GLOVES);
		ids.add(ItemID.SANTA_BOOTS);

		ids.add(ItemID.ANTISANTA_MASK);
		ids.add(ItemID.ANTISANTA_JACKET);
		ids.add(ItemID.ANTISANTA_PANTALOONS);
		ids.add(ItemID.ANTISANTA_GLOVES);
		ids.add(ItemID.ANTISANTA_BOOTS);

		ids.add(ItemID.BUNNY_TOP);
		ids.add(ItemID.BUNNY_LEGS);
		ids.add(ItemID.BUNNY_PAWS);
		ids.add(ItemID.BUNNY_FEET);

		ids.add(ItemID.CLUE_HUNTER_GARB);
		ids.add(ItemID.CLUE_HUNTER_TROUSERS);
		ids.add(ItemID.CLUE_HUNTER_GLOVES);
		ids.add(ItemID.CLUE_HUNTER_CLOAK);

		ids.add(ItemID.POLAR_CAMO_TOP);
		ids.add(ItemID.POLAR_CAMO_LEGS);
		ids.add(ItemID.WOOD_CAMO_TOP);
		ids.add(ItemID.WOOD_CAMO_LEGS);
		ids.add(ItemID.JUNGLE_CAMO_TOP);
		ids.add(ItemID.JUNGLE_CAMO_LEGS);
		ids.add(ItemID.DESERT_CAMO_TOP);
		ids.add(ItemID.DESERT_CAMO_LEGS);
		ids.add(ItemID.LARUPIA_HAT);
		ids.add(ItemID.LARUPIA_TOP);
		ids.add(ItemID.LARUPIA_LEGS);
		ids.add(ItemID.GRAAHK_HEADDRESS);
		ids.add(ItemID.GRAAHK_TOP);
		ids.add(ItemID.GRAAHK_LEGS);
		ids.add(ItemID.KYATT_HAT);
		ids.add(ItemID.KYATT_TOP);
		ids.add(ItemID.KYATT_LEGS);

		ids.add(ItemID.BOMBER_CAP);
		ids.add(ItemID.BOMBER_JACKET);

		ids.add(ItemID.YAKHIDE_ARMOUR);
		ids.add(ItemID.YAKHIDE_ARMOUR_10824);

		ids.add(ItemID.PYROMANCER_HOOD);
		ids.add(ItemID.PYROMANCER_GARB);
		ids.add(ItemID.PYROMANCER_ROBE);
		ids.add(ItemID.PYROMANCER_BOOTS);

		ids.add(ItemID.CHICKEN_HEAD);
		ids.add(ItemID.CHICKEN_WINGS);
		ids.add(ItemID.CHICKEN_LEGS);
		ids.add(ItemID.CHICKEN_FEET);

		ids.add(ItemID.EVIL_CHICKEN_HEAD);
		ids.add(ItemID.EVIL_CHICKEN_WINGS);
		ids.add(ItemID.EVIL_CHICKEN_LEGS);
		ids.add(ItemID.EVIL_CHICKEN_FEET);

		ids.add(ItemID.SANTA_HAT);
		ids.add(ItemID.BLACK_SANTA_HAT);
		ids.add(ItemID.INVERTED_SANTA_HAT);
		ids.add(ItemID.FESTIVE_ELF_HAT);
		ids.add(ItemID.FESTIVE_GAMES_CROWN);
		ids.add(ItemID.BUNNYMAN_MASK);
		ids.add(ItemID.BEARHEAD);
		ids.add(ItemID.FIRE_TIARA);
		ids.add(ItemID.ELEMENTAL_TIARA);
		ids.add(ItemID.LUMBERJACK_HAT);
		ids.add(ItemID.SNOW_GOGGLES__HAT);
		ids.add(ItemID.FIREMAKING_HOOD);
		ids.add(ItemID.FIRE_MAX_HOOD);
		ids.add(ItemID.FIRE_MAX_CAPE);

		ids.add(ItemID.JESTER_SCARF);
		ids.add(ItemID.TRIJESTER_SCARF);
		ids.add(ItemID.WOOLLY_SCARF);
		ids.add(ItemID.BOBBLE_SCARF);
		ids.add(ItemID.GNOME_SCARF);
		ids.add(ItemID.RAINBOW_SCARF);

		ids.add(ItemID.GLOVES_OF_SILENCE);
		ids.add(ItemID.FREMENNIK_GLOVES);
		ids.add(ItemID.WARM_GLOVES);

		ids.add(ItemID.FIREMAKING_CAPE);
		ids.add(ItemID.FIREMAKING_CAPET);
		ids.add(ItemID.MAX_CAPE);
		ids.add(ItemID.FIRE_CAPE);
		ids.add(ItemID.FIRE_MAX_CAPE);
		ids.add(ItemID.INFERNAL_CAPE);
		ids.add(ItemID.INFERNAL_MAX_CAPE);
		ids.add(ItemID.OBSIDIAN_CAPE);
		ids.add(ItemID.OBSIDIAN_CAPE_R);
		ids.add(ItemID.ACCUMULATOR_MAX_CAPE);
		ids.add(ItemID.ARDOUGNE_MAX_CAPE);
		ids.add(ItemID.ASSEMBLER_MAX_CAPE);
		ids.add(ItemID.MYTHICAL_MAX_CAPE);
		ids.add(ItemID.IMBUED_GUTHIX_MAX_CAPE);
		ids.add(ItemID.IMBUED_SARADOMIN_MAX_CAPE);
		ids.add(ItemID.IMBUED_ZAMORAK_MAX_CAPE);
		ids.add(ItemID.GUTHIX_MAX_CAPE);
		ids.add(ItemID.SARADOMIN_MAX_CAPE);
		ids.add(ItemID.ZAMORAK_MAX_CAPE);

		ids.add(ItemID.STAFF_OF_FIRE);
		ids.add(ItemID.FIRE_BATTLESTAFF);
		ids.add(ItemID.LAVA_BATTLESTAFF);
		ids.add(ItemID.STEAM_BATTLESTAFF);
		ids.add(ItemID.SMOKE_BATTLESTAFF);
		ids.add(ItemID.MYSTIC_FIRE_STAFF);
		ids.add(ItemID.MYSTIC_LAVA_STAFF);
		ids.add(ItemID.MYSTIC_STEAM_STAFF);
		ids.add(ItemID.MYSTIC_SMOKE_STAFF);
		ids.add(ItemID.INFERNAL_AXE);
		ids.add(ItemID.INFERNAL_AXE_OR);
		ids.add(ItemID.INFERNAL_PICKAXE);
		ids.add(ItemID.INFERNAL_PICKAXE_OR);
		ids.add(ItemID.INFERNAL_HARPOON);
		ids.add(ItemID.INFERNAL_HARPOON_OR);
		ids.add(ItemID.VOLCANIC_ABYSSAL_WHIP);
		ids.add(ItemID.ALE_OF_THE_GODS);
		ids.add(ItemID.BRUMA_TORCH);
		ids.add(ItemID.DRAGON_CANDLE_DAGGER);

		ids.add(ItemID.TOME_OF_FIRE);
		ids.add(ItemID.LIT_BUG_LANTERN);

		ids.add(ItemID.UGLY_HALLOWEEN_JUMPER_ORANGE);
		ids.add(ItemID.UGLY_HALLOWEEN_JUMPER_BLACK);
		ids.add(ItemID.CHRISTMAS_JUMPER);
		ids.add(ItemID.OLDSCHOOL_JUMPER);

		ids.add(ItemID.RING_OF_THE_ELEMENTS);

		ids.add(ItemID.FESTIVE_ELF_SLIPPERS);
		ids.add(ItemID.MOLE_SLIPPERS);
		ids.add(ItemID.BEAR_FEET);
		ids.add(ItemID.DEMON_FEET);
		ids.add(ItemID.FROG_SLIPPERS);
		ids.add(ItemID.BOB_THE_CAT_SLIPPERS);
		ids.add(ItemID.JAD_SLIPPERS);
	}

	public static int getCount()
	{
		return (int) ids.stream()
			.filter(Equipment::contains)
			.count();
	}
}
