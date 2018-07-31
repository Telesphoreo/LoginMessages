package us.flowdesigns.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.flowdesigns.loginmessages.LoginMessages;
import us.flowdesigns.loginmessages.Updater;
import us.flowdesigns.utils.NLog;

@CommandPermissions(source = SourceType.BOTH)
@CommandParameters(description = "Shows information about, reload, or update LoginMessages", usage = "/<command> [reload | update]")
public class Command_loginmessages extends BaseCommand
{
    @Override
    public boolean run(final CommandSender sender, final Player sender_p, final Command cmd, final String commandLabel, final String[] args, final boolean senderIsConsole)
    {
        String enabled = LoginMessages.plugin.getConfig().getString("enable_updater");
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.GOLD + plugin.getName() + " v" + plugin.getDescription().getVersion() + " by: Model_3");
            if (sender.hasPermission("loginmessages.reload"))
            {
                sender.sendMessage(ChatColor.GREEN + "Type /loginmessages reload to reload the configuration file");
            }
            if (sender.hasPermission("loginmessages.update") && enabled.equalsIgnoreCase("true"))
            {
                sender.sendMessage(ChatColor.GREEN + "Type /loginmessages update to update LoginMessages");
            }
            return true;
        }
        switch (args[0].toLowerCase())
        {
            case "update":
            {
                if (!sender.hasPermission("loginmessages.update"))
                {
                    sender.sendMessage(Messages.MSG_NO_PERMS);
                    return true;
                }
                if (enabled.equalsIgnoreCase("true"))
                {
                    Updater updater = new Updater(LoginMessages.plugin);
                    updater.update(sender);
                    return true;
                }
                else
                {
                    sender.sendMessage(Messages.DISABLED);
                }
                return true;
            }
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
                return true;
            }
            default:
                return false;
        }
    }
}