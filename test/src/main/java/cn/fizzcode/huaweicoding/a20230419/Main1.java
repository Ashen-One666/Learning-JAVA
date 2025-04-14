package cn.fizzcode.huaweicoding.a20230419;
import java.util.*;

public class Main1 {
    public static void main(String[] args) {
        solve();
    }

    static void solve() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] diff = new int[1000010];
        int start = Integer.MAX_VALUE;
        int end = 0;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            int l = sc.nextInt();
            int r = sc.nextInt();
            diff[l]++;
            diff[r + 1]--;
            start = Math.min(start, l);
            end = Math.max(end, r);
        }
        int now = 0;
        for (int i = start; i <= end; i++) {
            now += diff[i];
            if (now == 0) ans += 1;
            else if (now == 1) ans += 3;
            else ans += 4;
            //System.out.println("i = " + i + ", now = " + now + ", ans = " + ans);
        }
        System.out.print(ans);
    }
}
