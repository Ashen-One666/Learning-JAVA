package cn.fizzcode.huaweicoding.a20240417;
import java.util.*;

public class Main1 {
    public static void main(String[] args) {
        solve();
    }

    static void solve() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        String input = sc.nextLine();
        String[] s = input.split(" ");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            list.add(s[i]);
            if (list.size() <= 2) {
                continue;
            }

            while (list.size() >= 3) {
                int p = list.size() - 1;
                String s2 = list.get(p);
                String s1 = list.get(p - 1);
                String s0 = list.get(p - 2);
                if (s2.equals(s1) && s1.equals(s0)) {
                    list.remove(p);
                    list.remove(p - 1);
                    list.remove(p - 2);
                }
                else break;
            }
        }

        if (list.isEmpty()) System.out.print(0);
        else {
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) System.out.print(list.get(i));
                else System.out.print(list.get(i) + " ");
            }
        }
    }

}
