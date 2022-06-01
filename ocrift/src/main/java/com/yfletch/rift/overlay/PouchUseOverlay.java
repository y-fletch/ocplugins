package com.yfletch.rift.overlay;

import com.yfletch.rift.enums.Pouch;
import com.yfletch.rift.RiftConfig;
import com.yfletch.rift.RiftContext;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;

public class PouchUseOverlay extends WidgetItemOverlay
{
	@Inject
	private RiftContext context;

	@Inject
	private RiftConfig config;

	public PouchUseOverlay()
	{
		showOnInventory();
	}

	private Color getColor(int timesUsed, int max)
	{
		if (timesUsed < 0)
		{
			return Color.RED;
		}

		int left = max - timesUsed;
		if (left <= 5)
		{
			return Color.RED;
		}

		if (left <= max / 2)
		{
			return Color.YELLOW;
		}

		return Color.GREEN;
	}

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
	{
		if (!config.showPouchUses())
		{
			return;
		}

		Pouch pouch = Pouch.getById(itemId);
		if (pouch == null || pouch.getUses() < 0)
		{
			return;
		}

		int timesUsed = context.getTimesUsed(pouch);
		Color color = getColor(timesUsed, pouch.getUses());

		Rectangle bounds = widgetItem.getCanvasBounds();
		TextComponent textComponent = new TextComponent();

		textComponent.setColor(color);
		textComponent.setText((timesUsed < 0 ? "?" : timesUsed) + "/" + pouch.getUses());
		textComponent.setPosition(new Point(bounds.x - 1, bounds.y + bounds.height));

		textComponent.render(graphics);
	}
}
