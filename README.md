# The Lumenwilds

An alien, bioluminescent **custom dimension** for Minecraft — reached through a portal you build and
ignite yourself. Dim twilight under a giant pale moon, blue-green glowing flora, low gravity, native
living light, seven biomes, ten native mobs, and the ruined cities of a vanished civilization (the
**Lumenwrights**) with functional crystal-resonance tech and gravity liftshafts.

> **Status: 1.0 — released.** Feature-complete: working portal + dimension, low-gravity movement, seven
> biomes over bespoke terrain, a full atmosphere (sky/particles/soundscape/day-cycle/events), complete
> building-block sets, ten native mobs + a ruin guardian, status effects / food / brewing, an advancement
> progression, the Vestige Cities + Resonance subsystem, and the Lumenwright liftshaft mines. See the
> [latest release](https://github.com/Jus144tice/lumenwilds/releases/latest) and [CHANGELOG.md](CHANGELOG.md).

## At a glance

| | |
| --- | --- |
| Minecraft version | 1.21.1 |
| Loader | NeoForge (21.1.233) |
| Java | 21 |
| Mod id | `lumenwilds` |
| Main package | `com.jus144tice.lumenwilds` |
| License | Apache-2.0 |

## Install

Download `lumenwilds-<version>.jar` from the
[releases page](https://github.com/Jus144tice/lumenwilds/releases/latest) and drop it into the `mods/`
folder of a **NeoForge 1.21.1 (21.1.x)** client or server running **Java 21**.

## Getting started — open the portal

1. Craft a **Lumen Striker** — vertical `I / A / G` (I = iron ingot, A = amethyst shard, G = glow ink sac).
2. Craft **Lumenbound Stone** (makes 4) — `C G C / S A S / C G C` (C = chiseled stone bricks, G = glowstone
   dust, S = smooth stone, A = amethyst shard). The glowstone makes this a **mid-game** gate.
3. Build a frame with a **2-wide × 3-tall opening** (like a small Nether portal, in Lumenbound Stone), then
   **right-click it with the Lumen Striker**. Step through the glowing portal into the Lumenwilds.

Prefer to discover it? **Lumenbound Ruins** generate in the Overworld as the in-world tutorial —
`/locate structure lumenwilds:lumenbound_ruins`.

> The frame is **Lumenbound Stone**, never vanilla lodestone (dropped as too expensive for this
> progression point). Destination: `lumenwilds:lumenwilds`.

## What's inside

- **Dimension & travel** — player-built portal, **Lumen Anchors** for precise return travel, low-gravity
  movement, a bespoke twilight **sky** under the giant moon *Veyra*, a half-rate day cycle, ambient events,
  particles, and a soundscape.
- **Seven biomes** — Lumen Glade, Glowroot Forest, Glasspetal Crags, Sporefall Jungle, Moonmire, the
  underground Undercrown Caverns, and the rare Stillbloom Basin — over alien cliffy terrain with glowing
  **Lumenwater** seas and deep noise-caves, full of signature flora and worldgen.
- **Ten native mobs** + the ruin-guardian **Echo Sentinel**, each with bespoke models and emissive glow.
- **Building sets** — Glowwood, Moonstone, Deep Moonstone, Shimmerstone, Sporeglass, Lumen Crystal,
  Luminite, and the luminous **Glowbrick** family — plus status effects + brewing, foods, and advancements.
- **The Lumenwrights / Vestige Cities** — rare ruined alien cities (Outpost → Medium → Grand) with a
  functional **Resonance** power subsystem (cores, conduits, ancient doors, gravity lenses, restorable Light
  Engines), Memory-Crystal lore, and a buried Vault.
- **Lumenwright Liftshafts** — craftable gravity-elevator tech (Lumen Field Projector + Gravity Repeaters)
  and **Abandoned Luminite Mines** with working liftshafts built from real, reverse-engineerable components.
  Find the nearest mine-bearing city with `/locate structure lumenwilds:vestige_mine`.

## Building from source

JDK 21 required (`JAVA_HOME` pointing at it).

```bash
# Windows (PowerShell): $env:JAVA_HOME = 'C:\Program Files\Java\jdk-21'
./gradlew build            # spotlessApply -> compile -> tests -> jar (build/libs/lumenwilds-<ver>.jar)
./gradlew runClient        # dev client with the mod loaded
./gradlew runServer        # dev server
```

Contributor/AI navigation map: [CLAUDE.md](CLAUDE.md). Roadmap & history:
[docs/IMPLEMENTATION_PLAN.md](docs/IMPLEMENTATION_PLAN.md), [CHANGELOG.md](CHANGELOG.md). Design source of
truth: [docs/LUMENWILDS_WORLD_DEFINITION.md](docs/LUMENWILDS_WORLD_DEFINITION.md).

## License

[Apache License 2.0](LICENSE).
