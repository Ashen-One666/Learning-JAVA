## 基础
回溯算法能解决如下问题：
- 组合问题：N个数里面按一定规则找出k个数的集合
- 排列问题：N个数按一定规则全排列，有几种排列方式
- 切割问题：一个字符串按一定规则有几种切割方式
- 子集问题：一个N个数的集合里有多少符合条件的子集
- 棋盘问题：N皇后，解数独等等

回溯的流程：
```java
private void dfs(参数, for循环层数index){
    1. 终止条件
    2. 对第index层for循环进行处理
        2.1 处理过程
        2.2 dfs(参数, for循环层数index + 1)
        2.3 撤销处理
}
```

同一树枝和同一层去重的区别
- 同一树枝去重
  - 即在一个集合中（path中），不能出现重复的元素（如全排列，每次for循环会从头开始枚举，这样必定会枚举到path中已有的元素，此时需要用used避免）
  - 常见做法：使用used数组标记，回溯时恢复标记
- 同一层去重
  - 当数组中有重复元素时，为了避免生成重复的组合结果，同一层的相同元素只能被选择一次
  - 常见做法：排序 + 相邻相等元素跳过 （或使用set去重，注意，无需在回溯时删除，因为set是仅针对当前层的）


### 17. 电话号码组合
- 代码：
```java
class Solution {

    private List<String> ans = new ArrayList<>();
    private StringBuffer path = new StringBuffer();
    private Map<Character, String> map = new HashMap<>();

    public List<String> letterCombinations(String digits) {
        if(digits.length() == 0) return ans;
        map.put('2', "abc"); map.put('3', "def"); map.put('4', "ghi");
        map.put('5', "jkl"); map.put('6', "mno"); map.put('7', "pqrs");
        map.put('8', "tuv"); map.put('9', "wxyz");
        dfs(digits, 0);
        return ans;
    }

    private void dfs(String digits, int index){
        if(index == digits.length()){
            ans.add(new String(path));
            return;
        }
        // 第index层for循环 （n叉树的第index层）
        char ch = digits.charAt(index);
        String s = map.get(ch);
        // 遍历第index层的每个节点
        for(int i = 0; i < s.length(); i++){
            path.append(s.charAt(i));
            // 以每个节点为根继续搜索下一层
            dfs(digits, index + 1);
            path.deleteCharAt(path.length() - 1);
        }
    }
}
```

## 子集
### 78. 子集
- 描述： 求无重复元素的集合的所有子集
- 思路： 将搜索树中所有节点结果都加入ans中
- 代码：
```java
private void dfs(int[] nums, int index) {
    ans.add(new ArrayList<>(path));
    if (index >= nums.length) return;

    for (int i = index; i < nums.length; i++) {
        path.add(nums[i]);
        dfs(nums, i + 1);
        path.removeLast();
    }
}
```

### 90. 子集 II
- 描述： 求有重复元素的集合的所有子集
- 思路： 搜索时跳过每层中重复的元素
- 方法1： 先排序，如果当前元素是本层中的元素(i > index)，并且和前一个元素重复，则跳过
- 方法2： 使用used数组，used 来记录每个数字是否在当前路径中被使用过。
  - if判断条件为什么是!used[i - 1]的理解： 表面上，continue条件应该是前一个元素被使用过，这里跟正常逻辑相悖； 
    仔细想想，如果used[i - 1]被标记为1， 说明该元素刚被使用过且未回溯，那么就算第i个元素和第i-1个元素相等，也可以放心大胆地加到sublist里面，
    因为加入以后一定比之前元素多一个，不会重复； 相反，如果used[i - 1]被标记为0， 因为现在已经访问到之后的元素了，之前元素一定是被访问过的，
    那么被标0只有一种可能，就是该元素已经被回溯过了，此时必须跳过当前元素，否则会重复，因为list里面已经有相同的sublist.
- 方法3： 使用set对每层去重
- 注意： 3个方法都需要先对原数组排序
- 代码：
```java
class Solution {
    private List<List<Integer>> ans = new ArrayList<>();
    private List<Integer> path = new ArrayList<>();

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums); // 3种方法都必须先排序
        //dfs(nums, 0); // 不使用used数组
        //dfs2(nums, 0); // 使用used数组
        dfs3(nums, 0); // 每层使用set去重
        return ans;
    }

    private void dfs(int[] nums, int index){
        ans.add(new ArrayList<>(path));
        if(index >= nums.length) return;
        for(int i = index; i < nums.length; i++){
            if(i > index && nums[i] == nums[i - 1]) continue;
            path.add(nums[i]);
            dfs(nums, i + 1);
            path.removeLast();
        }
    }

    private boolean[] used = new boolean[10];
    private void dfs2(int[] nums, int index){
        ans.add(new ArrayList<>(path));
        if(index >= nums.length) return;
        for(int i = index; i < nums.length; i++){
            if(i > 0 && nums[i] == nums[i - 1] && used[i - 1] == false) continue;
            path.add(nums[i]);
            used[i] = true;
            dfs(nums, i + 1);
            used[i] = false;
            path.removeLast();
        }
    }

    private Set<Integer> set = new HashSet<>();
    private void dfs3(int[] nums, int index){
        ans.add(new ArrayList<>(path));
        if(index >= nums.length) return;
        for(int i = index; i < nums.length; i++){
            if(set.contains(nums[i])) continue;
            path.add(nums[i]);
            set.add(nums[i]);
            dfs(nums, i + 1);
            path.removeLast();
        }
    }
}
```

### 491. 非递减子序列
- 描述：找出并数组中所有不同的递增子序列（长度>=2）
- 思路：
  - 去重： 在每层中使用set
  - 判断： 当前元素大于path末尾则加入
- 代码：
```java
private void dfs(int[] nums, int index){
    if(path.size() > 1) ans.add(new ArrayList<>(path));
    if(index >= nums.length) return;

    Set<Integer> set = new HashSet<>();
    for(int i = index; i < nums.length; i++){
        if(path.size() > 0 && nums[i] < path.getLast()) continue;
        if(set.contains(nums[i])) continue;
        path.add(nums[i]);
        set.add(nums[i]);
        dfs(nums, i + 1);
        path.removeLast();
    }
}
```

## 排列
### 46. 全排列
- 描述： 求数组的全排列 （如nums=[1,2]，则ans=[[1,2],[2,1]]）
- 思路： 使用used进行树枝去重（注意，前面组合是每层去重，现在是每个树枝去重）
  - 树枝去重：path路径上的元素不能重复（排列问题）-> 定义全局的used数组，回溯时重置used，保证了一个path上不会又重复元素
  - 按层去重：每层中重复的元素不能选取（组合问题） -> 每层新建set去重，回溯时不用删去，因为进新的一层又new了一个全新的set
- 代码：
```java
class Solution {
    private List<List<Integer>> ans = new ArrayList<>();
    private List<Integer> path = new ArrayList<>();
    private boolean[] used;

    public List<List<Integer>> permute(int[] nums) {
        used = new boolean[nums.length];
        dfs(nums);
        return ans;
    }

    private void dfs(int[] nums){
        if(path.size() >= nums.length){
            ans.add(new ArrayList<>(path));
            return;
        }

        for(int i = 0; i < nums.length; i++){
            if(used[i]) continue;
            path.add(nums[i]);
            used[i] = true;
            dfs(nums);
            used[i] = false;
            path.removeLast();
        }
    }
}
```

