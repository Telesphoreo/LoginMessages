package me.telesphoreo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import me.telesphoreo.LoginMessages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import static org.bukkit.Bukkit.getServer;

public class Updater extends LoginMessagesBase
{
    private LoginMessages plugin;
    private LoginMessages.BuildProperties build = LoginMessages.build;
    private String oldHead = build.head;
    private String path = this.getFilePath();

    public Updater(LoginMessages plugin)
    {
        this.plugin = plugin;
    }

    public void update()
    {
        try
        {
            String versionLink = "https://www.telesphoreo.me/files/loginmessages/version.txt";
            URL url = new URL(versionLink);
            URLConnection con = url.openConnection();
            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader reader = new BufferedReader(isr);

            if (!reader.ready())
            {
                return;
            }

            String newHead = reader.readLine();
            reader.close();

            if (oldHead.equals("${git.commit.id.abbrev}") || oldHead.equals("unknown"))
            {
                logger.info("No Git head detected, not updating LoginMessages.");
                return;
            }

            if (newHead.equals(oldHead))
            {
                logger.info("There are no updates available.");
                return;
            }

            String dlLink = "https://telesphoreo.me/files/loginmessages/LoginMessages.jar";
            url = new URL(dlLink);
            con = url.openConnection();
            InputStream in = con.getInputStream();
            FileOutputStream out = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, size);
            }

            out.close();
            in.close();
            logger.info("An update to LoginMessages has been applied. (" + oldHead + ") -> " + "(" + newHead + ")");
        }
        catch (IOException ex)
        {
            logger.info("There was an issue connecting to the server to check for updates.");
        }
    }

    private String getFilePath()
    {
        try
        {
            JavaPlugin plugin = (JavaPlugin)getServer().getPluginManager().getPlugin(LoginMessages.pluginName);
            Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            File file = (File)getFileMethod.invoke(plugin);
            return "plugins" + File.separator + Bukkit.getUpdateFolder() + File.separator + file.getName();
        }
        catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return "plugins" + File.separator + "LoginMessages.jar";
    }
}