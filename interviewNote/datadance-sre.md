# 操作系统
1. 进程、线程、协程 区别
- 进程（Process）：资源分配与隔离的基本单位（独立地址空间、文件描述符、句柄等）。
- 线程（Thread）：CPU 调度的基本单位，同一进程内线程共享地址空间与大部分资源；创建/切换成本比进程低。
- 协程（Coroutine）：用户态轻量级“线程”，调度在用户态完成（非抢占/协作式），切换只需保存少量寄存器与栈指针，开销更小；
  需要遇到 await/yield 或 I/O 点主动让出。

2. 线程挂掉对其他进程、线程、协程影响
- 对其它进程：几乎无影响（有进程隔离）。
- 同进程其他线程：可能导致
  - 共享数据被破坏（持锁崩溃、未释放资源）；
  - 进程被 OS 杀死（如主线程崩溃触发默认处理器/abort）；
  - 触发进程级异常处理器（如 Java 的 UncaughtExceptionHandler 只终止该线程，除非你主动退出进程）。
- 协程：运行在某线程上，承载该协程的线程崩溃会让该线程上的所有协程一并终止；协程之间默认无隔离，需要框架做错误传播与取消。

3. 分页大小4kb，为什么？ 过大或过小会怎么样
- 折中原因：TLB 命中率与页表规模的平衡：页太小→页表巨大、TLB 覆盖小、缺页多；页太大→内部碎片严重。
- 太小：页表更大、页表遍历层级增多；TLB miss 增加；缺页/换入换出更频繁，CPU 开销上升。
- 太大：小对象内部碎片放大；COW 粒度变粗；一次换页 I/O 放大；但 TLB 覆盖变大、顺序大内存有利。

4. HugePage了解吗？
- 适用场景：大内存应用（数据库、机器学习、缓存系统）
- 做法：把一个页放大到 2MB / 1GB
- 好处：减少 TLB miss + 减少页表大小
- 分类：
  - 透明大页：把连续的 4KB 页合并成大页，对应用透明，但可能带来 内存碎片、延迟抖动。
  - 显式大页：应用显式申请大页，可预测性更好，减少抖动，但需要应用配合。

5. 了解内核的一些命令，参数，标识吗（sy这些）
- 常用排查指令：
  - top/htop
  - vmstat
  - iostat
  - pidstat
  - lsof
  - ss/netstat
  - sysctl
- top显示的CPU字段：
  - us 用户态
  - sy 内核态
  - ni nice（友好度，即进程优先级，越大越友好，优先级越低）
  - id 空闲
  - wa I/O 等待
  - hi/si 硬/软中断
- 内核参数（sysctl -a 查看）：
  - 内存管理相关（vm.*）
    - vm.swappiness： 内存交换（swap）倾向
    - vm.overcommit_memory： 内存分配策略：0=启发式，1=允许过量分配，2=严格模式。
    - vm.dirty_ratio： 脏页达到比例时触发写回。
  - 文件系统 / 打开文件数（fs.*）
    - fs.file-max： 系统允许打开的最大文件描述符数。
    - fs.nr_open： 单个进程能打开的最大 fd 数。
  - 网络协议栈（net.*）
    - net.ipv4.tcp_syncookies： SYN Flood 防护，1=启用。
    - net.ipv4.tcp_max_syn_backlog： 半连接队列大小，抗高并发。
    - net.ipv4.ip_local_port_range： 本地可用的临时端口范围（如 32768–60999）。
  - 进程 / 调度相关（kernel.*）
    - kernel.pid_max： 最大进程号。
    - kernel.threads-max： 系统允许的最大线程数。
  - 安全相关
    - net.ipv4.ip_forward： 是否允许 IP 转发（路由/容器场景必开）。

6. 用户进程快还是内核进程快
- 没有“谁本质更快”的结论——CPU 速度一样。差别在：
  - 进入内核态有 系统调用/上下文切换 开销；
  - 某些内核路径更贴近硬件、代码路径短，单次操作可能更高效；
  - 调度优先级/实时策略（SCHED_FIFO/RR）影响“看上去更快”。

7. 给出一个日志， 第二行是请求ip 倒数第二行是后端ip，最后一行是状态码，请使用linux命令查找cpu占用top3，且状态为5xx的后端进程
```bash
# 1) 从日志里抽取 5xx 的后端 IP
IPS=$(awk -v RS="" '{      # RS="" 表示按空行分段
    n=split($0,a,"\n");    # n 行，a[n-1]=倒数第二行，a[n]=最后一行
    if (a[n] ~ /^5[0-9][0-9]$/) print a[n-1];
}' access.log | sort -u)

# 2) 根据这些 IP 找本地相关进程 PID
PIDS=$(for ip in $IPS; do
    ss -ntp "dst $ip" | grep -o 'pid=[0-9]*' | cut -d= -f2
done | sort -u)

# 3) 打印 CPU 占用 Top3
ps -o pid,comm,%cpu --sort=-%cpu -p $(echo $PIDS | tr ' ' ,) | head -n 4
```

8. 用过awk吗？  
专门用来做 模式匹配 + 字段处理。  
- 语法： ```awk 'pattern { action }' file```
  - 如： ```awk '{print $2","$5}' access.log```，表示打印文件第一列 
  - 如： ```awk 'NR>=10 && NR<=20 {print}'```，行号过滤：打印第 10 到 20 行

8. 日志查看命令  
tail、cat、tac、head、echo


# 计网
1. http响应码
- 2xx：200 OK，201 Created，204 No Content
- 3xx：301/308（永久重定向），302/307（临时），304 Not Modified
- 4xx：400 Bad Request，401 Unauthorized，403 Forbidden，404 Not Found，405 Method Not Allowed，408 Timeout，409 Conflict
- 5xx：500 Internal Error，502 Bad Gateway，503 Service Unavailable，504 Gateway Timeout
  - 502: 客户端请求网关，网关再去请求后端，如果网关没能正确拿到后端的合法响应，就会返回 502。
    - 常见原因：网络不通，后端域名解析失败，后端进程挂掉，协议错误。
    - 排查方案：```ps -ef | grep <backend>```（查后端是否正常） 
               -> ```curl -v http://127.0.0.1:8080/health``` （测Nginx 到后端的连通性）
               -> ```tail -f /var/log/nginx/error.log``` （查nginx异常）
  - 504：网关成功连上了后端，但超时没等到响应。

2. http1/2/3区别？
- HTTP/1.1：文本协议；多连接/队头阻塞（pipelining 基本废弃）；无头压缩。
- HTTP/2：二进制分帧；单 TCP 多路复用；HPACK 头压缩；流量优先级；（Server Push 已基本弃用）；仍受 TCP 层队头阻塞影响。
- HTTP/3：基于 QUIC(UDP)；握手整合 TLS1.3、0-RTT；内置多路复用且无 TCP 队头阻塞；连接可迁移（IP/网络切换不断线）。

3. springboot收到一个请求，处理流程是怎样的？
![springmvc工作流程.png](images%2Fspringmvc%E5%B7%A5%E4%BD%9C%E6%B5%81%E7%A8%8B.png)

NIO Acceptor (Tomcat/Undertow/Netty) → 线程池取任务 → Servlet Filter 链 → HandlerMapping 定位处理器 
→ HandlerInterceptor.preHandle → HandlerAdapter 调用 @Controller/@RestController 
→ 参数绑定&校验（@Valid/@RequestBody）→ Service/Repository（事务、AOP）→ 返回值经 HttpMessageConverter 序列化 
→ Interceptor.post/afterCompletion → Filter 出口 → 写回响应。
异常走 HandlerExceptionResolver。静态资源/异步请求有各自分支。

# java
1. java是值传递还是引用传递
值传递

# 实习
1. 当你调用的ds大模型接口断连之后，你有什么处理方案？一定要人工重连码？