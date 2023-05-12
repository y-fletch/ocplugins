package com.yfletch.ocbankskills;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.widgets.WidgetInfo;
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
	@Inject BankSkillsContext context;
	@Inject BankSkillsConfig config;

	@Inject
	public void init(BankSkillsConfig config, BankSkillsContext context)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(BankSkillsConfig.GROUP_NAME);
	}

	@Override
	public void setup()
	{
		requirements()
			.must(c -> c.getConfig().primary() != -1, "Primary item must be set")
			.must(c -> c.getConfig().secondary() != -1, "Secondary item must be set");

		requirements()
			.when(c -> config.primary() != -1 && config.secondary() != -1)
			.mustHaveBanked(config.primary(), config.secondary())
			.must(c -> config.makeOption() > 0, "Make option must be valid");

		// bank
		action()
			.when(c -> !Bank.isOpen()
				&& (!Inventory.contains(config.primary()) || !Inventory.contains(config.secondary())))
			.then(c -> interact().click("Use", "Bank").on(name -> name.toLowerCase().contains("bank")));

		// deposit any items other than primary/secondary
		action()
			.when(c -> Bank.isOpen()
				&& (Inventory.contains(item -> item.getId() != config.primary() && item.getId() != config.secondary())))
			.then(c -> interact().click(WidgetInfo.BANK_DEPOSIT_INVENTORY));

		// withdraw primary
		action()
			.when(c -> !Inventory.contains(config.primary()))
			.then(c -> interact().withdrawX(config.primary()));

		// withdraw secondary
		action()
			.when(c -> !Inventory.contains(config.secondary()))
			.then(c -> interact().withdrawX(config.secondary()));

		// close bank
		action()
			.when(c -> Bank.isOpen())
			.then(c -> interact().click(c.getBankCloseButton()));

		// click make option
		action()
			.when(c -> c.getMakeButton() != null)
			.then(c -> interact().click(c.getMakeButton()));

		// use primary on secondary
		action()
			.when(c -> !c.isAnimating() && c.getMakeButton() == null)
			.then(c -> interact().use(config.primary()).onItem(config.secondary()));
	}

	@Provides
	BankSkillsConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BankSkillsConfig.class);
	}
}
