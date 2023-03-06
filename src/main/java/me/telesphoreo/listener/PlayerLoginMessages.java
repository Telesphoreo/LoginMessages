package me.telesphoreo.listener;

import java.util.Map;
import me.telesphoreo.util.LoginMessagesBase;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLoginMessages extends LoginMessagesBase implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        try
        {
            Map<String, Object> player_login_messages = plugin.getConfig().getConfigurationSection("players").getValues(false);
            boolean vanilla_join_msg = plugin.getConfig().getBoolean("show_vanilla_messages");

            for (String playerKeys : player_login_messages.keySet())
            {
                String message = plugin.getConfig().getString("players." + player.getName() + ".message");
                if (playerKeys.contains(player.getName()))
                {
                    if (message == null)
                    {
                        logger.severe("There is no message set!");
                        break;
                    }
                    if (!vanilla_join_msg)
                    {
                        // Set the join message
                        event.joinMessage(plugin.mmDeserialize((message.replace("%player%", player.getName()))));
                        break;
                    }
                    else
                    {
                        // Just broadcast it instead
                        Bukkit.broadcast(plugin.mmDeserialize(message.replace("%player%", player.getName())));
                        break;
                    }
                }
            }
        }
        catch (ClassCastException ex)
        {
            logger.severe("Failed to load login messages.");
            logger.severe(ex.toString());
        }
    }
}
