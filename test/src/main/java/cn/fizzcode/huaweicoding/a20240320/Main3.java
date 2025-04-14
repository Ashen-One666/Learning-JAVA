package cn.fizzcode.huaweicoding.a20240320;
import java.util.*;

/**
 * 题意：在有向图中寻找环
 * 方法：
 *      color标记： 0代表未访问， 1代表正在当前dfs路径上， 2代表该节点不可能在环上
 *      当一个节点首次访问时标1
 *      当dfs遍历时遇到color=1的节点时，说明碰到环了
 *      当dfs结束仍然没碰到环时，将当前节点color改为2并移出dfs路径，说明当前节点肯定不在环上
 */
public class Main3 {

    static int n = 100010;
    static Map<Integer, List<Integer>> g = new HashMap<>(); // 邻接表
    static Set<Integer> nodes = new HashSet<>(); // 记录所有节点
    static int[] color = new int[n]; // 0：未访问 1：在当前路径上 2：不可能在环上
    static List<Integer> path = new ArrayList<>(); // 记录当前dfs路径
    static List<Integer> circle = new ArrayList<>(); // 记录环
    static List<Integer> ans = new ArrayList<>(); // 整理环，输出答案

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        for (int i = 0; i < N; i++) {
            int num = sc.nextInt();
            // 当前节点u
            int u = sc.nextInt();
            g.putIfAbsent(u, new ArrayList<>());
            nodes.add(u);
            for (int j = 0; j < num - 1; j++) {
                int v = sc.nextInt();
                g.get(u).add(v); // 建边
                g.putIfAbsent(v, new ArrayList<>());
                nodes.add(v);
            }
        }

        // dfs
        for (int u : nodes) {
            if(color[u] == 0) dfs(u);
        }

        // 寻找最小节点在ans中的位置
        int index = 0;
        int minNode = Integer.MAX_VALUE;
        for (int i = 0; i < circle.size(); i++) {
            if(circle.get(i) < minNode) {
                minNode = circle.get(i);
                index = i;
            }
        }

        // 整理环并打印
        for (int i = index; i >= 0; i--) {
            ans.add(circle.get(i));
        }
        for (int i = circle.size() - 1; i > index; i--) {
            ans.add(circle.get(i));
        }
        ans.add(ans.get(0));

        for (int i = 0; i < ans.size(); i++) {
            if (i == ans.size() - 1) System.out.print(ans.get(i));
            else System.out.print(ans.get(i) + " ");
        }
    }

    static void dfs(int u){
        // 再次访问到在当前dfs路径上的节点时，说明遇到环了
        if (color[u] == 1) {
            //System.out.println("find cycle!");
            // 将path中的环放到ans中，注意ans中顺序是倒序的
            // 依次取出path中的节点，直到碰到u，说明已经将path中的环全部取出了
            while(path.get(path.size() - 1) != u){
                circle.add(path.get(path.size() - 1));
                path.remove(path.size() - 1);
            }
            circle.add(u); // 最后将u放入环中
            return;
        }
        // 标记并将u加入path
        color[u] = 1;
        path.add(u);


        for (int v : g.get(u)) {
            if (color[v] != 2) dfs(v);
        }

        // 当dfs到头依然没发现环时，说明末尾的点不在环上
        color[u] = 2; // 对u的访问已结束
        if(!path.isEmpty())
            path.remove(path.size() - 1);
    }
}
