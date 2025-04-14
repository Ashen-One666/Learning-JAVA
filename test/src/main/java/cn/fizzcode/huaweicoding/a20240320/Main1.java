package cn.fizzcode.huaweicoding.a20240320;
import java.util.*;

public class Main1 {
    static class Pair{
        int x;
        int y;
        public Pair(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    static void solve(){
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        int n = sc.nextInt();
        int x = sc.nextInt();

        int ans = 0;


        List<Pair> a = new ArrayList<>();

        for(int i = 0; i < x; i++){
            Pair p = new Pair(sc.nextInt(), sc.nextInt());
            a.add(p);
        }

        // 二进制枚举
        for(int bit = 0; bit < (1 << x); bit++){
            List<Pair> b = new ArrayList<>();
            int[] cnt = new int[n]; // 第i个站点时被占据的座位数量
            boolean flag = true;

            // 记录当前状态下选取的乘客
            for(int i = 0; i < x; i++){
                if((bit >> i & 1) == 1){
                    b.add(a.get(i));
                }
            }

            // 遍历每个站点，判断当前站点上乘客数量是否超限
            for(int i = 0; i < n; i++){
                for(Pair p : b){
                    // 当前乘客在该站点时需占据一个座位
                    if(i >= p.x && i < p.y) cnt[i]++;
                    if(cnt[i] > m){
                        flag = false;
                        break;
                    }
                }
                if(!flag) break;
            }

            // 如果当前枚举bit合法，更新ans
            if(flag){
                int tmp = 0;
                for(Pair p : b){
                    tmp += p.y - p.x;
                }
                ans = Math.max(ans, tmp);
            }
        }

        System.out.print(ans);
    }

    public static void main(String[] args) {
        solve();
    }
}
