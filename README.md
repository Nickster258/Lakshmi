# IRCBot

IRCBot.java is a simple IRC bot library I made so as to help making IRC bots easier for users. The IRC bot library, however, does not house a "keep alive" block. That block is stored in the nick_bot.java.

I use Properties to import variables I need from the settings.properties file and set such variables to the IRCBot using my setter method.

Note: Currently this bot works only if you have a registered account, because authentication is a pain.

# nick_bot

This is my custom IRC bot project. It is capable of doing a few things at the moment including send Slack messages, shorten URLs, and more.

- [ ] Implement mathematical functions (`calc 1*1)
- [X] Finalize URL shortener
- [X] Post Slack messages
- [ ] Figure out a way to handle custom methods for a command
- [ ] Add a proper commandHandler system including trigger events
- [ ] Add a plugin system

If you have any more ideas on what I should add, just tell me!
