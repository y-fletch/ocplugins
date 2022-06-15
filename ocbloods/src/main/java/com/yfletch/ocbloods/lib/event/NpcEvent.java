package com.yfletch.ocbloods.lib.event;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.queries.NPCQuery;

public class NpcEvent extends EventOverride
{
	private final Client client;

	NpcEvent(Client client)
	{
		this.client = client;
		setParam0(0);
		setParam1(0);
	}

	public NpcEvent setOption(String option, int optionId)
	{
		setOption(option);

		switch (optionId)
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

	public NpcEvent setNpc(NPC npc)
	{
		setTarget(npc.getName());
		setIdentifier(npc.getIndex());

		return this;
	}

	public NpcEvent setNpc(int npcId)
	{
		NPC npc = new NPCQuery().idEquals(npcId).result(client).nearestTo(client.getLocalPlayer());
		return npc != null ? setNpc(npc) : this;
	}
}
