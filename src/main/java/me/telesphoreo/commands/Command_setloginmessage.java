package me.telesphoreo.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.BOTH)
@CommandParameters(description = "Set your login message in-game.", usage = "/<command> <message>", aliases = "slm,setlogin")
public class Command_setloginmessage extends BaseCommand
{
    @Override
    public boolean run(final CommandSender sender, final Player playerSender, final Command cmd, final String commandLabel, final String[] args, final boolean senderIsConsole)
    {
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
        plugin.setLoginMessage(playerSender, loginMsg.replace(sender.getName(), "%player%"));
        return true;
    }
}