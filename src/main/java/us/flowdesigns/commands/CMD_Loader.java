package us.flowdesigns.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import us.flowdesigns.loginmessages.LoginMessages;
import us.flowdesigns.utils.NLog;
import us.flowdesigns.utils.NUtil;

import java.io.IOException;
import java.security.CodeSource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

// Credit to TF

public class CMD_Loader
{
    public static final Pattern COMMAND_PATTERN;
    private static final List<PC_CommandInfo> COMMAND_LIST;

    static
    {
        COMMAND_PATTERN = Pattern.compile(CMD_Handler.COMMAND_PATH.replace('.', '/') + "/(" + CMD_Handler.COMMAND_PREFIX + "[^\\$]+)\\.class");
        COMMAND_LIST = new ArrayList<>();
    }

    private CMD_Loader()
    {
        throw new AssertionError();
    }

    public static void scan()
    {
        CommandMap commandMap = getCommandMap();
        if (commandMap == null)
        {
            return;
        }
        COMMAND_LIST.clear();
        COMMAND_LIST.addAll(getCommands());

        COMMAND_LIST.stream().map((commandInfo) -> new PC_DynamicCommand(commandInfo)).forEach((dynamicCommand) ->
        {
            Command existing = commandMap.getCommand(dynamicCommand.getName());
            if (existing != null)
            {
                unregisterCommand(existing, commandMap);
            }

            commandMap.register(LoginMessages.plugin.getDescription().getName(), dynamicCommand);
        });
    }

    public static void unregisterCommand(String commandName)
    {
        CommandMap commandMap = getCommandMap();
        if (commandMap != null)
        {
            Command command = commandMap.getCommand(commandName.toLowerCase());
            if (command != null)
            {
                unregisterCommand(command, commandMap);
            }
        }
    }

    public static void unregisterCommand(Command command, CommandMap commandMap)
    {
        try
        {
            command.unregister(commandMap);
            HashMap<String, Command> knownCommands = getKnownCommands(commandMap);
            if (knownCommands != null)
            {
                knownCommands.remove(command.getName());
                command.getAliases().stream().forEach((alias) ->
                {
                    knownCommands.remove(alias);
                });
            }
        }
        catch (Exception ex)
        {
            NLog.severe(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static CommandMap getCommandMap()
    {
        final Object commandMap = NUtil.getField(Bukkit.getServer().getPluginManager(), "Nitrogen");
        if (commandMap != null)
        {
            if (commandMap instanceof CommandMap)
            {
                return (CommandMap)commandMap;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Command> getKnownCommands(CommandMap commandMap)
    {
        Object knownCommands = NUtil.getField(commandMap, "Nitrogen");
        if (knownCommands != null)
        {
            if (knownCommands instanceof HashMap)
            {
                return (HashMap<String, Command>)knownCommands;
            }
        }
        return null;
    }

    private static List<PC_CommandInfo> getCommands()
    {
        List<PC_CommandInfo> commandList = new ArrayList<>();

        try
        {
            CodeSource codeSource = LoginMessages.class.getProtectionDomain().getCodeSource();
            if (codeSource != null)
            {
                ZipInputStream zip = new ZipInputStream(codeSource.getLocation().openStream());
                ZipEntry zipEntry;
                while ((zipEntry = zip.getNextEntry()) != null)
                {
                    String entryName = zipEntry.getName();
                    Matcher matcher = COMMAND_PATTERN.matcher(entryName);
                    if (matcher.find())
                    {
                        try
                        {
                            Class<?> commandClass = Class.forName(CMD_Handler.COMMAND_PATH + "." + matcher.group(1));

                            CommandPermissions commandPermissions = commandClass.getAnnotation(CommandPermissions.class);
                            CommandParameters commandParameters = commandClass.getAnnotation(CommandParameters.class);

                            if (commandPermissions != null && commandParameters != null)
                            {
                                PC_CommandInfo commandInfo = new PC_CommandInfo(
                                        commandClass,
                                        matcher.group(1).split("_")[1],
                                        commandPermissions.source(),
                                        commandPermissions.blockHostConsole(),
                                        commandParameters.description(),
                                        commandParameters.usage(),
                                        commandParameters.aliases());

                                commandList.add(commandInfo);
                            }
                        }
                        catch (Exception ex)
                        {
                            NLog.severe(ex);
                        }
                    }
                }
            }
        }
        catch (IOException ex)
        {
            NLog.severe(ex);
        }

        return commandList;
    }

    public static class PC_CommandInfo
    {
        private final String commandName;
        private final Class<?> commandClass;
        private final SourceType source;
        private final boolean blockHostConsole;
        private final String description;
        private final String usage;
        private final List<String> aliases;

        public PC_CommandInfo(Class<?> commandClass, String commandName, SourceType source, boolean blockHostConsole, String description, String usage, String aliases)
        {
            this.commandName = commandName;
            this.commandClass = commandClass;
            this.source = source;
            this.blockHostConsole = blockHostConsole;
            this.description = description;
            this.usage = usage;
            this.aliases = ("".equals(aliases) ? new ArrayList<>() : Arrays.asList(aliases.split(",")));
        }

        public List<String> getAliases()
        {
            return Collections.unmodifiableList(aliases);
        }

        public Class<?> getCommandClass()
        {
            return commandClass;
        }

        public String getCommandName()
        {
            return commandName;
        }

        public String getDescription()
        {
            return description;
        }


        public SourceType getSource()
        {
            return source;
        }

        public String getUsage()
        {
            return usage;
        }

        public boolean getBlockHostConsole()
        {
            return blockHostConsole;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("commandName: ").append(commandName);
            sb.append("\ncommandClass: ").append(commandClass.getName());
            sb.append("\nsource: ").append(source);
            sb.append("\nblock_host_console: ").append(blockHostConsole);
            sb.append("\ndescription: ").append(description);
            sb.append("\nusage: ").append(usage);
            sb.append("\naliases: ").append(aliases);
            return sb.toString();
        }
    }

    public static class PC_DynamicCommand extends Command implements PluginIdentifiableCommand
    {
        private final PC_CommandInfo commandInfo;

        private PC_DynamicCommand(PC_CommandInfo commandInfo)
        {
            super(commandInfo.getCommandName(), "Admin command/Normal player command", commandInfo.getUsage(), commandInfo.getAliases());

            this.commandInfo = commandInfo;
        }

        @Override
        public boolean execute(CommandSender sender, String commandLabel, String[] args)
        {
            boolean success = false;

            if (!getPlugin().isEnabled())
            {
                return false;
            }

            try
            {
                success = getPlugin().onCommand(sender, this, commandLabel, args);
            }
            catch (Throwable ex)
            {
                throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + getPlugin().getDescription().getFullName(), ex);
            }

            if (!success && getUsage().length() > 0)
            {
                for (String line : getUsage().replace("<command>", commandLabel).split("\n"))
                {
                    sender.sendMessage(line);
                }
            }

            return success;
        }

        @Override
        public Plugin getPlugin()
        {
            return LoginMessages.plugin;
        }

        public PC_CommandInfo getCommandInfo()
        {
            return commandInfo;
        }
    }
}