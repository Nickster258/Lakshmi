package com.nickster258.nick_bot.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;

public class IRCBot {

  String server;
  int port;
  String channel;
  String username;
  String password;
  BufferedWriter writer;
  BufferedReader reader;
  Socket socket;

  // Constructors
  public IRCBot () {
  }

  public IRCBot (String server, int port, String username) {
    this.server = server;
    this.port = port;
    this.username = username;
  }

  public IRCBot (String server, int port, String username, String channel) {
    this(server, port, username);
    this.channel = channel;
  }

  public IRCBot (String server, int port, String username, String channel, String password) {
    this(server, port, username, channel);
    this.password = password;
  }

  // Getters
  public String getServer() {
    return server;
  }

  public int getPort() {
    return port;
  }

  public String getUsername() {
    return username;
  }

  public String getChannel() {
    return channel;
  }

  // Connection initialization
  public void connect () {
    try {
      this.socket = new Socket(server, port);
      this.writer = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream( )));
      this.reader = new BufferedReader( new InputStreamReader(  socket.getInputStream(  )));

      sendRaw("NICK " + username);
      sendRaw("USER " + username + " 8 * : nick_bot");

      String line = null;
      while ((line = this.readLine( )) != null) {
        if (line.indexOf("004") >= 0) {
          break;
        } else if (line.indexOf("433") >= 0) {
          System.out.println("Nickname is already in use.");
          return;
        }
      }

      if (channel!=null){
        sendRaw("JOIN " + channel);
      }
      if (username!=null){
        sendRaw("PRIVMSG NICKSERV IDENTIFY " + password);
      }

    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public void disconnect () {
    sendRaw("QUIT");
    sendRaw("LEAVE");
    sendRaw("PART");
    sendRaw("DISCONNECT");
  }

  // New username
  public void nick (String username) {
    this.username = username;
    sendRaw("NICK " + username);
  }

  // New password
  public void pass (String password) {
    this.password = password;
    sendRaw("USER " + password + " 8 * : ");
  }

  // New channel
  public void join (String channel) {
    this.channel = channel;
    sendRaw("JOIN " + channel);
  }

  // Leave channel
  public void leave () {
    this.sendRaw("PART");
  }

  // Leave and join channel
  public void change (String channel) {
    this.channel = channel;
    this.leave();
    this.join(channel);
  }

  // Read raw message
  public String readLine () {
    String blah = null;
    try {
      blah = this.reader.readLine();
    } catch (Exception e) {
      System.out.println(e);
    }
    blah = blah.replaceAll("[\\p{Cntrl}\\p{Cc}]","");
    return blah;
  }

  // Send raw message
  public void sendRaw (String line) {
    try {
      writer.write(line + "\r\n");
      writer.flush();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public void write (String line) {
    try {
      writer.write(line + "\r\n");
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public void flush () {
    try {
      writer.flush();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  // Send to server
  public void sendUser (String user, String line) {
    this.sendRaw("PRIVMSG " + user + " " + line);
  }

  public String toString() {
    return "IRCBot(" + username + ", " + server + ", " + port + ", " + channel + ")";
  }

  public static void main (String[] args) {
 }
}
