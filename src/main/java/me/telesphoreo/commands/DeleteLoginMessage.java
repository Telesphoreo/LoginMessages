package me.telesphoreo.commands;

import me.telesphoreo.LoginMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteLoginMessage implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Messages.PLAYER_ONLY);
            return true;
        }

        if (!sender.hasPermission("loginmessages.deleteloginmessage"))
        {
            sender.sendMessage(Messages.MSG_NO_PERMS);
            return true;
        }

        LoginMessages.plugin.deleteLoginMessage((Player)sender);
        return true;
    }
}