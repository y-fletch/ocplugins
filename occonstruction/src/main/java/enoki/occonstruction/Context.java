package enoki.occonstruction;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionContext;
import com.yfletch.occore.util.NpcHelper;
import com.yfletch.occore.util.ObjectHelper;
import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.ItemManager;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/*
 * Notes:
 * Phials ID = 1614
 * Portal Game Object = 15478
 * Larder Space = 15403
 * Oak Larder = 13566
 */

@Singleton
public class Context extends ActionContext
{
	@Inject private Client client;
	@Inject private ItemManager itemManager;
	@Inject private ObjectHelper objectHelper;
	@Inject private NpcHelper npcHelper;
	@Inject private Config config;

	private static final int MIN_COINS_NEEDED = 1250;

	/**
	 * World region of the player home
	 */
	private static final int REGION_PLAYER_HOME = 7513;


	/**
	 * 1 = ON
	 * 0 = OFF (default)
	 */
	private static final int VARBIT_BUILD_MODE = 2176;

	/**
	 * ID of the "Furniture Creation Menu"
	 */
	private static final int WIDGET_CREATION_MENU = 30015489;

	private static final int WIDGET_GROUP_ID = 458;

	/**
	 * Check if the player is animating
	 */

	public boolean isAnimating()
	{
		if (flag("animating"))
		{
			return true;
		}

		if (getPlayer().isAnimating())
		{
			flag("animating", true, 5);
			return true;
		}

		return false;
	}

	public Widget getMakeButton()
	{
		return client.getWidget(WIDGET_GROUP_ID, 3 + 2);
	}

	public boolean isDialogOpen()
	{
		return client.getWidget(WidgetInfo.DIALOG_PLAYER_TEXT) != null
				|| client.getWidget(WidgetInfo.DIALOG_NPC_TEXT) != null
				|| client.getWidget(WidgetInfo.DIALOG_OPTION_OPTIONS) != null;
	}

	public boolean isHouseOptionsOpen()
	{
		final Widget widget = client.getWidget(10551369);
		return widget != null && widget.isVisible();
	}

	/**
	 * Find the nearest buildable TileObject that contains a certain string
	 */
	public TileObject getNearestBuildable()
	{
		return objectHelper.getNearest(obj -> {
			final String[] actions = obj.getActions();
			return actions != null
					&& ArrayUtils.contains(actions, "Build");
		});
	}

	/**
	 * Find the nearest removable TileObject that contains a certain string
	 */
	public TileObject getNearestRemovable()
	{
		return objectHelper.getNearest(obj -> {
			final String[] actions = obj.getActions();
			return actions != null
					&& ArrayUtils.contains(actions, "Remove");
		});
	}

	/**
	 * Returns `true` if next to the nearest removable TileObject
	 */
	public boolean isNextToRemovable()
	{
		return objectHelper.isBeside(getPlayerLocation(), getNearestRemovable());
	}

	/**
	 * Phials can be used to de-note items. For our purposes: planks
	 */
	public NPC getPhials()
	{
		return npcHelper.getNearest(NpcID.PHIALS);
	}

	/**
	 * Furniture creation widget shares the same Widget ID, acts as a global check to see if we are able to see
	 * construction options in the client.
	 */
	public boolean isCreationWidgetOpen()
	{
		Widget widget = client.getWidget(WIDGET_CREATION_MENU);
		if (widget == null || widget.getChildren() == null) return false;
		for (Widget child : widget.getChildren())
		{
			if (child.getText().equals("Furniture Creation Menu")) return true;
		}
		return false;
	}

	/**
	 * Checks if the player wants to use Servants to retrieve items from the bank
	 */
	public boolean useServant()
	{
		return config.enableServant();
	}

	/**
	 * Returns the NPC object for the Demon Butler. `null` if non-existent
	 */
	public NPC getServant()
	{
		return npcHelper.getNearest(NpcID.DEMON_BUTLER);
	}

	/**
	 * Returns `true` if in player home. Regardless of whose home it is
	 */
	public boolean inHome()
	{
		return ArrayUtils.contains(client.getMapRegions(), REGION_PLAYER_HOME);
	}

	/**
	 * Returns `true` if in Player home and in `Build Mode`
	 */
	public boolean inBuildMode()
	{
		int buildMode = client.getVarbitValue(VARBIT_BUILD_MODE);
		return inHome() && buildMode == 1;
	}

	/**
	 * Checks if Player has any planks
	 */
	public boolean hasPlanks()
	{
		return hasItem(config.plankType().getItemId());
	}

	/**
	 * Returns how many planks the player has
	 */
	public int getPlankCount()
	{
		return getItemCount(config.plankType().getItemId());
	}

	/**
	 * Noted planks can be used on Phials to grab more planks
	 */
	public boolean hasNotedPlanks()
	{
		return hasItem(config.plankType().getNotedId());
	}

	/**
	 * A hammer is needed for construction
	 * @return True if player has `Hammer` in their inventory
	 */
	public boolean hasHammer()
	{
		return hasItem(ItemID.HAMMER);
	}

	/**
	 * A saw is needed for construction
	 * @return True if LocalPlayer has `Saw` in their inventory
	 */
	public boolean hasSaw()
	{
		return hasItem(ItemID.SAW);
	}

	/**
	 * Coin is needed to pay the Butler, or pay Phials.
	 * @return True if LocalPlayer has at least 1250 coins
	 */
	public boolean hasCoins()
	{
		return getItemCount(ItemID.COINS) >= MIN_COINS_NEEDED;
	}
}
