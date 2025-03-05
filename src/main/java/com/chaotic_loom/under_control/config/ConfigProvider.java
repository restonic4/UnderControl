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

    public Map<String, List<String>> getGroups() {
        return groups.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> List.copyOf(entry.getValue()),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    private final LinkedHashMap<String, Map<String, Object>> cachedGroupedConfigs = new LinkedHashMap<>();

    public Map<String, Map<String, Object>> getConfigs() {
        cachedGroupedConfigs.clear();

        // Iterar sobre los grupos en orden de inserción
        for (Map.Entry<String, List<String>> groupEntry : groups.entrySet()) {
            String groupName = groupEntry.getKey();
            Map<String, Object> groupSettings = new LinkedHashMap<>();

            // Obtener todas las keys del grupo en orden de inserción
            for (String key : groupEntry.getValue()) {
                Object currentValue = get(key, registeredOptions.get(key).defaultValue.getClass());
                if (currentValue != null) {
                    groupSettings.put(key, currentValue);
                }
            }

            if (!groupSettings.isEmpty()) {
                cachedGroupedConfigs.put(groupName, groupSettings);
            }
        }

        return cachedGroupedConfigs;
    }

    private record OptionData(Object defaultValue, String comment, String group) {
    }
}
