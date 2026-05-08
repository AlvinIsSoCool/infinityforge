# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]


## [0.3.0-beta.1] - 2026-05-00

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


[unreleased]: https://github.com/AlvinIsSoCool/Infinityforge/compare/v0.1.0-beta.1...HEAD
[0.3.0-beta.1]: https://github.com/AlvinIsSoCool/Infinityforge/compare/v0.1.0-beta.1...v0.3.0-beta.1
[0.1.0-beta.1]: https://github.com/AlvinIsSoCool/infinityforge/releases/tag/v0.1.0-beta.1