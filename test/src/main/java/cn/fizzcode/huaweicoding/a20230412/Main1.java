package cn.fizzcode.huaweicoding.a20230412;
import java.util.*;

// DAG动态规划 + 拓扑排序 （有向无环图最长路径）
public class Main1 {
    public static void main(String[] args) {
        solve();
    }

    static int n, m;
    static int[][] e;
    static int[] in;
    static int[] weight;
    static List<Node> ans = new ArrayList<Node>();

    static void solve() {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        e = new int[n + 1][n + 1];
        in = new int[n + 1];
        weight = new int[n + 1];
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            int w = sc.nextInt();
            // 注意，是v指向u
            e[v][u] = w;
            in[u]++;
        }

        topo();

        // 权重大的先输出，相等的再按id排序
        ans.sort((a, b) -> {
            if (a.w != b.w) return b.w - a.w;
            return a.id - b.id;
        });

        for (int i = 0; i < ans.size(); i++) {
            if (i == ans.size() - 1) System.out.print(ans.get(i).id);
            else System.out.print(ans.get(i).id + " ");
        }
    }

    static void topo() {
        Queue<Node> q = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            if (in[i] == 0) q.add(new Node(i, 0));
        }
        while (!q.isEmpty()) {
            Node u = q.poll();
            ans.add(u);
            for (int v = 1; v <= n; v++) {
                if (in[v] > 0 && e[u.id][v] > 0) {
                    in[v]--;
                    //System.out.println(u.id + " -> " + v);
                    weight[v] = Math.max(weight[v], u.w + e[u.id][v]);
                    if (in[v] == 0) {
                        //System.out.println(v + "的weight: " + weight[v]);
                        q.add(new Node(v, weight[v]));
                    }
                }
            }
        }
    }

    static class Node {
        int id, w;
        Node(int id, int w) {
            this.id = id;
            this.w = w;
        }
    }
}
