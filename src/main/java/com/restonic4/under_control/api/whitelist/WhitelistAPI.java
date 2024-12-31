package com.restonic4.under_control.api.whitelist;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class WhitelistAPI {
    private static final Map<Pair<String, String>, Function<?, Boolean>> whitelists = new HashMap<>();

    public static <T> void registerWhitelist(String modID, String whitelistKey, Function<T, Boolean> condition) {
        Pair<String, String> keyPair = new Pair<>(modID, whitelistKey);

        if (!whitelists.containsKey(keyPair)) {
            whitelists.put(keyPair, condition);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean isAllowed(String modID, String whitelistKey, T input) {
        Pair<String, String> keyPair = new Pair<>(modID, whitelistKey);

        if (whitelists.containsKey(keyPair)) {
            Function<T, Boolean> condition = (Function<T, Boolean>) whitelists.get(keyPair);
            return condition.apply(input);
        }

        return false;
    }
}
