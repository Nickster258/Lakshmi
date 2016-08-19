package com.nickster258.nick_bot.parsers;

public class LineParser {

  String raw;
  String service;
  String user;
  String msg;
  boolean pm;
  boolean status;

  public LineParser (String raw) {
    this.raw = raw;
    this.pm = !raw.contains("#");
    String temp = raw.replaceAll("[\\p{Cntrl}\\p{Cc}]","");
    if (raw.startsWith("shinobi.nonemu.ninja", 1) || raw.startsWith("services.nonemu.ninja", 1) || raw.startsWith("NickServ", 1) || raw.startsWith("ChanServ", 1)) {
      this.status = true;
    } else if (raw.startsWith("OREBuild",1)) {
      parseLine(raw);
    } else if (raw.startsWith("ORESchool",1)) {
      parseLine(raw);
    } else if (raw.startsWith("ORESurvival",1)) {
      parseLine(raw);
    } else if (raw.startsWith("ORESkyblock",1)) {
      parseLine(raw);
    } else {
      parseLineIRC(raw);
    }
  }

  public void parseLine (String temp) {
    this.service = temp.substring(1, temp.indexOf("!"));
    temp = temp.substring(temp.indexOf(":", 1)+3);
    this.user = temp.substring(0, temp.indexOf(":"));
    this.msg = temp.substring(temp.indexOf(" ")+1);
  }

  public void parseLineIRC (String temp) {
    this.service = "IRC";
    this.user = temp.substring(1,temp.indexOf("!"));
    this.msg = temp.substring(temp.indexOf(":", 2));
  }

  public String getRaw () {
    return raw;
  }

  public boolean isPM () {
    return pm;
  }

  public boolean status () {
    return status;
  }

  public String getService () {
    return service;
  }

  public String getUser () {
    return user;
  }

  public String getMSG () {
    return msg;
  }

  public String toString () {
    return "Line(isPM=" + pm + ", " + service + ", " + user + ", " + msg + ")";
  }

  public static void main (String[] args) {
  }
}
