package cn.fizzcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    // 快速排序，使用 big、small、equal 划分
    private static void quickSort(List<Integer> nums) {
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
    public static int[] sortArray(int[] nums) {
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

    public static void main(String[] args) {
        int[] nums = {5, 2, 9, 1, 5, 6};
        int[] sortedArray = sortArray(nums);
        System.out.println("Sorted Array: ");
        for (int num : sortedArray) {
            System.out.print(num + " ");
        }
    }
}