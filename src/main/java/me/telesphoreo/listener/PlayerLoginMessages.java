package me.telesphoreo.listener;

import java.util.Map;
import me.telesphoreo.LoginMessages;
import me.telesphoreo.util.NLog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLoginMessages implements Listener
{
    private LoginMessages plugin = LoginMessages.plugin;

    @EventHandler
    public boolean onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        try
        {
            Map<String, Object> player_login_messages = plugin.getConfig().getConfigurationSection("players").getValues(false);
            boolean vanilla_join_msg = plugin.getConfig().getBoolean("show_vanilla_messages");

            for (String playerKeys : player_login_messages.keySet())
            {
                String message = (String)plugin.getConfig().get("players." + player.getName() + ".message");
                if (message == null)
                {
                    NLog.severe("There is no message set!");
                    break;
                }
                if (playerKeys.contains(player.getName()))
                {
                    if (!vanilla_join_msg)
                    {
                        // Set the join message
                        event.setJoinMessage(LoginMessages.colorize(message.replace("%player%", player.getName())));
                        // Log it
                        NLog.info(LoginMessages.colorize(message.replace("%player%", player.getName())));
                    }
                    else
                    {
                        // Just broadcast it instead
                        Bukkit.broadcastMessage(LoginMessages.colorize(message.replace("%player%", player.getName())));
                    }
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
