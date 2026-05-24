# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

- Added REI integration.
- Added Mod Menu and Cloth Config API integration.
- New AbilityIcon system.
- Some new icons.
- Added some config options.

## [0.6.0-beta.1] - 2026-05-20

### Added

- Added stone use and hold abilities for the rest of the stones.
- Added some time stone abilities.
- Added translations.
- Added blueprint item and fake item.
- Using Datagen APIs.
- Added items for progression.
- Added ore gen.
- Added villager trade for infinity gauntlet blueprint.
- Added infinity gauntlet blueprint to armorer villager chest loot table.
- Added gauntlet cooldown saving to nbt.
- Added gauntlet equipped stones tooltip.
- Added blueprint table and infinity gauntlet crafting.

### Changed

- Bump versions of the fabric loader, fabric api and loom.
- ActiveAbility onActivate takes ServerWorld and ServerPlayerEntity now.
  Consistent with the other ability types.
- Made soul stone ability color brighter and more distinguishable.
- Changed default keybindings.
- Project hierarchy changes.
- Made GauntletAbilityRegistry a proper fabric registry.
- Made gauntlet textures brighter.

### Fixed

- Prevent gauntlet ability hud rendering on F3 Menu.
- Prevent gauntlet ability usage on spectator.
- Fixed issues with switching between one gauntlet to another gauntlet.
- Fixed ability scroll offset overflow in some cases.

### Removed

- Removed DrawContextMixin for changes made in infinity_gauntlet.json.
- Removed PlayerEntityMixin and LivingEntityMixin.


## [0.3.0-beta.1] - 2026-05-08

### Added

- Added flight and forcefield abilities. Beginning stages.
- Added rainbow color for abilities that use all the stones. (e.g. The Snap Ability)
- Added base attributes to infinity gauntlet as a weapon.

### Changed

- Infinity gauntlet render uses infinity stone internal render function now. Consistent with the infinity tesseract.
- Reduced stone collision effect.
- Changed gauntlet state classes and server tick to be more performant.
- Changed client state and client tick to be more performant.
- Gauntlet activeStones caching from nbt for performance improvements.
- Made changes to full stone set effects.

### Fixed

- Fixed stone effect with iris. Glint effect replaced with glow effect for iris with shaders.


## [0.1.0-beta.1] - 2026-04-26

- My first official release of the mod.


[unreleased]: https://github.com/AlvinIsSoCool/Infinityforge/compare/v0.6.0-beta.1...HEAD
[0.6.0-beta.1]: https://github.com/AlvinIsSoCool/Infinityforge/compare/v0.3.0-beta.1...v0.6.0-beta.1
[0.3.0-beta.1]: https://github.com/AlvinIsSoCool/Infinityforge/compare/v0.1.0-beta.1...v0.3.0-beta.1
[0.1.0-beta.1]: https://github.com/AlvinIsSoCool/infinityforge/releases/tag/v0.1.0-beta.1
