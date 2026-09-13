// Goes in: src/main/java/com/acentra/pmotools/PmoToolsMod.java
package com.acentra.pmotools;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PmoToolsMod implements ModInitializer {
    public static final String MOD_ID = "pmotools";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("PmoTools initialized (common side).");
    }
}
