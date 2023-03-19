package com.yfletch.ocsepulchre.obstacle;

import net.runelite.api.coords.WorldPoint;

public interface DrawableObstacle
{
	WorldPoint getDebugPosition();

	String getDebugText();

	String getDebugTextLine2();
}
