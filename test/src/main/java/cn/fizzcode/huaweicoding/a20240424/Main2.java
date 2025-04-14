package cn.fizzcode.huaweicoding.a20240424;
import java.util.*;

public class Main2 {
    public static void main(String[] args) {
        solve();
    }

    static class A {
        int a0;
        int a1;
        String a2;
        int a3;
        public A(int a0, int a1, String a2, int a3) {
            this.a0 = a0;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
        }
    }

    static int n,m;
    static String[] arr;
    static List<A> list = new ArrayList<>();

    static void solve() {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        arr = new String[n];
        sc.nextLine();
        arr = sc.nextLine().split(" ");
        for (int i = 0; i < n; i++) {
            String s = arr[i];
            int a0 = 0;
            int a1 = 0;
            String a2 = s;
            int a3 = i;

            // a0
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == '1') a0++;
            }
            // a1
            int len = 0;
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == '0') len = 0;
                else {
                    len++;
                    a1 = Math.max(a1, len);
                }
            }

            list.add(new A(a0, a1, a2, a3));
        }

        list.sort((a, b) -> {
            if (a.a0 != b.a0) return b.a0 - a.a0;
            if (a.a1 != b.a1) return b.a1 - a.a1;
            if (!a.a2.equals(b.a2)) {
                String s0 = a.a2;
                String s1 = b.a2;
                for (int i = 0; i < m; i++) {
                    if (s0.charAt(i) == s1.charAt(i)) continue;
                    if (s0.charAt(i) != s1.charAt(i) && s0.charAt(i) == '0') return 1;
                    else return -1;
                }
            }
            return a.a3 - b.a3;
        });

        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) System.out.print(list.get(i).a3 + 1);
            else System.out.print(list.get(i).a3 + 1 + " ");
        }
    }
}
