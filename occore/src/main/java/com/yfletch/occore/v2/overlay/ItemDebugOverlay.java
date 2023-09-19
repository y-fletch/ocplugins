package com.yfletch.occore.v2.overlay;

import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import lombok.Setter;
import net.runelite.api.Item;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.ImageComponent;

@Setter
public abstract class ItemDebugOverlay extends WidgetItemOverlay
{
	private Item item;
	private WidgetItem currentWidgetItem;

	@Inject private ItemManager itemManager;

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
	{
		if (item == null || itemId != item.getId() || item.getSlot() != widgetItem.getWidget().getIndex()) return;

		final var bounds = widgetItem.getCanvasBounds();
		final var outline = itemManager.getItemOutline(itemId, widgetItem.getQuantity(), Color.ORANGE);
		final var image = new ImageComponent(outline);
		image.setPreferredLocation(new Point(bounds.x, bounds.y));
		image.render(graphics);
	}
}
