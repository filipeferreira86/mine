# Dev Setup

Build, run, and iterate on Pendragon locally.

## Prerequisites

- **JDK 21** (any distribution: Temurin, Zulu, Oracle). The Gradle toolchain
  will attempt to auto-provision via Foojay if missing, but installing it
  yourself is faster.
- **Git**.
- **8 GB free RAM** for IDE + dev client.

No system Gradle is required — use the checked-in `./gradlew` wrapper.

## Clone and build

```bash
git clone <repo-url> pendragon
cd pendragon
./gradlew build
```

First build downloads the NeoForge SDK (~1 GB) and generates mappings —
allow 5–15 minutes. Subsequent builds are cached.

The mod JAR lands at `build/libs/pendragon-<version>.jar`.

## Run the dev client

```bash
./gradlew runClient
```

Minecraft 1.21.1 opens with Pendragon pre-loaded. Worlds and screenshots
persist under `runs/client/`. First launch downloads ~800 MB of MC assets
(sounds, textures); subsequent launches are quick.

## Run the dev server

```bash
./gradlew runServer
```

NeoForge's dev `runServer` task launches a dedicated server with the mod
loaded. EULA is bypassed in dev mode — no `eula.txt` step. The server uses
`run/` (not `runs/server/`) as its game directory. Type `stop` in the
console (if running interactively) or kill the process to shut down.

## IDE setup

**IntelliJ IDEA:** open the project (root `build.gradle`) → trust →
Gradle import. Run configs `runClient`, `runServer`, `runData` appear under
Run/Debug Configurations.

**VS Code:** install the *Extension Pack for Java* and *Gradle for Java*
extensions. Run via the Gradle tasks panel.

## Common pitfalls

- **"Unsupported class file major version 65"** — wrong JDK on PATH. Use
  JDK 21.
- **First build hangs at "Resolving NeoForge"** — large download; check your
  network. Behind a proxy: configure `~/.gradle/gradle.properties` with
  `systemProp.https.proxyHost` and `systemProp.https.proxyPort`.
- **`run/` / `runs/` deleted by accident** — safe; regenerated on next run.
  Worlds there are not in git.
