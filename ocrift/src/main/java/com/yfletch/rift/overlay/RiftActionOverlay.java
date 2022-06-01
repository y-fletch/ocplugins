package com.yfletch.rift.overlay;

import com.yfletch.rift.lib.ActionRunner;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Singleton;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class RiftActionOverlay extends OverlayPanel
{
	public static final String DEBUG_SET_N60 = "Set time to -1:00";
	public static final String DEBUG_SET_0 = "Set time to 0:00";
	public static final String DEBUG_SET_120 = "Set time to 2:00";
	public static final String DEBUG_CLEAR_POUCHES = "Clear pouch state";
	public static final String DEBUG_CLEAR_FLAGS = "Clear flags";
	public static final String DEBUG_SKIP_REPAIR = "Skip repair";

	private static final String PANEL_NAME = "Rift debug";

	private final ActionRunner<?> runner;

	public RiftActionOverlay(ActionRunner<?> runner)
	{
		this.runner = runner;

		getMenuEntries().add(
			new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, DEBUG_SET_N60, PANEL_NAME)
		);
		getMenuEntries().add(
			new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, DEBUG_SET_0, PANEL_NAME)
		);
		getMenuEntries().add(
			new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, DEBUG_SET_120, PANEL_NAME)
		);
		getMenuEntries().add(
			new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, DEBUG_CLEAR_POUCHES, PANEL_NAME)
		);
		getMenuEntries().add(
			new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, DEBUG_SKIP_REPAIR, PANEL_NAME)
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
