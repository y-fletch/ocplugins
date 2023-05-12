package com.yfletch.occore.v2.interaction;

import net.unethicalite.api.Interactable;
import net.unethicalite.api.magic.Spell;
import net.unethicalite.api.widgets.Widgets;

public class DeferredSpellInteraction extends DeferredInteraction
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
}
