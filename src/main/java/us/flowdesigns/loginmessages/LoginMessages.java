package us.flowdesigns.loginmessages;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.flowdesigns.commands.CMD_Handler;
import us.flowdesigns.commands.CMD_Loader;
import us.flowdesigns.listener.PlayerLoginMessages;
import us.flowdesigns.listener.RankLoginMessages;
import us.flowdesigns.listener.UpdateChecker;
import us.flowdesigns.utils.NLog;

public class LoginMessages extends JavaPlugin
{

    public static LoginMessages plugin;
    public static Server server;

    public static String pluginName;
    public static String pluginVersion;

    @Override
    public void onLoad()
    {
        LoginMessages.plugin = this;
        LoginMessages.server = plugin.getServer();
        NLog.setServerLogger(server.getLogger());
        NLog.setServerLogger(server.getLogger());
        LoginMessages.pluginName = plugin.getDescription().getName();
        LoginMessages.pluginVersion = plugin.getDescription().getVersion();
    }

    @Override
    public void onEnable()
    {
        server.getPluginManager().registerEvents(new PlayerLoginMessages(), LoginMessages.plugin);
        server.getPluginManager().registerEvents(new RankLoginMessages(), LoginMessages.plugin);
        server.getPluginManager().registerEvents(new UpdateChecker(), LoginMessages.plugin);
        Config.loadConfigs();
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                CMD_Loader.getCommandMap();
                CMD_Loader.scan();
            }
        };
    }

    @Override
    public void onDisable()
    {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        return CMD_Handler.handleCommand(sender, cmd, commandLabel, args);
    }
}