package com.restonic4.under_control.saving;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.api.saving.SavingAPI;
import net.minecraft.core.BlockPos;

import java.io.*;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavingProvider {
    private Map<String, Object> dataStore = new HashMap<>();
    private Map<String, Object> defaultValues = new HashMap<>();

    private Map<String, String> comments = new HashMap<>();

    private final String modID;
    private final String saveFilePath;

    public SavingProvider(String modID, String saveFilePath) {
        this.modID = modID;
        this.saveFilePath = saveFilePath;
    }

    public <T> void registerDefaultValue(String key, T defaultValue) {
        defaultValues.put(key, defaultValue);
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilePath))) {
            for (Map.Entry<String, Object> entry : dataStore.entrySet()) {
                String line = entry.getKey() + "=" + serializeObject(entry.getValue());

                if (comments.containsKey(entry.getKey())) {
                    line += "#" + comments.get(entry.getKey());
                }

                writer.write(line);
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File file = new File(saveFilePath);
        if (!file.exists()) {
            System.out.println("Save file not found.");
            dataStore.clear();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String lineComment;
            dataStore.clear();
            while ((line = reader.readLine()) != null) {
                String[] lineParts = line.split("#");

                line = lineParts[0];
                lineComment = lineParts.length == 2 ? lineParts[1] : "";

                String[] parts = line.split("=", 2);

                if (parts.length == 2) {
                    String key = parts[0];
                    Object value = deserializeObject(parts[1]);
                    dataStore.put(key, value);

                    if (!lineComment.isEmpty() && !lineComment.isBlank()) {
                        comments.put(key, lineComment);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String serializeObject(Object object) {
        if (object == null) {
            return "null";
        }

        if (object instanceof Integer || object instanceof Double || object instanceof Boolean) {
            return object.toString();
        }

        if (object instanceof Long) {
            return object.toString() + "L";
        }

        if (object instanceof Float) {
            return object.toString() + "f";
        }

        if (object instanceof String) {
            return "\"" + object.toString() + "\""; // Surround String with ""
        }

        if (object instanceof BlockPos blockPos) {
            VanillaSerializableTypes.BlockPos serializableBlockPos = new VanillaSerializableTypes.BlockPos();
            return serializableBlockPos.getIdentifier() + ";" + serializableBlockPos.serialize(blockPos);
        }

        if (object instanceof ClassProvider<?>) {
            @SuppressWarnings("unchecked")
            ClassProvider<Object> classProvider = (ClassProvider<Object>) object;
            return classProvider.getIdentifier() + ";" + classProvider.serialize(object);
        }

        return object.toString();
    }

    private Object deserializeObject(String objectData) {
        // Primitives
        if (objectData.matches("^-?\\d+$")) { // Integer
            return Integer.parseInt(objectData);
        }

        if (objectData.matches("^-?\\d*\\.\\d+$")) { // Double
            return Double.parseDouble(objectData);
        }

        if (objectData.equals("true") || objectData.equals("false")) { // Boolean
            return Boolean.parseBoolean(objectData);
        }

        if (objectData.matches("^-?\\d*\\.\\d+f$")) { // Float
            return Float.parseFloat(objectData.substring(0, objectData.length() - 1)); // Delete 'f'
        }

        if (objectData.matches("^-?\\d+L$")) { // Long
            return Long.parseLong(objectData.substring(0, objectData.length() - 1)); // Delete 'L'
        }

        if (objectData.matches("^\".*\"$")) { // String with ""
            return objectData.substring(1, objectData.length() - 1); // Delete ""
        }

        String[] parts = objectData.split(";", 2);
        if (parts.length <= 2) {
            String identifier = parts[0];
            String data = (parts.length == 2) ? parts[1] : "";

            ClassProvider<?> provider = SavingAPI.getClassProviders().get(identifier);
            if (provider != null) {
                return provider.deserialize(data);
            }

            throw new IllegalStateException("Class provider not found for " + identifier + ", you should register the provider using SavingAPI.registerClassProvider(...);");
        }

        return null;
    }

    public boolean containsKey(String key) {
        return dataStore.containsKey(key);
    }

    public <T> void save(String key, T value) {
        dataStore.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = dataStore.get(key);

        if (value == null) {
            if (defaultValues.containsKey(key)) {
                return (T) defaultValues.get(key);
            }
        }

        if (type.isInstance(value)) {
            return (T) value;
        }

        return null;
    }

    public Object getDefaultValue(String key) {
        return defaultValues.get(key);
    }

    public void delete(String key) {
        dataStore.remove(key);
    }

    public void addComment(String key, String comment) {
        comments.put(key, comment);
    }

    public String getComment(String key) {
        return comments.get(key);
    }

    public String getSaveFilePath() {
        return this.saveFilePath;
    }

    public Map<String, Object> getDataStore() {
        return this.dataStore;
    }

    public Map<String, Object> getDefaultValues() {
        return this.defaultValues;
    }

    public Map<String, String> getComments() {
        return this.comments;
    }

    public void setDataStore(Map<String, Object> dataStore) {
        this.dataStore = dataStore;
    }

    public void setDefaultValues(Map<String, Object> defaultValues) {
        this.defaultValues = defaultValues;
    }

    public void setComments(Map<String, String> comments) {
        this.comments = comments;
    }

    public String getModID() {
        return this.modID;
    }
}
