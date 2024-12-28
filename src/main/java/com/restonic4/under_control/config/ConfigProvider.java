package com.restonic4.under_control.config;

import com.restonic4.under_control.saving.SavingProvider;

public class ConfigProvider extends SavingProvider {
    public ConfigProvider(String saveFilePath) {
        super(saveFilePath);
    }

    public ConfigProvider(SavingProvider savingProvider) {
        super(savingProvider.getSaveFilePath());
        super.setDataStore(savingProvider.getDataStore());
        super.setDefaultValues(savingProvider.getDefaultValues());
    }

    public void reload() {
        super.loadFromFile();
    }
}
