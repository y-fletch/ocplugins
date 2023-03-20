package com.yfletch.ocsepulchre.overlay;

import com.google.inject.Inject;
import com.yfletch.ocsepulchre.OCSepulchreConfig;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.obstacle.DrawableObstacle;
import com.yfletch.ocsepulchre.util.TileDebugInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.unethicalite.client.Static;

public class ObstacleOverlay extends Overlay
{
	@Inject
	private OCSepulchreContext context;

	@Inject
	private OCSepulchreConfig config;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showDebugOverlay())
		{
			return null;
		}

		for (DrawableObstacle obstacle : context.getObstacles().allDrawable())
		{
			if (obstacle.getTileDebug() == null) continue;

			for (TileDebugInfo debug : obstacle.getTileDebug())
			{
				renderDebugTile(graphics, debug);
			}
		}

		return null;
	}

	private void renderDebugTile(Graphics2D graphics, TileDebugInfo debug)
	{
		Client client = Static.getClient();
		Color color = Color.PINK;

		LocalPoint point = debug.getRegionPoint().toLocal();
		if (point == null) return;

		Polygon polygon = Perspective.getCanvasTilePoly(client, point);
		if (polygon == null) return;

		graphics.setColor(color);
		graphics.draw(polygon);
		graphics.setColor(new Color(color.getRed(), color.getBlue(), color.getGreen(), 20));
		graphics.fill(polygon);

		if (debug.getLine1() != null)
		{
			renderDebugText(graphics, polygon.getBounds(), color, debug.getLine1(), 0);
		}

		if (debug.getLine2() != null)
		{
			renderDebugText(graphics, polygon.getBounds(), color, debug.getLine2(), 1);
		}
	}

	private void renderDebugText(Graphics2D graphics, Rectangle bounds, Color color, String text, int offset)
	{

		TextComponent textComponent = new TextComponent();

		FontMetrics fontMetrics = graphics.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(text);
		int textHeight = fontMetrics.getHeight();

		textComponent.setPosition(new Point(
			bounds.x + bounds.width / 2 - textWidth / 2,
			bounds.y - textHeight * offset
		));

		textComponent.setColor(color);
		textComponent.setText(text);

		textComponent.render(graphics);
	}
}
