/**
 * 题目：
 *  枚举删除a[]中每一个元素，计算当前a[]的mex值
 *  mex定义：数组中第一个未出现的非负整数
 */

import java.util.*;
public class Test8 {
    public static void main(String[] args) {
        calculateMex();
    }

    static void calculateMex() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        List<Integer> ans = new ArrayList<>();
        // 使用TreeSet存储不存在的元素
        Set<Integer> set = new TreeSet<>();
        // 使用HashMap存储arr中每个元素出现次数
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }

        for (int i = 0; i < n; i++) {
            map.put(a[i], map.getOrDefault(a[i], 0) + 1);
        }

        for (int i = 0; i <= n; i++) {
            if (!map.containsKey(i)) set.add(i);
        }

        // 枚举
        for (int i = 0; i < n; i++) {
            int x = a[i];
            boolean flag = false;
            if (map.get(x) == 1) flag = true;
            if (flag) set.add(x);

            ans.add(set.iterator().next()); // 取set第一个元素，即为mex值

            if (flag) set.remove(x);
        }
        System.out.println(ans.toString());
    }
}
