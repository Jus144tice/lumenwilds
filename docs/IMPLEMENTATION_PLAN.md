# The Lumenwilds — Implementation Plan

A phased roadmap that turns the [world bible](world_description.txt) into a buildable
**NeoForge 1.21.1 / Java 21** mod. The bible is the *what* and *why*; this file is the *how* — every
dream feature is mapped to a concrete Minecraft/NeoForge mechanism, a repo seam (`File#symbol`), and a
phase.

> **How to read this.** Phases are ordered so each one produces a *playable, shippable* increment and
> unblocks the next. Within a phase, **Scope** lists the concrete work (with the API/JSON it uses),
> **Seams** names the existing classes/files it fills in, **Done when** is the smoke test, and
> **Risks** flags the parts that are genuinely hard in Minecraft. Keep this file current per
> [CLAUDE.md](../CLAUDE.md) Mandate 1.

---

## Guiding technical principles

1. **Datapack-first worldgen.** Dimension, dimension type, biomes, noise settings, configured/placed
   features, and structures are JSON under `src/main/resources/data/lumenwilds/worldgen/…`. The Java
   `ResourceKey`s already live in `world/` (`LumenDimensionConstants`, `LumenBiomeBootstrap`,
   `LumenConfiguredFeatures`, `LumenPlacedFeatures`). Code-driven worldgen (via `RegistrySetBuilder` /
   `BootstrapContext`) goes through the `world/LumenWorldgenBootstrap` seam **only** if a feature is too
   dynamic for JSON.
2. **Movement = vanilla attributes, not custom physics.** The 1.20.5+ attribute overhaul gives us
   `minecraft:generic.gravity`, `minecraft:generic.jump_strength`, `minecraft:generic.safe_fall_distance`,
   and `minecraft:generic.fall_damage_multiplier`. The entire "low gravity" fantasy is transient
   attribute modifiers applied on dimension enter and removed on exit — no mixins into movement math.
   (Verify exact registry names against `net.minecraft.world.entity.ai.attributes.Attributes` when wiring.)
3. **Strict client/server split.** Sky, fog, particles, the Veyra moon, and HUD overlays are
   **client-only** (`DimensionSpecialEffects`, `ParticleProvider`, `ViewportEvent`s) and must sit behind
   `Dist.CLIENT` event subscribers / a client init class — never referenced from common code. Game state
   (weather events, portal links, spawns) is **server-authoritative** and synced to clients with a
   NeoForge `PayloadRegistrar` packet.
4. **No new dependencies** without justification recorded here (incl. portal-helper libraries) — see
   [CLAUDE.md](../CLAUDE.md) Mandate 6. Portal logic stays in-house under `portal/`.
5. **Lumenbound Stone is the only portal frame** — never lodestone ([CLAUDE.md](../CLAUDE.md) Mandate 4).
6. **Phased, not overbuilt.** Each phase adds the minimum to make its slice work and leaves typed TODOs
   at the next seam. Keep `./gradlew build` green and smoke-test with `runData` / `runClient` / `runServer`.

---

## Cross-cutting tech map (bible system → mechanism → seam)

| Bible system | Minecraft / NeoForge mechanism | Repo seam |
| --- | --- | --- |
| Custom dimension + 1:1 scaling | `LevelStem` + `DimensionType` JSON (`coordinate_scale: 1.0`) | `world/LumenDimensionConstants`, `data/.../dimension*` |
| Portal frame + ignition | Custom block + `useOn` item; in-house shape/fill/teleport | `portal/*`, `item/LumenStrikerItem` |
| Low gravity / high jump / soft falls | `generic.gravity`, `generic.jump_strength`, `generic.safe_fall_distance`, `generic.fall_damage_multiplier` attribute modifiers on dim change | `effects/LowGravityHandler`, `event/PlayerDimensionEvents` |
| Flatter projectile arcs | Per-tick `deltaMovement` nudge on projectiles in-dimension (`EntityTickEvent.Pre`) | new `event/ProjectileArcHandler` |
| Sky / Veyra moon / dim daylight | Client `DimensionSpecialEffects` (`renderSky`, `getBrightnessDependentFogColor`) | new `client/LumenDimensionEffects` |
| Colored fog (per biome) | Biome `effects` JSON (`fog_color`, `sky_color`, `water_color`, `water_fog_color`) + `ViewportEvent` fine-tuning | `data/.../worldgen/biome/*`, client |
| Native light blocks | `BlockBehaviour.Properties#lightLevel` | `registry/ModBlocks` |
| Climbable vines/roots | `#minecraft:climbable` block tag (no code) | `data/minecraft/tags/block/climbable.json` |
| Lumenwater | NeoForge `FluidType` + `BaseFlowingFluid` + fluid block + bucket | `registry/ModFluids` |
| Trees / patches / ore | Configured + placed features (`TreeFeature`, `RandomPatchFeature`, `OreFeature`, custom `Feature`) | `world/LumenConfiguredFeatures`, `LumenPlacedFeatures`, `registry/ModFeatures` |
| Biome layout | `MultiNoiseBiomeSource` climate params + custom `noise_settings` (density functions, surface rules) | `data/.../worldgen/{biome,noise_settings}` |
| Mobs | `EntityType` + entity class + attributes + renderer/model + spawn placement | `registry/ModEntities`, new `entity/`, `client/` |
| Structures | Jigsaw (`StructureTemplatePool`) or `Structure`/`StructurePiece` + `.nbt` templates | `data/.../worldgen/structure*`, `data/.../structure/*.nbt` |
| Status effects | `MobEffect` (+ attribute modifiers) + `Potion` + brewing | `registry/ModMobEffects`, new `effect/`, brewing event |
| Weather events (Sporefall/Moonwake/Deep Hush) | Server `SavedData` tick manager → synced packet → client visuals/spawn modifiers | new `world/event/LumenEventManager` |
| Particles | `ParticleType` + client `ParticleProvider` | `registry/ModParticles`, client |
| Sounds / ambience | `SoundEvent` + biome `effects` ambient/mood/music + `.ogg` | `registry/ModSounds`, `data`/`assets` |
| Food | `DataComponents.FOOD` (`FoodProperties`) on items | `registry/ModItems` |
| Portal stabilization (Lumen Anchor) | Block + `BlockEntity` storing linked pos/dim | `registry/ModBlockEntities`, new `portal/LumenAnchor*` |

---

## Phase 1 — Scaffolding ✅ (complete)

The repo compiles, loads on client/server, registers all Phase-1 content, shows the creative tab, and
the Lumen Striker detects Lumenbound Stone and logs an activation attempt.

- Project scaffolding (NeoForge 1.21.1, Java 21, Gradle, Spotless, CI).
- Registries wired to the mod bus (`registry/Mod*`, see `Lumenwilds` ctor).
- Placeholder blocks/items + auto-populating creative tab (`ModCreativeTabs`).
- Lumenbound Stone frame block (`ModBlocks#LUMENBOUND_STONE`); Lumen Striker item (`LumenStrikerItem`).
- Dimension keys + placeholder dimension/dimension_type JSON (loads, reuses overworld noise).
- Datagen providers for current content (`datagen/*`).

**Done when:** ✅ `runClient` shows the tab; the striker logs on Lumenbound Stone; `runServer` loads the
placeholder dimension datapack without error.

---

## Phase 2 — Reach the dimension (portal + entry) ✅ (built; custom terrain deferred)

> **Status.** Done except the deliberately-deferred custom terrain. Implemented: frame detection
> (`LumenPortalShape`, a port of vanilla keyed on Lumenbound Stone), ignite + fill + durability
> (`LumenStrikerItem`, `LumenPortalManager`), the `Portal`-interface block (`LumenPortalBlock` with
> `AXIS`, `entityInside`→`setAsInsidePortal`), teleport + find-or-build return portal at 1:1 coords
> (`LumenPortalTeleporter`, `LumenPortalManager#getOrCreateExitPortal`), and the Entering/Leaving
> messages (`PlayerDimensionEvents`). Verified: `./gradlew build` green + dedicated server reaches
> `Done` with the dimension loaded. **Deferred to Phase 5:** the destination still uses placeholder
> terrain (overworld noise + fixed `minecraft:plains`); custom `noise_settings` + the real arrival biome
> land with the 7-biome work. The teal screen overlay is a Phase 7 client effect.

**Goal:** a player can build a Lumenbound Stone frame, strike it, walk through teal portal blocks, and
arrive in a real (even if plain) Lumenwilds — and come back.

### Scope
- **Portal shape detection.** Implement `portal/LumenPortalShape#findEmptyPortalShape` with Nether-style
  rules from the bible: internal opening **2×3 min, 21×21 max**, frame = `ModBlocks#LUMENBOUND_STONE`
  (`#isFrameBlock` already keys off it). Support both axes (X/Z). Model on vanilla
  `net.minecraft.world.level.portal.PortalShape` but bound to our frame block.
- **Ignite + fill.** `LumenStrikerItem#useOn` → `LumenPortalManager#tryActivatePortal` finds the shape and
  fills the interior with `ModBlocks#LUMEN_PORTAL` (set the portal block's `AXIS` state). Consume/damage
  the striker (decide: durability vs. flint-and-steel-style use cost — bible implies a reusable mystical
  tool, so give it durability).
- **Portal block behavior.** `portal/LumenPortalBlock`: real collision-less `#getShape`, ambient teal
  spore particles in `#animateTick` (client), and `#entityInside` → start the portal-dwell timer.
- **Teleport + dimension transition.** `portal/LumenPortalTeleporter#teleport` using
  `Entity#changeDimension(ServerLevel, DimensionTransition)` (1.21 API). On the destination side, locate
  or build a return frame at the **1:1-scaled** X/Z (bible: not a travel shortcut). Implement a portal
  finder + frame-builder fallback (place a Lumenbound Stone frame on safe ground if none found).
- **Transition polish.** Custom messages "**Entering the Lumenwilds**" / "**Leaving the Lumenwilds**"
  (action-bar or system message via the dimension-change event), and the screen-edge darken + teal spore
  overlay (client; can be a stub here, finished in Phase 6 alongside sky/fog).
- **Real (minimal) terrain.** Replace the placeholder dimension JSON: a dedicated
  `data/.../worldgen/noise_settings/lumenwilds.json` producing solid, walkable ground out of
  `moonstone`/`moonloam`, and a `MultiNoiseBiomeSource` with a **single** real Lumenwilds biome
  (`LumenBiomeBootstrap#LUMEN_MEADOW` → "Lumen Glade") so arrival isn't in a void. Full biome set is Phase 5.
- **Dimension type values.** Set `DimensionType`: `coordinate_scale 1.0`, `has_skylight true`,
  `has_ceiling false`, `natural true`, `bed_works false`, `respawn_anchor_works false`, sensible
  `min_y`/`height`/`logical_height`, raised `ambient_light` (dim floor so caves aren't pitch black), and
  a placeholder `effects` id (custom sky comes in Phase 6).

### Seams
`portal/LumenPortalShape`, `portal/LumenPortalManager`, `portal/LumenPortalBlock`,
`portal/LumenPortalTeleporter`, `item/LumenStrikerItem`, `world/LumenDimensionConstants`,
`world/LumenBiomeBootstrap`, `data/lumenwilds/{dimension,dimension_type,worldgen/*}`.

### Done when
`runServer` + `runClient`: build & strike a frame → portal fills → walking in teleports you to solid
ground in the Lumenwilds with the "Entering…" message → returning lands you near your origin with
"Leaving…". No hard errors mentioning `lumenwilds`.

### Risks
- **Return-portal placement on rugged terrain** is the classic Nether-portal pain point. Ship a simple
  "find-or-build" first; the bible's **Lumen Anchor** (Phase 8) is the real fix.
- `DimensionTransition`/`changeDimension` signatures shifted across 1.21.x — pin to 21.1.x and adapt.

---

## Phase 3 — Movement & gravity identity ✅ (built)

> **Status.** Done. `effects/LowGravityHandler` applies transient vanilla attribute modifiers on
> dimension enter/exit (`GRAVITY` ×0.7 — which alone yields the bible's ~1.75-block jump, so
> `JUMP_STRENGTH` is intentionally untouched; `SAFE_FALL_DISTANCE` +3; `FALL_DAMAGE_MULTIPLIER` −0.5),
> re-synced on login/respawn via `event/CommonEvents`. `event/ProjectileArcHandler` flattens
> arrow/throwable arcs (restores 40% of per-tick gravity) via `EntityTickEvent.Post`. `./gradlew build`
> green. **Not yet interactively playtested** (needs a `runClient` session). **Deferred:** native-mob
> gravity (Phase 6, via their attribute suppliers); Elytra left as emergent (revisit if testing shows
> boosting feels wrong). Values are first-pass and will be tuned in Phase 9.

**Goal:** the moment you step through, the world *feels* low-gravity — higher jumps, floaty falls, soft
landings — using vanilla attributes only.

### Scope
- **Gravity & jump.** On dimension enter, apply transient (non-persistent) attribute modifiers via
  `effects/LowGravityHandler` (driven by `event/PlayerDimensionEvents#onPlayerChangedDimension`):
  - `minecraft:generic.gravity` ×~0.6 (floaty, not flight) — `LUMENWILDS_GRAVITY_MULTIPLIER` already
    stubbed.
  - `minecraft:generic.jump_strength` to raise jump from ~1.25 → ~1.75 blocks.
  - `minecraft:generic.safe_fall_distance` ~3 → ~6 blocks; `minecraft:generic.fall_damage_multiplier`
    ×0.5 (bible: fall damage starts later **and** is halved).
  Remove all modifiers on exit; re-apply on login if logging in inside the dimension. Use stable modifier
  UUIDs/ids so they don't stack.
- **Native mobs obey low gravity.** Apply the gravity attribute to Lumenwilds entity types in their base
  attribute suppliers (Phase 6), not via the dimension hook, so Overworld mobs that wander through
  aren't affected unexpectedly. (Decide policy: bible says *native* mobs — keep it native-only.)
- **Flatter projectile arcs.** New `event/ProjectileArcHandler` on `EntityTickEvent.Pre`: for
  `Projectile`s whose level is the Lumenwilds, reduce the per-tick gravity component of `deltaMovement`
  (arrows, tridents, snowballs, eggs). Tunable constant; guard against affecting non-ballistic projectiles.
- **Elytra.** Reduced gravity already lengthens glides somewhat. Note as **emergent**; only add a
  `travel`-phase tweak if testing shows boosting feels wrong. Document the decision here.

### Seams
`effects/LowGravityHandler`, `event/PlayerDimensionEvents`, new `event/ProjectileArcHandler`.

### Done when
`runClient`: entering visibly raises jump height and softens falls; a 5-block drop deals no damage;
arrows fly flatter than in the Overworld; leaving restores vanilla movement exactly.

### Risks
- Confirm `jump_strength`/`safe_fall_distance`/`fall_damage_multiplier` apply to **players** in 21.1
  (they were generalized in 1.20.5); fall back to a `LivingFallEvent` hook for fall damage if an attribute
  misbehaves.
- Persisting/cleaning modifiers across death, respawn, and `/kill` — cover all of
  `PlayerLoggedInEvent`, `PlayerRespawnEvent`, and dimension-change.

---

## Phase 4 — Building blocks: full sets & materials ✅ (complete)

> **Update.** Signs, hanging signs, boats + chest boats, and axe-stripping are now in (a 4th commit),
> completing the bible's Glowwood set. Signs use a bespoke `ModWoodTypes.GLOWWOOD` WoodType/BlockSetType
> (the whole wood set now uses it), the four sign blocks reuse the vanilla SIGN/HANGING_SIGN block
> entities (added via `BlockEntityTypeAddBlocksEvent`), and `client.LumenwildsClient` registers the
> WoodType with `Sheets`. Boats reuse vanilla `Boat`/`ChestBoat` via a Glowwood `Boat.Type` added with
> NeoForge enum extension (`META-INF/enumextensions.json` + `ModBoatTypes`); the client registers the
> boat/chest-boat model layers and the vanilla `BoatRenderer` draws them. Axe-stripping is the NeoForge
> `strippables` data map. Verified: `./gradlew build` green + dedicated server reaches `Done` with no
> errors (server-load validated the enum extension, sign BEs, and the data map; two format bugs were
> caught + fixed there). **Still pending only a visual `runClient` check** of sign/boat rendering. ~81
> blocks total.

> **Status (initial 3 commits 4a/4b/4c), ~77 blocks.** **Glowwood wood set** (log/wood/
> stripped, planks, leaves, stairs/slab/fence/gate/door/trapdoor/button/plate — wood sounds reuse vanilla
> `OAK` types). **Moonstone set** (smooth/bricks/chiseled/tiles + stairs/slabs/walls). **Deep Moonstone**
> (deepslate-analog: cobbled/polished/bricks/tiles + shapes). **Shimmerstone set** (polished/bricks/tiles/
> pillar/glass + shapes). **Sporeglass** (block + pane, light 6). All with placeholder textures, recipes
> (smelting + 2×2 + stonecutter, wood-set crafts, sporeglass crafts), drop-self loot (slab/door special-
> cased), and mining tags. Datagen providers now dispatch by block type and the committed assets are the
> runData output, copied in. `./gradlew build` green; runData validates the full model/texture graph.
> **Deferred:** signs + hanging signs (need block-entity + client sign renderers + a bespoke Glowwood
> `WoodType`) and boats/chest boats (need an entity type + renderer + dispenser behaviour) — both are a
> self-contained follow-up. Stripping logs with an axe is also TODO. Not yet visually playtested.

**Goal:** a player who arrives can immediately gather and build — the "reward builders immediately"
mandate from the bible. This is breadth work: lots of vanilla block subclasses + assets.

### Scope
- **Glowwood wood set** (`RotatedPillarBlock` logs/wood + stripped, `StairBlock`, `SlabBlock`,
  `FenceBlock`, `FenceGateBlock`, `DoorBlock`, `TrapdoorBlock`, `ButtonBlock`, `PressurePlateBlock`,
  signs + **hanging signs**, boats + chest boats). Register a `WoodType` + `BlockSetType` (needed for
  sign/door/button sounds and hanging-sign block entities). Leaves emit light **2**; logs/planks emit
  **0** (bible balance). Glowwood Lamp (planks + glow pollen).
- **Moonstone stone set** (moonstone, cobbled, smooth, bricks, chiseled, tiles + stairs/slabs/walls).
  Smelting `moonstone → smooth_moonstone`; stonecutter recipes.
- **Deep Moonstone** (deepslate analog): slower mine, generates below a y-threshold (Phase 5 worldgen).
- **Shimmerstone set** (polished, bricks, pillar, tiles, + Shimmerstone Glass) — high-end blue-violet
  decorative stone.
- **Sporeglass** (block + pane, light **6**, animated speckle texture) crafted from glass + glow pollen +
  lumen crystal dust.
- **Moonloam line:** tilled moonloam (`FarmBlock` analog, hoe interaction), packed/decorative variants.
- **Resource storage blocks** already present: `LUMEN_CRYSTAL_BLOCK`; add Shimmerstone block, etc.
- **Assets & data for every block:** blockstate + model + item model + texture; loot table (drop-self,
  silk-touch where relevant); mining tags (`mineable/{pickaxe,axe,shovel}` + correct tool tier);
  flammability/`#minecraft:flammable` for wood; lang entries. Most regenerate via `datagen/*` providers
  (extend `ModBlockStateProvider`, `ModItemModelProvider`, `ModLootTableProvider`, `ModTagProvider`,
  `ModRecipeProvider`, `ModLanguageProvider`).

### Seams
`registry/ModBlocks`, `registry/ModItems`, `registry/ModBlockEntities` (signs/hanging signs),
`datagen/*`, `assets/lumenwilds/*`, `data/lumenwilds/*`, `data/minecraft/tags/block/*`.

### Done when
`runData` regenerates cleanly; in `runClient` the creative tab shows full wood/stone/decorative sets with
correct models, recipes craft, blocks drop and mine at the right tier, and hanging signs place/edit.

### Risks
- `WoodType`/`BlockSetType` must be registered **early** (before blocks that reference them) and on both
  sides for sign rendering. Boats need an `EntityType` + renderer + dispenser behavior — scope as a small
  sub-task.
- Texture volume is large; keep the strict palette (deep teal/cyan/indigo, no Overworld green) per the
  bible's color rules.

---

## Phase 5 — World content & generation (the 7 biomes) 🔶 (in progress)

> **Increment plan** (each committed + validated): **5a** foundation — bespoke terrain + arrival biome
> ✅; **5b** flora blocks + ore as features ✅; **5c** trees (Glowwood, then the Glowroot mega tree
> with root bridges); **5d** the remaining 6 biomes + a MultiNoise spread; **5e** Lumenwater fluid.
>
> **5b done.** Lumen Crystal Ore (`#LUMEN_CRYSTAL_ORE` + `#DEEP_LUMEN_CRYSTAL_ORE`, `DropExperienceBlock`,
> glow, drop shards; block↔shard + smelt/blast recipes), Moonblossom converted to a real `FlowerBlock`,
> new Glow Fern (`TallGrassBlock`) — both cross-model. Configured + placed features (`lumen_crystal_ore`,
> `patch_moonblossom`, `patch_glow_fern`) wired into Lumen Glade's ore + vegetal feature steps; lumen
> grass + moonloam added to `#minecraft:dirt` so the plants survive. Verified: build green + dedicated
> server loads the worldgen with no codec/feature errors.
>
> **5a done.** Custom `worldgen/noise_settings/lumenwilds.json` (moonstone default; terrain = a y-gradient
> + multi-octave `worldgen/noise/hills.json`, centred ~y88 with ponds at sea level 63) + surface rules
> layering lumen grass → moonloam → moonstone → deep moonstone (below ~y0). Single fixed biome
> `worldgen/biome/lumen_glade.json` (blue-green palette, blue grass/foliage override, vanilla cave
> carvers, no spawns/features yet). `world/LumenBiomeBootstrap` now holds the bible's 7 biome keys;
> `LumenDimensionConstants#LUMENWILDS_NOISE` added. Verified: `build` green + dedicated server loads the
> worldgen datapack with no codec errors + a client session generated Lumenwilds chunks with zero
> worldgen exceptions. Terrain *shape* tuning deferred until real models/textures make it judgeable.

**Goal:** the dimension reads as the bible's layered, vertical, bioluminescent world — real biomes,
terrain shape, surface materials, plants, trees, and ores.

### Scope
- **Plants & light flora** as blocks with correct light levels and placement:
  - Moonblossom (`FlowerBlock`-like, light 6), Lumenbulb (light 10), Glowvine
    (`GrowingPlantHeadBlock`/`GrowingPlantBodyBlock` like weeping vines, light 7, in `#climbable`),
    Hanging Glowroot (climbable), Giant Glowcap (stem/block/gills as `HugeMushroomBlock` set, gills
    light 9), Spore Pads (`LilyPadBlock`-like, light 3, slight bounce), Stillbloom (multi-block 3–8 tall,
    core light 12), Glow Algae, Lumen Reeds, glow ferns.
  - Mark light-emitters, set `PushReaction`, bonemeal behavior (`BonemealableBlock`) where the bible says
    "can be bonemealed."
- **Trees as features:**
  - **Glowwood Tree** — `TreeFeature` with custom trunk/foliage placers, leaves light 2.
  - **Glowroot Tree** — the mega tree: custom `Feature` (or large-tree config) in `registry/ModFeatures`
    (register it on the mod bus when the first feature lands — see `ModFeatures` note). Wide above-ground
    **root bridges** (light 5), glowing core (light 8), hanging glowvines.
  - **Bogroot** (Moonmire) tree/feature.
- **Ores:** `OreFeature` configured features for **Lumen Crystal Ore** (in moonstone + deep moonstone,
  drops Lumen Crystal Shard) and **Shimmerstone** veins (crags + deep caves).
- **Lumenwater fluid:** NeoForge `FluidType` + flowing/source + fluid block (light 4) + bucket; fish-like
  spawns later (Phase 6). **Overworld decay:** scheduled block tick converting placed Lumenwater to
  vanilla water after a delay when outside the dimension (bible anti-OP rule). Glow Pools = Lumenwater +
  dense flora placed features (landmark decorators, not a new fluid).
- **The 7 biomes** (`data/.../worldgen/biome/*`, keys in `LumenBiomeBootstrap` — expand beyond the
  current three): **Lumen Glade, Glowroot Forest, Moonmire, Sporefall Jungle, Glasspetal Crags,
  Undercrown Caverns, Stillbloom Basin.** Each gets distinct `effects` colors (fog/sky/water per the
  bible's fog table), vegetation feature lists, carvers, and spawn lists. Stillbloom Basin sets greatly
  reduced monster spawns; Undercrown is a cave biome.
- **Terrain shape & layering:** a real `noise_settings/lumenwilds.json` (density functions + noise router)
  for rolling hills, cliffs, ravines (crags), wetlands (mire), and large caverns (undercrown). **Surface
  rules** select: lumen grass over moonloam on the surface, moonstone below, **deep moonstone** below a
  `vertical_gradient` threshold (mirrors vanilla deepslate). Verticality (root bridges, floating
  fragments) comes from placed features layered on the base terrain.
- **Biome distribution** via `MultiNoiseBiomeSource` climate parameters mapping the 7 biomes across
  temperature/humidity/erosion/weirdness; Stillbloom Basin tuned **rare**, Undercrown on the cave layer.

### Seams
`world/LumenBiomeBootstrap`, `world/LumenConfiguredFeatures`, `world/LumenPlacedFeatures`,
`registry/ModFeatures` (+ bus registration in `Lumenwilds` ctor), `registry/ModFluids`,
`registry/ModBlocks`, `data/lumenwilds/worldgen/{biome,configured_feature,placed_feature,noise_settings,carver}`.

### Done when
`runServer` generates without error; flying around `runClient` shows the seven distinct biomes with their
palettes, glowing trees/plants providing real light, ore veins underground, deep moonstone at depth, and
Lumenwater pools that glow in-dimension and decay to water in the Overworld.

### Risks
- **Custom `noise_settings` (density functions) is the hardest, most error-prone worldgen.** Start by
  cloning vanilla overworld noise and mutating incrementally; validate each change with `runServer`.
- The mega Glowroot tree + root bridges likely need a fully custom `Feature` (placers can't express
  above-ground bridging) — budget time and isolate it.
- Consider authoring worldgen via `DatapackBuiltinEntriesProvider` datagen so JSON stays in sync with the
  `world/*` keys instead of hand-editing 30+ files.

---

## Phase 6 — Living world: mobs

**Goal:** the ecosystem from the bible — passive, neutral, and hostile mobs that reinforce "living light."

### Scope (per the bible roster)
- **Passive:** Lumen Grazer (six-legged herbivore; eats lumen grass, herds, breeds with Lumen Fruit,
  faint night glow), Lantern Beetle (flying; moving light; bottleable → Bottled Lantern Beetle item that
  places a temporary light block), Sky Jelly (floating air mob; `FlyingMoveControl`; drops Air Gel).
- **Neutral:** Rootback (large turtle-like; plants grow where it rests; drops Living Fiber/Rootback
  Plate), Glowmoth (attracted to light; aggro if you break nearby Moonblossoms/Stillblooms; drops Glow
  Scales).
- **Hostile:** Shade Stalker (ground ambush predator; spawns in low light; avoids bright natural light /
  Stillbloom Cores), Sporeling (swarm; spawns during Sporefall; death spore cloud → Sporeblind),
  Crag Wraith (flying; dive attacks in crags; knockback near ledges), Mirelurker (swamp lurker;
  anglerfish-style lure; stronger at night).
- **Per mob:** `EntityType` (`registry/ModEntities`), entity class (`entity/…` extending
  `Animal`/`PathfinderMob`/`Monster`/`FlyingMob`), attributes via `EntityAttributeCreationEvent`
  (native mobs get the low-gravity `generic.gravity` here per Phase 3), AI goals, drops (loot tables),
  spawn eggs, **client** model (`LayerDefinition` via `RegisterLayerDefinitions`) + renderer
  (`EntityRenderersEvent`), and spawn placement (`RegisterSpawnPlacementsEvent`) + biome `spawners` lists.
- **Light-aware behavior** is the throughline: Shade Stalker light avoidance, Glowmoth attraction,
  beetles around Moonblossoms — encode as custom `Goal`s referencing block light / nearby blocks.

### Seams
`registry/ModEntities`, new `entity/` package, new `client/` renderers+models, loot tables under
`data/lumenwilds/loot_table/entities/`, biome `spawners`.

### Done when
Each mob spawns in its intended biome/conditions, renders correctly, exhibits its signature behavior
(grazers flee & herd, beetles glow & bottle, shade stalkers shun light, sporelings swarm and cloud on
death), drops its loot, and breeds/tames where specified.

### Risks
- Flying/floating navigation (Sky Jelly, Crag Wraith, Glowmoth) needs custom move controllers — model on
  `Bat`/`Phantom`/`Allay`. Vertical low-gravity drift for Sky Jelly is bespoke.
- Model/animation volume is large; consider a shared base model + tint layers to hold the palette.

---

## Phase 7 — Atmosphere: sky, fog, particles, sound, weather events

**Goal:** the sensory identity — endless dim twilight, the giant moon **Veyra**, colored fog, drifting
spores, and the signature ambient events. Mostly client rendering + a server event manager.

### Scope
- **Custom sky** via a client `DimensionSpecialEffects` (registered through
  `RegisterDimensionSpecialEffectsEvent`) bound to `lumenwilds:lumenwilds` (new
  `client/LumenDimensionEffects`): deep-turquoise-to-indigo sky gradient, **weak blurred sun** (no harsh
  shadows), and **Veyra** — an oversized pale blue-white moon rendered every cycle. Override `renderSky`
  for the custom celestial bodies; set fog/sky colors.
- **"Never bright noon" + dim daylight.** Shape perceived brightness through `DimensionSpecialEffects`
  fog/brightness and raised `ambient_light`; accept that *skylight values* still peak at 15 at noon
  (engine limit) — the **look** is controlled by sky/fog, and mob-spawn darkness is tuned via
  `monster_spawn_light_level`. Document this clearly so expectations match the engine.
- **Colored fog** per biome from biome `effects` JSON (Phase 5) plus `ViewportEvent.ComputeFogColor` /
  `RenderFog` for night-teal / cave-cyan / crystal blue-violet shifts.
- **Particles** (`registry/ModParticles` + client `ParticleProvider` via `RegisterParticleProvidersEvent`):
  portal teal spores, drifting pollen, Sporefall spores, Lantern-Beetle trails, crystal shimmer.
- **Sounds** (`registry/ModSounds` + `assets/.../sounds.json` + `.ogg`): striker/portal SFX, mob sounds,
  and biome `ambient_sound`/`mood_sound`/`additions_sound`/`music` (insect chorus, root groans, swamp
  bubbling, crystal resonance, distant whale-calls) so the dimension is identifiable with eyes closed.
- **Ambient events** via a server `SavedData` manager (new `world/event/LumenEventManager`) ticking the
  dimension and syncing state to clients with a `PayloadRegistrar` packet:
  - **Sporefall** (Sporefall Jungle): drifting spore particles, slight sky darken, plants pulse brighter,
    boosted Sporeling spawns, occasional bonemeal-tick growth, muffled soundscape.
  - **Moonwake** (rare night): brighter Veyra, brighter Lumenwater, more Lantern Beetles, stronger
    Moonblossom light, calmer passives, fewer surface Shade Stalkers.
  - **Deep Hush** (underground): quieter ambience, slow root-light pulses, slightly more hostiles, low
    distant tones.
- **The day cycle.** Bible asks for a **48,000-tick** cycle (twice Overworld). `DimensionType` has no
  day-length field, so this needs a server-side time controller in `LumenEventManager` that advances the
  Lumenwilds' apparent time at half rate. **Flag the caveats:** time is global per level and interacts
  with sleeping, daylight sensors, and mob spawn timing — pick one of: (a) half-rate custom time with
  documented side effects, or (b) ship Phase 7 at normal cycle length and treat 48k as a stretch goal.
  Decide and record the choice here before building it.

### Seams
new `client/LumenDimensionEffects` + particle/sound client init, `registry/ModParticles`,
`registry/ModSounds`, new `world/event/LumenEventManager` + networking payload, biome `effects`,
`data/lumenwilds/dimension_type` (`effects` id, `ambient_light`, `monster_spawn_light_level`).

### Done when
Entering shows the custom dim-twilight sky with Veyra and colored fog; particles and ambient sounds play
per biome; Sporefall/Moonwake/Deep Hush trigger their visual + gameplay changes; (if chosen) the day runs
at the slower cadence.

### Risks
- Sky rendering APIs are the most version-fragile client code; pin to 21.1 and keep it isolated.
- The 48k cycle is the single trickiest "dream vs. engine" item — see the explicit decision point above.

---

## Phase 8 — Structures, loot, food, brewing & portal stabilization

**Goal:** reasons to explore and a survivable, progression-rich loop.

### Scope
- **Structures** (overgrown/organic, never village-like):
  - **Lumenbound Ruins** (ruined portal sites: partial Lumenbound Stone frames, broken Lumen Portal
    remnants, striker-ingredient chests, lore) — the in-world explanation of the portal.
  - **Rootshrines** (small, inside giant roots; early reward).
  - **Glasspetal Spires** (crag crystal towers; Crag-Wraith-guarded).
  - **Undercrown Relics** (deep dungeon-like halls; spawners, rare loot, portal-stabilization parts).
  - Implement as Jigsaw (`StructureTemplatePool` + `.nbt` templates under `data/lumenwilds/structure/`)
    or `Structure`/`StructurePiece` for the irregular ones; add `structure` + `structure_set` JSON and
    biome tags. Author templates with the structure block, export `.nbt`.
- **Loot tables** for all structures and remaining mobs (`data/lumenwilds/loot_table/…`).
- **Food** (`DataComponents.FOOD`): Lumen Fruit (moderate hunger, brief weak night vision), Grazer Meat
  (raw/cooked), Glowcap Stew (bowl + glowcap + lumen fruit + moonblossom → hunger + night vision),
  Lumen Nectar (collected from Stillblooms with a bottle; hunger + brief soothing/regen).
- **Status effects + brewing** (`registry/ModMobEffects` + new `effect/` + `Potion`s +
  `RegisterBrewingRecipesEvent`):
  - **Lightfoot** (+jump, −fall damage, +air control via attribute modifiers; from Air Gel / Lumen
    Nectar brews),
  - **Glowmarked** (target glows / easier to see; from glow-pollen splash, Glowmoth scales),
  - **Sporeblind** (−visibility overlay + slight slow; from Sporeling clouds / Sporefall),
  - **Rooted** (−movement, −jump, no sprint; from root traps / Shade Stalker hits).
- **Portal stabilization — Lumen Anchor** (bible): block + `BlockEntity` (`registry/ModBlockEntities`,
  new `portal/LumenAnchorBlock` + `LumenAnchorBlockEntity`) crafted from Lumenbound Stone + Lumen Crystal
  Block + Shimmerstone + Echo Shard; **directly links two portals** (stores target pos/dim), removing
  rugged-terrain return placement problems. Integrate with `LumenPortalTeleporter` so a linked anchor
  overrides the find-or-build fallback.

### Seams
`data/lumenwilds/worldgen/{structure,structure_set,template_pool,processor_list}` +
`data/lumenwilds/structure/*.nbt`, `registry/ModMobEffects`, new `effect/`, `registry/ModItems`
(food components), `registry/ModBlockEntities`, new `portal/LumenAnchor*`, brewing registration event.

### Done when
Structures generate with themed loot; the four foods restore hunger and apply their effects; the four
status effects apply from their sources and behave per the bible; a Lumen Anchor links two portals so
return travel lands precisely.

### Risks
- Jigsaw + processors have a steep authoring curve; start with one simple Rootshrine end-to-end before
  the multi-piece Undercrown Relics.
- Glowmarked overlapping vanilla `glowing` and Sporeblind overlapping `darkness`/`blindness` — decide
  reuse vs. custom render to avoid conflicts.

---

## Phase 9 — Polish, balance & compat

**Goal:** ship-quality.

### Scope
- Full **datagen coverage** so assets/recipes/loot/tags/lang/advancements regenerate from code; reconcile
  with hand-authored placeholders (datagen output stays **off** the resource path per CLAUDE.md).
- **Advancements** tracking the bible's progression (first portal, first native light, reach each biome,
  Lumen Anchor, Stillbloom Core).
- **Balance pass:** light levels, mob spawn rates/difficulty curve (beautiful-first then dangerous),
  food/effect values, ore rarity, tree/structure frequency.
- **Compat & config:** JEI (recipe/info), config options (event frequencies, gravity strength toggle,
  cycle length), and verify no clashes with common mods.
- **Performance:** profile worldgen (mega trees, density functions), particle/sky cost, and event-manager
  ticking.
- **Sound/texture finalization:** replace flat-color placeholders with final art; final ambience mix.

### Done when
`./gradlew build` green; advancements fire; JEI shows recipes; configs work; a full playthrough from
portal to in-dimension base matches the bible's progression and "screenshots you want to share" bar.

---

## Known hard problems (read before estimating)

| Problem | Why it's hard | Plan |
| --- | --- | --- |
| **48,000-tick day** | No `DimensionType` field for day length; time is global per level and tied to sleep/sensors/spawns | Custom half-rate time controller in `LumenEventManager` **with documented side effects**, or ship normal-length and treat 48k as stretch (decide in Phase 7) |
| **"Never bright noon" / dim daylight** | Engine skylight still peaks at 15 at noon; can't cap propagation cheaply | Control the *look* via `DimensionSpecialEffects` + fog + `ambient_light`; tune spawns via `monster_spawn_light_level`; set expectations |
| **Custom sky + Veyra moon** | Sky rendering APIs are version-fragile client code | Isolate in `client/LumenDimensionEffects`, pin to NeoForge 21.1.x |
| **Mega Glowroot tree + root bridges** | Trunk/foliage placers can't express above-ground bridging | Fully custom `Feature` in `registry/ModFeatures` |
| **Custom noise_settings / density functions** | Most error-prone worldgen; easy to break terrain | Clone vanilla overworld noise, mutate incrementally, validate per change with `runServer` |
| **Flatter projectile arcs / elytra** | Gravity attribute doesn't apply to non-living projectiles or elytra glide math | Per-tick `deltaMovement` nudge for projectiles; treat elytra as emergent unless testing demands a `travel` hook |
| **Lumenwater Overworld decay** | Needs reliable cross-dimension conversion without lag | Scheduled block tick → vanilla water when outside the dimension |
| **Portal return on rugged terrain** | Classic Nether-portal placement failure | Find-or-build fallback first; **Lumen Anchor** as the real fix (Phase 8) |

---

## Design notes

**Portal frame material.** The portal frame is **Lumenbound Stone** (crafted from Overworld stonework +
amethyst resonance + Nether glowstone — mid-game, not netherite-expensive), never vanilla lodestone
([CLAUDE.md](../CLAUDE.md) Mandate 4). The bible, Lumenbound Ruins, and Lumen Anchor are all reconciled to
Lumenbound Stone; see [LUMENWILDS_WORLD_DEFINITION.md](LUMENWILDS_WORLD_DEFINITION.md) for the deltas.

**Lumen Striker recipe.** Amethyst shard + iron ingot + glow ink sac (no echo shard — not Deep-Dark-locked).

**Portal helper libraries.** We may evaluate a third-party custom-portal library later; for now we add
**no** such dependency and keep portal logic in-house under `portal/`.

**Phasing rationale.** Phases 2–3 deliver the *core fantasy* (reach it, feel the gravity) as the smallest
playable slice. Phase 4 front-loads building blocks because the bible insists builders be rewarded
immediately. Phases 5–6 fill the world and ecosystem; 7 adds the sensory soul; 8 adds depth and
progression; 9 polishes. Each phase is independently shippable.
