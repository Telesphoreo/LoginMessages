package me.telesphoreo.commands;

import java.util.Collections;
import java.util.List;
import me.telesphoreo.LoginMessages;
import me.telesphoreo.util.LoginMessagesBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class LoginMessagesCommand extends LoginMessagesBase implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args)
    {
        if (args.length == 0)
        {
            LoginMessages.BuildProperties build = LoginMessages.build;
            sender.sendMessage(plugin.mmDeserialize("<gold>LoginMessages is a lightweight plugin that allows players to have a custom join message."));
            sender.sendMessage(plugin.mmDeserialize("<gold>Version <blue>" + String.format("%s.%s.%s", build.version, build.number, build.head)));
            sender.sendMessage(plugin.mmDeserialize("<gold>Compiled on <blue>" + String.format("%s" + " <gold>by <blue>" + "%s", build.date, build.author)));
            sender.sendMessage(plugin.mmDeserialize("<green>Visit <aqua>https://github.com/Telesphoreo/LoginMessages <green>for more information."));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload"))
        {
            if (!sender.hasPermission("loginmessages.reload"))
            {
                sender.sendMessage(Messages.MSG_NO_PERMS);
                return true;
            }
            try
            {
                plugin.reloadConfig();
                sender.sendMessage(Messages.RELOADED);
                return true;
            }
            catch (Exception ex)
            {
                logger.severe(ex.toString());
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