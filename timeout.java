public class timeout extends Thread {
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        for (int i = 0; i < nick_bot.patreons.size(); i++) {
          users temp = nick_bot.patreons.get(i);
          if (temp.timeout > 0) {
            temp.timeout--;
          }
          nick_bot.patreons.remove(i);
          nick_bot.patreons.add(temp);
        }
        Thread.sleep(12000);
      } catch (InterruptedException e) {
        System.out.println(e);
      }
    }
  }
}
