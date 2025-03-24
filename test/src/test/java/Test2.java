import java.util.*;
public class Test2 {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        List<Integer> ans = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] a = new int[n];
        List<String> s = new ArrayList<>();
        for(int i = 0; i < n; i++){
            a[i] = sc.nextInt();
        }
        for(int i = 0; i < n; i += k){
            StringBuffer tmp = new StringBuffer();
            for(int j = i; j < i + k; j++){
                tmp.append(a[j] + " ");
            }
            String str = tmp.toString();
            if(!map.containsKey(str)){
                map.put(str, 1);
                s.add(str);
            }
            else map.put(str, map.get(str) + 1);
        }
        for(int i = 0; i < s.size() - 1; i++){
            String str = s.get(i);
            System.out.print(str + map.get(str) + " ");
        }
        String str = s.get(s.size() - 1);
        System.out.print(str + map.get(str));
    }
}
