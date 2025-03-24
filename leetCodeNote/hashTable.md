## map、set及其底层实现

### map实现类及其底层
  - HashMap  
    - 底层结构：哈希表 + 链表 + 红黑树。
    - 说明：HashMap 使用哈希表来存储键值对，当发生哈希冲突时，会将冲突的键值对存储在链表中。如果单个桶中的元素数量过多（默认阈值为 8），链表会转化为红黑树以加速查询。
    - 性能：平均情况下，基本操作（如插入、删除、查找）的时间复杂度为 O(1)，但在最坏情况下（例如所有键哈希值相同）为 O(log n)。
  - LinkedHashMap
    - 底层结构：哈希表 + 双向链表。
    - 说明：LinkedHashMap 保留了插入顺序或访问顺序（通过构造函数控制）。它在 HashMap 的基础上增加了一个双向链表，用于维护键值对的顺序。
    - 性能：与 HashMap 相同，平均操作时间复杂度为 O(1)，但由于维护了顺序，性能稍低于 HashMap。
  - TreeMap
    - 底层结构：红黑树。
    - 说明：TreeMap 是基于红黑树的实现，能够保证键值对按键的自然顺序或自定义顺序进行排序。它适用于需要按顺序访问键值对的情况。
    - 性能：基本操作的时间复杂度为 O(log n)。

### set实现类及其底层
  - HashSet
    - 底层结构：基于 HashMap 实现。
    - 说明：HashSet 使用一个 HashMap 来存储元素。每个添加到 HashSet 的元素实际上是作为 HashMap 的键值存储的，而 HashMap 的值固定为一个虚拟对象（通常是 new Object()）。这种结构确保了元素的唯一性，因为 HashMap 的键是唯一的。
    - 性能：HashSet 的操作（如添加、删除、查找）平均时间复杂度为 O(1)，前提是哈希冲突较少。
  - LinkedHashSet
    - 底层结构：基于 LinkedHashMap 实现。
    - 说明：LinkedHashSet 保留了元素的插入顺序。它使用 LinkedHashMap 存储元素，并且通过一个双向链表维护元素插入顺序。类似于 HashSet，每个元素作为 LinkedHashMap 的键值存储。
    - 性能：操作时间复杂度为 O(1)，但比 HashSet 稍慢，因为维护了插入顺序。
  - TreeSet
    - 底层结构：基于 TreeMap 实现，具体为红黑树。
    - 说明：TreeSet 保证元素的排序，排序顺序可以是自然顺序（由元素的 compareTo 方法定义）或指定的比较器顺序。TreeSet 的每个元素作为 TreeMap 的键值存储，TreeMap 维护了键的有序性。
    - 性能：基本操作的时间复杂度为 O(log n)，因为底层是红黑树。

## n数之和

### 描述
在数组中寻找n个数之和为target的n元组，要求各n元组不重复

### 例题
- 15.三数之和
  - 思路：  
  一层for循环 + 双指针：  
  先将数组排序。对于3元组（a, b, c），最外层枚举a。 双指针分别指向a之后的两端，sum<target就移左指针，sum>target就移右指针。
  另外，注意剪枝和去重（对于a,b,c都要去重）。
  - 代码：
  ```java
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);
        // (a, b, c)满足和为0
        for(int i = 0; i < nums.length; i++){
            int a = nums[i];
            // 剪枝
            if(a > 0) return ans;
            // 对a去重
            if(i > 0 && nums[i] == nums[i - 1]) continue;
            int left = i + 1;
            int right = nums.length - 1;
            while(left < right){
                int b = nums[left], c = nums[right];
                int sum = a + b + c;
                if(sum < 0){
                    left++;
                }
                else if(sum > 0){
                    right--;
                }
                else{
                    ans.add(Arrays.asList(a, b, c));
                    //System.out.println(a + " " + b + " " + c);
                    while(left < right && nums[left] == nums[left + 1]) left++;
                    while(left < right && nums[right] == nums[right - 1]) right--;
                    left++;
                    right--;
                }
            }
        }
        return ans;
    }
  ```

- 18.四数之和
  - 思路：  
  类似于三数之和，使用两层for循环 + 双指针。

- 454.四数相加
  - 思路：  
  两个循环遍历nums1和nums2，将其所有求和结果a + b的值存储在map的key中，对应的value存a + b出现的次数。  
  另外两个循环遍历nums3和nums4，判断target = - c - d是否在map中，存在则ans += target出现的次数。
  - 代码：
  ```java
  public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
      int ans = 0;
      Map<Integer, Integer> map = new HashMap<>();
      for(int a : nums1)
          for(int b : nums2)
              map.put(a + b, map.getOrDefault(a + b, 0) + 1);
          
      for(int c : nums3)
          for(int d : nums4)
              ans += map.getOrDefault(-c - d, 0);
                 
      return ans;
  }
  ```
  
## 哈希表 + 前缀和
### 560. 和为 K 的子数组
- 描述：给你一个整数数组 nums 和一个整数 k ，请你统计并返回 该数组中和为 k 的子数组的个数 。 子数组是数组中元素的连续非空序列。
- 思路：对于j处的前缀和presum[j]，寻找i的个数 (0<=i<j)，i满足presum[i] = presum[j] - k
- 题目特点：子数组为k，是连续的，而不是子序列为k
- 注意点：
  - presum[0] = 0，如果使用map存储，一定要记得把前缀和0的个数为1（即map.put(0,1)）加入map
  - 先先计算ans，再将当前presum放入map （因为i < j，计算ans时遍历的i在j之前而不会等于j）
- 代码：
```java
class Solution {
    public int subarraySum(int[] nums, int k) {
        int ans = 0;
        Map<Integer, Integer> map = new HashMap<>();
        int[] presum = new int[nums.length];
        presum[0] = nums[0];
        for(int i = 1; i < nums.length; i++)
            presum[i] = presum[i - 1] + nums[i];

        // presum[j] - presum[i] = k    (i < j)
        // 等价于 presum[i] = presum[j] - k
        // 即对于一个j，寻找前j个里面有多少i满足presum[i] = presum[j] - k
        // 用map统计数量
        for(int j = 0; j < nums.length; j++){
            int target = presum[j] - k;
            ans += map.getOrDefault(target, 0);
            map.put(presum[j], map.getOrDefault(presum[j], 0) + 1);
            if(presum[j] == k) ans++;
        }
        return ans;
    }
}
```

### 437. 路径总和 III
- 描述：给定一个二叉树的根节点 root ，和一个整数 targetSum ，求该二叉树里节点值之和等于 targetSum 的 路径 的数目。 路径 不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点）。
- 思路：和560一样

