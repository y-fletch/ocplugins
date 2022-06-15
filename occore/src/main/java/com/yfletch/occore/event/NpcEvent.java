package com.yfletch.occore.event;

import com.yfletch.occore.util.NpcHelper;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;

public class NpcEvent extends EventOverride
{
	private final NpcHelper npcHelper;

	NpcEvent(NpcHelper npcHelper)
	{
		this.npcHelper = npcHelper;
		setParam0(0);
		setParam1(0);
	}

	/**
	 * Set option name and index (one-based)
	 */
	public NpcEvent setOption(String option, int index)
	{
		setOption(option);

		switch (index)
		{
			case 1:
				setType(MenuAction.NPC_FIRST_OPTION);
				break;
			case 2:
				setType(MenuAction.NPC_SECOND_OPTION);
				break;
			case 3:
				setType(MenuAction.NPC_THIRD_OPTION);
				break;
			case 4:
				setType(MenuAction.NPC_FOURTH_OPTION);
				break;
			case 5:
				setType(MenuAction.NPC_FIFTH_OPTION);
				break;
		}

		return this;
	}

	/**
	 * Target the specific NPC
	 */
	public NpcEvent setNpc(NPC npc)
	{
		setTarget(npc.getName());
		setIdentifier(npc.getIndex());

		return this;
	}

	/**
	 * Target the nearest NPC to the player matching the given ID
	 */
	public NpcEvent setNpc(int npcId)
	{
		NPC npc = npcHelper.getNearest(npcId);
		return npc != null ? setNpc(npc) : this;
	}
}
