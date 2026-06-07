# CLAUDE.md — The Lumenwilds

> AI navigation map for this repo. Read this **first**; it is built to answer "where does X live?"
> without grepping. Keep it terse, accurate, and current (see [Mandates](#mandates)).

## What this mod is (and does)

**The Lumenwilds** is a **custom-dimension mod for NeoForge 1.21.1 / Java 21** (mod id `lumenwilds`,
package `com.jus144tice.lumenwilds`). The Lumenwilds is an alien, bioluminescent dimension of dim
twilight, bright moonlight, blue grass, glowing plants, low gravity, and native living light — reached
through a player-built portal.

The portal's defining rule: the frame is a **custom block, Lumenbound Stone** (`lumenwilds:lumenbound_stone`),
**never vanilla lodestone** (lodestone is netherite-gated, too expensive for this mid-game dimension).
The portal is lit with the **Lumen Striker** (`lumenwilds:lumen_striker`), its interior is the **Lumen
Portal** block (`lumenwilds:lumen_portal`), and the destination is **`lumenwilds:lumenwilds`**.

**Current state = Phase 2 (portal + dimension entry) working on Phase 1 scaffolding.** It compiles,
loads on client/server, registers all content, and shows the creative tab. **The portal works
end-to-end:** the Lumen Striker ignites a Lumenbound Stone frame (real frame detection + interior fill),
and stepping through teleports the player to `lumenwilds:lumenwilds` and back, find-or-building a return
portal at 1:1-scaled coordinates, with "Entering/Leaving the Lumenwilds" messages. The destination still
uses **placeholder terrain** (vanilla overworld noise + a fixed `minecraft:plains` biome) — solid ground
to arrive on, but the 7 custom biomes/terrain are Phase 5. What is deliberately *not* built yet: custom
terrain/biomes, low-gravity movement (Phase 3), mobs, structures, fluids, custom sky/fog. Those are
stubbed with TODOs. Roadmap:
[docs/IMPLEMENTATION_PLAN.md](docs/IMPLEMENTATION_PLAN.md). Design source of truth: the world bible at
[docs/world_description.txt](docs/world_description.txt), indexed by
[docs/LUMENWILDS_WORLD_DEFINITION.md](docs/LUMENWILDS_WORLD_DEFINITION.md).

---

## Mandates

Rules for any AI (or human) working in this repo. Follow them every session.

### 1. This file is self-updating — never defer
If you **materially change the codebase or anything this file describes**, you **must update CLAUDE.md
in the same session/turn that makes the change** — never defer to a "later" or future session. Material
changes include: adding/renaming/removing a class, registered object (block/item/tab/etc.), public
method, recipe, resource convention, version, build step, or registry wiring. After such a change, fix
the affected entry in [Codebase map](#codebase-map) and any other section it touches before ending your
turn. A reader must never be misled by this file. Pure formatting/comment typos are exempt. When unsure,
update.

### 2. Use anchors, not line numbers
Reference code as `File#symbol` (class, method, or field name) and link to files/section headers — never
line numbers (they rot and don't scale). This file and all nav docs follow that rule.

### 3. Optimize for minimal tokens / greps / finds
The point of this file is that you can locate any feature without searching. Prefer a precise pointer
(`ModBlocks#LUMENBOUND_STONE`) over pasting code. If you find yourself grepping for something that should
be findable here, add it here.

### 4. Lumenbound Stone is the portal frame — never lodestone
Do not reintroduce vanilla lodestone as the portal frame anywhere (code, recipes, or docs). The frame is
[ModBlocks#LUMENBOUND_STONE](src/main/java/com/jus144tice/lumenwilds/registry/ModBlocks.java). (Lodestone
is not used anywhere in the design after the bible cleanup.)

### 5. Keep it compiling + verify
`./gradlew build` must stay green. Spotless auto-formats on build (palantir, 4-space/120-col) — don't
hand-fight it; run `./gradlew spotlessApply`. New `.java` files keep the short Apache header comment.
For runtime-affecting changes, smoke-test with `runData` (registration/assets) and/or `runServer`
(datapack/dimension load) — see [Build & verify](#build--verify).

### 6. Don't overbuild ahead of the plan
This is phased scaffolding. Add the minimum that the current phase needs; leave TODOs where the next
phase belongs. No new dependencies (incl. portal-helper libraries) without justification — noted in
[docs/IMPLEMENTATION_PLAN.md](docs/IMPLEMENTATION_PLAN.md).

---

## Codebase map

Package root `com.jus144tice.lumenwilds` →
[src/main/java/com/jus144tice/lumenwilds/](src/main/java/com/jus144tice/lumenwilds/). Reference members
as `File#member`.

### Entry point
- [Lumenwilds.java](src/main/java/com/jus144tice/lumenwilds/Lumenwilds.java) — `@Mod` class.
  `#MOD_ID` (`"lumenwilds"`), `#MOD_NAME` (`"The Lumenwilds"`), `#LOGGER`. Ctor registers these
  DeferredRegisters to the **mod bus**: `ModSounds`, `ModParticles`, `ModMobEffects`, `ModFluids`,
  `ModBlocks`, `ModItems`, `ModBlockEntities`, `ModEntities`, `ModMenus`, `ModCreativeTabs`. `#onCommonSetup`
  logs only. **When you add a new (non-empty) DeferredRegister, register it here.**

### registry/ — all registered content
- [ModBlocks.java](src/main/java/com/jus144tice/lumenwilds/registry/ModBlocks.java) — `#BLOCKS`
  (`DeferredRegister.Blocks`). 13 blocks: `#LUMENBOUND_STONE` (portal frame), `#LUMEN_PORTAL`
  (`LumenPortalBlock`, non-solid/glowing), `#MOONLOAM`, `#LUMEN_GRASS_BLOCK`, `#MOONSTONE`,
  `#COBBLED_MOONSTONE`, `#GLOWWOOD_LOG`, `#GLOWWOOD_PLANKS`, `#GLOWROOT_LOG`, `#GLOWVINE`, `#MOONBLOSSOM`,
  `#LUMENBULB`, `#LUMEN_CRYSTAL_BLOCK`. **Add a block here → add its BlockItem in `ModItems` → asset +
  loot (see [Resources](#resources--srcmainresources)).**
- [ModItems.java](src/main/java/com/jus144tice/lumenwilds/registry/ModItems.java) — `#ITEMS`
  (`DeferredRegister.Items`). Standalone: `#LUMEN_STRIKER` (`LumenStrikerItem`, **durable: `stacksTo(1)
  .durability(64)`** — each ignition costs 1 use), `#LUMEN_CRYSTAL_SHARD`, `#GLOW_POLLEN`,
  `#LIVING_FIBER`, `#LUMEN_FRUIT`, `#LUMEN_NECTAR`, `#AIR_GEL`. Plus a `registerSimpleBlockItem` (returns
  `DeferredItem<BlockItem>`) for every block **except `LUMEN_PORTAL`**.
- [ModCreativeTabs.java](src/main/java/com/jus144tice/lumenwilds/registry/ModCreativeTabs.java) —
  `#CREATIVE_MODE_TABS`, `#LUMENWILDS_TAB` (id `lumenwilds`, title key `itemGroup.lumenwilds`, icon =
  Lumen Striker). **Auto-populates from `ModItems.ITEMS`** — new items appear without editing this file.
- Empty stubs (compile; carry phase TODOs). Wired to the bus already (registered empty): 
  [ModFluids](src/main/java/com/jus144tice/lumenwilds/registry/ModFluids.java) `#FLUIDS`,
  [ModMobEffects](src/main/java/com/jus144tice/lumenwilds/registry/ModMobEffects.java) `#MOB_EFFECTS`,
  [ModEntities](src/main/java/com/jus144tice/lumenwilds/registry/ModEntities.java) `#ENTITIES`,
  [ModBlockEntities](src/main/java/com/jus144tice/lumenwilds/registry/ModBlockEntities.java) `#BLOCK_ENTITIES`,
  [ModMenus](src/main/java/com/jus144tice/lumenwilds/registry/ModMenus.java) `#MENUS`,
  [ModSounds](src/main/java/com/jus144tice/lumenwilds/registry/ModSounds.java) `#SOUNDS`,
  [ModParticles](src/main/java/com/jus144tice/lumenwilds/registry/ModParticles.java) `#PARTICLES`.
- [ModFeatures.java](src/main/java/com/jus144tice/lumenwilds/registry/ModFeatures.java) — `#FEATURES`
  (custom `Feature` types). Empty and **intentionally NOT bus-registered** (register in `Lumenwilds` ctor
  when the first feature is added).
- [ModBiomes.java](src/main/java/com/jus144tice/lumenwilds/registry/ModBiomes.java) /
  [ModDimensions.java](src/main/java/com/jus144tice/lumenwilds/registry/ModDimensions.java) — thin
  re-exports of the worldgen/dimension `ResourceKey`s from `world/` (worldgen is datapack-driven, not a
  DeferredRegister).

### portal/ — portal mechanics (working, Phase 2)
- [LumenPortalBlock.java](src/main/java/com/jus144tice/lumenwilds/portal/LumenPortalBlock.java) — `Block`
  **implements `net.minecraft.world.level.block.Portal`**. `#CODEC`, `#codec`, `#AXIS`
  (`HORIZONTAL_AXIS` state), per-axis `#getShape`, `#entityInside` → `Entity#setAsInsidePortal(this,pos)`
  (engine drives dwell/teleport/cooldown), `#getPortalTransitionTime` (players 80t, else 0),
  `#getPortalDestination` (resolves overworld↔lumenwilds, 1:1-scaled target, delegates placement),
  `#getLocalTransition` = `NONE` (no Nether nausea), `#animateTick` (teal `GLOW` spores, placeholder).
- [LumenPortalShape.java](src/main/java/com/jus144tice/lumenwilds/portal/LumenPortalShape.java) — frame
  detection, a focused port of vanilla `PortalShape` keyed on **our** frame/interior (NOT the shared
  NeoForge `isPortalFrame` predicate — see [gotchas](#invariants--gotchas)). `#MIN/MAX_WIDTH`,
  `#MIN/MAX_HEIGHT`, `#isFrameBlock` (`ModBlocks.LUMENBOUND_STONE`), `#findEmptyPortalShape(level,seed,
  axis)` (tries both axes), `#isValid`, `#axis`, `#createPortalBlocks` (fills interior with `LUMEN_PORTAL`).
- [LumenPortalManager.java](src/main/java/com/jus144tice/lumenwilds/portal/LumenPortalManager.java) —
  `#tryActivatePortal(level, seed)`: validate empty frame → fill → ignition sound. `#getOrCreateExitPortal`
  + `#ExitPortal` record: find an existing `LUMEN_PORTAL` near the scaled destination, else build a 2×3
  frame at the surface. (Vanilla `PortalForcer` is NOT reused — it's obsidian/Nether-POI only.)
- [LumenPortalTeleporter.java](src/main/java/com/jus144tice/lumenwilds/portal/LumenPortalTeleporter.java) —
  `#createDestinationTransition(target, entity, approx, axis)`: find/build the exit portal and return a
  `DimensionTransition` placing the entity collision-free at the opening base (zeroed momentum).

### item/
- [LumenStrikerItem.java](src/main/java/com/jus144tice/lumenwilds/item/LumenStrikerItem.java) — `#useOn`:
  on a Lumenbound Stone frame, seeds detection from the air at the clicked face (fallback: block above),
  delegates to `LumenPortalManager#tryActivatePortal`; on success consumes 1 durability via
  `hurtAndBreak`. Returns `PASS` on non-frame blocks. **Never checks lodestone.**

### world/ — dimension & worldgen keys (datapack-driven)
- [LumenDimensionConstants.java](src/main/java/com/jus144tice/lumenwilds/world/LumenDimensionConstants.java)
  — **canonical** keys. `#DIMENSION_PATH` (`"lumenwilds"`), `#LUMENWILDS_LEVEL` (`ResourceKey<Level>`),
  `#LUMENWILDS_STEM` (`LevelStem`), `#LUMENWILDS_DIM_TYPE` (`DimensionType`).
- [LumenBiomeBootstrap.java](src/main/java/com/jus144tice/lumenwilds/world/LumenBiomeBootstrap.java) —
  biome keys `#LUMEN_MEADOW`, `#GLOWING_GROVE`, `#MOONLIT_BARRENS`.
- [LumenConfiguredFeatures.java](src/main/java/com/jus144tice/lumenwilds/world/LumenConfiguredFeatures.java)
  — `#GLOWWOOD_TREE`, `#MOONBLOSSOM_PATCH`, `#LUMEN_CRYSTAL_ORE`.
- [LumenPlacedFeatures.java](src/main/java/com/jus144tice/lumenwilds/world/LumenPlacedFeatures.java) —
  `#GLOWWOOD_TREE_PLACED`, `#MOONBLOSSOM_PATCH_PLACED`, `#LUMEN_CRYSTAL_ORE_PLACED`.
- [LumenWorldgenBootstrap.java](src/main/java/com/jus144tice/lumenwilds/world/LumenWorldgenBootstrap.java)
  — empty seam for code-generated worldgen (`RegistrySetBuilder`/`BootstrapContext`) if we leave JSON.

### effects/
- [LowGravityHandler.java](src/main/java/com/jus144tice/lumenwilds/effects/LowGravityHandler.java) —
  `#LUMENWILDS_GRAVITY_MULTIPLIER`, `#isInLumenwilds(entity)`, `#onChangedDimension(player)` (logs;
  Phase 4 plan = transient `minecraft:generic.gravity` attribute modifier on enter/leave).

### event/ — `@EventBusSubscriber` (game bus), auto-registered
- [CommonEvents.java](src/main/java/com/jus144tice/lumenwilds/event/CommonEvents.java) —
  `#onPlayerLoggedIn` (debug log).
- [PlayerDimensionEvents.java](src/main/java/com/jus144tice/lumenwilds/event/PlayerDimensionEvents.java) —
  `#onPlayerChangedDimension` → forwards to `LowGravityHandler#onChangedDimension`.

### util/
- [ResourceLocationHelper.java](src/main/java/com/jus144tice/lumenwilds/util/ResourceLocationHelper.java)
  — `#modLoc(path)` (`lumenwilds:`), `#mcLoc(path)` (`minecraft:`). Use these for all `ResourceLocation`s.

### datagen/ — `./gradlew runData` → `src/generated/resources` (git-ignored, OFF the resource path)
- [DataGenerators.java](src/main/java/com/jus144tice/lumenwilds/datagen/DataGenerators.java) —
  `@EventBusSubscriber(bus = MOD)`, `#gatherData(GatherDataEvent)` wires the six providers below.
- [ModBlockStateProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModBlockStateProvider.java) —
  cube_all blockstate+model for every block (`#registerStatesAndModels`).
- [ModItemModelProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModItemModelProvider.java) —
  block items inherit the block model; others `basicItem` (`#registerModels`).
- [ModLanguageProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModLanguageProvider.java) —
  auto names from registry paths (`#addTranslations`, `#titleCase`) + the tab title.
- [ModRecipeProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModRecipeProvider.java) —
  `#buildRecipes`: Lumenbound Stone (`CGC/SAS/CGC`) + Lumen Striker (`I/A/G`).
- [ModLootTableProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModLootTableProvider.java) —
  `#create` + inner `ModBlockLoot` (drop-self for all blocks except `LUMEN_PORTAL`).
- [ModTagProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModTagProvider.java) —
  `#addTags`: `mineable/pickaxe|axe|shovel`.

> NOTE: hand-authored placeholder assets in `src/main/resources` are **authoritative** (the mod works
> from a plain `build`, no datagen needed). `runData` output is a regeneration/diff aid only; it is NOT
> on the resource path, so it can't duplicate-clash with the committed assets. Copy anything worth
> keeping into `src/main/resources`.

## Resources — `src/main/resources`

- [META-INF/neoforge.mods.toml](src/main/resources/META-INF/neoforge.mods.toml) — mod metadata; only
  `neoforge` + `minecraft` deps (required). `pack.mcmeta` → `pack_format` 48.
- `assets/lumenwilds/`: `blockstates/`, `models/block|item/`, `textures/block|item/` (flat-colour 16px
  placeholders), `lang/en_us.json` (display names + `itemGroup.lumenwilds` + portal transition messages
  `lumenwilds.portal.{entering,leaving}`).
- `data/lumenwilds/`: `recipe/{lumenbound_stone,lumen_striker}.json`, `loot_table/blocks/*` (drop-self),
  `dimension/lumenwilds.json` + `dimension_type/lumenwilds.json` (placeholder — reuses overworld noise +
  fixed plains biome; valid & loads), `worldgen/README.md` (future-home note).
- `data/minecraft/tags/block/mineable/{pickaxe,axe,shovel}.json`.

## Adding content — quick recipes

| Task | Touch (in order) |
| --- | --- |
| **Add a block** | `ModBlocks#<NEW>` → BlockItem in `ModItems` → `assets/.../blockstates/<n>.json` + `models/block/<n>.json` + `models/item/<n>.json` + `textures/block/<n>.png` → `data/lumenwilds/loot_table/blocks/<n>.json` → mining tag in `data/minecraft/tags/block/mineable/*` → lang `block.lumenwilds.<n>` → update [Codebase map](#codebase-map). (Datagen regenerates assets/loot/tags/lang if you run `runData`.) |
| **Add an item** | `ModItems#<NEW>` → `models/item/<n>.json` + `textures/item/<n>.png` → lang `item.lumenwilds.<n>` (auto in datagen) → it auto-joins the creative tab. |
| **Add a recipe** | `data/lumenwilds/recipe/<n>.json` (result key is `{"id":…,"count":…}`) and/or `ModRecipeProvider#buildRecipes`. |
| **Add a non-empty registry** | create/populate `Mod*`, then register it on the mod bus in `Lumenwilds` ctor. |
| **Wire dimension/worldgen** | keys in `world/*`; JSON under `data/lumenwilds/{dimension,dimension_type,worldgen/...}`. |

---

## Invariants & gotchas

- **1.21.1 datapack folders are singular**: `recipe/`, `loot_table/`, `dimension/`, `dimension_type/`,
  `tags/block/`. Pack format **48**. Recipe result uses `{"id":…,"count":…}` (not `"item":`).
- **`DeferredRegister.getEntries()` yields `DeferredHolder`**, not `DeferredBlock`/`DeferredItem` —
  iterate with `var`. `registerSimpleBlockItem(...)` returns `DeferredItem<BlockItem>`.
- **`@EventBusSubscriber(bus = Bus.MOD)` warns "deprecated for removal"** — still the correct route for
  `GatherDataEvent` on 21.1.233; harmless.
- **Datagen output is OFF the resource path on purpose** (no `srcDir 'src/generated/resources'`) so it
  can't clash with committed hand-authored assets.
- **Empty stub registries with no entries are still bus-registered** (harmless) except `ModFeatures`,
  which is left unwired until it has an entry.
- **Portal uses the 1.21 `Portal` interface, not manual teleport.** `LumenPortalBlock implements
  net.minecraft.world.level.block.Portal`; `entityInside` calls `Entity#setAsInsidePortal(this,pos)` and
  the engine handles dwell timer + `Entity#changeDimension(DimensionTransition)` + post-teleport cooldown.
- **Do NOT reuse vanilla `PortalShape`/`PortalForcer` for the Lumen portal.** Their frame predicate is
  NeoForge's shared `IBlockStateExtension#isPortalFrame` (default obsidian) and the forcer is bound to the
  `nether_portal` POI — overriding `isPortalFrame` on Lumenbound Stone would let it ignite Nether portals.
  `LumenPortalShape` is our own port keyed explicitly on `LUMENBOUND_STONE` + `LUMEN_PORTAL`.
- **Exact MC API signatures** for portal work (`Portal`, `DimensionTransition`, `PortalShape`) were taken
  from the decompiled sources in the NeoForm cache: `~/.gradle/caches/neoformruntime/intermediate_results/
  sourcesAndCompiledWithNeoForge_*_output.jar`. Extract from there (not the `neoforge-*-sources.jar`,
  which holds only NeoForge's own classes) when you need a vanilla signature.

---

## Build & verify

Versions in [gradle.properties](gradle.properties): MC `1.21.1`, NeoForge `21.1.233`, Java 21, Gradle
8.10, ModDevGradle `2.0.141`, Spotless `6.25.0`. License **Apache-2.0**. `JAVA_HOME` → a JDK 21.

```bash
export JAVA_HOME="/c/Program Files/Java/jdk-21"
./gradlew build            # spotlessApply -> compile -> (tests) -> jar  (build/libs/lumenwilds-<ver>.jar)
./gradlew runData          # regenerate placeholder assets -> src/generated/resources (git-ignored)
./gradlew runClient        # dev client: "The Lumenwilds" tab; striker logs on Lumenbound Stone
./gradlew runServer        # dev server: validates the dimension datapack loads
```

CI: [.github/workflows/build.yml](.github/workflows/build.yml) runs `./gradlew build` on JDK 21 per
push/PR to `main`. Release (later): bump `gradle.properties` + `CHANGELOG.md`, then
`gh release create vX.Y.Z build/libs/*.jar`.

**Smoke signal** (log): `[lumenwilds] Initialising The Lumenwilds`, `DeferredRegisters wired…`,
`Common setup complete.`; on a server, `Done (Ns)!` with no error mentioning `lumenwilds`.
