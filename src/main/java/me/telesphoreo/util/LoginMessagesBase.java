package me.telesphoreo.util;

import java.util.logging.Logger;
import me.telesphoreo.LoginMessages;
import org.bukkit.Bukkit;
import org.bukkit.Server;

public class LoginMessagesBase
{
    protected LoginMessages plugin = LoginMessages.plugin;
    protected Server server = LoginMessages.server;
    protected Logger logger = Bukkit.getLogger();
}
