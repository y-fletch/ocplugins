package com.yfletch.ocbloods;

import com.yfletch.ocbloods.action.bank.DepositPieDish;
import com.yfletch.ocbloods.action.bank.DepositRunes;
import com.yfletch.ocbloods.action.bank.FillPouch;
import com.yfletch.ocbloods.action.bank.WithdrawBloodEssence;
import com.yfletch.ocbloods.action.bank.WithdrawPureEssence;
import com.yfletch.ocbloods.action.bank.WithdrawSummerPie;
import com.yfletch.ocbloods.action.bank.castlewars.CastleWarsRingOfDueling;
import com.yfletch.ocbloods.action.bank.castlewars.UseCastleWarsBank;
import com.yfletch.ocbloods.action.bank.castlewars.WearRingOfDueling;
import com.yfletch.ocbloods.action.bank.castlewars.WithdrawRingOfDueling;
import com.yfletch.ocbloods.action.bank.repair.CastNpcContact;
import com.yfletch.ocbloods.action.bank.repair.ClickToContinueNPC;
import com.yfletch.ocbloods.action.bank.repair.ClickToContinuePlayer;
import com.yfletch.ocbloods.action.bank.repair.DepositRunePouch;
import com.yfletch.ocbloods.action.bank.repair.RepairPouch;
import com.yfletch.ocbloods.action.bank.repair.WithdrawRunePouch;
import com.yfletch.ocbloods.action.craft.ActivateBloodEssence;
import com.yfletch.ocbloods.action.craft.CraftRuneAltar;
import com.yfletch.ocbloods.action.craft.EmptyPouch;
import com.yfletch.ocbloods.action.craft.EnterMysteriousRuins;
import com.yfletch.ocbloods.action.travel.caves.EatSummerPie;
import com.yfletch.ocbloods.action.travel.caves.EnterCave3;
import com.yfletch.ocbloods.action.travel.caves.EnterCave4;
import com.yfletch.ocbloods.action.travel.caves.EnterCaveEntrance1;
import com.yfletch.ocbloods.action.travel.caves.EnterCaveEntrance2;
import com.yfletch.ocbloods.action.travel.house.BreakHouseTab;
import com.yfletch.ocbloods.action.travel.house.CloseBank;
import com.yfletch.ocbloods.action.travel.house.DrinkRejuvenation;
import com.yfletch.ocbloods.action.travel.house.TeleportFairyRing;
import com.yfletch.ocbloods.lib.ActionRunner;
import com.yfletch.ocbloods.lib.event.EventBuilder;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OCBloodsRunnerFactory
{
	@Inject
	private OCBloodsContext context;

	@Inject
	private EventBuilder eventBuilder;

	public ActionRunner<OCBloodsContext> create()
	{
		ActionRunner<OCBloodsContext> runner = new ActionRunner<>(context, eventBuilder);


		// bank - repair
		runner.add(new WithdrawRunePouch());
		runner.add(new ClickToContinueNPC());
		runner.add(new ClickToContinuePlayer());
		runner.add(new RepairPouch());
		runner.add(new DepositRunePouch());
		runner.add(new CastNpcContact());

		// bank - castlewars
		runner.add(new CastleWarsRingOfDueling());
		runner.add(new UseCastleWarsBank());
		runner.add(new WithdrawRingOfDueling());
		runner.add(new WearRingOfDueling());

		// bank
		runner.add(new DepositPieDish());
		runner.add(new DepositRunes());
		runner.add(new WithdrawSummerPie());
		runner.add(new WithdrawBloodEssence());
		// repeat these two
		runner.add(new WithdrawPureEssence());
		runner.add(new FillPouch());

		// travel - house
		runner.add(new CloseBank());
		runner.add(new BreakHouseTab());
		runner.add(new DrinkRejuvenation());
		runner.add(new TeleportFairyRing());

		// travel - caves
		runner.add(new EnterCaveEntrance1());
		runner.add(new EnterCaveEntrance2());
		runner.add(new EatSummerPie());
		runner.add(new EnterCave3());
		runner.add(new EnterCave4());

		// craft
		runner.add(new EnterMysteriousRuins());
		runner.add(new ActivateBloodEssence());
		runner.add(new CraftRuneAltar());
		runner.add(new EmptyPouch());

		return runner;
	}
}
