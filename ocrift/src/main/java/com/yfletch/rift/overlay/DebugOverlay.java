package com.yfletch.rift.overlay;

import com.yfletch.rift.RiftConfig;
import com.yfletch.rift.RiftContext;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.ObjectID;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class DebugOverlay extends OverlayPanel
{
	@Inject
	private RiftContext context;

	@Inject
	private RiftConfig config;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showDebugOverlay())
		{
			return null;
		}

		Map<String, String> debugs = new HashMap<>();
		debugs.put("Game time", "" + new DecimalFormat("0.00").format(context.getGameTime()));
		debugs.put("Pouch capacity", "" + context.getPouchCapacity());
		context.getPouchEssence().forEach((pouch, qty) -> debugs.put(pouch.getItemName(), "" + qty));
		context.getFlags().forEach((flag, value) -> debugs.put(flag, "" + value));

		debugs.forEach((name, value) ->
			panelComponent.getChildren().add(LineComponent.builder().left(name).right(value).build())
		);

		return super.render(graphics);
	}
}
