package me.telesphoreo.listener;

import java.util.Map;
import me.telesphoreo.loginmessages.LoginMessages;
import me.telesphoreo.loginmessages.NLog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLoginMessages implements Listener
{
    @EventHandler
    public boolean onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        try
        {
            Map<String, Object> player_login_messages = LoginMessages.plugin.getConfig().getConfigurationSection("players").getValues(false);
            boolean vanilla_join_msg = LoginMessages.plugin.getConfig().getBoolean("show_vanilla_messages");

            for (String playerKeys : player_login_messages.keySet())
            {
                String message = (String)LoginMessages.plugin.getConfig().get("players." + player.getName() + ".message");
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
