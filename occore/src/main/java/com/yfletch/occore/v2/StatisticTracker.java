package com.yfletch.occore.v2;

import com.yfletch.occore.v2.util.RSNumberFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatisticTracker
{
	private final DecimalFormat decimal = new DecimalFormat("0.##");

	private final Map<String, Double> values = new HashMap<>();
	private final List<String> standardDisplays = new ArrayList<>();
	private final List<String> perHourDisplays = new ArrayList<>();
	private final Map<String, List<String>> percentageDisplays = new HashMap<>();

	private int ticks = 0;

	public void tick()
	{
		ticks++;
	}

	public void clear()
	{
		ticks = 0;
		values.clear();
	}

	public Map<String, String> collect()
	{
		final var map = new LinkedHashMap<String, String>();

		map.put("Elapsed", getElapsedTime());

		final var added = new HashSet<String>();
		for (var key : standardDisplays)
		{
			final var rawValue = values.getOrDefault(key, 0d);
			if (percentageDisplays.containsKey(key))
			{
				final var totalValue = percentageDisplays.get(key).stream()
					.mapToDouble(relatedKey -> values.getOrDefault(relatedKey, 0d))
					.sum();
				final var percentage = totalValue > 0
					? decimal.format((rawValue / totalValue) * 100)
					: "0";

				map.put(key, RSNumberFormat.format(rawValue) + " (" + percentage + "%)");
			}
			else
			{
				map.put(key, RSNumberFormat.format(rawValue));
			}

			if (perHourDisplays.contains(key))
			{
				final var ph = rawValue / getHours();
				map.put(key + "/h", RSNumberFormat.format(ph));
			}

			added.add(key);
		}

		for (var key : perHourDisplays)
		{
			if (added.contains(key))
			{
				continue;
			}

			final var rawValue = values.getOrDefault(key, 0d);
			final var ph = rawValue / getHours();
			map.put(key + "/h", RSNumberFormat.format(ph));
		}

		return map;
	}

	public void addDisplays(String... keys)
	{
		standardDisplays.addAll(Arrays.asList(keys));
	}

	public void addPerHourDisplays(String... keys)
	{
		perHourDisplays.addAll(Arrays.asList(keys));
	}

	public void addPercentageDisplay(String key, List<String> related)
	{
		percentageDisplays.put(key, related);
	}

	public void set(String key, double value)
	{
		values.put(key, value);
	}

	public void add(String key, double value)
	{
		values.put(key, values.getOrDefault(key, 0d) + value);
	}

	public void sub(String key, double value)
	{
		values.put(key, values.getOrDefault(key, 0d) - value);
	}

	private double getHours()
	{
		return ticks * 0.6d / 3600d;
	}

	private String getElapsedTime()
	{
		final var seconds = (int) (ticks * 0.6d);
		final var minutes = (int) (seconds / 60d);
		final var hours = (int) (minutes / 60d);

		return hours > 0
			? String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
			: String.format("%02d:%02d", minutes % 60, seconds % 60);
	}
}
