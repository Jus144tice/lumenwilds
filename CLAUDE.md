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

**Current state = Phases 2–4 done; Phase 5 in progress.** It compiles, loads on client/server,
registers all content, and shows the creative tab. **The portal works end-to-end** (Phase 2): the Lumen
Striker ignites a Lumenbound Stone frame (real frame detection + interior fill), and stepping through
teleports the player to `lumenwilds:lumenwilds` and back, find-or-building a return portal at 1:1-scaled
coordinates, with "Entering/Leaving the Lumenwilds" messages. **Low-gravity movement works** (Phase 3):
reduced gravity, higher jumps, later + halved fall damage, and flatter projectile arcs, all via vanilla
attribute modifiers applied on dimension entry. **The building-block sets are complete** (Phase 4): full
Glowwood wood set (incl. signs, hanging signs, boats + chest boats, and axe-stripping), Moonstone + Deep
Moonstone stone sets, Shimmerstone set, and Sporeglass — ~81 blocks with placeholder assets, recipes
(incl. stonecutter), loot, and tags. **Bespoke terrain + first content are in** (Phase 5a/5b): the dimension uses a
custom `noise_settings/lumenwilds.json` (moonstone default, hilly/cliffy terrain, ponds) with surface
rules layering lumen grass → moonloam → moonstone → deep moonstone, under the **Lumen Glade** biome
(blue-green palette). The Glade now generates **Lumen Crystal Ore** (in moonstone + deep moonstone, glows
faintly), scattered flora — **Moonblossom** (a real flower) and **Glow Fern** — (5b), and **Glowwood
trees** (with a growable **Glowwood Sapling**) (5c), via configured/placed features. **Glowroot trees**
(5c-2/5c-3) are the bible's signature species, sharing one procedural shape (`world.feature.GlowrootShape`)
at three scales: an ordinary **2×2 tree** (tall, spreading, leafy — a normal feature, distinct from
vanilla dark oak), a growable **Glowroot Sapling** (4 → the 2×2), and the **mega tree** — a town-sized
worldgen **structure** (~20-wide trunk, ~80 tall, ~50-wide canopy, arching roots + a Lumen-Crystal-Ore
cluster beneath) that spawns sporadically (~every 20 chunks). **More biomes are landing one per effort
(Phase 5d.1–5d.6):** the dimension now uses a **`multi_noise` biome source** instead of a fixed one. **5d.1**
added the **Glowroot Forest** (the signature wood biome — a dim, dense forest of giant self-lit Glowroot
trees over a dark-teal floor; humidity-split from the Glade). **5d.2** added the **Glasspetal Crags** (the
crystal highlands — a blue-violet palette lit by mineral growth, scattering the new **Glasspetal Cluster**
block + exposed Lumen Crystal Ore; carved out by a cold temperature band). **5d.3** added the **Sporefall
Jungle** (the densest biome — a lush green rainforest of new **Giant Glowcap** huge mushrooms with drifting
glowing-spore ambient particles; hot+humid band), plus the **mega Glowcap** — a town-sized worldgen
**structure** (a giant flared-stem, domed-cap mushroom with a Lumen-Crystal-Ore cluster beneath) that
spawns sporadically in the jungle (its own mushroom geometry, parallel to the mega Glowroot tree).
**Lumenwater is in (Phase 5e):** the dimension's native glowing water — a full NeoForge fluid (`FluidType`
+ source/flowing pair + liquid block, light 4 + bucket), rendered as glowing teal water (reusing vanilla
water animations, tinted). Per the bible's anti-OP rule, Lumenwater carried **out** of the Lumenwilds
slowly reverts to ordinary water (a dimension-gated random tick on `fluid.LumenwaterBlock`). **5d.4** added
the **Moonmire** (the glowing swamp — a dark misty wetland whose pools are real Lumenwater [via a `lake`
feature], fringed by the new **Glow Algae** + **Lumen Reeds** flora and sparse Glowroot; mild+wettest band).
**5d.5** added the **Undercrown Caverns** — the underground **cave biome**: the noise router's `depth` was
made y-varying so the `multi_noise` source layers Undercrown *under* the surface biomes (deep-depth point);
it's naturally lit by dense Lumen Crystal Ore + **Glowvine veins** (an ore feature) with underground
Lumenwater pools. **5d.6** added the **Stillbloom Basin** — the rare sanctuary: open fields of the new
multi-block giant **Stillbloom** (stem + petal dome + glowing core, built by a custom `StillbloomFeature`),
brightest/softest palette, placed at a hot+driest climate *corner* so it's rare. **All seven biomes are now
in — Phase 5d complete.** **Mobs are starting (Phase 6):** Lumenwater now *functions as water* (boats/
farmland/fire/fish, via the `#minecraft:water` tag + FluidType caps — 6.0); the **Lumen Grazer** (6a, a
peaceful herd herbivore) stood up the shared entity infrastructure (`ModEntities`, the `entity/` package,
the attribute + spawn-placement events, the client renderer seam, loot/spawn-egg) reused by every later
mob; the **Shade Stalker** (6b) is the core hostile — a fast dark ambush predator that **flees bright
light** (daylight, Stillbloom Cores, Lumen lanterns) via the reusable `entity.ai.FleeBrightLightGoal`, so
living light genuinely wards it off; the **Lantern Beetle** (6c) is the first **flying** mob — a small
glowing insect that flies to flowers/Lumenbulbs (reusable `entity.ai.FlyToBlocksGoal`) and is **bottleable**
(glass bottle → Bottled Lantern Beetle, a placeable glowing lamp block), establishing the `FlyingMoveControl`/
`FlyingPathNavigation` pattern; the **Sporeling** (6d) is the jungle/cave **swarm** — weak fungal mobs
that aggro as a group and burst into a vision-clouding **spore cloud** (`AreaEffectCloud`, Darkness + Slowness)
on death; the **Mirelurker** (6e) is the Moonmire **amphibious** ambusher — lurks in shallow Lumenwater
(doesn't drown, treats water as walkable via `AmphibiousPathNavigation`), lunges at players, and is faster at
night (a transient speed modifier); the **Lumen Fish** (6f) is the native passive **schooling** swimmer of
the glowing water — bucketable (catch it → a fish bucket) and the in-world source of edible Mirefish; the
**Sky Jelly** (6g) is the floating air-ambience drifter — a harmless jellyfish-like mob that hovers on
near-zero gravity and drops Air Gel; the **Glowmoth** (6h) is the neutral flower **guardian** — it circles
flowers/lights but **turns hostile if you break a nearby Moonblossom/Stillbloom** (a `BlockEvent.BreakEvent`
handler aggros nearby moths); the **Rootback** (6i) is the **massive** slow neutral "living-feature"
turtle (100 HP, knockback-immune, retaliates only when hit, and *seeds flora* as it wanders); and the **Crag
Wraith** (6j) is the Glasspetal Crags aerial threat — a fast flying hostile that dives at players with heavy
knockback (deadly near the cliffs). **All 10 mobs are now in — Phase 6 complete.** **Atmosphere is starting
(Phase 7):** the dimension now has a **bespoke sky** (7a) — a client `DimensionSpecialEffects`
(`client.LumenDimensionEffects`) bound to the dimension's effects id renders perpetual dim twilight (a
deep-indigo→teal dome), a weak blurred sun, and **Veyra**, the oversized pale moon the world lives under;
`dimension_type.effects` now points to `lumenwilds:lumenwilds` with raised `ambient_light` 0.2. *(Sky
rendering can only be verified in-client — `runClient` through a portal — not on a headless server.)*
**Atmosphere particles are in (7b):** three bespoke `SimpleParticleType`s (`ModParticles` — `lumen_spore`,
`glow_pollen`, `crystal_shimmer`, render factories reusing vanilla particle classes) drift through the world
as biome ambience (`effects.particle` per biome) and rise from the portal. **Soundscapes are in (7c):** every
biome carries a vanilla-sourced `ambient_sound`/`additions_sound`/`music` (the Nether ambience loops —
warped/crimson/basalt/soul-sand/nether-wastes — read as alien; calm overworld music for the open biomes), and
the portal hums (`PORTAL_AMBIENT`). *Bespoke recorded SFX (custom `.ogg`) is a Phase 9 asset task — I can't
author audio, so the soundscape is built from vanilla sound events.* **The half-rate day cycle is in (7d.1):**
the dimension runs a **48,000-tick day** (twice the Overworld) — the project's first **Mixin** flips the
Lumenwilds `ServerLevel` to tick its own clock and decouples that clock from the Overworld at 0.5 day-time/
tick (`mixin/`, `world/time/`, `event.LumenTimeEvents`), reusing NeoForge's per-dimension time sync.
*(Verified headlessly: Lumenwilds dayTime advances at exactly half the Overworld's.)* **Ambient events are in
(7d.2) — Phase 7 complete:** a transient `world.event.LumenEventManager` (ticked per Lumenwilds tick by
`event.LumenEventDriver`) schedules one ambient event at a time — **Sporefall** (boosted Sporeling spawns in
the jungle + dense spore particles), **Moonwake** (night-only: brighter Veyra + extra Lantern Beetles), **Deep
Hush** (more hostiles near deep players) — and syncs the active event to clients via a `network`
`CustomPacketPayload` (`LumenEventPayload` → `LumenEventClientState`), which the sky + `client.LumenEventClientEffects`
read for the visuals. **The whole atmosphere (Phase 7) is now in.** **Phase 8 is starting — status effects
(8a):** `registry.ModMobEffects` adds four `effect.LumenMobEffect`s — **Lightfoot** (beneficial: +jump,
+safe-fall), **Glowmarked** (neutral: target glows, via `event.LumenEffectEvents` toggling `setGlowingTag`),
**Sporeblind** (harmful: a spore-clouded slow — what the Sporeling death cloud now applies), **Rooted**
(harmful: heavy slow + no jump), all attribute-driven. **Food is in (8b):** Lumen Fruit (→ brief night
vision) and Lumen Nectar (→ brief regeneration) are now real foods (`DataComponents.FOOD`), the latter
**collected from a Stillbloom with a glass bottle** (`event.StillbloomInteractEvents`); and **Glowcap Stew**
(`ModItems#GLOWCAP_STEW`, bowl + glowcap + lumen fruit + moonblossom → hunger + night vision, returns the
bowl). **The Lumen Anchor is in (8c):** a portal-link device — `block.LumenAnchorBlock` (a `BaseEntityBlock`)
+ `block.LumenAnchorBlockEntity` (stores a partner `GlobalPos`); right-click two anchors with the Lumen
Striker to pair them, and a linked anchor near a portal makes return travel land **precisely** at its partner
(`portal.LumenAnchorLinks` overrides the scaled find-or-build in `LumenPortalBlock#getPortalDestination`).
Crafted from Shimmerstone + Lumen Crystal Block + Echo Dust + Lumenbound Stone; it's the first `ModBlockEntities`
content. **Structures are starting (8d):** the **Rootshrine** — a small early-reward shrine (a Moonstone floor
under a cage of arching Glowroot-log roots + a loot chest), a procedural `world.structure.RootshrinePiece`
like the mega tree, generating in the Glowroot Forest; and the **Lumenbound Ruins** (8e) — a ruined
Lumenwilds-portal site in the **Overworld** (broken Lumenbound Stone frame + rubble + a chest of
striker/frame ingredients), the in-world tutorial for reaching the dimension; and the **Glasspetal Spires**
(8f) — a cluster of tapering crystal towers in the Glasspetal Crags (Shimmerstone/crystal, Glasspetal-Cluster
crowns, base loot chest), **Crag-Wraith-guarded** via the structure's `spawn_overrides`. What is deliberately
*not* built yet: brewing/potions; and the **Undercrown Relics** (8g) — a buried Deep-Moonstone dungeon hall
in the Undercrown Caverns (a Shade Stalker spawner + two chests of rare loot & Lumen-Anchor parts), placed at
a **deep** Y. **All four Phase 8 structures are now in.** **Brewing is in (8h):** the four 8a effects are brewable —
`registry.ModPotions` registers a Potion per effect and `event.ModBrewing` wires the mixes (awkward + Air Gel
→ Lightfoot, + Glow Pollen → Glowmarked, + Spore Sac → Sporeblind, + Living Fiber → Rooted). **Phase 8 is
complete.** What is deliberately *not* built yet: the final art/audio/polish pass (Phase 9) — and the
visual-only deferrals logged throughout (final mob models, the Sporeblind overlay, real `.ogg` audio, etc.). **All biomes share one terrain *height*** (only `depth` varies, for the cave
layer) — per-biome terrain silhouette is a deferred cross-cutting pass (see IMPLEMENTATION_PLAN). Roadmap:
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
  DeferredRegisters to the **mod bus**: `ModSounds`, `ModParticles`, `ModMobEffects`, `ModPotions`,
  `ModFluidTypes`, `ModFluids`, `ModBlocks`, `ModItems`, `ModStructures` (`STRUCTURE_TYPES` + `STRUCTURE_PIECES`),
  `ModBlockEntities`, `ModEntities`, `ModMenus`, `ModCreativeTabs`. Also calls `ModWoodTypes#init()` first (WoodType/
  BlockSetType must register before blocks build). `#onCommonSetup` logs only. **When you add a new
  (non-empty) DeferredRegister, register it here.**

### registry/ — all registered content
- [ModBlocks.java](src/main/java/com/jus144tice/lumenwilds/registry/ModBlocks.java) — `#BLOCKS`
  (`DeferredRegister.Blocks`), ~78 blocks. Core: `#LUMENBOUND_STONE` (portal frame), `#LUMEN_ANCHOR`
  (`block.LumenAnchorBlock`, portal-link device w/ a block entity, 8c), `#LUMEN_PORTAL`
  (`LumenPortalBlock`, non-solid/glowing), `#MOONLOAM`, `#LUMEN_GRASS_BLOCK`, `#MOONSTONE`/`#COBBLED_MOONSTONE`,
  `#GLOWROOT_LOG` (`RotatedPillarBlock`), `#GLOWVINE`, `#LUMENBULB`, `#LUMEN_CRYSTAL_BLOCK`. **Phase 5
  content:** `#MOONBLOSSOM` (`FlowerBlock`, night-vision), `#GLOW_FERN`/`#GLOW_ALGAE`/`#LUMEN_REEDS`
  (`TallGrassBlock` cross flora — the latter two are Moonmire's glowing swamp cover, 5d.4),
  `#STILLBLOOM_STEM`/`#STILLBLOOM_PETAL`/`#STILLBLOOM_CORE` (soft glowing cubes the `StillbloomFeature`
  assembles into a giant flower, core light 12, 5d.6), `#BOTTLED_LANTERN_BEETLE` (`block.BottledLanternBeetleBlock`
  — a placeable jar-shaped lamp, light 12, needs support below; the caught Lantern Beetle, 6c),
  `#GLOWWOOD_SAPLING`/`#GLOWROOT_SAPLING` (`SaplingBlock` + `TreeGrower`; the Glowroot grower's mega slot
  is the 2×2), `#GLOWROOT_LEAVES` (`LeavesBlock`, glow) — cross-model; `#LUMEN_CRYSTAL_ORE` +
  `#DEEP_LUMEN_CRYSTAL_ORE` (`DropExperienceBlock`, drop shards, glow); `#GLASSPETAL_CLUSTER`
  (`AmethystClusterBlock`, directional/waterloggable, glow 7 — the Glasspetal Crags crystal, 5d.2);
  `#GIANT_GLOWCAP_BLOCK` (glow 9) + `#GIANT_GLOWCAP_STEM` (`HugeMushroomBlock` — the Sporefall Jungle giant
  mushroom, placed by the vanilla `huge_brown_mushroom` feature, 5d.3); `#LUMENWATER_BLOCK`
  (`fluid.LumenwaterBlock`, the Lumenwater liquid block — **no BlockItem, `noLootTable`**, 5e). **Phase 4 sets**
  (helpers `moonCube/moonStairs/moonSlab/moonWall`, `deep*`, `shimmer*`, `logProps/planksProps`):
  Glowwood wood set (`#GLOWWOOD_LOG` pillar, `#GLOWWOOD_WOOD`, stripped log/wood, `#GLOWWOOD_PLANKS`,
  `#GLOWWOOD_LEAVES`, stairs/slab/fence/fence_gate/door/trapdoor/button/pressure_plate + signs
  `#GLOWWOOD_SIGN`/`#GLOWWOOD_WALL_SIGN`/`#GLOWWOOD_HANGING_SIGN`/`#GLOWWOOD_WALL_HANGING_SIGN` — all using
  `ModWoodTypes.GLOWWOOD`/`GLOWWOOD_SET`); Moonstone set (smooth/bricks/chiseled/tiles + stairs/slabs/
  walls); Deep Moonstone (deepslate-analog: cobbled/polished/bricks/tiles + shapes); Shimmerstone
  (polished/bricks/tiles/pillar/glass + shapes); Sporeglass (`TransparentBlock`) + pane (`IronBarsBlock`).
  **Add a block here → it auto-gets a BlockItem in `ModItems` (loop); add asset + loot via datagen.**
- [ModWoodTypes.java](src/main/java/com/jus144tice/lumenwilds/registry/ModWoodTypes.java) — bespoke
  `#GLOWWOOD` (`WoodType`, name `lumenwilds:glowwood` → sign textures) + `#GLOWWOOD_SET` (`BlockSetType`).
  NOT DeferredRegister content; `#init()` is called early in the `Lumenwilds` ctor.
- [ModBoatTypes.java](src/main/java/com/jus144tice/lumenwilds/registry/ModBoatTypes.java) — `#GLOWWOOD_BOAT_TYPE`
  (`EnumProxy<Boat.Type>`) adds a Glowwood `Boat.Type` via NeoForge enum extension
  (`META-INF/enumextensions.json` + the `enumExtensions` key in `neoforge.mods.toml`); `#glowwood()` →
  the created type. Reuses vanilla `Boat`/`ChestBoat`/renderer — no custom entity.
- [ModItems.java](src/main/java/com/jus144tice/lumenwilds/registry/ModItems.java) — `#ITEMS`
  (`DeferredRegister.Items`). Standalone: `#LUMEN_STRIKER` (`LumenStrikerItem`, **durable: `stacksTo(1)
  .durability(64)`** — each ignition costs 1 use), `#LUMEN_CRYSTAL_SHARD`, `#GLOW_POLLEN`,
  `#LIVING_FIBER`, `#LUMEN_FRUIT` (**food**, 8b — brief night vision), `#LUMEN_NECTAR` (**food**, 8b — brief
  regen; collected from a Stillbloom with a bottle via `event.StillbloomInteractEvents`), `#AIR_GEL`,
  `#GLOWCAP_STEW` (**food**, 8b — bowl + glowcap + lumen fruit + moonblossom → hunger + night vision, returns
  a bowl via `usingConvertsTo`), `#LUMENWATER_BUCKET` (`BucketItem` over
  `ModFluids.LUMENWATER`, 5e); **mob drops + spawn eggs (Phase 6):** `#RAW_GRAZER_MEAT`/`#COOKED_GRAZER_MEAT`
  (foods), `#GRAZER_HIDE`, `#GLOW_SINEW`, `#LUMEN_GRAZER_SPAWN_EGG` (`DeferredSpawnEggItem`) — all 6a;
  `#SHADE_CLAW`/`#DARK_HIDE`/`#ECHO_DUST` + `#SHADE_STALKER_SPAWN_EGG` (6b); `#LANTERN_BEETLE_SPAWN_EGG` (6c —
  the Bottled Lantern Beetle is a *block*, `ModBlocks#BOTTLED_LANTERN_BEETLE`); `#SPORE_SAC`/`#GLOWCAP_SPORES`
  + `#SPORELING_SPAWN_EGG` (6d); `#MIRE_TOOTH`/`#LUMEN_ALGAE`/`#RAW_MIREFISH`/`#COOKED_MIREFISH` (foods) +
  `#MIRELURKER_SPAWN_EGG` (6e); `#LUMEN_FISH_BUCKET` (`MobBucketItem`) + `#LUMEN_FISH_SPAWN_EGG` (6f);
  `#SKY_JELLY_SPAWN_EGG` (6g — drops the existing `#AIR_GEL`); `#GLOW_SCALES` + `#GLOWMOTH_SPAWN_EGG` (6h);
  `#ROOTBACK_PLATE`/`#MOONLOAM_CLUMPS` + `#ROOTBACK_SPAWN_EGG` (6i); `#WRAITH_MEMBRANE`/`#CRYSTAL_DUST`
  + `#CRAG_WRAITH_SPAWN_EGG` (6j); boats
  `#GLOWWOOD_BOAT`/`#GLOWWOOD_CHEST_BOAT`
  (`BoatItem` over `ModBoatTypes.glowwood()`); signs `#GLOWWOOD_SIGN` (`SignItem`)/`#GLOWWOOD_HANGING_SIGN`
  (`HangingSignItem`) — wall variants share these. A **static loop auto-registers a simple `BlockItem` for
  every block except `LUMEN_PORTAL`, `LUMENWATER_BLOCK`, and the sign blocks** (runs after the standalone/sign
  items so the striker stays first in the tab) — new blocks get an item with no edits here.
- [ModCreativeTabs.java](src/main/java/com/jus144tice/lumenwilds/registry/ModCreativeTabs.java) —
  `#CREATIVE_MODE_TABS`, `#LUMENWILDS_TAB` (id `lumenwilds`, title key `itemGroup.lumenwilds`, icon =
  Lumen Striker). **Auto-populates from `ModItems.ITEMS`** — new items appear without editing this file.
- [ModFluidTypes.java](src/main/java/com/jus144tice/lumenwilds/registry/ModFluidTypes.java) — `#FLUID_TYPES`
  (`NeoForgeRegistries.Keys.FLUID_TYPES`); `#LUMENWATER_TYPE` (`FluidType`, light 4, no infinite source). The
  non-state half of Lumenwater (5e). **Functions as water (Phase 6.0):** `canExtinguish`/`canHydrate`/
  `supportsBoating` set (swim/drown/push default true) **and** both fluids are in `#minecraft:water`
  (`data/minecraft/tags/fluid/water.json`) — so boats float, farmland hydrates, fire extinguishes, and fish
  survive. Glow + overworld-decay are unaffected.
- [ModFluids.java](src/main/java/com/jus144tice/lumenwilds/registry/ModFluids.java) — `#FLUIDS`;
  `#LUMENWATER` (source) + `#LUMENWATER_FLOWING` (`BaseFlowingFluid`). `#props()` lazily wires
  type↔still↔flowing↔block↔bucket (avoids a static forward-ref). The fluid registers **before** blocks, so
  `ModBlocks.LUMENWATER_BLOCK`'s factory can call `LUMENWATER.get()`.
- [ModEntities.java](src/main/java/com/jus144tice/lumenwilds/registry/ModEntities.java) — `#ENTITIES`; the
  native fauna (Phase 6). `#LUMEN_GRAZER` (`CREATURE`, 6a), `#SHADE_STALKER` (`MONSTER`, 6b), `#LANTERN_BEETLE`
  (`CREATURE`, flying, 6c), `#SPORELING` (`MONSTER`, swarm, 6d), `#MIRELURKER` (`MONSTER`, amphibious, 6e),
  `#LUMEN_FISH` (`WATER_AMBIENT`, schooling fish, 6f), `#SKY_JELLY` (`CREATURE`, floating, 6g), `#GLOWMOTH`
  (`CREATURE`, neutral flying guardian, 6h), `#ROOTBACK` (`CREATURE`, massive 3.0×2.2 turtle, 6i),
  `#CRAG_WRAITH` (`MONSTER`, flying dive-attacker, 6j) — **all 10 live, Phase 6 done**. Each entity also needs
  attributes + spawn placement (`event.ModEntityEvents`), a renderer (`client.LumenwildsClient`), a loot table
  (`loot_table/entities/`), and biome `spawners` entries.
- [ModPotions.java](src/main/java/com/jus144tice/lumenwilds/registry/ModPotions.java) — `#POTIONS`; a
  brewable `Potion` per 8a effect (`#LIGHTFOOT`/`#GLOWMARKED`/`#SPOREBLIND`/`#ROOTED`, 8h). The drinkable/
  splash/lingering/tipped item variants are vanilla; the brewing mixes are in `event.ModBrewing`.
- [ModMobEffects.java](src/main/java/com/jus144tice/lumenwilds/registry/ModMobEffects.java) — `#MOB_EFFECTS`;
  the four status effects (Phase 8a), each a `effect.LumenMobEffect` (a trivial public-ctor `MobEffect`
  subclass — vanilla's ctor is protected). `#LIGHTFOOT` (+`JUMP_STRENGTH`/+`SAFE_FALL_DISTANCE`), `#GLOWMARKED`
  (no attrs — glow via `event.LumenEffectEvents`), `#SPOREBLIND` (−`MOVEMENT_SPEED`; the Sporeling cloud
  applies it), `#ROOTED` (−`MOVEMENT_SPEED` & −`JUMP_STRENGTH`). Icons: `textures/mob_effect/<name>.png`.
- [ModBlockEntities.java](src/main/java/com/jus144tice/lumenwilds/registry/ModBlockEntities.java) —
  `#BLOCK_ENTITIES`; `#LUMEN_ANCHOR` (`BlockEntityType` for `block.LumenAnchorBlockEntity`, 8c) — the first BE.
- Empty stubs (compile; carry phase TODOs). Wired to the bus already (registered empty): 
  [ModMenus](src/main/java/com/jus144tice/lumenwilds/registry/ModMenus.java) `#MENUS`,
  [ModSounds](src/main/java/com/jus144tice/lumenwilds/registry/ModSounds.java) `#SOUNDS` (still empty — the
  7c soundscape is built from **vanilla** sound events; bespoke recorded `.ogg` SFX → Phase 9, then register
  custom events here + a `sounds.json`).
- [ModParticles.java](src/main/java/com/jus144tice/lumenwilds/registry/ModParticles.java) — `#PARTICLES`
  (atmosphere, Phase 7b); `#LUMEN_SPORE` (signature drifting glow mote — biome ambience + the portal),
  `#GLOW_POLLEN` (flower-biome float), `#CRYSTAL_SHIMMER` (Crags sparkle), all `SimpleParticleType`. Client
  render factories + sprites are wired in `client.LumenwildsClient`; usage is the portal `animateTick` + biome
  `effects.particle`.
- [ModFeatures.java](src/main/java/com/jus144tice/lumenwilds/registry/ModFeatures.java) — `#FEATURES`
  (custom `Feature` types), bus-wired. `#GLOWROOT_TREE_2X2` (`GlowrootTreeFeature`) — the ordinary 2×2
  Glowroot tree (the mega tree is a structure; both share `world.feature.GlowrootShape`); `#STILLBLOOM`
  (`StillbloomFeature`) — the giant Stillbloom flower (5d.6); `#LUMENWATER_POOL`
  (`world.feature.LumenwaterPoolFeature`) — a small **chunk-safe** Moonloam+Lumenwater basin replacing the
  vanilla `lake` (which crashed chunk-gen near borders — the Moonmire/Undercrown pools).
- [ModStructures.java](src/main/java/com/jus144tice/lumenwilds/registry/ModStructures.java) —
  `#STRUCTURE_TYPES` + `#STRUCTURE_PIECES`; `#GLOWROOT_TREE` + `#GLOWROOT_TREE_PIECE` (the mega Glowroot
  tree), `#MEGA_GLOWCAP` + `#MEGA_GLOWCAP_PIECE` (the town-sized Giant Glowcap mushroom), `#ROOTSHRINE` +
  `#ROOTSHRINE_PIECE` (the small early-reward Rootshrine, 8d), `#LUMENBOUND_RUINS` + `#LUMENBOUND_RUINS_PIECE`
  (the Overworld ruined-portal tutorial site, 8e), and `#GLASSPETAL_SPIRES` + `#GLASSPETAL_SPIRES_PIECE` (the
  crystal towers, 8f), and `#UNDERCROWN_RELICS` + `#UNDERCROWN_RELICS_PIECE` (the buried dungeon, 8g — placed at
  a deep Y). All are structures (generate per-chunk via a bounding box). Structure instances + spawn spacing
  (and Crag-Wraith `spawn_overrides`) are datapack JSON (`worldgen/structure*`); these are the code types.
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
  `#getLocalTransition` = `NONE` (no Nether nausea), `#animateTick` (rises `ModParticles.LUMEN_SPORE` + an
  occasional `PORTAL_AMBIENT` hum, 7b/7c).
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
- [LumenAnchorLinks.java](src/main/java/com/jus144tice/lumenwilds/portal/LumenAnchorLinks.java) — `#findLinkedTarget(sourceLevel, portalPos, destination)`
  (8c): scans a small box around the source portal for a linked `LumenAnchorBlockEntity` whose partner is in
  the destination dim; if found, `LumenPortalBlock#getPortalDestination` uses that exact `approx` instead of
  the 1:1-scaled position — so an anchored return lands precisely.

### block/ — custom block behaviours
- [BottledLanternBeetleBlock.java](src/main/java/com/jus144tice/lumenwilds/block/BottledLanternBeetleBlock.java)
  — the placed Bottled Lantern Beetle (6c): a jar-sized (`6×11×6`) glowing lamp that **must sit on a flat
  surface** — `#canSurvive` needs a sturdy face below, `#updateShape` pops it off if support is removed
  (lantern/candle pattern). `#CODEC`/`#codec`, `#getShape`. Registered as `ModBlocks#BOTTLED_LANTERN_BEETLE`
  (light 12); obtained by bottling a `LanternBeetle`.
- [LumenAnchorBlock.java](src/main/java/com/jus144tice/lumenwilds/block/LumenAnchorBlock.java) — the Lumen
  Anchor (8c), a `BaseEntityBlock` (`#getRenderShape` = MODEL so it still draws normally). `#useItemOn`: with
  the Lumen Striker in hand, picks/links anchors — a transient per-player `#PENDING` map holds the first pick,
  the second click `#link`s both block entities (`setLink` loads the partner's chunk via `getBlockEntity`).
  `ModBlocks#LUMEN_ANCHOR` (light 7).
- [LumenAnchorBlockEntity.java](src/main/java/com/jus144tice/lumenwilds/block/LumenAnchorBlockEntity.java) —
  stores the partner `GlobalPos` (`#getLinkedTo`/`#setLinkedTo`, saved as `LinkDim`+`LinkPos` NBT). The
  project's first block entity (`ModBlockEntities#LUMEN_ANCHOR`).

### fluid/ — Lumenwater (Phase 5e)
- [LumenwaterBlock.java](src/main/java/com/jus144tice/lumenwilds/fluid/LumenwaterBlock.java) — the
  placeable Lumenwater `LiquidBlock`. `#CODEC` (typed `MapCodec<LiquidBlock>` but builds a `LumenwaterBlock`)
  + `#codec`. `#randomTick` enforces the bible's anti-OP rule: **outside** `LUMENWILDS_LEVEL` the block
  reverts to vanilla water (preserving the flow `LEVEL`); in-dimension it's a no-op. The `FluidType` +
  source/flowing fluids + bucket live in `registry/ModFluidTypes`, `ModFluids`, `ModItems`; client render
  (teal-tinted vanilla water textures) is registered in `client.LumenwildsClient`.

### item/
- [LumenStrikerItem.java](src/main/java/com/jus144tice/lumenwilds/item/LumenStrikerItem.java) — `#useOn`:
  on a Lumenbound Stone frame, seeds detection from the air at the clicked face (fallback: block above),
  delegates to `LumenPortalManager#tryActivatePortal`; on success consumes 1 durability via
  `hurtAndBreak`. Returns `PASS` on non-frame blocks. **Never checks lodestone.**

### entity/ — native fauna (Phase 6)
- [LumenGrazer.java](src/main/java/com/jus144tice/lumenwilds/entity/LumenGrazer.java) — `Animal`; the
  peaceful herd herbivore (6a). `#registerGoals` (panic + breed/tempt on **Lumen Fruit** + a skittish
  `AvoidEntityGoal<Player>` that the tempt out-prioritises + stroll/look), `#isFood` (Lumen Fruit),
  `#createAttributes` (**native low gravity baked in: `Attributes.GRAVITY` base 0.056** ≈ 0.08×0.7),
  `#getBreedOffspring`. Placeholder render reuses the vanilla cow model. The pattern (entity class +
  `ModEntities` type + `ModEntityEvents` attributes/placement + renderer + loot + spawn egg + biome
  `spawners`) is the template every later mob follows.
- [ShadeStalker.java](src/main/java/com/jus144tice/lumenwilds/entity/ShadeStalker.java) — `Monster`; the
  core hostile (6b). Fast dark ambush predator (targets players: `NearestAttackableTargetGoal` + `HurtByTarget`,
  `MeleeAttackGoal`) that **flees bright light at top priority** (`FleeBrightLightGoal`, above attacking —
  daylight/cores/lanterns ward it off). Native low gravity in `#createAttributes`. Spawns in low light
  (`Monster::checkMonsterSpawnRules`) in Forest/Glade/Jungle/Undercrown. Placeholder render = vanilla spider.
- [entity/ai/FleeBrightLightGoal.java](src/main/java/com/jus144tice/lumenwilds/entity/ai/FleeBrightLightGoal.java)
  — the **reusable** "living light keeps danger away" `Goal`: when the mob's `getMaxLocalRawBrightness` (block
  + day-adjusted sky) ≥ a threshold, it bolts to a sampled darker spot. Covers both natural and Lumen light,
  dormant in the dark. Shared by all light-shy mobs (Shade Stalker first).
- [LanternBeetle.java](src/main/java/com/jus144tice/lumenwilds/entity/LanternBeetle.java) — `Animal`, the
  first **flying** mob (6c). Establishes the flight setup: `FlyingMoveControl(this, 20, true)` +
  `FlyingPathNavigation` (`#createNavigation`) + `FLYING_SPEED`. Non-breeding (`#isFood` false,
  `#getBreedOffspring` null). `#mobInteract` with a glass bottle → catches it (consumes bottle, discards the
  mob) and yields the **Bottled Lantern Beetle block item** (`ModBlocks.BOTTLED_LANTERN_BEETLE`) — a placeable
  glowing lamp. Goals: panic + `FlyToBlocksGoal` (flowers/lights) + `WaterAvoidingRandomFlyingGoal`.
  (Moving-light emission deferred → Phase 9.)
- [entity/ai/FlyToBlocksGoal.java](src/main/java/com/jus144tice/lumenwilds/entity/ai/FlyToBlocksGoal.java) —
  **reusable** flight goal: throttled scan of a small box for a block matching a `Predicate<BlockState>`
  (Moonblossom/Lumenbulb/Glowvine), then flies to hover above the nearest. Shared by the Lantern Beetle (and
  later the Glowmoth).
- [Sporeling.java](src/main/java/com/jus144tice/lumenwilds/entity/Sporeling.java) — `Monster`, the
  jungle/cave **swarm** (6d). Weak melee attacker that targets players; `HurtByTargetGoal#setAlertOthers`
  makes the group aggro together. `#die` bursts a **spore cloud** — an `AreaEffectCloud` (radius 2.5, 80t,
  spore particle) applying **`ModMobEffects.SPOREBLIND`** (the real effect, 8a) + Darkness (the visibility
  overlay is Phase 9). Native low gravity. Placeholder render = vanilla slime.
- [Mirelurker.java](src/main/java/com/jus144tice/lumenwilds/entity/Mirelurker.java) — `Monster`, the Moonmire
  **amphibious** ambusher (6e); the first water-capable mob. `#createNavigation` = `AmphibiousPathNavigation`
  + `setPathfindingMalus(WATER, 0)` so it walks land and water freely; doesn't drown (via the
  `#minecraft:can_breathe_under_water` entity-type tag — `canBreatheUnderwater()` is final), `#isPushedByFluid`
  false (lurks in place). Lunges at players (`MeleeAttackGoal` + targeting); `#customServerAiStep` adds a
  transient **+30% MOVEMENT_SPEED at night** (stable `ResourceLocation` modifier, like `LowGravityHandler`).
  Placeholder render = vanilla salmon. (The plant-mimic lure visual → Phase 9.)
- [LumenFish.java](src/main/java/com/jus144tice/lumenwilds/entity/LumenFish.java) — `AbstractSchoolingFish`,
  the native glowing-water swimmer (6f). Schooling + swim nav + water-survival come from the base; reuses cod
  sounds. **Bucketable** (`#getBucketItemStack` → `ModItems.LUMEN_FISH_BUCKET`, a `MobBucketItem`). Drops Raw
  Mirefish (the edible-fish source). Native low gravity; in `#minecraft:can_breathe_under_water`. Spawns
  `IN_WATER` (`WaterAnimal::checkSurfaceWaterAnimalSpawnRules`). Placeholder render = vanilla cod.
- [SkyJelly.java](src/main/java/com/jus144tice/lumenwilds/entity/SkyJelly.java) — `Animal`, the floating
  air-ambience drifter (6g). Reuses the flight setup (`FlyingMoveControl` + `FlyingPathNavigation`) but with a
  **near-zero `GRAVITY` (0.01)** so it hovers, and only a slow `WaterAvoidingRandomFlyingGoal` + a look goal —
  harmless, non-breeding. Drops `AIR_GEL`. Placeholder render = vanilla ghast model scaled to 0.35 (see
  `SkyJellyRenderer#scale`).
- [Glowmoth.java](src/main/java/com/jus144tice/lumenwilds/entity/Glowmoth.java) — `Animal`, the **neutral**
  flying flower guardian (6h). Circles flowers/lights (`FlyToBlocksGoal` over `#isAttractor` — Moonblossom /
  Stillbloom Core / Lumenbulb / Bottled Lantern Beetle) + drifts; idle `MeleeAttackGoal` (only acts with a
  target) + `HurtByTargetGoal`. Turns hostile via **`event.GlowmothAggroEvents`** (a block-break handler that
  `setTarget`s nearby moths on whoever broke a guarded bloom). Drops Glow Scales. Placeholder render =
  vanilla endermite scaled 1.6.
- [Rootback.java](src/main/java/com/jus144tice/lumenwilds/entity/Rootback.java) — `Animal`, the **massive**
  (`3.0×2.2`) slow neutral "living-feature" turtle (6i). Tanky: 100 HP, knockback-immune, `STEP_HEIGHT` 1.5,
  only retaliates (idle `MeleeAttackGoal` + `HurtByTargetGoal`, no auto-targeting). `#customServerAiStep`
  occasionally **seeds a Glow Fern / Moonblossom** on nearby Lumenwilds soil — the "plants grow where it
  rests" rule. Drops Rootback Plate / Living Fiber / Moonloam Clumps. Placeholder render = the cow model
  scaled **3.4** (the turtle shell + shell-plants are Phase 9).
- [CragWraith.java](src/main/java/com/jus144tice/lumenwilds/entity/CragWraith.java) — `Monster`, the
  Glasspetal Crags aerial threat (6j; the last mob). Flying hostile that hunts players from a long range
  (`FOLLOW_RANGE` 32) and dives at them (fast flying `MeleeAttackGoal`) with **heavy `ATTACK_KNOCKBACK` (1.5)**
  — deadly near the crags' ledges. Floaty (low gravity). Drops Wraith Membrane / Crystal Dust. Placeholder
  render = the ghast model scaled/flattened ~0.7.

### world/ — dimension & worldgen keys (datapack-driven)
- [LumenDimensionConstants.java](src/main/java/com/jus144tice/lumenwilds/world/LumenDimensionConstants.java)
  — **canonical** keys. `#DIMENSION_PATH` (`"lumenwilds"`), `#LUMENWILDS_LEVEL` (`ResourceKey<Level>`),
  `#LUMENWILDS_STEM` (`LevelStem`), `#LUMENWILDS_DIM_TYPE` (`DimensionType`), `#LUMENWILDS_NOISE`
  (`NoiseGeneratorSettings` — the bespoke terrain, Phase 5a).
- [LumenBiomeBootstrap.java](src/main/java/com/jus144tice/lumenwilds/world/LumenBiomeBootstrap.java) —
  the bible's 7 biome keys: `#LUMEN_GLADE` (5a) + `#GLOWROOT_FOREST` (5d.1) + `#GLASSPETAL_CRAGS` (5d.2)
  + `#SPOREFALL_JUNGLE` (5d.3) + `#MOONMIRE` (5d.4) + `#UNDERCROWN_CAVERNS` (5d.5) + `#STILLBLOOM_BASIN`
  (5d.6) — **all seven live (Phase 5d complete)**.
- [LumenConfiguredFeatures.java](src/main/java/com/jus144tice/lumenwilds/world/LumenConfiguredFeatures.java)
  — keys for `data/.../worldgen/configured_feature/`: `#LUMEN_CRYSTAL_ORE`, `#PATCH_MOONBLOSSOM`,
  `#PATCH_GLOW_FERN`, `#GLOWWOOD_TREE`, `#GLOWROOT_TREE` (1×1, vanilla `tree`), `#GLOWROOT_TREE_2X2`
  (custom `GlowrootTreeFeature`), `#PATCH_GLASSPETAL` (5d.2, Glasspetal Cluster `random_patch`),
  `#GIANT_GLOWCAP` (5d.3, vanilla `huge_brown_mushroom` with the glowcap blocks), `#LUMENWATER_POOL`
  (5d.4, vanilla `lake` filled with Lumenwater) + `#PATCH_GLOW_ALGAE`/`#PATCH_LUMEN_REEDS`,
  `#UNDERCROWN_GLOWVINE` (5d.5, an `ore` feature threading Glowvine through cave rock), `#STILLBLOOM` (5d.6,
  the custom giant-flower `StillbloomFeature`) — all live. (The Glowroot *mega* tree is a structure.)
- [LumenPlacedFeatures.java](src/main/java/com/jus144tice/lumenwilds/world/LumenPlacedFeatures.java) —
  same paths under `placed_feature/` (different registry), referenced from `biome/lumen_glade.json`'s
  feature lists: `#LUMEN_CRYSTAL_ORE` (ores step), `#GLOWROOT_TREE_2X2`/`#GLOWROOT_TREE`/`#GLOWWOOD_TREE`
  + `#PATCH_MOONBLOSSOM`/`#PATCH_GLOW_FERN` (vegetal step). `#GLOWROOT_FOREST_TREES` (5d.1) is a
  forest-density placement of the 2×2 tree (Glowroot Forest); `#PATCH_GLASSPETAL` (5d.2) scatters Glasspetal
  Clusters (Glasspetal Crags); `#GIANT_GLOWCAP` (5d.3) places giant mushrooms (Sporefall Jungle);
  `#LUMENWATER_POOL`/`#PATCH_GLOW_ALGAE`/`#PATCH_LUMEN_REEDS` (5d.4) make the Moonmire swamp;
  `#UNDERCROWN_GLOWVINE`/`#UNDERCROWN_CRYSTAL`/`#UNDERCROWN_POOL` (5d.5, height-ranged) light the deep caves;
  `#STILLBLOOM` (5d.6) fields the giant flowers.
- [LumenWorldgenBootstrap.java](src/main/java/com/jus144tice/lumenwilds/world/LumenWorldgenBootstrap.java)
  — empty seam for code-generated worldgen (`RegistrySetBuilder`/`BootstrapContext`) if we leave JSON.

### world/feature/ — Glowroot tree geometry (Phase 5c-3)
- [GlowrootShape.java](src/main/java/com/jus144tice/lumenwilds/world/feature/GlowrootShape.java) — the
  **shared** procedural Glowroot tree (trunk/roots/branches/dual-dome canopy + optional ore). `#generate`
  draws into a `#Placer` (abstracts structure box-clipping vs. feature direct writes); `#Params` size
  knobs with presets `#MEGA` (the structure giant) and `#MEDIUM` (the ordinary 2×2 feature). **Tune tree
  shape here** — both variants share it.
- [GlowrootTreeFeature.java](src/main/java/com/jus144tice/lumenwilds/world/feature/GlowrootTreeFeature.java)
  — `Feature<NoneFeatureConfiguration>` for the ordinary 2×2 Glowroot tree; `#place` runs
  `GlowrootShape.generate(..., MEDIUM)`. Bound to `ModFeatures#GLOWROOT_TREE_2X2`.
- [StillbloomFeature.java](src/main/java/com/jus144tice/lumenwilds/world/feature/StillbloomFeature.java) —
  `Feature<NoneFeatureConfiguration>` (5d.6); `#place` builds a 3–8-tall giant Stillbloom (stem column +
  petal disc dome + glowing core) into air/replaceable space, stopping if it hits solid. Bound to
  `ModFeatures#STILLBLOOM`.
- [MegaGlowcapShape.java](src/main/java/com/jus144tice/lumenwilds/world/feature/MegaGlowcapShape.java) —
  the procedural geometry for the **mega Glowcap** mushroom structure: a flared solid stem + a broad domed
  cap *shell* (hollow underside) of glowing cap blocks + a Lumen-Crystal-Ore cluster beneath (`#seedOreColumn`,
  same idea as the Glowroot mega). `#generate` draws into a `GlowrootShape.Placer` (the shared block sink —
  only the interface and ore idea are shared; the silhouette is its own mushroom, NOT the tree). `#Params`
  + preset `#MEGA`. **Tune the giant mushroom here.**

### world/structure/ — procedural structures (Phases 5c-2, 5d.3, 8d+)
- [GlowrootTreeStructure.java](src/main/java/com/jus144tice/lumenwilds/world/structure/GlowrootTreeStructure.java)
  — `Structure` (`#CODEC` via `simpleCodec`); `#findGenerationPoint` places one `GlowrootTreePiece` at the
  surface chunk centre. Bound to `ModStructures#GLOWROOT_TREE`.
- [GlowrootTreePiece.java](src/main/java/com/jus144tice/lumenwilds/world/structure/GlowrootTreePiece.java)
  — `StructurePiece` whose `#postProcess` runs `GlowrootShape.generate(..., MEGA)` from a position-seeded
  RNG (deterministic across chunks) through a box-clipped `Placer` — so the ~20-wide/~80-tall/~50-canopy
  giant spans chunks with **no "far chunk" errors**. Geometry lives in `GlowrootShape` (shared with the
  2×2 feature); tweak the giant's size via `GlowrootShape#MEGA`.
- [MegaGlowcapStructure.java](src/main/java/com/jus144tice/lumenwilds/world/structure/MegaGlowcapStructure.java)
  / [MegaGlowcapPiece.java](src/main/java/com/jus144tice/lumenwilds/world/structure/MegaGlowcapPiece.java) —
  the **mega Glowcap** giant mushroom (same structure/piece plumbing as the Glowroot mega, position-seeded
  RNG + box-clipped `Placer`). The piece runs `MegaGlowcapShape.generate(..., MEGA)`; geometry/size live in
  `MegaGlowcapShape#MEGA`. Bound to `ModStructures#MEGA_GLOWCAP`. Spawns in the Sporefall Jungle only.
- [RootshrineStructure.java](src/main/java/com/jus144tice/lumenwilds/world/structure/RootshrineStructure.java)
  / [RootshrinePiece.java](src/main/java/com/jus144tice/lumenwilds/world/structure/RootshrinePiece.java) — the
  **Rootshrine** (8d), a small early-reward shrine. Same plumbing as the giants but the geometry is built
  inline in `#postProcess` (no shared Shape): a Moonstone floor disc, four Glowroot-log roots arching to a
  shared peak (the "inside giant roots" cage) + leaf cap / hanging Glowvine / Lumenbulb lights, and a central
  pedestal **loot chest** (`setLootTable` → `chests/rootshrine`). Bound to `ModStructures#ROOTSHRINE`; spawns
  in the Glowroot Forest.
- [LumenboundRuinsStructure.java](src/main/java/com/jus144tice/lumenwilds/world/structure/LumenboundRuinsStructure.java)
  / [LumenboundRuinsPiece.java](src/main/java/com/jus144tice/lumenwilds/world/structure/LumenboundRuinsPiece.java)
  — the **Lumenbound Ruins** (8e), a ruined portal site in the **Overworld** (not the Lumenwilds — it's the
  discovery/tutorial). `#postProcess` builds a broken 4×5 Lumenbound Stone frame (random axis, ~30% missing,
  rest weathered into mossy/cracked stone) around a 2×3 hole, a ragged base, scattered rubble, and a
  half-buried **chest of striker + frame ingredients** (`chests/lumenbound_ruins`). Bound to
  `ModStructures#LUMENBOUND_RUINS`.
- [GlasspetalSpiresStructure.java](src/main/java/com/jus144tice/lumenwilds/world/structure/GlasspetalSpiresStructure.java)
  / [GlasspetalSpiresPiece.java](src/main/java/com/jus144tice/lumenwilds/world/structure/GlasspetalSpiresPiece.java)
  — the **Glasspetal Spires** (8f). `#postProcess` grows a main spire + two satellites — tapering discs of
  mixed Shimmerstone / Shimmerstone Bricks / Lumen Crystal Block crowned with a Glasspetal Cluster — and a
  base loot chest (`chests/glasspetal_spires`). Bound to `ModStructures#GLASSPETAL_SPIRES`; spawns in the
  Glasspetal Crags, **Crag-Wraith-guarded** via the structure JSON's `spawn_overrides` (not code).
- [UndercrownRelicsStructure.java](src/main/java/com/jus144tice/lumenwilds/world/structure/UndercrownRelicsStructure.java)
  / [UndercrownRelicsPiece.java](src/main/java/com/jus144tice/lumenwilds/world/structure/UndercrownRelicsPiece.java)
  — the **Undercrown Relics** (8g), a buried dungeon hall. Unlike the surface structures, `#findGenerationPoint`
  picks a **deep** Y (deterministic per chunk, well below the surface). `#postProcess` carves a Deep-Moonstone
  shell around a 9×7×5 air chamber (tiled floor, four pillars, Lumenbulb lights), a central **mob spawner**
  (`SpawnerBlockEntity#setEntityId` → Shade Stalker), and two loot chests (`chests/undercrown_relics`: rare
  loot + Lumen-Anchor parts). Bound to `ModStructures#UNDERCROWN_RELICS`; spawns in the Undercrown Caverns.

### effects/ — movement (Phase 3, working)
- [LowGravityHandler.java](src/main/java/com/jus144tice/lumenwilds/effects/LowGravityHandler.java) —
  applies the low-gravity feel via **transient vanilla attribute modifiers** (stable `ResourceLocation`
  ids), added on dimension enter and removed on exit. `#GRAVITY_MULTIPLIER` (0.7 → `Attributes.GRAVITY`
  ×0.7; jump height ~1.79 blocks emerges from this, so `JUMP_STRENGTH` is deliberately NOT touched),
  `#SAFE_FALL_BONUS` (+3 → 6-block safe fall), `#FALL_DAMAGE_REDUCTION` (−0.5 → half damage),
  `#isInLumenwilds(entity)`, `#refresh(livingEntity)` (server-side; the GRAVITY attr is syncable),
  `#onChangedDimension(player)`, `#remove(livingEntity)`. Native-mob gravity comes via their attribute
  suppliers in Phase 6, so this hook is player-only.

### mixin/ + world/time/ — the half-rate day clock (Phase 7d.1; the project's only Mixins)
- [ServerLevelMixin.java](src/main/java/com/jus144tice/lumenwilds/mixin/ServerLevelMixin.java) —
  `@Mixin(ServerLevel.class)`; `@Shadow @Final @Mutable boolean tickTime` + a `@Unique` setter
  (`LumenwildsTickTime`). Vanilla builds only the Overworld with `tickTime = true`; this lets the Lumenwilds
  be switched on so its `tickTime()` (own clock) runs.
- [DerivedLevelDataMixin.java](src/main/java/com/jus144tice/lumenwilds/mixin/DerivedLevelDataMixin.java) —
  `@Mixin(DerivedLevelData.class)` implementing `LumenwildsTimeData`. Non-Overworld dims mirror the Overworld
  through `DerivedLevelData` (its `setDayTime` is a **no-op** — the reason a custom clock was impossible).
  When `#lumenwilds$decouple`d, `@Inject(HEAD, cancellable)` on the day-time getters/setters
  (`getDayTime`/`setDayTime`/`getDayTimePerTick`/`setDayTimePerTick`/`getDayTimeFraction`/`setDayTimeFraction`)
  reads/writes private fields instead. `gameTime` stays derived (only the day cycle diverges).
- [world/time/LumenwildsTickTime.java](src/main/java/com/jus144tice/lumenwilds/world/time/LumenwildsTickTime.java)
  + [LumenwildsTimeData.java](src/main/java/com/jus144tice/lumenwilds/world/time/LumenwildsTimeData.java) —
  the duck-type interfaces the Mixins implement so non-mixin code can drive them.
- **Mixin tooling:** `src/main/resources/lumenwilds.mixins.json` (config) + a top-level `[[mixins]]` in
  `neoforge.mods.toml`. **No annotation processor or refmap needed** — NeoForge's runtime is mojmap, so mixins
  apply by source name (sponge-mixin 0.8.7 is provided transitively; the config has no `refmap` key).

### network/ + world/event/ — ambient events (Phase 7d.2)
- [world/event/LumenEvent.java](src/main/java/com/jus144tice/lumenwilds/world/event/LumenEvent.java) — the
  event enum: `NONE` / `SPOREFALL` / `MOONWAKE` / `DEEP_HUSH` (`#id`/`#byId` for networking).
- [world/event/LumenEventManager.java](src/main/java/com/jus144tice/lumenwilds/world/event/LumenEventManager.java)
  — the server scheduler (transient static state, reset on server stop). `#tick` advances the timer and runs
  `#applyOngoing` (per-event **boosted spawns** near players: Sporefall→Sporelings in the jungle,
  Moonwake→Lantern Beetles, Deep Hush→Shade Stalkers underground, all capped + placement-checked); `#roll`
  picks the next event (Moonwake is night-only, using the Lumenwilds' half-rate clock); `#setActive` logs +
  `PacketDistributor.sendToPlayersInDimension(LumenEventPayload)`. **Timing constants** (`INITIAL_DELAY`,
  `COOLDOWN/EVENT_MIN/MAX`) tune the cadence.
- [network/LumenEventPayload.java](src/main/java/com/jus144tice/lumenwilds/network/LumenEventPayload.java) —
  `CustomPacketPayload` record (`eventId`, `ticksRemaining`) + `#TYPE`/`#CODEC`.
- [network/ModNetworking.java](src/main/java/com/jus144tice/lumenwilds/network/ModNetworking.java) — mod-bus
  `#onRegisterPayloads(RegisterPayloadHandlersEvent)` → `registrar.playToClient(...)`; the client handler
  writes `LumenEventClientState`.
- [network/LumenEventClientState.java](src/main/java/com/jus144tice/lumenwilds/network/LumenEventClientState.java)
  — a **common** (no client imports) holder of the active event, read by the sky + `client.LumenEventClientEffects`.

### event/ — `@EventBusSubscriber` (game bus), auto-registered
- [CommonEvents.java](src/main/java/com/jus144tice/lumenwilds/event/CommonEvents.java) —
  `#onPlayerLoggedIn` + `#onPlayerRespawn` → `LowGravityHandler#refresh` (rebuilt player entities lose
  transient modifiers, so re-sync them; no-op outside the dimension).
- [PlayerDimensionEvents.java](src/main/java/com/jus144tice/lumenwilds/event/PlayerDimensionEvents.java) —
  `#onPlayerChangedDimension` → "Entering/Leaving the Lumenwilds" action-bar messages + forwards to
  `LowGravityHandler#onChangedDimension`.
- [ProjectileArcHandler.java](src/main/java/com/jus144tice/lumenwilds/event/ProjectileArcHandler.java) —
  `#onEntityTick(EntityTickEvent.Post)`: restores `#FLATTEN_FRACTION` (0.4) of per-tick gravity to
  `AbstractArrow`/`ThrowableProjectile` in-dimension (server-side), for subtly flatter arcs.
- [LumenTimeEvents.java](src/main/java/com/jus144tice/lumenwilds/event/LumenTimeEvents.java) — the
  **half-rate day cycle** activator (7d.1). `#onLevelLoad(LevelEvent.Load)`: for the Lumenwilds `ServerLevel`,
  flips `tickTime` on (via `LumenwildsTickTime`) and decouples its day clock at `#LUMENWILDS_DAY_TIME_PER_TICK`
  (0.5 → a 48,000-tick day) via `LumenwildsTimeData`. The two Mixins do the bytecode work; NeoForge's
  per-dimension time sync carries it to clients. *Side effect: sleeping doesn't advance Lumenwilds time.*
- [LumenEventDriver.java](src/main/java/com/jus144tice/lumenwilds/event/LumenEventDriver.java) — ticks
  `world.event.LumenEventManager` each Lumenwilds `LevelTickEvent.Post` (7d.2); `#onServerStopping` resets it
  (state is transient per session).
- [LumenEffectEvents.java](src/main/java/com/jus144tice/lumenwilds/event/LumenEffectEvents.java) — drives
  `ModMobEffects.GLOWMARKED`'s outline (8a): `setGlowingTag(true/false)` on `MobEffectEvent.Added` /
  `Expired` / `Remove` (not per-tick). The flag syncs to clients → vanilla glowing outline.
- [StillbloomInteractEvents.java](src/main/java/com/jus144tice/lumenwilds/event/StillbloomInteractEvents.java)
  — `#onRightClickBlock(PlayerInteractEvent.RightClickBlock)` (8b): a glass bottle on a Stillbloom Core/Petal
  fills into `ModItems.LUMEN_NECTAR` (bloom not consumed — renewable, like honey).
- [ModBrewing.java](src/main/java/com/jus144tice/lumenwilds/event/ModBrewing.java) — **mod-bus**
  `#onRegisterBrewingRecipes(RegisterBrewingRecipesEvent)` (8h): `builder.addMix(awkward, ingredient, potion)`
  for the four `ModPotions` (Air Gel / Glow Pollen / Spore Sac / Living Fiber).
- [GlowmothAggroEvents.java](src/main/java/com/jus144tice/lumenwilds/event/GlowmothAggroEvents.java) —
  `#onBlockBreak(BlockEvent.BreakEvent)` (6h): when a player breaks a guarded bloom (Moonblossom / any
  Stillbloom part), every `Glowmoth` within ~12 blocks `setTarget`s the culprit — the flower-guardian aggro.
- [ModBlockEntityTypes.java](src/main/java/com/jus144tice/lumenwilds/event/ModBlockEntityTypes.java) —
  mod-bus `#addSignBlocks(BlockEntityTypeAddBlocksEvent)`: adds the Glowwood sign blocks to the vanilla
  `BlockEntityType.SIGN`/`HANGING_SIGN` (modded signs reuse the vanilla block entities).
- [ModEntityEvents.java](src/main/java/com/jus144tice/lumenwilds/event/ModEntityEvents.java) — **mod-bus**
  (Phase 6); `#onAttributeCreation(EntityAttributeCreationEvent)` builds each native mob's `AttributeSupplier`
  (`event.put(...)`) and `#onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent)` declares where on the
  ground a type may spawn (`ON_GROUND` + `Animal::checkAnimalSpawnRules`). **Add each new mob in both.**

### client/ — `@EventBusSubscriber(value = Dist.CLIENT, bus = MOD)`, client-only
- [LumenwildsClient.java](src/main/java/com/jus144tice/lumenwilds/client/LumenwildsClient.java) —
  `#onClientSetup` → `Sheets.addWoodType(ModWoodTypes.GLOWWOOD)` (sign atlas material);
  `#onRegisterClientExtensions(RegisterClientExtensionsEvent)` → Lumenwater's `IClientFluidTypeExtensions`
  (reuses vanilla `water_still`/`water_flow` with a teal tint `0xFF36E0C0`);
  `#onRegisterRenderers(EntityRenderersEvent.RegisterRenderers)` → mob renderers (Phase 6); and
  `#onRegisterDimensionEffects(RegisterDimensionSpecialEffectsEvent)` → binds `LumenDimensionEffects` under
  its `EFFECTS_ID` (Phase 7a). (Boat layers are NOT registered here — vanilla auto-registers them for every
  `Boat.Type`; doing it again crashed.)
- [LumenDimensionEffects.java](src/main/java/com/jus144tice/lumenwilds/client/LumenDimensionEffects.java) —
  the bespoke sky (7a), a `DimensionSpecialEffects` registered for the `lumenwilds:lumenwilds` effects id
  (`#EFFECTS_ID`, = `dimension_type.effects`). `#renderSky` fully replaces vanilla: a deep-indigo→teal
  **twilight dome** (inline `TRIANGLE_FAN` disc), a weak blurred sun, and **Veyra** — an oversized pale moon
  (`#VEYRA_RADIUS` 55 vs. vanilla 20) from `textures/environment/veyra.png`; structure mirrors vanilla
  `LevelRenderer#renderSky` on 1.21.1. `#getBrightnessDependentFogColor` tints fog teal-indigo;
  `#getSunriseColor` returns null (no horizon band). **Visual-only — verify via `runClient`, not a server.**
- `LumenwildsClient#onRegisterParticleProviders(RegisterParticleProvidersEvent)` (7b) — render factories for
  the three atmosphere particles, **reusing vanilla classes** (Lumen Spore → `EndRodParticle.Provider`, Glow
  Pollen → `SuspendedTownParticle.Provider`, Crystal Shimmer → `GlowParticle.GlowSquidProvider`); sprites from
  `assets/lumenwilds/particles/<name>.json` → `textures/particle/<name>.png`. (Visuals verify via `runClient`.)
- [LumenEventClientEffects.java](src/main/java/com/jus144tice/lumenwilds/client/LumenEventClientEffects.java) —
  client `ClientTickEvent.Post` (7d.2): while an event is active (per `network.LumenEventClientState`) and the
  player's in the Lumenwilds, sprinkles event particles (Sporefall→spores, Moonwake→pollen, Deep Hush→shimmer).
  `LumenDimensionEffects#renderSky` also reads that state to **brighten Veyra during a Moonwake**.
- The 10 `MobRenderer`s (`LumenGrazerRenderer`, `ShadeStalkerRenderer`, …) each bake a **bespoke model**
  (Phase 9b — the vanilla-model placeholders are gone): `textures/entity/<name>.png`.
- **Bespoke models (Phase 9b):** [client/model/](src/main/java/com/jus144tice/lumenwilds/client/model/) holds
  one custom `HierarchicalModel` per mob — `SkyJellyModel` (bell + tentacles), `GlowmothModel` (moth + 2 wing
  pairs), `CragWraithModel` (manta + wings/tail), `LanternBeetleModel` (shell + 6 legs + glow abdomen),
  `SporelingModel` (body + mushroom cap), `ShadeStalkerModel` (sleek 4-legged), `LumenGrazerModel` (**6 legs**),
  `RootbackModel` (domed turtle, built ~3×2 to fill the hitbox), `MirelurkerModel` (anglerfish + glowing lure),
  `LumenFishModel` (small fish). [LumenModelLayers.java](src/main/java/com/jus144tice/lumenwilds/client/LumenModelLayers.java)
  declares each `ModelLayerLocation`, registered in `LumenwildsClient#onRegisterLayerDefinitions` and baked in
  the renderer. Textures carry per-box region coloring + a mood-matched **face** (ominous on hostiles, friendly
  on passives; the Sky Jelly is faceless). *(Visual-only — verify via `runClient`; iterate from there.)*
- **Emissive glow (Phase 9c, "native living light"):**
  [client/layer/LumenEmissiveLayer.java](src/main/java/com/jus144tice/lumenwilds/client/layer/LumenEmissiveLayer.java)
  extends vanilla `EyesLayer` (model re-rendered fullbright + additive), driven by a per-mob
  `textures/entity/<name>_glow.png` (glowing regions bright on black). Added to 9 mobs in one place via
  `LumenwildsClient#onAddLayers` (`EntityRenderersEvent.AddLayers` + the `#addGlow` helper). **The Shade Stalker
  is deliberately excluded** — a jump-scare ambusher that flees light; a glow would betray its position.

### util/
- [ResourceLocationHelper.java](src/main/java/com/jus144tice/lumenwilds/util/ResourceLocationHelper.java)
  — `#modLoc(path)` (`lumenwilds:`), `#mcLoc(path)` (`minecraft:`). Use these for all `ResourceLocation`s.

### datagen/ — `./gradlew runData` → `src/generated/resources` (git-ignored, OFF the resource path)
- [DataGenerators.java](src/main/java/com/jus144tice/lumenwilds/datagen/DataGenerators.java) —
  `@EventBusSubscriber(bus = MOD)`, `#gatherData(GatherDataEvent)` wires the six providers below.
- [ModBlockStateProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModBlockStateProvider.java) —
  **dispatches by block type** (`#registerStatesAndModels`): logs/wood → `logBlock`/`axisBlock`,
  stairs/slab/fence/gate/wall/door/trapdoor/button/plate → the matching helper, panes → `paneBlock`,
  `AmethystClusterBlock` → `directionalBlock` of a cutout cross, `BushBlock` → cutout cross,
  signs → `#registerSigns` (particle model), `LiquidBlock` → **skipped** (hand-authored particle model),
  else `cube_all`. `#baseTex(name)` resolves a shape's base texture (Glowwood shapes → planks;
  `_brick`/`_tile` → plural `_bricks`/`_tiles`).
- [ModItemModelProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModItemModelProvider.java) —
  block items inherit `block/<name>` (via `UncheckedModelFile`, to dodge cross-provider validation);
  `BushBlock`/`AmethystClusterBlock` items → flat `item/generated` from the block texture;
  fence/wall/button → `_inventory`, trapdoor → `_bottom`, doors + panes + signs → flat `item/<name>`;
  standalone items (incl. boats) → `basicItem`.
- [ModLanguageProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModLanguageProvider.java) —
  auto names from registry paths (`#addTranslations`, `#titleCase`) + tab title + portal messages,
  **deduped by description id** (SignItem/BlockItem reuse a block's key).
- [ModRecipeProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModRecipeProvider.java) —
  `#buildRecipes`: Lumenbound Stone (`CGC/SAS/CGC`) + Lumen Striker (`I/A/G`); `#buildGlowwoodRecipes`
  (wood set incl. signs, hanging signs, boat + chest boat), `#buildMoonstoneRecipes` +
  `#buildShimmerstoneRecipes` (smelting + 2×2 crafting + stonecutter via helpers `#smelt`/`#square2x2`/`#cut`).
- [ModLootTableProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModLootTableProvider.java) —
  `#create` + inner `ModBlockLoot`: drop-self for all blocks except `LUMEN_PORTAL` + `LUMENWATER_BLOCK`
  (both `noLootTable`), with slab (drops 2) and door (drops 1) special-cased.
- [ModTagProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModTagProvider.java) —
  `#addTags`: classifies blocks by name into `mineable/pickaxe|axe|shovel|hoe` + `leaves` (auto-covers new
  stone/wood blocks; `_ore`/`_cluster`/moonstone → pickaxe).

> NOTE: hand-authored placeholder assets in `src/main/resources` are **authoritative** (the mod works
> from a plain `build`, no datagen needed). `runData` output is a regeneration/diff aid only; it is NOT
> on the resource path, so it can't duplicate-clash with the committed assets. Copy anything worth
> keeping into `src/main/resources`.

## Resources — `src/main/resources`

- [META-INF/neoforge.mods.toml](src/main/resources/META-INF/neoforge.mods.toml) — mod metadata; only
  `neoforge` + `minecraft` deps (required); `enumExtensions = "META-INF/enumextensions.json"` (Glowwood
  `Boat.Type`); `[[mixins]] config = "lumenwilds.mixins.json"` (the 7d.1 day-clock mixins). `pack.mcmeta` →
  `pack_format` 48.
- `lumenwilds.mixins.json` (resource root) — the Mixin config (`package` = `…lumenwilds.mixin`, JAVA_21, no
  `refmap`); lists `ServerLevelMixin` + `DerivedLevelDataMixin`.
- `META-INF/enumextensions.json` — the Glowwood `Boat.Type` entry (constant name `lumenwilds_glowwood` is
  a Java identifier; the constructor's name string `lumenwilds:glowwood` drives textures). See `ModBoatTypes`.
- `assets/lumenwilds/`: `blockstates/`, `models/block|item/`, `textures/block|item/` (**Phase 9 world-art pass:
  all ~53 base block textures are now patterned procedural art — plant shapes, crystal facets, stone speckle,
  brick/tile, wood grain — not flat colours**; `glowvine` is a passable glowing **cross** (vine) model, not a
  cube), `textures/entity/{signs,signs/hanging,boat,chest_boat}/glowwood.png` + `textures/gui/
  hanging_signs/glowwood.png` (sign/boat placeholders), `lang/en_us.json` (display names +
  `itemGroup.lumenwilds` + portal messages `lumenwilds.portal.{entering,leaving}` + `fluid_type.lumenwilds.lumenwater`
  + `entity.lumenwilds.*` mob names). Mob art (placeholders): `textures/entity/lumen_grazer.png` (64×32
  cow-layout) + `shade_stalker.png` (spider) + `lantern_beetle.png` (silverfish) + `sporeling.png` (slime,
  all 64×32) + `mirelurker.png` (salmon) + `lumen_fish.png` (cod), both 32×32, + `sky_jelly.png` (ghast) +
  `glowmoth.png` (endermite) + `rootback.png` (cow) + `crag_wraith.png` (ghast), 64×32, under `textures/entity/`.
  Sky art (7a): `textures/environment/veyra.png` (64×64 pale-moon disc on transparent — the giant Veyra moon).
  Particles (7b): `particles/{lumen_spore,glow_pollen,crystal_shimmer}.json` (texture lists) +
  `textures/particle/<name>.png` (8×8 soft glow dots). Status-effect icons (8a):
  `textures/mob_effect/{lightfoot,glowmarked,sporeblind,rooted}.png` (18×18) + `effect.lumenwilds.*` lang.
  Lumenwater (5e) has a particle-only `blockstates/lumenwater.json` + `models/block/lumenwater.json` (the
  fluid itself renders via the client `IClientFluidTypeExtensions`, not a block model) and a flat
  `lumenwater_bucket` item; it has **no fluid textures** of its own (reuses vanilla water, tinted).
- `data/lumenwilds/`: `recipe/*` (incl. `cooked_grazer_meat` + `cooked_mirefish` furnace/smoker/campfire,
  `glowcap_stew` shapeless [8b], `lumen_anchor` shaped [8c]),
  `loot_table/blocks/*` + `loot_table/entities/*` (mob drops — `lumen_grazer` 6a, `shade_stalker` 6b,
  `lantern_beetle` 6c, `sporeling` 6d, `mirelurker` 6e, `lumen_fish` 6f, `sky_jelly` 6g, `glowmoth` 6h, `rootback` 6i, `crag_wraith` 6j), `dimension/lumenwilds.json` (custom noise gen +
  a **`multi_noise` biome source** — humidity splits `lumen_glade`/`glowroot_forest`, a cold band carves out
  `glasspetal_crags`, a hot+humid band gives `sporefall_jungle`, a mild+wettest band gives `moonmire`, and a
  **deep `depth` band** gives `undercrown_caverns` [5d.5]; one parameter point added per 5d.x) +
  `dimension_type/lumenwilds.json` (`effects` → **`lumenwilds:lumenwilds`** selects the bespoke client sky,
  `ambient_light` 0.2, 7a), and `worldgen/` — `noise_settings/lumenwilds.json` (bespoke terrain +
  surface rules; the router's **`depth` is y-varying** [5d.5] so cave biomes layer under the surface, and
  **`temperature` + `vegetation` are shifted-noise** (Phase 9 fix — they were `0.0` constants, which pinned the
  whole surface to one biome; now all 7 biomes spread) — `continents`/`erosion`/`ridges` stay constant since no
  biome differentiates on them; **`default_fluid` is `lumenwilds:lumenwater`** so seas/aquifers are glowing
  Lumenwater, not vanilla water), `biome/lumen_glade.json` + `biome/glowroot_forest.json`
  (5d.1, dark-teal) + `biome/glasspetal_crags.json` (5d.2, blue-violet) + `biome/sporefall_jungle.json`
  (5d.3, lush green + warped_spore particle) + `biome/moonmire.json` (5d.4, dark glowing swamp) +
  `biome/undercrown_caverns.json` (5d.5, deep cave biome) + `biome/stillbloom_basin.json` (5d.6, rare bright
  sanctuary). **Every biome's `effects` now also carries (7b) an ambient `particle` and (7c) a vanilla-sourced
  soundscape** — `ambient_sound`/`additions_sound`/`music` (Nether ambience loops for the alien biomes, calm
  overworld music for the open ones) + the existing `mood_sound`. Worldgen continues: `noise/hills.json`,
  `configured_feature/` + `placed_feature/` (`lumen_crystal_ore`,
  `patch_moonblossom`, `patch_glow_fern`, `glowwood_tree`, `glowroot_tree` [1×1], `glowroot_tree_2x2`
  [custom feature], `patch_glasspetal` [5d.2], `giant_glowcap` [5d.3], `lumenwater_pool` [5d.4, **custom
  chunk-safe pool feature** — was a crashing vanilla `lake`] +
  `patch_glow_algae` + `patch_lumen_reeds`, `undercrown_glowvine` [5d.5] + placed-only `undercrown_crystal`/
  `undercrown_pool`, `stillbloom` [5d.6, custom feature]; placed-only `glowroot_forest_trees`
  [forest-density 2×2]), and **two town-sized structures** — `structure/glowroot_tree.json`
  + `structure_set/glowroot_tree.json` (spacing 20/sep 7, in `lumen_glade`) and `structure/mega_glowcap.json`
  + `structure_set/mega_glowcap.json` (spacing 20/sep 7, distinct salt, in `sporefall_jungle`), plus the
  **Rootshrine** (8d) — `structure/rootshrine.json` + `structure_set/rootshrine.json` (spacing 14/sep 5, in
  `glowroot_forest`) with a `chests/rootshrine` loot table, and the **Lumenbound Ruins** (8e) —
  `structure/lumenbound_ruins.json` + `structure_set` (spacing 28/sep 8) in the **Overworld**
  (`has_structure/lumenbound_ruins` → `#minecraft:is_overworld`) with `chests/lumenbound_ruins` loot, and the
  **Glasspetal Spires** (8f) — `structure/glasspetal_spires.json` (with a `spawn_overrides.monster` →
  `crag_wraith`) + `structure_set` (spacing 22/sep 7, in `glasspetal_crags`) + `chests/glasspetal_spires` loot,
  and the **Undercrown Relics** (8g) — `structure/undercrown_relics.json` (step `underground_structures`) +
  `structure_set` (spacing 24/sep 8, in `undercrown_caverns`) + `chests/undercrown_relics` loot — each with its
  `tags/worldgen/biome/has_structure/<name>.json` biome tag. Hand-authored (not datagen).
- `data/neoforge/data_maps/block/strippables.json` — axe-stripping (glowwood_log/wood → stripped).
- `data/minecraft/tags/block/mineable/{pickaxe,axe,shovel,hoe}.json` + `leaves`; `tags/block/dirt.json`
  adds lumen grass + moonloam (so BushBlock plants survive on Lumenwilds soil); `tags/fluid/water.json`
  adds Lumenwater (source + flowing) to `#minecraft:water` so it behaves as water (Phase 6.0);
  `tags/entity_type/can_breathe_under_water.json` adds the Mirelurker + Lumen Fish (6e/6f, so they don't drown);
  `tags/block/animals_spawnable_on.json` (Phase 9 fix) adds `lumen_grass_block` + `moonloam` so the native
  fauna (`Animal::checkAnimalSpawnRules` needs the block below in that tag) actually spawn on the Lumenwilds
  surface — without it, the surface is `lumen_grass_block` (not in vanilla's tag) and **no animals spawned at all**.

## Adding content — quick recipes

| Task | Touch (in order) |
| --- | --- |
| **Add a block** | `ModBlocks#<NEW>` (BlockItem is auto-registered by the `ModItems` loop) → add the base `textures/block/<n>.png` (+ `_top` for pillars, `_bottom`/`_top` for doors) → run `runData` (the providers handle blockstate/model/item-model/loot/tag/lang by block type) → copy the new files from `src/generated/resources` into `src/main/resources` → update [Codebase map](#codebase-map). Add a recipe in `ModRecipeProvider` if craftable. |
| **Add an item** | `ModItems#<NEW>` → `models/item/<n>.json` + `textures/item/<n>.png` → lang `item.lumenwilds.<n>` (auto in datagen) → it auto-joins the creative tab. |
| **Add a recipe** | `data/lumenwilds/recipe/<n>.json` (result key is `{"id":…,"count":…}`) and/or `ModRecipeProvider#buildRecipes`. |
| **Add a non-empty registry** | create/populate `Mod*`, then register it on the mod bus in `Lumenwilds` ctor. |
| **Wire dimension/worldgen** | keys in `world/*`; JSON under `data/lumenwilds/{dimension,dimension_type,worldgen/...}`. |

---

## Invariants & gotchas

- **Mixins on NeoForge 1.21.1 need no AP/refmap.** The runtime is mojmap, so mixins apply by source name —
  just write the class, list it in `lumenwilds.mixins.json` (no `refmap` key), and add `[[mixins]] config=…`
  to `neoforge.mods.toml`. `org.spongepowered.asm.mixin.*` is on the compile classpath via NeoForge (no extra
  dependency). Verify a mixin actually *applies* at runtime (boot log: `SpongePowered MIXIN Subsystem …`,
  and no `InvalidInjectionException`) — a green compile only proves it parsed. The half-rate clock (7d.1) was
  confirmed by a temp `ServerTickEvent` logger showing Lumenwilds dayTime advancing at half the Overworld's.
- **`Requested chunk unavailable during world generation` has TWO causes — don't conflate them.** (1) The
  vanilla **`minecraft:lake` feature is not chunk-safe**: it writes up to ~16 blocks from origin, which with
  `in_square` placement reaches a not-yet-generated neighbour and crashes chunk-gen — *position-dependent*, so
  a small force-gen can miss it (it crashed a real playthrough in the Moonmire/Undercrown pools). **Fixed** by
  replacing the lake with `world.feature.LumenwaterPoolFeature` (bounded to ±4, chunk-safe by construction).
  **Lesson: never use `minecraft:lake`; write a bounded custom feature.** (2) Two dev JVMs on the same `run/`
  world throw `OverlappingFileLockException`, which *also* surfaces as "chunk unavailable" — so always kill
  stray dev JVMs (`Get-CimInstance Win32_Process | … 'forgeclientdev|forgeserverdev|modFolders=lumenwilds'`)
  before `runClient`/`runServer`, and verify the user isn't mid-session. To validate worldgen, force-gen a
  **large fresh far region** single-instance (a small one can miss position-dependent feature bugs — use 16×16+
  chunks).
- **Client rendering (sky/particles/fog) can't be validated headlessly.** `DimensionSpecialEffects#renderSky`
  (Phase 7a `LumenDimensionEffects`) only runs in-client after entering the dimension; a server (even with
  force-gen) never calls it. Build + boot confirm registration/no-crash; the *look* needs `runClient` through
  a portal. The effects id is keyed to `dimension_type.effects` (`lumenwilds:lumenwilds`), registered via
  `RegisterDimensionSpecialEffectsEvent`.
- **1.21.1 datapack folders are singular**: `recipe/`, `loot_table/`, `dimension/`, `dimension_type/`,
  `tags/block/`. Pack format **48**. Recipe result uses `{"id":…,"count":…}` (not `"item":`).
- **Biome feature lists must share a globally-consistent order per step** (the engine topo-sorts all biomes'
  features into one order; conflicting relative orders throw *"Feature order cycle found"* at **chunk-gen**,
  NOT at boot — so `runServer` reaching "Done" does NOT catch it). Keep shared features in the same relative
  order in every biome: trees → ground patches, with `patch_glow_fern` **before** `patch_moonblossom`, and
  `patch_moonblossom` last. To actually exercise this (and any custom feature), force-generate Lumenwilds
  chunks — a temp `data/minecraft/tags/function/load.json` → a fn running `execute in lumenwilds:lumenwilds
  run forceload add …` (delete before commit).
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
