# The Lumenwilds

An alien, bioluminescent custom dimension for Minecraft — reached through a portal you build and
ignite yourself. Blue grass, dim twilight under bright moonlight, glowing plants, low gravity, and
native living light sources (planned).

> **Status: scaffolding (Phase 1).** The project compiles and loads, with placeholder blocks/items, a
> creative tab, the portal frame block + igniter, and the dimension keys in place. Worldgen, mobs,
> structures, textures and full portal behaviour are intentionally placeholders. See
> [docs/IMPLEMENTATION_PLAN.md](docs/IMPLEMENTATION_PLAN.md).

## At a glance

| | |
| --- | --- |
| Minecraft version | 1.21.1 |
| Loader | NeoForge (21.1.233) |
| Java | 21 |
| Build | Gradle (wrapper, ModDevGradle) |
| Mod id | `lumenwilds` |
| Main package | `com.jus144tice.lumenwilds` |

## Portal concept

- **Frame:** Lumenbound Stone (`lumenwilds:lumenbound_stone`) — *not* vanilla lodestone.
- **Ignition:** Lumen Striker (`lumenwilds:lumen_striker`).
- **Interior:** Lumen Portal (`lumenwilds:lumen_portal`).
- **Destination:** The Lumenwilds (`lumenwilds:lumenwilds`).

Lumenbound Stone is crafted from Overworld stonework + amethyst resonance + Nether light — mid-game,
not netherite-expensive. (Lodestone was dropped from the original design as too expensive for this
progression point.)

### Recipes

- **Lumenbound Stone** (makes 4): `C G C / S A S / C G C` — C = chiseled stone bricks, G = glowstone
  dust, S = smooth stone, A = amethyst shard.
- **Lumen Striker** (makes 1): vertical `I / A / G` — I = iron ingot, A = amethyst shard, G = glow ink
  sac.

## Build

JDK 21 must be installed (`JAVA_HOME` pointing at it).

```bash
# Windows (PowerShell): $env:JAVA_HOME = 'C:\Program Files\Java\jdk-21'
./gradlew build            # spotlessApply -> compile -> tests -> jar (build/libs/lumenwilds-<ver>.jar)
./gradlew spotlessApply    # auto-format ("prettier for Java"); runs automatically before compileJava
```

## Run a dev client

```bash
./gradlew runClient        # launches a dev Minecraft client with the mod loaded
./gradlew runServer        # dev server
./gradlew runData          # (optional) regenerate placeholder assets into src/generated/resources
```

In the client you should see the **The Lumenwilds** creative tab containing every placeholder block
and item; crafting the Lumenbound Stone + Lumen Striker; and a log line when the Lumen Striker is used
on Lumenbound Stone ("portal activation attempted").

## Roadmap (summary)

1. **Phase 1 — Scaffolding** (current): registries, placeholder content, creative tab, frame block +
   striker, dimension keys.
2. **Phase 2** — working dimension entry, basic terrain/biome, real portal activation + teleport.
3. **Phase 3** — biomes, surface blocks, trees/plants, Lumenwater, lighting blocks.
4. **Phase 4** — low gravity, ambient effects, weather/sporefall, custom sky/fog.
5. **Phase 5** — mobs, structures, loot, progression.
6. **Phase 6** — polish, full datagen, JEI compat, config, balancing.

Full detail in [docs/IMPLEMENTATION_PLAN.md](docs/IMPLEMENTATION_PLAN.md). The dimension's design
source of truth lives in [docs/LUMENWILDS_WORLD_DEFINITION.md](docs/LUMENWILDS_WORLD_DEFINITION.md)
(to be filled in).

## ⚠️ Placeholders

Textures are flat-colour placeholders; models are simple cubes; the dimension reuses vanilla terrain;
the portal does not teleport yet; there are no mobs/structures/worldgen. None of this is final — it is
scaffolding to build on.

## License

[Apache License 2.0](LICENSE).
