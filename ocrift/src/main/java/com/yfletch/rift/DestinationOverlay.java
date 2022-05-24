package com.yfletch.rift;

import com.yfletch.rift.lib.ObjectManager;
import com.yfletch.rift.util.ObjectHelper;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TextComponent;

public class DestinationOverlay extends OverlayPanel
{
	@Inject
	private Client client;

	@Inject
	private RiftContext context;

	@Inject
	private ObjectManager objectManager;

	@Inject
	private ObjectHelper objectHelper;

	DestinationOverlay()
	{
		setPosition(OverlayPosition.DYNAMIC);
	}

	private void drawTile(Graphics2D graphics, LocalPoint localPoint, Color color, boolean coords, int objId)
	{
		Polygon polygon = Perspective.getCanvasTilePoly(client, localPoint);
		if (polygon == null)
		{
			return;
		}

		graphics.setColor(color);
		graphics.draw(polygon);
		graphics.setColor(new Color(color.getRed(), color.getBlue(), color.getGreen(), 20));
		graphics.fill(polygon);

		if (!coords)
		{
			return;
		}

		drawText(
			graphics,
			polygon,
			localPoint.getSceneX() + "," + localPoint.getSceneY(),
			Color.CYAN,
			2
		);

		net.runelite.api.Point offset = objectHelper.getCenterOffset(objId);

		drawText(
			graphics,
			polygon,
			(localPoint.getSceneX() - offset.getX()) + "," + (localPoint.getSceneY() - offset.getY()),
			Color.RED,
			1
		);

	}

	private void drawText(Graphics2D g, Polygon polygon, String text, Color color, int height)
	{
		TextComponent textComponent = new TextComponent();
		Rectangle bounds = polygon.getBounds();

		FontMetrics fontMetrics = g.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(text);
		int textHeight = fontMetrics.getHeight();
		textComponent.setPosition(new Point(
			bounds.x + bounds.width / 2 - textWidth / 2,
			bounds.y - textHeight * height
		));
		textComponent.setColor(color);
		textComponent.setText(text);
		textComponent.render(g);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		for (TileObject obj : objectManager.getAll())
		{
			LocalPoint localPoint = LocalPoint.fromWorld(client, obj.getWorldLocation());
			if (localPoint == null)
			{
				continue;
			}

			drawTile(graphics, localPoint, Color.CYAN, true, obj.getId());
		}


		return null;
	}
}
