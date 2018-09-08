package us.flowdesigns.loginmessages;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.EnumMap;
import java.util.Map;

public class Config
{
    private static Map<ConfigFile, YamlConfiguration> config = new EnumMap<ConfigFile, YamlConfiguration>(ConfigFile.class);
    private static Map<ConfigFile, File> configFile = new EnumMap<ConfigFile, File>(ConfigFile.class);
    private static Map<ConfigFile, Boolean> loaded = new EnumMap<ConfigFile, Boolean>(ConfigFile.class);

    public static YamlConfiguration getConfig(ConfigFile configfile)
    {
        if (loaded.containsKey(configfile) && !loaded.get(configfile))
        {
            loadConfig(configfile);
        }
        return config.get(configfile);
    }

    public static File getConfigFile(ConfigFile configfile)
    {
        return configFile.get(configfile);
    }

    public static boolean getLoaded(ConfigFile configfile)
    {
        return loaded.get(configfile);
    }

    public static void loadConfigs()
    {
        for (ConfigFile configfile : ConfigFile.values())
        {
            loadConfig(configfile);
        }
    }

    public static void loadConfig(ConfigFile configfile)
    {
        configFile.put(configfile, new File(Bukkit.getServer().getPluginManager().getPlugin("LoginMessages").getDataFolder(), configfile.getFile()));
        if (configFile.get(configfile).exists())
        {
            config.put(configfile, new YamlConfiguration());
            try
            {
                config.get(configfile).load(configFile.get(configfile));
            }
            catch (IOException | InvalidConfigurationException ex)
            {
                loaded.put(configfile, false);
                return;
            }
            loaded.put(configfile, true);
        }
        else
        {
            try
            {
                Bukkit.getServer().getPluginManager().getPlugin("LoginMessages").getDataFolder().mkdir();
                InputStream jarURL = LoginMessages.class.getResourceAsStream("/" + configfile.getFile());
                copyFile(jarURL, configFile.get(configfile));
                config.put(configfile, new YamlConfiguration());
                config.get(configfile).load(configFile.get(configfile));
                loaded.put(configfile, true);
            }
            catch (Exception e)
            {
            }
        }
    }

    static private void copyFile(InputStream in, File out) throws Exception
    {
        InputStream fis = in;
        FileOutputStream fos = new FileOutputStream(out);
        try
        {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1)
            {
                fos.write(buf, 0, i);
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (fis != null)
            {
                fis.close();
            }
            if (fos != null)
            {
                fos.close();
            }
        }
    }

    private Config()
    {
    }

    public enum ConfigFile
    {
        // Enums
        CONFIG("config.yml");

        // Variables
        private String file;

        /**
         * Constructor of ConfigFile.
         *
         * @param file
         */
        ConfigFile(String file)
        {
            this.file = file;
        }

        /**
         * Gets the file associated with the enum.
         *
         * @return File associated wiht the enum
         */
        public String getFile()
        {
            return this.file;
        }
    }
}