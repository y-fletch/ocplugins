# OCPlugins

OpenOSRS OC plugins. Use at your own risk.

Plugins in `alpha` aren't fully tested and may not work correctly.

## OC Bank Skills

Auto train Crafting/Fletching/Herblore

### Setup

* Set the primary and secondary ingredients in the config, as well as the product item (all are case-sensitive)
  * e.g. `Willow longbow (u)` + `Bow string` = `Willow longbow`
  * or `Dwarf weed potion (unf)` + `Wine of Zamorak` = `Ranging potion(3)`
* Run the plugin near any bank

## OC Barbarian Fishing

3-tick Barbarian fishing

### Features

* Guam/Swamp tar & drop method
* Cut fish & eat method
* Combination of above

### Setup

Put required items for the method in your inventory (the plugin will tell you what's needed). You must click at least 3
times per tick for this to work properly.

## OC Bloods

Auto blood Runecrafting

### Features

* Uses Blood essence
* Banks at Castle wars
* Uses Summer pies to boost to 93 Agility for shortcuts
* Restores run in house Rejuvenation pool
* Repairs pouches with NPC Contact
* Tracks performance and runes/profit made

### Setup

* Set last-destination for your POH fairy ring to d&middot;l&middot;s
* Prep inventory with
    * Colossal pouch
    * Any active Blood essence (if required)
    * All teleport to house tabs
* Fill rune pouch with NPC Contact runes
* Equip Dramen/Lunar staff if required
* Use more than one click per tick for fastest trips (not required)

## OC Bwans

Perfect 1-tick Karambwan cooking, ~1m xp/hr

### Setup

* Run the plugin near any range/fire with a bank nearby

## OC Clicker

Auto-clicker that doesn't need mouse focus.

### Features

* Customizable toggle hotkey
* Ability to consume yellow clicks
* Ability to change number of clicks per tick

### Setup

* Put your cursor over blank space (either in inventory or other UI)
* Press hotkey

## OC Core

Core library for one-click plugins. See example implementations in [TestPlugin](https://github.com/y-fletch/ocplugins/blob/main/occore/src/main/java/com/yfletch/occore/v2/test/TestPlugin.java), [OCBankSkillsPlugin](https://github.com/y-fletch/ocplugins/blob/main/ocbankskills/src/main/java/com/yfletch/ocbankskills/OCBankSkillsPlugin.java) or [OCBwansPlugin](https://github.com/y-fletch/ocplugins/blob/main/ocbwans/src/main/java/com/yfletch/ocbwans/OCBwansPlugin.java).

**Does not need to be installed for other plugins to work**

## OC Garden [alpha]

Auto/one-click Summer Garden. Runs the garden, crushes Sq'irks and banks for stamina potions. **Warning** - not
maintained and can be a buggy

### Features

* Uses POH tabs to bank in Al-Kharid
* Aligns Elementals automatically (through trial and error)
* Drinks Stamina (and drinks an extra dose if energy is too low)

## OC Nightmare Zone

Auto stat boosting and absorption for Nightmare Zone

## OC Rift [alpha]

Auto Guardians of the Rift (for mass worlds). Not optimal gameplay, but it's OK.

### Features

* Configurable time to exit huge essence mine
* Crafts best rune based on level and current energy balance
* Repairs pouches with NPC Contact
* Supports all pouches (only confirmed with Colossal pouch)
* Switches Eye robe top and Varrock platebody for mining
* Configurable runes to drop or deposit
* Performance and rune tracking

### Setup

* Enter GOTR with regular setup
    * All available pouches
    * Rune pouch with NPC Contact runes
    * Varrock platebody
    * Pickaxe/lantern/robes equipped
* If pouches don't need to be repaired, shift+right click the overlay to `Skip repair`

## OC Sepulchre [WIP]

Auto Hallowed Sepulchre. Very very WIP
