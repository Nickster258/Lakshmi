import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class slackbot {

  public static void main (String[] args) {
    String slackURL = "https://openredstone.slack.com/services/hooks/slackbot?token=Boyu9ZtgpUAgoEi1851YRFCZ&channel=%23nick_bot";

    try {
      URL url = new URL(slackURL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");

      String input = "simple test 4";

      OutputStream stream = conn.getOutputStream();
      stream.write(input.getBytes());
      stream.flush();

      BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

      conn.disconnect();

    } catch (IOException e) {
      System.out.println(e);
    }
  }
}
