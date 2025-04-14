package cn.fizzcode.huaweicoding.a20240619;
import java.util.*;

public class Main3 {
    public static void main(String[] args) {
        solve();
    }

    static int x0, y0;
    static int n, m;
    static int[][] g;
    static boolean[][] visited;
    static int[][] dir = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    static List<Point> source = new ArrayList<Point>();

    static void solve() {
        Scanner sc = new Scanner(System.in);
        x0 = sc.nextInt() - 1;
        y0 = sc.nextInt() - 1;
        n = sc.nextInt();
        m = sc.nextInt();
        g = new int[n][m];
        visited = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                g[i][j] = sc.nextInt();
            }
        }
        // 标记
        dfs(x0, y0);
        //debug();
        // bfs搜最近距离
        System.out.print(bfs());
    }

    // 标记当前子网为2
    static void dfs(int x, int y) {
        if (!check(x, y) || visited[x][y] || g[x][y] == 0) return;
        visited[x][y] = true;
        g[x][y] = 2;
        source.add(new Point(x, y, -1));
        for (int[] d : dir) {
            int vx = x + d[0];
            int vy = y + d[1];
            dfs(vx, vy);
        }
    }

    static int bfs() {
        for (int i = 0; i < n; i++) Arrays.fill(visited[i], false);
        Queue<Point> q = new LinkedList<>(source);

        while (!q.isEmpty()) {
            Point p = q.poll();
            if (g[p.x][p.y] == 1) return p.d;

            for (int i = 0; i < 4; i++) {
                int vx = p.x + dir[i][0];
                int vy = p.y + dir[i][1];
                int vd = p.d + 1;
                if (!check(vx, vy) || visited[vx][vy] || g[vx][vy] == 2) continue;
                visited[vx][vy] = true;
                q.add(new Point(vx, vy, vd));
            }
        }
        return -1;
    }

    static boolean check(int x, int y) {
        if (x < 0 || x >= n || y < 0 || y >= m) return false;
        return true;
    }

    static void debug() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(g[i][j] + " ");
            }
            System.out.println();
        }
    }

    static class Point {
        int x, y, d;
        public Point(int x, int y, int d) {
            this.x = x;
            this.y = y;
            this.d = d;
        }
    }
}
