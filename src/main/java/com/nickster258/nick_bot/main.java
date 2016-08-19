package com.nickster258.nick_bot;
import com.nickster258.nick_bot.assemblers.*;
import com.nickster258.nick_bot.parsers.*;
import com.nickster258.nick_bot.handlers.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import java.net.InetAddress;

import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;

public class main {

  public static ArrayList <Command>     COMMANDS     = new ArrayList <Command>     ();
  public static ArrayList <Users>       PATREONS     = new ArrayList <Users>       ();
  public static ArrayList <String>      OPERATORS    = new ArrayList <String>      ();
  public static ArrayList <String>      IRC          = new ArrayList <String>      ();
  public static ArrayList <String>      OREBUILD     = new ArrayList <String>      ();
  public static ArrayList <String>      ORESCHOOL    = new ArrayList <String>      ();
  public static ArrayList <String>      ORESURVIVAL  = new ArrayList <String>      ();
  public static ArrayList <String>      ORESKYBLOCK  = new ArrayList <String>      ();
  public static ArrayList <String>      SERVERS      = new ArrayList <String>      ();

  // Fetching global variables from "settings.properties"
  private static Properties SETTINGS = new Properties();
  private static void loadSettings () {
    try {
      InputStream input = new FileInputStream("../resources/settings.properties");
      SETTINGS.load(input);
      input.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }
  private static String getProperty (String key) {
    return SETTINGS.getProperty(key);
  }

  // Setting bot
  private static IRCBot BOT = new IRCBot();

  private static Timeout thread = new Timeout();

  private static void loadBot() {
    BOT = new IRCBot(SETTINGS.getProperty("server"), Integer.parseInt(SETTINGS.getProperty("port")), SETTINGS.getProperty("nick"), SETTINGS.getProperty("channel"), SETTINGS.getProperty("pass"));
  }

  // Main
  public static void main(String[] args) {
    loadSettings();
    assembleOPs();
    assembleCommands();
    thread.start();
    loadBot();
    BOT.connect();
    listener();
  }

  public static void resetAntispam() {
    PATREONS.clear();
  }

  // KeepAlive
  public static void keepAlive(String line) {
    BOT.sendRaw("PONG " + line.substring(5));
  }

  public static boolean containsCommand(String line) {
    if (line.contains("`")) {
      String temp = line.substring(line.indexOf("`")+1);
      for (int i = 0; i<COMMANDS.size(); i++) {
        Command comm = COMMANDS.get(i);
        if (comm.getCommand().equals(temp)) {
          return true;
        }
      }
    }
    return false;
  }

  public static ArrayList<String> getVals(String command) {
    for (int i = 0; i<COMMANDS.size(); i++) {
      Command comm = COMMANDS.get(i);
      if (comm.getCommand().equals(command)) {
        return comm.getVals();
      }
    }
    return OPERATORS;
  }

  public static boolean isOP(String name) {
    if (OPERATORS.contains(name)) {
      return true;
    }
    return false;
  }

  public static boolean canSpeak(String name) {
    boolean found = false;
    if (isOP(name)) {
      return true;
    }
    for (int i = 0; i < PATREONS.size(); i++) {
      Users temp = PATREONS.get(i);
      if (temp.getName().equals(name)) {
        temp.timeout++;
        PATREONS.remove(i);
        PATREONS.add(temp);
        found = true;
      }
    }

    if (!found) {
      Users temp = new Users(name);
      temp.timeout++;
      PATREONS.add(temp);
    }

    for (int i = 0; i < PATREONS.size(); i++) {
      Users temp = PATREONS.get(i);
      if ((temp.getName().equals(name)) && (temp.timeout < 6 )) {
        return true;
      }
    }
    return false;
  }

  public static void reload() {
    BOT.disconnect();
    resetAntispam();
    loadSettings();
    assembleOPs();
    assembleCommands();
    loadBot();
    BOT.connect();
  }

  // Listener
  public static void listener() {
    String line = null;
    while ((line = BOT.readLine( )) != null) {
      if (line.contains("PING")) {
        keepAlive(line);

      // Basic COMMANDS
      } else if (containsCommand(line)) {
        CommandParser comm = new CommandParser(line);
        if (canSpeak(comm.getUser())) {
          ArrayList<String> vals = getVals(comm.getCommand());
          for (int i = 0; i < vals.size(); i++) {
            sendUser(comm.getService(), comm.getUser(), vals.get(i));
          }
        } else {
          sendUser(comm.getService(), comm.getUser(), "You have exceeded your timeout!");
        }
        System.out.println("COMMAND EXECUTED: " + comm.toString());

      // Complicated COMMANDS
      } else if (line.contains("http://") || line.contains("https://")) {
        if (!line.contains("#")) {
          LineParser temp = new LineParser(line);
          System.out.println(temp.toString());
          if (shorten(line) != null) {
            sendUser(temp.getService(), temp.getUser(), shorten(line));
          }
        }

      } else if (line.contains("`uuid")) {
        CommandParser comm = new CommandParser(line);
        if (canSpeak(comm.getUser())) {
          if (comm.getPostCommandRaw().equals("NULL")) {
            sendUser(comm.getService(), comm.getUser(), uuid(comm.getUser()));
          } else {
            sendUser(comm.getService(), comm.getUser(), uuid(comm.getPostCommand(0)));
          }
        } else {
          sendUser(comm.getService(), comm.getUser(), "You have exceeded your timeout!");
        }
        System.out.println("COMMAND EXECUTED: " + comm.toString());

      } else if (line.contains("`status")) {
        CommandParser comm = new CommandParser(line);
        if (comm.getPostCommandRaw().equals("NULL")) {
          sendUser(comm.getService(), comm.getUser(), "You have to define a server!");
        } else {
          if (canSpeak(comm.getUser())) {
            sendUser(comm.getService(), comm.getUser(), status(comm.getPostCommand(0)));
          } else {
            sendUser(comm.getService(), comm.getUser(), "You have exceeded your timeout!");
          }
        }
        System.out.println("COMMAND EXECUTED: " + comm.toString());

      } else if (line.contains("`urban")) {
        CommandParser comm = new CommandParser(line);
        if (comm.getPostCommandRaw().equals("NULL")) {
          sendUser(comm.getService(), comm.getUser(), "You have to define a variable!");
        } else {
          if (canSpeak(comm.getUser())) {
            sendUser(comm.getService(), comm.getUser(), urban(comm.getPostCommand(0)));
          } else {
            sendUser(comm.getService(), comm.getUser(), "You have exceeded your timeout!");
          }
        }
        System.out.println("COMMAND EXECUTED: " + comm.toString());

      } else if (line.contains("`define")) {
        CommandParser comm = new CommandParser(line);
        if (comm.getPostCommandRaw().equals("NULL")) {
          sendUser(comm.getService(), comm.getUser(), "You have to define a variable!");
        } else {
          if (canSpeak(comm.getUser())) {
            sendUser(comm.getService(), comm.getUser(), define(comm.getPostCommand(0)));
          } else {
          sendUser(comm.getService(), comm.getUser(), "You have exceeded your timeout!");
          }
        }
        System.out.println("COMMAND EXECUTED: " + comm.toString());

      } else if (line.contains("`commands")) {
        CommandParser comm = new CommandParser(line);
        String commList = "Commands: ";
        for (int i = 0; i < COMMANDS.size(); i++) {
          Command temp = COMMANDS.get(i);
          commList = commList.concat(temp.getCommand() + " ");
        }
        sendUser(comm.getService(), comm.getUser(), commList);
        sendUser(comm.getService(), comm.getUser(), "Complex commands (*OP required): urban define staff status uuid sudo* reload* quit*");
        System.out.println("COMMAND EXECUTED: " + comm.toString());

      /*} else if (line.contains("`list")) {
        commandParser comm = new commandParser(line);
        if (canSpeak(comm.getUser())) {
          assembleUsers();
          sendUser(comm.getService(), comm.getUser(), OREBUILD.toString());
          sendUser(comm.getService(), comm.getUser(), ORESCHOOL.toString());
          sendUser(comm.getService(), comm.getUser(), ORESURVIVAL.toString());
          sendUser(comm.getService(), comm.getUser(), ORESKYBLOCK.toString());
          sendUser(comm.getService(), comm.getUser(), IRC.toString());
        } else {
          sendUser(comm.getService(), comm.getUser(), "You have exceeded your timeout!");
        }
        System.out.println("COMMAND EXECUTED: " + comm.toString());*/

      } else if (line.contains("`staff")) {
        CommandParser comm = new CommandParser(line);
        if (comm.getPostCommandRaw() != null) {
          postSlack("<!channel> " + comm.getUser() + " (" + comm.getService() + "): " + comm.getPostCommandRaw());
        } else {
          sendUser(comm.getService(), comm.getUser(), "Please include a statement!");
        }
        System.out.println("COMMAND EXECUTED: " + comm.toString());

      // OP only commands
      } else if (line.contains("`reload")) {
        CommandParser comm = new CommandParser(line);
        if (isOP(comm.getUser())) {
          reload();
        } else {
          sendUser(comm.getService(), comm.getUser(), "You are not authorized!");
        }
        System.out.println("COMMAND EXECUTED: " + comm.toString());

      } else if (line.contains("`sudo")) {
        CommandParser comm = new CommandParser(line);
        if (isOP(comm.getUser())) {
          BOT.sendRaw(comm.getPostCommandRaw());
        } else {
          sendUser(comm.getService(), comm.getUser(), "You are not authorized!");
        }
        System.out.println("COMMAND EXECUTED: " + comm.toString());

      } else if (line.contains("`quit")) {
        CommandParser comm = new CommandParser(line);
        if (isOP(comm.getUser())) {
          quit();
          break;
        } else {
          sendUser(comm.getService(), comm.getUser(), "You are not authorized!");
        }
        System.out.println("COMMAND EXECUTED: " + comm.toString());
      } else {
        System.out.println(line);
      }
    }
  }

  public static void sendUser(String service, String user, String line) {
    if (service.equals("IRC")) {
      BOT.sendRaw("PRIVMSG " + user + " " + line);
    } else {
      BOT.sendRaw("PRIVMSG " + service + " /msg " + user + " " + line);
    }
  }

  // Method to gracefully shutdown the bot
  public static void quit() {
    BOT.sendRaw("QUIT Time for me to head out!");
    thread.interrupt();
  }

  // Generates the list of users across IRC and the servers
  public static void assembleUsers() {
    SERVERS.clear();
    String line = null;

    BOT.sendRaw("PRIVMSG OREBuild /list");
    if ((line = BOT.readLine( )) != null) {
      if (line.contains("No such nick")) {
        OREBUILD.clear();
        OREBUILD.add("Server offline");
      } else {
        line.replaceAll("\\s+", "");
        OREBUILD = new ArrayList<String>(Arrays.asList(line.substring(line.lastIndexOf(": ") + 1).split(", ")));
      }
    }

    BOT.sendRaw("PRIVMSG ORESchool /list");
    if ((line = BOT.readLine( )) != null) {
      if (line.contains("No such nick")) {
        ORESCHOOL.clear();
        ORESCHOOL.add("Server offline");
      } else {
        line.replaceAll("\\s+", "");
        ORESCHOOL = new ArrayList<String>(Arrays.asList(line.substring(line.lastIndexOf(": ") + 1).split(", ")));
      }
    }

    BOT.sendRaw("PRIVMSG ORESurvival /list");
    if ((line = BOT.readLine( )) != null) {
      if (line.contains("No such nick")) {
        ORESURVIVAL.clear();
        ORESURVIVAL.add("Server offline");
      } else {
        line.replaceAll("\\s+", "");
        ORESURVIVAL = new ArrayList<String>(Arrays.asList(line.substring(line.lastIndexOf(": ") + 1).split(", ")));
      }
    }

    BOT.sendRaw("PRIVMSG ORESkyblock /list");
    if ((line = BOT.readLine( )) != null) {
      if (line.contains("No such nick")) {
        ORESKYBLOCK.clear();
        ORESKYBLOCK.add("Server offline");
      } else {
        line.replaceAll("\\s+", "");
        ORESKYBLOCK = new ArrayList<String>(Arrays.asList(line.substring(line.lastIndexOf(": ") + 1).split(", ")));
      }
    }

    BOT.sendRaw("NAMES " + SETTINGS.getProperty("channel") + "\r\n");
    if ((line = BOT.readLine( )) != null) {
      IRC = new ArrayList<String>(Arrays.asList(line.substring(line.lastIndexOf(":") + 1).split(" ")));
    }
  }

  // URL Shortener
  public static String shorten(String message) {
    String URL = message.substring(message.indexOf("http"));
    String domain = URL.substring(URL.indexOf("/") + 2);
    if (domain.indexOf("/") != -1) {
      domain = domain.substring(0, domain.indexOf("/"));
    }
    //System.out.println(domain);
    String shortenedURL = null;
/*    boolean reachable = false;
    try {
      reachable = InetAddress.getByName(domain).isReachable(500);
    } catch (Exception e) {
      System.out.println(e);
    }
    if (reachable) {*/
      try {
        if (URL.contains(" ")) {
          URL = URLEncoder.encode(URL.substring(0, URL.indexOf(" ")), "UTF-8");
        } else {
          URL = URLEncoder.encode(URL, "UTF-8");
        }
      } catch (Exception e) {
        System.out.println(e);
      }

      HttpURLConnection conn = null;
      try {
        URL url = new URL("https://api-ssl.bitly.com/v3/shorten?access_token=" + SETTINGS.getProperty("bitly") + "&longUrl=" + URL + "&format=txt");
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        shortenedURL = in.readLine();
        in.close();
      } catch (Exception e) {
        System.out.println(e);
      //}
    }
    return shortenedURL;
  }

  // Search things on Urban Dictionary
  public static String urban(String word) {
    String temp = "No definitions found for " + word;
    HttpsURLConnection conn = null;
    try {
      URL url = new URL("https://mashape-community-urban-dictionary.p.mashape.com/define?term=" + word);
      conn = (HttpsURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestMethod("GET");
      conn.setRequestProperty("X-Mashape-Key", SETTINGS.getProperty("mashape"));
      conn.setRequestProperty("Accept", "text/plain");

      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line = in.readLine();
      if (line.contains("\"definition\":")) {
        temp = line.substring(line.indexOf("\"definition\":") + 14, line.indexOf(",\"permalink\"") - 1);
        if (temp.length() > 150) {
          temp = temp.substring(0, 150) + "...";
        }
        temp = temp.replace("\\\"", "\"");
        temp = temp.replace("\\r", " ");
        temp = temp.replace("\\n", ". ");
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return word + ": " + temp;
  }

  public static String define(String word) {
    String temp = "No definitions found for " + word;
    HttpURLConnection conn = null;
    try {
      URL url = new URL("http://www.dictionaryapi.com/api/v1/references/collegiate/xml/" + word + "?key=" + SETTINGS.getProperty("dictionary"));
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line = null;
      while((line = in.readLine()) != null) {
        if (line.contains("entry id")) {
          String type = line.substring(line.indexOf("<fl>") + 4, line.indexOf("</", line.indexOf("<fl>") + 5));
          String definition = line.substring(line.indexOf("<dt>") + 4, line.indexOf("</dt>", line.indexOf("<dt>") + 4));
          temp = type + ": " + definition.replaceAll("<[^>]+>", "");
          break;
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return word + ": " + temp;
  }

  public static String uuid(String name) {
    String temp = "UUID not found";
    HttpURLConnection conn = null;
    try {
      URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line = null;
      while((line = in.readLine()) != null) {
        if (!line.contains("Not Found")) {
          temp = line.substring(7, line.indexOf("\"", 10));
        }
        break;
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return temp;
  }


  public static String status(String server) {
    String temp = server + " not found";
    HttpURLConnection conn = null;
    try {
      URL url = new URL("http://status.openredstone.org/" + server + ".php");
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line = null;
      while((line = in.readLine()) != null) {
        if (!line.contains("File not found")) {
          temp = line;
        }
        break;
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return temp;
  }


  // Simple method to send a Slack message to the specified channel
  public static void postSlack(String message) {
    String input = "payload={\"channel\": \"#botspam\", \"username\": \"nick_bot\", \"text\": \"" + message.replaceAll(SETTINGS.getProperty("allowedChars"), " ") + "\", \"icon_emoji\": \":robot_face:\"}";

    HttpsURLConnection conn = null;
    try {
      URL url = new URL(SETTINGS.getProperty("slackURL"));
      conn = (HttpsURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestMethod("POST");

      OutputStream stream = conn.getOutputStream();
      stream.write(input.getBytes());
      stream.flush();

      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
      }

      conn.disconnect();

    } catch (MalformedURLException e) {
      System.out.println(e);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  // Assembles all operators from operators.txt
  public static void assembleOPs () {
    try {
      OPERATORS.clear();
      String operator;
      BufferedReader in = new BufferedReader(new FileReader("../resources/operators.txt"));

      while ((operator = in.readLine()) != null) {
          OPERATORS.add(operator);
      }

      in.close();

      System.out.println("LOADED OPERATORS: " + OPERATORS.toString());

    } catch (IOException e) {
      System.out.println(e);
    }
  }

  // Assembles all commands from commands.txt
  public static void assembleCommands () {
    try {
      String command;
      BufferedReader in = new BufferedReader(new FileReader("../resources/commands.txt"));
      int id = 0;
      COMMANDS.clear();
      while ((command = in.readLine()) != null) {
        if (!command.startsWith("#") && command.length()>2) {
          id++;
          String com = command.substring(0, command.indexOf("="));
          ArrayList<String> val = new ArrayList<String>(Arrays.asList(command.substring(command.indexOf("=") +1).split(", ")));
          Command comm = new Command(id, com, val);
          COMMANDS.add(comm);
          System.out.println("LOADED COMMAND: " + comm.toString());
        }
      }

      in.close();

    } catch (Exception e) {
      System.out.println(e);
    }
  }
}

