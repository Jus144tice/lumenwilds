# The Lumenwilds World Definition

This file is the design source of truth for the dimension.

The full world bible **has been provided** and currently lives verbatim at
[world_description.txt](world_description.txt). Treat that document as canonical for design intent
(biomes, blocks, mobs, structures, effects, lore, progression). This file is the curated index +
the running list of where the **implementation intentionally diverges** from the bible.

> Note: the bible is preserved exactly as supplied. As designs are confirmed, fold sections into
> structured Markdown here and keep `world_description.txt` as the original reference.

## ⚠️ Design deltas — scaffolding vs. the bible

The Phase 1 scaffolding follows the **build instructions**, which deliberately supersede parts of the
bible.

| Topic | Bible (now) | Implemented (this repo) | Status |
| --- | --- | --- | --- |
| **Portal frame** | **Lumenbound Stone** (bible updated) | **Lumenbound Stone** (`lumenwilds:lumenbound_stone`) | ✅ Reconciled. Lodestone was netherite-gated — too expensive for a mid-game dimension. All stale lodestone references in the bible were updated (the *Lumenbound Ruins* structure and *Lumen Anchor* recipe were renamed/retooled to match; the Anchor keeps its Echo Shard). |
| **Lumen Striker recipe** | **amethyst shard + iron ingot + glow ink sac** (bible updated) | iron ingot + amethyst shard + glow ink sac (vertical `I/A/G`) | ✅ Reconciled. The echo shard was dropped so the striker isn't Deep-Dark-locked. |

Everything else in the bible stands. Lodestone is no longer referenced anywhere in the design (the
portal frame, ruins lore, and anchor were all moved to Lumenbound Stone), and the Lumen Striker no
longer requires an echo shard.

## What Phase 1 already scaffolds toward the bible

Placeholder blocks/items registered now map directly onto bible content (final behaviour is TODO):
moonloam, lumen grass block, moonstone + cobbled moonstone, glowwood log/planks, glowroot log,
glowvine, moonblossom, lumenbulb, lumen crystal block; lumen striker, lumen crystal shard, glow pollen,
living fiber, lumen fruit, lumen nectar, air gel. The dimension key is `lumenwilds:lumenwilds`.

Not yet started (later phases per [IMPLEMENTATION_PLAN.md](IMPLEMENTATION_PLAN.md)): the 7 biomes,
deep moonstone / shimmerstone / sporeglass / glowcap / stillbloom sets, lumenwater fluid, the full
mob roster, structures, brewing effects (Lightfoot/Glowmarked/Sporeblind/Rooted), weather (Sporefall/
Moonwake/Deep Hush), custom sky/fog, sound design, and the low-gravity movement rules.
