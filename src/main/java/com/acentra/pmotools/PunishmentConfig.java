// Goes in: src/main/java/com/acentra/pmotools/PunishmentConfig.java
package com.acentra.pmotools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PunishmentConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
        FabricLoader.getInstance().getConfigDir().resolve("pmotools").resolve("punishments.json");

    private static List<PunishmentType> cache;

    public static List<PunishmentType> load() {
        if (cache != null) return cache;
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH, StandardCharsets.UTF_8);
                Type listType = new TypeToken<ArrayList<PunishmentType>>() {}.getType();
                cache = GSON.fromJson(json, listType);
                if (cache != null) return cache;
            }
        } catch (IOException e) {
            PmoToolsMod.LOGGER.error("Failed to read punishments.json, using defaults", e);
        }
        cache = defaults();
        save(cache);
        return cache;
    }

    public static void save(List<PunishmentType> types) {
        cache = types;
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(types), StandardCharsets.UTF_8);
        } catch (IOException e) {
            PmoToolsMod.LOGGER.error("Failed to write punishments.json", e);
        }
    }

    public static void addType(PunishmentType type) {
        List<PunishmentType> list = load();
        list.add(type);
        save(list);
    }

    // Add a reason to an existing type by name, if not already present.
    public static void addReason(String typeName, String reason) {
        List<PunishmentType> list = load();
        for (PunishmentType t : list) {
            if (t.name.equalsIgnoreCase(typeName) && !t.reasons.contains(reason)) {
                t.reasons.add(reason);
                save(list);
                return;
            }
        }
    }

    private static List<PunishmentType> defaults() {
        List<PunishmentType> list = new ArrayList<>();

        list.add(new PunishmentType("Mute", "/mute {player} {duration} {reason}", true, true, true,
            new ArrayList<>(List.of("Spam", "Chat Flood", "Light Toxicity", "Staff Disrespect",
                "Server Defamation", "Racism", "Suicidal Encouragement", "Inappropriate Comments", "Advertisement")),
            new ArrayList<>(List.of("15m", "30m", "1h", "2h", "12h", "1d", "3d", "PERMANENT"))));

        list.add(new PunishmentType("IP Mute", "/ipmute {player} {duration} {reason}", true, true, true,
            new ArrayList<>(List.of("Spam", "Chat Flood", "Advertisement", "Ban Evasion")),
            new ArrayList<>(List.of("1h", "1d", "3d", "PERMANENT"))));

        list.add(new PunishmentType("Ban", "/ban {player} {reason}", true, true, false,
            new ArrayList<>(List.of("Hacking", "Ban Evasion", "Severe Harassment", "Scamming")),
            new ArrayList<>()));

        list.add(new PunishmentType("IP Ban", "/ipban {player} {reason}", true, true, false,
            new ArrayList<>(List.of("Ban Evasion", "Alt Abuse")),
            new ArrayList<>()));

        list.add(new PunishmentType("Kick", "/kick {player} {reason}", true, true, false,
            new ArrayList<>(List.of("AFK", "Combat Logging", "Misbehavior")),
            new ArrayList<>()));

        list.add(new PunishmentType("Warn", "/warn {player} {reason}", true, true, false,
            new ArrayList<>(List.of("Minor Rule Break", "Disrespect")),
            new ArrayList<>()));

        list.add(new PunishmentType("Alts", "/alts {player}", true, false, false,
            new ArrayList<>(), new ArrayList<>()));

        list.add(new PunishmentType("Check History", "/hist {player}", true, false, false,
            new ArrayList<>(), new ArrayList<>()));

        return list;
    }
}
