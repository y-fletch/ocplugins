package com.yfletch.ocsepulchre.util;

import com.yfletch.occore.util.RegionPoint;
import lombok.Getter;

@Getter
public class TileDebugInfo
{
	private final RegionPoint regionPoint;
	private final String line1;
	private final String line2;

	public TileDebugInfo(RegionPoint regionPoint, String line1, String line2)
	{
		this.regionPoint = regionPoint;
		this.line1 = line1;
		this.line2 = line2;
	}

	public TileDebugInfo(RegionPoint regionPoint, String line1)
	{
		this(regionPoint, line1, null);
	}

	public TileDebugInfo(RegionPoint regionPoint)
	{
		this(regionPoint, null, null);
	}
}
