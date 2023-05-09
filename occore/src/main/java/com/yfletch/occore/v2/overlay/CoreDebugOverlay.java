package com.yfletch.occore.v2.overlay;

import com.yfletch.occore.v2.CoreContext;
import com.yfletch.occore.v2.RunnerPlugin;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

public class CoreDebugOverlay extends OverlayPanel
{
	private final RunnerPlugin<?> plugin;
	private final CoreContext context;

	public CoreDebugOverlay(RunnerPlugin<?> plugin, CoreContext context)
	{
		this.plugin = plugin;
		this.context = context;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left(plugin.getName() + " Debug")
				.leftColor(Color.LIGHT_GRAY)
				.build()
		);

		context.getDebugMap().forEach(
			(name, value) ->
				panelComponent.getChildren().add(LineComponent.builder().left(name).right(value).build())
		);

		return super.render(graphics);
	}
}
