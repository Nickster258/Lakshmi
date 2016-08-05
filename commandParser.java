public class commandParser {

  String raw;
  String service;
  String user;
  String command;
  String postCommand;
  boolean pm;

  public commandParser (String raw) {
    this.raw = raw;
    this.pm = !raw.contains("#");
    String temp = raw;
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
    temp = temp.substring(temp.indexOf(":", 1)+4);
    this.user = temp.substring(0, temp.indexOf(":"));
    temp = temp.substring(temp.indexOf(" ")+1);
    assembleCommand(temp);
  }

  public void parseIRC (String temp) {
    this.service = "IRC";
    temp = temp.substring(temp.indexOf(":") + 1);
    this.user = temp.substring(0, temp.indexOf("!"));
    temp = temp.substring(temp.indexOf(":") + 1);
    assembleCommand(temp);
  }

  public void assembleCommand (String temp) {
    if (temp.indexOf(" ") != -1) {
      this.command = temp.substring(0, temp.indexOf(" "));
      this.postCommand = temp.substring(temp.indexOf(" ") + 1);
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

  public String getPostCommand () {
    return postCommand;
  }

  public String toString () {
    return "Command(isPM=" + pm + ", " + service + ", " + user + ", " + command + ", " + postCommand + ")";
  }

  public static void main (String[] args) {
  }
}
