package com.chaotic_loom.under_control.config;

import com.mojang.datafixers.util.Pair;
import com.chaotic_loom.under_control.saving.SavingProvider;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigProvider extends SavingProvider {
    private final Map<String, OptionData> registeredOptions;
    private final LinkedHashMap<String, List<String>> groups;

    public ConfigProvider(String modID, String saveFilePath) {
        super(modID, saveFilePath);
        this.registeredOptions = new HashMap<>();
        this.groups = new LinkedHashMap<>();
    }

    public <T> void registerOption(String key, T defaultValue, String comment) {
        registerOption("default", key, defaultValue, comment);
    }

    public <T> void registerOption(String group, String key, T defaultValue, String comment) {
        Class<?> type = defaultValue.getClass();

        if (!registeredOptions.containsKey(key)) {
            OptionData data = new OptionData(defaultValue, comment, group);
            registeredOptions.put(key, data);

            groups.computeIfAbsent(group, k -> new ArrayList<>()).add(key);
        }

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
        for (String key : registeredOptions.keySet()) {
            resetOption(key);
        }
    }

    public void reload() {
        getDataStore().clear();
        getDefaultValues().clear();
        getComments().clear();

        super.loadFromFile();

        for (Map.Entry<String, OptionData> entry : registeredOptions.entrySet()) {
            String optionKey = entry.getKey();
            OptionData data = entry.getValue();

            registerOption(data.group, optionKey, data.defaultValue, data.comment);
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

    public Map<String, List<String>> getGroups() {
        return groups.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> List.copyOf(entry.getValue()),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    private record OptionData(Object defaultValue, String comment, String group) {
    }
}
