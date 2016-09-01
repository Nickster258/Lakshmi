package com.nickster258.lakshmi.assemblers;

import java.util.ArrayList;

public class Command {

  int id;
  String command;
  ArrayList<String> vals;

  public Command (int id, String command, ArrayList<String> vals) {
    this.id = id;
    this.command = command;
    this.vals = vals;
  }

  public Command (int id, String command) {
    this.id = id;
    this.command = command;
  }

  public int getID() {
    return id;
  }

  public String getCommand() {
    return command;
  }

  public ArrayList<String> getVals() {
    return vals;
  }

  public String toString () {
    return "Command(" + id + ", " + command + ", " + vals.toString() + ")";
  }
}
