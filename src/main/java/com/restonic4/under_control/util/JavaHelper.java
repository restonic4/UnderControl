package com.restonic4.under_control.util;

import java.io.*;

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
}
