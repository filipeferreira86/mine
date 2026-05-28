# Pendragon — Fase 0: Fundação Técnica

**Status:** Design aprovado, pronto para planejamento de implementação
**Data:** 2026-05-29
**Escopo da fase:** Fundação técnica do mod (repo, toolchain, skeleton). Zero conteúdo D&D, zero IA, zero gameplay.
**Mod ID:** `pendragon`
**Display name:** `Pendragon: 5e Adventures`

---

## 1. Contexto e decomposição

O projeto-mãe é um modpack Minecraft que combina:

1. Regras D&D 5e (raças, classes, backgrounds, combate d20)
2. Bestiário D&D 5e
3. UI de criação de personagem na geração do mundo
4. Mestre de Jogo (DM) controlado por IA (DeepSeek/Qwen primários; Claude/Gemini opcionais via chave do usuário)
5. Narração por voz (TTS) do DM
6. Comunicação jogador → DM via chat do Minecraft
7. Function calling do DM para spawnar mobs, estruturas, itens, mudar clima, etc.
8. Suporte single-player e multiplayer
9. Empacotamento como modpack no CurseForge

Por ser grande demais para um único spec, decompomos em fases. Cada fase tem seu próprio spec → plano → ciclo de implementação.

| Fase | Foco | Status |
|---|---|---|
| **0** | **Fundação técnica (este documento)** | **Em design** |
| 1 | Mod núcleo D&D (ficha, criação de personagem, regras básicas) — sem IA | Não iniciado |
| 2 | Bestiário (subset SRD + criaturas próprias via estratégia híbrida) | Não iniciado |
| 3 | Ponte IA (provider abstraction, function calling, contexto de sessão) | Não iniciado |
| 4 | TTS (engine de voz, streaming de áudio) | Não iniciado |
| 5 | Empacotamento CurseForge | Não iniciado |

---

## 2. Stack técnico (decisões travadas)

| Camada | Escolha | Justificativa |
|---|---|---|
| Minecraft | 1.21.1 | LTS-de-facto NeoForge atual; Geckolib e libs prontas |
| Mod loader | NeoForge 1.21.1 (último patch estável da branch 21.1.x) | Fork moderno do Forge; ecossistema grande |
| Linguagem | Java 21 | Padrão NeoForge 1.21.x; zero atrito |
| Build | Gradle 8.x + NeoGradle 7.x | Padrão NeoForge |
| Mappings | Mojang official + Parchment (param names) | Melhor legibilidade |
| Estrutura | Single-module | Simples para Fases 0–2; refatora se virar lib reutilizável |
| Mod ID | `pendragon` | CC-BY safe, sem trademark D&D |
| Licença do código | MIT | Compatível com SRD CC-BY 4.0 |
| Atribuição SRD | `docs/srd-attribution.md` + nota no README | Requisito CC-BY 4.0 |
| Estratégia de conteúdo (Fase 2) | Híbrida: deps soft + ~20 criaturas próprias | Decidida; sem impacto na Fase 0 |

---

## 3. Escopo da Fase 0

### 3.1 Entregáveis

- Repo git com `.gitignore`, `LICENSE` (MIT), `README.md`, `CHANGELOG.md`
- Projeto Gradle NeoForge 1.21.1 buildando do zero (`./gradlew build` produz JAR carregável)
- Tasks `runClient` e `runServer` funcionando
- Mod entrypoint `Pendragon.java` registrado com `@Mod("pendragon")`, logando "Pendragon initialized" no startup
- `neoforge.mods.toml` com metadata correto
- CI placeholder (GitHub Actions: `./gradlew build` em push/PR)
- `docs/srd-attribution.md` declarando uso de SRD 5.1 CC-BY 4.0
- `docs/dev-setup.md` com passos para clonar/buildar/rodar dev

### 3.2 Fora de escopo (vai para fases seguintes)

- Qualquer registry custom (items, blocks, entities) — só skeleton vazio
- Qualquer regra D&D
- Qualquer UI
- Qualquer integração IA / TTS
- Qualquer conteúdo SRD propriamente dito
- Suporte multiplayer validado além de "servidor sobe e cliente conecta"
- Deploy automatizado para CurseForge
- Testes unitários (sem lógica de domínio ainda; testes começam Fase 1)

### 3.3 Critérios de "Fase 0 done" (auditáveis)

1. `./gradlew build` passa limpo
2. `./gradlew runClient` abre MC 1.21.1 com Pendragon na lista de mods
3. `./gradlew runServer` sobe servidor dedicado e cliente conecta com mod carregado
4. Log mostra a string de inicialização do mod
5. CI no GitHub Actions passa em PR de teste
6. `docs/srd-attribution.md` e `docs/dev-setup.md` populados
7. `README.md` cobre: o que é, como buildar, como rodar dev, atribuição SRD, license — em ≤80 linhas
8. Tag git `v0.0.1` empurrada

---

## 4. Estrutura do repositório

```
pendragon/
├── .github/
│   └── workflows/
│       └── build.yml                  # CI: ./gradlew build em push/PR
├── .gitignore
├── LICENSE                            # MIT (nosso código)
├── README.md
├── CHANGELOG.md
├── gradle.properties
├── settings.gradle
├── build.gradle
├── gradlew, gradlew.bat, gradle/      # wrapper
├── docs/
│   ├── srd-attribution.md             # CC-BY 4.0 attribution + escopo de uso
│   ├── dev-setup.md                   # passos clonar/buildar/rodar
│   └── superpowers/specs/             # design docs vivem aqui
└── src/
    └── main/
        ├── java/
        │   └── com/pendragon/mod/
        │       ├── Pendragon.java     # entrypoint @Mod("pendragon")
        │       ├── PendragonConfig.java   # placeholder
        │       ├── core/              # vazio — base utils
        │       ├── dnd/               # vazio — rules engine
        │       ├── ai/                # vazio — DM bridge
        │       ├── tts/               # vazio — voz
        │       └── client/            # vazio — UI/render
        └── resources/
            ├── META-INF/
            │   └── neoforge.mods.toml
            ├── pack.mcmeta
            ├── assets/pendragon/
            │   └── lang/en_us.json
            └── data/pendragon/
```

**Notas:**

- Pacotes `core/dnd/ai/tts/client` ficam vazios na Fase 0, ancorados por `package-info.java` que documenta a intenção de cada um. Cada um vira foco de uma fase futura.
- `client/` é separado para permitir `@OnlyIn(Dist.CLIENT)` sem vazar render no servidor.
- `data/` e `assets/` sob namespace `pendragon` para ResourceLocations consistentes.

---

## 5. Build e toolchain dev

### 5.1 `gradle.properties` (chaves principais)

```properties
org.gradle.jvmargs=-Xmx3G
org.gradle.daemon=true

minecraft_version=1.21.1
neo_form_version=1.21.1-20240808.144430
neoforge_version=21.1.x   # patch fixado na geração final
parchment_minecraft=1.21
parchment_version=2024.07.28

mod_id=pendragon
mod_name=Pendragon: 5e Adventures
mod_license=MIT
mod_version=0.0.1
mod_group_id=com.pendragon.mod
mod_authors=<a definir>
mod_description=AI-driven 5e tabletop adventures in Minecraft.
```

> Versões exatas (especialmente NeoForge patch) são validadas no momento da geração; o canal/branch `1.21.1` é o que está travado.

### 5.2 `build.gradle` essencial

- Plugin `net.neoforged.gradle.userdev` (NeoGradle 7)
- Plugin `org.parchmentmc.librarian.forgegradle` (parchment mappings)
- Java toolchain 21
- Runs: `client`, `server`, `data` (datagen) — todos com working dir `runs/<name>`
- Repos: NeoForged Maven, ParchmentMC Maven, Mojang
- Dependencies da Fase 0: apenas `neoforge` userdev — nenhum mod externo ainda

### 5.3 `neoforge.mods.toml`

```toml
modLoader="javafml"
loaderVersion="[4,)"
license="MIT"
issueTrackerURL="<TBD>"

[[mods]]
modId="pendragon"
version="${file.jarVersion}"
displayName="Pendragon: 5e Adventures"
authors="<TBD>"
description='''
AI-driven 5e tabletop adventures in Minecraft.
Uses SRD 5.1 content under CC-BY 4.0.
'''

[[dependencies.pendragon]]
modId="neoforge"
type="required"
versionRange="[21.1,)"
ordering="NONE"
side="BOTH"

[[dependencies.pendragon]]
modId="minecraft"
type="required"
versionRange="[1.21.1,1.22)"
ordering="NONE"
side="BOTH"
```

### 5.4 Dev workflow (documentado em `docs/dev-setup.md`)

1. Clone do repo
2. JDK 21 instalado (toolchain Gradle baixa se ausente)
3. `./gradlew genIntellijRuns` (ou `genEclipseRuns`)
4. Abrir IDE → importar Gradle
5. Run config `runClient` → MC abre com o mod
6. Run config `runServer` → aceitar EULA em `runs/server/eula.txt`

### 5.5 CI

`.github/workflows/build.yml` dispara em push e PR. Setup JDK 21, cache Gradle, `./gradlew build`. Falha bloqueia merge. Sem deploy automático na Fase 0.

---

## 6. Mod skeleton (código mínimo)

### 6.1 `Pendragon.java` — entrypoint único da Fase 0

```java
package com.pendragon.mod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(Pendragon.MOD_ID)
public final class Pendragon {
    public static final String MOD_ID = "pendragon";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Pendragon(IEventBus modBus) {
        LOGGER.info("Pendragon: 5e Adventures — booting (Fase 0 skeleton)");
        modBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Pendragon commonSetup done");
    }
}
```

### 6.2 `PendragonConfig.java`

```java
package com.pendragon.mod;

public final class PendragonConfig {
    private PendragonConfig() {}
    // Fase 3+ popula com chaves IA, TTS, etc.
}
```

### 6.3 `package-info.java` em cada pacote vazio

Exemplo para `ai/`:

```java
/**
 * AI Dungeon Master bridge (provider abstraction, prompt assembly,
 * function-call tool dispatch). Populated from Fase 3 onward.
 */
package com.pendragon.mod.ai;
```

Mesmo padrão para `core`, `dnd`, `tts`, `client`.

### 6.4 `assets/pendragon/lang/en_us.json`

```json
{
  "pendragon.mod.name": "Pendragon: 5e Adventures"
}
```

### 6.5 Sem registries na Fase 0

Nenhum `DeferredRegister` para items/blocks/entities. Skeleton fica zerado de propósito — força fases seguintes a justificarem cada registro novo.

---

## 7. Posicionamento legal SRD

O projeto trabalha sob o System Reference Document 5.1 (SRD 5.1), licenciado em CC-BY 4.0 pela Wizards of the Coast.

### 7.1 Permissões garantidas pelo SRD 5.1

- Mecânicas: d20, vantagem/desvantagem, ability scores, saving throws, attack rolls, proficiency, AC, HP, exhaustion, conditions
- Raças SRD: Human, Dwarf (Hill/Mountain), Elf (High/Wood/Dark), Halfling (Lightfoot/Stout), Dragonborn, Gnome (Forest/Rock), Half-Elf, Half-Orc, Tiefling
- Classes SRD: Barbarian, Bard, Cleric, Druid, Fighter, Monk, Paladin, Ranger, Rogue, Sorcerer, Warlock, Wizard (subset SRD de subclasses)
- Backgrounds: apenas Acolyte é SRD completo; outros backgrounds serão originais nossos
- Criaturas SRD: ~300+ incluindo goblin, owlbear, beholder, bulette, displacer beast, gnoll, kobold, lich, mimic, rust monster, etc.
- Spells SRD: maioria dos spells core

### 7.2 Proibições

- Nomes trademark: "Dungeons & Dragons", "D&D", "Forgotten Realms"
- Termos restritos: validar lista antes da Fase 2 (alguns nomes de monstros têm proteção marcária separada do conteúdo SRD; usar termos genéricos onde houver dúvida)
- Settings proprietários (Faerûn, Eberron, Ravnica, etc.)
- Mecânicas pós-2014 que não estão no SRD 5.1 (Tasha's, Xanathar's, etc.)
- Lore proprietário

### 7.3 Cumprimento da CC-BY 4.0

`docs/srd-attribution.md` (texto final validado na escrita):

```
This work includes material taken from the System Reference Document 5.1
("SRD 5.1") by Wizards of the Coast LLC and available at
https://dnd.wizards.com/resources/systems-reference-document.

The SRD 5.1 is licensed under the Creative Commons Attribution 4.0
International License (https://creativecommons.org/licenses/by/4.0/legalcode).
```

Mais: menção no `README.md` (1 parágrafo) e splash in-game na tela de criação de personagem (entrega na Fase 1).

### 7.4 Posicionamento de marketing

Termos aceitáveis: "5e-compatible", "tabletop adventures", "fantasy TTRPG rules".
Nunca usar "Dungeons & Dragons" no listing CurseForge ou material promocional.

---

## 8. Riscos e mitigações

| ID | Risco | Mitigação |
|---|---|---|
| R1 | Drift de versão NeoForge (patches frequentes) | Pin completo da versão patch em `gradle.properties`; Renovate config futuro |
| R2 | Lag do Parchment para MC novo | Fallback para Mojang mappings puro (já é base) |
| R3 | JDK 21 ausente em devs | `dev-setup.md` aponta toolchain Gradle (auto-baixa); nota explícita |
| R4 | Interpretação errada de trademark/SRD | Lista "OK / não OK" em `docs/srd-attribution.md` revisada antes da Fase 2 |

---

## 9. Handoff para Fase 1

Quando Fase 0 estiver "done", Fase 1 (mod núcleo D&D) começa com:

- Repo e skeleton funcionais
- Pacotes `dnd/` e `client/` prontos para receber código
- Decisões travadas: NeoForge 1.21.1, Java 21, single-module, mod id `pendragon`, SRD-only

Fase 1 vai precisar de brainstorm próprio sobre:

- Modelo de dados da ficha de personagem (sync cliente↔servidor via Capability ou `AttachmentType`)
- UI de criação de personagem (vanilla `Screen` vs lib como Cloth Config Menus)
- Sistema de regras de combate (interceptar `LivingDamageEvent` vs sistema paralelo)
- Persistência da ficha (NBT do jogador, save por-mundo)

---

## 10. Glossário

- **NeoForge** — fork moderno do Minecraft Forge usado como mod loader.
- **NeoGradle** — plugin Gradle oficial do NeoForge para configurar o ambiente de desenvolvimento.
- **Parchment** — provê nomes de parâmetros legíveis em cima dos Mojang mappings.
- **SRD 5.1** — System Reference Document 5.1; subset oficialmente liberado das regras de D&D 5e em CC-BY 4.0.
- **CC-BY 4.0** — Creative Commons Attribution 4.0 International; exige atribuição mas permite uso, modificação e redistribuição comercial.
- **`mods.toml`** — descritor de metadata do mod no NeoForge (caminho: `META-INF/neoforge.mods.toml`).
- **`@OnlyIn(Dist.CLIENT)`** — anotação NeoForge que marca código que só existe no cliente (não no servidor dedicado).
- **DM** — Dungeon Master; neste projeto, a IA narradora.
- **Capability / AttachmentType** — mecanismos NeoForge para anexar dados customizados a entidades, BlockEntities ou ItemStacks; usados em Fase 1 para guardar a ficha do jogador.
