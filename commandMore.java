import java.util.ArrayList;

public class commandMore extends command {

  String method;
  ArrayList<String> returnVals = new ArrayList<String> ();
  ArrayList<String> args = new ArrayList<String> ();

  public commandMore (String command, String method) {
    super (command);
    this.method = method;
  }

  public commandMore (String command) {
    super (command);
  }

  public void setArgs (ArrayList<String> args) {
    this.args = args;
  }

  public void setVals (ArrayList<String> returnVals) {
    this.returnVals = returnVals;
  }

  public String toString () {
    return "commandMore(" + command + ", " + method + ", " + returnVals.toString() + ", " + args.toString() + ")";
  }
}
