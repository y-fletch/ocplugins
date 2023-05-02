package com.yfletch.ocgranite.action;

import com.yfletch.occore.action.PrepAction;
import com.yfletch.ocgranite.OCGraniteContext;
import java.util.HashMap;
import net.runelite.api.ItemID;

public class Prep extends PrepAction<OCGraniteContext>
{
	private static final int[] ACCEPTED_PICKAXES = new int[]{
		ItemID.RUNE_PICKAXE,
		ItemID.DRAGON_PICKAXE,
		ItemID.DRAGON_PICKAXE_12797,
		ItemID.DRAGON_PICKAXE_OR,
		ItemID.DRAGON_PICKAXE_OR_25376,
		ItemID.GILDED_PICKAXE,
		ItemID.CRYSTAL_PICKAXE,
		ItemID.INFERNAL_PICKAXE,
		ItemID.INFERNAL_PICKAXE_OR,
		ItemID.INFERNAL_PICKAXE_UNCHARGED,
		ItemID.INFERNAL_PICKAXE_UNCHARGED_25369,
		ItemID._3RD_AGE_PICKAXE
	};

	@Override
	protected HashMap<String, Boolean> getConditions(OCGraniteContext ctx)
	{
		HashMap<String, Boolean> map = new HashMap<>();

		map.put("Not in mine", ctx.isInMine());

		map.put(
			"Missing guam leaf, marrentill, harralander or tarromin leaf",
			ctx.hasItem(
				ItemID.GUAM_LEAF, ItemID.HARRALANDER,
				ItemID.MARRENTILL, ItemID.TARROMIN
			)
		);

		map.put("Missing swamp tar", ctx.hasItem(ItemID.SWAMP_TAR));
		map.put("Missing pestle and mortar", ctx.hasItem(ItemID.PESTLE_AND_MORTAR));

		map.put("Missing pickaxe", ctx.hasEquipped(ACCEPTED_PICKAXES));

		if (ctx.useHumidify())
		{
			map.put(
				"Missing humidify runes",
				ctx.hasItem(
					ItemID.RUNE_POUCH,
					ItemID.RUNE_POUCH_23650,
					ItemID.RUNE_POUCH_27086
				)
					|| (ctx.hasItem(ItemID.ASTRAL_RUNE)
					&& ctx.hasItem(ItemID.FIRE_RUNE)
					&& ctx.hasItem(ItemID.WATER_RUNE))
			);
			map.put(
				"Missing waterskins",
				ctx.hasItem(
					ItemID.WATERSKIN0,
					ItemID.WATERSKIN1,
					ItemID.WATERSKIN2,
					ItemID.WATERSKIN3,
					ItemID.WATERSKIN4
				)
			);
		}

		return map;
	}
}
