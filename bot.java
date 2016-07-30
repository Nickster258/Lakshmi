import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;

public class bot {

  private static ArrayList <String> IRC = new ArrayList <String> ();
  private static ArrayList <String> OREBuild = new ArrayList <String> ();
  private static ArrayList <String> ORESchool = new ArrayList <String> ();
  private static ArrayList <String> ORESurvival = new ArrayList <String> ();
  private static ArrayList <String> ORESkyblock = new ArrayList <String> ();

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
        System.out.println("Build - " + OREBuild.toString() + "\nSchool - " + ORESchool.toString() + "\nSurvival - " + ORESurvival.toString() + "\nSkyblock - " + ORESkyblock.toString() + "\nIRC - " + IRC.toString());
      } else {
        System.out.println(line);
      }
    }
  }

  // Testing if IRC user is online, mainly for use with servers
  public static boolean isOnline(String user) {
    bot.sendRaw("ISON " + user + "\r\n");
    String line = bot.readLine();
    if (line.contains(":" + user)) {
      return true;
    }
    return false;
  }

  // Fetches the user list from the servers
  public static void assembleServerUsers(String server) {
    bot.sendUser(server, "/list");
    String line = null;
    while ((line = bot.readLine()) != null) {
      if (line.contains("PRIVMSG " + settings.getProperty("nick"))) {
        if (server == "OREBuild") {
          OREBuild = new ArrayList<String>(Arrays.asList(line.substring(line.lastIndexOf(": ") + 1).split(", ")));
          break;
        }
        if (server == "ORESchool") {
          ORESchool = new ArrayList<String>(Arrays.asList(line.substring(line.lastIndexOf(": ") + 1).split(", ")));
          break;
        }
        if (server == "ORESurvival") {
          ORESurvival = new ArrayList<String>(Arrays.asList(line.substring(line.lastIndexOf(": ") + 1).split(", ")));
          break;
        }
        if (server == "ORESkyblock") {
          ORESkyblock = new ArrayList<String>(Arrays.asList(line.substring(line.lastIndexOf(": ") + 1).split(", ")));
          break;
        }
      }
    }
  }

  // Generates the list of users across IRC and the servers
  public static void assembleUsers() {
    if (isOnline("OREBuild")) {
      assembleServerUsers("OREBuild");
    } else {
      OREBuild.clear();
      OREBuild.add("Server not online");
    }

    if (isOnline("ORESchool")) {
      assembleServerUsers("ORESchool");
    } else {
      ORESchool.clear();
      ORESchool.add("Server not online");
    }

    if (isOnline("ORESurvival")) {
      assembleServerUsers("ORESurvival");
    } else {
      ORESurvival.clear();
      ORESurvival.add("Server not online");
    }

    if (isOnline("ORESkyblock")) {
      assembleServerUsers("ORESkyblock");
    } else {
      ORESkyblock.clear();
      ORESkyblock.add("Server not online");
    }

    bot.sendRaw("NAMES " + settings.getProperty("channel") + "\r\n");
    String line = null;
    while ((line = bot.readLine()) != null) {
      if (line.contains("353 " + settings.getProperty("nick"))) {
        IRC = new ArrayList<String>(Arrays.asList(line.substring(line.lastIndexOf(":") + 1).split(" ")));
        break;
      }
    }
  }
}
