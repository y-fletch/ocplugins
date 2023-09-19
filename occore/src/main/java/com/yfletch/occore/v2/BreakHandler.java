package com.yfletch.occore.v2;

import com.google.inject.Singleton;
import static com.yfletch.occore.v2.util.Util.formatTickTime;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.unethicalite.api.commons.Rand;

@Slf4j
@Singleton
public class BreakHandler
{
	@Setter private int interval;
	@Setter private int duration;

	@Getter private int ticksToNextBreak = 0;
	@Getter private int ticksRemainingInBreak = 0;

	@Getter private boolean isInBreak = false;

	public void tick()
	{
		// startup / reset after break
		if (ticksToNextBreak <= 0 && ticksRemainingInBreak <= 0)
		{
			reset();
			return;
		}

		// during break
		if (ticksToNextBreak <= 0)
		{
			isInBreak = true;
			ticksRemainingInBreak--;
			return;
		}

		isInBreak = false;
		ticksToNextBreak--;
	}

	public void reset()
	{
		isInBreak = false;
		ticksToNextBreak = sway(minutesToTicks(interval));
		ticksRemainingInBreak = sway(minutesToTicks(duration));
	}

	public String getTimeToNextBreak()
	{
		return formatTickTime(ticksToNextBreak);
	}

	public String getTimeRemainingInBreak()
	{
		return formatTickTime(ticksRemainingInBreak);
	}

	private int minutesToTicks(int minutes)
	{
		return minutes * 100;
	}

	private int sway(int ticks)
	{
		// sway values by 15%
		final var sway = (int) (ticks * 0.15d);
		return ticks + Rand.nextInt(-sway, sway);
	}
}
