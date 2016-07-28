import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.ArrayList;

public class bot {

  // Fetching global variables from "settings.properties"
  private static Properties settings = new Properties();
  static{
    try {
      InputStream input = new FileInputStream("settings.properties");
      settings.load(input);
      input.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }
  public static String getProperty (String key) {
    return settings.getProperty(key);
  }

  // Setting bot
  private static IRCBot bot = new IRCBot(settings.getProperty("server"), Integer.parseInt(settings.getProperty("port")), settings.getProperty("nick"), settings.getProperty("channel"), settings.getProperty("pass"));

  // Main
  public static void main(String[] args) {
    bot.connect();
    listener();
  }

  // Listener / stayAlive
  public static void listener () {
    String line = null;
    while ((line = bot.readLine( )) != null) {
      if (line.contains("PING")) {
        bot.sendRaw("PONG " + line.substring(5) + "\r\n");
      } else if (line.contains("hi bot")) {
        System.out.println(line);
        String send = "PRIVMSG " + settings.getProperty("channel") + " hi there.\r\n";
        bot.sendRaw(send);
        System.out.println("\nRECEIVED - " + line + "\nSENT - " + send);
      } else {
        System.out.println(line);
      }
    }
  }
}
