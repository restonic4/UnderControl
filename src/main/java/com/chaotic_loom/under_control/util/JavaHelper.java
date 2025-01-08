package com.chaotic_loom.under_control.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class JavaHelper {
    public static <T> T cloneObject(T object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(object);
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                @SuppressWarnings("unchecked")
                T clonedObject = (T) ois.readObject();
                return clonedObject;
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error during object cloning", e);
        }
    }

    public static <T> void processRandomly(List<T> list, Consumer<T> action) {
        List<T> copy = new ArrayList<>(list);

        Collections.shuffle(copy);

        for (T element : copy) {
            action.accept(element);
        }
    }
}
