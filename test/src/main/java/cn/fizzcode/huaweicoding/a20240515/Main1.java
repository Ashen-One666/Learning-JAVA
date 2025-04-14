package cn.fizzcode.huaweicoding.a20240515;
import java.util.*;

public class Main1 {
    public static void main(String[] args) {
        solve();
    }

    static class LRUCache {
        Map<Integer, Integer> map;
        int capacity;
        public LRUCache(int capacity) {
            this.capacity = capacity;
            map = new LinkedHashMap<>();
        }

        public int get(int key) {
            if (capacity == 0 || !map.containsKey(key)) return -1;
            int val = map.get(key);
            map.remove(key);
            map.put(key, val);
            return val;
        }

        public void put(int key, int value) {
            if (capacity == 0) return;
            if (!map.containsKey(key)) {
                if (map.size() == capacity) {
                    int eldestKey = map.keySet().iterator().next();
                    map.remove(eldestKey);
                }
                map.put(key, value);
            }
            else {
                map.remove(key);
                map.put(key, value);
            }
        }

        public void remove(int key) {
            map.remove(key);
        }

        public void print() {
            List<Integer> ans = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                ans.add(entry.getKey());
            }
            for (int i = ans.size() - 1; i >= 0; i--) {
                if (i == 0) System.out.print(ans.get(i));
                else System.out.print(ans.get(i) + " ");
            }
        }
    }

    static void solve() {
        Scanner in = new Scanner(System.in);
        int capacity = in.nextInt();
        int K = in.nextInt();
        LRUCache cache = new LRUCache(capacity);
        for (int i = 0; i < K; i++) {
            String op = in.next();
            int key = Integer.parseInt(in.next());
            if (op.equals("A")){
                cache.put(key, 0);
            }
            if (op.equals("Q")){
                cache.get(key);
            }
            if (op.equals("D")){
                cache.remove(key);
            }
        }
        cache.print();
    }
}
