public class Sample {

  public static void main(String[] args) {
    String server = "irc.your.domain";
    int port = 6667; // Your port
    String nick = "yournick";
    String channel = "#yourchannel";
    IRCBot BOT = new IRCBot(server, port, nick, channel);
    BOT.connect();

    String line = null;
    while ((line = BOT.readLine()) != null) {
      if (line.indexOf("PING") == 0) {                      // DO NOT REMOVE - Keep alive statement
        BOT.sendRaw("PONG " + line.substring(5));
      } else if (line.contains("blah")) {                   // What your bot is listening to
        BOT.sendRaw("PRIVMSG " + channel + " ahk...");      // What your bot responds with
      } else {
        System.out.println(line);                           // If prior conditions are not met, print (not necessary)
      }
    }
  }
}

