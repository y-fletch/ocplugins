package com.yfletch.ocbankskills;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.banked;
import static com.yfletch.occore.v2.interaction.Entities.entity;
import static com.yfletch.occore.v2.interaction.Entities.item;
import static com.yfletch.occore.v2.interaction.Entities.widget;
import static com.yfletch.occore.v2.util.Util.containing;
import static com.yfletch.occore.v2.util.Util.join;
import static com.yfletch.occore.v2.util.Util.notContaining;
import static com.yfletch.occore.v2.util.Util.parseList;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Bank Skills",
	enabledByDefault = false
)
public class OCBankSkillsPlugin extends RunnerPlugin<BankSkillsContext>
{
	@Inject BankSkillsConfig config;

	@Inject
	public void init(BankSkillsConfig config, BankSkillsContext context)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(BankSkillsConfig.GROUP_NAME);
		refreshOnConfigChange(true);
	}

	@Override
	public void setup()
	{
		requirements().name("Config requirements")
			.must(c -> primary().length > 0, "Primary item(s) must be set")
			.must(c -> secondary().length > 0, "Secondary item(s) must be set")
			.must(c -> product().length > 0, "Product item(s) must be set")
			.mustBeNear(() -> entity(containing("bank")), "any bank");

		requirements().name("Item checks")
			.when(c -> primary().length > 0 && secondary().length > 0)
			.mustHave(join(primary(), secondary()));

		// bank
		action().name("Open bank")
			.when(c -> !Bank.isOpen()
				&& (!Inventory.contains(primary()) || !Inventory.contains(secondary())))
			.then(c -> entity(containing("bank")).interact("Use", "Bank"));

		// deposit any items other than primary/secondary
		action().name("Deposit inventory")
			.when(c -> Bank.isOpen() && (Inventory.contains(
				item -> notContaining(join(primary(), secondary())).test(item.getName())))
			)
			.then(c -> widget("Deposit inventory").interact());

		// withdraw primary
		action()
			.when(c -> !Inventory.contains(primary()))
			.then(c -> banked(primary()).withdrawX());

		// withdraw secondary
		action()
			.when(c -> !Inventory.contains(secondary()))
			.then(c -> banked(secondary()).withdrawX());

		// close bank
		action()
			.when(c -> Bank.isOpen())
			.then(c -> widget(WidgetID.BANK_GROUP_ID, "Close").interact());

		// click make option
		action()
			.when(c -> widget(product()).exists())
			.then(c -> widget(product()).interact("Make"));

		// use primary on secondary
		action()
			.when(c -> !c.isAnimating() && !widget(product()).exists())
			.then(c -> item(primary()).useOn(item(secondary())));
	}

	private String[] primary()
	{
		return parseList(config.primary());
	}

	private String[] secondary()
	{
		return parseList(config.secondary());
	}

	private String[] product()
	{
		return parseList(config.product());
	}

	@Provides
	BankSkillsConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BankSkillsConfig.class);
	}
}
