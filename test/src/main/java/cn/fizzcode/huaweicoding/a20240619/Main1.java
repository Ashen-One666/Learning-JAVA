package cn.fizzcode.huaweicoding.a20240619;
import java.util.*;

public class Main1 {
    public static void main(String[] args) {
        solve();
    }

    static int n;
    static List<Integer>[] g;
    static int[] in;

    static void solve() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        g = new ArrayList[n + 1];
        in = new int[n + 1];
        for (int i = 1; i <= n; i++) g[i] = new ArrayList<>();
        int v = 1;
        while (sc.hasNext()) {
            int m = sc.nextInt();
            for (int i = 0; i < m; i++) {
                int u = sc.nextInt();
                g[u].add(v);
                in[v]++;
            }
            v++;
        }

        System.out.print(topoSort());
    }

    static int topoSort() {
        int level = 0;
        Queue<Integer> q = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            if (in[i] == 0) q.add(i);
        }
        while (!q.isEmpty()) {
            int size = q.size();
            level++;
            while (size-- > 0) {
                int u = q.poll();
                for (int v : g[u]) {
                    in[v]--;
                    if (in[v] == 0) {
                        q.add(v);
                    }
                }
            }
        }
        for (int i = 1; i <= n; i++) {
            if (in[i] != 0) return -1;
        }
        return level;
    }
}
