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
complete.** **Phase 10 is starting — the Lumenwrights / Vestige Cities (`docs/ancient_cities.txt`):** rare
ruined alien cities built by a vanished civilization. **10a (materials foundation) is in:** the **Luminite**
ore chain (`luminite_ore` in moonstone + `deep_luminite_ore` in deep moonstone → `raw_luminite` → smelt to
`luminite_ingot` → `luminite_block`; ore injected dimension-wide via a `luminite_ore` biome modifier), and the
**Glowbrick** family — the cities' signature luminous brick (`glowbrick` light 6, `cracked_glowbrick` 3,
`ancient_glowbrick` 1 so ruins visibly fade; `glowbrick_tiles`/`chiseled_glowbrick`/`glowbrick_pillar` +
stairs/slab/wall), crafted `L I L / I C I / L I L` (ingot/crystal-shard/glow-pollen) → 4 — plus decay blocks
(`overgrown_glowbrick`, `broken_sporeglass`, `mossy_moonstone_bricks`, `rooted_moonstone`) for the ruin
processors. **10b (first ruins) is in:** the **Small Vestige Outpost** — an uncommon procedural surface ruin
(`world.structure.VestigeOutpostStructure`/`Piece`): a broken glowbrick road, toppled pillars, one roofless
collapsed building shell (still-lit Lumenbulb + a `chests/ruined_cache` chest), an empty plinth, and scattered
debris — all run through the shared `world.structure.VestigeDecay` processors (glowbrick fades
intact→cracked→ancient, chunks go missing, glowvine/lumen-grass creep in), rooted to the ground so nothing
floats. In glade/forest/jungle/basin; "Vestiges of Light" advancement fires inside any
`#lumenwilds:vestige_city`-tagged ruin. **10c (lore layer) is in:** the **Memory Crystal** (a glowing block —
right-click reads a position-hashed fragment of Lumenwright lore via `event.MemoryCrystalInteractEvents`, drops
`memory_crystal_shard`), six **Ancient Glyph Tablets** (`item.GlyphTabletItem` — right-click/tooltip shows a
lore line), and the **Lumen Conduit** (`block.LumenConduitBlock`, `conduit_state` dead/dim/active → light
0/2/8 — decorative in ruins now, made functional by the Resonance network in 10e); plus the
`chests/scholars_reliquary` loot table and "The City Remembers" advancement. **10d (the main city) is in:**
the **Medium Vestige City** (`world.structure.VestigeCityStructure`/`Piece`) — a rare village-sized radial
ruin: a circular chiseled-glowbrick plaza with a dry crystal fountain + flickering light pylons, four broken
roads (embedded dead/dim Lumen Conduit lines) spoking to an outer ring of building "stamps" (crescent house /
hollow pod / archway / root chamber / plinth-with-Memory-Crystal), all decayed + overgrown via `VestigeDecay`;
chests are a Scholar's Reliquary in the civic hall + scattered Ruined Caches; Shade Stalkers/Sporelings via
`spawn_overrides`. **10e.1 (functional Resonance network) is in:** the **Resonance Core**
(`block.ResonanceCoreBlock` + ticking `ResonanceCoreBlockEntity`) floods power through connected Lumen
Conduits (driving them ACTIVE via `block.ResonanceNetwork`'s bounded BFS) and **opens the Ancient Doors**
(`ModBlocks#ANCIENT_DOOR`, an iron-set `DoorBlock` — hand-openable disabled) they touch; removing the core
tears the network down. Craft the core from a looted `resonance_core_fragment` (+ shimmerstone/crystal/
luminite). **10e.2 completes the Resonance subsystem:** the **Gravity Lens** (`block.GravityLensBlock`) — when
the network powers it, it gently lifts entities in the column above (`event.LumenGravityEvents`, controlled
velocity per `docs/lumenwright_liftshafts.txt`, not the gravity attribute) — plus its **Cracked Gravity Lens**
(drops `gravity_lens_fragment`); the **Lumen Relay** (`block.LumenRelayBlock`, bridges the network across
gaps); and the **Dormant Light Engine** (`block.DormantLightEngineBlock`) — right-click with a
`resonance_core_fragment` to **restore** it into an **Active Light Engine** (`block.ActiveLightEngineBlock`, a
Resonance Core), waking a dead city's network: the bible's "city's heartbeat returns" payoff. *(The full
paired descent/ascension liftshafts + craftable Lumen Field Projector + abandoned-mine structures from the
liftshaft doc are a later follow-up; 10e.2's gravity lens + `gravity_lens_fragment` are forward-compatible
with it.)* **10f.1 (the Echo Sentinel) is in:** the city-specific ruin guardian (`entity.EchoSentinel`) — a
floating construct (near-zero gravity, flying nav) that attacks with a charged **light-pulse beam**
(`entity.ai.LightPulseAttackGoal` — a hitscan line of particles + damage, not a projectile entity), bespoke
model (`client.model.EchoSentinelModel` — shell + crystal eye + orbiting ring fragments) + emissive glow.
Drops `resonance_core_fragment`/`luminite_ingot`/`memory_crystal_shard`/rare `lumen_relay`; spawns rarely in
the Undercrown; "Still On Watch" advancement on kill. **10f.2 (the vault) completes Phase 10f:** every Medium
Vestige City now generates a buried **Vestige Vault** (`world.structure.VestigeVaultPiece`, a 2nd piece added
under the plaza by `VestigeCityStructure`) — a Deep-Moonstone + glowbrick chamber holding the **resonance
lock-and-key**: a dead central Dormant Light Engine wired by dead Lumen Conduits to two sealed Ancient Doors
that guard a Vault chest + an Engineer's Cache; **restore the engine (a fragment) → conduits power → doors
open** (the 10e tech as a dungeon mechanic), with an Echo Sentinel spawner on guard, Memory Crystals for lore,
and a corner spiral stair up to a hole in the plaza. **10g (spires + grand cities) is in:** `VestigeCityStructure`
now rolls a **size tier** — ~25% are **Grand** (a larger second building ring + a central restorable **Dormant
Light Engine** monument on the plaza + 1–2 **Vestige Spires**), the rest medium; the **Vestige Spire**
(`world.structure.VestigeSpirePiece`) is a broken tapering glowbrick tower — shattered upper floors, an exposed
Lumen-Crystal core, jutting stair fragments, floating debris, and a `chests/spire` reward. "Under a Dead
Skyline" advancement on reaching a city plaza. **10h is in progress** (the finale): **10h.1 (craftability) is
in** — the buildable Lumenwright tech is now craftable so a player who's looted ancient fragments + mined
resources can rebuild the kit (Lumen Conduit, Lumenbulb, Memory Crystal from shards, Active Light Engine, +
aged building variants); the *fragments* stay loot-only (the ruin gate). **10h.2 (weathered foundations)** makes
slope foundations read as crumbling supports (`VestigeDecay#weatheredFoundation`). **10h.3 (biome flavor)** —
the city reads its biome (`VestigeCityPiece#flavorFor`/`#applyFlavor`) and scatters distinct accents:
**overgrown** (Glowroot Forest / Sporefall Jungle — glowvine/glow-fern/Glowroot-log roots), **cracked-spire**
(Glasspetal Crags — Glasspetal Clusters + exposed Luminite veins), and **sunken** (Moonmire — glow algae,
lumen reeds, rooted moonstone over the drowned ruin; the swamp's pools do the flooding). Crags + Moonmire added
to the city biome tag — all three flavors in. **Phase 10 (the Lumenwrights / Vestige Cities) is complete.**
What is deliberately *not* built (deferred optional tail): a surface resonance "sanctum", ruin hazards/traps,
the Lumenwright armor trim (art-gated), and the ambient music disc (audio-gated). **Phase 11 is starting — the
Lumenwright Liftshafts (`docs/lumenwright_liftshafts.txt`):** the cities' signature gravity-elevator tech. **11a
(field blocks + ride physics) is in:** two non-solid, no-collision, unbreakable, glowing "gravity column" cells —
`block.AscensionFieldBlock` (light 7) and `block.DescentFieldBlock` (light 5), sharing `block.AbstractFieldBlock`
(built like `portal.LumenPortalBlock`: empty shape, `entityInside`, hand-authored translucent model, no BlockItem,
`noLootTable`). Their `entityInside` applies the bible's *controlled velocity* (not gravity-attribute hacking,
reusing `event.LumenGravityEvents#lift`'s cap/step/`ClientboundSetEntityMotionPacket` re-sync): ascension eases up
to +0.40 (sneak holds, a jump past the cap is preserved), descent holds a safe −0.35…−0.45 band; both zero fall
distance every tick (so descent is a safe drop). **11b (the player-craftable elevator kit) is in:** the **Lumen
Field Projector** (`block.LumenFieldProjectorBlock` + ticking `LumenFieldProjectorBlockEntity` — the 3rd block
entity) projects a gravity column out of one face — Ascension up / Descent down per its `MODE` (right-click to
toggle; chat + tooltip show which) — recomputed each tick by `block.LiftShaftNetwork` (walk the column, drop a
field cell per open space up to a 16-cell budget, stop at solid, clear cells it no longer owns); the
**Gravity Repeater** (`block.GravityRepeaterBlock`) is a flush wall block that resets that budget whenever a
column cell touches it, so wall-mounted repeaters chain a shaft arbitrarily tall without cluttering it. The
projector is standalone-powered (no Resonance network); crafted `G L G / R C R / I E I` (gravity-lens-fragment /
crystal-shard / lumen-relay / resonance-core-fragment / luminite-ingot / memory-crystal-shard), the repeater
cheaply (shimmerstone + fragment + relay). "Carried by the Field" advancement on obtaining a projector. **11c
(the mine access dais + abandoned Luminite mine) is in:** `world.structure.VestigeMinePiece` — a single tall
sub-piece a Vestige City rolls (grand always, medium ~40%; offset from the plaza, clear of the vault shaft;
`VestigeCityStructure` adds it) that spans a deep carved-Moonstone mine chamber (arched Glowbrick ribs, exposed
Luminite + Lumen-Crystal ore, dead conduits, broken Gravity Lenses, a Shimmerstone lift platform, an Echo
Sentinel spawner, Memory-Crystal lore, Miner's + Engineer's-Mine caches) up two side-by-side shafts — each
built from **real, lootable components a player can reverse-engineer** (`VestigeMinePiece#gravityColumn`): a
Lumen Field Projector (its BE floods the column at runtime) + Gravity Repeaters spaced so the 16-cell budget
terminates the field **exactly** at each ramp (last repeater 16 from the end + a near-anchor → clean step-in/out,
no overshoot; mine a repeater and the field above drops). **Descent** projector on a short glowbrick head above
the dais (walk in beneath it), **ascension** projector flush in the chamber floor. Fields span chamber-floor →
one above the dais (reachable at both ends). The shafts open onto a surface octagonal Glowbrick-Tiles dais —
offset to the **city edge** (~22, clear of the buildings), terrain **carved away above** so it's never buried,
marked by four tall **glowing Lumen-Crystal/Lumenbulb pylons** so it's findable. The Engineer's-Mine Cache also
carries the loose tech so the player can rebuild it at home. Mine lore added to `event.MemoryCrystalInteractEvents`. **11d (atmosphere, "alive + purposeful")
is in:** bespoke `ModParticles#ASCENSION_MOTE`/`#DESCENT_MOTE` (EndRod factories) stream up/down the columns with
horizontal energy-ring pulses + pitched hums (ascension high / descent low) via `block.AbstractFieldBlock#animateTick`;
the Lumen Field Projector hums + glints from its working face (`LumenFieldProjectorBlock#animateTick`); and the
mine reads its biome for accents (`VestigeMinePiece#flavor` — Crags glasspetal/crystal seams, Moonmire seeped
Lumenwater pools, forest/jungle Glowroot-log roots breaking the walls). **Cave-aware placement is also in:** the
mine probes the dais column with `ChunkGenerator#getBaseColumn` (read-only noise terrain — the same probe vanilla
structures use, so no chunk-load risk; this dim's caverns are noise caves and show up in it) and, when it finds an
open pocket over a solid floor, drops the chamber **at that cavern** and `VestigeMinePiece#breach`es its lower
walls so it opens into the real cave — else it falls back to the fixed artificial depth
(`VestigeMinePiece#findCaveFloor`, called from `VestigeCityStructure#findGenerationPoint`). **Phase 11
(Lumenwright Liftshafts) is complete (11a–11d, incl. cave-aware mines).** **Force-gen-verified:** a 1522-chunk dense-city test generated 380
city + 15 mine chunks (descent/ascension fields + both caches present) with zero exceptions — and **fixed a
latent crash it surfaced:** `VestigeCityPiece`/`VestigeMinePiece` read `level.getBiome(origin)` in `postProcess`
(→ "Requested chunk unavailable"); biome flavor is now decided at placement and passed in (see the
getBiome-in-postProcess gotcha). **Playtest-confirmed (Phase 11):** the component-built shafts fill + ride
cleanly (step in at the floor, out at the dais), the pylon-marked edge dais is easy to find, tall hanging
glowvine, and the spawn fixes all check out. **Post-playtest polish (this pass):** native fauna that read as
ambient — **Sporeling** + **Mirelurker** — now spawn **light-agnostically** (`event.ModEntityEvents`,
`checkAnyLightMonsterSpawnRules`) so the dim Sporefall Jungle / Moonmire actually teem (the Shade Stalker stays
darkness-gated — fleeing light is its identity); the **Moonmire** gained Glowmoth + Sky Jelly ambient creatures;
**Glowberry Bushes** are right-click-harvestable (now `block.GlowberryBushBlock`, v1.1c); **Undercrown Glowvine** now
hangs in tall strands from cave ceilings (`world.feature.UndercrownDecorFeature`); and **Glowroot tree roots**
reliably reach the ground (`world.feature.GlowrootShape` buttress-leg fix — the drop started on the tip's own
log and stopped instantly). **Playtest-confirmed (10a–10g):** the full
city→vault→restore-engine→open-doors→loot loop works (fixed
in-session: vault doors, Echo Sentinel spawn in light, guaranteed fragment sources, dry-land placement).
Roadmap:
[the plan](.claude/plans/delegated-juggling-locket.md)
(10a–10h). What is deliberately
*not* built yet: the final art/audio/polish pass (Phase 9) — and the
visual-only deferrals logged throughout (final mob models, the Sporeblind overlay, real `.ogg` audio, etc.). **All biomes share one terrain *height*** (only `depth` varies, for the cave
layer) — per-biome terrain silhouette is a deferred cross-cutting pass (see IMPLEMENTATION_PLAN). **Post-1.0
playthrough fixes (v1.1) are in progress** (see [the plan](.claude/plans/delegated-juggling-locket.md)):
**v1.1a** gave **Glowroot a full wood set** (the signature self-lit species — planks/wood/stairs/slab/fence/
gate/door/trapdoor/button/plate/signs/boats, all faintly glowing, on `ModWoodTypes.GLOWROOT`), closing the
"can't make planks from Glowroot logs" gap. **v1.1b** fixed **flora generating on top of bushes**
(moonblossom + glow fern used `matching_blocks:air` with no survival check → floated above glowberry bushes;
now `would_survive` like every other patch, on `WORLD_SURFACE_WG`). **v1.1c** made **Glowberries plantable +
renewable** — `GLOWBERRY_BUSH` is now a sweet-berry-style `block.GlowberryBushBlock` (`AGE 0..3`,
bone-mealable, glow 3→6, right-click-harvest), and the Glowberry item is an `ItemNameBlockItem` that plants it.
**v1.1d** gave **every orphan mob drop a use** (`ModRecipeProvider#buildOrphanDropRecipes` — hides→leather,
glow sinew→string, lumen algae→green dye, wraith membrane→phantom membrane, mire tooth→bone meal, rootback
plate→iron nuggets, glow scales→glow pollen, shade claw→echo dust, crystal dust→glasspetal block, moonloam
clumps→moonloam; glowcap spores brew Sporeblind in `event.ModBrewing`; several also become Lumenwater fishing
bait in v1.1f). *(This pass also surfaced + fixed a pre-existing gap: several craftable recipes —
`ancient_door`, `resonance_core` — and many recipe-unlock advancements were defined in `ModRecipeProvider`
but never copied into `src/main/resources`, so they weren't actually obtainable/shown; all are now shipped.)*
**v1.1e** added **cooking-mod integration** — the lumen foods/crops are tagged into the universal `#c:`
convention tags (`data/c/tags/item/foods*`, `crops`) that **Farmer's Delight, Create, and Delightful all
read**, so lumen ingredients drop into those mods' tag-based recipes with no hard dependency. *(Bespoke
per-mod dishes — FD `cutting`/`cooking`, Create `mixing` — are deferred: their foreign recipe-type schemas
can't be verified without the mods present, and a malformed one would error in the pack; the `#c:` tags give
the universal integration safely.)* **v1.1f** added **Lumenwater fishing** — a NeoForge built-in
`neoforge:add_table` global loot modifier (`data/.../loot_modifiers/`, indexed by
`data/neoforge/loot_modifiers/global_loot_modifiers.json`) gated by `location_check` (dimension +
`#lumenwilds:lumenwater` fluid) appends a bonus lumen catch (native fish/flora/materials/treasure) to vanilla
fishing — no custom Java. **v1.1g** made the dimension's effects into **fished "spell-book" enchantments** —
six data-driven enchantments (`registry.ModEnchantments` keys + `data/lumenwilds/enchantment/*.json`): armor
*while-worn* (`minecraft:tick`→`apply_mob_effect`: Lightfooted/Nightsight/Lumenward) and weapon *on-hit*
(`post_attack`: Glowbrand/Sporestrike/Rootbinding), obtainable **only** as enchanted books from Lumenwater
fishing (kept out of the enchanting table/trades/loot tags). *(All verified loading clean on a headless
server: GLM + enchantments + loot + tags, no datapack errors.)* **v1.1i** hardened **Lumenwater = water**
for compat (e.g. a water-allergic cat-people race mod): it was already in `#minecraft:water` + returns
`isInWater()` true; now also in the NeoForge convention `#c:water` (`data/c/tags/fluid/water.json`). Only a
mod that hard-codes `Blocks.WATER`/`Fluids.WATER` would still miss it (unreachable from our side). **v1.1h**
added the **in-game player guide** — a **Patchouli** book (`lumenwilds:lumenwilds_guide`, 7 categories / ~24
entries). Patchouli is an **optional/soft dependency** (`runtimeOnly` for dev, `optional` in
`neoforge.mods.toml`, zero Java API used — the mod loads + plays without it). It's craftable (**book + glowstone
dust**, `mod_loaded`-gated) and also auto-added to the "The Lumenwilds" creative tab. **The book text is fully
cross-linked** — every look-up-able term is a real Patchouli `$(l:entry)` link (NOT decorative emphasis; note
`$(l)` *alone* is Patchouli's LINK macro and renders `[ERROR]` without a target — use `$(l:entryId)…$(/l)` for
links and plain text otherwise). There's one entry per biome (Lumen Glade/Glowroot Forest/Glasspetal Crags/
Sporefall Jungle/Moonmire/Undercrown Caverns/Stillbloom Basin) + a Veyra/sky entry so biome names link to
them. **v1.1 (playthrough fixes) is complete (a–i).** **v1.1.1 (playthrough #2 fixes):** the wood sets now
carry the **vanilla wood tags** (`#minecraft:planks` + all `wooden_*`/sign/sapling/log tags, block + item via
the new `datagen.ModItemTagProvider`) so Glowwood/Glowroot planks craft crafting tables/chests/etc. and burn as
fuel; the **wood sets now glow** — both species are **emissive-rendered** (every shape's block model inherits
an `_emissive_*` parent with `neoforge_data block_light`, so they look luminous in any light incl. daylight)
AND emit light (logs/wood 7, planks + derived 5); the **Glowroot
buttress roots reliably join the trunk** (place-then-advance fix in `GlowrootShape#buildButtressRoots`); the
**Lumen Grazer breeds with the renewable Glowberry** (not just the rare Lumen Fruit); and the **built/loot
structures moved to the `top_layer_modification` step** so they generate *after* trees and overwrite them (no
more tree-through-chest / tree-corrupted pieces). **v1.1.3 (Lumenwater fishing rework):** fishing in Lumenwater
now yields **only native species** — a custom `loot.LumenwaterFishingModifier` (registered via
`registry.ModLootModifiers`) **replaces** the vanilla catch (no more earth cod/pufferfish) with a roll of
`loot_table/gameplay/fishing/lumenwater.json` (native fish — `glimmerfish`/`cooked_glimmerfish`/`sporefin` +
mirefish + a rare live `lumen_fish_bucket`; lumen junk; treasure that **keeps** vanilla enchanted rod/bow/book
+ the 6 fished spell-book enchantments); and a `mixin.FishingHookMixin` restores the fish-strike bubble/splash
animation over Lumenwater (vanilla hardcodes those particles to `Blocks.WATER`). **v1.1.3 also added glowing
wood storage:** `#GLOWWOOD_BARREL`/`#GLOWROOT_BARREL` (vanilla `BarrelBlock` added to `BlockEntityType.BARREL`
via `event.ModBlockEntityTypes`, like the signs; emissive `_emissive_cube_bottom_top` models so they glow +
light 7) — and **fixed a latent v1.1a bug where the Glowroot signs were never added to `BlockEntityType.SIGN`/
`HANGING_SIGN`** (so their text didn't save). It also added glowing **chests**: `#GLOWWOOD_CHEST`/`#GLOWROOT_CHEST`
(vanilla `ChestBlock` with our shared `ModBlockEntities#LUMEN_CHEST` BE [`block.LumenChestBlockEntity`] +
`client.LumenChestRenderer`, which picks the species texture from the chests atlas and renders fullbright to
glow; light 7). **v1.2.0 (playthrough #4):** added a full in-dimension **tool progression** — **Moonstone**
(stone-tier, from Cobbled Moonstone) and **Luminite** (iron-tier, from Luminite Ingots) pickaxe/axe/shovel/
hoe/sword sets (`registry.ModToolTiers` + tool items in `ModItems`, enchantable like vanilla); **fixed harvest
gating** — blocks now carry `#minecraft:needs_stone_tool`/`needs_iron_tool` so a wooden pickaxe no longer mines
everything and harvest-HUD mods read the right tier (was missing — `requiresCorrectToolForDrops()` with no tier
tag = "any tool works"); made the **Sporeling neutral** (`entity.Sporeling` — no auto-target, retaliate-only,
still swarms + death cloud); and added the **Sporeman** (`entity.SporeTrader`) — a rare "fully grown Sporeling"
wandering **trader** of the Sporefall Jungle (an `AbstractVillager` that sells Lumenwilds goods for Overworld
valuables; neutral, fights back if struck; reuses the scaled-up `SporelingModel`). Roadmap:
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
  BlockSetType must register before blocks build). Also `container.registerConfig(COMMON, LumenConfig.SPEC)`
  (Phase 9h). `#onCommonSetup` logs only. **When you add a new (non-empty) DeferredRegister, register it here.**

### config/ — gameplay config (Phase 9h)
- [LumenConfig.java](src/main/java/com/jus144tice/lumenwilds/config/LumenConfig.java) — a `COMMON`
  `ModConfigSpec` (`#SPEC`), registered in the `Lumenwilds` ctor. `#GRAVITY_STRENGTH` (0.1–1.0, read by
  `effects.LowGravityHandler#apply`), `#AMBIENT_EVENTS` (toggle, read by `world.event.LumenEventManager#roll`),
  `#DAY_CYCLE_MULTIPLIER` (0.1–2.0, read by `event.LumenTimeEvents` — 0.5 = the 48k half-rate day). Read at
  runtime so an edit applies on the next dimension entry / world load.

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
  (`fluid.LumenwaterBlock`, the Lumenwater liquid block — **no BlockItem, `noLootTable`**, 5e). **Underwater life
  (Phase 9 drawing-board):** `#LUMENSAND` (soft-glowing seabed, light 6 — placed under water by the surface rule
  instead of dead moonloam), `#LUMEN_CORAL_BLOCK` (solid bright coral, light 10 — reef mounds + building), and
  `#LUMEN_CORAL` (`block.LumenCoralBlock` — a waterlogged glowing cross frond, light 9), and `#LUMEN_KELP` (another
  `LumenCoralBlock`, teal-green sea plant); grown on the seabed by the `world.feature.LumenReefFeature` (reef fronds
  are a coral/kelp mix). **Surface harvestables (Phase 9 / v1.1c):** `#GLOWBERRY_BUSH`
  (`block.GlowberryBushBlock` — a bone-mealable `AGE 0..3` sweet-berry-style bush, glow 3→6, right-click-harvest;
  **no own BlockItem** — planted by the `ModItems#GLOWBERRY` `ItemNameBlockItem`) scattered (at age 3) on the green
  biomes via `patch_glowberry` + a `glowberry` biome modifier.
  **Lumenwright materials (Phase 10a, `docs/ancient_cities.txt`):** `#LUMINITE_ORE` + `#DEEP_LUMINITE_ORE`
  (`DropExperienceBlock`, drop `ModItems#RAW_LUMINITE`, non-glowing metal — contrast with the self-lit crystal
  ore; injected dimension-wide via the `luminite_ore` biome modifier), `#LUMINITE_BLOCK` (ingot storage);
  the **Glowbrick** family (helpers `glowbrickProps(light)`/`glowbrickStairs`): `#GLOWBRICK` (light 6),
  `#CRACKED_GLOWBRICK` (3), `#ANCIENT_GLOWBRICK` (1) — the fading-light decay chain — `#GLOWBRICK_TILES`,
  `#CHISELED_GLOWBRICK`, `#GLOWBRICK_PILLAR`, `#GLOWBRICK_STAIRS`/`#GLOWBRICK_SLAB`/`#GLOWBRICK_WALL` (blast
  resistance 9, > stone); decay/overgrowth variants `#OVERGROWN_GLOWBRICK`, `#BROKEN_SPOREGLASS`,
  `#MOSSY_MOONSTONE_BRICKS`, `#ROOTED_MOONSTONE` (for the ruin processors in later 10x phases). **Lumenwright
  lore tech (10c):** `#MEMORY_CRYSTAL` (emissive glowing block, light 11 — right-click lore via
  `event.MemoryCrystalInteractEvents`, drops `ModItems#MEMORY_CRYSTAL_SHARD`), `#LUMEN_CONDUIT`
  (`block.LumenConduitBlock`, `conduit_state` dead/dim/active → light 0/2/8). **Resonance tech (10e.1):**
  `#RESONANCE_CORE` (`block.ResonanceCoreBlock` + BE — the network power source, light 10), `#ANCIENT_DOOR`
  (iron-set `DoorBlock`, opened only by the Resonance network). **Gravity tech (10e.2):** `#GRAVITY_LENS`
  (`block.GravityLensBlock`, powered → lifts entities above), `#CRACKED_GRAVITY_LENS` (drops the fragment),
  `#LUMEN_RELAY` (`block.LumenRelayBlock`, network gap-bridge), `#DORMANT_LIGHT_ENGINE` /
  `#ACTIVE_LIGHT_ENGINE` (`block.DormantLightEngineBlock`/`ActiveLightEngineBlock` — restore with a fragment).
  **Liftshafts (Phase 11):** `#ASCENSION_FIELD`/`#DESCENT_FIELD` (`block.AscensionFieldBlock`/`DescentFieldBlock`,
  the projected gravity-column cells — no BlockItem, `noLootTable`, 11a), `#LUMEN_FIELD_PROJECTOR`
  (`block.LumenFieldProjectorBlock` + BE — the player-craftable column source, 11b), `#GRAVITY_REPEATER`
  (`block.GravityRepeaterBlock` — flush wall range-extender, 11b).
  **Phase 4 sets**
  (helpers `moonCube/moonStairs/moonSlab/moonWall`, `deep*`, `shimmer*`, `logProps/planksProps`):
  Glowwood wood set (`#GLOWWOOD_LOG` pillar, `#GLOWWOOD_WOOD`, stripped log/wood, `#GLOWWOOD_PLANKS`,
  `#GLOWWOOD_LEAVES`, stairs/slab/fence/fence_gate/door/trapdoor/button/pressure_plate + signs
  `#GLOWWOOD_SIGN`/`#GLOWWOOD_WALL_SIGN`/`#GLOWWOOD_HANGING_SIGN`/`#GLOWWOOD_WALL_HANGING_SIGN` — all using
  `ModWoodTypes.GLOWWOOD`/`GLOWWOOD_SET`); **Glowroot wood set (v1.1a)** — the identical full set
  (`#GLOWROOT_WOOD`, stripped log/wood, `#GLOWROOT_PLANKS`, stairs/slab/fence/fence_gate/door/trapdoor/button/
  pressure_plate + the four signs) on `ModWoodTypes.GLOWROOT`/`GLOWROOT_SET`, but **self-lit** (helpers
  `glowrootLogProps` light 4 / `glowrootPlanksProps` light 3) since Glowroot is the glowing species
  (`#GLOWROOT_LOG`/`#GLOWROOT_LEAVES`/`#GLOWROOT_SAPLING` declared earlier); Moonstone set (smooth/bricks/chiseled/tiles/**cracked_bricks** + stairs/slabs/
  walls); Deep Moonstone (deepslate-analog: cobbled/polished/bricks/tiles/**cracked_bricks** + shapes); Shimmerstone
  (polished/bricks/tiles/pillar/glass + shapes); Sporeglass (`TransparentBlock`) + pane (`IronBarsBlock`).
  **Add a block here → it auto-gets a BlockItem in `ModItems` (loop); add asset + loot via datagen.**
- [ModWoodTypes.java](src/main/java/com/jus144tice/lumenwilds/registry/ModWoodTypes.java) — bespoke
  `#GLOWWOOD` (`WoodType`, name `lumenwilds:glowwood` → sign textures) + `#GLOWWOOD_SET` (`BlockSetType`),
  plus `#GLOWROOT`/`#GLOWROOT_SET` (v1.1a, the Glowroot wood set). NOT DeferredRegister content; `#init()`
  is called early in the `Lumenwilds` ctor. Both are registered with `Sheets` in `LumenwildsClient#onClientSetup`.
- [ModBoatTypes.java](src/main/java/com/jus144tice/lumenwilds/registry/ModBoatTypes.java) — `#GLOWWOOD_BOAT_TYPE`
  + `#GLOWROOT_BOAT_TYPE` (`EnumProxy<Boat.Type>`) add Glowwood/Glowroot `Boat.Type`s via NeoForge enum extension
  (`META-INF/enumextensions.json` + the `enumExtensions` key in `neoforge.mods.toml`); `#glowwood()`/`#glowroot()` →
  the created types. Reuses vanilla `Boat`/`ChestBoat`/renderer — no custom entity.
- [ModItems.java](src/main/java/com/jus144tice/lumenwilds/registry/ModItems.java) — `#ITEMS`
  (`DeferredRegister.Items`). Standalone: `#LUMEN_STRIKER` (`LumenStrikerItem`, **durable: `stacksTo(1)
  .durability(64)`** — each ignition costs 1 use), `#LUMEN_CRYSTAL_SHARD`, `#GLOW_POLLEN`,
  `#LIVING_FIBER`, `#RAW_LUMINITE`/`#LUMINITE_INGOT` (10a — ore → raw → smelt to ingot; ingot crafts Glowbrick),
  `#MEMORY_CRYSTAL_SHARD` + six `#GLYPH_TABLET_*` (`item.GlyphTabletItem`, lore items, 10c),
  `#RESONANCE_CORE_FRAGMENT` (10e — crafts the Resonance Core; loot/Echo-Sentinel drop),
  `#GRAVITY_LENS_FRAGMENT` (10e.2 — from a cracked lens / loot; crafts the Gravity Lens + future projector),
  `#LUMEN_FRUIT` (**food**, 8b — brief night vision), `#LUMEN_NECTAR` (**food**, 8b — brief
  regen; collected from a Stillbloom with a bottle via `event.StillbloomInteractEvents`), `#AIR_GEL`,
  `#GLOWCAP_STEW` (**food**, 8b — bowl + glowcap + lumen fruit + moonblossom → hunger + night vision, returns
  a bowl via `usingConvertsTo`), `#LUMENWATER_BUCKET` (`BucketItem` over
  `ModFluids.LUMENWATER`, 5e); **mob drops + spawn eggs (Phase 6):** `#RAW_GRAZER_MEAT`/`#COOKED_GRAZER_MEAT`
  (foods), `#GRAZER_HIDE`, `#GLOW_SINEW`, `#LUMEN_GRAZER_SPAWN_EGG` (`DeferredSpawnEggItem`) — all 6a;
  `#SHADE_CLAW`/`#DARK_HIDE`/`#ECHO_DUST` + `#SHADE_STALKER_SPAWN_EGG` (6b); `#LANTERN_BEETLE_SPAWN_EGG` (6c —
  the Bottled Lantern Beetle is a *block*, `ModBlocks#BOTTLED_LANTERN_BEETLE`); `#SPORE_SAC`/`#GLOWCAP_SPORES`
  + `#SPORELING_SPAWN_EGG` (6d); `#MIRE_TOOTH`/`#LUMEN_ALGAE`/`#RAW_MIREFISH`/`#COOKED_MIREFISH` (foods) +
  `#MIRELURKER_SPAWN_EGG` (6e); `#LUMEN_FISH_BUCKET` (`MobBucketItem`) + `#LUMEN_FISH_SPAWN_EGG` (6f);
  native fishing fish (v1.1.3) `#GLIMMERFISH`/`#COOKED_GLIMMERFISH` + `#SPOREFIN` (a pufferfish-analog food that
  inflicts Sporeblind);
  `#SKY_JELLY_SPAWN_EGG` (6g — drops the existing `#AIR_GEL`); `#GLOW_SCALES` + `#GLOWMOTH_SPAWN_EGG` (6h);
  `#ROOTBACK_PLATE`/`#MOONLOAM_CLUMPS` + `#ROOTBACK_SPAWN_EGG` (6i); `#WRAITH_MEMBRANE`/`#CRYSTAL_DUST`
  + `#CRAG_WRAITH_SPAWN_EGG` (6j); `#ECHO_SENTINEL_SPAWN_EGG` (10f); boats
  `#GLOWWOOD_BOAT`/`#GLOWWOOD_CHEST_BOAT` + `#GLOWROOT_BOAT`/`#GLOWROOT_CHEST_BOAT`
  (`BoatItem` over `ModBoatTypes.glowwood()`/`glowroot()`); signs `#GLOWWOOD_SIGN`/`#GLOWROOT_SIGN` (`SignItem`) +
  `#GLOWWOOD_HANGING_SIGN`/`#GLOWROOT_HANGING_SIGN`
  (`HangingSignItem`) — wall variants share these. **Tools (v1.2):** `#MOONSTONE_PICKAXE`/`_AXE`/`_SHOVEL`/
  `_HOE`/`_SWORD` + `#LUMINITE_*` (built by the private `pickaxe/axe/shovel/hoe/sword` helpers — each wires the
  `ModToolTiers` tier + `.attributes(...)`); `#SPORE_TRADER_SPAWN_EGG`. A **static loop auto-registers a simple
  `BlockItem` for every block except `LUMEN_PORTAL`, `LUMENWATER_BLOCK`, and the sign blocks** (runs after the
  standalone/sign items so the striker stays first in the tab) — new blocks get an item with no edits here.
- [ModCreativeTabs.java](src/main/java/com/jus144tice/lumenwilds/registry/ModCreativeTabs.java) —
  `#CREATIVE_MODE_TABS`, `#LUMENWILDS_TAB` (id `lumenwilds`, title key `itemGroup.lumenwilds`, icon =
  Lumen Striker). **Auto-populates from `ModItems.ITEMS`** — new items appear without editing this file.
- [ModFluidTypes.java](src/main/java/com/jus144tice/lumenwilds/registry/ModFluidTypes.java) — `#FLUID_TYPES`
  (`NeoForgeRegistries.Keys.FLUID_TYPES`); `#LUMENWATER_TYPE` (`FluidType`, light 4, no infinite source). The
  non-state half of Lumenwater (5e). **Functions as water (Phase 6.0):** `canExtinguish`/`canHydrate`/
  `supportsBoating` set (swim/drown/push default true) **and** both fluids are in `#minecraft:water`
  (`data/minecraft/tags/fluid/water.json`) **and `#c:water`** (`data/c/tags/fluid/water.json`, v1.1i) — so
  boats float, farmland hydrates, fire extinguishes, fish survive, and tag/`isInWater`-based water detection
  (e.g. a water-allergy mod) treats Lumenwater as water. Glow + overworld-decay are unaffected.
- [ModFluids.java](src/main/java/com/jus144tice/lumenwilds/registry/ModFluids.java) — `#FLUIDS`;
  `#LUMENWATER` (source) + `#LUMENWATER_FLOWING` (`BaseFlowingFluid`). `#props()` lazily wires
  type↔still↔flowing↔block↔bucket (avoids a static forward-ref). The fluid registers **before** blocks, so
  `ModBlocks.LUMENWATER_BLOCK`'s factory can call `LUMENWATER.get()`.
- [ModEntities.java](src/main/java/com/jus144tice/lumenwilds/registry/ModEntities.java) — `#ENTITIES`; the
  native fauna (Phase 6). `#LUMEN_GRAZER` (`CREATURE`, 6a), `#SHADE_STALKER` (`MONSTER`, 6b), `#LANTERN_BEETLE`
  (`CREATURE`, flying, 6c), `#SPORELING` (`MONSTER`, swarm, 6d), `#MIRELURKER` (`MONSTER`, amphibious, 6e),
  `#LUMEN_FISH` (`WATER_AMBIENT`, schooling fish, 6f), `#SKY_JELLY` (`CREATURE`, floating, 6g), `#GLOWMOTH`
  (`CREATURE`, neutral flying guardian, 6h), `#ROOTBACK` (`CREATURE`, massive 3.0×2.2 turtle, 6i),
  `#CRAG_WRAITH` (`MONSTER`, flying dive-attacker, 6j) — **all 10 Phase-6 mobs live**; plus `#ECHO_SENTINEL`
  (`MONSTER`, floating ranged ruin guardian, 10f); plus `#SPORE_TRADER` (`CREATURE`, the rare Sporeman
  wandering trader, v1.2). Each entity also needs
  attributes + spawn placement (`event.ModEntityEvents`), a renderer (`client.LumenwildsClient`), a loot table
  (`loot_table/entities/`), and biome `spawners` entries.
- [ModToolTiers.java](src/main/java/com/jus144tice/lumenwilds/registry/ModToolTiers.java) — tool material
  tiers (v1.2): `#MOONSTONE` (stone-tier, `INCORRECT_FOR_STONE_TOOL`, repair = Cobbled Moonstone) + `#LUMINITE`
  (iron-tier, `INCORRECT_FOR_IRON_TOOL`, repair = Luminite Ingot). A tiny `SimpleTier` record implements
  `Tier`; the tool items live in `ModItems` (`#MOONSTONE_PICKAXE`…`#LUMINITE_SWORD`, via the `pickaxe/axe/
  shovel/hoe/sword` helpers), recipes in `ModRecipeProvider#buildToolRecipes`, enchantability + type tags in
  `ModItemTagProvider`. **Not a DeferredRegister** (tiers are plain objects).
- [ModEnchantments.java](src/main/java/com/jus144tice/lumenwilds/registry/ModEnchantments.java) — `ResourceKey<Enchantment>`
  handles for the six **fished** enchantments (v1.1g); the enchantments are data (`data/lumenwilds/enchantment/*.json`,
  1.21.1 data-driven), NOT a DeferredRegister — no bus wiring. Armor while-worn (`tick`→`apply_mob_effect`):
  `#LIGHTFOOTED`/`#NIGHTSIGHT`/`#LUMENWARD`; weapon on-hit (`post_attack`): `#GLOWBRAND`/`#SPORESTRIKE`/`#ROOTBINDING`.
  Obtainable only as enchanted books from Lumenwater fishing (kept out of the enchanting-table/trade/loot tags).
- [ModLootModifiers.java](src/main/java/com/jus144tice/lumenwilds/registry/ModLootModifiers.java) — `#LOOT_MODIFIER_SERIALIZERS`
  (on `NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS`); registers the `#LUMENWATER_FISHING` codec
  (`loot.LumenwaterFishingModifier`, v1.1.3). Wired on the mod bus in `Lumenwilds` ctor.
- [ModPotions.java](src/main/java/com/jus144tice/lumenwilds/registry/ModPotions.java) — `#POTIONS`; a
  brewable `Potion` per 8a effect (`#LIGHTFOOT`/`#GLOWMARKED`/`#SPOREBLIND`/`#ROOTED`, 8h). The drinkable/
  splash/lingering/tipped item variants are vanilla; the brewing mixes are in `event.ModBrewing`.
- [ModMobEffects.java](src/main/java/com/jus144tice/lumenwilds/registry/ModMobEffects.java) — `#MOB_EFFECTS`;
  the four status effects (Phase 8a), each a `effect.LumenMobEffect` (a trivial public-ctor `MobEffect`
  subclass — vanilla's ctor is protected). `#LIGHTFOOT` (+`JUMP_STRENGTH`/+`SAFE_FALL_DISTANCE`), `#GLOWMARKED`
  (no attrs — glow via `event.LumenEffectEvents`), `#SPOREBLIND` (−`MOVEMENT_SPEED`; the Sporeling cloud
  applies it), `#ROOTED` (−`MOVEMENT_SPEED` & −`JUMP_STRENGTH`). Icons: `textures/mob_effect/<name>.png`.
- [ModBlockEntities.java](src/main/java/com/jus144tice/lumenwilds/registry/ModBlockEntities.java) —
  `#BLOCK_ENTITIES`; `#LUMEN_ANCHOR` (`BlockEntityType` for `block.LumenAnchorBlockEntity`, 8c) — the first BE;
  `#RESONANCE_CORE` (for `block.ResonanceCoreBlockEntity`, 10e.1 — ticks the conduit power network; its type
  also covers `ACTIVE_LIGHT_ENGINE`, which is a core); `#LUMEN_FIELD_PROJECTOR` (for
  `block.LumenFieldProjectorBlockEntity`, 11b — ticks a liftshaft's gravity column) — the 3rd BE; and
  `#LUMEN_CHEST` (for `block.LumenChestBlockEntity`, v1.1.3 — shared by both glowing chest blocks).
- Empty stubs (compile; carry phase TODOs). Wired to the bus already (registered empty): 
  [ModMenus](src/main/java/com/jus144tice/lumenwilds/registry/ModMenus.java) `#MENUS`,
  [ModSounds](src/main/java/com/jus144tice/lumenwilds/registry/ModSounds.java) `#SOUNDS` (still empty — the
  7c soundscape is built from **vanilla** sound events; bespoke recorded `.ogg` SFX → Phase 9, then register
  custom events here + a `sounds.json`).
- [ModParticles.java](src/main/java/com/jus144tice/lumenwilds/registry/ModParticles.java) — `#PARTICLES`
  (atmosphere, Phase 7b); `#LUMEN_SPORE` (signature drifting glow mote — biome ambience + the portal),
  `#GLOW_POLLEN` (flower-biome float), `#CRYSTAL_SHIMMER` (Crags sparkle), `#ASCENSION_MOTE`/`#DESCENT_MOTE`
  (liftshaft column motes, 11d), all `SimpleParticleType`. Client render factories + sprites are wired in
  `client.LumenwildsClient` (the liftshaft motes reuse the EndRod factory); usage is the portal `animateTick`,
  biome `effects.particle`, and the field/projector `animateTick` (11d).
- [ModFeatures.java](src/main/java/com/jus144tice/lumenwilds/registry/ModFeatures.java) — `#FEATURES`
  (custom `Feature` types), bus-wired. `#GLOWROOT_TREE_2X2` (`GlowrootTreeFeature`) — the ordinary 2×2
  Glowroot tree (the mega tree is a structure; both share `world.feature.GlowrootShape`); `#STILLBLOOM`
  (`StillbloomFeature`) — the giant Stillbloom flower (5d.6); `#LUMENWATER_POOL`
  (`world.feature.LumenwaterPoolFeature`) — a small **chunk-safe** Moonloam+Lumenwater basin replacing the
  vanilla `lake` (which crashed chunk-gen near borders — the Moonmire/Undercrown pools); `#LUMEN_REEF`
  (`world.feature.LumenReefFeature`) — glowing coral mounds + fronds on the submerged seabed (Phase 9, added to
  the surface biomes' seas via a NeoForge `biome_modifier`); `#GLASSPETAL_GROWTH`
  (`world.feature.GlasspetalGrowthFeature`) — varied-size glasspetal crystal growths: fountains of tapering
  `glasspetal_block` crystal SPIKES (leaning, pointed, cluster-bristled) — lone cluster / small / large / rare MEGA
  burst — scattered on the dry Crags floor (Phase 9 "feels like it's growing"; the rare town-sized version
  is the Glasspetal Spires *structure*). Drives `configured_feature/patch_glasspetal` (was a flat single-cluster
  `random_patch`); `#UNDERCROWN_DECOR` (`world.feature.UndercrownDecorFeature`) — finds open cave air in the
  Undercrown and grows crystals on rock faces, **tall Glowvine strands (3–8 blocks) hanging from cave ceilings**,
  and glow ferns on floors (Phase 9 cave-richness; the ceiling-hang gives the iconic drape of living light
  instead of 1-block bits); `#GLOWCAP`
  (`world.feature.GlowcapFeature`) — the ordinary Sporefall Glowcap mushroom: **bell-shaped, size-varied
  (small/medium/large) and 3-coloured** (red / `GIANT_GLOWCAP_AZURE` / `GIANT_GLOWCAP_VIOLET`), replacing the flat
  vanilla `huge_brown_mushroom` (Phase 9 variation; the mega is still the `MegaGlowcap` structure).
- [ModStructures.java](src/main/java/com/jus144tice/lumenwilds/registry/ModStructures.java) —
  `#STRUCTURE_TYPES` + `#STRUCTURE_PIECES`; `#GLOWROOT_TREE` + `#GLOWROOT_TREE_PIECE` (the mega Glowroot
  tree), `#MEGA_GLOWCAP` + `#MEGA_GLOWCAP_PIECE` (the town-sized Giant Glowcap mushroom), `#ROOTSHRINE` +
  `#ROOTSHRINE_PIECE` (the small early-reward Rootshrine, 8d), `#LUMENBOUND_RUINS` + `#LUMENBOUND_RUINS_PIECE`
  (the Overworld ruined-portal tutorial site, 8e), and `#GLASSPETAL_SPIRES` + `#GLASSPETAL_SPIRES_PIECE` (the
  crystal towers, 8f), and `#UNDERCROWN_RELICS` + `#UNDERCROWN_RELICS_PIECE` (the buried dungeon, 8g — placed at
  a deep Y), and `#VESTIGE_OUTPOST` + `#VESTIGE_OUTPOST_PIECE` (the Small Vestige Outpost, 10b), and
  `#VESTIGE_CITY` + `#VESTIGE_CITY_PIECE` (the Medium/Grand Vestige City, 10d/10g), `#VESTIGE_VAULT_PIECE` (the
  buried Vestige Vault, 10f.2), `#VESTIGE_SPIRE_PIECE` (the broken tower, 10g), and `#VESTIGE_MINE_PIECE` (the
  Lumenwright liftshaft + abandoned Luminite mine, 11c) — the last three are piece-only
  types (no structure; added by the city). All are
  structures (generate per-chunk via a bounding box). Structure instances + spawn spacing
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
  `#getLocalTransition` = `NONE` (no Nether nausea — the "teleporting" feedback is `client.LumenPortalOverlay`
  instead), `#animateTick` (rises `ModParticles.LUMEN_SPORE` + an occasional `PORTAL_AMBIENT` hum, 7b/7c).
  Renders as an **animated translucent teal portal-plane** (axis-oriented `blockstates/lumen_portal.json` → a
  thin emissive plane model; `textures/block/lumen_portal.png` is a 16-frame swirl + `.mcmeta`), not a cube.
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
- [LumenChestBlockEntity.java](src/main/java/com/jus144tice/lumenwilds/block/LumenChestBlockEntity.java) — a
  thin `ChestBlockEntity` subclass (v1.1.3) carrying the `ModBlockEntities#LUMEN_CHEST` type so the glowing
  Glowwood/Glowroot chests (vanilla `ChestBlock` + `client.LumenChestRenderer`) get per-species textures; all
  chest behaviour is inherited. Chest textures: `textures/entity/chest/{glowwood,glowroot}{,_left,_right}.png`
  on the vanilla chests atlas (+ an insurance `assets/lumenwilds/atlases/chests.json`); item = a flat icon.
- [GlowberryBushBlock.java](src/main/java/com/jus144tice/lumenwilds/block/GlowberryBushBlock.java) — the
  Glowberry Bush (v1.1c), a `BushBlock` + `BonemealableBlock` modelled on vanilla `SweetBerryBushBlock`.
  `#AGE` (0..3, `AGE_3`), `#lightFor` (light 3→6, wired in `ModBlocks`), `#randomTick` ripens in light ≥9,
  `#useWithoutItem` harvests a mature bush (pops 1–2 Glowberries, reverts to age 1 — renewable), bone meal
  advances age. `ModBlocks#GLOWBERRY_BUSH`; planted by the `ModItems#GLOWBERRY` `ItemNameBlockItem`
  (no own BlockItem); age-conditioned berry loot is hand-authored.
- [LumenCoralBlock.java](src/main/java/com/jus144tice/lumenwilds/block/LumenCoralBlock.java) — the glowing
  underwater Lumen Coral frond (Phase 9). A waterlogged (`SimpleWaterloggedBlock`) no-collision cross plant:
  `#WATERLOGGED`, `#getStateForPlacement` (waterlogs in a full water source), `#getFluidState` (returns
  **Lumenwater**, not vanilla water, so the frond's cell matches the teal sea instead of a clear-blue seam), `#canSurvive`
  (sturdy face below), `#updateShape` (pops off if support lost). Unlike vanilla coral it doesn't die out of
  water. `ModBlocks#LUMEN_CORAL`; grown by `world.feature.LumenReefFeature`.
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
- [LumenConduitBlock.java](src/main/java/com/jus144tice/lumenwilds/block/LumenConduitBlock.java) — the Lumen
  Conduit (10c). `#CONDUIT_STATE` (`EnumProperty<State>` dead/dim/active) + `#lightFor` (0/2/8, wired as the
  block's `lightLevel` in `ModBlocks`). Decorative in 10c (ruins place dead/dim, state never changes); the
  Resonance network (10e) will drive the state dynamically. `ModBlocks#LUMEN_CONDUIT`.
- [ResonanceCoreBlock.java](src/main/java/com/jus144tice/lumenwilds/block/ResonanceCoreBlock.java) +
  [ResonanceCoreBlockEntity.java](src/main/java/com/jus144tice/lumenwilds/block/ResonanceCoreBlockEntity.java)
  — the Resonance Core (10e.1), a `BaseEntityBlock` whose BE `#serverTick` (every 20t, position-staggered)
  floods power and `#shutdown` (from the block's `onRemove`) tears it down. `ModBlocks#RESONANCE_CORE`,
  `ModBlockEntities#RESONANCE_CORE` (the 2nd BE). Glows (light 10).
- [ResonanceNetwork.java](src/main/java/com/jus144tice/lumenwilds/block/ResonanceNetwork.java) — the static
  network logic: `#flood` (bounded BFS over `LumenConduitBlock`, cap `#MAX_NODES`), `#energize`/`#deenergize`
  (set conduits ACTIVE/DIM, diffed vs. the core's previous reach so cuts drop downstream), and door power
  (`ancient_door` opens while a neighbour is an active conduit or core). All flag-2 sets (no neighbour
  cascade), server-side, transient. **Tune the resonance behaviour here.** (`ANCIENT_DOOR` is a plain iron-set
  `DoorBlock` — no subclass; the network opens it.) **10e.2:** the flood also traverses `LumenRelayBlock`
  nodes (which bridge to other conductors within `#RELAY_BRIDGE`), and `#updateDevices` also powers
  `GravityLensBlock` (sets its `POWERED`).
- [GravityLensBlock.java](src/main/java/com/jus144tice/lumenwilds/block/GravityLensBlock.java) — the Gravity
  Lens (10e.2). `#POWERED` boolean (set by the network) → light 6/2 (`#lightFor`); the lift itself is in
  `event.LumenGravityEvents`. `ModBlocks#GRAVITY_LENS` (+ `#CRACKED_GRAVITY_LENS`, drops the fragment).
- [LumenRelayBlock.java](src/main/java/com/jus144tice/lumenwilds/block/LumenRelayBlock.java) — a marker block
  the `ResonanceNetwork` flood treats as a gap-bridging conductor node (10e.2). `ModBlocks#LUMEN_RELAY`.
- [DormantLightEngineBlock.java](src/main/java/com/jus144tice/lumenwilds/block/DormantLightEngineBlock.java)
  + [ActiveLightEngineBlock.java](src/main/java/com/jus144tice/lumenwilds/block/ActiveLightEngineBlock.java) —
  the city centrepiece (10e.2). `Dormant#useItemOn` with a `resonance_core_fragment` → swaps to the Active
  engine (`extends ResonanceCoreBlock`, so it powers the network; shares the `RESONANCE_CORE` BE type).
  `ModBlocks#DORMANT_LIGHT_ENGINE`/`#ACTIVE_LIGHT_ENGINE`.
- [AbstractFieldBlock.java](src/main/java/com/jus144tice/lumenwilds/block/AbstractFieldBlock.java) +
  [AscensionFieldBlock.java](src/main/java/com/jus144tice/lumenwilds/block/AscensionFieldBlock.java) +
  [DescentFieldBlock.java](src/main/java/com/jus144tice/lumenwilds/block/DescentFieldBlock.java) — the
  Lumenwright liftshaft **gravity-column field cells** (Phase 11a). Built like `portal.LumenPortalBlock`:
  non-solid/`noCollission`/unbreakable (strength −1)/`noLootTable`, empty `#getShape` (no outline/target),
  no BlockItem (`ModItems` skip), hand-authored translucent model (`ModBlockStateProvider` skip).
  `AbstractFieldBlock#entityInside` → `#applyField` + `#resync` (the shared `#approach`/`STEP`/motion-resync
  helpers, mirroring `event.LumenGravityEvents#lift`). `AscensionFieldBlock` eases vertical speed up to +0.40
  (sneak holds via `#approach`→0, a jump past the cap is preserved), `DescentFieldBlock` holds the −0.35…−0.45
  safe band; both `resetFallDistance()` each tick. The shared `AbstractFieldBlock#animateTick` (11d) streams the
  field's mote (`ASCENSION_MOTE`/`DESCENT_MOTE`) up/down + a horizontal energy-ring pulse every 4th cell + a
  pitched hum (subclasses supply `#mote`/`#riseSign`/`#hum`). `ModBlocks#ASCENSION_FIELD`/`#DESCENT_FIELD`.
  **Projected/cleared by the Lumen Field Projector (11b)** and pre-placed in ruin shafts (11c); never hand-placed.
- [LumenFieldProjectorBlock.java](src/main/java/com/jus144tice/lumenwilds/block/LumenFieldProjectorBlock.java)
  + [LumenFieldProjectorBlockEntity.java](src/main/java/com/jus144tice/lumenwilds/block/LumenFieldProjectorBlockEntity.java)
  — the player-craftable liftshaft source (Phase 11b), a `BaseEntityBlock` + the **3rd** `ModBlockEntities`
  type. `#MODE` (`EnumProperty<Mode>` ascend/descend); `#useWithoutItem` toggles mode (clears the old column
  first via `be.clearField`, plays a chime, chat + `#appendHoverText` show the mode); `#onRemove` tears the
  column down. The BE `#serverTick` (every 10t, position-staggered) calls `LiftShaftNetwork.project` and clears
  cells it no longer owns. `#animateTick` (11d) hums + glints from the working face so it reads as alive.
  `ModBlocks#LUMEN_FIELD_PROJECTOR` (light 6). Standalone-powered (no Resonance net).
- [GravityRepeaterBlock.java](src/main/java/com/jus144tice/lumenwilds/block/GravityRepeaterBlock.java) — a
  flush wall marker block (Phase 11b, light 3). Pure marker: `LiftShaftNetwork` resets the field's range budget
  whenever a column cell is orthogonally adjacent to one, so building repeaters into a shaft wall chains it
  arbitrarily tall (the user's design — function is adjacency, not facing). `ModBlocks#GRAVITY_REPEATER`.
- [LiftShaftNetwork.java](src/main/java/com/jus144tice/lumenwilds/block/LiftShaftNetwork.java) — the static
  field-projection logic (Phase 11b; mirrors `ResonanceNetwork`). `#project` walks the column in the mode
  direction placing field cells (budget `#RANGE` 16, reset by `#adjacentRepeater`, hard cap `#MAX_LENGTH` 256,
  stop at solid); `#clearStale` removes cells a projector dropped; `#clearColumn` walks-and-clears the
  contiguous column on removal/mode-flip. **Tune shaft range/cap here.**

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
- [GlyphTabletItem.java](src/main/java/com/jus144tice/lumenwilds/item/GlyphTabletItem.java) — Ancient Glyph
  Tablet (10c). A lore item: `#use` displays its fragment (`displayClientMessage`) and `#appendHoverText`
  shows it as an italic tooltip. The line is passed at registration (one per tablet in `ModItems`).

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
  glowing lamp. Goals: `FlyToBlocksGoal` (flowers/lights) + `WaterAvoidingRandomFlyingGoal` + look.
  (Moving-light emission deferred → Phase 9.) **v1.2.1:** removed the `PanicGoal` (a ground-mob flee that made
  the beetle dive at the floor) and overrode `#checkFallDamage` to a no-op — its low-gravity descents toward
  ground-level attractors were accruing fall distance and killing this 4-HP mob on landing (the "dive-bomb +
  die" bug). **All flying mobs are now fall-damage-immune** (the same `checkFallDamage` no-op, like vanilla
  bees): Lantern Beetle, Glowmoth, Sky Jelly, Crag Wraith.
- [entity/ai/FlyToBlocksGoal.java](src/main/java/com/jus144tice/lumenwilds/entity/ai/FlyToBlocksGoal.java) —
  **reusable** flight goal: throttled scan of a small box for a block matching a `Predicate<BlockState>`
  (Moonblossom/Lumenbulb/Glowvine), then flies to hover above the nearest. Shared by the Lantern Beetle (and
  later the Glowmoth).
- [Sporeling.java](src/main/java/com/jus144tice/lumenwilds/entity/Sporeling.java) — `Monster`, the
  jungle/cave **swarm** (6d). **Neutral as of v1.2** — no `NearestAttackableTargetGoal` (doesn't aggro on
  sight); only `HurtByTargetGoal#setAlertOthers` (fights back when hit + alerts the swarm). `#die` bursts a
  **spore cloud** — an `AreaEffectCloud` (radius 2.5, 80t, spore particle) applying **`ModMobEffects.SPOREBLIND`**
  (8a) + Darkness. Native low gravity. Render = bespoke `client.model.SporelingModel`.
- [SporeTrader.java](src/main/java/com/jus144tice/lumenwilds/entity/SporeTrader.java) — `AbstractVillager`, the
  **Sporeman** (v1.2) — a rare "fully grown Sporeling" wandering merchant of the Sporefall Jungle. Neutral
  (`TradeWithPlayerGoal` + stroll/look; `MeleeAttackGoal` + `HurtByTargetGoal` so it retaliates if struck — no
  auto-target). `#updateTrades` builds a pool of `MerchantOffer`s (Lumenwilds goods sold for Overworld
  valuables — emeralds, plus premium gold/diamond wares) and offers a random 6–8. `#mobInteract` opens the
  trade screen (mirrors `WanderingTrader`, excludes our spawn egg). Render = the scaled-up `SporelingModel`
  (`client.SporeTraderRenderer`, ~1.7×). **Spawn rule uses `Mob::checkMobSpawnRules`** (it's not an `Animal`).
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
- [EchoSentinel.java](src/main/java/com/jus144tice/lumenwilds/entity/EchoSentinel.java) — `Monster`, the
  Vestige City **ruin guardian** (10f). Floating construct (near-zero gravity, `FlyingMoveControl` +
  `FlyingPathNavigation`), slow drift, **no melee** — fights only with `entity.ai.LightPulseAttackGoal`. Tanky
  (24 HP, knockback-resistant), `xpReward` 12. Bespoke `client.model.EchoSentinelModel` + emissive glow.
  Drops resonance/luminite/memory + rare relay; spawn egg + `loot_table/entities/echo_sentinel`. Spawns rarely
  in Undercrown `spawners` + the vault (10f.2).
- [entity/ai/LightPulseAttackGoal.java](src/main/java/com/jus144tice/lumenwilds/entity/ai/LightPulseAttackGoal.java)
  — the Echo Sentinel's ranged attack (10f): a **hitscan** charge→fire→cooldown cycle — when in range + LOS it
  charges (eye glow particles), then fires an instant beam (a line of `END_ROD` particles) that damages +
  lightly knocks back the target. No projectile entity/renderer (cheaper + robust). Tunables in the ctor
  (damage/range/charge/cooldown).

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
  — keys for `data/.../worldgen/configured_feature/`: `#LUMEN_CRYSTAL_ORE`, `#LUMINITE_ORE` (10a, `ore`
  feature in moonstone + deep moonstone), `#PATCH_MOONBLOSSOM`,
  `#PATCH_GLOW_FERN`, `#GLOWWOOD_TREE`, `#GLOWROOT_TREE` (1×1, vanilla `tree`), `#GLOWROOT_TREE_2X2`
  (custom `GlowrootTreeFeature`), `#PATCH_GLASSPETAL` (5d.2, Glasspetal Cluster `random_patch`),
  `#GIANT_GLOWCAP` (5d.3, vanilla `huge_brown_mushroom` with the glowcap blocks), `#LUMENWATER_POOL`
  (5d.4, vanilla `lake` filled with Lumenwater) + `#PATCH_GLOW_ALGAE`/`#PATCH_LUMEN_REEDS`,
  `#UNDERCROWN_GLOWVINE` (5d.5, an `ore` feature threading Glowvine through cave rock), `#STILLBLOOM` (5d.6,
  the custom giant-flower `StillbloomFeature`), `#SHIMMERSTONE_ORE` (v1.2.1, an `ore` feature — moonstone/
  deep_moonstone → shimmerstone blobs, in the Glasspetal Crags) — all live. (The Glowroot *mega* tree is a
  structure.)
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
  **shared** procedural Glowroot tree (trunk/roots + a full canopy built from tiers of leafy branches via
  `#buildCanopy`/`#growBranch` + optional ore). `#generate` draws into a `#Placer` (abstracts structure
  box-clipping vs. feature direct writes); `#Params` size knobs with presets `#MEGA` (the structure giant) and
  `#MEDIUM` (the ordinary 2×2 feature). **Tune tree shape here** — both variants share it. Canopy invariant:
  every leaf blob is seated on a branch-log and capped at radius 3 (≤5.2 leaf-steps from a log → 0 gen-decay);
  lushness comes from MANY overlapping branch-blobs across tiers, not a trunk-centred dome (census-verified:
  28.6k leaves dead-flat incl. a MEGA tree). Needs the logs in `#minecraft:logs` (see the leaf-decay gotcha).
  `#buildButtressRoots` anchors each arching root to the ground with a vertical log leg — the leg starts **one
  below the root tip** and passes through its own logs to solid ground (starting *at* the tip stopped on the
  tip's own log, so roots used to dangle in mid-air).
- [GlowrootTreeFeature.java](src/main/java/com/jus144tice/lumenwilds/world/feature/GlowrootTreeFeature.java)
  — `Feature<NoneFeatureConfiguration>` for the ordinary 2×2 Glowroot tree; `#place` runs
  `GlowrootShape.generate(..., MEDIUM)`. Bound to `ModFeatures#GLOWROOT_TREE_2X2`.
- [StillbloomFeature.java](src/main/java/com/jus144tice/lumenwilds/world/feature/StillbloomFeature.java) —
  `Feature<NoneFeatureConfiguration>` (5d.6); `#place` builds a 3–8-tall giant Stillbloom (stem column +
  petal disc dome + glowing core) into air/replaceable space, stopping if it hits solid. Bound to
  `ModFeatures#STILLBLOOM`.
- [LumenReefFeature.java](src/main/java/com/jus144tice/lumenwilds/world/feature/LumenReefFeature.java) —
  `Feature<NoneFeatureConfiguration>` (Phase 9); `#place` grows a small reef on the submerged seabed — coral-block
  mounds capped by fronds + sand accents — only if `#isWater` at the origin (so it no-ops on dry land). Bound to
  `ModFeatures#LUMEN_REEF`; placed on `OCEAN_FLOOR_WG` in the surface biomes via the `lumen_reef` biome modifier.
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
  — the **Glasspetal Spires** (8f; reworked Phase 9). `#postProcess` grows a main spire + satellites — tapering
  discs of mixed Shimmerstone / Shimmerstone Bricks / Lumen Crystal Block crowned with Glasspetal Clusters — and a
  base loot chest (`chests/glasspetal_spires`). Rolls a **size tier** (regular / large / rare MASSIVE, like the
  trees) so the Crags vary; the box is sized for the massive case + position-seeded RNG picks the actual size.
  Each spire **roots into the terrain** via `#fillFoundation` (fills Shimmerstone DOWN through water/air to solid)
  so nothing floats — and the structure anchors to **`OCEAN_FLOOR_WG`** (ground, not the water surface — see the
  floating-structure gotcha). Bound to `ModStructures#GLASSPETAL_SPIRES`; spawns in the Glasspetal Crags,
  **Crag-Wraith-guarded** via the structure JSON's `spawn_overrides` (not code).
- [UndercrownRelicsStructure.java](src/main/java/com/jus144tice/lumenwilds/world/structure/UndercrownRelicsStructure.java)
  / [UndercrownRelicsPiece.java](src/main/java/com/jus144tice/lumenwilds/world/structure/UndercrownRelicsPiece.java)
  — the **Undercrown Relics** (8g), a buried dungeon hall. Unlike the surface structures, `#findGenerationPoint`
  picks a **deep** Y (deterministic per chunk, well below the surface). `#postProcess` carves a Deep-Moonstone
  shell around a 9×7×5 air chamber (tiled floor, four pillars, Lumenbulb lights), a central **mob spawner**
  (`SpawnerBlockEntity#setEntityId` → Shade Stalker), and two loot chests (`chests/undercrown_relics`: rare
  loot + Lumen-Anchor parts). Bound to `ModStructures#UNDERCROWN_RELICS`; spawns in the Undercrown Caverns.
- [VestigeDecay.java](src/main/java/com/jus144tice/lumenwilds/world/structure/VestigeDecay.java) — the
  **shared ruin processors** for the whole Vestige City family (Phase 10b+). Static helpers every Vestige
  piece runs its clean layout through so the world reclaimed the ruin: `#glowbrick`/`#ancientGlowbrick`
  (weathered glowbrick mix — fades intact→cracked→ancient), `#rubble`, `#decayedGlowbrick` (place-or-miss),
  `#overgrow` (creeps lumen grass/glow fern/moonblossom/glowvine onto air), `#fillFoundation` (roots to
  ground), `#weatheredFoundation` (10h.2 — roots with a crumbling moonstone/moonloam mix so slope foundations
  read as ancient broken supports, not bare grey pillars; used by the surface city/outpost), `#set`
  (box-clipped). **Tune the decay/overgrowth feel here.**
- [VestigeOutpostStructure.java](src/main/java/com/jus144tice/lumenwilds/world/structure/VestigeOutpostStructure.java)
  / [VestigeOutpostPiece.java](src/main/java/com/jus144tice/lumenwilds/world/structure/VestigeOutpostPiece.java)
  — the **Small Vestige Outpost** (10b), the first/smallest Lumenwright ruin. `#findGenerationPoint` anchors
  one piece at the surface (`OCEAN_FLOOR_WG`). `#postProcess` (position-seeded RNG, box-clipped) builds a
  broken glowbrick `#road`, `#toppledPillars` (standing stubs + horizontal-axis fallen runs), one roofless
  `#buildingShell` (decayed walls + doorway + Lumenbulb + the `chests/ruined_cache` chest), a `#plinth`, and
  `#debris` — all via `VestigeDecay`. Bound to `ModStructures#VESTIGE_OUTPOST`.
- [VestigeCityStructure.java](src/main/java/com/jus144tice/lumenwilds/world/structure/VestigeCityStructure.java)
  / [VestigeCityPiece.java](src/main/java/com/jus144tice/lumenwilds/world/structure/VestigeCityPiece.java) —
  the **Medium Vestige City** (10d), the main ruin. `#postProcess` (position-seeded, box-clipped; spans chunks
  like the spires) builds a radial city: `#plaza` (concentric chiseled-glowbrick rings) + `#fountain` (dry
  sunken basin + dead central crystal pylon) + `#lightPylons` (Lumenbulb-topped, some broken) + four `#road`s
  (with embedded `LumenConduitBlock` dim/dead lines) + an outer ring of stamps — `#crescentHouse`, `#hollowPod`
  (stepped dome), `#archway` (crescent peak), `#rootChamber` (sunken, glowvine-choked), `#plinth` (often a
  Memory Crystal). `#placeChest` drops Reliquary/Cache; `#floorAndFoundation` roots stamps. Size constants
  `#PLAZA_R`/`#CITY_R`. Bound to `ModStructures#VESTIGE_CITY`. **`findGenerationPoint` also adds a buried
  `VestigeVaultPiece` ~22 below the plaza (10f.2), rolls the size `tier` (10g), rejects below-sea-level
  placements (dry-land only), samples the biome for `flavor`, and rolls a `VestigeMinePiece` (11c — grand always
  / medium ~40%, offset ~22 to the city edge).** **`VestigeCityStructure` has a `guaranteed_mine` codec field**
  (RecordCodecBuilder, not `simpleCodec`): the default `vestige_city` instance leaves it false (the rare roll),
  while a second datapack structure **`lumenwilds:vestige_mine` sets it true** so every such city has a mine —
  making it **`/locate`-able** (`/locate structure lumenwilds:vestige_mine` = the nearest ancient city with a
  mineshaft; its own `structure_set`, salt, biomes-tag-reuse, and in the `vestige_city` structure tag). **10h.3:**
  `#flavorFor`/`#applyFlavor` read the biome and scatter overgrown
  (forest/jungle), cracked-spire (Crags), or sunken (Moonmire) accents.
- [VestigeVaultPiece.java](src/main/java/com/jus144tice/lumenwilds/world/structure/VestigeVaultPiece.java) —
  the **Vestige Vault** (10f.2), a sub-piece of the city (`ModStructures#VESTIGE_VAULT_PIECE`, no own structure
  type). `#chamber` carves a Deep-Moonstone+glowbrick room; `#resonancePuzzle` is the lock-and-key — a dead
  central Dormant Light Engine + dead Lumen Conduit runs to two sealed `ancient_door`s + alcove chests
  (`chests/vault` east, `chests/engineers_cache` west) + an Echo Sentinel spawner; `#decor` adds Memory
  Crystals + dim conduits; `#shaft` climbs a corner spiral of glowbrick steps (`#RING`) to a hole in the plaza.
  Carries `surfaceY` (the plaza level) so the shaft reaches it.
- [VestigeSpirePiece.java](src/main/java/com/jus144tice/lumenwilds/world/structure/VestigeSpirePiece.java) —
  a **Vestige Spire** (10g), the broken tower of a grand city (`ModStructures#VESTIGE_SPIRE_PIECE`). `#postProcess`
  grows a tapering glowbrick shaft (hollow rings, more broken with height) around an exposed Lumen-Crystal core,
  with floor discs, jutting `GLOWBRICK_STAIRS` fragments, floating debris near the top, a `chests/spire` chest
  at the base, and a foundation. Added by `VestigeCityStructure` for grand cities. (Grand vs. medium is rolled
  in `VestigeCityStructure#findGenerationPoint`; `VestigeCityPiece` carries the `tier` — grand adds a 2nd
  building ring + a central restorable Dormant Light Engine; medium keeps the dry fountain.)
- [VestigeMinePiece.java](src/main/java/com/jus144tice/lumenwilds/world/structure/VestigeMinePiece.java) — the
  **Lumenwright Liftshaft + Abandoned Luminite Mine** (Phase 11c, `ModStructures#VESTIGE_MINE_PIECE`), a single
  tall sub-piece added by `VestigeCityStructure#findGenerationPoint` (grand always / medium ~40%, offset ~22 to
  the **city edge** so the dais is a distinct, findable satellite, only if it clears `y-16`). Like the vault,
  spans deep→surface in one box (box-clipped, position-seeded). `#chamber`/`#ribs`/`#machinery` carve the deep
  mine (Moonstone shell, Glowbrick rib arches, Luminite + Lumen-Crystal ore veins, dim conduits, broken Gravity
  Lenses, Shimmerstone lift, Echo Sentinel spawner, Memory Crystals, `chests/miners_cache` +
  `chests/engineers_mine_cache`); `#shafts` carves the two columns open and builds each as a **working, lootable
  gravity engine** via `#gravityColumn` — a `LumenFieldProjectorBlock` (its BE floods the field at runtime) +
  `GravityRepeaterBlock`s spaced so the 16-cell budget terminates the field **exactly** at each ramp (last
  repeater 16 from the far end + a near-anchor within projector reach → clean ramps, no overshoot/sky-launch;
  ascension projector flush in the floor, descent projector on a `#descentHead` you walk under). `#dais` builds
  the surface Glowbrick-Tiles octagon — terrain carved above (`DAIS_CLEAR`) so it's never buried, + four glowing
  Lumen-Crystal/Lumenbulb **pylons** (findability), `VestigeDecay.weatheredFoundation`-rooted. **Order matters:**
  `postProcess` runs `#dais` *before* `#shafts` so the descent projector head survives the dais headroom carve.
  `#flavor` (11d) reads the biome for pop-safe accents (Crags glasspetal/crystal seams, Moonmire seeped
  Lumenwater pools, forest/jungle Glowroot-log roots). **Cave-aware:** `#findCaveFloor` (static, called from
  `VestigeCityStructure`) probes the dais column with `ChunkGenerator#getBaseColumn` for an open pocket over a
  solid floor → the chamber drops at that cavern (`naturalCave`) and `#breach` opens its lower walls into the
  cave; else a fixed depth (`y-38`). **Tune the mine here.**

### effects/ — movement (Phase 3, working)
- [LowGravityHandler.java](src/main/java/com/jus144tice/lumenwilds/effects/LowGravityHandler.java) —
  applies the low-gravity feel via **transient vanilla attribute modifiers** (stable `ResourceLocation`
  ids), added on dimension enter and removed on exit. `#GRAVITY_MULTIPLIER` (0.7 → `Attributes.GRAVITY`
  ×0.7; jump height ~1.79 blocks emerges from this, so `JUMP_STRENGTH` is deliberately NOT touched),
  `#SAFE_FALL_BONUS` (+3 → 6-block safe fall), `#FALL_DAMAGE_REDUCTION` (−0.5 → half damage),
  `#isInLumenwilds(entity)`, `#refresh(livingEntity)` (server-side; the GRAVITY attr is syncable),
  `#onChangedDimension(player)`, `#remove(livingEntity)`. Native-mob gravity comes via their attribute
  suppliers in Phase 6, so this hook is player-only.

### loot/ — global loot modifiers
- [LumenwaterFishingModifier.java](src/main/java/com/jus144tice/lumenwilds/loot/LumenwaterFishingModifier.java)
  — the custom GLM (v1.1.3) that REPLACES the vanilla fishing catch with a roll of our native table when the
  bobber is in Lumenwater (its `location_check` conditions are handled by the `LootModifier` base). `#doApply`
  clears the generated loot then rolls `#table` (mirrors `neoforge:add_table` but replaces instead of adds).
  Codec registered in `registry.ModLootModifiers`.

### mixin/ + world/time/ — the half-rate day clock (7d.1) + FishingHook (v1.1.3)
- [FishingHookMixin.java](src/main/java/com/jus144tice/lumenwilds/mixin/FishingHookMixin.java) — `@Redirect`s
  the `BlockState#is(Block)` water checks in `FishingHook#catchingFish` so the approaching-bubble + splash
  particles also fire over Lumenwater (vanilla hardcodes them to `Blocks.WATER`). Listed in `lumenwilds.mixins.json`.
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
  *(Glowberry harvest moved into `block.GlowberryBushBlock#useWithoutItem` in v1.1c — the old
  `GlowberryInteractEvents` was deleted.)*
- [MemoryCrystalInteractEvents.java](src/main/java/com/jus144tice/lumenwilds/event/MemoryCrystalInteractEvents.java)
  — `#onRightClickBlock` (10c): right-clicking a `ModBlocks#MEMORY_CRYSTAL` prints a fragment chosen
  deterministically from the block position (`#FRAGMENTS`, some broken/unreadable; the liftshaft/mine lore
  lines were appended in 11c); crystal not consumed.
- [LumenGravityEvents.java](src/main/java/com/jus144tice/lumenwilds/event/LumenGravityEvents.java) —
  `#onEntityTick(EntityTickEvent.Post)` (10e.2): a powered `GravityLensBlock` within `#REACH_BELOW` of a
  living entity (clear column) floats it up at a capped speed, zeroes fall distance, sneaking holds — the
  velocity-field gravity lift (server-side; re-syncs `ServerPlayer` motion).
- [ModBrewing.java](src/main/java/com/jus144tice/lumenwilds/event/ModBrewing.java) — **mod-bus**
  `#onRegisterBrewingRecipes(RegisterBrewingRecipesEvent)` (8h): `builder.addMix(awkward, ingredient, potion)`
  for the four `ModPotions` (Air Gel / Glow Pollen / Spore Sac / Living Fiber); + Glowcap Spores → Sporeblind
  (a second jungle-sourced mix, v1.1d).
- [GlowmothAggroEvents.java](src/main/java/com/jus144tice/lumenwilds/event/GlowmothAggroEvents.java) —
  `#onBlockBreak(BlockEvent.BreakEvent)` (6h): when a player breaks a guarded bloom (Moonblossom / any
  Stillbloom part), every `Glowmoth` within ~12 blocks `setTarget`s the culprit — the flower-guardian aggro.
- [ModBlockEntityTypes.java](src/main/java/com/jus144tice/lumenwilds/event/ModBlockEntityTypes.java) —
  mod-bus `#addSignBlocks(BlockEntityTypeAddBlocksEvent)`: adds the **Glowwood + Glowroot** sign blocks to the
  vanilla `BlockEntityType.SIGN`/`HANGING_SIGN` and the barrels to `BlockEntityType.BARREL` (modded signs/
  barrels reuse the vanilla block entities; the Glowroot-sign add was missing pre-v1.1.3).
- [ModEntityEvents.java](src/main/java/com/jus144tice/lumenwilds/event/ModEntityEvents.java) — **mod-bus**
  (Phase 6); `#onAttributeCreation(EntityAttributeCreationEvent)` builds each native mob's `AttributeSupplier`
  (`event.put(...)`) and `#onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent)` declares where on the
  ground a type may spawn (`ON_GROUND` + `Animal::checkAnimalSpawnRules`). **Add each new mob in both.**
  **Light gating:** the Shade Stalker + Crag Wraith use `Monster::checkMonsterSpawnRules` (darkness — design),
  but the **Sporeling, Mirelurker, and Echo Sentinel use `checkAnyLightMonsterSpawnRules`** (light-agnostic) —
  the dim-but-lit Lumenwilds biomes rarely hit the darkness threshold, so darkness-gated ambient fauna barely
  spawned; light-agnostic makes the jungle/Moonmire/Undercrown actually populated.

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
  the atmosphere particles, **reusing vanilla classes** (Lumen Spore → `EndRodParticle.Provider`, Glow
  Pollen → `SuspendedTownParticle.Provider`, Crystal Shimmer → `GlowParticle.GlowSquidProvider`, and the 11d
  liftshaft `ASCENSION_MOTE`/`DESCENT_MOTE` → `EndRodParticle.Provider`); sprites from
  `assets/lumenwilds/particles/<name>.json` → `textures/particle/<name>.png`. (Visuals verify via `runClient`.)
- [LumenEventClientEffects.java](src/main/java/com/jus144tice/lumenwilds/client/LumenEventClientEffects.java) —
  client `ClientTickEvent.Post` (7d.2): while an event is active (per `network.LumenEventClientState`) and the
  player's in the Lumenwilds, sprinkles event particles (Sporefall→spores, Moonwake→pollen, Deep Hush→shimmer).
  `LumenDimensionEffects#renderSky` also reads that state to **brighten Veyra during a Moonwake**.
- [LumenPortalOverlay.java](src/main/java/com/jus144tice/lumenwilds/client/LumenPortalOverlay.java) — the
  portal "you're teleporting" screen effect (the deferred Phase-7 teal overlay). `ClientTickEvent.Post` ramps a
  static `#intensity` up while the local player stands in a `LumenPortalBlock` (checks feet+eye block) over the
  ~80-tick dwell and fades it fast on exit; `RenderGuiEvent.Post` draws two opposite-scrolling teal swirl veils
  (`textures/gui/lumen_portal_overlay.png`) over the screen at alpha ∝ intensity — calm (no nausea wobble), just
  a rising glow. Pairs with the animated swirl block texture/model. **Client-visual — verify via `runClient`.**
- The `MobRenderer`s (`LumenGrazerRenderer`, `ShadeStalkerRenderer`, …, + `EchoSentinelRenderer` 10f) each bake
  a **bespoke model** (Phase 9b — the vanilla-model placeholders are gone): `textures/entity/<name>.png`.
- **Bespoke models (Phase 9b):** [client/model/](src/main/java/com/jus144tice/lumenwilds/client/model/) holds
  one custom `HierarchicalModel` per mob — `SkyJellyModel` (bell + tentacles), `GlowmothModel` (moth + 2 wing
  pairs), `CragWraithModel` (manta + wings/tail), `LanternBeetleModel` (shell + 6 legs + glow abdomen),
  `SporelingModel` (body + mushroom cap), `ShadeStalkerModel` (sleek 4-legged), `LumenGrazerModel` (**6 legs**),
  `RootbackModel` (domed turtle, built ~3×2 to fill the hitbox), `MirelurkerModel` (anglerfish + glowing lure),
  `LumenFishModel` (small fish), `EchoSentinelModel` (10f — floating shell + crystal eye + orbiting ring
  fragments). [LumenModelLayers.java](src/main/java/com/jus144tice/lumenwilds/client/LumenModelLayers.java)
  declares each `ModelLayerLocation`, registered in `LumenwildsClient#onRegisterLayerDefinitions` and baked in
  the renderer. Textures carry per-box region coloring + a mood-matched **face** (ominous on hostiles, friendly
  on passives; the Sky Jelly is faceless). *(Visual-only — verify via `runClient`; iterate from there.)*
- **Emissive glow (Phase 9c, "native living light"):**
  [client/layer/LumenEmissiveLayer.java](src/main/java/com/jus144tice/lumenwilds/client/layer/LumenEmissiveLayer.java)
  extends vanilla `EyesLayer` (model re-rendered fullbright + additive), driven by a per-mob
  `textures/entity/<name>_glow.png` (glowing regions bright on black). Added to 9 mobs + the Echo Sentinel (10f)
  in one place via `LumenwildsClient#onAddLayers` (`EntityRenderersEvent.AddLayers` + the `#addGlow` helper).
  **The Shade Stalker is deliberately excluded** — a jump-scare ambusher that flees light; a glow would betray
  its position.

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
  signs → `#registerSigns` (particle model), `LiquidBlock` + `AbstractFieldBlock` → **skipped** (hand-authored
  translucent model — the liftshaft field columns, 11a),
  `lumen_grass_block` → `cubeBottomTop` (real grass: green top, moonloam + grass-fringe sides, moonloam
  bottom — `block/lumen_grass_block_{top,side,bottom}`), else `cube_all`. `#baseTex(name)` resolves a shape's base texture (Glowwood shapes → planks;
  `_brick`/`_tile` → plural `_bricks`/`_tiles`).
- [ModItemModelProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModItemModelProvider.java) —
  block items inherit `block/<name>` (via `UncheckedModelFile`, to dodge cross-provider validation);
  `BushBlock`/`AmethystClusterBlock` items → flat `item/generated` from the block texture;
  fence/wall/button → `_inventory`, trapdoor → `_bottom`, doors + panes + signs → flat `item/<name>`;
  standalone items (incl. boats) → `basicItem`. **Spawn eggs (`DeferredSpawnEggItem`) are skipped** — they use
  the hand-authored vanilla `template_spawn_egg` model (no flat texture), so `runData` no longer demands one.
- [ModLanguageProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModLanguageProvider.java) —
  auto names from registry paths (`#addTranslations`, `#titleCase`) + tab title + portal messages,
  **deduped by description id** (SignItem/BlockItem reuse a block's key).
- [ModRecipeProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModRecipeProvider.java) —
  `#buildRecipes`: Lumenbound Stone (`CGC/SAS/CGC`) + Lumen Striker (`I/A/G`); `#buildGlowwoodRecipes` +
  `#buildGlowrootRecipes` (both call the shared `#buildWoodSetRecipes` — full wood set incl. signs, hanging
  signs, boat + chest boat), `#buildMoonstoneRecipes` +
  `#buildShimmerstoneRecipes` (2×2 crafting + stonecutter via helpers `#square2x2`/`#cut`; **v1.2.1** added the
  base-Shimmerstone craft `4 Moonstone + 1 Lumen Crystal Shard → 4` so the set isn't structure-gated),
  `#buildLuminiteRecipes` (10a — ore/raw → ingot smelt+blast, ingot ↔ block, the Glowbrick craft
  `L I L / I C I / L I L`, + Glowbrick family cuts/shapes), `#buildResonanceRecipes` (10e — Resonance Core
  from a fragment, Ancient Door from glowbrick, Gravity Lens from fragments + shimmerstone, Lumen Relay),
  `#buildRebuildRecipes` (10h.1 "rebuild the Lumenwrights' kit" — Lumen Conduit, Lumenbulb, Memory Crystal
  (4 shards), Active Light Engine, + aged-block stonecutter cuts; the *fragments* stay loot-only),
  `#buildLiftshaftRecipes` (11b — Lumen Field Projector `G L G / R C R / I E I` + Gravity Repeater),
  `#buildOrphanDropRecipes` (v1.1d — gives each formerly-useless mob drop a use; `#salvage` helper for
  1-input conversions, explicit recipe ids so same-result recipes don't collide).
- [ModLootTableProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModLootTableProvider.java) —
  `#create` + inner `ModBlockLoot`: drop-self for all blocks except `LUMEN_PORTAL` + `LUMENWATER_BLOCK`
  (both `noLootTable`) + the liftshaft fields + `GLOWBERRY_BUSH` (hand-authored age-conditioned berry loot),
  with slab (drops 2) and door (drops 1) special-cased; `memory_crystal` →
  `createOreDrop`(MEMORY_CRYSTAL_SHARD); `DropExperienceBlock` → `createOreDrop` (Luminite ores →
  `RAW_LUMINITE`, Lumen Crystal ores → shard); Glowroot wall-signs drop the Glowroot sign item.
- [ModTagProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModTagProvider.java) — a `BlockTagsProvider`.
  `#addTags`: classifies blocks by name into `mineable/pickaxe|axe|shovel|hoe` + `leaves` (auto-covers new
  stone/wood blocks; `_ore`/`_cluster`/moonstone/`glowbrick`/`luminite`/`conduit`/`resonance`/`memory_crystal`/
  `ancient_door` → pickaxe). **v1.1.1:** also tags the Glowwood/Glowroot wood-species blocks into the vanilla
  wood **block** tags (`PLANKS`, `WOODEN_SLABS|STAIRS|FENCES|DOORS|TRAPDOORS|BUTTONS|PRESSURE_PLATES`,
  `FENCE_GATES`, `STANDING_SIGNS`/`WALL_SIGNS`/`CEILING_HANGING_SIGNS`/`WALL_HANGING_SIGNS`, `LOGS_THAT_BURN`,
  `SAPLINGS`) — gated on the species name so the stone families/glowcap aren't included.
- [ModItemTagProvider](src/main/java/com/jus144tice/lumenwilds/datagen/ModItemTagProvider.java) — an
  `ItemTagsProvider` (v1.1.1; wired in `DataGenerators` with `ModTagProvider#contentsGetter`). Mirrors the wood
  block tags onto the matching **item** tags via `#copy` (`#minecraft:planks` etc. — what recipes read, so the
  planks craft a crafting table/chest/…), plus item-only `SIGNS`/`HANGING_SIGNS`/`BOATS`/`CHEST_BOATS`.

> NOTE: hand-authored placeholder assets in `src/main/resources` are **authoritative** (the mod works
> from a plain `build`, no datagen needed). `runData` output is a regeneration/diff aid only; it is NOT
> on the resource path, so it can't duplicate-clash with the committed assets. Copy anything worth
> keeping into `src/main/resources`.

## Resources — `src/main/resources`

- [META-INF/neoforge.mods.toml](src/main/resources/META-INF/neoforge.mods.toml) — mod metadata; required
  deps `neoforge` + `minecraft`; **optional** deps `patchouli` (guide) and **`create` (ordering only,
  `type="optional"` + `ordering="AFTER"`, v1.1.4)** — the latter forces Lumenwilds to register *after* Create
  to dodge a Create boot crash (see the Create ordering gotcha); it is a pure load-order hint, a no-op when
  Create is absent, and never a requirement. `enumExtensions = "META-INF/enumextensions.json"` (Glowwood
  `Boat.Type`); `[[mixins]] config = "lumenwilds.mixins.json"` (the 7d.1 day-clock mixins). `pack.mcmeta` →
  `pack_format` 48.
- `lumenwilds.mixins.json` (resource root) — the Mixin config (`package` = `…lumenwilds.mixin`, JAVA_21, no
  `refmap`); lists `ServerLevelMixin` + `DerivedLevelDataMixin`.
- `META-INF/enumextensions.json` — the Glowwood `Boat.Type` entry (constant name `lumenwilds_glowwood` is
  a Java identifier; the constructor's name string `lumenwilds:glowwood` drives textures). See `ModBoatTypes`.
- `assets/lumenwilds/`: `blockstates/`, `models/block|item/`, `textures/block|item/` (**Phase 9 world-art pass:
  all ~53 base block textures AND all ~35 item textures are now patterned procedural art — plant shapes, crystal
  facets, stone speckle, brick/tile, wood grain; item icons (foods, mob drops, tools, buckets, boats, signs) —
  not flat colours**; `glowvine` is a passable glowing **cross** (vine) model, not a cube; **Phase 9c world
  glow**: `models/block/_emissive_cube.json` / `_emissive_cube_soft.json` / `_emissive_cross.json` are emissive
  parents (NeoForge `neoforge_data` `block_light` — 15 fullbright / 7 soft / 13 cross) that the glowing blocks
  inherit so they render **bright in the dark**: Lumenbulb / Lumen Crystal Block / Stillbloom Core / Memory
  Crystal / `lumen_conduit_active` / `resonance_core` / `gravity_lens_powered` / `active_light_engine`
  (fullbright), the two ores + `lumen_conduit_dim` + `lumen_relay` (soft), and the flora
  Moonblossom / Glow Fern / Glow Algae / Lumen Reeds / Glowvine (cross). Light
  emission stays via each block's `lightLevel` (ores bumped 4→6 so the Undercrown is lit by dense ore).
  **v1.1.2 — glowing wood:** the whole Glowwood + Glowroot sets are emissive too, via per-shape parents
  `_emissive_{cube_column,cube_column_horizontal,stairs,inner_stairs,outer_stairs,slab,slab_top,fence_post,
  fence_side,template_fence_gate*,door_*,template_orientable_trapdoor_*,button*,pressure_plate_*}.json`
  (vanilla shape geometry + `neoforge_data block_light 15` on every element); the wood block-models inherit
  these (planks/leaves use `_emissive_cube`). Saplings (cross) are left non-emissive),
  `textures/entity/{signs,signs/hanging,boat,chest_boat}/glowwood.png` + `textures/gui/
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
  `lantern_beetle` 6c, `sporeling` 6d, `mirelurker` 6e, `lumen_fish` 6f, `sky_jelly` 6g, `glowmoth` 6h, `rootback` 6i, `crag_wraith` 6j, `echo_sentinel` 10f), `dimension/lumenwilds.json` (custom noise gen +
  a **`multi_noise` biome source** — humidity splits `lumen_glade`/`glowroot_forest`, a cold band carves out
  `glasspetal_crags`, a hot+humid band gives `sporefall_jungle`, a mild+wettest band gives `moonmire`, and a
  **deep `depth` band** gives `undercrown_caverns` [5d.5]; one parameter point added per 5d.x) +
  `dimension_type/lumenwilds.json` (`effects` → **`lumenwilds:lumenwilds`** selects the bespoke client sky,
  `ambient_light` 0.2, 7a), and `worldgen/` — `noise_settings/lumenwilds.json` (bespoke **alien cliffy terrain**
  [Phase 9 drawing-board]: a gentle y-gradient (zero ~y70, just above sea 63) + three `lumenwilds:hills` octaves —
  BIG (xz 0.35, regional plateaus/basins ≈ continents), MID (xz 1.0, main local relief), SHARP (xz 2.2, rugged
  cliff faces) — swings the surface ~y0..170: deep **Lumenwater seas** in the basins, high cliffs on the rises.
  **Noise CAVES** (big cheese **caverns** [`lumenwilds:caverns`, strength −8.5] + winding **tunnels** [`lumenwilds:cave_tunnels`,
  a ridged carve where `|noise|` is small] so caverns link into a system, depth-gated below ~y52 → ~28% of the deep
  hollow, meeting the surface biomes' vanilla carvers so you can cave down) hollow the deep into the
  **Undercrown** — folded INSIDE the `interpolated`/`squeeze` tree (adding it OUTSIDE does NOT carve — see gotcha).
  The Undercrown is decorated as a living crystal grotto by `world.feature.UndercrownDecorFeature` (`undercrown_decor`:
  crystals on cave rock faces + glowing Glowvine/Glow-Fern on floors) instead of the old floating-prone `lake` pool.
  **`aquifers_enabled` is now `true`** so those deep caves are AIR caverns with Lumenwater POOLS at the local water
  table (not flooded); `lava` router is the constant `0.0` (water-only — any non-zero value enables aquifer lava;
  lava is then only the engine's global floor at y<-54, below the Undercrown). The router's **`depth` is y-varying**
  [5d.5] so cave biomes layer under the surface, and **`temperature` + `vegetation` are shifted-noise** (all 7
  biomes spread; `xz_scale` 0.55 — raised from 0.25 in Phase 9 to make biomes **smaller** so the rarer ones, e.g.
  Moonmire, fall within `/locate` range); `continents`/`erosion`/`ridges` stay constant. **`default_fluid` is `lumenwilds:lumenwater`**
  (light 4) so seas/ponds/pools glow teal — safe at this scale (re-measured post-leaf-fix; see the fluid gotcha).
  Glowing Lumenwater also fills the **Moonmire** surface pools [`LumenwaterPoolFeature`]),
  `biome/lumen_glade.json` + `biome/glowroot_forest.json`
  (5d.1, dark-teal) + `biome/glasspetal_crags.json` (5d.2, blue-violet) + `biome/sporefall_jungle.json`
  (5d.3, lush green + warped_spore particle) + `biome/moonmire.json` (5d.4, dark glowing swamp) +
  `biome/undercrown_caverns.json` (5d.5, deep cave biome) + `biome/stillbloom_basin.json` (5d.6, rare bright
  sanctuary). **Every biome's `effects` now also carries (7b) an ambient `particle` and (7c) a vanilla-sourced
  soundscape** — `ambient_sound`/`additions_sound`/`music` (Nether ambience loops for the alien biomes, calm
  overworld music for the open ones) + the existing `mood_sound`. Worldgen continues: `noise/hills.json` (terrain
  relief) + `noise/caverns.json` (the deep cave-carve 3D noise, Phase 9),
  `configured_feature/` + `placed_feature/` (`lumen_crystal_ore`, `luminite_ore` [10a, dimension-wide via
  the `luminite_ore` biome modifier],
  `patch_moonblossom`, `patch_glow_fern`, `glowwood_tree`, `glowroot_tree` [1×1], `glowroot_tree_2x2`
  [custom feature], `patch_glasspetal` [5d.2], `shimmerstone_ore` [v1.2.1, `ore` blobs in the Glasspetal Crags],
  `giant_glowcap` [5d.3], `lumenwater_pool` [5d.4, **custom
  chunk-safe pool feature** — was a crashing vanilla `lake`] +
  `patch_glow_algae` + `patch_lumen_reeds`, `undercrown_glowvine` [5d.5] + placed-only `undercrown_crystal`/
  `undercrown_pool`, `stillbloom` [5d.6, custom feature], `lumen_reef` [Phase 9, custom seabed-coral feature on
  `OCEAN_FLOOR_WG`]; placed-only `glowroot_forest_trees`
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
  `structure_set` (spacing 24/sep 8, in `undercrown_caverns`) + `chests/undercrown_relics` loot, and the
  **Small Vestige Outpost** (10b) — `structure/vestige_outpost.json` + `structure_set` (spacing 28/sep 9, in
  glade/forest/jungle/basin) + `chests/ruined_cache` loot, and the **Medium Vestige City** (10d) —
  `structure/vestige_city.json` (with a `spawn_overrides.monster` → shade_stalker + sporeling) + `structure_set`
  (rare, spacing 40/sep 13) + `chests/ruined_cache`/`chests/scholars_reliquary` loot, and the
  **`vestige_mine`** (11c) — `structure/vestige_mine.json` (same `lumenwilds:vestige_city` type but
  `"guaranteed_mine": true`) + `structure_set` (spacing 48/sep 16, distinct salt) so `/locate structure
  lumenwilds:vestige_mine` finds the nearest ancient city with a mineshaft, plus a
  `tags/worldgen/structure/vestige_city.json` structure tag (groups all Vestige ruins — outpost + city + mine —
  read by the `vestiges_of_light` advancement) — each with its `tags/worldgen/biome/has_structure/<name>.json`
  biome tag (`vestige_mine` reuses the city's). **10c** adds `chests/scholars_reliquary` (lore/crafting loot) + a `tags/item/glyph_tablets.json` item
  tag. **10f.2:** the city also generates a buried `VestigeVaultPiece` (no JSON — added in code), with
  `chests/vault` (high-tier) + `chests/engineers_cache` (tech) loot. **10g:** grand cities add `VestigeSpirePiece`
  towers (in code) with `chests/spire` loot. **11c:** some cities add a `VestigeMinePiece` (in code) with
  `chests/miners_cache` (raw materials) + `chests/engineers_mine_cache` (the liftshaft tech to rebuild) loot.
  Hand-authored (not datagen).
- `data/lumenwilds/advancement/*` — the **progression tree (Phase 9f)**: `root` (enter the dimension) →
  `living_light`→`anchored`, `the_wilds_provide`→`soothing_nectar`, `native_fauna`→`apex_of_the_dark`, and a
  biome-reach goal per biome (`into_the_glowroot`, `crystal_highlands`, `spore_rainforest`, `the_glowing_mire`,
  `beneath_the_crown`, `sanctuary`); **+ `brick_of_living_light`** (10a — obtain Glowbrick) **+
  `vestiges_of_light`** (10b — stand inside any `#lumenwilds:vestige_city` ruin) **+ `the_city_remembers`**
  (10c — obtain a memory shard or any `#lumenwilds:glyph_tablets` item) **+ `still_on_watch`** (10f — kill an
  Echo Sentinel, via `player_killed_entity`) **+ `under_a_dead_skyline`** (10g — reach a `vestige_city`
  structure) **+ `carried_by_the_field`** (11b — obtain a Lumen Field Projector). Triggers: `changed_dimension` /
  `inventory_changed` /
  `location`+biome/+structure / `player_killed_entity`; direct-text titles (no lang keys). Hand-authored.
- `data/lumenwilds/neoforge/biome_modifier/*` — `lumen_reef.json` (the project's **first NeoForge biome
  modifier**, `neoforge:add_features`: injects `lumen_reef` into the 6 surface biomes at `vegetal_decoration`,
  avoiding each biome's feature list + the order topo-sort), `glowberry.json` (Glowberry Bush on green biomes),
  and `luminite_ore.json` (10a — `luminite_ore` into all 7 biomes at `underground_ores`). The chest loot tables
  (`loot_table/chests/*`, Phase 9) are now **tiered** — a guaranteed signature reward pool (enchanted gear/books,
  Lumen Anchor, striker, crystal blocks) + themed mid loot + treasure scaled by structure difficulty (no more
  all-filler chests). Underwater the surface rule places **`lumensand`** as the seabed (was dead moonloam).
- **Lumenwater fishing (v1.1f, reworked v1.1.3):** `data/neoforge/loot_modifiers/global_loot_modifiers.json`
  (the **required** GLM index) + `data/lumenwilds/loot_modifiers/lumenwater_fishing.json` (a **custom**
  `lumenwilds:lumenwater_fishing` GLM = `loot.LumenwaterFishingModifier`, `location_check` on dimension + the
  `#lumenwilds:lumenwater` fluid tag) that **REPLACES** the vanilla catch (clears it, rolls our table) with
  `loot_table/gameplay/fishing/lumenwater.json` — native fish + lumen junk + treasure (vanilla enchanted
  rod/bow/book + the spell-books). (Was a `neoforge:add_table` that *appended*, leaving earth fish; the custom
  replace removes cod/pufferfish while keeping treasure.)
- **Fished enchantments (v1.1g):** `data/lumenwilds/enchantment/{lightfooted,nightsight,lumenward,glowbrand,
  sporestrike,rootbinding}.json` + `data/minecraft/tags/enchantment/tooltip_order.json` (append, so they show
  on items) + `enchantment.lumenwilds.*` lang; the books are rolled in the fishing sub-table's spell-book pool.
- **Patchouli guide (v1.1h, optional dep):** `data/lumenwilds/patchouli_books/lumenwilds_guide/book.json` (the
  ONLY part in `data/`; needs `use_resource_pack: true`) + the content under **`assets/lumenwilds/patchouli_books/
  lumenwilds_guide/en_us/{categories,entries}/*`** (client/resource side, post-1.20 Patchouli; ~24 entries,
  one per biome). Obtained two ways: the `recipe/lumenwilds_guide.json` craft (**book + glowstone dust**,
  `mod_loaded`-gated) and the creative tab (`book.json` `creative_tab` + `dont_generate_book:false`). Patchouli
  is `runtimeOnly` in `build.gradle` (Modrinth maven) + `optional` in `neoforge.mods.toml`. **Entry text uses
  real `$(l:entryId)…$(/l)` cross-links, never bare `$(l)`** (which renders `[ERROR]`).
- `data/neoforge/data_maps/block/strippables.json` — axe-stripping (glowwood/glowroot log+wood → stripped).
- `data/c/tags/item/*` (v1.1e) — the universal `#c:` convention food/crop tags (`foods`, `foods/{fruit,berry,
  raw_meat,cooked_meat,raw_fish,cooked_fish,soup}`, `crops`) so lumen foods integrate into Farmer's Delight /
  Create / Delightful (and any `#c:`-aware cooking mod) with no hard dependency.
- `data/minecraft/tags/block/mineable/{pickaxe,axe,shovel,hoe}.json` + `leaves` (glowwood + glowroot) +
  `tags/block/{needs_stone_tool,needs_iron_tool}.json` (v1.2 harvest tiers — stone family vs. valuable
  ores/crystal/Luminite) + `logs.json` (all `_log`/`_wood` blocks — **required for leaf decay to recognise the
  trunk**, Phase 9d fix); the tool **item** tags `tags/item/{pickaxes,axes,shovels,hoes,swords}.json` +
  `tags/item/enchantable/*.json` (v1.2 — type + enchantability for the Moonstone/Luminite tools, from
  `ModItemTagProvider`); `tags/block/dirt.json`
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
- **Light-emitting default fluid is fine HERE — even with aquifers ON — the real danger is bulk single-tick
  placement, not the fluid being default.** `default_fluid` **is `lumenwilds:lumenwater`** (light 4); seas/ponds/
  cave-pools glow teal. The old "11.5s spike" fear was a pathological `/fill` of 18k contiguous blocks in ONE tick;
  the "31s behind while walking" was the **leaf-decay flood**, not the fluid. Re-measured post-leaf-fix with a temp
  `ServerTickEvent.Pre/Post` timer + `setChunkForced`: a 12×12 region flooded to `sea_level 110` → **11.2k** light-4
  Lumenwater blocks, steady tick **~0.9–2.6 ms = water baseline** (1 "Can't keep up", the one-time gen). And the full
  worldgen with **`aquifers_enabled: true`** (dramatic terrain + big seas + cave pools, ~140k+ surface Lumenwater +
  cave pools per region) still gen'd in ~5s/144 chunks with 1 "Can't keep up" — because worldgen places fluid
  chunk-by-chunk and light is computed once at gen (static blocks = ~0 per-tick cost). Note aquifers-on here uses
  LESS Lumenwater than aquifers-off-flooded (pools at the water table vs. fully-flooded sub-sea caves). **Lesson:
  bounded worldgen fluid is free; avoid only the single-tick bulk `/fill`.**
- **A 2D (`y_scale: 0`) relief noise that can exceed the vertical gradient's clamp makes terrain SPIKE to the
  build ceiling.** The terrain density is `y_clamped_gradient(…→ -1.6 above y130) + hills relief`; the relief
  noises are `y_scale: 0` (constant in Y) and sum to ~±2.4. At any column where relief > 1.6 (the clamp
  magnitude), density stays positive for ALL y above y130 → a thin grass-topped spire to y319. Fix: a **top
  suppressor** — a `y_clamped_gradient` that is 0 below ~y128 (leaves the dramatic terrain untouched) and ramps
  strongly negative above (to_y 175, to_value −28), added to the base in BOTH `initial_density_without_jaggedness`
  AND `final_density`. Caps terrain ~y125; **verified** force-gen 20×20 → max y125, 0 columns > y140, 0 runaway.
  (Either add this, raise the clamp magnitude past max relief, or give the relief a small `y_scale`.)
- **Surface structures must anchor to `OCEAN_FLOOR_WG`, not `WORLD_SURFACE_WG` — the latter includes fluid, so
  they generate on the SEA SURFACE and float.** All the procedural structures (`GlowrootTree`/`MegaGlowcap`/
  `Rootshrine`/`GlasspetalSpires`/`LumenboundRuins`) now use `getFirstOccupiedHeight(..., OCEAN_FLOOR_WG, ...)`
  (`UndercrownRelics` keeps WORLD_SURFACE only as a deep-Y reference — it's buried). For wide/multi-part
  structures on the dramatic cliffy terrain, anchoring alone isn't enough — also **root each part DOWN to the
  ground** (`GlasspetalSpiresPiece#fillFoundation`: fill from the base down through replaceable blocks until
  solid) or the base floats over slopes/water.
- **Never call `WorldGenLevel#getBiome` (or any neighbour-chunk read) inside a `StructurePiece#postProcess`.**
  `getBiome` → `getNoiseBiome` → `WorldGenRegion#getChunk`, which throws **`Requested chunk unavailable during
  world generation`** when the piece's `origin` is in a chunk outside the currently-generating chunk's small
  available radius. It's **position-dependent**, so sparse force-gens / playtests miss it — a dense-city
  force-gen (spacing 7) crashed on it where the sparse one (spacing 40) never did. Decide anything biome-derived
  at **placement** (`findGenerationPoint`, via `context.chunkGenerator().getBiomeSource().getNoiseBiome(x>>2,
  y>>2, z>>2, context.randomState().sampler())`) and pass it into the piece as a field (NBT-saved), like `tier`.
  Fixed for the Vestige City + Mine flavor (`VestigeCityPiece.flavorFor(Holder<Biome>)` is now static + called
  from the structure). **Verified:** a 1522-chunk dense-city force-gen generated 380 city + 15 mine chunks with
  zero exceptions.
- **To detect caves at structure-placement time, probe `ChunkGenerator#getBaseColumn`, NOT neighbour-chunk
  reads.** Reading neighbour chunks in `findGenerationPoint`/`postProcess` risks "chunk unavailable"; the safe
  way is `generator.getBaseColumn(x, z, heightAccessor, randomState)` → a `NoiseColumn` of the column's
  noise-terrain (read-only math, the same probe vanilla uses for terrain adaptation). This dimension's caverns
  are **noise** caves (folded into the density), so they appear in the base column — `VestigeMinePiece#findCaveFloor`
  scans it for an open run over a solid floor. Count air **and** fluid as "open" (sub-sea noise caverns read as
  the default fluid in the base column even though aquifers later carve them to air pockets — see the noise-cave
  census gotcha).
- **Noise CAVES must be folded INSIDE the `interpolated`/`squeeze` final-density tree, not added outside it.**
  Adding a carve term as `add(squeeze(interpolated(base)), caveCarve)` does NOT carve — even a constant `-2.0`
  left the deep solid (the engine only honours the interpolated cell tree for terrain). Fold it in:
  `squeeze(0.64 * interpolated(blend_density(add(base, caveCarve))))`, where `caveCarve = mul(depthMask(0 above
  ~y45 → 1 deep), mul(-6, max(0, caverns_noise - 0.1)))`. **Verified** by counting deep *non-solid* (air+fluid),
  not air alone — sub-sea-level caves FLOOD with the default fluid, so an air-only census reads 0 even when caves
  carve fine (a 11.6% deep-hollow region was 779 air + 260k fluid until aquifers were enabled).
- **Aquifers: `lava` router `0.0` = water-only; ANY non-zero constant ENABLES aquifer lava (counter-intuitively,
  more negative = MORE lava).** With `0.0`, lava is only the engine's global floor at y<-54 (below the Undercrown
  play zone -20..-40, harmless). `aquifers_enabled: true` turns the deep noise caves into AIR caverns + Lumenwater
  pools (the bible's Undercrown) instead of flooding.
- **Dev worlds use a RANDOM seed each `runServer` — pin `level-seed` in `run/server.properties` before comparing
  terrain numbers across builds** (absolute height/sea/cave counts swing wildly by seed; I burned several runs
  comparing different worlds before pinning the seed).
- **Custom logs MUST be in `#minecraft:logs` or ALL leaves decay (incl. ones touching the trunk).**
  `LeavesBlock`'s distance check (`getDistanceAt`) only counts a neighbour as "distance 0" if it `is(BlockTags.LOGS)`
  — there is no Forge hook for it. Our Glowwood/Glowroot logs were never added to that tag, so every leaf computed
  `DISTANCE 7` and decayed regardless of proximity — the user watched leaves vanish *next to* trunks, and the
  dimension-wide mass decay drove an item flood (the "E: ramping" lag). Fix: `data/minecraft/tags/block/logs.json`
  lists `glowwood_log`/`glowwood_wood`/`stripped_glowwood_log`/`stripped_glowwood_wood`/`glowroot_log`, and
  `ModTagProvider` adds any `_log`/`_wood` block to `BlockTags.LOGS` (non-exclusive with axe). `glowroot_leaves`
  was also missing from `#minecraft:leaves` — added. **Verify leaf SURVIVAL by counting leaf BLOCKS, not items:**
  with leaf decay now dropping mostly nothing, an item census reads `items=0` whether leaves survive OR silently
  vanish — that false signal once "verified" a still-broken fix. **Verified (the right way):** a temp census
  `setChunkForced`'d a 12×12 region, ticked 6000t → `leaves=12424 logs=6328` dead-flat (0 decay), `items=0`.
- **Tree leaves: non-persistent + a real leaves loot table + canopy geometry sized so leaves stay within 6 of
  a log.** Two further compounding bugs flooded the world with leaf-block items (>20,000 entities, ~0.94 TPS): (a)
  our leaf loot tables were **drop-self**, so any decaying leaf dropped the leaf *block*; and (b) the custom
  Glowroot canopies put leaves **>6 leaf-steps from a log**, so they decayed *on generation* (no player). The
  decay rule: a leaf survives only if a log is within **6 orthogonal leaf-steps**; worst-case for a leaf sphere
  radius R around one log is `R·√3`, for a leaf disc around the trunk `(R−trunkRadius)·√2`. Fixes — keep
  vanilla's normal "decay when logs are cut" mechanic AND zero gen-decay: (1) **proper loot tables**
  `loot_table/blocks/glowwood_leaves.json` + `glowroot_leaves.json` (shears/silk → block, else sapling ~5% /
  stick / mostly nothing — never the block); (2) leaves **non-persistent** (`GlowrootShape#leaves` sets
  `DISTANCE 7`; `glowwood_tree.json`/`glowroot_tree.json` `foliage_provider` `"persistent": "false"`); (3)
  `GlowrootShape.MEGA`/`MEDIUM` params sized to keep every leaf within 6 — **end-blob ≤3, along-blob ≤4 (on a
  log line), crown horiz ≤ trunkRadius+4** — so the wide canopy comes from log-supported branch blobs, not a
  giant trunk-only crown. **(None of this matters until the logs are in `#minecraft:logs` — see above.)**
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
- **Light EMISSION (`lightLevel`) ≠ emissive RENDERING — a "glowing" block needs both.** `lightLevel` makes a
  block *cast* light into the world, but it's only visible in darkness — in bright/daylight (or even the
  Lumenwilds' twilight at a low value) the block still renders normally and looks unlit. To make a block
  *look* luminous in any light, its block MODEL must be emissive: an element with
  `"neoforge_data": { "block_light": N, "sky_light": 0 }` renders that face at min light N (15 = fullbright).
  Shape geometry lives in the vanilla parent models, so emissive shapes need a custom `_emissive_<shape>`
  parent (copy the vanilla model, add `neoforge_data` to every element) that the block model inherits. The
  glowing wood (v1.1.2) does both: emissive `_emissive_*` shape parents + a `lightLevel`. Set BOTH or the
  feedback "it doesn't glow" recurs (it was emitting faint light but not rendering emissive).
- **Patchouli (1.20+) books split across `data/` and `assets/`.** Only `book.json` stays in
  `data/<modid>/patchouli_books/<book>/` and it MUST set `use_resource_pack: true`; the categories/entries/
  templates live in `assets/<modid>/patchouli_books/<book>/<lang>/…` (client side). A book.json with
  `use_resource_pack:false` (the old layout) is rejected at load (`Failed to load book … skipping`) — caught
  on a headless `runServer` (Patchouli validates book.json server-side). Give the book via a recipe whose
  result is `patchouli:guide_book` with a `patchouli:book` component = the book id; gate it `mod_loaded`.
- **Global Loot Modifiers need the index file + live under `loot_modifiers/` (plural).** The per-modifier
  JSON is `data/<modid>/loot_modifiers/<name>.json` AND it must be listed in
  `data/neoforge/loot_modifiers/global_loot_modifiers.json` (`{"replace":false,"entries":["modid:name"]}`) or
  it never loads (verified in `LootModifierManager`: `folder = "loot_modifiers"`, reads the index for ordering).
  NeoForge 21.1 ships a built-in **`neoforge:add_table`** GLM (fields: `conditions` array + `table` id) — no
  custom GLM class/serializer needed to append a sub-table to e.g. `gameplay/fishing`. But `add_table` only
  **appends** — to REPLACE a catch (e.g. strip vanilla fish from Lumenwater fishing) write a custom
  `LootModifier` whose `doApply` does `generatedLoot.clear()` then rolls its table (`getRandomItemsRaw`, with
  `@SuppressWarnings("deprecation")` as NeoForge's own AddTableLootModifier does) — see `loot.LumenwaterFishingModifier`.
- **Vanilla `FishingHook#catchingFish` hardcodes its bubble/splash particles to `Blocks.WATER`**, not the
  `#minecraft:water` fluid tag — so a modded water block (Lumenwater) catches fish but shows no strike
  animation. Fix is a mixin redirecting those `BlockState#is(Block)` checks to also accept the modded water
  (`mixin.FishingHookMixin`). (The lure-ripple `FISHING` particles aren't gated, which is why it half-worked.)
- **1.21.1 data-driven enchantment schema (verified from source — easy to get wrong):** `supported_items`/
  `primary_items` use the `#minecraft:enchantable/<foot_armor|head_armor|chest_armor|sword|weapon|…>` tags (NOT
  `*_enchantable`). "While worn/held" = a `minecraft:tick` effect (`List<ConditionalEffect<EnchantmentEntityEffect>>`,
  element `{ "effect": { "type": "minecraft:apply_mob_effect", "to_apply": …, "min/max_duration", "min/max_amplifier" } }`)
  — runs every server tick, so a short refreshing duration stays topped up while equipped and lapses when removed.
  "On hit" = `minecraft:post_attack` (element adds `"enchanted":"attacker","affected":"victim"`).
  **`apply_mob_effect` durations are in SECONDS** (×20 internally) — a weapon on-hit needs ~3–12, not ticks.
  To make an enchantment fishing/loot-only, simply leave it OUT of `#minecraft:in_enchanting_table`/`tradeable`/
  `on_random_loot`; add it to `#minecraft:tooltip_order` (append) so it still displays on items.
- **Trees overwrite structures unless the structure decorates at a LATER step.** Within each chunk, each
  `GenerationStep.Decoration` runs structures-at-that-step THEN features-at-that-step; trees are
  `vegetal_decoration` (step 9), so a structure at `surface_structures` (step 4) is built first and then trees
  grow *through* it (tree-through-chest, partial roots in a plaza). Fix: set the built/loot structures'
  `"step"` to **`top_layer_modification`** (10, after trees) so their pieces build last and overwrite trees.
  The Vestige family + Rootshrine + Glasspetal Spires + Lumenbound Ruins use this; `glowroot_tree`/`mega_glowcap`
  (organic) stay at `surface_structures`, `undercrown_relics` stays underground. (`step` only changes piece
  build TIMING, not `findGenerationPoint` placement — so Y/spacing are unaffected.)
- **A `random_patch` flora feature must filter on `would_survive`, not `matching_blocks:air`.** The latter
  places the plant in ANY air cell within `y_spread` — including the air *directly above* another plant — so
  moonblossoms/glow ferns generated floating on top of glowberry bushes (the v1.1b bug). `would_survive` (the
  block's own `canSurvive`, what `patch_glowberry` already used) only places where the plant is actually
  supported. Every flora `random_patch` here now uses `would_survive` + `WORLD_SURFACE_WG`.
- **`DeferredRegister.getEntries()` yields `DeferredHolder`**, not `DeferredBlock`/`DeferredItem` —
  iterate with `var`. `registerSimpleBlockItem(...)` returns `DeferredItem<BlockItem>`.
- **`@EventBusSubscriber` takes NO `bus` param** (NeoForge 21.1.1+): the `bus`/`Bus` value is **ignored** and
  deprecated-for-removal — the annotation auto-routes each `@SubscribeEvent` to the correct bus by event type
  (events implementing `IModBusEvent` → mod bus, everything else → `NeoForge.EVENT_BUS`). Just write
  `@EventBusSubscriber(modid = MOD_ID)` (+ `value = Dist.CLIENT` for client-only); a class can even mix mod-bus
  and game-bus listeners. (Verified: datagen + all registration subscribers fire with the param removed.)
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
- **Cross-mod RegisterEvent crashes report the OTHER mod, but the trigger can be our load-order — fix with an
  optional ordering dep, and reproduce by adding the mod as a dev `runtimeOnly`.** v1.1.3 boot-crashed large
  packs running **Create** (`NPE: unbound create:chocolate_bucket`, blamed on Create's `RegisterEvent`).
  Mechanism (decompiled from the cached Create jar — `javap -p -c`): `Create#onRegister` fires per registry
  phase and, on the `minecraft:trigger_type` phase, calls `AllAdvancements.register()` whose `<clinit>` reads
  a fluid bucket registered back in the `minecraft:item` phase. NeoForge's phase order is `block → item →
  trigger_type → …` (verified via a temp `RegisterEvent` probe logging `event.getRegistryKey()` +
  `BuiltInRegistries.ITEM.containsKey(create:chocolate_bucket)`), so the bucket is normally bound by
  `trigger_type` — but a big pack's mod-load order (which v1.1.3's extra registrations shifted) can surface
  Create's latent ordering bug. Fix: an **optional `ordering="AFTER"` dep on `create`** in `neoforge.mods.toml`
  (`type="optional"`, so a no-op without Create) → Create registers first in every phase. To reproduce/verify
  in dev, temporarily add `runtimeOnly 'maven.modrinth:create:6.0.10+mc1.21.1'` to `build.gradle` (Create 6
  jarjars Flywheel — no separate dep) and `runServer`; **note a 2-mod repro may NOT trigger an order-sensitive
  pack crash** (it booted clean here), so it confirms the fix doesn't regress more than it proves the fix.
- **`requiresCorrectToolForDrops()` does NOTHING without a `#needs_*_tool` tag — it defaults to "any tool
  works."** A block with the flag set but in no `needs_stone/iron/diamond_tool` tag is in no
  `incorrect_for_*_tool` tag either, so every tier is "correct" → a wooden pickaxe harvests it, and
  harvest-HUD/tooltip mods (which read the same tags) report it as wood-harvestable. This was the v1.2 bug:
  the stone/ore blocks all set the flag but were never tiered. Fix: `ModTagProvider` adds them to
  `BlockTags.NEEDS_STONE_TOOL` / `NEEDS_IRON_TOOL` (vanilla's `incorrect_for_*` tags reference these). The
  matching tool tiers reuse `INCORRECT_FOR_STONE_TOOL`/`INCORRECT_FOR_IRON_TOOL` so a Moonstone (stone) tool
  can't drop an iron-tier block and Luminite (iron) can.
- **A custom tool item's `Item.Properties` needs `.attributes(...)` or the tool has no attack damage/speed.**
  `new PickaxeItem(tier, props)` only wires the mining `Tool` component; the melee stats come from
  `props.attributes(DiggerItem.createAttributes(tier, dmg, speed))` (or `SwordItem.createAttributes` for
  swords) — see `ModItems#pickaxe/axe/shovel/hoe/sword`. And register the tools into the `#minecraft:enchantable/*`
  item tags (`ModItemTagProvider`) or they can't be enchanted.
- **A non-`Animal` creature mob can't use `Animal::checkAnimalSpawnRules` in `RegisterSpawnPlacementsEvent`**
  (the method ref is typed `SpawnPredicate<Animal>`). The Sporeman is an `AbstractVillager` (→ `AgeableMob`,
  not `Animal`), so it uses `Mob::checkMobSpawnRules`. (Trader mobs reuse `WanderingTrader`'s pattern:
  `AbstractVillager` + `TradeWithPlayerGoal` + `updateTrades`/`rewardTradeXp`; open the screen with
  `openTradingScreen`.)

---

## Build & verify

Versions in [gradle.properties](gradle.properties): MC `1.21.1`, NeoForge `21.1.233`, Java 21, Gradle
8.10, ModDevGradle `2.0.141`, Spotless `6.25.0`. License **Apache-2.0**. `JAVA_HOME` → a JDK 21. Optional
dep: **Patchouli** `1.21.1-93-neoforge` (`runtimeOnly`, Modrinth maven — the in-game guide; dev-run only,
never compiled against, not bundled).

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

### Dev-run gotchas (port / JVM / headless verification) — read before `runClient`/`runServer`

These bite **every** session; the drill that avoids them:

- **FAIL FAST: never start the ~90s build until the port is provably free.** The dev `runServer` builds first
  and only tries to `bind` at the very end, so a held port surfaces as `BindException` ~90s in — wasting a full
  cycle. **Pre-flight check, abort if not zero**, *before* launching: stray dev runs == 0 AND port listeners ==
  0. If non-zero, kill + re-check (or bump the port); do not launch on faith.
- **Match dev runs by `modFolders=lumenwilds`, NOT `forgeserverdev`.** The `forgeserverdev`/`forgeclientdev`
  token is a *program* arg that `Win32_Process.CommandLine` often truncates away, so those filters silently miss
  the real JVMs (they show as "OTHER") — the cause of repeatedly relaunching into a still-held port. The
  reliable tag present in every Lumenwilds dev JVM's args is `-Dfml.modFolders=lumenwilds`. Always filter
  `Name='java.exe'` too (else the PowerShell query matches its own command line and miscounts):
  `Get-CimInstance Win32_Process -Filter "Name='java.exe'" | Where-Object { $_.CommandLine -match 'modFolders=lumenwilds' } | Stop-Process -Force`.
  After killing, **wait ≥8s and re-verify count==0** (a force-killed JVM lingers + the socket sits in
  `TIME_WAIT`); when in doubt **switch `server-port`** in `run/server.properties`.
- **A force-killed (or crashed-but-still-ticking) `runServer`/`runClient` keeps the socket + `latest.log`.** A
  server that hits a runtime exception during gen does NOT exit — it keeps ticking and holding the port, so the
  *next* launch collides. Always confirm the prior run actually died (count==0) before relaunching.
- **`run/logs/latest.log` gets lock-contended** when two dev JVMs overlap (the new one logs `Unable to delete
  file latest.log … used by another process`). Don't trust `latest.log` across runs — **redirect the run's
  console to your own file** (`./gradlew runServer > /tmp/srv.log 2>&1 &`) and read that. (The two `Unable to
  delete` ERROR lines are benign — not a crash.)
- **Force-killing a server skips the world save** → no `region/*.mca` written, so worldgen can't be inspected.
  For headless worldgen verification use a **self-stopping** harness: a `#minecraft:load` function that
  `forceload add`s a region then `schedule`s a finish function which runs `save-all flush` + `stop`. **`stop`
  and `save-all` require `function-permission-level=4`** in `run/server.properties` (default 2 makes them parse
  as "unknown command" inside a function). `#load` also fires too early to `forceload` a custom dimension —
  `schedule` the real work ~60t later.
- **`server.properties` is reset whenever you revert test scaffolding**, silently dropping `level-name` /
  `level-seed` / `function-permission-level`. Symptom: the verify server gens into `world` (not your temp
  world) and never self-stops (the `stop` command lacks permission). **Re-set all four keys** (`level-name`,
  `level-seed`, `function-permission-level=4`, `server-port`) every time before a verify run; keep a
  `server.properties.bak` to restore the user's real config afterward.
- **Inspect generated chunks by decompressing region files** (`run/<world>/dimensions/lumenwilds/lumenwilds/
  region/r.*.mca`): parse the 4 KB location table, `zlib.decompress` each chunk, and substring-search the raw
  NBT for block/structure ids (e.g. `vestige_mine`, `descent_field`, `lumen_field_projector`). This is how to
  confirm a structure/feature actually placed without loading a client.
- **Never run a headless `runServer` while the user has `runClient` open** — they share `run/` (config + logs)
  and contend; ask the user to close the client first, verify, then relaunch it.
