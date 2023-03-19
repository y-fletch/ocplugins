package com.yfletch.ocsepulchre.overlay;

import com.google.inject.Singleton;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.action.Action;
import com.yfletch.occore.action.MoveAction;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.unethicalite.client.Static;

@Singleton
public class TileOverlay extends Overlay
{
	private final ActionRunner<?> runner;

	public TileOverlay(ActionRunner<?> runner)
	{
		this.runner = runner;
		setPosition(OverlayPosition.DYNAMIC);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Client client = Static.getClient();
		Color color = Color.CYAN;

		Action<?> current = runner.getCurrent();
		if (current instanceof MoveAction)
		{
			LocalPoint point = ((MoveAction<?>) current).getTarget().toLocal();
			if (point == null) return null;

			Polygon polygon = Perspective.getCanvasTilePoly(client, point);

			graphics.setColor(color);
			graphics.draw(polygon);
			graphics.setColor(new Color(color.getRed(), color.getBlue(), color.getGreen(), 20));
			graphics.fill(polygon);
		}

		return null;
	}
}
