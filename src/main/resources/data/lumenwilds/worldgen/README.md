# Lumenwilds worldgen data (placeholder home)

This folder is the future home for the dimension's datapack worldgen JSON. Nothing here is wired up
yet — the dimension currently reuses vanilla overworld terrain + a fixed plains biome (see
`../dimension/lumenwilds.json`).

Expected subfolders (1.21.1, all singular except `tags`):

- `biome/` — one file per Lumenwilds biome (keys in `world.LumenBiomeBootstrap`).
- `configured_feature/` — the "what to place" (keys in `world.LumenConfiguredFeatures`).
- `placed_feature/` — the "where/how often" (keys in `world.LumenPlacedFeatures`).
- `noise_settings/` — optional custom terrain shaping (only if we move off `minecraft:overworld`).

See `docs/IMPLEMENTATION_PLAN.md` (Phases 2–3) for the order of work.
