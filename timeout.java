public class timeout extends Thread {
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        for (int i = 0; i < nick_bot.PATREONS.size(); i++) {
          users temp = nick_bot.PATREONS.get(i);
          if (temp.timeout > 0) {
            temp.timeout--;
          }
          nick_bot.PATREONS.remove(i);
          nick_bot.PATREONS.add(temp);
        }
        Thread.sleep(12000);
      } catch (InterruptedException e) {
        System.out.println(e);
      }
    }
  }
}
