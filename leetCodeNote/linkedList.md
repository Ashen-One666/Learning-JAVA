## 单向链表

### 模板
#### 单向链表
```java
    // 单向链表
    public class ListNode{
        int val;
        ListNode next;
        
        ListNode() {}
    
        ListNode(int val) { 
            this.val = val; 
        }
        
        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
```
#### 双向链表 + 增删查
- 707.设计链表
```java
    // 双链表
class ListNode{
    int val;
    ListNode pre;
    ListNode next;

    ListNode() {}
    ListNode(int val) { this.val = val;}
}

class MyLinkedList {
    // 链表大小
    private int size;
    // 虚拟头节点和尾节点
    private ListNode head, tail;

    public MyLinkedList() {
        this.size = 0;
        this.head = new ListNode();
        this.tail = new ListNode();
        // 关键,否则在加入头结点的操作中会出现null.next的错误
        head.next = tail;
        tail.pre = head;
    }

    public int get(int index) {
        // 判断index是否有效
        if(index < 0 || index >= size) return -1;
        ListNode cur = head;
        // 判断从哪一端开始寻找
        if(index < size / 2){
            // 正向
            for(int i = 0; i <= index; i++){
                cur = cur.next;
            }
        }
        else{
            // 反向
            cur = tail;
            for(int i = 0; i < size - index; i++){
                cur = cur.pre;
            }
        }
        return cur.val;
    }

    public void addAtHead(int val) {
        addAtIndex(0, val);
    }

    public void addAtTail(int val) {
        addAtIndex(size, val);
    }

    // 在index前加入（index=size即在尾部插入）
    public void addAtIndex(int index, int val) {
        // 判断index是否有效
        if(index < 0 || index > size) return;

        size++;

        // 寻找前驱
        // cur: 目标点的前驱
        ListNode cur = head;

        for(int i = 0; i < index; i++){
            cur = cur.next;
        }

        // 插入
        ListNode newNode = new ListNode(val);
        newNode.next = cur.next;
        newNode.pre = cur;
        cur.next = newNode;
        newNode.next.pre = newNode;
    }
    
    public void deleteAtIndex(int index) {
        // 判断index是否有效
        if(index < 0 || index >= size) return;

        size--;

        // 寻找前驱(省略了判断从哪一端开始寻找的过程)
        // cur: 目标点的前驱
        ListNode cur = head;
        for(int i = 0; i < index; i++){
            cur = cur.next;
        }
        cur.next.next.pre = cur;
        cur.next = cur.next.next;
    }
}
```

## 链表删除

### 例题
- 203.移除链表元素  
  ```java
    public ListNode removeElements(ListNode head, int val) {
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode cur = dummy;
        while(cur.next != null){
            if(cur.next.val == val){
                cur.next = cur.next.next;
            }
            else{
                cur = cur.next;
            }
        }
        return dummy.next;
    }
  ```

- 19.删除链表的倒数第 N 个结点  
  - 思路：  
    双指针：begin和end，初始时两指针均指向dummy，先让end右移n步，然后让begin和end一起移动，直到end到tail，
    此时begin的位置就是要删除的点的前驱。
  - 代码：
  ```java
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode end = dummy;
        ListNode begin = dummy;
        for(int i = 0; i < n; i++)
            end = end.next;
        while(end.next != null){
            end = end.next;
            begin = begin.next;
        }
        begin.next = begin.next.next;
        return dummy.next;
    }
  ```

## 链表反转

### 例题
- 206.反转链表  
  - 方法1：迭代。两个指针，分别指向当前点cur和前驱pre，更新cur.next=pre之后pre和cur一起右移（需先暂存tmp=cur.next,右移时cur=tmp）  
  ```java
    private ListNode reverse1(ListNode cur){
        ListNode pre = null;
        ListNode tmp = null;
        while(cur != null){
            tmp = cur.next; // 暂存下一个节点
            cur.next = pre;
            pre = cur; // pre右移
            cur = tmp; // cur右移
        }
        return pre;
    }
  ```
  - 方法2：递归。
  ```java
    private ListNode reverse2(ListNode pre, ListNode cur){
        if(cur == null) return pre;
        ListNode tmp = cur.next;
        cur.next = pre;
        return reverse2(cur, tmp);
    }
  ```
  
- 92.反转链表 II  
  - 描述：  
    给定left和right，反转该区间内链表
  - 方法：  
    定义节点p0和p1： p0 -> 子链表 -> p1。 初始时cur指向子链表的head（即p0.next），pre为null。反转过程和反转链表1一样。  
    注意：反转完成后，新的子链表的tail指向p1，p0指向新的子链表的head。
  - 代码：
  ```java
    public ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode p0 = dummy;
        for(int i = 1; i <= left - 1; i++)
            p0 = p0.next;
        // p0是left的前一个节点
        // 后续和反转链表1一样
        ListNode begin = p0.next; // 记录原子链表的head，即新链表的tail，后续要指向p1
        ListNode pre = null;
        ListNode cur = p0.next; // cur指向子链表的head
        ListNode nxt = null;
        for(int i = left; i <= right; i++){
            nxt = cur.next;
            cur.next = pre;
            pre = cur;
            cur = nxt;
        }
        // 反转完成后，pre是原链表的tail,cur是tail的next
        // 反转完成后，新的子链表的tail指向p1，p0指向新的子链表的head
        p0.next = pre;
        begin.next = cur;
        return dummy.next;
    }
  ```

## 模拟

### 例题
- 24.两两交换链表中的节点  
  cur初始为dummy。初始情况： cur -> 1（tmp1） -> 2（tmp2） -> 3（tmp3） -> 4
  - 步骤1： cur 指向 tmp2
  - 步骤2： tmp2 指向 tmp1
  - 步骤3： tmp1 指向 tmp3
  ```java
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode cur = dummy;
        ListNode tmp1 = null;
        ListNode tmp2 = null;
        ListNode tmp3 = null;
        while(cur.next != null && cur.next.next != null){
            tmp1 = cur.next;
            tmp2 = cur.next.next;
            tmp3 = cur.next.next.next;
            cur.next = tmp2;
            tmp2.next = tmp1;
            tmp1.next = tmp3;

            cur = cur.next.next;
        }
        return dummy.next;
    }
  ```

- 160.相交链表  
  - 描述： 给定两个单链表，求链表相交的节点
  - 思路： 先求两个链表的长度，根据长度差值将两个指针移到同一位置。然后两个指针一起右移，直到两指针相等。
  - 注意： 使用虚拟头节点和直接使用头节点两者在循环时有区别，一个代表当前节点，一个代表前驱。
  - 代码：  
  ```java
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode a0 = new ListNode();
        ListNode a = new ListNode();
        a0.next = headA;
        a.next = headA;
        ListNode b0 = new ListNode();
        ListNode b = new ListNode();
        b0.next = headB;
        b.next = headB;
        
        // 找长度
        int sizeA = 0, sizeB = 0;
        while(a0.next != null){
            a0 = a0.next;
            sizeA++;
        }
        while(b0.next != null){
            b0 = b0.next;
            sizeB++;
        }
        //System.out.println(sizeA +" "+ sizeB);
        if(sizeA > sizeB){
            int div = sizeA - sizeB;
            for(int i = 0; i < div; i++)
                a = a.next;
            for(int i = 0; i < sizeB; i++){
                a = a.next;
                b = b.next;
                if(a == b) return a;
            }
        }
        else{
            int div = sizeB - sizeA;
            for(int i = 0; i < div; i++)
                b = b.next;
            //System.out.println(b.next.val);
            for(int i = 0; i < sizeA; i++){
                a = a.next;
                b = b.next;
                if(a == b){
                    return a;
                }
            }
        }
        return null;
    }
  ```
## 链表求环

### 例题
- 142.环形链表 II
  - 介绍：求单向链表中环入口节点
  - 思路：
    - 判断是否有环：**[双指针]** fast和slow，fast每次移动2，slow每次移动1，如果相遇表示有环。
    - 确定环入口：再用两个双指针p0和p1，p0指向head，p1指向相遇点。p0，p1每次都移动1，直到相遇，相遇点就是环入口。（证明见代码随想录-链表-8）
  - 代码：  
  ```java
    public ListNode detectCycle(ListNode head) {
      if(head == null || head.next == null) return null;
      // fast每次移动2，slow每次移动1
      ListNode fast = head;
      ListNode slow = head;
      // 寻找相遇点
      while(fast != null && fast.next != null){
        fast = fast.next.next;
        slow = slow.next;
        if(fast == slow) break;
      }
      if(fast != slow) return null;
      else{
        // p1从起点出发，p2从相遇点出发，p1,p2相遇时就到达了环入口
        ListNode p1 = head;
        ListNode p2 = slow;
        while(true){
          if(p1 == p2) return p1;
          p1 = p1.next;
          p2 = p2.next;
        }
      }
    }
  ```

## 总结
<img src="images/链表总结.png" alt="img 加载失败">

