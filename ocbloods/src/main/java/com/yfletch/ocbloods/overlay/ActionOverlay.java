package com.yfletch.ocbloods.overlay;

import com.yfletch.occore.ActionRunner;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Singleton;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class ActionOverlay extends OverlayPanel
{
	public static final String ACTION_SKIP_REPAIR = "Skip repair";
	private static final String PANEL_NAME = "OC Bloods";

	private final ActionRunner<?> runner;

	public ActionOverlay(ActionRunner<?> runner)
	{
		this.runner = runner;

		getMenuEntries().add(
			new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, ACTION_SKIP_REPAIR, PANEL_NAME)
		);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (runner.getDisplayLine() != null)
		{
			panelComponent.getChildren().add(runner.getDisplayLine());
			if (runner.isWorking())
			{
				panelComponent.getChildren().add(LineComponent.builder().left("Working...").build());
			}
		}
		else
		{
			panelComponent.getChildren().add(LineComponent.builder().left("No action").build());
		}

		return super.render(graphics);
	}
}
