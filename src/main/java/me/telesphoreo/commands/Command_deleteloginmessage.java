package me.telesphoreo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.BOTH)
@CommandParameters(description = "Delete your login message.", usage = "/<command> <message>", aliases = "dellogin,deletelogin,delloginmessage")
public class Command_deleteloginmessage extends BaseCommand
{
    @Override
    public boolean run(final CommandSender sender, final Player playerSender, final Command cmd, final String commandLabel, final String[] args, final boolean senderIsConsole)
    {
        if (!sender.hasPermission("loginmessages.deleteloginmessage"))
        {
            sender.sendMessage(Messages.MSG_NO_PERMS);
            return true;
        }

        plugin.deleteLoginMessage(playerSender);
        return true;
    }
}