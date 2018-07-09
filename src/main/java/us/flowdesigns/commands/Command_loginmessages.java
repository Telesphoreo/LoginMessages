package us.flowdesigns.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import us.flowdesigns.loginmessages.LoginMessages;
import us.flowdesigns.utils.NLog;

@CommandPermissions(source = SourceType.BOTH)
@CommandParameters(description = "Shows information or reloads LoginMessages", usage = "/<command> [reload]")
public class Command_loginmessages extends BaseCommand
{
    @Override
    public boolean run(final CommandSender sender, final Player sender_p, final Command cmd, final String commandLabel, final String[] args, final boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.GOLD + "LoginMessages v1.0.0 by: handleDisconnect");
            sender.sendMessage(ChatColor.GREEN + "Type /loginmessages reload to reload the configuration file");
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
                    LoginMessages.plugin.reloadConfig();
                    sender.sendMessage(Messages.RELOADED);
                    return true;
                }
                catch (Exception ex)
                {
                    NLog.severe(ex);
                    sender.sendMessage(Messages.FAILED);
                }
            }
            default:
                return false;
        }
    }
}