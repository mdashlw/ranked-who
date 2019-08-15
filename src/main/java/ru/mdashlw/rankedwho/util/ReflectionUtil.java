package ru.mdashlw.rankedwho.util;

import java.lang.reflect.Field;

public class ReflectionUtil {
    @SuppressWarnings("unchecked")
    public static <T> T getField(Object obj, String name) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return (T) field.get(obj);
    }

    public static <T> T getObfuscatedField(Object obj, String obfuscatedName, String deobfuscatedName) throws NoSuchFieldException, IllegalAccessException {
        try {
            return getField(obj, obfuscatedName);
        } catch (NoSuchFieldException exception) {
            return getField(obj, deobfuscatedName);
        }
    }
}
