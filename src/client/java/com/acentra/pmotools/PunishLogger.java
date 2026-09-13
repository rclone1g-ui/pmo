// Goes in: src/main/java/com/acentra/pmotools/PunishLogger.java
package com.acentra.pmotools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PunishLogger {

    // EDIT THIS to your real 1.21.5 instance's config/logs folder.
    private static final String LOG_FOLDER_PATH = "C:/Users/muham/Documents/Minecraft/PunishLogger";

    private static final List<String> LOGGED_COMMANDS = List.of(
        "/sc", "/kick", "/warn", "/mute", "/ipmute", "/hist", "/staff", "/checkmute",
        "/ban", "/ipban", "/unban", "/unbanip", "/alts", "/unmute", "/alerts",
        "/grimac history", "/grimac verbose", "/v", "/silent", "/co inspect", "/fly"
    );

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    static {
        TimeZone dubai = TimeZone.getTimeZone("GMT+4");
        DATE_FORMAT.setTimeZone(dubai);
        TIME_FORMAT.setTimeZone(dubai);
    }

    public static void maybeLog(String fullCommand) {
        String lower = fullCommand.toLowerCase();
        boolean matched = false;
        for (String cmd : LOGGED_COMMANDS) {
            if (lower.startsWith(cmd)) {
                matched = true;
                break;
            }
        }
        if (!matched) return;

        try {
            File folder = new File(LOG_FOLDER_PATH);
            if (!folder.exists()) folder.mkdirs();

            Date now = new Date();
            String currentDate = DATE_FORMAT.format(now);
            String currentTime = TIME_FORMAT.format(now);

            File dailyFile = new File(folder, currentDate + ".txt");
            if (!dailyFile.exists()) {
                dailyFile.createNewFile();
                try (FileWriter header = new FileWriter(dailyFile, true)) {
                    header.write("==============================\n");
                    header.write("DATE: " + currentDate + " (GMT+4)\n");
                    header.write("==============================\n");
                }
            }

            try (FileWriter writer = new FileWriter(dailyFile, true)) {
                writer.write("[" + currentTime + "] " + fullCommand + "\n");
            }
        } catch (IOException e) {
            PmoToolsMod.LOGGER.error("PunishLogger failed to write log", e);
        }
    }
}
