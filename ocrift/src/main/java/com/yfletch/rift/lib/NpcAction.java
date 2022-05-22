package com.yfletch.rift.lib;

import java.awt.Color;
import lombok.AllArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;

@AllArgsConstructor
public class NpcAction<T extends ActionContext> extends Action<T>
{
	protected String action;
	protected String npcName;

	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		return LineComponent.builder()
			.left(action).leftColor(Color.WHITE)
			.right(npcName).rightColor(Color.YELLOW)
			.build();
	}
}
