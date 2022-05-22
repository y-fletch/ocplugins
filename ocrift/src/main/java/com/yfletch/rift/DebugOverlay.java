package com.yfletch.rift;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class DebugOverlay extends OverlayPanel
{
	@Inject
	private RiftContext context;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Map<String, String> debugs = new HashMap<>();
		debugs.put("Game time", "" + new DecimalFormat("0.00").format(context.getGameTime()));
		debugs.put("Elemental", context.getElementalGuardian() != null ? context.getElementalGuardian().getName() : "null");
		debugs.put("Catalytic", context.getCatalyticGuardian() != null ? context.getCatalyticGuardian().getName() : "null");
		debugs.put("Special", "" + context.getSpecialEnergy());

		debugs.forEach((name, value) ->
			panelComponent.getChildren().add(LineComponent.builder().left(name).right(value).build())
		);

		return super.render(graphics);
	}
}
