package com.chaotic_loom.under_control.networking.services;

import com.chaotic_loom.under_control.UnderControl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ApiClient {
    private final String baseUrl;
    private final Map<String, String> defaultHeaders;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.defaultHeaders = new HashMap<>();
        this.defaultHeaders.put("Content-Type", "application/json");
    }

    public void setHeader(String key, String value) {
        defaultHeaders.put(key, value);
    }

    public ApiResponse get(String endpoint) {
        try {
            return HttpClient.sendRequest("GET", baseUrl + endpoint, null, defaultHeaders);
        } catch (IOException ioException) {
            UnderControl.LOGGER.error(ioException);
            return HttpClient.getFailedResponse(ioException);
        }
    }

    public void getAsync(String endpoint, Consumer<ApiResponse> callback) {
        CompletableFuture.runAsync(() -> {
            ApiResponse response = get(endpoint);
            callback.accept(response);
        });
    }

    public ApiResponse post(String endpoint, String body) {
        try {
            return HttpClient.sendRequest("POST", baseUrl + endpoint, body, defaultHeaders);
        } catch (IOException ioException) {
            UnderControl.LOGGER.error(ioException);
            return HttpClient.getFailedResponse(ioException);
        }
    }

    public void postAsync(String endpoint, String body, Consumer<ApiResponse> callback) {
        CompletableFuture.runAsync(() -> {
            ApiResponse response = post(endpoint, body);
            callback.accept(response);
        });
    }

    public ApiResponse put(String endpoint, String body) {
        try {
            return HttpClient.sendRequest("PUT", baseUrl + endpoint, body, defaultHeaders);
        } catch (IOException ioException) {
            UnderControl.LOGGER.error(ioException);
            return HttpClient.getFailedResponse(ioException);
        }
    }

    public void putAsync(String endpoint, String body, Consumer<ApiResponse> callback) {
        CompletableFuture.runAsync(() -> {
            ApiResponse response = put(endpoint, body);
            callback.accept(response);
        });
    }

    public static String mapToJson(Map<String, String> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":\"")
                    .append(entry.getValue()).append("\",");
        }
        if (json.length() > 1) {
            json.setLength(json.length() - 1); // Remove last comma
        }
        json.append("}");
        return json.toString();
    }
}
