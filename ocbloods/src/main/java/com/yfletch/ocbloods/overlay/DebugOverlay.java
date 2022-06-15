package com.yfletch.ocbloods.overlay;

import com.yfletch.ocbloods.OCBloodsConfig;
import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.ActionRunner;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Setter;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class DebugOverlay extends OverlayPanel
{
	@Inject
	private OCBloodsContext context;

	@Inject
	private OCBloodsConfig config;

	@Setter
	private ActionRunner<OCBloodsContext> runner;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showDebugOverlay())
		{
			return null;
		}

		Map<String, String> debugs = new HashMap<>();
		if (runner != null && runner.getCurrent() != null)
		{
			debugs.put("action", runner.getCurrent().getClass().getSimpleName());

			String status = "none";
			if (runner.getCurrent().isWorking(context))
			{
				status = "working";
			}
			else if (runner.getCurrent().isReady(context))
			{
				status = "ready";
			}
			else if (runner.getCurrent().isDone(context))
			{
				status = "done";
			}

			debugs.put("status", status);
		}

		debugs.put("pouch", "" + context.getPouchEssence());

		context.getDebugFlags().forEach((key, value) -> debugs.put(key, "" + value));

		debugs.forEach((name, value) ->
			panelComponent.getChildren().add(LineComponent.builder().left(name).right(value).build())
		);

		return super.render(graphics);
	}
}
