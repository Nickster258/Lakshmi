package com.nickster258.nick_bot.parsers;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandParser {

  String raw;
  String service;
  String user;
  String command;
  String postCommandRaw;
  ArrayList<String> postCommand = new ArrayList<String> ();
  boolean pm;

  public CommandParser (String raw) {
    this.raw = raw;
    this.pm = !raw.contains("#");
    String temp = raw.replaceAll("[\\p{Cntrl}\\p{Cc}]","");
    if (raw.startsWith("OREBuild",1)) {
      parseServer(temp);
    } else if (raw.startsWith("ORESchool",1)) {
      parseServer(temp);
    } else if (raw.startsWith("ORESurvival",1)) {
      parseServer(temp);
    } else if (raw.startsWith("ORESkyblock",1)) {
      parseServer(temp);
    } else {
      parseIRC(temp);
    }
  }

  public void parseServer (String temp) {
    this.service = temp.substring(1, temp.indexOf("!"));
    temp = temp.substring(temp.indexOf(":", 1) + 3);
    this.user = temp.substring(0, temp.indexOf(":"));
    temp = temp.substring(temp.indexOf(" ") + 2);
    assembleCommand(temp);
  }

  public void parseIRC (String temp) {
    this.service = "IRC";
    temp = temp.substring(temp.indexOf(":") + 1);
    this.user = temp.substring(0, temp.indexOf("!"));
    temp = temp.substring(temp.indexOf(":") + 2);
    assembleCommand(temp);
  }

  public void assembleCommand (String temp) {
    this.postCommandRaw = "NULL";
    if (temp.indexOf(" ") != -1) {
      temp = temp.substring(temp.indexOf("`") + 1);
      if (temp.indexOf(" ") != -1) {
        this.command = temp.substring(0, temp.indexOf(" "));
      } else {
        this.command = temp.substring(0);
      }
      this.postCommandRaw = temp.substring(temp.indexOf(" ") +1);
      this.postCommand = new ArrayList<String>(Arrays.asList(temp.substring(temp.indexOf(" ") +1).split(" ")));
    } else {
      this.command = temp;
    }
  }

  public String getRaw () {
    return raw;
  }

  public boolean isPM() {
    return pm;
  }

  public String getService () {
    return service;
  }

  public String getUser () {
    return user;
  }

  public String getCommand () {
    return command;
  }

  public String getPostCommandRaw () {
    return postCommandRaw;
  }

  public String getPostCommand (int index) {
    return postCommand.get(index);
  }

  public String toString () {
    return "Command(isPM=" + pm + ", " + service + ", " + user + ", " + command + ", " + postCommand.toString() + ")";
  }

  public static void main (String[] args) {
  }
}
