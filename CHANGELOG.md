# Changelog

All notable changes to The Lumenwilds are documented here. The format is based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.2.1] - 2026-06-18
**Playthrough #4 follow-up fixes:**

- **Lantern Beetles no longer dive-bomb into the ground and die.** They were taking fall damage from their
  low-gravity descents toward ground-level flowers/Glowvine and dying on landing (only 4 HP). Flying mobs are
  now **fall-damage-immune** (like vanilla bees) — Lantern Beetle, Glowmoth, Sky Jelly, Crag Wraith — and the
  beetle's inappropriate panic-flee (which made it dart at the floor) was removed.
- **Gravity Repeaters (and Lumen Field Projectors) now drop when mined.** They were missing their loot tables,
  so they broke into nothing at any tool tier; fixed. (They were always craftable — Gravity Repeater =
  Shimmerstone + Gravity Lens Fragment + Lumen Relay; Field Projector from the fragment kit.)
- **Shimmerstone is now obtainable without hunting structures.** Added a craft — **4 Moonstone + 1 Lumen
  Crystal Shard → 4 Shimmerstone** — and Shimmerstone now generates as ore-blobs in the **Glasspetal Crags**
  (newly generated Crags). Previously it only existed pre-built into Glasspetal Spires / Vestige Mines, which
  could dead-end the Gravity Repeater craft.

## [1.2.0] - 2026-06-17
**Playthrough #4 — tools, harvest tiers, friendlier Sporelings & a new trader:**

- **Lumenwilds tool sets.** A full in-dimension stone→iron progression so you can gear up without hauling
  Overworld tools: **Moonstone tools** (pickaxe/axe/shovel/hoe/sword — stone-tier, crafted from Cobbled
  Moonstone, a touch more durable than stone) and **Luminite tools** (iron-tier, from Luminite Ingots). They
  enchant exactly like their vanilla counterparts.
- **Proper pickaxe restrictions.** Blocks are now tagged with their required tool tier
  (`#minecraft:needs_stone_tool` / `needs_iron_tool`), so a wooden pickaxe no longer mines everything, and
  harvest-level HUD/tooltip mods finally report the correct tier. Stone-tier = the build/stone families;
  iron-tier = the valuable ores + crystal/Luminite blocks (mine those with Luminite or iron+).
- **Sporelings are cute now.** They no longer aggro on sight — they wander harmlessly and only fight back if
  you attack one (still alerting the swarm, still bursting their Sporeblind spore cloud on death).
- **New mob — the Sporeman.** A rare "fully grown Sporeling" wandering **trader** of the Sporefall Jungle:
  neutral (fights back hard only if struck), he deals exclusively in Lumenwilds goods sold for **Overworld
  valuables** — mostly emeralds, with a few premium gold/diamond wares. Each one carries a random handful of
  trades.

## [1.1.4] - 2026-06-17
**Hotfix — dedicated-server boot crash with Create installed.**

- Fixed a startup crash (`NullPointerException: unbound create:chocolate_bucket`, reported as a Create
  `RegisterEvent` failure) that struck large modpacks running **Create** alongside v1.1.3. Create initializes
  its advancements during the `trigger_type` registry phase, reading fluid buckets registered in the earlier
  `item` phase; v1.1.3's extra content shifted the modpack's load order enough to surface a latent
  ordering bug inside Create. Lumenwilds now declares an optional **load-after-Create** ordering so Create
  always registers first, restoring a known-good order. No gameplay change; harmless when Create is absent.

## [1.1.3] - 2026-06-17
**Playthrough #3 — fishing & wood storage:**

- **Lumenwater fishing is now native-only.** You no longer reel in earth fish (cod/salmon/pufferfish);
  the catch is replaced with Lumenwilds species — new **Glimmerfish** (+cooked) and **Sporefin** (a
  pufferfish-analog: edible but usually inflicts Sporeblind), plus Mirefish and a rare live Lumen Fish bucket.
  **Treasure is preserved** — enchanted fishing rods, bows, and books are still catchable, alongside the 6
  fished spell-book enchantments and lumen treasure.
- **Fishing strike animation restored** in Lumenwater (the approaching-bubble + splash particles; vanilla
  hardcoded them to plain water).
- **Glowing wood storage** — **Glowwood/Glowroot Chests** (per-species glowing texture) and
  **Glowwood/Glowroot Barrels**, all emit light and render luminous. Crafted like their vanilla counterparts.
- Fixed a bug where **Glowroot signs didn't save text** (the sign block-entity wasn't registered for them).

## [1.1.2] - 2026-06-16
**Playthrough #2 polish:**

- **Glowing wood is now actually glowing.** The Glowwood & Glowroot sets are emissive-rendered, so they
  look luminous in *any* light (including daylight), and they cast light too (logs 7, planks/shapes 5).
  (v1.1.1 only emitted faint light, which is invisible in daylight — hence "it doesn't glow.")
- **In-game guide overhaul.** Fixed the `[ERROR]` text in the Patchouli book (a misused link macro), and
  reworked it so every look-up-able term is a real clickable link — added a page for each biome + the sky,
  so biome/mob/tech names jump to their own entries. Re-added the survival craft (**book + glowstone dust**).
- **Rootshrines no longer float** on sloped ground (they now root a foundation down to the terrain).

## [1.1.1] - 2026-06-15
**Playthrough #2 fixes:**

- **Wood actually works as wood** — Glowwood & Glowroot planks/logs/etc. are now in the vanilla wood tags
  (`#minecraft:planks` and all `wooden_*`/sign/sapling/log tags). You can now craft a crafting table, chests,
  and every other planks-based recipe from them, and they burn as fuel. (Note: "4 logs → 3 wood blocks" is
  vanilla-correct; planks are the net-positive path — 1 log → 4 planks.)
- **Glowwood now glows** — logs/planks emit light (a gentle radiance), matching the biome's living-light
  theme. Glowroot is brighter still.
- **Glowroot tree roots** — fixed roots generating with a gap between the root and the trunk; they now always
  connect.
- **Grazers are breedable** with **Glowberries** (renewable — plant/harvest the bushes) as well as Lumen
  Fruit. Hold one out to lure and breed them.
- **Worldgen order** — the ruins/cities/mines now generate *after* trees, so trees no longer grow through
  chests or leave partial pieces. (Applies to newly generated chunks.)

## [1.1.0] - 2026-06-15
**Playthrough fixes & integration** — first survival-playthrough feedback, addressed before any v2.0 work:

- **Glowroot wood set** — the signature self-lit tree now has a full wood set (planks, wood/stripped,
  stairs/slab/fence/gate/door/trapdoor/button/plate, signs, hanging signs, boats + axe-stripping), all
  faintly glowing. Closes the "can't make planks from Glowroot logs" gap.
- **Plantable, renewable Glowberries** — the bush is now a sweet-berry-style block (grows through ages,
  bone-mealable, glows brighter as it ripens, right-click to harvest); plant it by right-clicking soil with
  a Glowberry.
- **Flora fix** — Moonblossoms / Glow Ferns no longer generate floating on top of bushes (they now require
  valid ground like every other patch).
- **A use for every mob drop** — the 12 formerly-useless drops now craft into useful items (hides→leather,
  sinew→string, algae→dye, membrane→phantom membrane, tooth→bone meal, plate→iron nuggets, scales→glow
  pollen, claw→echo dust, crystal dust→glasspetal block, clumps→moonloam) or brew (glowcap spores→Sporeblind).
- **Cooking-mod integration** — lumen foods/crops are tagged into the universal `#c:` convention tags that
  Farmer's Delight, Create, Delightful, and other cooking mods read.
- **Lumenwater fishing** — fishing in Lumenwater yields a unique bonus catch (native fish/flora/materials/
  treasure), including rare enchanted **"spell books"**.
- **Fished enchantments** — six new gear enchantments obtainable ONLY from Lumenwater fishing: Lightfooted,
  Nightsight, Lumenward (armor) and Glowbrand, Sporestrike, Rootbinding (weapons), applied at an anvil.
- **In-game guide** — an optional **Patchouli** guidebook that explains the whole dimension. Creative-only
  by design (grab it from the creative tab) so survival players explore and discover. The mod works without
  Patchouli installed.
- **Lumenwater = water** — also tagged `#c:water` so water-detection mods (e.g. a water-allergy race mod)
  treat it as water.
- Also fixed a pre-existing gap where several recipes (Ancient Door, Resonance Core) and recipe-book unlocks
  were defined in code but never shipped.

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
