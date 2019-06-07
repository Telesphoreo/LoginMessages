package me.telesphoreo.commands;

import java.util.Collections;
import java.util.List;
import me.telesphoreo.LoginMessages;
import me.telesphoreo.util.NLog;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class LoginMessagesCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args)
    {
        if (args.length == 0)
        {
            LoginMessages.BuildProperties build = LoginMessages.build;
            sender.sendMessage(ChatColor.GOLD + "LoginMessages is a lightweight plugin that allows players to have a custom join message.");
            sender.sendMessage(ChatColor.GOLD + String.format("Version "
                            + ChatColor.BLUE + "%s.%s.%s",
                    build.version,
                    build.number,
                    build.head));
            sender.sendMessage(String.format(ChatColor.GOLD + "Compiled on "
                            + ChatColor.BLUE + "%s" + ChatColor.GOLD + " by "
                            + ChatColor.BLUE + "%s",
                    build.date,
                    build.author));
            sender.sendMessage(ChatColor.GREEN + "Visit " + ChatColor.AQUA + "https://github.com/Telesphoreo/LoginMessages" + ChatColor.GREEN + " for more information");
            return true;
        }
        if (args[0].toLowerCase().equals("reload"))
        {
            if (!sender.hasPermission("loginmessages.reload"))
            {
                sender.sendMessage(Messages.MSG_NO_PERMS);
                return true;
            }
            try
            {
                LoginMessages.plugin.isConfigOutOfDate();
                LoginMessages.plugin.reloadConfig();
                sender.sendMessage(Messages.RELOADED);
                return true;
            }
            catch (Exception ex)
            {
                NLog.severe(ex);
                sender.sendMessage(Messages.FAILED);
            }
            return true;
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender.hasPermission("loginmessages.reload"))
        {
            return Collections.singletonList("reload");
        }
        else
        {
            return null;
        }
    }
}