package com.yfletch.occore.v2.interaction;

import com.yfletch.occore.v2.util.TextColor;
import net.runelite.api.GameObject;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.widgets.Dialog;

public class MenuEntryUtil
{
	public static String getTarget(Interactable interactable)
	{
		if (interactable instanceof NPC)
		{
			return TextColor.NPC + ((NPC) interactable).getName() + TextColor.END;
		}

		if (interactable instanceof TileObject)
		{
			return TextColor.OBJECT + ((TileObject) interactable).getName() + TextColor.END;
		}

		if (interactable instanceof Widget)
		{
			if (isDialogOption(interactable))
			{
				return TextColor.GRAY + "(" + ((Widget) interactable).getText() + ")" + TextColor.END;
			}

			return TextColor.WHITE + ((Widget) interactable).getName() + TextColor.END;
		}

		if (interactable instanceof Item)
		{
			return TextColor.ITEM + ((Item) interactable).getName() + TextColor.END;
		}

		// all other types of targets are handled by subclasses
		return TextColor.DANGER + "Unknown target" + TextColor.END;
	}

	public static int getIdentifier(Interactable interactable)
	{
		return getIdentifier(interactable, -1);
	}

	public static int getIdentifier(Interactable interactable, int index)
	{
		if (interactable instanceof NPC)
		{
			return ((NPC) interactable).getIndex();
		}

		if (interactable instanceof TileObject)
		{
			return ((TileObject) interactable).getId();
		}

		if (isDialogOption(interactable))
		{
			return 0;
		}

		// widgets: option ID (1-index)
		return index + 1;
	}

	public static int getParam0(Interactable interactable)
	{
		if (interactable instanceof NPC)
		{
			return 0;
		}

		if (interactable instanceof TileObject)
		{
			return getSceneLocation((TileObject) interactable).getX();
		}

		if (interactable instanceof Widget)
		{
			return ((Widget) interactable).getIndex();
		}

		if (interactable instanceof Item)
		{
			if (((Item) interactable).getType() == Item.Type.EQUIPMENT)
			{
				return -1;
			}

			return ((Item) interactable).getSlot();
		}

		return 0;
	}

	public static int getParam1(Interactable interactable)
	{
		if (interactable instanceof NPC)
		{
			return 0;
		}

		if (interactable instanceof TileObject)
		{
			return getSceneLocation((TileObject) interactable).getY();
		}

		if (interactable instanceof Widget)
		{
			return ((Widget) interactable).getId();
		}

		if (interactable instanceof Item)
		{
			return ((Item) interactable).getWidgetId();
		}

		return 0;
	}

	public static boolean isDialogOption(Interactable interactable)
	{
		return interactable instanceof Widget
			&& (Dialog.getOptions().contains((Widget) interactable)
			|| ((Widget) interactable).getText().equals("Click here to continue"));
	}

	private static Point getSceneLocation(TileObject object)
	{
		if (object instanceof GameObject)
		{
			return ((GameObject) object).getSceneMinLocation();
		}

		final var local = object.getLocalLocation();
		return new Point(local.getSceneX(), local.getSceneY());
	}
}
