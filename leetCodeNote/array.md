## 二分

### 例题： 
- 704.二分查找
- 35.搜索插入位置
- 69.x 的平方根

### 模板

```java
    public static int search(int[]nums, int l, int r, int target){
    // 避免当 target 小于nums[0] nums[nums.length - 1]时多次循环运算
    if (target < nums[0] || target > nums[nums.length - 1]) {
        return -1;
    }
    int mid = 0;
    while (l <= r) {
        mid = l + (r - l) / 2;
        if(nums[mid] > target){
            r = mid - 1;
        }
        else if(nums[mid] < target){
            l = mid + 1;
        }
        else{
            return mid;
        }
    }
    return -1;
}
```

### 注意
- 左闭右闭写法： r=mid-1 和 l=mid+1 （是mid还是mid-1取决于定义的区间是开还是闭，闭区间使用r=mid-1表示mid不符合，从mid-1开始找）

## 移除元素

### 介绍
给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素，并返回移除后数组的新长度。

### 思路
数组的元素在内存地址中是连续的，不能单独删除数组中的某个元素，只能覆盖。  
使用双指针，快指针（read）指向新数组的元素（即不包含val的答案数组），慢指针（write）指向新数组目前写到的位置。  
read指针遍历原数组，跳过当前等于val的元素，对于不等于val的元素，写入nums[write], 同时write++

### 例题： 
- 27.移除元素  
  双指针：快指针（读指针）指向新数组的元素，慢指针（写指针）指向新数组目前写到的位置
- 26.删除有序数组中的重复项  
  类似27
- 283.移动零  
  类似27，相当于删除0

## 有序数组的平方

### 介绍
给你一个有正有负的 非递减 数组，返回每个数字的平方组成的新数组，要求也按非递减顺序排序。

### 思路
数组其实是有序的， 只不过负数平方之后可能成为最大数了。那么数组平方的最大值就在数组的两端，不是最左边就是最右边。  
使用双指针，从两端取数并按从大到小倒序加入ans，每次加入的数必为nums两端中较大的那个数。

### 例题： 
- 977.有序数组的平方  
  双指针：从两端取数并按从大到小倒序加入ans，每次加入的数必为nums两端中较大的那个数

## 滑动窗口

### 介绍
在“子序列”问题中：所谓滑动窗口，就是不断的调节子序列的起始位置和终止位置，从而得出我们要想的结果。  
在暴力解法中，是一个for循环滑动窗口的起始位置，一个for循环为滑动窗口的终止位置。  
滑动窗口用一个for循环，***循环的索引一定是表示滑动窗口的终止位置***。  
滑动窗口通常考虑3个问题：  
- 窗口内是什么？
- 如何移动窗口的起始位置？
- 如何移动窗口的结束位置？

### 例题： 
- 209.长度最小的子数组  
  滑动窗口：for循环枚举窗口的结束位置right，当窗口内sum>=target时，更新ans并移动窗口起始位置(left++)
- 76.最小覆盖子串[hard]  
  滑动窗口：
  - 窗口内是什么？  
    窗口维护的是当前窗口内每个有效字母的个数
  - 如何移动窗口的结束位置？  
    right指针遍历整个字符串，每向右移一次，判断当前窗口内每个有效字母的个数是不是都超过了需要的个数
  - 如何移动窗口的起始位置？  
    如果都超过了（即字串满足了题意），则更新ans，并且右移窗口的起始位置（left++）

## 模拟行为

### 介绍
如模拟螺旋式地填充矩阵

### 例题
- 59.螺旋矩阵 II  
  模拟过程： 按右下左上的顺序填矩阵，每填完一行或一列，更新边界
  ```java
    public int[][] generateMatrix(int n) {
        int l = 0, r = n - 1, t = 0, b = n - 1;
        int[][] mat = new int[n][n];
        int num = 1, tar = n * n;
        while(num <= tar){
            for(int i = l; i <= r; i++) mat[t][i] = num++; // left to right.
            t++;
            for(int i = t; i <= b; i++) mat[i][r] = num++; // top to bottom.
            r--;
            for(int i = r; i >= l; i--) mat[b][i] = num++; // right to left.
            b--;
            for(int i = b; i >= t; i--) mat[i][l] = num++; // bottom to top.
            l++;
        }
        return mat;
    }
  ```
- LCR146.螺旋遍历二维数组
  待做

## 区间和

### 介绍
求给定区间的和

### 思路
前缀和

### 例题
(卡码网KamaCoder)  
- 58.区间和
  https://kamacoder.com/problempage.php?pid=1070
- 44.开发商购买土地
  https://kamacoder.com/problempage.php?pid=1044

## 总结
<img src="images/数组总结.png" alt="img 加载失败">
  

