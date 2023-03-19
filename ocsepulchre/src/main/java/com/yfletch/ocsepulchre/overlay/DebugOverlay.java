package com.yfletch.ocsepulchre.overlay;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.ocsepulchre.OCSepulchreConfig;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class DebugOverlay extends OverlayPanel
{
	@Inject
	private OCSepulchreContext context;

	@Inject
	private OCSepulchreConfig config;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showDebugOverlay())
		{
			return null;
		}

		Map<String, String> debugs = new HashMap<>();

		context.getDebugFlags().forEach((key, value) -> debugs.put(key, "" + value));

		debugs.put("Floor", "" + (context.getFloor() == 0 ? "Lobby" : context.getFloor()));
		debugs.put("Path", "" + context.getFloorPath());
		debugs.put("RPOS", "" + context.getPlayerRegionLocation());

		debugs.forEach(
			(name, value) ->
				panelComponent.getChildren().add(
					LineComponent.builder()
						.left(name)
						.right(value)
						.build()
				)
		);

		return super.render(graphics);
	}
}
