package com.yfletch.occore.v2.interaction;

import com.yfletch.occore.v2.util.TextColor;
import net.runelite.api.Item;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.magic.Magic;
import net.unethicalite.api.magic.Spell;
import net.unethicalite.api.widgets.Widgets;

/**
 * Represents a deferred interaction with a spell widget, or one that
 * is casting a spell on another entity
 */
public class DeferredSpellInteraction extends DeferredInteraction<Widget>
{
	private final Spell spell;
	private final Interactable target;

	public DeferredSpellInteraction(Spell spell, int actionIndex)
	{
		super(Widgets.get(spell.getWidget()), actionIndex);
		this.spell = spell;
		this.target = null;
	}

	public DeferredSpellInteraction(Spell spell, Interactable target)
	{
		super(Widgets.get(spell.getWidget()), 0);
		this.spell = spell;
		this.target = target;
	}

	@Override
	public void execute()
	{
		if (target != null)
		{
			spell.castOn(target);
			return;
		}

		super.execute();
	}

	@Override
	public void prepare()
	{
		if (target != null)
		{
			Magic.selectSpell(spell);
		}

		super.prepare();
	}

	@Override
	public String getMenuOption()
	{
		if (interactable.getActions() == null)
		{
			return TextColor.WHITE + "Cast" + TextColor.END;
		}

		return super.getMenuOption();
	}

	@Override
	public String getMenuTarget()
	{
		if (target != null)
		{
			return TextColor.SPELL + interactable.getName() + TextColor.END
				+ TextColor.WHITE + " -> " + MenuEntryUtil.getTarget(target);
		}

		return super.getMenuTarget();
	}

	@Override
	protected MenuAction getMenuType()
	{
		if (target != null)
		{
			if (target instanceof NPC)
			{
				return MenuAction.WIDGET_TARGET_ON_NPC;
			}

			if (target instanceof TileObject)
			{
				return MenuAction.WIDGET_TARGET_ON_GAME_OBJECT;
			}

			if (target instanceof Widget || target instanceof Item)
			{
				return MenuAction.WIDGET_TARGET_ON_WIDGET;
			}
		}

		return super.getMenuType();
	}

	@Override
	protected int getMenuIdentifier()
	{
		if (target != null)
		{
			return MenuEntryUtil.getIdentifier(target);
		}

		return super.getMenuIdentifier();
	}

	@Override
	protected int getMenuParam0()
	{
		if (target != null)
		{
			return MenuEntryUtil.getParam0(target);
		}

		return super.getMenuParam0();
	}

	@Override
	protected int getMenuParam1()
	{
		if (target != null)
		{
			return MenuEntryUtil.getParam1(target);
		}

		return super.getMenuParam1();
	}
}
