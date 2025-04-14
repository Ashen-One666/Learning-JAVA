package cn.fizzcode.huaweicoding.a20240320;
import java.util.*;

public class Main2 {

    static void solve() {
        Scanner sc = new Scanner(System.in);
        List<Integer> list = new ArrayList<>();

        String[] in = sc.nextLine().split(" ");
        for(String s : in) {list.add(Integer.valueOf(s));}

        List<Integer> ans = new ArrayList<>();
        for(int cur : list){
            ans.add(cur);
            boolean flag = true;

            while(flag){
                int p = ans.size() - 1;
                int top = ans.get(p);
                p--;
                int sum = 0;
                while(p >= 0){
                    sum += ans.get(p);
                    if(sum >= top){break;}
                    else p--;
                }
                // 判断是否要取出木块
                if(sum == top){
                    //System.out.println(" 当前指针：" + p);
                    for(int i = ans.size() - 1; i >= p; i--){
                        ans.remove(i);
                    }
                    ans.add(top * 2);
                }
                else flag = false;
            }

            //System.out.println(ans);
        }

        for(int i = ans.size() - 1; i >= 0; i--){
            if(i == 0) System.out.print(ans.get(i));
            else System.out.print(ans.get(i) + " ");
        }
    }

    public static void main(String[] args) {
        solve();
    }
}
