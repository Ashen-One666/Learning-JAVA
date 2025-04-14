package cn.fizzcode.huaweicoding.a20230426;
import java.util.*;

public class Main1 {
    static public void main(String[] args) {
        solve();
    }

    static int n;
    static boolean[][] edge;
    static int[] in;

    static void solve() {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        edge = new boolean[n + 1][n + 1];
        in = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int m = sc.nextInt();
            for (int j = 0; j < m; j++) {
                int x = sc.nextInt();
                edge[x][i] = true;
                in[i]++;
            }
        }
        System.out.print(bfs());
    }

    static int bfs() {
        Queue<Integer> q = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            if (in[i] == 0) q.add(i);
        }

        int level = 0;
        while (!q.isEmpty()){
            int size = q.size();
            level++;
            while (size-- > 0) {
                int u = q.poll();
                for (int v = 1; v <= n; v++){
                    if (in[v] != 0 && edge[u][v]){
                        in[v]--;
                        if (in[v] == 0) q.add(v);
                    }
                }
            }
        }
        for (int i = 1; i <= n; i++){
            if (in[i] > 0) return -1;
        }
        return level;
    }
}
