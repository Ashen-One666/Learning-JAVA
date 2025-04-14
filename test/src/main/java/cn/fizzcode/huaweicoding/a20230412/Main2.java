package cn.fizzcode.huaweicoding.a20230412;
import java.util.*;

public class Main2 {
    public static void main(String[] args) {
        System.out.print(solve());
    }

    static int n;
    static long[] a;
    static long max;

    static int solve() {
        Scanner sc = new Scanner(System.in);
        String[] input = sc.nextLine().split(" ");
        n = input.length;
        a = new long[n];
        long sumA = 0;
        for (int i = 0; i < n; i++) {
            a[i] = Long.parseLong(input[i]);
            sumA += a[i];
        }
        max = sc.nextInt();

        if (sumA <= max) return -1;

        int l = 0;
        int r = 100001;
        int ans = 0;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (check(mid) == 1) r = mid - 1;
            else if (check(mid) == -1){
                ans = mid;
                //System.out.println(mid);
                l = mid + 1;
            }
            else return mid;
        }
        return ans;
    }

    static int check(int value){
        long sum = 0;
        for (long x : a) {
            if (x > value) sum += value;
            else sum += x;
        }
        if (sum > max) return 1;
        else if (sum < max) return -1;
        return 0;
    }
}
