package com.restonic4.under_control.api.whitelist;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class WhitelistAPI {
    private static final Map<Pair<String, String>, TypedCondition<?>> whitelists = new HashMap<>();

    private record TypedCondition<T>(Function<T, Boolean> condition) {
        public boolean test(T input) {
            return condition.apply(input);
        }
    }

    public static <T> void registerWhitelist(String modID, String whitelistKey, Function<T, Boolean> condition) {
        Pair<String, String> keyPair = new Pair<>(modID, whitelistKey);

        if (!whitelists.containsKey(keyPair)) {
            whitelists.put(keyPair, new TypedCondition<>(condition));
        }
    }

    public static <T> boolean isAllowed(String modID, String whitelistKey, T input) {
        Pair<String, String> keyPair = new Pair<>(modID, whitelistKey);

        if (whitelists.containsKey(keyPair)) {
            TypedCondition<T> typedCondition = (TypedCondition<T>) whitelists.get(keyPair);
            return typedCondition.test(input);
        }

        return false;
    }
}
