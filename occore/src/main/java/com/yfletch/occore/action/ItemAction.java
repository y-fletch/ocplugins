package com.yfletch.occore.action;

import com.yfletch.occore.ActionContext;
import java.awt.Color;
import lombok.AllArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;

/**
 * Represents an action on an item in an inventory (not just the player's inventory).
 */
@AllArgsConstructor
public class ItemAction<T extends ActionContext> extends Action<T>
{
	protected String action;
	protected String itemName;

	@Override
	public String getName()
	{
		return action + " " + itemName;
	}

	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		return LineComponent.builder()
			.left(action).leftColor(Color.WHITE)
			.right(itemName).rightColor(Color.ORANGE)
			.build();
	}
}
