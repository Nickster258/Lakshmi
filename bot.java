import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;

public class bot {

  private ArrayList <String> userIRC = new ArrayList <String> ();
  private static ArrayList <String> userBuild;
  private static ArrayList <String> userSchool;
  private static ArrayList <String> userSurvival;
  private static ArrayList <String> userSkyblock;

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
  public static void listener() {
    String line = null;
    while ((line = bot.readLine( )) != null) {
      if (line.contains("PING")) {
        bot.sendRaw("PONG " + line.substring(5) + "\r\n");
      } else if (line.contains("`users")) {
        System.out.println(line);
        assembleUsers();
        System.out.println("\nBuild - " + userBuild.toString() + "\nSchool - " + userSchool.toString() + "\nSurvival - " + userSurvival.toString());
      } else {
        System.out.println(line);
      }
    }
  }

  public static boolean isOnline(String user) {
    bot.sendRaw("WHOIS " + user + "\r\n");
    String line = bot.readLine();
    if (line.contains("401 " + settings.getProperty("nick"))) {
      return false;
    }
    return true;
  }

  public static void assembleUsers() {
    if (isOnline("OREBuild")) {
      bot.sendUser("OREBuild", "/list");
      String line = null;
      while ((line = bot.readLine()) != null) {
        if (line.contains("PRIVMSG " + settings.getProperty("nick"))) {
          String[] list = (line.substring(line.lastIndexOf(": ") + 1)).split(", ");
          userBuild = new ArrayList<String>(Arrays.asList(list));
          break;
        }
      }
    }

    if (isOnline("ORESchool")) {
      bot.sendUser("ORESchool", "/list");
      String line = null;
      while ((line = bot.readLine()) != null) {
        if (line.contains("PRIVMSG " + settings.getProperty("nick"))) {
          String[] list = (line.substring(line.lastIndexOf(": ") + 1)).split(", ");
          userSchool = new ArrayList<String>(Arrays.asList(list));
          break;
        }
      }
    }

    if (isOnline("ORESurvival")) {
      bot.sendUser("ORESurvival", "/list");
      String line = null;
      while ((line = bot.readLine()) != null) {
        if (line.contains("PRIVMSG " + settings.getProperty("nick"))) {
          String[] list = (line.substring(line.lastIndexOf(": ") + 1)).split(", ");
          userSurvival = new ArrayList<String>(Arrays.asList(list));
          break;
        }
      }
    }

/*
    if (isOnline("ORESkyblock")) {
      bot.sendUser("ORESkyblock", "/list");
      String line = null;
      while ((line = bot.readLine()) != null) {
        if (line.contains("PRIVMSG " + settings.getProperty("nick"))) {
          String[] list = (line.substring(line.lastIndexOf(": ") + 1)).split(", ");
          userSkyblock = new ArrayList<String>(Arrays.asList(list));
          break;
        }
      }
    }
*/
    bot.sendRaw("NAMES\r\n");
    System.out.println(bot.readLine());
  }
}
