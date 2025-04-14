package cn.fizzcode.huaweicoding.a20240410;
import java.util.*;

public class Main2 {
    public static void main(String[] args) {
        solve();
    }

    static class UFSet {
        int n;
        int[] parent;
        UFSet(int n) {
            this.n = n;
            parent = new int[n];
            for (int i = 0; i < n; i++)
                parent[i] = i;
        }

        int find(int x) {
            if (parent[x] == x) return x;
            return parent[x] = find(parent[x]);
        }

        void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            if (rootP == rootQ) return;
            parent[rootP] = rootQ;
        }

        boolean connected(int p, int q) {
            return find(p) == find(q);
        }
    }

    static void solve() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] g = new int[n][n];
        boolean[] isIsland = new boolean[n];
        int[] sum = new int[n];
        List<Integer> ans = new ArrayList<>();

        Arrays.fill(isIsland, true);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                g[i][j] = sc.nextInt();
            }
        }


        UFSet uf = new UFSet(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (g[i][j] != 0) {
                    uf.union(i, j);
                    isIsland[i] = false;
                    isIsland[j] = false;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (g[i][j] != 0) {
                    int fa = uf.find(j);
                    sum[fa] += g[i][j];
                }
            }
        }

        int isIslandCount = 0;
        for (int i = 0; i < n; i++) {
            if (isIsland[i]) {
                isIslandCount++;
            }
        }

        for (int i = 0; i < isIslandCount; i++) {
            ans.add(0);
        }
        for (int i = 0; i < n; i++) {
            if (sum[i] != 0) ans.add(sum[i]);
        }

        ans.sort(Collections.reverseOrder());
        for (int i = 0; i < ans.size(); i++) {
            if (i == ans.size() - 1) System.out.println(ans.get(i));
            else System.out.print(ans.get(i) + " ");
        }
    }
}
