# 二分查找
## 基础模板
```java
public static int findLowerBound(int[] arr, int x) {
    int left = 0, right = arr.length - 1;
    int result = -1; // 如果找不到lower_bound，返回-1

    while (left <= right) {
        int mid = left + (right - left) / 2;

        if (arr[mid] <= x) {
            result = mid;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }

    return result;
}
```

## 4种变形
### 1.查找第一个值等于给定值的元素
```java
public int findFirstEqual(int[] arr, int target) {
    int low = 0, high = arr.length - 1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] < target) {
            low = mid + 1; // 在右侧区域查找
        } else if (arr[mid] > target) {
            high = mid - 1; // 在左侧区域查找
        } else {
            // 找到目标值，但需要确认是否是第一个
            if (mid == 0 || arr[mid - 1] != target) {
                return mid; // 是第一个值等于 target 的元素
            }
            high = mid - 1; // 往左侧继续查找
        }
    }
    return -1; // 未找到
}
```

### 2.查找最后一个值等于给定值的元素
```java
public int findLastEqual(int[] arr, int target) {
    int low = 0, high = arr.length - 1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] < target) {
            low = mid + 1; // 在右侧区域查找
        } else if (arr[mid] > target) {
            high = mid - 1; // 在左侧区域查找
        } else {
            // 找到目标值，但需要确认是否是最后一个
            if (mid == arr.length - 1 || arr[mid + 1] != target) {
                return mid; // 是最后一个值等于 target 的元素
            }
            low = mid + 1; // 往右侧继续查找
        }
    }
    return -1; // 未找到
}
```

### 3.查找第一个大于等于给定值的元素
```java
public int findFirstGreaterOrEqual(int[] arr, int target) {
    int low = 0, high = arr.length - 1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] >= target) {
            // 确认是否是第一个大于等于 target 的元素
            if (mid == 0 || arr[mid - 1] < target) {
                return mid; // 是第一个大于等于 target 的元素
            }
            high = mid - 1; // 往左侧继续查找
        } else {
            low = mid + 1; // 在右侧区域查找
        }
    }
    return -1; // 未找到
}
```

### 4.查找最后一个小于等于给定值的元素
```java
public int findLastLessOrEqual(int[] arr, int target) {
    int low = 0, high = arr.length - 1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] <= target) {
            // 确认是否是最后一个小于等于 target 的元素
            if (mid == arr.length - 1 || arr[mid + 1] > target) {
                return mid; // 是最后一个小于等于 target 的元素
            }
            low = mid + 1; // 往右侧继续查找
        } else {
            high = mid - 1; // 在左侧区域查找
        }
    }
    return -1; // 未找到
}
```

## 二维二分分查找
### 240. 搜索二维矩阵 II
- 题意：编写一个高效的算法来搜索 m x n 矩阵 matrix 中的一个目标值 target 。该矩阵具有以下特性： 每行的元素从左到右升序排列。 每列的元素从上到下升序排列。
- 代码：
```java
//  四分法（分治）
class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        return binSearch(matrix, target, 0, m - 1, 0, n - 1);
    }

    boolean binSearch(int[][] matrix, int target, int r_l, int r_r, int c_l, int c_r){
        if(r_l <= r_r && c_l <= c_r){
            int r_mid = r_l + (r_r - r_l) / 2;
            int c_mid = c_l + (c_r - c_l) / 2;
            if(matrix[r_mid][c_mid] == target) return true;
            // 矩阵中间值大于目标，则除了右下角的矩阵外，左侧的整个和上面的整个矩阵作为下一步的搜索
            else if(matrix[r_mid][c_mid] > target){
                return binSearch(matrix, target, r_l, r_mid - 1, c_l, c_r)
                            || binSearch(matrix, target, r_l, r_r, c_l, c_mid - 1);
            }
            // 同理
            else{
                return binSearch(matrix, target, r_mid + 1, r_r, c_l, c_r)
                            || binSearch(matrix, target, r_l, r_r, c_mid + 1, c_r);
            }
        }
        return false;
    }
}
```

## 有序数组部分旋转后寻找分割点
### 153. 寻找旋转排序数组中的最小值
- 描述：将一个有序不重复的数组的前半部分移到后面（也可能数组保持不变），在新数组中寻找最小元素
- 将mid和nums末尾的元素进行比较，一共分两种情况：
  - 情况1：nums[mid] > nums[n - 1]，说明数组一定旋转过了，且mid位于前半段（前半段元素全部大于后半段元素）。此时l=mid+1
  - 情况2：nums[mid] <= nums[n - 1]，说明要么数组保持不变（此时如果mid=0，那么直接返回mid），要么mid在右半段（如果mid-1处元素大于mid处，那么答案就是mid）此时r=mid-1
- 代码：
```java
public int findMin(int[] nums) {
    int n = nums.length;
    int left = 0, right = n - 1;
    while(left <= right){
        int mid = left + (right - left) / 2;
        // mid在左半段
        if(nums[mid] > nums[n - 1]){
            left = mid + 1;
        }
        // mid在右半段
        else{
            if(mid == 0 || nums[mid - 1] > nums[mid])
                return nums[mid];
            else right = mid - 1;
        }
    }
    return -1234567;
}
```

