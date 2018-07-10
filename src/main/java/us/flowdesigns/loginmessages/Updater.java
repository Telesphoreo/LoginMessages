package us.flowdesigns.loginmessages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.flowdesigns.utils.NLog;
import us.flowdesigns.utils.NUtil;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Updater {
    final String dlLink = "https://flowdesigns.us/LoginMessages.jar";
    final String versionLink = "https://flowdesigns.us/loginmessagesversion.txt";
    private Plugin plugin;

    public Updater (Plugin plugin) {
        this.plugin = plugin;
    }

    public void update(CommandSender sender)
    {
        int oldVersion = this.getVersionFromString(plugin.getDescription().getVersion());
        String path = this.getFilePath();

        try
        {
            URL url = new URL(versionLink);
            URLConnection con = url.openConnection();
            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            reader.ready();
            int newVersion = this.getVersionFromString(reader.readLine());
            reader.close();

            if (newVersion > oldVersion)
            {
                url = new URL(dlLink);
                con = url.openConnection();
                InputStream in = con.getInputStream();
                FileOutputStream out = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while((size = in.read(buffer)) != -1) {
                    out.write(buffer, 0, size);
                }

                out.close();
                in.close();
                NLog.info(sender.getName() + " - Updating to the latest version of LoginMessages");
                sender.sendMessage(ChatColor.GRAY + "Updating to the latest version of LoginMessages, please wait.");
                Bukkit.reload();
                sender.sendMessage(ChatColor.GRAY + "LoginMessages was successfully updated.");
            }
            else
            {
                sender.sendMessage(ChatColor.GRAY + "There are no updates available for LoginMessages.");
            }
        } catch (IOException e)
        {
            sender.sendMessage(ChatColor.GRAY + "There are no over the air updates available for LoginMessages. Try restarting your server and checking for update. If this does not work, please go to https://github.com/Telesphoreo/LoginMessages/releases and download the latest release from there.");
            NLog.severe(e);
        }
    }

    public String getFilePath()
    {
        if (plugin instanceof JavaPlugin)
        {
            try
            {
                Method method = JavaPlugin.class.getDeclaredMethod("getFile");
                boolean wasAccessible = method.isAccessible();
                method.setAccessible(true);
                File file = (File) method.invoke(plugin);
                method.setAccessible(wasAccessible);

                return file.getPath();
            } catch (Exception e) {
                return "plugins" + File.separator + plugin.getName();
            }
        } else {
            return "plugins" + File.separator + plugin.getName();
        }
    }

    public int getVersionFromString(String from)
    {
        String result = "";
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(from);

        while(matcher.find())
        {
            result += matcher.group();
        }

        return result.isEmpty() ? 0 : Integer.parseInt(result);
    }
}