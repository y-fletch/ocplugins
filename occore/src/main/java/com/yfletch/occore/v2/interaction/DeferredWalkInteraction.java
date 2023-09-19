package com.yfletch.occore.v2.interaction;

import com.yfletch.occore.v2.overlay.WorldDebug;
import com.yfletch.occore.v2.util.TextColor;
import lombok.AllArgsConstructor;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.client.Static;

@AllArgsConstructor
public class DeferredWalkInteraction implements DeferredInteraction
{
	private final WorldPoint target;

	@Override
	public String getTooltip()
	{
		return TextColor.WHITE + "Walk-to " + getTarget();
	}

	@Override
	public void execute()
	{
		Movement.walk(target);
	}

	@Override
	public void onActive()
	{
		WorldDebug.setTile(target);
	}

	@Override
	public MenuEntry createMenuEntry()
	{
		final var client = Static.getClient();

		final var sceneX = target.getX() - client.getBaseX();
		final var sceneY = target.getY() - client.getBaseY();
		final var canvas = Perspective.localToCanvas(client, LocalPoint.fromScene(sceneX, sceneY), client.getPlane());
		final var x = canvas != null ? canvas.getX() : -1;
		final var y = canvas != null ? canvas.getY() : -1;

		return client.createMenuEntry(-1)
			.setOption("Walk here")
			.setTarget(getTarget())
			.setType(MenuAction.WALK)
			.setIdentifier(0)
			.setParam0(x)
			.setParam1(y);
	}

	public String getTarget()
	{
		return TextColor.NPC + target.getX() + ", " + target.getY();
	}
}
