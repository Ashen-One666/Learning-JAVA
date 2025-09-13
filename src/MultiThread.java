import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// 基于synchronized 和 object的wait/notify 实现的多线程轮流打印
class WaitNotifyBasedPrinter {
    private final int n;
    private final int MAX;
    private int turn = 0; // 记录轮次
    private int num = 1; // 记录当前数字
    private Object lock = new Object();

    WaitNotifyBasedPrinter(int n, int MAX) {
        this.n = n;
        this.MAX = MAX;
    }

    public void print(int threadId) {
        while(true) {
            synchronized (lock) {
                try {
                    while(num <= MAX && threadId != turn)
                        lock.wait();

                    if(num > MAX) {
                        lock.notifyAll();
                        return;
                    }
                    System.out.println(Thread.currentThread().getName() + " -> " + num);
                    num++;
                    turn = (turn + 1) % n;
                    lock.notifyAll();

                }  catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " is interrupted");
                }
            }
        }
    }
}


// 基于 ReentrantLock 和 Condition 实现的多线程轮流打印
class ReentrantLockBasedPrinter {
    private final int n;
    private final int MAX;
    private int turn = 0; // 记录轮次
    private int num = 1; // 记录当前数字
    private final ReentrantLock lock = new ReentrantLock();
    private List<Condition> conds = new ArrayList<>();

    public ReentrantLockBasedPrinter(int n, int MAX) {
        this.n = n;
        this.MAX = MAX;
        for(int i = 0; i < n; i++)
            conds.add(lock.newCondition());
    }

    public void print(int threadId) {
        while(true) {
            lock.lock();
            try {
                // 没有轮到当前线程输出
                while(num <= MAX && threadId != turn)
                    conds.get(threadId).await();

                // 任务执行完毕，唤醒所有线程
                if(num > MAX){
                    for(Condition cond : conds) cond.signal();
                    return;
                }

                // 轮到当前线程输出
                System.out.println(Thread.currentThread().getName() + " -> " + num);
                num++;
                turn = (turn + 1) % n;
                // 按序唤醒下一个线程
                conds.get(turn).signal();
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " is interrupted");
            } finally {
                lock.unlock();
            }
        }
    }

}

public class MultiThread {
    public static void main(String[] args) {
        int n = 5;
        int MAX = 15;

        WaitNotifyBasedPrinter printer = new WaitNotifyBasedPrinter(n, MAX); // 基于wait/notify
        //ReentrantLockBasedPrinter printer = new ReentrantLockBasedPrinter(n, MAX); // 基于可重入锁

        // 注意：线程池默认的线程名，编号从1开始
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for(int i = 0; i < n; i++){
            int finalI = i; // lambda表达式要求入参必须为final
            pool.submit(() -> printer.print(finalI));
        }
        pool.shutdown();
    }
}
