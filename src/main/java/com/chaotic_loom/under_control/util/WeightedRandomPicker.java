package com.chaotic_loom.under_control.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that allows adding objects with an associated weight and randomly selecting
 * one of them, giving more chances to those with higher weight.
 *
 * @param <T> The type of objects to be added and selected.
 */
public class WeightedRandomPicker<T> {
    private static class WeightedItem<T> {
        T item;
        double cumulativeWeight;

        WeightedItem(T item, double cumulativeWeight) {
            this.item = item;
            this.cumulativeWeight = cumulativeWeight;
        }
    }

    private final List<WeightedItem<T>> items = new ArrayList<>();
    private double totalWeight = 0.0;
    private final Random random = new Random();

    /**
     * Adds an object with its associated weight to the picker.
     *
     * @param item   The object to add.
     * @param weight The weight of the object. Must be a positive number.
     * @throws IllegalArgumentException if the weight is less than or equal to zero.
     */
    public void addItem(T item, double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        }
        totalWeight += weight;
        items.add(new WeightedItem<>(item, totalWeight));
    }

    /**
     * Randomly selects an object from the list, giving more chances to those
     * with higher weight.
     *
     * @return The randomly selected object.
     * @throws IllegalStateException if there are no objects in the list.
     */
    public T getRandomItem() {
        if (items.isEmpty()) {
            throw new IllegalStateException("No hay elementos para seleccionar.");
        }
        double randomValue = random.nextDouble() * totalWeight;
        for (WeightedItem<T> item : items) {
            if (randomValue < item.cumulativeWeight) {
                return item.item;
            }
        }
        // Debería ser imposible llegar aquí si la lógica es correcta
        throw new RuntimeException("Error en la selección aleatoria.");
    }
}
