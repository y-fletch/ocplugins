package com.yfletch.summergarden;

import com.yfletch.summergarden.util.RSNumberFormat;
import java.text.DecimalFormat;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;

@Getter
@Singleton
public class Stats
{
	@Inject
	private State state;

	private int fruitPicked = 0;
	private int fails = 0;
	private int ticks = 0;

	public void incFruitPicked()
	{
		fruitPicked++;
	}

	public void incFails()
	{
		fails++;
	}

	public void tick()
	{
		ticks++;
	}

	public String xpGained()
	{
		return RSNumberFormat.format(fruitPicked * 1500);
	}

	public String xpHr()
	{
		double hours = ticks * 0.6d / 3600d;
		return RSNumberFormat.format(fruitPicked * 1500 / hours);
	}

	public boolean canSeeTotalJuice()
	{
		return state.getBankCount(Consts.SUMMER_SQIRK_JUICE) > 0;
	}

	public int getTotalJuice()
	{
		return state.getTotalCount(Consts.SUMMER_SQIRK_JUICE);
	}

	public String xpBanked()
	{
		return RSNumberFormat.format(getTotalJuice() * 3000);
	}

	public String failRate()
	{
		if (fails == 0)
		{
			return "0%";
		}

		return new DecimalFormat("0.0").format((double) (fails * 100) / (double) (fails + fruitPicked)) + "%";
	}
}
