package com.daxue.auth;

/**
 * @author daxue0929
 * @date 2022/8/2
 */

public class SyncTest {
    public synchronized void showA() {
        System.out.println("showA...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void showB() {
        synchronized (this) {
            System.out.println("showB...");
        }
    }
}
