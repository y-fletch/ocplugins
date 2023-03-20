package com.yfletch.ocsepulchre.obstacle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class WizardCycle
{
	@Getter
	private int currentTick = 0;

	/**
	 * If this object is confirmed to be synced
	 * with the object in game
	 */
	@Getter
	private boolean synced = false;

	/**
	 * Possible offsets to current step that match
	 * the current cycle in game
	 */
	@Getter
	private List<Integer> possibleOffsets = new ArrayList<>();

	private final List<WizardCycleStep> cycle = new ArrayList<>();

	public WizardCycle add(int[] left, int[] right, int duration)
	{
		List<Boolean> bLeft = Arrays.stream(left).mapToObj(v -> v == 1).collect(Collectors.toList());
		List<Boolean> bRight = Arrays.stream(right).mapToObj(v -> v == 1).collect(Collectors.toList());
		cycle.add(new WizardCycleStep(bLeft, bRight, duration));
		return this;
	}

	public int getCycleTime()
	{
		int cycleTime = 0;
		for (WizardCycleStep step : cycle)
		{
			cycleTime += step.getDuration();
		}

		return cycleTime;
	}

	public int getCurrentStepIndex()
	{
		int ticks = currentTick;
		int i = 0;
		for (WizardCycleStep step : cycle)
		{
			if (ticks >= step.getDuration())
			{
				ticks -= step.getDuration();
			}
			else
			{
				return i;
			}
			i++;
		}

		throw new RuntimeException("No step found for tick: " + currentTick);
	}

	public WizardCycleStep getStepAt(int tick)
	{
		for (WizardCycleStep step : cycle)
		{
			if (tick >= step.getDuration())
			{
				tick -= step.getDuration();
			}
			else
			{
				return step;
			}
		}

		throw new RuntimeException("No step found for tick: " + currentTick);
	}

	public WizardCycleStep getCurrentStep()
	{
		return getStepAt(currentTick);
	}

	public Collection<Boolean> getLeftActive()
	{
		return getCurrentStep().getLeft();
	}

	public Collection<Boolean> getRightActive()
	{
		return getCurrentStep().getRight();
	}

	public void trySync(List<Boolean> currentActiveLeft, List<Boolean> currentActiveRight)
	{
		WizardCycleStep currentStep = getCurrentStep();
		if (
			synced &&
				(!currentActiveLeft.equals(currentStep.getLeft()) || !currentActiveRight.equals(currentStep.getRight()))
		)
		{
			synced = false;
			possibleOffsets.clear();
		}

		if (synced) return;

		int cycleTime = getCycleTime();

		if (possibleOffsets.size() > 1)
		{
			// reduce possible offsets
			List<Integer> stillValidOffsets = new ArrayList<>();
			for (int i : possibleOffsets)
			{
				int tick = (currentTick + i) % cycleTime;
				WizardCycleStep step = getStepAt(tick);

				if (step.getLeft().equals(currentActiveLeft) && step.getRight().equals(currentActiveRight))
				{
					stillValidOffsets.add(i);
				}
			}
			possibleOffsets = stillValidOffsets;
			return;
		}

		if (possibleOffsets.size() == 0)
		{
			// no possible offsets yet - let's create the first guess
			for (int i = 1; i < getCycleTime(); i++)
			{
				int tick = (currentTick + i) % cycleTime;
				WizardCycleStep step = getStepAt(tick);

				if (step.getLeft().equals(currentActiveLeft) && step.getRight().equals(currentActiveRight))
				{
					possibleOffsets.add(i);
				}
			}
		}

		if (possibleOffsets.size() == 1)
		{
			synced = true;
			currentTick = (currentTick + possibleOffsets.get(0)) % cycleTime;
		}
	}

	public void tick()
	{
		currentTick++;

		if (currentTick >= getCycleTime())
		{
			currentTick = 0;
		}
	}

	@Getter
	@AllArgsConstructor
	private static class WizardCycleStep
	{
		private List<Boolean> left;
		private List<Boolean> right;

		private int duration;
	}
}
