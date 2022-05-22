package com.yfletch.rift.lib;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class TrackedObject
{
	@NonNull
	private Integer id;
	private WorldPoint worldPoint;
}
