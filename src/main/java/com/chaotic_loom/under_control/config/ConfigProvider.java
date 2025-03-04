package com.chaotic_loom.under_control.config;

import com.mojang.datafixers.util.Pair;
import com.chaotic_loom.under_control.saving.SavingProvider;

import java.util.HashMap;
import java.util.Map;

public class ConfigProvider extends SavingProvider {
    private final Map<String, Pair<?, String>> registeredOptions;

    public ConfigProvider(String modID, String saveFilePath) {
        super(modID, saveFilePath);
        this.registeredOptions = new HashMap<>();
    }

    public <T> void registerOption(String key, T defaultValue, String comment) {
        Class<?> type = defaultValue.getClass();

        if (!registeredOptions.containsKey(key)) {
            registeredOptions.put(key, new Pair<>(defaultValue, comment));
        }

        //T duplicatedInstance = JavaHelper.cloneObject(defaultValue);

        if (get(key, type) == null) {
            addComment(key, comment);
            save(key, defaultValue);
        }

        registerDefaultValue(key, defaultValue);
    }

    public void resetOption(String key) {
        save(key, getDefaultValue(key));
    }

    public void resetAll() {
        for (Map.Entry<String, Pair<?, String>> entry : registeredOptions.entrySet()) {
            resetOption(entry.getKey());
        }
    }

    public void reload() {
        getDataStore().clear();
        getDefaultValues().clear();
        getComments().clear();

        super.loadFromFile();

        for (Map.Entry<String, Pair<?, String>> entry : registeredOptions.entrySet()) {
            String optionKey = entry.getKey();
            Object optionDefaultValue = entry.getValue().getFirst();
            String optionComment = entry.getValue().getSecond();

            registerOption(optionKey, optionDefaultValue, optionComment);
        }
    }

    Map<String, Object> cachedConfigs = new HashMap<>();
    public Map<String, Object> getConfigs() {
        cachedConfigs.clear();

        for (Map.Entry<String, Object> entry : getDataStore().entrySet()) {
            String key = entry.getKey();

            if (registeredOptions.containsKey(key)) {
                cachedConfigs.put(key, entry.getValue());
            }
        }

        return cachedConfigs;
    }
}
