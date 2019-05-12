package me.telesphoreo.commands;

import me.telesphoreo.LoginMessages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLoginMessage implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        if (!sender.hasPermission("loginmessages.setloginmessage"))
        {
            sender.sendMessage(Messages.MSG_NO_PERMS);
            return true;
        }

        if (args.length < 1)
        {
            return false;
        }

        String loginMsg = StringUtils.join(args, " ", 0, args.length);
        LoginMessages.plugin.setLoginMessage((Player)sender, loginMsg.replace(sender.getName(), "%player%"));
        return true;
    }
}