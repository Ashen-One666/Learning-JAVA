## 最短路
### dijkstra
- 优先队列的本质
  - 优先队列中存储的是那些与当前已确定最短路径的节点直接相连的节点及其当前最短距离（mindis）。
  - 每加入一个节点到最优路径中，则检查该节点直连的点是否可以更新已有的 mindis（尝试进行松弛操作）。
- 一个形象的比喻
  - 可以把 Dijkstra 算法想象成“灯塔扩展光亮”的过程：
    - 起点 s 就是最早点亮的灯塔，优先队列存储的是目前被光照到的区域边界。
    - 每次从优先队列中取出离灯光最近的区域（即最短路径上的下一个点）。 
    - 当一个新区域被光照到时（松弛成功），将它连接的更远的区域加入候选范围（入队）。
- 模板（记录距离和最短路径）
```java
import java.util.*;
import java.lang.Math;

public class Main {

    static int n, m;
    static List<LinkedList<int[]>> graph = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        for (int i = 0; i < n + 1; i++)
            graph.add(new LinkedList<>());

        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            graph.get(u).add(new int[]{v, w});
        }

        // Call Dijkstra and get both distance and predecessor array
        int[][] result = dijkstra(1, n);
        int[] dis = result[0];
        int[] pre = result[1];

        if (dis[n] == Integer.MAX_VALUE) {
            System.out.print(-1); // No path
        } else {
            System.out.println("Shortest Distance: " + dis[n]);
            List<Integer> path = getPath(pre, n); // Recover the path
            System.out.println("Path: " + path);
        }
    }

    static int[][] dijkstra(int s, int n) {
        PriorityQueue<int[]> pq
                = new PriorityQueue<>(new Comparator<int[]>(){
            @Override
            public int compare(int[] a, int[] b){
                return a[1] - b[1];
            }
        });

        boolean[] vis = new boolean[n + 1];
        int[] dis = new int[n + 1];
        int[] pre = new int[n + 1]; // To store the predecessor of each node
        Arrays.fill(dis, Integer.MAX_VALUE);
        Arrays.fill(pre, -1); // Initialize all predecessors to -1

        dis[s] = 0;
        pq.add(new int[]{s, 0});

        while (!pq.isEmpty()) {
            int u = pq.peek()[0];
            int d = pq.peek()[1];
            pq.poll();

            if (vis[u]) continue;
            vis[u] = true;

            for (int[] edge : graph.get(u)) {
                int v = edge[0];
                int w = edge[1];
                if (!vis[v] && dis[u] + w < dis[v]) {
                    dis[v] = dis[u] + w;
                    pre[v] = u; // Update predecessor
                    pq.add(new int[]{v, dis[v]});
                }
            }
        }
        return new int[][]{dis, pre}; // Return both distance and predecessor arrays
    }

    static List<Integer> getPath(int[] pre, int target) {
        List<Integer> path = new ArrayList<>();
        for (int at = target; at != -1; at = pre[at]) {
            path.add(at);
        }
        Collections.reverse(path); // Reverse the path to get the correct order
        return path;
    }
}
```

