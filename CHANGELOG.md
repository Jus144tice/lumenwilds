# Changelog

All notable changes to The Lumenwilds are documented here. The format is based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.0] - 2026-06-06
### Added
- Phase 1 scaffolding for the NeoForge 1.21.1 / Java 21 mod (`lumenwilds`).
- Registries: blocks, items, creative tab, plus empty stubs for fluids, mob effects, entities, block
  entities, menus, sounds, particles, features, biomes, dimensions.
- Placeholder blocks: lumenbound_stone, lumen_portal, moonloam, lumen_grass_block, moonstone,
  cobbled_moonstone, glowwood_log, glowwood_planks, glowroot_log, glowvine, moonblossom, lumenbulb,
  lumen_crystal_block.
- Placeholder items: lumen_striker, lumen_crystal_shard, glow_pollen, living_fiber, lumen_fruit,
  lumen_nectar, air_gel, plus block items.
- Creative tab "The Lumenwilds" (auto-populated from the item registry).
- Portal scaffolding: `LumenPortalBlock` (non-solid, glowing, no teleport yet), `LumenPortalShape`,
  `LumenPortalManager`, `LumenPortalTeleporter` (all stubbed with TODOs).
- `LumenStrikerItem`: detects right-clicking Lumenbound Stone and logs a portal-activation attempt.
- Dimension scaffolding: resource keys (`world.LumenDimensionConstants`) and placeholder
  dimension/dimension_type JSON reusing vanilla overworld terrain + a fixed biome.
- Low-gravity scaffolding (`effects.LowGravityHandler`) gated to the Lumenwilds dimension (logs only).
- Datagen providers (blockstates, item models, lang, recipes, loot tables, block tags) — run with
  `./gradlew runData`.
- Recipes for Lumenbound Stone and the Lumen Striker.
- Flat-colour placeholder textures + cube_all models for all blocks/items.
- Docs: README, `docs/IMPLEMENTATION_PLAN.md`, `docs/LUMENWILDS_WORLD_DEFINITION.md`.

[Unreleased]: https://github.com/Jus144tice/lumenwilds/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/Jus144tice/lumenwilds/releases/tag/v0.1.0
