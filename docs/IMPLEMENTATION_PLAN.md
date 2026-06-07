# The Lumenwilds — Implementation Plan

A phased roadmap. **Phase 1 is the current scaffolding pass** — the repo compiles, loads, and is
organized for everything below. Later phases are deliberately not implemented yet.

## Phase 1 — Scaffolding (current)
- Project scaffolding (NeoForge 1.21.1, Java 21, Gradle).
- Registries (`registry/Mod*`).
- Placeholder blocks/items.
- Creative tab ("The Lumenwilds").
- Lumenbound Stone frame block.
- Lumen Striker item.
- Minimal dimension key setup (`world.LumenDimensionConstants` + placeholder dimension JSON).

## Phase 2 — Dimension entry & portal
- Working custom dimension entry (player can actually travel to `lumenwilds:lumenwilds`).
- Basic terrain generation.
- Basic biome source.
- Portal activation with a Lumenbound Stone frame:
  - frame detection (`portal/LumenPortalShape`),
  - fill interior with `lumen_portal` (`portal/LumenPortalManager`),
  - teleport + return-portal search/creation (`portal/LumenPortalTeleporter`),
  - entity collision teleport in `portal/LumenPortalBlock#entityInside`.

## Phase 3 — World content
- Biomes (`world/LumenBiomeBootstrap`).
- Surface blocks (moonloam / lumen grass).
- Trees/plants (glowwood, glowroot, glowvine, moonblossom) via configured/placed features.
- Lumenwater fluid (`registry/ModFluids`).
- Lighting blocks (lumenbulb, native living light sources).

## Phase 4 — Atmosphere & movement
- Low gravity (`effects/LowGravityHandler`) — higher jumps, slower fall, reduced fall damage, and
  later projectile-arc / Elytra tuning. Preferred approach: the vanilla `minecraft:generic.gravity`
  attribute via a transient modifier added on dimension entry and removed on exit.
- Ambient effects + particles (`registry/ModParticles`).
- Weather / sporefall events.
- Custom sky/fog rendering (custom dimension `effects`).

## Phase 5 — Living world
- Mobs (`registry/ModEntities`).
- Structures.
- Loot.
- Progression.

## Phase 6 — Polish
- Polish pass.
- Full datagen coverage (`datagen/`).
- JEI compatibility.
- Config options.
- Balancing.

---

## Design notes

**Portal frame material.** The original portal-frame idea used vanilla lodestones, but this was
replaced with **Lumenbound Stone** because lodestones are too expensive for the intended progression.
Lumenbound Stone is crafted from Overworld stonework + amethyst resonance + Nether light (glowstone),
which is mid-game but not netherite-expensive.

**Portal helper libraries.** We may evaluate a third-party custom-portal helper library later. For now
we intentionally add **no** such dependency and keep portal logic in-house under `portal/`.
