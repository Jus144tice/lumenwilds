# Changelog

All notable changes to The Lumenwilds are documented here. The format is based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.0] - 2026-06-14
The first full release of **The Lumenwilds** — a complete, explorable bioluminescent dimension for
NeoForge 1.21.1 / Java 21, reached through a player-built portal.

### Dimension & portal
- A working player-built portal: a **Lumenbound Stone** frame (never lodestone) lit with the **Lumen
  Striker** opens the **Lumen Portal** to `lumenwilds:lumenwilds`; stepping through teleports you in and
  back, building a return portal at 1:1-scaled coordinates. **Lumen Anchors** let you pair two anchors to
  land return travel precisely.
- **Lumenbound Ruins** generate in the Overworld as the in-world tutorial: a broken portal site with a
  chest of striker + frame ingredients.
- Bespoke **low-gravity movement**: reduced gravity, higher jumps, later/halved fall damage, flatter
  projectile arcs (vanilla attribute modifiers applied on dimension entry).

### World, terrain & atmosphere
- **Seven biomes** via a `multi_noise` source: Lumen Glade, Glowroot Forest, Glasspetal Crags, Sporefall
  Jungle, Moonmire, Undercrown Caverns (underground), and the rare Stillbloom Basin — over bespoke alien
  cliffy terrain with glowing **Lumenwater** seas/pools and a deep noise-cave Undercrown.
- A bespoke **sky** (perpetual dim twilight under the giant moon *Veyra*), ambient **particles**, a
  vanilla-sourced **soundscape**, a **half-rate (48k-tick) day cycle**, and rotating **ambient events**
  (Sporefall / Moonwake / Deep Hush).
- Signature flora & worldgen: Moonblossom, Glow Fern, Glowwood & **Glowroot** trees (incl. town-sized mega
  trees), Giant Glowcaps (+ mega), Glasspetal crystal growths, giant Stillblooms, undersea Lumen reefs,
  and harvestable Glowberry bushes.

### Content
- Full **building-block sets**: Glowwood wood set (signs, boats, stripping), Moonstone / Deep Moonstone /
  Shimmerstone stone sets, Sporeglass, Lumen Crystal, Luminite, and the luminous **Glowbrick** family.
- **Ten native mobs**: Lumen Grazer, Shade Stalker, Lantern Beetle, Sporeling, Mirelurker, Lumen Fish,
  Sky Jelly, Glowmoth, Rootback, Crag Wraith — plus the ruin-guardian **Echo Sentinel**. Each with bespoke
  models, emissive glow, loot, and spawn-egg.
- **Status effects** (Lightfoot, Glowmarked, Sporeblind, Rooted) with brewing; **foods** (Lumen Fruit,
  Lumen Nectar, Glowcap Stew); a full **advancement** progression tree.

### The Lumenwrights — Vestige Cities (Phase 10)
- Rare ruined alien cities (Small Outpost → Medium → Grand), built from Glowbrick/Luminite, heavily decayed
  and overgrown, with biome-specific flavors, broken Spires, and a buried **Vestige Vault**.
- A functional **Resonance** subsystem: Resonance Cores flood power through Lumen Conduits to open Ancient
  Doors and drive Gravity Lenses; restore a Dormant Light Engine to wake a dead city. Lore via **Memory
  Crystals** + **Glyph Tablets**. The whole Lumenwright tech kit is craftable from looted fragments.

### Lumenwright Liftshafts (Phase 11)
- Gravity-column traversal tech: non-solid **Ascension/Descent Field** blocks (controlled lift / safe
  descent) driven by a craftable **Lumen Field Projector** extended by wall-mounted **Gravity Repeaters**.
- **Abandoned Luminite Mines** generate at Vestige Cities — paired working liftshafts built from real,
  reverse-engineerable components down to a carved alien mine (cave-aware: connects to natural caverns when
  found). Locate the nearest mine-bearing city with `/locate structure lumenwilds:vestige_mine`.

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

[Unreleased]: https://github.com/Jus144tice/lumenwilds/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/Jus144tice/lumenwilds/releases/tag/v1.0.0
[0.1.0]: https://github.com/Jus144tice/lumenwilds/releases/tag/v0.1.0
