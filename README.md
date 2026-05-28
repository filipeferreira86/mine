# Pendragon: 5e Adventures

> AI-narrated 5e tabletop adventures inside Minecraft.

[![build](https://github.com/filipeferreira86/mine/actions/workflows/build.yml/badge.svg)](https://github.com/filipeferreira86/mine/actions/workflows/build.yml)

> **Status — Fase 0 (foundation).** The mod loads in 1.21.1 and lists in the
> Mods screen, but does nothing yet. Roadmap below.

## What it will be

A Minecraft 1.21.1 mod (NeoForge) that lets you and your friends play
5e-style adventures with an AI Dungeon Master. The DM narrates by voice
and spawns mobs, structures, and loot to drive the story. Players reply
through the in-game chat. Solo or multiplayer.

## Roadmap

| Fase | Topic |
|---|---|
| 0 | **Foundation** — repo, toolchain, mod skeleton. *(current)* |
| 1 | Core 5e rules — character sheet, races, classes, backgrounds, combat. |
| 2 | Bestiary — hybrid of upstream mob mods + ~20 D&D-exclusive creatures. |
| 3 | AI DM bridge — DeepSeek / Qwen / Claude / Gemini + function calling. |
| 4 | TTS — voiced narration. |
| 5 | CurseForge modpack packaging. |

## Build and run

See [`docs/dev-setup.md`](docs/dev-setup.md).

Short version:

```bash
git clone https://github.com/filipeferreira86/mine.git pendragon
cd pendragon
./gradlew runClient
```

JDK 21 required. First build downloads the NeoForge SDK (~1 GB).

## Licensing

- **Project code:** MIT — see [`LICENSE`](LICENSE).
- **D&D content:** drawn solely from the System Reference Document 5.1
  ([SRD 5.1](https://dnd.wizards.com/resources/systems-reference-document))
  by Wizards of the Coast LLC, used under
  [CC-BY 4.0](https://creativecommons.org/licenses/by/4.0/legalcode).
  Full attribution and scope: [`docs/srd-attribution.md`](docs/srd-attribution.md).

"Dungeons & Dragons" and "D&D" are trademarks of Wizards of the Coast LLC.
Pendragon is not affiliated with or endorsed by Wizards of the Coast.

## Contributing

Specs live in [`docs/superpowers/specs/`](docs/superpowers/specs/); plans in
[`docs/superpowers/plans/`](docs/superpowers/plans/). Each Fase has its own
spec → plan → implementation cycle. Open issues for proposals before
starting work.
