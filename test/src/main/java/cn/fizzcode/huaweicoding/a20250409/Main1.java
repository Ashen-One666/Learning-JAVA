package cn.fizzcode.huaweicoding.a20250409;
import java.util.*;

public class Main1 {
    static public void main(String[] args) {
        solve();
    }

    static int n;
    static Set<String> set = new HashSet<>();
    static Map<String, List<String>> map = new HashMap<>();
    static int maxLen = 0;
    static int len = 0; // 用于每次dfs
    static List<String> slist = new ArrayList<>(); // 用于记录每次dfs得到的字符串
    static List<String> ans = new ArrayList<>();
    static Set<String> vis = new HashSet<>();

    static void solve() {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        sc.nextLine();
        for(int i = 0; i < m; i++){
            String v = sc.next();
            String u = sc.next();
            map.putIfAbsent(u, new ArrayList<>());
            map.get(u).add(v);
        }

        // dfs
        for(String root : map.get("NA")){
            len = 0;
            slist.clear();
            dfs(root, 1);
            if (len > maxLen) {
                ans.clear();
                ans.addAll(slist);
                maxLen = len;
            }
            else if (len == maxLen) {
                ans.addAll(slist);
            }
        }

        //System.out.println(maxLen);

        Collections.sort(ans);
        for(int i = 0; i < ans.size(); i++){
            if(i == ans.size() - 1) System.out.print(ans.get(i));
            else System.out.print(ans.get(i) + " ");
        }
    }

    static void dfs(String u, int depth) {

        if(map.get(u) == null) {
            if (len < depth){
                slist.clear();
                slist.add(u);
                len = depth;
            }
            else if (len == depth) {
                slist.add(u);
            }
            //System.out.println(u + ", depth = " + depth);
            return;
        }

        for(String v : map.get(u)){
            if(!vis.contains(v)){
                vis.add(v);
                dfs(v, depth + 1);
            }
        }
    }
}
