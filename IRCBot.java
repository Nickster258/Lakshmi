import java.util.Date;
import java.io.*;
import java.net.*;

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

      this.sendRaw("NICK " + username + "\r\n");
      this.sendRaw("USER " + username + " 8 * : IRC Bot\r\n");

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
        this.sendRaw("JOIN " + channel + "\r\n");
      }
      if (username!=null){
        this.sendRaw("PRIVMSG NICKSERV IDENTIFY " + password + "\r\n");
      }

    } catch (IOException e) {
      System.out.println(e);
    }
  }

  // New username
  public void nick (String username) {
    this.username = username;
    this.sendRaw("NICK " + username + "\r\n");
  }

  // New password
  public void pass (String password) {
    this.password = password;
    this.sendRaw("USER " + password + " 8 * : \r\n");
  }

  // New channel
  public void join (String channel) {
    this.channel = channel;
    this.sendRaw("JOIN " + channel + "\r\n");
  }

  // Leave channel
  public void leave () {
    this.sendRaw("PART\r\n");
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
    } catch (IOException e) {
      System.out.println(e);
    }
    return blah;
  }

  // Send raw message
  public void sendRaw (String line) {
    try {
      this.writer.write(line);
      this.writer.flush();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  // Send to server
  public void sendUser (String user, String line) {
    this.sendRaw("PRIVMSG " + user + " " + line + "\r\n");
  }

  public static void main (String[] args) {
  }
}
