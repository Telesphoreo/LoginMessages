package me.telesphoreo.listener;

import java.util.Map;
import me.telesphoreo.util.LoginMessagesBase;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class PermissionLoginMessages extends LoginMessagesBase implements Listener
{
    private boolean hasPermission(Player player, String permission)
    {
        Permission p = new Permission(permission, PermissionDefault.FALSE);
        return player.hasPermission(p);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        try
        {
            Map<String, Object> login_messages = plugin.getConfig().getConfigurationSection("ranks").getValues(false);
            Map<String, Object> player_login_messages = plugin.getConfig().getConfigurationSection("players").getValues(false);
            boolean vanilla_join_msg = plugin.getConfig().getBoolean("show_vanilla_messages");
            for (String key : login_messages.keySet())
            {
                MemorySection login = (MemorySection)login_messages.get(key);
                String permission = login.getString("permission");
                String message = login.getString("message");
                if (hasPermission(player, permission) && !player_login_messages.keySet().contains(player.getName()))
                {
                    if (message == null)
                    {
                        logger.severe("There is no message set!");
                        break;
                    }
                    if (!vanilla_join_msg)
                    {
                        // Set the join message
                        event.joinMessage(plugin.mmDeserialize(message.replace("%player%", player.getName())));
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
