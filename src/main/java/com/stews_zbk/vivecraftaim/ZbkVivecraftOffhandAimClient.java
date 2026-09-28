package com.stews_zbk.vivecraftaim;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Loads client configuration before the controller hooks consume it. Server gameplay remains in the datapack. */
public class ZbkVivecraftOffhandAimClient implements ClientModInitializer {
    public static final String MOD_ID = "zbk_vivecraft_offhand_aim";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        OffhandAimConfig.load();
        LOGGER.info(
            "ZBK Vivecraft Offhand Aim loaded: enabled={}, controller_index={}, hud_controller_index={}",
            OffhandAimConfig.enabled(),
            OffhandAimConfig.controllerIndex(),
            OffhandAimConfig.hudControllerIndex());
    }
}
