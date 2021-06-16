package me.telesphoreo.commands;

import me.telesphoreo.util.LoginMessagesBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteLoginMessage extends LoginMessagesBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args)
    {
        if (!sender.hasPermission("loginmessages.deleteloginmessage"))
        {
            sender.sendMessage(Messages.MSG_NO_PERMS);
            return true;
        }

        if (args.length == 0 && sender instanceof Player)
        {
            plugin.deleteLoginMessage((Player)sender);
            return true;
        }
        else if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase("-o"))
            {
                if (!sender.hasPermission("loginmessages.deleteloginmessage.others"))
                {
                    sender.sendMessage(Messages.MSG_NO_PERMS);
                    return true;
                }

                if (args.length < 2)
                {
                    sender.sendMessage(Messages.SPECIFY_PLAYER);
                    return true;
                }

                Player player = Bukkit.getPlayer(args[1]);
                if (player == null)
                {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return true;
                }
                plugin.deleteLoginMessage(sender, player);
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
        return true;
    }
}