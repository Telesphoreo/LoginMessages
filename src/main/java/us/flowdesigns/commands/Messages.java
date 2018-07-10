package us.flowdesigns.commands;

import org.bukkit.ChatColor;

public class Messages {
    public static final String MSG_NO_PERMS = ChatColor.RED + "I'm sorry but you do not have permission to perform this command. Please contact the server administrator if you believe that this is in error.";
    public static final String RELOADED = ChatColor.GRAY + "The configuration file was successfully reloaded!";
    public static final String FAILED = ChatColor.RED + "There was an error reloading the configuration file.";
    public static final String UPDATE_FAILED = ChatColor.RED + "There was an error checking for an update.";
    public static final String LATEST_VERSION = ChatColor.GRAY + "You are using the latest version of LoginMessages.";
    public static final String UPDATE_FOUND = ChatColor.GRAY + "An update was found for LoginMessages. Download the latest release here: https://www.spigotmc.org/resources/loginmessages.58564/";
    public static final String DISABLED = ChatColor.GRAY + "The update checker has been disabled in the configuration file.";
}
