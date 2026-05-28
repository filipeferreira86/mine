package com.pendragon.mod;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

/**
 * Mod entrypoint. Fase 0 skeleton: registers no content, only logs lifecycle
 * to prove the loader picked us up. Subsequent fases attach registries,
 * payload handlers, and game logic via dedicated classes under sibling
 * packages (core, dnd, ai, tts, client).
 */
@Mod(Pendragon.MOD_ID)
public final class Pendragon {

    public static final String MOD_ID = "pendragon";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Pendragon(IEventBus modBus) {
        LOGGER.info("Pendragon: 5e Adventures — booting (Fase 0 skeleton)");
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Pendragon commonSetup done");
    }
}
