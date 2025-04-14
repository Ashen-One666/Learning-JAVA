package cn.fizzcode.huaweicoding.a20240417;
import java.util.*;

public class Main3 {
    public static void main(String[] args) {
        solve();
    }

    static int n;
    static int root;
    static int need;
    static int[][] g;
    static int[] capacity;
    static int[] dis;
    static boolean[] visited;
    static List<int[]> disArr = new ArrayList<>(); // 节点编号 + 距离
    static List<Integer> ans = new ArrayList<>();

    static void solve() {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        g = new int[n][n];
        capacity = new int[n];
        dis = new int[n];
        visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                g[i][j] = sc.nextInt();
            }
        }
        for (int i = 0; i < n; i++) {
            capacity[i] = sc.nextInt();
        }
        root = sc.nextInt();
        need = sc.nextInt();

        dijkstra();

        for (int i = 0; i < n; i++) {
            if (i == root) continue;
            disArr.add(new int[]{i, dis[i]});
        }
        disArr.sort((a, b) -> {
            if (a[1] == b[1]) return a[0] - b[0];
            return a[1] - b[1];
        });

        int sum = 0;
        for (int[] pair : disArr) {
            int node = pair[0];
            int distance = pair[1];
            // 不够的情况，直接退出，ans中是全部的容灾节点
            if (distance == Integer.MAX_VALUE) break;
            ans.add(node);
            sum += capacity[node];
            if (sum >= need) break;
        }

        for (int i = 0; i < ans.size(); i++) {
            if (i == ans.size() - 1) System.out.print(ans.get(i));
            else System.out.print(ans.get(i) + " ");
        }

    }

    static void dijkstra() {
        Arrays.fill(dis, Integer.MAX_VALUE);
        PriorityQueue<int[]> q = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        q.add(new int[]{0, root});
        dis[root] = 0;
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int dist = cur[0];
            int u = cur[1];
            if (visited[u]) continue;
            visited[u] = true;
            for (int v = 0; v < n; v++) {
                if (!visited[v] && g[u][v] != -1 && dis[u] + g[u][v] < dis[v]) {
                    dis[v] = dis[u] + g[u][v];
                    q.add(new int[]{dis[v], v});
                }
            }
        }
    }
}
