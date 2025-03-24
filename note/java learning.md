## JVM
### JVM内存模型
#### 类加载器

#### 方法区
- 作用：存储了每一个类的结构信息（例如运行时常量池、字段和方法数据、构造函数和普通方法的字节码内容）
#### 堆
- 作用：用来为类实例对象和数组分配内存。
- 分类：
  - 新生代（存瞬时对象
    细分为 Eden 和 两个 Survivor 区域（from Survivor 和 to Survivor）
  - 老年代（存长久对象）  
    
#### 栈
这是我们的代码运行空间。我们编写的每一个方法都会放到 栈 里面运行。
- 虚拟机栈
  - 用途：用于管理 Java 方法的调用
  - 过程：每当一个 Java 方法 被调用时，JVM 会为该方法分配一个 栈帧，栈帧包括：
    - 局部变量表：  
      方法中的局部变量，包括基本数据类型、对象引用。
    - 操作数栈：  
      用于存储操作指令的中间计算结果。
    - 方法调用和返回地址：  
      用于保存调用方法的位置，以便方法执行完毕后返回。
- 本地方法栈
  - 用途：用于管理 本地方法（非 Java 语言例如 C++） 的调用
  - 过程：Native 方法（本地方法）通常通过 JNI（java native interface） 来实现。
    可以认为是 Native 方法相当于 C/C++ 暴露给 Java 的一个接口， Java 通过调用这个接口从而调用到 C/C++ 方法。

#### 程序计数器
- 介绍：程序计数器是一个记录着当前线程所执行的字节码的行号指示器。
- 作用：多线程时，挂起的线程重新获取到时间片时，要想从被挂起的地方继续执行，就必须知道它上次执行到哪个位置，程序计数器可以帮助实现。
- 特性：和栈一样，都是 线程独享 的

#### 总结
- 方法区，堆：都是线程共享区域，有线程安全问题
- 栈和本地方法栈和计数器：都是独享区域，不存在线程安全问题

## 反射

### Class 对象
在 JVM 中，每个类在第一次被加载时，JVM 会为其创建一个 Class 对象， 用来封装指向方法区元信息的引用。  
这个 Class 对象存储在 堆内存 中，用来封装该类的元信息， 如类名、方法、字段、构造器等。  

### 反射操作过程
先访问 Class 对象，再根据Class 对象中的引用获取方法区中的元数据。

### 创建方法：
```java
// 全限定符获取
Class<?> clazz = Class.forName("com.example.MyClass");
// 直接获取
Class<?> clazz = MyClass.class;
// 调用对象的 getClass() 方法
Class<?> clazz = MyClass.getClass();
```

### 创建实例
```java
// 直接创建
Class<?> c = String.class;
Object str = c.newInstance();

// 获取指定的Constructor对象再创建
Class<?> c = String.class;
Constructor constructor = c.getConstructor(String.class);
Object obj = constructor.newInstance("23333");
```

### 获取方法
```java
// getDeclaredMethods方法返回类或接口声明的所有方法
public Method[] getDeclaredMethods() throws SecurityException;
// getMethods方法返回某个类的所有公用（public）方法
public Method[] getMethods() throws SecurityException;
// getMethod方法返回一个特定的方法（参数列表写方法名 和 参数名.class）
public Method getMethod(String name, Class<?>... parameterTypes);
```

### 获取构造器信息
```java
/* 通过Class类的getConstructor方法得到Constructor类的一个实例
   而Constructor类有一个newInstance方法可以创建一个对象实例 */
public T newInstance(Object ... initargs);
```

### 获取类的成员变量（字段）信息
```java
// getField：访问公有的成员变量
Field[] fields = clazz.getFields(); // 全部
// getDeclaredField：所有已声明的成员变量，但不能得到其父类的成员变量
Field[] declaredFields = clazz.getDeclaredFields(); // 全部
```

### 优缺点
优点：
#### 动态性和灵活性  
反射最大的优点是 动态性。通过反射，可以在运行时动态加载和操作类，而不需要在编译时确定类的具体信息。  
示例：可以动态加载第三方库或插件，而无需在代码中硬编码它们的类名和方法。
#### 提高代码的通用性
反射使得代码更具通用性，可以编写一些通用框架、工具类或注解处理器等。  
应用场景：
- 依赖注入（如 Spring）。
- ORM 框架（如 Hibernate）：根据实体类的字段和数据库表自动映射。
- 序列化/反序列化（如 JSON、XML 解析）。
#### 便于调试和开发工具
反射广泛用于开发工具和调试工具，例如：
- IDE 中的代码提示、代码补全。
- 测试框架（如 JUnit）利用反射调用测试方法。
- 注解处理（如解析自定义注解）。

缺点：
#### 性能开销
需要在运行时进行类型检查、方法查找和访问控制检查。
#### 破坏封装性
绕过访问修饰符的限制，访问 private 字段和方法。
#### 安全性问题
绕过访问修饰符的限制可能会导致安全性问题。
#### 可读性和可维护性差
当程序动态加载类、调用方法时，缺乏静态类型检查。不像普通代码那样直观，尤其当错误发生时，可能更难调试。
#### 依赖于运行时环境
如果类名或方法名改变，而代码中未同步更新，可能导致运行时错误。