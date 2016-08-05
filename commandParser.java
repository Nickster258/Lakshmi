public class commandParser {

  String raw;
  String service;
  String user;
  String command;
  String postCommand;

  public commandParser (String raw) {
    this.raw = raw.replaceAll("[^!:`A-Za-z0-9]", " ");
    String temp = raw.replaceAll("[^!:`A-Za-z0-9]", " ");
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
    this.service = temp.substring(0, temp.indexOf("!"));
    temp = temp.substring(temp.indexOf(":08") + 3);
    this.user = temp.substring(0, temp.lastIndexOf(":"));
    temp = temp.substring(temp.indexOf(" ") + 1);
    this.command = temp.substring(0, temp.indexOf(" "));
    this.postCommand = temp.substring(temp.indexOf(" "));
  }

  public void parseIRC (String temp) {
    this.service = "IRC";
    temp = temp.substring(temp.indexOf(":") + 1);
    this.user = temp.substring(0, temp.indexOf("!"));
    temp = temp.substring(temp.lastIndexOf(":") + 1);
    this.command = temp.substring(0, temp.indexOf(" "));
    this.postCommand = temp.substring(temp.indexOf(" "));
  }

  public String getRaw () {
    return raw;
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
    return "Command(" + service + ", " + user + ", " + command + ", " + postCommand + ")";
  }

  public static void main (String[] args) {
  }
}
