package cn.fizzcode.huaweicoding.a20240410;
import java.util.*;

public class Main3_2 {
    public static void main(String[] args) {
        solve();
    }

    static int n;
    static int[][] g;
    static boolean[] visited;
    static int[] exposed;
    static int[] access; // 权限
    static int minR = Integer.MAX_VALUE;
    static int ans = -1;

    static void solve() {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        g = new int[n][n];
        visited = new boolean[n];
        access = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                g[i][j] = sc.nextInt();
            }
        }
        sc.nextLine();
        String line = sc.nextLine();
        String[] input = line.split(" ");
        exposed = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            exposed[i] = Integer.parseInt(input[i]);
        }
        Arrays.sort(exposed);

        // 枚举下线的节点
        for (int choose : exposed) {
            // 初始化权限
            Arrays.fill(visited, false);
            Arrays.fill(access, 0);
            for (int x : exposed) {
                if (x == choose) continue;
                access[x] = 10;
            }

            int R = bfs();

            if (R < minR){
                minR = R;
                ans = choose;
            }
            //System.out.println("choose = " + choose + ", R = " + R);
        }
        System.out.print(ans);
    }

    static int bfs() {
        int R = 0;
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (access[i] == 10) q.add(i);
        }
        while (!q.isEmpty()) {
            int u = q.poll();
            if (visited[u]) continue;
            visited[u] = true;
            R++;
            for (int v = 0; v < n; v++) {
                if (!visited[v] && g[u][v] > 0 && access[u] >= g[u][v]) {
                    access[v] = Math.max(access[v], g[u][v]);
                    q.add(v);
                }
            }
        }
        return R;
    }
}
