package me.telesphoreo.commands;

import org.bukkit.ChatColor;

public class Messages
{
    static final String MSG_NO_PERMS = ChatColor.RED + "I'm sorry but you do not have permission to perform this command. Please contact the server administrator if you believe that this is in error.";
    static final String RELOADED = ChatColor.GRAY + "The configuration file was successfully reloaded!";
    static final String FAILED = ChatColor.RED + "The configuration file failed to reload.";
    static final String SPECIFY_PLAYER = ChatColor.RED + "Please specify a player.";
    static final String PLAYER_NOT_FOUND = ChatColor.RED + "Player not found.";
    static final String PROVIDE_A_MESSAGE = ChatColor.RED + "Please enter a new login message.";
}
