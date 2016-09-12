package com.nickster258.lakshmi.assemblers;

public class Users {

  String name;
  boolean OP = false;
  int ID;
  public int timeout;
  public int commandCount;

  public Users (int id, String name) {
    this.ID = id;
    this.timeout = 0;
    this.name = name;
  }

  public void setOp (boolean op) {
    this.OP = op;
  }

  public String getName () {
    return name;
  }

  public int getTimeout() {
    return timeout;
  }

  public String toString() {
    return "user(id=" + ID + ", " + "OP=" + OP + ", commandCount=" + commandCount + ", name=" + name + ", timeout=" + timeout + ")";
  }
}
