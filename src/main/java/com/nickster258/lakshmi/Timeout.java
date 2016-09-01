package com.nickster258.lakshmi;
import com.nickster258.lakshmi.assemblers.*;

public class Timeout extends Thread {
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        for (int i = 0; i < Main.PATREONS.size(); i++) {
          Users temp = Main.PATREONS.get(i);
          if (temp.timeout > 0) {
            temp.timeout--;
          }
          Main.PATREONS.remove(i);
          Main.PATREONS.add(temp);
        }
        Thread.sleep(12000);
      } catch (InterruptedException e) {
        System.out.println(e);
      }
    }
  }
}
