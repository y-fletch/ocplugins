package com.yfletch.ocsepulchre.overlay;

import com.google.inject.Inject;
import com.yfletch.ocsepulchre.OCSepulchreConfig;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.obstacle.DrawableObstacle;
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

		Client client = Static.getClient();
		Color color = Color.PINK;

		for (DrawableObstacle obstacle : context.getObstacles().allDrawable())
		{
			if (obstacle.getDebugPosition() == null) continue;

			LocalPoint point = LocalPoint.fromWorld(client, obstacle.getDebugPosition());
			if (point == null) return null;

			Polygon polygon = Perspective.getCanvasTilePoly(client, point);
			if (polygon == null) return null;

			graphics.setColor(color);
			graphics.draw(polygon);
			graphics.setColor(new Color(color.getRed(), color.getBlue(), color.getGreen(), 20));
			graphics.fill(polygon);

			TextComponent textComponent = new TextComponent();
			Rectangle bounds = polygon.getBounds();

			FontMetrics fontMetrics = graphics.getFontMetrics();
			int textWidth = fontMetrics.stringWidth(obstacle.getDebugText());
			int textHeight = fontMetrics.getHeight();

			textComponent.setPosition(new Point(
				bounds.x + bounds.width / 2 - textWidth / 2,
				bounds.y - textHeight
			));

			textComponent.setColor(color);
			textComponent.setText(obstacle.getDebugText());

			textComponent.render(graphics);
		}

		return null;
	}
}
