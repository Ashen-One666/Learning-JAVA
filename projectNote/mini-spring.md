## Spring中的线程上下文加载器
### Spring中 资源加载使用线程上下文加载器打破双亲委派模型
Spring在加载资源时，通常会使用 线程上下文加载器 来加载资源，比如 application.properties、XML 配置文件、YAML 文件等
```java
Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties")
```
- 1.作用？
  - 获取当前线程的上下文类加载器（ContextClassLoader）
  - 使用这个类加载器查找 application.properties 资源文件
  - 以 InputStream 的方式打开文件，从而可以读取文件内容
- 2.为什么要用 ContextClassLoader 来加载资源？
  - Spring 框架和 JavaEE 容器（如 Tomcat、Jetty）中，不同的模块可能使用不同的类加载器
- 3.为什么 ContextClassLoader 破坏了双亲委派？
  - 默认情况下，类加载是由 ClassLoader.class.getResourceAsStream() 进行的，它遵循双亲委派模型
  -  但 Thread.currentThread().getContextClassLoader() 允许我们 在当前线程动态指定类加载器，而不是由 AppClassLoader 交给 ExtClassLoader 或 BootstrapClassLoader 处理，这就跳过了默认的双亲委派逻辑
- 4.为什么需要打破双亲委派？
  - Spring 作为一个 框架，通常由 应用类加载器（AppClassLoader） 加载，而不是 BootstrapClassLoader 或 ExtClassLoader
  - 但是 Spring 运行时可能需要加载 Spring 配置文件，JDBC 驱动，其他插件化的类等，这些资源可能不是由 Spring 自己的类加载器加载的，而是由 用户应用的类加载器加载的（如tomcat的 Web 应用由WebAppClassLoader加载）。
  - 如果 Spring 还是按照 双亲委派模型，它的 父类加载器可能找不到这些资源，因此需要使用 线程上下文类加载器 让 应用级的 ClassLoader 来加载它们。
- 5.线程上下文类加载器具体使用方式？
  - 例：当 Spring 需要加载 JDBC 驱动时，它会 暂时切换线程上下文类加载器，执行完后再恢复
  ```java
  ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
  try {
  // 设置当前线程的类加载器为 Spring 自己的类加载器（可能是 AppClassLoader 或 WebAppClassLoader）
  Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
  
      // 触发 JDBC 驱动的加载
      Class.forName("com.mysql.cj.jdbc.Driver");
  
  } finally {
  // 恢复原来的类加载器，避免影响其他代码
  Thread.currentThread().setContextClassLoader(originalClassLoader);
  }
  ```

### Tomcat和Spring打破双亲委派模型的区别
https://blog.csdn.net/bigtree_3721/article/details/75947762

### 线程上下文加载器使用场景
- 当高层提供了统一接口让低层去实现，同时又要是在高层加载（或实例化）低层的类时， 必须通过线程上下文类加载器来帮助高层的ClassLoader找到并加载该类。
- 当使用本类托管类加载，然而加载本类的ClassLoader未知时，为了隔离不同的调用者， 可以取调用者各自的线程上下文类加载器代为托管。

##

## Bean

### Bean生命周期流程
![bean-life.png](..%2FleetCodeNote%2Fimages%2Fbean-life.png)

### 后置处理器：BeanPostProcessor
- 作用：如果我们希望容器中创建的每一个bean，在创建的过程中可以执行一些自定义的逻辑，那么我们就可以编写一个类，并让他实现BeanPostProcessor接口，然后将这个类注册到一个容器中。容器在创建bean的过程中，会优先创建实现了BeanPostProcessor接口的bean，然后，在创建其他bean的时候，会将创建的每一个bean作为参数，调用BeanPostProcessor的方法。而BeanPostProcessor接口的方法，即是由我们自己实现的。
- 方法：
  - postProcessBeforeInitialization
  - postProcessAfterInitialization
- 应用场景：
  - @PostConstruct注解的方法是在postProcessBeforeInitialization中执行的
  - AOP就是基于BeanPostProcessor的，AOP 代理的创建发生在 postProcessAfterInitialization 阶段。

### 拓展性接口：InitializingBean
- 为bean提供了属性初始化后的处理方法
- 方法：
  - afterPropertiesSet

### bean工厂实现类：DefaultListableBeanFactory
- 定义bean：bean配置是在xml中的，但在工厂中是以beanDefinition形式包装的
- 注册bean：bean工厂实现类实现了BeanDefinitionRegistry接口，有了注册bean的能力
- 懒加载：只有去getBean才会创建bean（ApplicationContext相反，容器启动完成后所有的bean就已经被创建好了）
- 存储bean定义信息： Map<String, BeanDefinition> beanDefinitionMap
- 存储单例bean的实例： Map<String, Object> singletonObjects
- 加载xml配置： 使用XmlBeanDefinitionReader
- 属性注入：populateBean()方法，在这个方法中有一步就是执行@Autowired注解对象的注入

### 初始化阶段的三个方法（初始化前依次执行这三个方法）
#### @PostConstruct
自定义bean中的方法使用该注解即可，好处是无需实现InitializingBean接口
#### InitializingBean # afterPropertiesSet()
自定义bean需要实现InitializingBean接口并重写afterPropertiesSet()方法
#### BeanPostProcessor # postProcessBeforeInitialization
全局应用：自定义了BeanPostProcessor的实现类，重写其中的方法可以应用到所有被 Spring 管理的 Bean，适用于全局性的 Bean 初始化逻辑。

## AOP
### 所需接口
- AopProxy
- MethodInterceptor
- MethodInvocation

### 实现类
- JdkDynamicAopProxy
  - 实现了 AopProxy 和 InvocationHandler 接口
  - getProxy：
    - 获取代理对象（参数可以传入类加载器，从而用指定类加载器加载）
    - 获取方法：通过aop的配置类AdvisedSupport中的信息得到需要被代理的对象
  - invoke：
    - 在 JDK 动态代理 中，所有的方法调用都会被 InvocationHandler 处理，代理对象不会直接调用目标方法，而是通过 InvocationHandler.invoke() 进行调用。
    - 此实现类的invoke方法创建了拦截器链，并 使用方法调用实现类ReflectiveMethodInvocation 提供的方法调用功能，实现了拦截器的依此执行和最终的目标方法调用。
- ReflectiveMethodInvocation
  - 实现了 MethodInvocation 接口 
  - 方法调用的封装类
  - 封装目标对象、方法、参数、拦截器列表
  - 维护拦截器链，按顺序执行所有拦截器（拦截器全部存在一个链表中，当执行拦截器到最后一个之后就执行原始方法）
  - 执行原始方法 
- MethodBeforeAdviceInterceptor
  - 实现了 MethodInterceptor 接口
  - 执行前置通知，并继续执行拦截器链
- AdvisedSupport
  - aop配置类，存有目标对象，方法拦截器列表，方法匹配器（检查是否符合切点表达式）

### AOP流程
- 1.代理创建
  - 创建代理工厂，指定代理方式（jdk/cglib）并生成代理类
  - jdk代理方式实现类：JdkDynamicAopProxy 
- 2.方法拦截
  - 创建方法调用，获取拦截器链并执行链
  - 方法调用实现类：ReflectiveMethodInvocation
- 3.目标方法调用
  - 调用目标方法，处理返回值和异常

### AOP拦截器链的执行流程
- 关键角色：
  - 方法调用链的执行器（一个中间类，用于处理一个接一个的拦截器）：ReflectiveMethodInvocation
  - 拦截器（实现类为例，如方法前置增强）：MethodBeforeAdviceInterceptor
- 流程（本质是递归，每深一层，ReflectiveMethodInvocation中的计数器就+1，计数器到达拦截器链size时调用目标方法）：
  - JdkDynamicAopProxy # invoke() 方法会调用 ReflectiveMethodInvocation # proceed() 方法
  - ReflectiveMethodInvocation # proceed() 获取下一个拦截器并执行 interceptor.invoke(this)
  - 如果当前拦截器（MethodInterceptor）还需要执行下一个拦截器，它会再调用 proceed()，直到所有拦截器执行完毕。

### AOP中的适配器模式
#### 接口：AdvisorAdapter
- 作用：适配器接口，用于将 不同类型的 Advice（通知） 适配为 MethodInterceptor（方法拦截器）
- 使用适配器的原因：Spring AOP 支持不同类型的通知（Advice），但 AOP 代理只认识 MethodInterceptor
- 对应关系（以前置通知为例）：
  - 适配器：MethodBeforeAdviceAdapter
  - 适配的 Advice：MethodBeforeAdvice（前置通知）
  - 转换后的 MethodInterceptor：MethodBeforeAdviceInterceptor

### AOP中的AspectJ切点表达式解析器
- 总体思想：分层管理
  - 哪些类 需要被代理 —— 由 ClassFilter 负责
  - 哪些方法 需要被拦截 —— 由 MethodMatcher 负责
  - 表达式管理 —— 由 ExpressionPointcut 负责
- 匹配机制：
  - 类级别匹配：判断目标类是否可能包含目标连接点
  - 方法级别匹配：判断方法是否精确地匹配表达式
- 实现类：AspectJExpressionPointcut 实现了上述的3个接口
- 如何解析表达式：
  - AspectJExpressionPointcut 使用了 AspectJ 提供的工具类 org.aspectj.weaver.tools.PointcutParser 进行解析

### Spring AOP 是如何获取拦截器链的？ Spring AOP 拦截过程大致是什么？
- 事先维护一个 Advisor 列表，在 Spring AOP 启动时，扫描所有 @Aspect 类（即我们定义的切面）， 
  将 @Before、@AfterReturning 等注解的方法解析为 Advisor 并加入 Advisor 列表。
- 在调用目标方法时，遍历 Advisor 列表，查询 每个 Advice 是否和当前目标方法匹配。
- 前置拦截器链方法执行过程：
  - ReflectiveMethodInvocation 执行拦截器链，先维护一个计数器cnt，通过 proceed方法 调用 列表中cnt处的拦截器的 invoke方法（然后计数器加1），
    每个拦截器又会调用 ReflectiveMethodInvocation 的 proceed方法。
  - 通过这种来回调用，达到一个递归调用的效果，最终能遍历执行完整个拦截器链。
  - 后置拦截器链方法执行过程类似

## 事务
### 事务属性
#### 接口：TransactionDefinition
- 用处：定义事务的 隔离级别 和 传播行为

### 事务状态
#### 接口：TransactionStatus
- 用处：检查当前事务是否是 1.新事务 2.有保存点（嵌套事务） 3.标记为已回滚（提交时自动回滚） 4.已提交或已回滚

### 事务同步管理器
#### 抽象类：TransactionSynchronizationManager
- 用处：
  - 绑定、管理事务资源（对于事务，dataSource创建的conn就是资源）
  - 支持事务同步回调（在事务提交前、提交后、回滚后执行额外逻辑）
- 基于 ThreadLocal 的原因（保障事务的原理）：
  - spring的事务通过数据库DataSource获取connection来实现，为了使事务方法service.methodA，调用dao.methodB时，仍然能够位于当前事务，则需要将connection共享，由于二者都在同一线程中，因此使用 ThreadLocal 存放 connection
  - （简单来说：spring 事务在 service层某方法中实现。service层持有conn，而service层需要与DAO层交互，因此也要保证DAO也可以获取conn，因此将conn存在线程中）
- 1\. 事务资源：
  - [**重要**] Spring每个事务资源都与当前线程绑定，事务资源 基于 ThreadLocal
  - 实现方法：维护4个 ThreadLocal 变量：
    - resources（map类型）：存储事务资源，如数据库连接
    - synchronizations（list类型）：存储事务同步器，即各种回调方法
    - 两个标志位：标志事务是否开启同步
  - 资源管理：获取到资源后放入当前线程的 resources 这个map中
- 2\. 事务同步管理：
  - 如果事务开启了同步，则遍历 synchronizations 列表，执行回调方法

### 事务管理器
#### 接口：PlatformTransactionManager
- 用处：提供事务操作接口，包括获取事务，事务提交，事务回滚
#### 抽象类：AbstractPlatformTransactionManager
- 用处：实现了事务管理器的基本逻辑
- 1\. 获取事务（返回值是事务状态TransactionStatus）：
  - 对于不存在的事务：根据事务的传播行为，判断是否要 新建事务/以非事务方式执行/抛出异常。
  - 对于已存在的事务：根据事务的传播行为，执行不同操作（如挂起当前事务等），最后返回不同事务状态。
- 2\. commit：
  - 根据事务状态，如果当前事务为已回滚则在提交之前就回滚。
  - 执行commit前后的回调，以及doCommit操作。
  - 具体的回调方法由事务同步器 TransactionSynchronization 执行。
  - 具体的doCommit操作由该抽象类的实现类实现。
- 3\. rollback：
  - 与commit类似
#### 实现类（基于JDBC的）：
- 用处：在抽象事务管理器以及实现基本逻辑的基础上，负责 数据库事务的开启、提交、回滚
- doGetTransaction：
  - 从 TransactionSynchronizationManager 获取当前线程的数据库连接。
  - 同时，若没有事务则创建。
- doBegin：
  - 开启事务，通过 DataSource 获取数据库连接 Connection。
  - 关闭 autoCommit，手动管理事务。
  - 绑定 ConnectionHolder 到 TransactionSynchronizationManager，用于事务同步。
- doCommit：
  - 调用 con.commit() 提交事务
- doRollback：
  - 调用 con.rollback() 回滚事务
- doCleanupAfterCompletion：
  - 事务完成后，解绑 ConnectionHolder，防止内存泄漏。
  - 释放数据库连接。

### 事务拦截器
#### 实现类：TransactionInterceptor （继承 TransactionAspectSupport， 实现 MethodInterceptor）
- 用处：最终执行事务控制的核心，上面提到的事务管理器，事务同步管理器等都是它的一个模块
- 调用流程：当一个 @Transactional 方法被调用时，Spring AOP 代理拦截 @Transactional 方法，调用 TransactionInterceptor
- 拦截器的执行流程：
  - 获取事务属性
  - 获取事务管理器
  - 创建事务
  - 执行目标方法
  - 提交事务

### 事务失效
#### 一个 @Transactional方法 调用同个类中另一个 @Transactional方法，会导致被调用方法事务失效
- 原因：调用时， this.method2() 中的this会跳过AOP代理，导致执行的是原本类的方法而不是代理对象的事务方法
- 结果：被调用方法会事务失效，其中的操作会加入调用方的事务中
- 解决方案：
  - 1\. 不通过this去调用method2，而是先@Autowired注入自己，再调用method2 （缺点：违背单一职责原则）
  - 2\. 将事务方法拆分到不同的 Service 类中，这样 @Transactional 事务能正常生效

### 声明式事务是如何基于AOP实现的？
- Spring 在启动时解析 @Transactional，创建 TransactionInterceptor 并注册到 Advisor 列表中。
- 也就是说 事务 是作为一个 Interceptor 被放到 Advisor 列表中的，在AOP执行目标方法的拦截器链的时候就会执行对应的 事务方法了。
- TransactionInterceptor 包含了前置和后置拦截方法，AOP会自动的在方法前后对应的位置执行。
- 前置方法包括了开启事务、设置事务上下文等操作， 后置方法包括提交事务或回滚事务。

# 三级缓存
https://zhuanlan.zhihu.com/p/610322151

循环依赖处理流程图：
![循环依赖处理流程.jpg](images%2F%E5%BE%AA%E7%8E%AF%E4%BE%9D%E8%B5%96%E5%A4%84%E7%90%86%E6%B5%81%E7%A8%8B.jpg)

# 循环依赖为什么用三级缓存而非二级缓存？
https://zhuanlan.zhihu.com/p/377878056  
并不是说二级缓存如果存在aop的话就无法将代理对象注入的问题，本质应该说是初始spring是没有解决循环引用问题的，
设计原则是 bean 实例化、属性设置、初始化之后 再 生成aop对象，但是为了解决循环依赖但又尽量不打破这个设计原则的情况下，
使用了存储了函数式接口的第三级缓存； 如果使用二级缓存的话，可以将aop的代理工作提前到 提前暴露实例的阶段执行； 
也就是说所有的bean在创建过程中就先生成代理对象再初始化和其他工作； 但是这样的话，就和spring的aop的设计原则相驳，
aop的实现需要与bean的正常生命周期的创建分离； 这样只有使用第三级缓存封装一个函数式接口对象到缓存中， 发生循环依赖时，触发代理类的生成  
先创建 singletonFactory 的好处就是：在真正需要实例化的时候，再使用 singletonFactory.getObject() 获取 Bean 或者 Bean 的代理。
相当于是延迟实例化。  
补充：其实即使延迟实例化，仍然打破了spring的设计顺序，即循环依赖时代理类在被获取时触发lambda保存的方法创建，这时该类populateBean方法都还没走完，更别说初始化了。
所以说，当出现代理对象间的循环引用，提前代理是不可避免的，但是如果我们都直接提前代理，那对于原本没有循环依赖的代理对象，实际上是破坏了它们创建的顺序，作者指的应该是没有循环依赖的代理对象。
总结：
- 尽可能避免在实例化阶段就开始代理，只在发生循环依赖时提前代理，没有循环依赖就不会提前代理
- （Spring无论是否存在循环依赖，在创建bean实例时都会创建bean的工厂并放入3级缓存中，但是否要使用factory是由是否存在循环依赖决定的，如果没有循环依赖，则直接移除3级缓存并放入1级缓存，不涉及到factory的创建bean方法）
- 有构造器注入，不一定会产生问题，具体得看是否都是构造器注和 BeanName 的字母序



# 面试题
## JDK动态代理和CGLIB动态代理怎么实现的？
### JDK动态代理
- 反射 + InvocationHandler
### CGLIB动态代理
- 继承 + MethodInterceptor

## 如何设计切点表达式解析器来支持AspectJ表达式的？
### AspectJ 是什么
- AOP的扩展框架
- JDK和CGLIB是运行时代理，AspectJ是修改字节码实现的
- SpringAOP只能代理bean，AspectJ可以代理普通java类
- AspectJ 在 Spring 中主要用于切点表达式解析，如@Aspect注解
### 如何设计切点表达式解析器来支持AspectJ表达式
- 总体方案：分层可扩展，将切点表达式 解析 和 匹配 分离
- 解析：负责解析 AspectJ 表达式（使用 AspectJ 原生的解析能力）
  - 使用 AspectJ 的 PointcutParser 解析，支持表达式的懒加载和缓存
- 匹配： 分为 类 和 方法 层面的匹配
  - ClassFilter： 判断目标类是否可能包含匹配的连接点
  - MethodMatcher： 判断当前方法是否匹配切点表达式
- 保证了和SpringAOP的兼容

## 声明式事务处理机制是什么样的？
### 事务管理体系
- 顶层接口：PlatformTransactionManager
  - 定义事务管理操作：事务获取，事务提交，事务回滚
- 抽象类：AbstractPlatformTransactionManager
  - 实现了事务管理器的基本逻辑
- 实现类：DataSourceTransactionManager
  - 用于处理基于JDBC的数据库事务
### 事务属性
- 接口：TransactionDefinition  
- 4种隔离级别 + 7种传播行为
### 事务状态
是否是新事务 / 有检查点（嵌套事务） / 只回滚 / 已提交 / 已回滚
### 事务同步管理
- 抽象类：TransactionSynchronizationManager
- 作用： 绑定事务所需资源（基于ThreadLocal） + 事务同步方法回调
- 使用ThreadLocal的原因：
  - 每个线程只能访问自己的事务副本，确保即使在多线程环境下，也能保持事务的一致性
### 声明式事务实现
- 基于AOP
- 通过@Transactional注解事务方法
- 使用TransactionInterceptor作为AOP的通知，在方法执行前后进行事务处理
- 实现异常处理机制：区分 检查型异常 和 运行时异常 （默认运行时异常回滚，但可通过rollbackFor自定义异常处理策略）

## spring核心框架设计中遇到的挑战是什么？
### Bean生命周期的管理
- 考虑bean定义的设计，以及bean定义的注册管理（definition + definitionRegister 分层）
- 如何提供扩展点（BeanPostProcessor 机制，在初始化前后执行回调方法）

### 如何在IOC容器管理bean的基础上


## 责任链模式作用？怎么设计责任链？
- 作用：解耦，易扩展，灵活（可以任意改变执行顺序）
- 设计：
  - 场景：设计一个订单处理系统，包含 库存检查、支付验证、物流发货 操作。
  - 1\. 处理器抽象类： abstract class OrderHandler
    - 包含一个属性：protected OrderHandler nextHandler （类似于链表中的指针）
    - 对外暴露两个方法：
      - public void setNextHandler(OrderHandler nextHandler)，用来指定下一个链是什么。
      - public void handle(Order order)，用来调用每一个实现类的doHandle方法。
    - 需要重写的方法：
      - protected abstract boolean doHandle(Order order);
  - 2\. 具体处理器实现类（以库存检查为例）： class StockHandler extends OrderHandler
    - 重写doHandle方法
  - 3\. 责任链工厂： OrderHandlerFactory
    - 负责组装责任链（即为调用每个Handler的 setNextHandler方法 指定下一个Handler，同时我们也可以将责任链顺序配置到配置文件中，factory通过读取配置文件来实现组装）
    - 提供创建责任链的方法，返回值是责任链第一个Handler，获取到之后我们就可以执行这个Handler的handle方法了

## 建造者模式
用于逐步构建复杂对象，适用场景：当一个对象包含很多可选参数，构造方法参数太多，对象的创建过程复杂，需要分步骤构建。  
样例代码：  
```java
User user = new User.UserBuilder("Alice", 25)  // 必填字段
                .setGender("Female")           // 可选字段
                .setAddress("123 Street")
                .setPhone("123456789")
                .build(); // 最后调用 build() 构造对象
```
- 优点：
  - 避免构造方法过长
  - 链式调用，代码简洁
  - 易于扩展
- 应用：
  - Spring 创建 Bean 时，使用 建造者模式 来定义 Bean，即 BeanDefinitionBuilder
  - 代码：
  ```java
  BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MyService.class)
        .addPropertyValue("name", "Alice")
        .addPropertyValue("age", 25);
  BeanDefinition beanDefinition = builder.getBeanDefinition();
  ```
  
## 结合mini-spring谈谈你对反射和动态代理的理解
### 反射
- 定义： 在运行时检查和操作类、方法、字段的机制，允许动态创建对象、调用方法，甚至修改私有属性。
- 原因：
  - Spring 需要根据配置文件或注解，动态创建 Bean 并注入依赖，这就离不开反射。
  - 依赖注入（DI）：通过反射注入属性。
  - Spring AOP 通过代理模式拦截方法调用，而拦截的方法是通过反射调用的。
- 举例：
  - Spring通过反射解析配置信息（如xml文件）并生成对应的bean。
    通过io流读取配置文件并获取类的全类名和相应的方法 ```String getName(String key) ``` 。
    然后用 ```Class<?> c = Class.forName(getName("className")); ``` 创建类。

### 动态代理
动态代理 主要用于 AOP，拦截方法并增强（如事务管理、日志、权限控制）
  
### 简介异常处理机制
分层处理
- 第一层：基础异常体系
  - 基础异常类：BeanException
  - 针对不同模块定义不同异常，如：
    - bean定义异常
    - 事务相关异常
    - web相关异常
- 第二层：web层统一异常处理
  - @ExceptionHandler 注解
    - 用于局部方法捕获，与抛出异常的方法处于同一个 Controller 类。
  - HandlerExceptionResolver 接口
    - 用来处理全局异常，但是只能返回 ModelAndView，不适合 JSON API，所以不推荐使用
  - @ControllerAdvice 注解
    - 处理全局异常
- 第三层：AOP异常处理
  - 可以统一记录异常日志，事务回滚，返回统一的错误响应  

多层次的好处：
- 异常处理统一规范
- 开发者可专注于业务逻辑
- 便于维护扩展


# 2025.09.01
## Spring 创建Bean过程
### 1. 加载Bean
- 准备Bean，通过@Component 或 @Bean 方式指定Bean。前者通过 ClassPathBeanDefinitionScanner 扫描 @ComponentScan 注解中指定的包下的类，
后者通过 ConfigurationClassPostProcessor 解析配置类，扫描其中 @Bean 方法，然后将得到的类信息封装成 BeanDefinition ，再加载进 BeanDefinitionRegistry。

### 2. 实例化Bean
- 2.1 实例化前，容器先尝试从三级缓存中获取Bean，如果无法获取则开始实例化流程
- 2.2 通过 BeanDefinition 判断 Bean 是单例还是原型，并走不同的流程
- 2.3 开始实例化，调用 getSingleton() 方法，传入 对象工厂（实际是lambda表达式）。
  - （注意：对象工厂负责通过反射创建Bean实例， getSingleton 负责缓存 Bean 和处理缓存依赖）
- 2.4 将当前 Bean 加入 “创建中”集合
- 2.4 调用对象工厂的getObject方法，底层通过反射技术获取构造参数将对象创建了出来，此时的对象只是通过空参构造创建出来的对象，他并没有任何的属性
- 2.5 调用 addSingletonFactory 将实例化完成的bean加入到三级缓存

### 3. 属性填充
- 3.1 populateBean 该方法是填充属性的入口，传入beanName和BeanDefinition
- 3.2 从 BeanDefinition 中获取属性注入相关信息然后判断是名称注入还是类型注入
- 3.3 调用 getSingleton 从容器中获取依赖对象，若是获取不到则会重走对象创建的整个流程，拿到完整对象后将其给到当前bean的属性

### 4. 初始化Bean 
（主要动作： aware接口的处理，postprocessor接口的处理，初始化的处理）
- 4.1 判断Bean是否实现Aware接口，有则执行其实现类，提供其需要感知的 Spring 容器内部信息
  - BeanNameAware → 注入当前 Bean 的名字
  - BeanFactoryAware → 注入 BeanFactory
  - ApplicationContextAware → 注入 ApplicationContext
- 4.2 获取容器中所有postprocessor接口，然后开始执行其前置方法
  - 可理解为 Bean 初始化的拦截器链
- 4.3 处理 InitializingBean & init-method

### 5. 后置操作
- 5.1 将bean从创建中的集合中删除
- 5.2 将bean加入到单例池中将其从二级三级缓存中删除

### 示例图
![生命周期.jpg](images%2F%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.jpg)

## @Component 和 @Bean 区别
### @Component:
- 用途: @Component是一个泛化的定义，用于标识任何Spring管理组件。
- 范围: @Component注解可以用于类级别，表示整个类作为一个组件，通常与Spring的组件扫描机制结合使用。
- 配置方式: 基于类的配置，适用于没有特殊要求的普通组件。
- 构建方式： 反射创建

### @Bean:
- 用途: @Bean通常用于方法级别，用于定义一个由方法返回的bean。
- 范围: @Bean注解通常在@Configuration类中使用，与方法关联。
- 配置方式: 基于方法的配置，适用于自定义、复杂或需要特殊处理的bean。
- 构建方式： 为了保证单例，是先将 配置类 包装成代理类，再调用 @Bean 方法，保证仅调用一次

## Spring 生命周期扩展点
### 容器级的方法（BeanPostProcessor 一系列接口）
接口的实现类是独立于 Bean 的，并且会注册到 Spring 容器中。Spring 容器创建任何 Bean 的时候，这些后处理器都会发生作用。  
举例：postProcessAfterInitialization 方法，在初始化后阶段使用，是AOP生成代理对象的关键

