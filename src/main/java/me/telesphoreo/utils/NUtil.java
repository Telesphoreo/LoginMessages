package me.telesphoreo.utils;

import java.lang.reflect.Field;
import org.bukkit.ChatColor;

public class NUtil
{
    public static String colorize(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getField(Object from, String name)
    {
        Class<?> checkClass = from.getClass();
        do
        {
            try
            {
                Field field = checkClass.getDeclaredField(name);
                field.setAccessible(true);
                return (T)field.get(from);
            }
            catch (NoSuchFieldException | IllegalAccessException ex)
            {
            }
        }
        while (checkClass.getSuperclass() != Object.class
                && ((checkClass = checkClass.getSuperclass()) != null));
        return null;
    }
}