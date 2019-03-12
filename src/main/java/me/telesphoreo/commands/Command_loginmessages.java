package me.telesphoreo.commands;

import me.telesphoreo.LoginMessages;
import me.telesphoreo.util.NLog;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.BOTH)
@CommandParameters(description = "Shows information about, reload, or update LoginMessages", usage = "/<command> [reload]")
public class Command_loginmessages extends BaseCommand
{
    @Override
    public boolean run(final CommandSender sender, final Player sender_p, final Command cmd, final String commandLabel, final String[] args, final boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            LoginMessages.BuildProperties build = LoginMessages.build;
            sender.sendMessage(ChatColor.GOLD + "LoginMessages is a lightweight plugin that allows players to have a custom join message.");
            sender.sendMessage(ChatColor.GOLD + String.format("Version "
                            + ChatColor.BLUE + "%s - %s Build %s " + ChatColor.GOLD + "("
                            + ChatColor.BLUE + "%s" + ChatColor.GOLD + ")",
                    build.codename,
                    build.version,
                    build.number,
                    build.head));
            sender.sendMessage(String.format(ChatColor.GOLD + "Compiled on "
                            + ChatColor.BLUE + "%s" + ChatColor.GOLD + " by "
                            + ChatColor.BLUE + "%s",
                    build.date,
                    build.author));
            sender.sendMessage(ChatColor.GOLD + "Visit " + ChatColor.BLUE + "https://github.com/Telesphoreo/LoginMessages" + ChatColor.GOLD + " for more information");
            return true;
        }
        switch (args[0].toLowerCase())
        {
            case "reload":
            {
                if (!sender.hasPermission("loginmessages.reload"))
                {
                    sender.sendMessage(Messages.MSG_NO_PERMS);
                    return true;
                }
                try
                {
                    plugin.isConfigOutOfDate();
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
            default:
                return false;
        }
    }
}