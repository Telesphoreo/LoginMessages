package us.flowdesigns.listener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.flowdesigns.utils.NLog;
import us.flowdesigns.utils.NUtil;

import java.util.Map;

import static us.flowdesigns.loginmessages.LoginMessages.plugin;

public class PlayerLoginMessages implements Listener
{
    @EventHandler
    public boolean onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        try
        {
            Map<String, Object> player_login_messages = plugin.getConfig().getConfigurationSection("players").getValues(false);

            for (String playerKeys : player_login_messages.keySet())
            {
                MemorySection login = (MemorySection) player_login_messages.get(playerKeys);
                String message = (String) login.get("message");
                if (player_login_messages.keySet().contains(player.getName()))
                {
                    Bukkit.broadcastMessage(NUtil.colorize(message.replace("%player%", player.getName())));
                    return true;
                }
            }
        }
        catch (ClassCastException ex)
        {
            NLog.severe("Failed to load login messages.");
            NLog.severe(ex);
        }
        return true;
    }
}
