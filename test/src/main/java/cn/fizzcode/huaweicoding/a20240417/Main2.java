package cn.fizzcode.huaweicoding.a20240417;
import java.util.*;

public class Main2 {
    public static void main(String[] args) {
        solve();
    }

    static class TreeNode {
        int val;
        Set<TreeNode> children;
        TreeNode() {
            children = new HashSet<>();
        }
    }

    static int M, N;
    static Map<String, TreeNode> map = new HashMap<>();
    static Set<String> roots = new HashSet<>();

    static void solve() {
        Scanner sc = new Scanner(System.in);
        M = sc.nextInt();
        N = sc.nextInt();
        int ans = 0;

        // 建树
        for (int i = 0; i < N; i++) {
            String v = sc.next();
            String u = sc.next();
            int type = sc.nextInt();
            int num = sc.nextInt();

            // 添加u, v
            if (!u.equals("*")) {
                map.putIfAbsent(u, new TreeNode());
            }
            map.putIfAbsent(v, new TreeNode());
            // 计算节点值
            TreeNode child = map.get(v);
            if (type == 0) child.val += 5 * num;
            else child.val += 2 * num;
            // 装配子节点
            if (!u.equals("*")) {
                TreeNode parent = map.get(u);
                parent.children.add(child);
            }
            // 记录根节点
            if (u.equals("*")) {
                roots.add(v);
            }
        }

        // dfs
        for (String root : roots) {
            int cnt = dfs(map.get(root));
            if (cnt > M) ans++;
            //System.out.println("root = " + root + ", cnt = " + cnt);
        }

        System.out.println(ans);
    }

    static int dfs(TreeNode cur) {
        if (cur.children.isEmpty()) return cur.val;
        int sum = 0;
        for (TreeNode child : cur.children) {
            sum += dfs(child);
        }
        return sum + cur.val;
    }
}
