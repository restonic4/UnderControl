package com.chaotic_loom.under_control.api.client;

import java.util.UUID;

public class ClientAPI {
    private static String username;
    private static String uuid;

    public static void setCustomDevelopmentProfile(String newUsername, String newUUID) {
        username = newUsername;
        uuid = newUUID;
    }

    public static String getUsername() {
        return username;
    }

    public static String getUuid() {
        return uuid;
    }
}
