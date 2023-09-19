package com.yfletch.occore.v2.overlay;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.GroundObject;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.unethicalite.client.Static;

@Setter
@Singleton
public class WorldDebugOverlay extends Overlay
{
	private static final int OUTLINE_WIDTH = 2;

	@Inject private Client client;
	@Inject private ModelOutlineRenderer modelOutlineRenderer;

	private WorldPoint tile;
	private List<WorldPoint> path;
	private NPC npc;
	private TileObject tileObject;
	private GroundObject groundObject;
	private TileItem tileItem;

	public WorldDebugOverlay()
	{
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.LOW);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		renderTile(graphics);
		renderPath(graphics);
		renderNpc(graphics);
		renderTileObject(graphics);
		renderGroundObject(graphics);
		renderTileItem(graphics);

		return null;
	}

	private void renderTile(Graphics2D graphics)
	{
		if (tile == null) return;
		final var local = LocalPoint.fromWorld(Static.getClient(), tile);
		if (local == null) return;
		final var polygon = Perspective.getCanvasTilePoly(Static.getClient(), local);
		if (polygon == null) return;

		graphics.setColor(Color.WHITE);
		graphics.draw(polygon);
	}

	private void renderPath(Graphics2D graphics)
	{
		if (path == null) return;

		graphics.setColor(Color.GRAY);
		Point prev = null;
		for (final var point : path)
		{
			final var centre = getTileCentre(point);
			if (centre == null) continue;

			if (prev != null)
			{
				graphics.drawLine(prev.x, prev.y, centre.x, centre.y);
			}

			prev = centre;
		}
	}

	private void renderNpc(Graphics2D graphics)
	{
		if (npc == null) return;

		modelOutlineRenderer.drawOutline(npc, OUTLINE_WIDTH, Color.YELLOW, 0);
	}

	private void renderTileObject(Graphics2D graphics)
	{
		if (tileObject == null) return;

		modelOutlineRenderer.drawOutline(tileObject, OUTLINE_WIDTH, Color.CYAN, 0);
	}

	private void renderGroundObject(Graphics2D graphics)
	{
		if (groundObject == null) return;

		modelOutlineRenderer.drawOutline(groundObject, OUTLINE_WIDTH, Color.CYAN, 0);
	}

	private void renderTileItem(Graphics2D graphics)
	{
		if (tileItem == null) return;

		modelOutlineRenderer.drawOutline(tileItem.getTile().getItemLayer(), OUTLINE_WIDTH, Color.ORANGE, 0);
	}

	private Point getTileCentre(WorldPoint point)
	{
		LocalPoint local = LocalPoint.fromWorld(client, point);
		if (local == null) return null;

		Polygon poly = Perspective.getCanvasTilePoly(client, local);
		if (poly == null) return null;

		int cx = poly.getBounds().x + poly.getBounds().width / 2;
		int cy = poly.getBounds().y + poly.getBounds().height / 2;
		return new Point(cx, cy);
	}
}
