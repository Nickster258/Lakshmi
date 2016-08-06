public class command {

  String command;
  String returnVal;

  public command (String command, String returnVal) {
    this.command = command;
    this.returnVal = returnVal;
  }

  public command (String command) {
    this.command = command;
  }

  public String toString () {
    return "command(" + command + ", " + returnVal + ")";
  }
}
