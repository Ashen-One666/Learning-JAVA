class MySingleton {
    private static volatile MySingleton instance = null;
    private MySingleton(){}
    public static MySingleton getInstance(){
        if(instance == null){
            synchronized (MySingleton.class){
                if(instance == null){
                    instance = new MySingleton();
                }
            }
        }
        System.out.println("获取单例成功");
        return instance;
    }
}

public class Singleton {
    public static void main(String[] args){
        MySingleton instance = MySingleton.getInstance();
    }
}
