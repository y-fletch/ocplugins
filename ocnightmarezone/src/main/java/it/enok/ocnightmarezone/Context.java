package it.enok.ocnightmarezone;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionContext;
import com.yfletch.occore.util.NpcHelper;
import com.yfletch.occore.util.ObjectHelper;
import it.enok.ocnightmarezone.config.ItemOption;
import it.enok.ocnightmarezone.config.PotionOption;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.NpcID;
import net.runelite.api.Varbits;
import net.runelite.api.Skill;

@Singleton
public class Context extends ActionContext
{
	@Inject private Client client;
	@Inject private Config config;
	@Inject private ObjectHelper objectHelper;
	@Inject private NpcHelper npcHelper;

	@Getter
	private static final int DOMINIC_NPC_ID = NpcID.DOMINIC_ONION;

	@Getter
	private static final int VIAL_OBJECT_ID = 26291;

	/**
	 * Returns `true` if using damageItem
	 */
	public boolean usingDamageItem()
	{
		return config.itemOption() == ItemOption.NONE;
	}

	/**
	 * Returns `true` if using absorption potions.
	 * default: `true`
	 */
	public boolean useAbsorptionPotion()
	{
		return config.useAbsorptionPotion();
	}

	/**
	 * Returns `true` for Rock Cake
	 */
	public boolean usingRockCake()
	{
		return config.itemOption() == ItemOption.ROCK_CAKE;
	}

	/**
	 * Returns `true` for Locator Orb
	 */
	public boolean usingLocatorOrb()
	{
		return config.itemOption() == ItemOption.LOCATOR_ORB;
	}

	/**
	 * Returns `true` for no damage item
	 */
	public int getDamageItemId()
	{
		return config.itemOption().getItemId();
	}

	/**
	 * Returns an array of ItemIds associated with the users chosen potion
	 */
	public int[] getPotionOptionIds()
	{
		return config.potionOption().getItemIds();
	}

	public boolean usingPotionOption()
	{
		return config.potionOption() != PotionOption.NONE;
	}

	public String getPotionOptionLabel()
	{
		return config.potionOption().getLabel();
	}

	/**
	 * Returns value of local player health
	 */
	public int getCurrentPlayerHealth()
	{
		return client.getBoostedSkillLevel(Skill.HITPOINTS);
	}

	/**
	 * Returns value of local player absorption (always 0 if outside NMZ)
	 */
	public int getCurrentAbsorption()
	{
		return client.getVarbitValue(Varbits.NMZ_ABSORPTION);
	}

	/**
	 * Wrapper around `Client.isInInstancedRegion()`
	 */
	public boolean inInstancedRegion()
	{
		return client.isInInstancedRegion();
	}

	/**
	 * Returns `true` if absorption is below threshold and has finished over-potting
	 */
	public boolean drinkAbsorptionPotion()
	{
		return getCurrentAbsorption() < config.absorptionThreshold() && !flag("drinkAbsorption");
	}

	/**
	 * Only drinks if all the stats are below the threshold (no potions)
	 */
	public boolean drinkCombatPotion()
	{
		if(flag("combatPotion"))
		{
			return false;
		}
		final int attack = client.getBoostedSkillLevel(Skill.ATTACK) - client.getRealSkillLevel(Skill.ATTACK);
		final int strength = client.getBoostedSkillLevel(Skill.STRENGTH) - client.getRealSkillLevel(Skill.STRENGTH);
		final int defence = client.getBoostedSkillLevel(Skill.DEFENCE) - client.getRealSkillLevel(Skill.DEFENCE);
		final int ranged = client.getBoostedSkillLevel(Skill.RANGED) - client.getRealSkillLevel(Skill.RANGED);
		final int magic = client.getBoostedSkillLevel(Skill.MAGIC) - client.getRealSkillLevel(Skill.MAGIC);
		return config.combatThreshold() > attack
				&& config.combatThreshold() > strength
				&& config.combatThreshold() > defence
				&& config.combatThreshold() > ranged
				&& config.combatThreshold() > magic;
	}
}
