package cn.fizzcode.huaweicoding.a20250409;
import java.util.*;

public class Main2 {
    static public void main(String[] args){
        solve();
    }

    static int n;
    static long[][] g;
    static char start, end;
    static long[] dis;
    static char[] pre;
    static List<Character> ans = new ArrayList<>();

    static void solve(){
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        g = new long[128][128];
        pre = new char[128];
        dis = new long[128];
        sc.nextLine();
        start = sc.next().charAt(0);
        end = sc.next().charAt(0);
        sc.nextLine();
        while(sc.hasNextLine()){
            String inputStr = sc.nextLine();
            if(inputStr.equals("0000")) break;
            String[] input = inputStr.split(" ");
            char u = input[0].charAt(0);
            char v = input[1].charAt(0);
            long w = Long.parseLong(input[2]);
            g[u][v] = w;
            g[v][u] = w;
        }

        dij();

        char cur = end;
        while(cur != 0){
            ans.add(cur);
            cur = pre[cur];
        }

        Collections.reverse(ans);
        for(int i = 0; i < ans.size(); i++){
            if(i == ans.size() - 1) System.out.print(ans.get(i));
            else System.out.print(ans.get(i) + " ");
        }
    }

    static void dij(){
        Arrays.fill(dis, Long.MAX_VALUE);
        dis[start] = 0L;
        PriorityQueue<Node> q = new PriorityQueue<>((a, b) -> Long.compare(a.d, b.d));
        q.add(new Node(start, 0));

        while(!q.isEmpty()){
            Node cur = q.poll();
            char u = cur.x;

            for(char v = 'a'; v <= 'z'; v++){
                if(g[u][v] != 0 && dis[v] > dis[u] + g[u][v]) {
                    pre[v] = u;
                    dis[v] = dis[u] + g[u][v];
                    q.add(new Node(v, dis[v]));
                }
            }
        }
    }

    static class Node{
        char x;
        long d;
        public Node(char x, long d){
            this.x = x;
            this.d = d;
        }
    }
}
