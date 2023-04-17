package com.yfletch.occlicker;

import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

public class OCClickerOverlay extends OverlayPanel
{
	@Inject
	private OCClickerPlugin plugin;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("OC Clicker").leftColor(Color.CYAN)
				.right(plugin.isEnabled() ? "ON" : "OFF")
				.rightColor(plugin.isEnabled() ? Color.GREEN : Color.YELLOW)
				.build()
		);

		return super.render(graphics);
	}
}
