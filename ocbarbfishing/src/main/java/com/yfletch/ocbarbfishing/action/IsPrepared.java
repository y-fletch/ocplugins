package com.yfletch.ocbarbfishing.action;

import com.yfletch.ocbarbfishing.OCBarbFishingContext;
import com.yfletch.occore.action.PrepAction;
import java.util.HashMap;

public class IsPrepared extends PrepAction<OCBarbFishingContext>
{
	@Override
	protected HashMap<String, Boolean> getConditions(OCBarbFishingContext ctx)
	{
		HashMap<String, Boolean> map = new HashMap<>();

		map.put("Not near fishing spots", ctx.isAtFishingSpots());
		map.put("Missing knife", ctx.hasKnife());
		map.put("Missing barbarian rod", ctx.hasBarbarianRod());
		map.put("Missing feathers", ctx.hasFeathers());

		return map;
	}
}
