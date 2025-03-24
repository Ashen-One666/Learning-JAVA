## 限流策略
### 

## 延时队列
### Redisson延时队列
- 依赖两个组件:
  - RDelayedQueue（延时队列）：存储任务，并按照执行时间排序。
  - RQueue（普通队列）：当任务到期后，从 RDelayedQueue 移动到 RQueue，然后被消费者消费。
- 工作流程：
  - 生产者 将任务加入 RDelayedQueue，并设置延迟时间（任务存入 Redis 的 ZSet）。
  - Redisson 内部 维护一个后台线程，定期检查 RDelayedQueue，并把到期的任务移动到 RQueue。
  - 消费者 监听 RQueue，获取到期任务并执行。
- 本质上是使用 Redis ZSet（有序集合）存储任务，再通过轮询机制处理到期任务