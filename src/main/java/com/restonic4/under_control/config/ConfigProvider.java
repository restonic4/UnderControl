package com.restonic4.under_control.config;

import com.restonic4.under_control.saving.SavingProvider;

public class ConfigProvider extends SavingProvider {
    public ConfigProvider(String modID, String saveFilePath) {
        super(modID, saveFilePath);
    }

    public <T> void registerOption(String key, T defaultValue, String comment) {
        Class<?> type = defaultValue.getClass();

        if (get(key, type) == null) {
            addComment(key, comment);
            save(key, defaultValue);
        }
    }

    public void reload() {
        super.loadFromFile();
    }
}
