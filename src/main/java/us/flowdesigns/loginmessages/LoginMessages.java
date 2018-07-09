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
import us.flowdesigns.utils.NLog;
import us.flowdesigns.utils.NUtil;

import java.io.File;

public class LoginMessages extends JavaPlugin
{

    public static LoginMessages plugin;
    public static Server server;
    public static LoginMessages instance;

    public static final String COMPILE_NMS_VERSION = "v1_12_R1";
    public static String pluginName;
    public static String pluginVersion;

    File jarFile = this.getFile();

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
        warnVersion();
        server.getPluginManager().registerEvents(new RankLoginMessages(), LoginMessages.plugin);
        server.getPluginManager().registerEvents(new PlayerLoginMessages(), LoginMessages.plugin);
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
        instance = this;
    }

    @Override
    public void onDisable()
    {
    }

    public static LoginMessages getInstance()
    {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        return CMD_Handler.handleCommand(sender, cmd, commandLabel, args);
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