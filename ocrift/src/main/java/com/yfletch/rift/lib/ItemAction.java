package com.yfletch.rift.lib;

import java.awt.Color;
import lombok.AllArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;

@AllArgsConstructor
public class ItemAction<T extends ActionContext> extends Action<T>
{
	protected String action;
	protected String itemName;

	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		return LineComponent.builder()
			.left(action).leftColor(Color.WHITE)
			.right(itemName).rightColor(Color.ORANGE)
			.build();
	}
}
