package me.telesphoreo;

import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.telesphoreo.commands.DeleteLoginMessage;
import me.telesphoreo.commands.LoginMessagesCommand;
import me.telesphoreo.commands.SetLoginMessage;
import me.telesphoreo.listener.PermissionLoginMessages;
import me.telesphoreo.listener.PlayerLoginMessages;
import me.telesphoreo.util.Updater;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class LoginMessages extends JavaPlugin
{
    public static final BuildProperties build = new BuildProperties();
    public static LoginMessages plugin;
    public static Server server;
    public static String pluginName;
    public static String pluginVersion;
    private final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public Component mmDeserialize(String message)
    {
        return MINI_MESSAGE.deserialize(message).clickEvent(null).hoverEvent(null);
    }

    @Override
    public void onLoad()
    {
        plugin = this;
        server = plugin.getServer();
        pluginName = plugin.getDescription().getName();
        pluginVersion = plugin.getDescription().getVersion();
        this.saveDefaultConfig();
    }

    @Override
    public void onEnable()
    {
        build.load(this);
        server.getPluginManager().registerEvents(new PlayerLoginMessages(), this);
        server.getPluginManager().registerEvents(new PermissionLoginMessages(), this);
        int pluginId = 2975;
        new Metrics(this, pluginId);
        registerCommands();
        try
        {
            Updater updater = new Updater(this);
            updater.update();
        }
        catch (NoClassDefFoundError ex)
        {
            getLogger().info("Failed to check for an update.");
        }
    }

    @Override
    public void onDisable()
    {
    }

    public void registerCommands()
    {
        getCommand("deleteloginmessage").setExecutor(new DeleteLoginMessage());
        getCommand("loginmessages").setExecutor(new LoginMessagesCommand());
        getCommand("loginmessages").setTabCompleter(new LoginMessagesCommand());
        getCommand("setloginmessage").setExecutor(new SetLoginMessage());
    }

    public void setLoginMessage(Player player, String message)
    {
        if (getConfig().get("players." + player.getName()) == null)
        {
            getConfig().createSection("players." + player.getName());
        }
        player.sendMessage(mmDeserialize("<gray>Your login message is now:"));
        player.sendMessage(mmDeserialize("<gray>> " + message.replace("%player%", player.getName())));
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
        player.sendMessage(mmDeserialize("<gray>" + player.getName() + "'s login message is now:"));
        player.sendMessage(mmDeserialize("<gray>> " + message.replace("%player%", player.getName())));
        getConfig().set("players." + player.getName() + ".message", message);
        saveConfig();
        reloadConfig();
    }

    public void deleteLoginMessage(Player player)
    {
        if (getConfig().get("players." + player.getName()) == null)
        {
            player.sendMessage(mmDeserialize("<red>You do not have a login message set."));
            return;
        }
        player.sendMessage(mmDeserialize("<gray>Your login message has been removed."));
        getConfig().set("players." + player.getName(), null);
        saveConfig();
        reloadConfig();
    }

    public void deleteLoginMessage(CommandSender sender, Player player)
    {
        if (getConfig().get("players." + player.getName()) == null)
        {
            sender.sendMessage(mmDeserialize("<red>" + player.getName() + " does not have a login message set."));
            return;
        }
        sender.sendMessage(mmDeserialize("<gray>" + player.getName() + "'s login message has been removed."));
        getConfig().set("players." + player.getName(), null);
        saveConfig();
        reloadConfig();
    }

    public static class BuildProperties
    {
        public String author;
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
                version = props.getProperty("buildVersion", pluginVersion);
                number = props.getProperty("buildNumber", "1");
                date = props.getProperty("buildDate", "unknown");
                head = props.getProperty("buildHead", "unknown").replace("${git.commit.id.abbrev}", "unknown");
            }
            catch (Exception ex)
            {
                Bukkit.getLogger().severe("Could not load build properties! Did you compile with NetBeans/Maven?");
                Bukkit.getLogger().severe(ex.toString());
            }
        }
    }
}