import java.util.ArrayList;

public class command {

  int id;
  String command;
  ArrayList<String> vals;

  public command (int id, String command, ArrayList<String> vals) {
    this.id = id;
    this.command = command;
    this.vals = vals;
  }

  public command (int id, String command) {
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
    return "command(" + id + ", " + command + ", " + vals.toString() + ")";
  }
}
