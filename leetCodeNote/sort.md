# 快速排序
## 模板1
```java
Random random = new Random();

// 入口
void qsort(int[] nums, int l, int r){
    if(nums == null || nums.length == 0) return;
    sort(nums, l, r);
}

void sort(int[] nums, int l, int r){
    if(l >= r) return;
    int pid = random.nextInt(r - l + 1) + l;
    int pivot = nums[pid];
    swap(nums, pid, r);
    int le = l;
    int ge = r - 1;
    while(true){
        while(le <= ge && nums[le] < pivot) le++;
        while(le <= ge && nums[ge] > pivot) ge--;
        // le 来到了第一个大于等于 pivot 的位置
        // ge 来到了第一个小于等于 pivot 的位置
        if(le >= ge) break;
        swap(nums, le, ge);
        le++;
        ge--;
    }
    swap(nums, le, r);
    sort(nums, l, le - 1);
    sort(nums, le + 1, r);
}

void swap(int[] nums, int i, int j){
    if(i == j) return;
    int tmp = nums[i];
    nums[i] = nums[j];
    nums[j] = tmp;
}
```

## 模板2
使用small, equal, big 这3个数组存储每轮分割结果, 空间复杂度有额外消耗, 但写起来不容易错
```java
// 快速排序，使用 big、small、equal 划分
private void quickSort(List<Integer> nums) {
    if (nums.size() <= 1) return; // 基本情况：如果数组大小为 1 或 0，直接返回
    // 随机选择一个基准数
    Random rand = new Random();
    int pivot = nums.get(rand.nextInt(nums.size()));

    // 划分数组，生成 big, small, equal
    List<Integer> big = new ArrayList<>();
    List<Integer> equal = new ArrayList<>();
    List<Integer> small = new ArrayList<>();
    for (int num : nums) {
        if (num > pivot)
            big.add(num);
        else if (num < pivot)
            small.add(num);
        else
            equal.add(num);
    }

    // 递归排序 big 和 small
    quickSort(small);
    quickSort(big);

    // 连接结果
    nums.clear();
    nums.addAll(small);
    nums.addAll(equal);
    nums.addAll(big);

}

// 主方法
public int[] sortArray(int[] nums) {
    // 将数组转换为 List
    List<Integer> numList = new ArrayList<>();
    for (int num : nums) {
        numList.add(num);
    }

    // 调用快速排序
    quickSort(numList);

    // 将结果转换回数组
    int[] sortedArray = new int[nums.length];
    for (int i = 0; i < nums.length; i++) {
        sortedArray[i] = numList.get(i);
    }
    return sortedArray;
}
```

## 例题
### 215. 数组中的第K个最大元素
- 描述：求有重复元素的数组中第K大的元素，时间复杂度要求O(N).
- 思路：类似快排
- 代码：
```java
// 类似快排，每轮将数组划分为三个部分：小于、等于和大于基准数的所有元素
class Solution {
    Random random = new Random();
    
    public int findKthLargest(int[] nums, int k) {
        List<Integer> arr 
            = Arrays.stream(nums).boxed().collect(Collectors.toList());
        return findKth(arr, k);
    }

    int findKth(List<Integer> arr, int k){
        int poiltIndex = random.nextInt(arr.size());
        int poilt = arr.get(poiltIndex);
        
        List<Integer> bigger = new ArrayList<>();
        List<Integer> equal = new ArrayList<>();
        List<Integer> smaller = new ArrayList<>();
        
        for(int x : arr){
            if(x > poilt) bigger.add(x);
            else if(x < poilt) smaller.add(x);
            else equal.add(poilt);
        }
        
        int size0 = bigger.size();
        int size1 = equal.size();
        int size2 = smaller.size();
        
        if(k <= size0)
            return findKth(bigger, k);
        else if(k <= size0 + size1)
            return poilt;
        else
            return findKth(smaller, k - (size0 + size1));
    }
}
```

# 归并排序
## 模板
```java
// 主方法
public void mergeSort(int[] arr, int left, int right) {
    if (left >= right) return; // 递归终止条件：数组只剩下一个元素

    int mid = left + (right - left) / 2; // 计算中点，防止溢出
    mergeSort(arr, left, mid);  // 递归排序左半部分
    mergeSort(arr, mid + 1, right); // 递归排序右半部分
    merge(arr, left, mid, right); // 合并两个有序数组
}

private void merge(int[] arr, int left, int mid, int right) {
    int[] temp = new int[right - left + 1]; // 临时数组存放合并后的有序序列
    int i = left, j = mid + 1, k = 0;

    // 归并左右两部分
    while (i <= mid && j <= right) {
        if (arr[i] <= arr[j]) { // 这里的 `<=` 保证稳定性
            temp[k++] = arr[i++];
        } else {
            temp[k++] = arr[j++];
        }
    }

    // 复制左边剩余的元素
    while (i <= mid) {
        temp[k++] = arr[i++];
    }

    // 复制右边剩余的元素
    while (j <= right) {
        temp[k++] = arr[j++];
    }

    // 将排序后的数据复制回原数组
    System.arraycopy(temp, 0, arr, left, temp.length);
}
```