package com.yfletch.summergarden;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class SummerGardenOverlay extends Overlay
{

	private final Client client;
	private final ElementalCollisionDetector collisionDetector;

	private static final WorldPoint LAUNCH_POINT_REGULAR_START = new WorldPoint(2907, 5485, 0);
	private static final WorldPoint LAUNCH_POINT_GATE_START = new WorldPoint(2907, 5484, 0);

	@Inject
	public SummerGardenOverlay(Client client, ElementalCollisionDetector collisionDetector)
	{
		this.client = client;
		this.collisionDetector = collisionDetector;
		setPosition(OverlayPosition.DYNAMIC);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		client.getNpcs()
			.stream()
			.filter(ElementalCollisionDetector::isSummerElemental)
			.forEach(npc -> renderNpc(npc, graphics));

		renderTile(graphics, LAUNCH_POINT_GATE_START, Color.decode("#00ADFF"));

		return null;
	}

	private void renderTile(Graphics2D graphics, WorldPoint wp, Color color)
	{
		LocalPoint lp = LocalPoint.fromWorld(client, wp);
		if (lp != null)
		{
			Polygon poly = Perspective.getCanvasTilePoly(client, lp);
			if (poly != null)
			{
				OverlayUtil.renderPolygon(graphics, poly, color);
			}
		}
	}

	private Color selectColor(int npcId, int parity)
	{
		if (collisionDetector.isLaunchCycle() && (npcId == 1801 || npcId == 1803))
		{
			return Color.decode("#00ADFF");
		}
		if (parity == 0)
		{
			return Color.green.darker();
		}
		if (parity == -1)
		{
			return Color.gray;
		}
		return Color.orange;
	}

	private void renderNpc(NPC npc, Graphics2D graphics)
	{
		// determine parity and color
		int npcId = npc.getId();
		int parity = collisionDetector.getParity(npcId);
		Color highlightColor = selectColor(npcId, parity);

		// draw tile under
		renderTile(graphics, npc.getWorldLocation(), highlightColor);

		// draw text
		if (parity != 0)
		{
			String text = parity != -1 ? String.valueOf(parity) : "?";
			Point p2 = Perspective.getCanvasTextLocation(client, graphics, npc.getLocalLocation(), text, 1);
			if (p2 != null)
			{
				OverlayUtil.renderTextLocation(graphics, p2, text, highlightColor);
			}
		}
	}

}
