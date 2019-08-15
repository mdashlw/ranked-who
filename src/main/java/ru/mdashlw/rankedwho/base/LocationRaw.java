package ru.mdashlw.rankedwho.base;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationRaw {
    private String server;
    @JsonProperty("gametype")
    private String gameType;
    private String mode;

    public String getServer() {
        return server;
    }

    public String getGameType() {
        return gameType;
    }

    public String getMode() {
        return mode;
    }
}
