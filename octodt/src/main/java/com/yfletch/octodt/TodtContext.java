package com.yfletch.octodt;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import com.yfletch.octodt.config.Brazier;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Locatable;
import net.runelite.api.NpcID;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.coords.Direction;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Widgets;

@Slf4j
@Singleton
public class TodtContext extends CoreContext
{
	@Inject private Client client;
	@Inject private TodtConfig config;

	public final static WorldPoint EAST_SAFESPOT = new WorldPoint(1638, 3988, 0);
	public final static WorldPoint WEST_SAFESPOT = new WorldPoint(1622, 3988, 0);

	private final static Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");
	private final static WorldPoint EAST_BRAZIER_LOCATION = new WorldPoint(1638, 3997, 0);
	private final static WorldPoint WEST_BRAZIER_LOCATION = new WorldPoint(1620, 3997, 0);
	private final static WorldPoint EAST_PYRO_LOCATION = new WorldPoint(1641, 3996, 0);
	private final static WorldPoint WEST_PYRO_LOCATION = new WorldPoint(1619, 3996, 0);

	private final static int SNOW_FALL = 26690;
	private final static int SNOW_PILE = 29325;
	private final static int BREAK_DANGER_Y = 3996;

	@Getter
	private Direction side;

	@Override
	public void tick(boolean isGameTick)
	{
		// TODO: only switch sides if trying to get brazier?
		if (isGameTick && isInWintertodt()
			&& (side == null || isEastPyromancerDown() || isWestPyromancerDown()))
		{
			chooseSide();
			log.info("Side chosen: " + side.name());
		}

		super.tick(isGameTick);
	}

	public void chooseSide()
	{
		final var preferred = config.preferredBrazier();

		if (preferred == Brazier.EAST && !isEastPyromancerDown() || isWestPyromancerDown())
		{
			side = Direction.EAST;
			return;
		}

		if (preferred == Brazier.WEST && !isWestPyromancerDown() || isEastPyromancerDown())
		{
			side = Direction.WEST;
			return;
		}

		if (Rand.nextBool())
		{
			side = Direction.EAST;
		}
		else
		{
			side = Direction.WEST;
		}
	}

	private WorldPoint getLocation()
	{
		return client.getLocalPlayer().getWorldLocation();
	}

	public boolean isInWintertodt()
	{
		return getLocation().getY() >= 3968;
	}

	public boolean isInSafeArea()
	{
		return getLocation().getY() <= 3986;
	}

	public int getWintertodtEnergy()
	{
		final var widget = Widgets.get(396, 21);
		if (widget != null)
		{
			final var matcher = NUMBER_PATTERN.matcher(widget.getText());
			if (matcher.find())
			{
				return Integer.parseInt(matcher.group(0));
			}
		}

		return -1;
	}

	public boolean isRoundComplete()
	{
		return !isInWintertodt() || getWintertodtEnergy() <= 0;
	}

	public boolean hasEnoughFood()
	{
		final var count = Inventory.getCount(config.food().getIds());
		// switch target when bank is open, in order to withdraw max
		return count >= (Bank.isOpen() ? config.maxFood() : config.minFood());
	}

	public int getFoodRequired()
	{
		final var count = Inventory.getCount(config.food().getIds());
		return config.maxFood() - count;
	}

	public boolean shouldEat()
	{
		return client.getBoostedSkillLevel(Skill.HITPOINTS) < config.minHealth();
	}

	public boolean isEastPyromancerDown()
	{
		return NPCs.getAll(NpcID.INCAPACITATED_PYROMANCER).stream()
			.anyMatch(n -> n.getWorldLocation().equals(EAST_PYRO_LOCATION));
	}

	public boolean isWestPyromancerDown()
	{
		return NPCs.getAll(NpcID.INCAPACITATED_PYROMANCER).stream()
			.anyMatch(n -> n.getWorldLocation().equals(WEST_PYRO_LOCATION));
	}

	public List<TileObject> getSnowAttacks()
	{
		return TileObjects.getAll(SNOW_FALL, SNOW_PILE);
	}

	public boolean isOnDangerousTile()
	{
		final var snowAttackTiles = getSnowAttacks().stream()
			.map(Locatable::getWorldLocation)
			.collect(Collectors.toCollection(HashSet::new));

		return snowAttackTiles.contains(getLocation())
			|| getLocation().getY() == BREAK_DANGER_Y
			&& getBrazier() != null && snowAttackTiles.contains(getBrazier().getWorldLocation());
	}

	public boolean isInSafespot()
	{
		return getLocation().equals(WEST_SAFESPOT)
			|| getLocation().equals(EAST_SAFESPOT);
	}

	public TileObject getBrazier()
	{
		return TileObjects.getFirstAt(
			side == Direction.EAST
				? EAST_BRAZIER_LOCATION
				: WEST_BRAZIER_LOCATION,
			"Brazier", "Burning brazier"
		);
	}

	public WorldPoint getSafespot()
	{
		return side == Direction.EAST
			? EAST_SAFESPOT : WEST_SAFESPOT;
	}

	@Override
	public Map<String, String> getDebugMap()
	{
		final var map = super.getDebugMap();
		map.put("energy", "" + getWintertodtEnergy());
		map.put("round-active", "" + !isRoundComplete());
		map.put("in-wintertodt", "" + isInWintertodt());
		map.put("in-safe-area", "" + isInSafeArea());
		map.put("food-required", "" + getFoodRequired());
		map.put("should-eat", "" + shouldEat());
		map.put("has-enough-food", "" + hasEnoughFood());
		if (side != null)
		{
			map.put("brazier", "" + getBrazier().getWorldX() + ", " + getBrazier().getWorldY());
			map.put("side", side.name().toLowerCase());
		}
		map.put("snow-attacks", "" + getSnowAttacks().size());

		return map;
	}
}
