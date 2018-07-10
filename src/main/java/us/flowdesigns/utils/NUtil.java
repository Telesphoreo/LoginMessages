package us.flowdesigns.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Field;

import static us.flowdesigns.loginmessages.LoginMessages.COMPILE_NMS_VERSION;

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
    public static void warnVersion()
    {
        final String nms = NUtil.getNMSVersion();

        if (!COMPILE_NMS_VERSION.equals(nms))
        {
            NLog.warning("LoginMessages is compiled for " + COMPILE_NMS_VERSION + " but the server is running version " + nms + "!");
            NLog.warning("This might result in unexpected behavior!");
        }
    }
}