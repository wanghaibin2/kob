package com.kob.botRunningSystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author :王冰冰
 * @date : 2022/9/23
 */
public class BotPool extends Thread {

    private final static ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Queue<Bot> botQueue = new LinkedList<>();

    public void addBot(Integer userId, String botCode, String input) {
        lock.lock();
        try {
            botQueue.add(new Bot(userId, botCode, input));
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
    private void consume(Bot bot) {
        Consumer consumer = new Consumer();
        consumer.startTimeOut(2000, bot);
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (botQueue.isEmpty()) {
                try {
                    condition.await();  // 默认锁释放
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            } else {
                Bot bot = botQueue.remove();
                lock.unlock();
                consume(bot);  // 执行代码的过程比较慢
            }
        }
    }
}
