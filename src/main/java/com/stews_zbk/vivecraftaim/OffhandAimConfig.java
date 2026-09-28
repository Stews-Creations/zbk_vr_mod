package com.stews_zbk.vivecraftaim;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Owns persisted client preferences and validated controller selection shared by the mixins. */
public final class OffhandAimConfig {
    private static final String FILE_NAME = "zbk-vivecraft-offhand-aim.properties";
    private static final int DEFAULT_CONTROLLER_INDEX = 1;
    private static final int DEFAULT_HUD_CONTROLLER_INDEX = 0;

    private static boolean loaded;
    private static boolean enabled = true;
    private static int controllerIndex = DEFAULT_CONTROLLER_INDEX;
    private static int hudControllerIndex = DEFAULT_HUD_CONTROLLER_INDEX;

    private OffhandAimConfig() {
    }

    public static boolean enabled() {
        ensureLoaded();
        return enabled;
    }

    public static int controllerIndex() {
        ensureLoaded();
        return controllerIndex;
    }

    public static int hudControllerIndex() {
        ensureLoaded();
        return hudControllerIndex;
    }

    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
        Properties properties = new Properties();
        properties.setProperty("enabled", Boolean.toString(enabled));
        properties.setProperty("controller_index", Integer.toString(controllerIndex));
        properties.setProperty("hud_controller_index", Integer.toString(hudControllerIndex));

        try {
            if (Files.exists(path)) {
                try (InputStream input = Files.newInputStream(path)) {
                    properties.load(input);
                }
            } else {
                Files.createDirectories(path.getParent());
                try (OutputStream output = Files.newOutputStream(path)) {
                    properties.store(output, "ZBK Vivecraft Offhand Aim");
                }
            }

            enabled = Boolean.parseBoolean(properties.getProperty("enabled", "true").trim());
            controllerIndex = parseControllerIndex(
                properties.getProperty("controller_index"),
                DEFAULT_CONTROLLER_INDEX);
            hudControllerIndex = parseControllerIndex(
                properties.getProperty("hud_controller_index"),
                DEFAULT_HUD_CONTROLLER_INDEX);
            loaded = true;
        } catch (IOException exception) {
            loaded = true;
            ZbkVivecraftOffhandAimClient.LOGGER.warn(
                "Could not load config {}; using defaults",
                path,
                exception);
        }
    }

    private static int parseControllerIndex(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed <= 0 ? 0 : 1;
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    private static void ensureLoaded() {
        if (!loaded) {
            load();
        }
    }
}
