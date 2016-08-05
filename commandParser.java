public class commandParser {

  String raw;
  String service;
  String user;
  String command;
  String postCommand;

  public commandParser (String raw) {
    this.raw = raw;
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
    temp = temp.substring(temp.lastIndexOf("08")+2);
    this.user = temp.substring(0, temp.lastIndexOf(":"));
    temp = temp.substring(temp.indexOf(" ")+1);
    this.command = temp.substring(0, temp.indexOf(" "));
    this.postCommand = temp.substring(temp.indexOf(" ") + 1);
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
String test = ":ORESchool!ORESchool@97-89-20-163.dhcp.gwnt.ga.charter.com PRIVMSG #openredstone :08Nickster258: `staff sfskjndfskdjfns";
//    String test = ":ORESchool!ORESchool@97-89-20-163.dhcp.gwnt.ga.charter.com PRIVMSG #openredstone :08Reewass_Squared: `staff like 0.1 is 1/2";
    commandParser text = new commandParser(test);
    System.out.println(text.toString());
  }
}
