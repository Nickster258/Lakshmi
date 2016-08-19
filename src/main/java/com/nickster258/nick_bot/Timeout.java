package com.nickster258.nick_bot;
import com.nickster258.nick_bot.Assemblers.*;

public class Timeout extends Thread {
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        for (int i = 0; i < main.PATREONS.size(); i++) {
          Users temp = main.PATREONS.get(i);
          if (temp.timeout > 0) {
            temp.timeout--;
          }
          main.PATREONS.remove(i);
          main.PATREONS.add(temp);
        }
        Thread.sleep(12000);
      } catch (InterruptedException e) {
        System.out.println(e);
      }
    }
  }
}
