package me.telesphoreo.commands;

import me.telesphoreo.LoginMessages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLoginMessage implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args)
    {
        if (!sender.hasPermission("loginmessages.setloginmessage"))
        {
            sender.sendMessage(Messages.MSG_NO_PERMS);
            return true;
        }

        if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase("-o"))
            {
                if (!sender.hasPermission("loginmessages.setloginmessage.others"))
                {
                    sender.sendMessage(Messages.MSG_NO_PERMS);
                    return true;
                }

                if (args.length < 2)
                {
                    sender.sendMessage(Messages.SPECIFY_PLAYER);
                    return true;
                }

                if (args.length < 3)
                {
                    sender.sendMessage(Messages.PROVIDE_A_MESSAGE);
                    return true;
                }

                Player player = Bukkit.getPlayer(args[1]);
                if (player == null)
                {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return true;
                }
                String loginMsg = StringUtils.join(args, " ", 2, args.length);
                LoginMessages.plugin.setLoginMessage(sender, player, loginMsg.replace(player.getName(), "%player%"));
            }
            else
            {
                if (!(sender instanceof Player))
                {
                    return false;
                }
                String loginMsg = StringUtils.join(args, " ", 0, args.length);
                LoginMessages.plugin.setLoginMessage((Player)sender, loginMsg.replace(sender.getName(), "%player%"));
                return true;
            }
        }
        else
        {
            return false;
        }
        return true;
    }
}