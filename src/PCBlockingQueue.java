import java.util.concurrent.*;

// BlockingQueue实现生产者和消费者
public class PCBlockingQueue {
    public static void main(String[] args){
        BlockingQueue<Integer> bq = new ArrayBlockingQueue<>(5);
        Runnable producer = () -> {
            for(int i = 0; i < 10; i++){
                try {
                    bq.put(i);
                    System.out.println(Thread.currentThread().getName() + " produced: " + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Runnable consumer = () -> {
            for(int i = 0; i < 10; i++){
                try {
                    bq.take();
                    System.out.println(Thread.currentThread().getName() + " consumed: " + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        ExecutorService pool = Executors.newFixedThreadPool(5);
        pool.submit(producer);
        pool.submit(consumer);
    }
}
