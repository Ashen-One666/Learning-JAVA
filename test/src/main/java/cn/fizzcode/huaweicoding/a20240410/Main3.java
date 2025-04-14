package cn.fizzcode.huaweicoding.a20240410;
import java.util.*;

public class Main3 {
    public static void main(String[] args) {
        solve();
    }

    static int[][] g;
    static int[] exposed;
    static boolean[] visited;
    static int[] access;
    static int R = 0; // 当前dfs的R
    static int minR = Integer.MAX_VALUE; // 全局dfs的R
    static int ans = -1;

    static void solve() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        g = new int[n][n];
        access = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                g[i][j] = sc.nextInt();
            }
        }
        sc.nextLine();
        String input = sc.nextLine();
        String[] in = input.split(" ");
        exposed = new int[in.length];
        for (int i = 0; i < exposed.length; i++) {
            exposed[i] = Integer.parseInt(in[i]);
        }

        visited = new boolean[n];
        Arrays.sort(exposed);

        // 枚举所有exposed节点
        for (int choose : exposed) {
            // 初始化
            Arrays.fill(visited, false);
            R = 0;
            Arrays.fill(access, 0);
            for (int x : exposed) {
                if (x == choose) continue;
                access[x] = 10;
            }
            // dfs
            for (int x : exposed) {
                if(!visited[x] && x != choose) dfs(x);
            }

            // 统计R
            for (int i = 0; i < n; i++) {
                if(visited[i]) R++;
            }

            if (R < minR){
                minR = R;
                ans = choose;
            }
            //System.out.println("choose = " + choose + ", R = " + R);
        }

        System.out.print(ans);
    }

     static void dfs(int u) {
        visited[u] = true;
        for (int v = 0; v < g[u].length; v++) {
            if (v == u) continue;
            if (!visited[v] && access[u] >= g[u][v] && g[u][v] > 0) {
                //System.out.println("u = " + u + ", v = " + v + ", g[u][v] = " + g[u][v]);
                /**
                 * DFS 可能先用一个较低权限访问到某个点，标记 visited[v] = true,
                 * 后面可能有更高权限能访问这个点，但它已经被标记 true，不会再访问。
                 * 导致这个点的权限没被正确更新，影响后续传播。
                 * 因此本题应该使用bfs，并且节点在出队时才设置 visited[v] = true，就能避免上述问题
                 */
                access[v] = Math.max(access[v], g[u][v]);
                dfs(v);
            }
        }
    }
}
