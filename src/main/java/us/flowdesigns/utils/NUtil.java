package us.flowdesigns.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Field;

public class NUtil
{
    public static String colorize(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String getNMSVersion()
    {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
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
                return (T) field.get(from);
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