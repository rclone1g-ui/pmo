// Goes in: src/main/java/com/acentra/pmotools/PunishmentType.java
package com.acentra.pmotools;

import java.util.ArrayList;
import java.util.List;

// Plain data class, loaded/saved as JSON via Gson. Public no-arg
// constructor + public fields is intentional (Gson needs it).
public class PunishmentType {
    public String name = "";
    public String commandTemplate = ""; // use {player}, {duration}, {reason}
    public boolean requiresPlayer = true;
    public boolean requiresReason = true;
    public boolean requiresDuration = true;
    public List<String> reasons = new ArrayList<>();
    public List<String> durations = new ArrayList<>(); // "PERMANENT" = no duration arg

    public PunishmentType() {}

    public PunishmentType(String name, String commandTemplate, boolean requiresPlayer,
                           boolean requiresReason, boolean requiresDuration,
                           List<String> reasons, List<String> durations) {
        this.name = name;
        this.commandTemplate = commandTemplate;
        this.requiresPlayer = requiresPlayer;
        this.requiresReason = requiresReason;
        this.requiresDuration = requiresDuration;
        this.reasons = reasons;
        this.durations = durations;
    }
}
