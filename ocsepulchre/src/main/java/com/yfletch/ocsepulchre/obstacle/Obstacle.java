package com.yfletch.ocsepulchre.obstacle;

import com.yfletch.ocsepulchre.OCSepulchreContext;

public interface Obstacle
{
	void tick(OCSepulchreContext ctx);

	boolean tickIf(OCSepulchreContext ctx);
}
