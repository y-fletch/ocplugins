# OCPlugins

OpenOSRS OC plugins. Use at your own risk.

Plugins marked `alpha` aren't fully tested and may not work correctly.

For any questions, reach out to me on Discord: `@yfletch`

### Common features

Some plugins built with the newest version of the core library share these features:

<details>
<summary>Features</summary>

* Master control switch, and bindable quick toggle hotkey
* Selection of automation API
    * `One-click` - forwards any manual clicks in the game window to the plugin, that alters it to perform the next
      action.
    * `One-click (consume)` - same as `One-click`, but consumes (blocks) any extra clicks to prevent stray walking.
    * `One-click (auto)` - uses the same API as the above two, but starts an in-built autoclicker. Clicks per tick can
      be configured per plugin.
    * `Devious` - uses Devious client's API, i.e. whatever method Devious is currently set to (packets, invokes, etc).
    * **Note**: Use whichever option at your own risk. None are proven to be "un-bannable"
* Break handling
    * Set an interval and duration for breaks, which are randomised slightly. The plugin will be more or less disabled
      while it is in break mode.
* Safety [WIP]
    * Auto logout if the plugin is stuck (hasn't performed an action in some amount of time)
* Overlays - separately toggleable
    * Current action (and break information)
    * Plugin statistics
* Debug
    * Internal state debug overlay
    * World debug overlay / interaction highlights
    * Menu entry logging to ingame chatbox

</details>

## Plugins

### OC Bank Skills

Auto train Crafting/Fletching/Herblore

<details>
<summary>Features</summary>

* Easily set ingredients and product
* Withdraws ingredients from the nearest bank, uses item 1 on item 2, crafts/mixes, and deposits products back

</details>

<details>
<summary>Setup</summary>

* Set the primary and secondary ingredients in the config, as well as the product item (all are case-sensitive)
    * e.g. `Willow longbow (u)` + `Bow string` = `Willow longbow`
    * or `Dwarf weed potion (unf)` + `Wine of Zamorak` = `Ranging potion(3)`
* Set your bank "Withdraw X" to the required amount (e.g. 14)
* If using a third item (like thread for crafting), withdraw it from your bank and fill with bank fillers. Then it won't
  be deposited.
* Run the plugin near any bank

e.g. dragonhide crafting setup

* Withdraw all threads from bank, remove placeholder and fill all bank fillers
* Withdraw 26x of any item (and deposit again)
* Set Item 1 to "Green dragon leather"
* Set Item 2 to "Needle"
* Set product to "Green d'hide chaps"
* Run the plugin
    * When the plugin deposits all, the thread will stay in the inventory
    * After withdrawing 26 leathers, there will only be one slot left for the needle

</details>

### OC Barbarian Fishing

3-tick Barbarian fishing

<details>
<summary>Features</summary>

* Guam/Swamp tar & drop method
* Cut fish & eat method
* Combination of above

</details>

<details>
<summary>Setup</summary>

Put required items for the method in your inventory (the plugin will tell you what's needed). You must click at least 3
times per tick for this to work properly.
</details>

### OC Blast Furnace [alpha]

One-click Blast Furnace for official BF worlds. Unfinished, only supports creating gold bars.

<details>
<summary>Features</summary>

* Drink stamina when required
* Switches between goldsmith and ice gloves

</details>

### OC Bloods

Auto blood Runecrafting

<details>
<summary>Features</summary>

* Uses Blood essence
* Banks at Castle wars
* Uses Summer pies to boost to 93 Agility for shortcuts
* Restores run in house Rejuvenation pool
* Repairs pouches with NPC Contact
* Tracks performance and runes/profit made

</details>

<details>
<summary>Setup</summary>

* Set last-destination for your POH fairy ring to d&middot;l&middot;s
* Prep inventory with
    * Colossal pouch
    * Any active Blood essence (if required)
    * All teleport to house tabs
* Fill rune pouch with NPC Contact runes
* Equip Dramen/Lunar staff if required
* Use more than one click per tick for fastest trips (not required)

</details>

### OC Bwans

Perfect 1-tick Karambwan cooking, ~1m xp/hr

<details>
<summary>Setup</summary>

* Run the plugin near any range/fire with a bank nearby

</details>

### OC Clicker

Auto-clicker that doesn't need mouse focus.

**Note**: New plugins have this clicker built-in, so this isn't required. It can be enabled by setting the plugin's "
Plugin API" to `One-click (auto)`

<details>
<summary>Features</summary>

* Customizable toggle hotkey
* Ability to consume yellow clicks
* Ability to change number of clicks per tick

</details>

<details>
<summary>Setup</summary>
* Put your cursor over blank space (either in inventory or other UI)
* Press hotkey
</details>

### OC Core

Core FSM library for one-click plugins. See example implementations
in [TestPlugin](https://github.com/y-fletch/ocplugins/blob/main/occore/src/main/java/com/yfletch/occore/v2/test/TestPlugin.java)
, [OCBankSkillsPlugin](https://github.com/y-fletch/ocplugins/blob/main/ocbankskills/src/main/java/com/yfletch/ocbankskills/OCBankSkillsPlugin.java)
, [OCBwansPlugin](https://github.com/y-fletch/ocplugins/blob/main/ocbwans/src/main/java/com/yfletch/ocbwans/OCBwansPlugin.java)
or [OCPickpocketPlugin](https://github.com/y-fletch/ocplugins/blob/main/ocpickpocket/src/main/java/com/yfletch/ocpickpocket/OCPickpocketPlugin.java)
.

**Does not need to be installed for other plugins to work**

### OC Dancing

Clicks back and forth between two tiles, with a customizable delay. Useful for stacking mobs for bursting etc.

<details>
<summary>Setup</summary>

* Shift-right click on the target tiles to add them to config (or enter coordinates manually)

</details>

### OC Fly Fishing

One-click fly fishing for trout and salmon. Cooks and drops fish at a nearby fire (use in Barbarian Village)

<details>
<summary>Setup</summary>

* Bring a fly fishing rod and feathers to the Barbarian Village fishing spot
* Run the plugin

</details>

### OC Garden [alpha]

Auto/one-click Summer Garden. Runs the garden, crushes Sq'irks and banks for stamina potions. **Warning** - not
maintained and can be a buggy

<details>
<summary>Features</summary>

* Uses POH tabs to bank in Al-Kharid
* Aligns Elementals automatically (through trial and error)
* Drinks Stamina (and drinks an extra dose if energy is too low)

</details>

### OC Nightmare Zone

Auto stat boosting and absorption for Nightmare Zone

### OC Pickpocket

Full auto pickpocketing for any* NPC. Can be used at Ardougne Knigts, Vyres and Elves.

<details>
<summary>Features</summary>

* Customisable pickpocket target(s)
* Auto banking
* Auto eat
    * Drinks wine jugs that have been pickpocketed
* Auto equip Dodgy necklaces
* Auto cast Shadow Veil
* Drops low value items
* Prioritizes high value items
* Auto pathing to bank / target (can open doors)

</details>

<details>
<summary>Setup</summary>

* Enter desired target, food, and items into the config, or use the preset buttons
* Start the plugin in the vicinity of the target(s) and any bank

</details>

### OC Rift [alpha]

Auto Guardians of the Rift (for mass worlds). Not optimal gameplay, but it's OK.

<details>
<summary>Features</summary>

* Configurable time to exit huge essence mine
* Crafts best rune based on level and current energy balance
* Repairs pouches with NPC Contact
* Supports all pouches (only confirmed with Colossal pouch)
* Switches Eye robe top and Varrock platebody for mining
* Configurable runes to drop or deposit
* Performance and rune tracking

</details>

<details>
<summary>Setup</summary>

* Enter GOTR with regular setup
    * All available pouches
    * Rune pouch with NPC Contact runes
    * Varrock platebody
    * Pickaxe/lantern/robes equipped
* If pouches don't need to be repaired, shift+right click the overlay to `Skip repair`

</details>

### OC Salamanders

Black Salamander hunter. Does not support other salamanders.

<details>
<summary>Features</summary>

* Sets traps on young trees
* Picks up broken traps
* Checks full traps
* Drops salamanders

</details>

<details>
<summary>Setup</summary>

* Take as many small fishing nets & ropes as you can setup in the wilderness
* Go to the black salamander hunting spot
* Run the plugin

</details>

### OC Sepulchre [WIP]

Auto Hallowed Sepulchre. Very very WIP

### OC Todt

One-click Wintertodt

<details>
<summary>Features</summary>

* Auto banking
* Auto eating
* Dodges _most_ environmental hazards
* Fixes braziers
* Optionally fletches bruma roots

</details>

<details>
<summary>Setup</summary>

Use your normal Wintertodt setup. Start the plugin anywhere within the Wintertodt camp / boss
</details>