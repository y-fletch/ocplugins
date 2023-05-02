package com.yfletch.ocgranite.overlay;

import com.google.inject.Inject;
import com.yfletch.ocgranite.OCGraniteContext;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

public class DebugOverlay extends OverlayPanel
{
	@Inject
	private OCGraniteContext context;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("tick")
				.right("" + context.getTick())
				.build());

		Map<String, String> debugs = new HashMap<>();

		context.getDebugFlags().forEach((key, value) -> debugs.put(key, "" + value));
		debugs.put("Success", "" + context.receivedGranite());

		debugs.forEach((name, value) ->
			panelComponent.getChildren().add(LineComponent.builder().left(name).right(value).build())
		);

		return super.render(graphics);
	}
}
