public class Test{
    public static void main(String[] args) throws Exception{
        Resource res = new Resource();
        new Thread(new Producer(res)).start();
        new Thread(new Consumer(res)).start();
    }
}
//生产者
class Producer implements Runnable{
    private Resource resource;
    public Producer(Resource resource) {
        this.resource = resource;
    }
    @Override
    public void run() {
        for (int x = 0; x < 50; x++) {
            try {
                this.resource.make();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
//消费者
class Consumer implements Runnable{
    private Resource resource;
    public Consumer(Resource resource) {
        this.resource = resource;
    }
    @Override
    public void run() {
        for (int x = 0; x < 50; x++) {
            try {
                this.resource.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}

class Resource {
    private Computer computer;
    private boolean flag = true;//标记
    public synchronized void make() throws Exception {
        if(this.computer != null) {//已经生产过了
            super.wait();
        }
        Thread.sleep(100);
        this.computer = new Computer("MLDB电脑", 3500.0);
        System.out.println("【生产电脑】"+this.computer);
        super.notifyAll();
    }
    public synchronized void get() throws Exception {
        if(this.computer == null) {//没有生产过
            super.wait();
        }
        Thread.sleep(10);
        System.out.println("【取走电脑】"+this.computer);
        System.out.println(this.computer);
        this.computer = null;
        super.notify();
    }
}
//商品
class Computer{
    private static int count = 0;//表示生产的个数
    private String name;
    private double price;
    public Computer(String name,double price) {
        this.name = name;
        this.price = price;
        this.count++;
    }
    public String toString() {
        return "【第"+count+"台电脑】"+"电脑名字："+this.name+"价值："+this.price;
    }
}