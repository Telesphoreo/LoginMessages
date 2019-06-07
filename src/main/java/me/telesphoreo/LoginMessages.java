package me.telesphoreo;

import java.io.InputStream;
import java.util.Properties;
import me.telesphoreo.commands.DeleteLoginMessage;
import me.telesphoreo.commands.LoginMessagesCommand;
import me.telesphoreo.commands.SetLoginMessage;
import me.telesphoreo.listener.PermissionLoginMessages;
import me.telesphoreo.listener.PlayerLoginMessages;
import me.telesphoreo.util.Config;
import me.telesphoreo.util.NLog;
import me.telesphoreo.util.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LoginMessages extends JavaPlugin
{
    public static LoginMessages plugin;
    public static final BuildProperties build = new BuildProperties();
    public static Server server;
    public static String pluginName;
    public static String pluginVersion;

    @Override
    public void onLoad()
    {
        LoginMessages.plugin = this;
        LoginMessages.server = plugin.getServer();
        NLog.setPluginLogger(plugin.getLogger());
        NLog.setServerLogger(server.getLogger());
        LoginMessages.pluginName = plugin.getDescription().getName();
        LoginMessages.pluginVersion = plugin.getDescription().getVersion();
    }

    @Override
    public void onEnable()
    {
        build.load(this);
        server.getPluginManager().registerEvents(new PlayerLoginMessages(), this);
        server.getPluginManager().registerEvents(new PermissionLoginMessages(), this);
        new Metrics(this);
        Config.loadConfigs();
        if (isConfigOutOfDate())
        {
            NLog.info("Configuration file has been successfully updated with new entries.");
        }
        registerCommands();
    }

    @Override
    public void onDisable()
    {
        try
        {
            Updater updater = new Updater(this);
            updater.update();
        }
        catch (NoClassDefFoundError ex)
        {
            NLog.info("Failed to check for an update.");
        }
    }

    public void registerCommands()
    {
        getCommand("deleteloginmessage").setExecutor(new DeleteLoginMessage());
        getCommand("loginmessages").setExecutor(new LoginMessagesCommand());
        getCommand("loginmessages").setTabCompleter(new LoginMessagesCommand());
        getCommand("setloginmessage").setExecutor(new SetLoginMessage());
    }

    public boolean isConfigOutOfDate()
    {
        if (!getConfig().isSet("show_vanilla_messages"))
        {
            NLog.info("Unable to find valid configuration entry: show_vanilla_messages! Creating new entry with default value");
            getConfig().set("show_vanilla_messages", false);
            saveConfig();
            reloadConfig();
            return true;
        }
        return false;
    }

    public void setLoginMessage(Player player, String message)
    {
        if (getConfig().get("players." + player.getName()) == null)
        {
            getConfig().createSection("players." + player.getName());
        }
        player.sendMessage(ChatColor.GRAY + "Your login message is now:");
        player.sendMessage(ChatColor.GRAY + "> " + ChatColor.RESET + colorize(message.replace("%player%", player.getName())));
        getConfig().set("players." + player.getName() + ".message", message);
        saveConfig();
        reloadConfig();
    }

    public void setLoginMessage(CommandSender sender, Player player, String message)
    {
        if (getConfig().get("players." + player.getName()) == null)
        {
            getConfig().createSection("players." + player.getName());
        }
        sender.sendMessage(ChatColor.GRAY + player.getName() + "'s login message is now:");
        sender.sendMessage(ChatColor.GRAY + "> " + ChatColor.RESET + colorize(message.replace("%player%", player.getName())));
        getConfig().set("players." + player.getName() + ".message", message);
        saveConfig();
        reloadConfig();
    }

    public void deleteLoginMessage(Player player)
    {
        if (getConfig().get("players." + player.getName()) == null)
        {
            player.sendMessage(ChatColor.RED + "You do not have a login message set.");
            return;
        }
        player.sendMessage(ChatColor.GRAY + "Your login message has been removed.");
        getConfig().set("players." + player.getName(), null);
        saveConfig();
        reloadConfig();
    }

    public void deleteLoginMessage(CommandSender sender, Player player)
    {
        if (getConfig().get("players." + player.getName()) == null)
        {
            sender.sendMessage(ChatColor.RED + player.getName() + " does not have a login message set.");
            return;
        }
        sender.sendMessage(ChatColor.GRAY + player.getName() + "'s login message has been removed.");
        getConfig().set("players." + player.getName(), null);
        saveConfig();
        reloadConfig();
    }

    public static String colorize(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static class BuildProperties
    {
        public String author;
        public String codename;
        public String version;
        public String number;
        public String date;
        public String head;

        void load(LoginMessages plugin)
        {
            try
            {
                final Properties props;

                try (InputStream in = plugin.getResource("build.properties"))
                {
                    props = new Properties();
                    props.load(in);
                }

                author = props.getProperty("buildAuthor", "unknown");
                codename = props.getProperty("buildCodename", "unknown");
                version = props.getProperty("buildVersion", pluginVersion);
                number = props.getProperty("buildNumber", "1");
                date = props.getProperty("buildDate", "unknown");
                head = props.getProperty("buildHead", "unknown").replace("${git.commit.id.abbrev}", "unknown");
            }
            catch (Exception ex)
            {
                NLog.severe("Could not load build properties! Did you compile with NetBeans/Maven?");
                NLog.severe(ex);
            }
        }
    }
}