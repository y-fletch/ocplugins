package com.yfletch.rift;

import com.yfletch.rift.lib.ObjectManager;
import com.yfletch.rift.util.ObjectSize;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.ObjectID;
import net.runelite.api.Perspective;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;

public class DestinationOverlay extends OverlayPanel
{
	@Inject
	private Client client;

	@Inject
	private RiftContext context;

	@Inject
	private ObjectManager objectManager;

	DestinationOverlay()
	{
		setPosition(OverlayPosition.DYNAMIC);
	}

	private void drawTile(Graphics2D graphics, WorldPoint point, Color color)
	{
		LocalPoint localPoint = LocalPoint.fromWorld(client, point);
		if (localPoint == null)
		{
			return;
		}

		Polygon polygon = Perspective.getCanvasTilePoly(client, localPoint);

		graphics.setColor(color);
		graphics.draw(polygon);
		graphics.setColor(new Color(color.getRed(), color.getBlue(), color.getGreen(), 20));
		graphics.fill(polygon);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		TileObject obj = objectManager.get(ObjectID.UNCHARGED_CELLS_43732);
		if (obj == null)
		{
			return null;
		}

		for (int i = 3602; i < 3629; i++)
		{
			for (int j = 9482; j < 9500; j++)
			{
				WorldPoint wp = new WorldPoint(i, j, 0);
				if (ObjectSize.isBeside(wp, obj))
				{
					drawTile(graphics, wp, Color.GREEN);
				}
			}
		}

		drawTile(graphics, obj.getWorldLocation(), Color.CYAN);

		return null;
	}
}
