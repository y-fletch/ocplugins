package com.yfletch.ocbloods.overlay;

import com.yfletch.ocbloods.OCBloodsConfig;
import com.yfletch.ocbloods.OCBloodsContext;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;

@Singleton
public class PouchOverlay extends WidgetItemOverlay
{
	@Inject
	private OCBloodsContext context;

	@Inject
	private OCBloodsConfig config;

	public PouchOverlay()
	{
		showOnInventory();
	}

	@Override
	public void renderItemOverlay(Graphics2D graphics2D, int itemId, WidgetItem widgetItem)
	{
		if (itemId != ItemID.COLOSSAL_POUCH || !config.showPouchUsesOverlay())
		{
			return;
		}

		Rectangle bounds = widgetItem.getCanvasBounds();
		TextComponent textComponent = new TextComponent();

		textComponent.setFont(FontManager.getRunescapeSmallFont());
		textComponent.setColor(Color.WHITE);
		textComponent.setText((context.getPouchUses() < 0 ? "?" : context.getPouchUses()) + "/" + OCBloodsContext.POUCH_DURABILITY);
		textComponent.setPosition(new Point(bounds.x - 1, bounds.y + bounds.height));

		textComponent.render(graphics2D);
	}
}
