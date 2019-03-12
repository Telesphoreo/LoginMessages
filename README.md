## LoginMessages [![Build Status](https://travis-ci.org/Telesphoreo/LoginMessages.svg?branch=master)](https://travis-ci.org/Telesphoreo/LoginMessages)
LoginMessages is a simple plugin that allows you to create custom login messages for players. In the configuration section, there are two options: ranks and players. Ranks are permission-node based and are meant for more than one person to be added to. Color codes are supported, and you should replace %player% where you would want the player's name to go. The player section is username based. You only need to provide a message, and their username. Color codes are supported and %player% is where their name would go.
LoginMessages v1.5 is confirmed to run on Minecraft versions 1.8 to 1.13.2.

Commands and Permissions:
/loginmessages - Shows information and help about LoginMessages
/loginmessages reload - Reloads the configuration file - Required permission node: loginmessages.reload
/setloginmessage <message> - Set your login message in-game. Required permission node: loginmessages.setloginmessage
/deleteloginmessage - Delete your login message. Required permission node: loginmessages.deleteloginmessage

Configuration Options:
show_vanilla_messages: false (true/false) - Enables or disables the default "player joined the game" message (so the login message only shows)

Default Configuration File: https://raw.githubusercontent.com/Telesphoreo/LoginMessages/master/src/main/resources/config.yml
