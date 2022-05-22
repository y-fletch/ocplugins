package com.yfletch.rift;

import com.yfletch.rift.lib.ActionContext;
import com.yfletch.rift.lib.ObjectManager;
import com.yfletch.rift.util.ObjectSize;
import java.util.Arrays;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.VarPlayer;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;

@Singleton
public class RiftContext extends ActionContext
{
	private static final int ELEMENTAL_RUNE_WIDGET_ID = 48889879;
	private static final int CATALYTIC_RUNE_WIDGET_ID = 48889876;

	@Inject
	private Client client;

	@Inject
	private ObjectManager objectManager;

	@Getter
	@Setter
	private double gameTime = -60;

	public WorldPoint getCurrentLocation()
	{
		return client.getLocalPlayer().getWorldLocation();
	}

	public WorldPoint getDestinationLocation()
	{
		LocalPoint location = client.getLocalDestinationLocation();
		return location != null ? WorldPoint.fromLocal(client, client.getLocalDestinationLocation()) : null;
	}

	private Guardian getGuardian(int widgetId)
	{
		Widget widget = client.getWidget(widgetId);
		if (widget == null)
		{
			return null;
		}

		int spriteId = widget.getSpriteId();

		return Arrays.stream(Guardian.values())
			.filter(g -> g.getSpriteId() == spriteId)
			.findFirst()
			.orElse(null);
	}

	public Guardian getElementalGuardian()
	{
		return getGuardian(ELEMENTAL_RUNE_WIDGET_ID);
	}

	public Guardian getCatalyticGuardian()
	{
		return getGuardian(CATALYTIC_RUNE_WIDGET_ID);
	}

	public int getItemCount(int itemId)
	{
		ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);
		return container != null ? container.count(itemId) : 0;
	}

	public boolean isAt(Point location)
	{
		WorldPoint currentLocation = getCurrentLocation();
		return currentLocation.getX() == location.getX()
			&& currentLocation.getY() == location.getY();
	}

	public boolean isNextTo(int objectId)
	{
		TileObject object = objectManager.get(objectId);
		WorldPoint location = getCurrentLocation();
		return object != null
			&& location != null
			&& ObjectSize.isBeside(location, object);
	}

	public boolean isPathingTo(int objectId)
	{
		TileObject object = objectManager.get(objectId);
		WorldPoint dest = getDestinationLocation();
		return object != null
			&& dest != null
			&& ObjectSize.isBeside(dest, object)
			|| isNextTo(objectId);
	}

	public boolean isInLargeMine()
	{
		return WorldPoint.isInZone(
			new WorldPoint(3637, 9500, 0),
			new WorldPoint(3642, 9507, 0),
			getCurrentLocation()
		);
	}

	public int getSpecialEnergy()
	{
		return client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT.getId()) / 10;
	}

	public boolean isMining()
	{
		return client.getLocalPlayer().getAnimation() == 7139;
	}

	public boolean isCraftingEssence()
	{
		return client.getLocalPlayer().getAnimation() == 9365;
	}
}
