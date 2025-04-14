package cn.fizzcode.huaweicoding.a20240515;
import java.util.*;

/**
 * 01背包，判断数组是否能正好分为sum相等的两部分，并输出这两部分
 * 判断能否均分方法： 01背包dp
 * 输出两部分方法： 记录 背包容量到达j时 是用物品i进行填充的
 */
public class Main3 {
    public static void main(String[] args) {
        solve();
    }

    static void solve() {
        Scanner in = new Scanner(System.in);
        int capacity = in.nextInt();
        int n = in.nextInt();
        int[] a = new int[n];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            a[i] = in.nextInt();
            sum += a[i];
        }
        if (sum != capacity * 2) {
            System.out.print(-1);
            return;
        }

        boolean[] dp = new boolean[capacity + 1];
        dp[0] = true;
        boolean[][] path = new boolean[n][capacity + 1]; // 记录路径（此处必须是二维数组，否则会丢失信息）
        for (int i = 0; i < n; i++) {
            for (int j = capacity; j >= a[i]; j--) {
                // 当前dp[j]是由dp[j - a[i]]转移而来时，记录这个路径
                if (dp[j - a[i]] && !dp[j]) {
                    dp[j] = true;
                    path[i][j] = true; // 记录：物品i放入了容量为j的背包
                }
            }
        }

        if (!dp[capacity]) {
            System.out.print(-1);
            return;
        }

        // 记录两部分使用的物品的编号
        List<Integer> part1 = new ArrayList<>();
        List<Integer> part2 = new ArrayList<>();
        boolean[] used = new boolean[n];

        // 统计path路径
        //System.out.print("path路径： ");
        int j = capacity;
        while (j > 0) {
            for (int i = n - 1; i >= 0; i--) {
                if (path[i][j]) {
                    used[i] = true;
                    j -= a[i];
                    //System.out.print(a[i] + " ");
                }
            }
        }
        //System.out.println();

        // 分配
        for (int k = 0; k < n; k++) {
            if (used[k]) part1.add(a[k]);
            else part2.add(a[k]);
        }

        Collections.sort(part1);
        Collections.sort(part2);

        if (part1.get(0) > part2.get(0) ||
                (part1.get(0).equals(part2.get(0)) && part1.size() < part2.size())) {
            List<Integer> tmp = part1;
            part1 = part2;
            part2 = tmp;
        }

        print(part1);
        System.out.println();
        print(part2);
    }

    static void print(List<Integer> part) {
        for (int i = 0; i < part.size(); i++) {
            if (i == part.size() - 1) System.out.print(part.get(i));
            else System.out.print(part.get(i) + " ");
        }
    }
}
