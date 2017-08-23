package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.List;

public class MainConcurrency {
    public static final int THREADS_NUMBER = 10000;
    private int counter;
    private static final Object LOCK = new Object();
    public static final Object Lock1 = new Object();
    public static final Object Lock2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());

        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ", " + getState());
                throw new IllegalStateException();
            }
        };
        thread0.start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState());
            }

            private void inc() {
                synchronized (this) {
//                    counter++;
                }
            }

        }).start();

        System.out.println(thread0.getState());

        final MainConcurrency mainConcurrency = new MainConcurrency();
        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);

        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    mainConcurrency.inc();
                    mainConcurrency.inc2();
                }
            });
            thread.start();
            threads.add(thread);
        }

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(mainConcurrency.counter);
    }

    private void inc() {
        synchronized (Lock1) {
            System.out.println("Thread 1: Holding lock 1...");
            counter++;

            try { Thread.sleep(10); }
            catch (InterruptedException e) {}
            System.out.println("Thread 1: Waiting for lock 2...");

            synchronized (Lock2) {
                System.out.println("Thread 1: Holding lock 1 & 2...");
                counter++;
//                wait();
//                readFile
//                ...
//        }
            }
        }
    }
    private void inc2() {
        synchronized (Lock2) {
            System.out.println("Thread 2: Holding lock 2...");
            counter++;

            try { Thread.sleep(10); }
            catch (InterruptedException e) {}
            System.out.println("Thread 2: Waiting for lock 1...");

            synchronized (Lock1) {
                System.out.println("Thread 2: Holding lock 1 & 2...");
                counter++;
//                wait();
//                readFile
//                ...
//        }
            }
        }
    }

}