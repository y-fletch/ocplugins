package com.yfletch.summergarden;

import com.yfletch.summergarden.actions.Idle;
import com.yfletch.summergarden.actions.Unknown;
import com.yfletch.summergarden.actions.garden.Running;
import com.yfletch.summergarden.actions.garden.stamina.DrinkStamina1;
import com.yfletch.summergarden.actions.garden.stamina.DrinkStamina2;
import com.yfletch.summergarden.actions.garden.stamina.DrinkStamina3;
import com.yfletch.summergarden.actions.garden.stamina.DrinkStamina4;
import com.yfletch.summergarden.actions.restock.bank.DepositBeerGlass;
import com.yfletch.summergarden.actions.restock.bank.DepositSqirkJuice;
import com.yfletch.summergarden.actions.restock.bank.OpenApprenticeDoor;
import com.yfletch.summergarden.actions.restock.bank.OpenBankChest;
import com.yfletch.summergarden.actions.restock.bank.OpenPalaceDoor;
import com.yfletch.summergarden.actions.restock.bank.TeleportApprentice;
import com.yfletch.summergarden.actions.restock.bank.UseShortcut;
import com.yfletch.summergarden.actions.restock.bank.WithdrawBeerGlass;
import com.yfletch.summergarden.actions.restock.bank.WithdrawStamina4;
import com.yfletch.summergarden.actions.restock.bank.stamina.DepositStamina1;
import com.yfletch.summergarden.actions.restock.bank.stamina.DepositStamina2;
import com.yfletch.summergarden.actions.restock.bank.stamina.DepositStamina3;
import com.yfletch.summergarden.actions.restock.bank.stamina.DepositVial;
import com.yfletch.summergarden.actions.restock.house.BreakTeletab;
import com.yfletch.summergarden.actions.restock.house.DrinkFromPool;
import com.yfletch.summergarden.actions.restock.house.TeleportMountedGlory;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.MenuEntryProvider;
import com.yfletch.summergarden.util.action.Action;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.actions.garden.CrushSqirkFruit;
import com.yfletch.summergarden.actions.garden.EnableRun;
import com.yfletch.summergarden.actions.garden.GrabSqirk;
import com.yfletch.summergarden.actions.garden.OpenGate;
import com.yfletch.summergarden.actions.garden.UsePestle;
import com.yfletch.summergarden.actions.garden.WaitForTick;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import net.runelite.api.events.MenuOptionClicked;

@Singleton
public class ActionRunner
{
	// RESTOCK - HOUSE
	@Inject
	private BreakTeletab breakTeletab;
	@Inject
	private DrinkFromPool drinkFromPool;
	@Inject
	private TeleportMountedGlory teleportMountedGlory;

	// RESTOCK - BANK
	@Inject
	private OpenPalaceDoor openPalaceDoor;
	@Inject
	private UseShortcut useShortcut;
	@Inject
	private OpenBankChest openBankBooth;
	@Inject
	private DepositSqirkJuice depositSqirkJuice;
	@Inject
	private DepositVial depositVial;
	@Inject
	private DepositStamina1 depositStamina1;
	@Inject
	private DepositStamina2 depositStamina2;
	@Inject
	private DepositStamina3 depositStamina3;
	@Inject
	private WithdrawStamina4 withdrawStamina4;
	@Inject
	private WithdrawBeerGlass withdrawBeerGlass;
	@Inject
	private DepositBeerGlass depositBeerGlass;
	@Inject
	private OpenApprenticeDoor openApprenticeDoor;
	@Inject
	private TeleportApprentice teleportApprentice;

	// GARDEN
	@Inject
	private EnableRun enableRun;
	@Inject
	private CrushSqirkFruit crushSqirkFruit;
	@Inject
	private UsePestle usePestle;
	@Inject
	private DrinkStamina1 drinkStamina1;
	@Inject
	private DrinkStamina2 drinkStamina2;
	@Inject
	private DrinkStamina3 drinkStamina3;
	@Inject
	private DrinkStamina4 drinkStamina4;
	@Inject
	private OpenGate openGate;
	@Inject
	private Running running;
	@Inject
	private GrabSqirk grabSqirk;
	@Inject
	private WaitForTick waitForTick;

	@Inject
	private Unknown unknown;

	@Inject
	private ActionOverlay overlay;

	@Inject
	private MenuEntryProvider menuEntryProvider;

	@Inject
	private ActionContext context;

	private Action[] actions;

	@Getter
	private Action current;

	private Action[] getActions()
	{
		if (actions == null)
		{
			actions = new Action[]{
				// highest priority
				enableRun,

				// actions that can be done while running
				drinkStamina1,
				drinkStamina2,
				drinkStamina3,
				drinkStamina4,
				crushSqirkFruit,
				usePestle,

				// banking
				depositSqirkJuice,
				depositVial,
				depositStamina1,
				depositStamina2,
				depositStamina3,
				withdrawStamina4,
				withdrawBeerGlass,
				depositBeerGlass,

				// block extra clicks when already running
				running,

				// house actions
				drinkFromPool,
				teleportMountedGlory,

				// post-bank
				openApprenticeDoor,
				teleportApprentice,

				// run to bank
				openPalaceDoor,
				useShortcut,
				openBankBooth,
				breakTeletab,

				// garden loop
				openGate,
				grabSqirk,
				waitForTick,

				new Idle(),

				// catch-all
				unknown
			};
		}
		return actions;
	}

	public void run(MenuOptionClicked event)
	{
		if (current == null)
		{
			return;
		}

		current.run(context, new WrappedEvent(menuEntryProvider, event));
	}

	public void onTick()
	{
		for (Action action : getActions())
		{
			if (action.isReady(context) && !action.isDone(context))
			{
				if (current != null && action != current)
				{
					current.done(context);
				}

				overlay.setLine(action.getDisplayLine(context));
				current = action;
				break;
			}
		}
	}
}
