package com.yfletch.occore.overlay;

import com.yfletch.occore.ActionContext;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

public class DebugOverlay extends OverlayPanel
{
	private final String name;
	private final ActionContext context;

	public DebugOverlay(String name, ActionContext context)
	{
		this.name = name;
		this.context = context;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left(name + " Debug").leftColor(Color.LIGHT_GRAY)
				.build()
		);

		Map<String, String> debugs = new HashMap<>();

		context.getDebugFlags().forEach((key, value) -> debugs.put(key, "" + value));

		debugs.forEach(
			(name, value) ->
				panelComponent.getChildren().add(LineComponent.builder().left(name).right(value).build())
		);

		return super.render(graphics);
	}
}
