package cn.fizzcode.huaweicoding.a20240424;
import java.util.*;

public class Main1 {
    public static void main(String[] args) {
        solve();
    }

    static void solve() {
        Scanner sc = new Scanner(System.in);
        String[] input = sc.nextLine().split(" ");
        int n = input.length;
        int[] node = new int[n];
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            node[i] = Integer.parseInt(input[i]);
        }
        int target = sc.nextInt();
        boolean flag = false;

        Arrays.sort(node);
        ans.append("S");

        int l = 0;
        int r = n - 1;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (node[mid] == target) {
                flag = true;
                break;
            }
            else if (node[mid] < target) {
                l = mid + 1;
                ans.append("R");
            }
            else {
                r = mid - 1;
                ans.append("L");
            }
        }

        if (flag) ans.append("Y");
        else ans.append("N");

        System.out.print(ans);
    }
}
