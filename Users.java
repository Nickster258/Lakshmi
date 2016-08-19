public class Users {

  int timeout;
  String name;

  public Users (String name) {
    this.timeout = 0;
    this.name = name;
  }

  public String getName () {
    return name;
  }

  public int getTimeout() {
    return timeout;
  }

  public String toString() {
    return "user(" + name + ", " + timeout + ")";
  }
}
