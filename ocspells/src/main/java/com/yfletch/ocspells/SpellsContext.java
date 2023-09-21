package com.yfletch.ocspells;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import static com.yfletch.occore.v2.util.Util.getSpellByName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Item;
import net.unethicalite.api.items.Inventory;

@Slf4j
@Singleton
public class SpellsContext extends CoreContext
{
	@Inject private SpellsConfig config;

	@Getter
	@Setter
	@Accessors(fluent = true)
	private boolean canCast;

	public String[] getBankableItems()
	{
		final var all = Inventory.getAll();
		final var spell = getSpellByName(config.spell());

		return all.stream()
			.map(Item::getName)
			// ignore anything ending with " rune"
			// ignore rune pouch & divine rune pouch
			.filter(name -> !name.endsWith(" rune")
				&& !name.contains("une pouch"))
			.toArray(String[]::new);
	}
}
