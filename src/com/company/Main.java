package com.company;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {

    static Object lock = new Object();

    public static void main(String[] args){

        Scanner scan = new Scanner(System.in);
        System.out.print("How many steps should we make? ");
        int steps = scan.nextInt();

        Thread left = new Thread(new Runnable() {
            public void run() {
                int k = steps % 2 == 0 ? steps / 2 : steps / 2 + 1; // Начинаем здесь с левой
                for (int i = 0; i < k; i++) {                       // поэтому при нечетном числе
                    synchronized (lock) {                           // шагов левая сделает лишний шаг
                        if (i == 0) System.out.print("Left");
                        else System.out.print(" Left");
                        try {
                            lock.notify();
                            if(i == k - 1) break;
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        Thread right = new Thread(new Runnable() {
            public void run() {

                for (int i = 0; i < steps/2; i++) {
                    synchronized (lock) {
                        System.out.print(" Right");
                        try {
                            lock.notify();
                            if(i == steps/2 - 1) break;
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        try {
            left.start();
            right.start();
            left.join();
            right.join();
            return;
        } catch (Exception e) {

        }
    }
}
