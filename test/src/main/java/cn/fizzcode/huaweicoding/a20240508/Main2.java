package cn.fizzcode.huaweicoding.a20240508;
import java.util.*;

/**
 * LFU 缓存
 *  LeetCode 460 原题
 *  做法：
 *  Node 存储每个键值对的 访问次数，最新访问时间，键，值
 *  HashMap 存储 key : Node
 *  TreeSet 维护 Node 的顺序 （先按 访问次数 排序，再按 访问时间 排序）
 */

public class Main2 {

    // 节点：访问次数，访问时间，键，值
    static class Node {
        int cnt, time, key, value;
        public Node(int cnt, int time, int key, int value) {
            this.cnt = cnt;
            this.time = time;
            this.key = key;
            this.value = value;
        }
    }

    static class LFUCache {
        int capacity;
        // 当前时间（每次操作时间 + 1）
        int curTime;
        // HashMap存储缓存内容
        Map<Integer, Node> map;
        // TreeSet维护节点顺序
        Set<Node> set;

        public LFUCache(int capacity) {
            this.capacity = capacity;
            this.curTime = 0;
            map = new HashMap<>(capacity);
            set = new TreeSet<>((a, b) -> {
                if (a.cnt != b.cnt) return Integer.compare(a.cnt, b.cnt);
                return Integer.compare(a.time, b.time);
            });
        }

        public int get(int key) {
            if (capacity == 0 || !map.containsKey(key)) return -1;
            curTime++;
            Node node = map.get(key);
            set.remove(node);
            node.cnt += 1;
            node.time = curTime;
            // 更新set中的Node
            set.add(node);
            // 更新map中的Node
            map.put(key, node);
            return node.value;
        }

        public void put(int key, int value) {
            if (capacity == 0) return;
            curTime++;
            Node node = map.get(key);

            // 不存在该节点
            if (node == null) {
                // 如果缓存溢出，按 先LFU再LRU 清理缓存
                if (map.size() == capacity) {
                    Node eldestNode = set.iterator().next();
                    set.remove(eldestNode);
                    map.remove(eldestNode.key);
                }
                node = new Node(1, curTime, key, value);
                set.add(node);
                map.put(key, node);
            }
            // 存在该节点，更新值 并 更新计数和访问时间
            else {
                set.remove(node);
                node.cnt++;
                node.time = curTime;
                // 更新值
                node.value = value;
                set.add(node);
                map.put(key, node);
            }
        }
    }

    public static void main(String[] args) {
        solve();
    }

    static void solve() {
        Scanner sc = new Scanner(System.in);
        int capacity;
        sc.nextLine(); // 跳过 "capacity:"
        capacity = Integer.parseInt(sc.nextLine());

        LFUCache cache = new LFUCache(capacity);

        while (sc.hasNextLine()) {
            String op = sc.nextLine();

            if (op.equals("read:")) {
                int key = Integer.parseInt(sc.nextLine());
                cache.get(key);
                //Debug(op, cache);
            }

            if (op.equals("write:")) {
                int n = Integer.parseInt(sc.nextLine());
                for (int i = 0; i < n; i++) {
                    String[] input = sc.nextLine().split(" ");
                    int key = Integer.parseInt(input[0]);
                    int value = Integer.parseInt(input[1]);
                    cache.put(key, value);
                }
                //Debug(op, cache);
            }

            if (op.equals("query:")) {
                int key = Integer.parseInt(sc.nextLine());
                System.out.print(cache.get(key));
            }
        }
    }

    static void Debug(String op, LFUCache cache) {
        Map<Integer, Node> map = cache.map;
        Set<Node> set = cache.set;
        System.out.println(op);
        System.out.println("Map:");
        for (Map.Entry<Integer, Node> entry : map.entrySet()) {
            Node node = entry.getValue();
            System.out.println("cnt:" + node.cnt + ", time:" + node.time + ", key:" + node.key + ", value:" + node.value);
        }
        System.out.println("Set:");
        for (Node node : set) {
            System.out.println("cnt:" + node.cnt + ", time:" + node.time + ", key:" + node.key + ", value:" + node.value);
        }
        System.out.println();
    }
}
