package com.yfletch.occore.event;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.movement.Movement;

public class MoveEvent extends EventOverride
{
	private Client client;
	private WorldPoint worldPoint;

	MoveEvent(Client client)
	{
		this.client = client;

		setTarget("");
		setOption("Walk here");
		setIdentifier(0);
		setType(MenuAction.WALK);
	}

	@Override
	protected void validate()
	{
		if (getParam0() == -1)
		{
			throw new RuntimeException("MoveEvent: Requires target point");
		}

		super.validate();
	}

	public MoveEvent to(WorldPoint worldPoint)
	{
		setParam0(worldPoint.getX() - client.getBaseX());
		setParam1(worldPoint.getY() - client.getBaseY());
		this.worldPoint = worldPoint;
		return this;
	}

	@Override
	public void override()
	{
		if (getTargetEvent().getMenuOption().equals("Automated"))
		{
			return;
		}

		validate();

		getTargetEvent().consume();
		Movement.walk(worldPoint);
	}
}
