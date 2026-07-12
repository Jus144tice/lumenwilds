# Changelog

All notable changes to The Lumenwilds are documented here. The format is based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.7.2] - 2026-07-11
**Visual follow-ups from playtesting.**

### Fixed
- **Ripe Glowgourds/Moonmelons now actually connect to their stem.** v1.7.1 corrected the stem's rotation, but
  the stem's reaching-arm sprite was a straight stem instead of the proper bent "arm" shape, so it never met the
  fruit. The attached-stem sprite is now the correct reaching-arm shape (recolored to the gourd's teal).
- **The glowing teal rain no longer looks "grid-y."** Each rain column was oriented along the line from the
  camera, so columns straight ahead rendered edge-on and side columns rendered wide — which revealed the block
  grid. The rain now faces the camera like vanilla rain, blending into a smooth sheet.

## [1.7.1] - 2026-07-11
**Playthrough fixes — water, plants, the boat, the umbrella, and a rename.**

### Fixed
- **Boats no longer look "full of water" over Lumenwater.** Lumenwater was rendering as an *opaque, solid-pass*
  fluid — a custom fluid defaults to the solid render layer — which drew before boats and let the sea show
  straight through the hull. Lumenwater now renders **translucent**: proper see-through water, and boats look
  dry inside again.
- **Wild plants no longer spawn in or over Lumenwater, underground, or stacked on each other.** Every wild-plant
  patch (glow ferns, moonblossom, lumenberry bushes, all crops, and the gourds) now only generates where the
  spot is **air** *and* the plant can actually survive there — so no more ferns/crops in the sea, no plants
  growing on top of other plants, and no drifting plant-drop litter floating in the water.
- **Baby Lumen Silkworms are now visibly smaller than adults** (they render small and grow to full size).
- **Growing gourds now look attached to their vine.** The bent "attached stem" was rotated 90° wrong, so a ripe
  Glowgourd/Moonmelon looked disconnected from the stem pointing at it.
- **Freshly-planted gourd seeds now show a small stem sprout** instead of looking like bare Moonloam.

### Changed
- **"Echo Dust" is renamed to "Shade Dust."** It's a Shade Stalker drop (used to craft the Lumen Anchor), and
  the old name wrongly implied a link to the unrelated Echo Sentinel. **Breaking rename:** existing *Echo Dust*
  items are removed on world load — gather *Shade Dust* anew from Shade Stalkers (still an 8% drop, or 2 Shade
  Claws → 1 Shade Dust).

### Fixed (compatibility)
- **The Luminite Umbrella now shields rain-allergic race mods (e.g. an Origins feline).** Mods like Origins/Apoli
  check whether it's raining **at the player's position** rather than on the entity, which the umbrella's existing
  entity-level shield didn't cover. Holding the umbrella now also suppresses that position check — so a cat-person
  under an umbrella stops taking rain damage.

## [1.7.0] - 2026-07-10
**Broken portals scattered through every realm — with reward chests.**

Ruined portal sites now generate across the dimensions, each with a loot chest to reward finding it:

- **Broken Lumenbound (Lumen) portals** now also generate in the **Lumenwilds** (previously Overworld-only), in
  addition to the existing Overworld ones. As before, there's **no lava/magma** near them — just the broken
  frame, weathered stone, and rubble — and the chest has a **good chance of a Lumen Striker and/or the materials
  to craft a striker and Lumenbound Stone** (Lumen Crystal Shards, Lumenbound Stone, iron, amethyst, glow ink…).
- **New: broken Duskglass (Dusk) portals** generate in the **Lumenwilds** and the **Nether** — a broken Duskglass
  frame with **a couple of small lava pools** beside it (fitting, since Duskglass is quenched lava). The chest has
  a **high chance of Lumenwater buckets**, plus the new Dusk Striker and/or its materials, and valuable treasure
  (Luminite, Lumen Crystal, gold, the odd enchanted book / diamond).
- Vanilla's ruined obsidian portals already cover the Overworld and the Nether, so between all three there's a
  ruined portal to stumble on wherever you are.

### New: the Dusk Striker (each portal has its own igniter)
- **Dusk Portals are now lit with a new Dusk Striker, not flint & steel.** Every portal now has its own key:
  obsidian → fire, Lumenbound Stone → **Lumen Striker**, Duskglass → **Dusk Striker**.
- The **Dusk Striker** is crafted from Lumenwilds materials — **Emberglow + Duskglass + Luminite Ingot** (stacked
  top-to-bottom) — and, like the Lumen Striker, is a durable tool (each ignition costs one use). It only lights a
  Duskglass frame in the Lumenwilds or the Nether.
- The broken **Duskglass Ruins** chests now drop the **Dusk Striker and/or its crafting materials** instead of
  flint & steel, so a ruined Dusk portal always points you toward re-lighting one.

### Fixed / Changed (Glowgourds)
- **Glowgourds are now glowing teal**, not orange — a proper bioluminescent gourd that glows in any light (both
  the gourd and its Carved Glowgourd lantern).
- **Fixed the black-and-pink artifact on a fully-grown Glowgourd/Moonmelon on the vine** — the attached stem's
  model was missing a texture (`upperstem`), so one panel rendered as the missing-texture checkerboard.
- **Fixed gourd seeds sometimes being consumed without planting on Lumen Farmland** — the stem now anchors its
  survival to the Lumenwilds' own soils before the vanilla soil hook that a modpack could intermittently override.

## [1.6.1] - 2026-07-10
**Portals are strictly targeted to their two realms.**

Each portal type now only works between its two intended realms and is a no-op in the third ("alien") realm:

- **Lumen portal** (Overworld ↔ Lumenwilds) no longer lights or teleports in the Nether (or any other
  dimension) — a Lumenbound Stone frame there just stays dark.
- **Obsidian / vanilla Nether portal** (Overworld ↔ Nether) no longer forms in the Lumenwilds — lighting an
  obsidian frame there just makes fire, no portal. Use a **Dusk Portal** to reach the Nether from the Lumenwilds.
- (The Dusk Portal was already Lumenwilds ↔ Nether only.)

## [1.6.0] - 2026-07-09
**Duskglass + the Dusk Portal to the Nether.**

- **Lumenwater now reacts with lava** — just like water, but into dimension-native stone: a lava **source** +
  Lumenwater → **Duskglass** (the Lumenwilds' "obsidian" — a dark, glossy, faintly teal-glowing, blast-resistant
  stone), and **flowing** lava + Lumenwater → **Cobbled Moonstone**. (Mostly a Lumenwilds affair — bring lava in
  a bucket, like an Overworld obsidian farm.)
- **New: the Dusk Portal.** Build a **Duskglass frame** (2×3 up to 21×21, like a Nether portal) and light it with
  **flint & steel** to open a portal between the **Lumenwilds and the Nether** — in the Lumenwilds it goes to the
  Nether, in the Nether it comes back. (It only lights in those two dimensions; elsewhere flint & steel behaves
  normally.) A real new progression: Overworld → Lumenwilds → gather Duskglass from lava → build the portal → the
  Nether, with the Nether ↔ Lumenwilds link running both ways.

## [1.5.0] - 2026-07-09
**Farming overhaul pass — plant physics, gourds, seed balance, and the Glowberry → Lumenberry rename.**

> ⚠️ **Breaking (world data):** the Glowberry item/bush was renamed to **Lumenberry** (registry ids changed).
> Existing `lumenwilds:glowberry` items and planted glowberry bushes in a world made before this update will be
> **removed on load** (they become invalid registry entries). Everything else is unaffected. This resolves the
> name/recipe clash with vanilla's glow berries.

- **Renamed Glowberry → Lumenberry** everywhere (item, bush, recipes, loot, tags, worldgen, book), so it no
  longer collides with vanilla glow berries.
- **Vegetation obeys surface-placement physics.** Moonblossom is now *replaceable* like the ferns — placing a
  block where a flower is **replaces** it instead of stacking on top and burying it. And the native crops no
  longer grow **under** a solid block (they break when capped), so nothing farms itself underground.
- **Glowgourd/Moonmelon stems work on tilled Lumenwilds soil.** They were hard-coded to vanilla farmland only,
  so on hoed **Lumen Farmland** they broke instantly (the "seeds vanish / show blank" bug). They now grow on
  Lumen Farmland and Moonloam/Lumen Grass, like the other crops.
- **Burying Lumen Grass reverts it to Moonloam promptly.** Capping a grass block now turns it back within a
  moment instead of waiting on a slow random tick (which often never fired if you weren't standing there).
- **Crop seeds are no longer over-generous.** Harvesting a mature crop now gives a clean **1 produce + 1 seed**
  (plus a Fortune bonus), instead of the vanilla-style ~1.7 seeds that piled up 2:1. Farms stay replantable.
- **Lumen Fruit is now farmable.** It drops occasionally from **Glowroot leaves** (the apple-analog), so the
  night-vision food is renewable by growing Glowroot trees instead of being chest-loot only.
- **Glimmerroot planting clarified:** the Glimmerroot *is* its own seed (like a carrot) — right-click tilled
  soil with it to plant.

## [1.4.13] - 2026-07-07
**Shade Stalkers ambush from the periphery instead of spawning in your lap.**

- **Shade Stalkers no longer spawn right next to you.** They're meant to be patient, opportunistic ambush
  predators — set up in the dark at the edge of your awareness and strike when you move into range. But vanilla
  only keeps mobs 24 blocks away, which is exactly a Shade Stalker's follow range, so they'd appear close and
  immediately charge. Natural Shade Stalker spawns now require **≥ 40 blocks** from any player, well beyond their
  follow range — so a fresh one lurks in the shade and ambushes you as you approach, rather than materialising
  beside you and attacking. (Their darkness requirement and light-fleeing are unchanged; the Undercrown Relics
  Shade Stalker spawner and spawn eggs are unaffected.)

## [1.4.12] - 2026-07-06
**Lantern Beetles stop crowding out the other fauna.**

- **The ground fauna spawn properly again.** Lantern Beetles were in the same spawn-cap category as Lumen
  Grazers, Silkworms, and Rootbacks — and being prolific flyers (boosted by the Moonwake event, and persistent),
  they filled that shared cap so the ground animals barely spawned. Beetles are now in the **ambient** category
  (like vanilla bats), which has its own separate cap: they stay dense around you and refresh, but they no longer
  starve out Grazers, Silkworms, and Rootbacks. If you'd been struggling to find Silkworms, this is why.

## [1.4.11] - 2026-07-06
**Glowvine sever, cobbled-stone stonecutting, the Luminite Umbrella, and rain/root polish.**

- **Glowvine severs like a real vine.** Breaking a glowvine — or mining the ceiling it hangs from — now drops
  the whole strand below it, instead of leaving the middle floating. Cut the top and the strand falls; cut the
  middle and everything below drops while the part still anchored above stays. It's modelled on vanilla vines,
  so embedded rock-veins and ruin ground/wall cover (which have solid support) are untouched — only free-hanging
  strands sever.
- **Cobbled Lumenwilds stone works in the stonecutter.** The *mined* cobbled forms — Cobbled Moonstone and
  Cobbled Deep Moonstone — are now full stonecutter inputs (like vanilla cobbled deepslate), so you can cut
  freshly-mined stone into the whole family without smelting it to the smooth form first. (Previously only the
  smelted forms were accepted, so a stack of cobbled deep moonstone wouldn't even go in the stonecutter.)
- **New: the Luminite Umbrella.** A craftable light stone-tier weapon (lumensilk + luminite) that doubles as a
  rain shield — while you hold it in either hand, the rain doesn't wet you. Built for water-allergic race/class
  mods: it wards off the **rain** specifically, not water in general (standing in water still counts).
- **Lumenwater rain no longer looks like a rigid grid.** The rain texture was a barcode of dead-straight
  vertical lines; it's now scattered dashes/droplets so the streaks read as falling rain while keeping the
  glowing-teal alien look.
- **Colossal Glowroot roots that reach out over air now drop thick, tapering support tendrils** to the ground
  (matched to the root's footprint, with piers along the span) instead of a single-block spike, so they read as
  structurally sound. *(Applies to newly-generated trees.)*

## [1.4.10] - 2026-07-05
**Bookshelf enchanting fix.**

- **Glowwood & Glowroot bookshelves now boost an enchanting table.** They were missing from the
  `#minecraft:enchantment_power_provider` tag that the enchanting table reads, so — unlike vanilla bookshelves —
  they contributed no enchantment power. They now provide the bonus like any other bookshelf.

## [1.4.9] - 2026-06-25
**Emberglow Torch render fix.**

- **Fixed the black box around Emberglow Torches.** The torch models were missing their render type, so the
  transparent parts of the torch texture rendered as opaque black (visible from the sides). They now declare
  `render_type: cutout`, like vanilla torches — so they render cleanly from every angle.

## [1.4.8] - 2026-06-24
**Sporeling map color + spawn census fixes.**

- **Sporelings show as neutral on minimaps.** They're now a neutral mob type (not a `Monster`/`Enemy`
  subclass), so Xaero's/JourneyMap-style radar no longer paints them hostile red — matching their
  retaliate-only behavior. They still spawn as a dense swarm.
- **The native fauna actually spawn now.** All six animals (Lumen Grazer, Lumen Silkworm, Lantern Beetle,
  Sky Jelly, Glowmoth, Rootback) used the vanilla animal rule, which requires **light > 8** and grass-like
  ground — but the Lumenwilds is a *dim* dimension and several biomes are stone, so they barely spawned (you'd
  only reliably see beetles, which the Moonwake event boosts). They're now **light-agnostic and spawn on any
  ground**, like the hostiles already were — so silkworms, sky jellies, and glowmoths appear across their
  biomes day or night.
- **Crag Wraiths patrol the Crags.** The Glasspetal Crags are lit by mineral glow, so the darkness-gated Crag
  Wraith almost never spawned there; it's now light-agnostic, so the crags get their aerial threat.

## [1.4.7] - 2026-06-23
**Playthrough fixes + Emberglow torches.**

- **Shade Stalkers are surface-only now.** They no longer spawn in the Undercrown caverns (a surface ambush
  predator underground made no sense), and the Deep Hush ambient event — which used to force-spawn them right
  next to a deep player regardless of light — now (a) spawns the underground **Sporeling** swarm instead, and
  (b) **respects lighting**, so a lit base is safe. Natural surface spawns already respected light; this closes
  the event-spawn loophole that put stalkers inside lit houses.
- **Veinstone has a build tree.** The accent rock now has **Polished Veinstone** and **Veinstone Bricks**, each
  with stairs/slabs/walls — crafted and stonecut like the Moonstone family.
- **Emberglow Torches.** Craft Emberglow (the fuel material) over a stick into torches that burn with a
  greenish-blue flame. They work exactly like a normal torch (floor + wall placement, light 14) — the
  Lumenwilds' own torch variant.

## [1.4.6] - 2026-06-23
**Crash fix: placing a glowing wood chest.**

- **Glowwood/Glowroot chests no longer crash on placement.** They were registered as a plain vanilla
  `ChestBlock`, whose `newBlockEntity` hardcodes a `minecraft:chest` block entity and ignores the type given to
  it — so placing one put a `minecraft:chest` BE at a `lumenwilds:*_chest` block and the game threw
  `IllegalStateException: Invalid block entity … got Block{…glowroot_chest}`. They now use a proper
  `ChestBlock` subclass that creates the correct block entity (so they place safely, render with their glowing
  per-species texture, and pair into double chests). Reproduced and verified fixed on a server.

## [1.4.5] - 2026-06-23
**Worldgen plant placement fixes.**

- **Gourds no longer float.** Wild Glowgourds/Moonmelons could generate hanging in mid-air on slopes and
  cliffs (a full-block plant "survives" anywhere, and `random_patch` scatters tries without re-finding the
  ground per column). They now require solid ground directly below, like vanilla pumpkins —
  force-gen-verified: 0 of 16 generated gourds were floating.
- **No more plants on top of plants.** Glow Algae and Lumen Reeds were placing in any air cell (including above
  other plants); they now require valid soil (`would_survive`), like the other flora.

## [1.4.4] - 2026-06-23
**Weather, wool, and infinite water.**

- **Infinite Lumenwater sources.** Two Lumenwater source blocks placed close together now form an infinite
  source, exactly like vanilla water (`canConvertToSource`) — so you can scoop endlessly and run Lumenwater
  farms/aquariums. *(In the Lumenwilds; carried to the Overworld it still reverts to ordinary water — which is
  itself infinite — per the anti-OP rule.)*
- **Wool at last — the Lumen Silkworm.** The Lumenwilds had no wool animal (so no beds). A small glowing
  **Lumen Silkworm** now grazes the leafy biomes; it's breedable with Glow Fern and drops **Lumensilk**, and
  **4 Lumensilk craft into white wool** — so you can finally make beds. (Beds already worked here; you just had
  no wool.)
- **It rains in the Lumenwilds.** The surface biomes now get occasional rain — and it's bespoke **glowing teal
  "Lumenwater rain"** (the dimension rains its own native water). No snow (the climate's too warm). Rain also
  hydrates Lumen Farmland.

## [1.4.3] - 2026-06-22
**Lumenwater is now real, swimmable water.**

- **The root fix.** Instead of patching each water interaction one at a time, a single `mixin.EntityMixin`
  bridges Lumenwater into the engine's water checks (NeoForge routes them all through the vanilla water
  *FluidType*, which Lumenwater wasn't). Now `isInWater()` is genuinely true in Lumenwater — so **swimming
  physics, buoyancy, no fall damage, underwater breathing, boats, and fishing all work natively**, while the
  water keeps its teal glow (its eye-fog type is unchanged). The 1.4.2 `LivingFallEvent` fall-damage patch is
  removed — the systemic fix subsumes it.
- **A true glowing aquarium.** The **Bucket of Prismfin** now places **Lumenwater** (not plain water), so
  emptying it builds a glowing tank where the fish actually swims. *(In the Overworld, loose Lumenwater still
  slowly reverts to ordinary water per the dimension's anti-OP rule — build the aquarium in the Lumenwilds to
  keep it glowing.)*

## [1.4.2] - 2026-06-22
**Playthrough fixes.**

- **No fall damage in Lumenwater.** Jumping into Lumenwater from a height now behaves like water — it breaks
  your fall and deals no damage (the custom glowing fluid wasn't recognised by vanilla's water fall-reset).
- **Lumenwilds chests are lumen-only.** The in-dimension structure chests (caches, reliquaries, vaults,
  spires, mine caches, shrines) no longer hand out Overworld loot — diamonds/iron/netherite/emeralds/enchanted
  books are swapped for their lumen counterparts (Resonite/Luminite/Shimmerstone/Lumen Crystal, etc.). *(The
  Overworld Lumenbound Ruins cache keeps its iron/amethyst/gold — those teach you to build the portal.)*
- **Wood variants for Glowwood + Glowroot.** Each species now has a **bookshelf**, **ladder**, and **wooden
  post** (all glowing) — so Quark (which removes the vanilla recipes for its own per-wood variants but doesn't
  know about modded woods) no longer leaves you unable to craft them. Craftable directly; no mod dependency.
- **Prismfin — a catchable tropical fish.** A vivid, multi-hued schooling fish of the glowing water. Scoop one
  with a water bucket (a **Bucket of Prismfin**) and set up a tank for an aquarium. Spawns in the Moonmire and
  the surface seas.
- **Lumenwright remnants take precedence.** Vestige cities/outposts/spires, Glasspetal Spires, and Rootshrines
  now wipe their footprint before building, so they override whatever generated there first (trees growing
  through them, an overlapping structure) — natural terrain and caves underneath are left intact.

## [1.4.1] - 2026-06-21
- **Luminite armor.** Since Luminite is the iron-equivalent (same tools/stats), it now crafts into a full
  armor set (helmet/chestplate/leggings/boots) at iron-tier defense, enchantable and repairable with Luminite
  Ingots — the mid-game counterpart to the Resonite (diamond-tier) set.

## [1.4.0] - 2026-06-21
**Farming overhaul — a full alien farming tree.** (Also includes the climbable Glowvine from 1.3.2.)

- **Till the soil.** Hoe Moonloam or Lumen Grass into **Lumen Farmland**; shovel them into a **Lumen Dirt
  Path**. Farmland is hydrated by **Lumenwater** (which makes crops grow much faster) — dig irrigation.
- **Eight native crops**, each with an alien twist. Light barely matters (they grow in the dim twilight);
  mature crops glow:
  - **Lumengrain** (wheat → Glowloaf), **Glimmerroot** (carrot; the root is its own seed → Gilded
    Glimmerroot), **Moonbeet** (beetroot → Moonbeet Soup).
  - **Moonmelon** (a glowing melon → slices) and **Glowgourd** (a pumpkin you shear-carve into a glowing
    face).
  - **Glimmerreed** — a cane that grows beside Lumenwater (→ Lumen Sugar).
  - **Duskbean** — grows *in the dark* (an alien inversion).
  - **Cavecap** — grows on bare cave stone; farm the Undercrown, no soil needed.
- **Seeds two ways:** wild crop patches across the world, and seeds dropped by breaking Glow Fern.
- **Cooking, lumen-only and hybrid:** Glowloaf, Moonbeet Soup, Dusk Stew, plus lumen×overworld dishes — Gilded
  Glimmerroot, Wilds Pie (Glowgourd + Lumen Sugar + egg), Lumen Cookie (Lumengrain + cocoa). Everything is
  compostable and tagged into the universal `#c:` crop/seed/food tags, so cooking mods (Farmer's Delight,
  Create, Delightful) and auto-replant mods just work.
- An in-game **Farming guide entry** (Patchouli).

## [1.3.2] - 2026-06-21
- **Glowvine is now climbable** — climb the hanging cave strands and surface vines like ladders/vines
  (added to `#minecraft:climbable`).

## [1.3.1] - 2026-06-20
**Moonstone is now a proper Stone analog (and Deep Moonstone a Deepslate analog):**

- **Mining Moonstone now drops Cobbled Moonstone** (Silk Touch still gets the smooth Moonstone block), exactly
  like Stone → Cobblestone. Deep Moonstone likewise drops **Cobbled Deep Moonstone**. This also makes Cobbled
  Moonstone — and the Moonstone tool set that's crafted from it — properly obtainable by mining.
- **Smelting chain** (already in, now actually reachable): Cobbled Moonstone → Moonstone → Smooth Moonstone;
  Cobbled Deep Moonstone → Deep Moonstone.
- **Full crafting paths.** Every variant (cobbled, smooth, bricks, tiles, polished + their stairs/slabs/walls)
  is now craftable at a **crafting table** too, not just the stonecutter — each shape sourced from its own block
  (Cobbled → Cobbled Stairs, etc.), matching vanilla stone.

## [1.3.0] - 2026-06-20
**Mining overhaul (Phases A–E) — a complete rework of the Lumenwilds mining loop: reachable lively caves,
varied strata, new depth-banded ores, a deep gear tier (tools + the dimension's first armor), and cave
surprises (geodes, jackpot veins, landmarks):**

- **Caves now reach the mining band.** The dimension's terrain is tall (surface ~y70–170) but caves only
  carved below y52, so a mine dug into the upper stone sat entirely *above* the cave zone — solid rock all the
  way down. The cave zone now starts at ~**y90**, so straight mining actually breaks into caverns.
- **The deep is alive everywhere.** All biomes' deep caves now grow the Undercrown's character — wall
  crystals, hanging glowvine, glow ferns — plus scattered glowing **Lumenwater pools** (previously only the
  Undercrown biome had this; everywhere else was bare stone).
- **Strata variety (Phase B).** Two new accent rocks break up the old Moonstone→Deep-Moonstone monotony,
  generated as blobs through the stone: **Veinstone** (a violet granite-like rock in the Moonstone band) and
  **Pale Tuff** (a soft pale rock in the deep). Both are new building blocks too.
- **New ores + a depth curve (Phase C).** Three new ores, depth-banded so digging deeper pays off (and the
  old ores re-banded to match — Lumen Crystal shallow, Luminite mid):
  - **Emberglow** (common, mid depth) — the dimension's coal: smelt with it (a real furnace fuel), or store it
    as an Emberglow Block. Stone-tier to mine.
  - **Pale Opal** (uncommon, deep) — a pale decorative gem + storage block.
  - **Resonite** (rare, deepest) — raw → smelt to a Resonite Ingot → the new gear tier (coming next). Cold-glowing.
  All three also generate inside the new Veinstone/Pale Tuff rock. Iron-tier (Luminite pickaxe) for Opal + Resonite.
- **Resonite tools (Phase D1).** The deep's chase reward: a full **diamond-capable** tool set
  (pickaxe/axe/shovel/hoe/sword) smithed from Resonite Ingots — a touch faster than diamond, highly
  enchantable, the dimension's top tier.
- **Resonite armor (Phase D2) — the dimension's first armor.** A full set (helmet/chestplate/leggings/boots)
  crafted from Resonite Ingots: defense between iron and diamond, toughness 1.5, highly enchantable. (Worn-armor
  art is a placeholder ice-blue layer for now.)
- **Lumen Geodes (Phase E1).** Rare buried crystal pockets — a Deep-Moonstone/Shimmerstone shell around a
  hollow lined with **Lumen Crystal Block** and glowing **Budding Lumen Crystal** that slowly grows crystal
  buds → clusters. Mine the clusters for a renewable supply of Lumen Crystal Shards (Silk Touch keeps the
  crystals). A proper "wow, what's that glow?" cave find.
- **Jackpot veins (Phase E2).** Occasional big, rare deep deposits of Luminite and Resonite (copper-vein
  style) — the satisfying "hit the motherlode" moment that makes a deep dig pay off.
- **Cave landmarks (Phase E3).** Two more reasons to look around down there: glowing **Lumen Crystal Block
  pockets** embedded in the deep stone, and **Glowcap mushrooms** now growing on cave floors underground (not
  just in the jungle).
- *(This completes the v1.3 mining overhaul (Phases A–E). All amounts/rarity are tunable — feedback welcome.)*

## [1.2.2] - 2026-06-19
**Art + usability:**

- **Tech blocks now explain themselves.** The Lumenwright resonance/gravity blocks get a hover description
  so it's clear how to use them: Resonance Core, Lumen Conduit, Ancient Door, Lumen Relay, Dormant/Active
  Light Engine, Gravity Lens (+ cracked), Gravity Repeater, Memory Crystal, and the Lumen Anchor.
- **Art pass** (community contribution): Lumen Grass is now a proper grass block (green top, moonloam sides/
  bottom) instead of a flat cube, and the Glowroot logs got painted bark + end textures.
- **Infra:** published releases now auto-deploy to the family server (no gameplay impact).

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
