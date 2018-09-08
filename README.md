## LoginMessages
LoginMessages is a simple plugin that allows you to create custom login messages for players. In the configuration section, there are two options: ranks and players. Ranks are permission-node based and are meant for more than one person to be added. Color codes are supported, and replace %player% where you would want the player's name to go. The player section is username based. You only need to provide a message, and their username. Color codes are supported and %player% is where their name would go.
LoginMessages v1.3.1 is confirmed to run on Minecraft versions 1.8 to 1.13.1.

Commands and Permissions:
/loginmessages - Shows information and help about LoginMessages
/loginmessages update - Updates LoginMessages - Required permission node: loginmessages.update
/loginmessages reload - Reloads the configuration file - Required permission node: loginmessages.update

Configuration Options:
enable_updater: true (true/false) - Enabled or disabled the automatic update checking service
 show_vanilla_messages: false (true/false) - Enables or disables the default "player joined the game" message (so the login message only shows)

Default Configuration File: https://raw.githubusercontent.com/Telesphoreo/LoginMessages/master/src/main/resources/config.yml
