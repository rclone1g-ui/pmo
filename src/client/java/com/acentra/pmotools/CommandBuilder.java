// Goes in: src/main/java/com/acentra/pmotools/CommandBuilder.java
package com.acentra.pmotools;

public class CommandBuilder {
    public static String build(PunishmentType type, String player, String duration, String reason) {
        String cmd = type.commandTemplate;
        cmd = cmd.replace("{player}", player == null ? "" : player);
        cmd = cmd.replace("{reason}", reason == null ? "" : reason);
        if (duration != null && duration.equalsIgnoreCase("PERMANENT")) {
            // Drop the duration token entirely for permanent actions.
            cmd = cmd.replace(" {duration}", "").replace("{duration}", "");
        } else {
            cmd = cmd.replace("{duration}", duration == null ? "" : duration);
        }
        return cmd.replaceAll("\\s+", " ").trim();
    }
}
