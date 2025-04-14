package cn.fizzcode.huaweicoding.a20240410;
import java.util.*;

public class Main1 {
    public static void main(String[] args) {
        solve();
    }

    static int M, N;
    static List<String[]> input = new ArrayList<>();
    static Map<String, Integer> fee = new HashMap<>();
    static Set<String> used = new HashSet<>();
    static Map<String, Integer> ans = new TreeMap<>();

    static void solve() {
        Scanner sc = new Scanner(System.in);
        M = sc.nextInt();
        for (int i = 0; i < M; i++) {
            String s = sc.next();
            input.add(s.split(","));
        }
        input.sort((o1, o2) -> o1[0].compareTo(o2[0]));
        N = sc.nextInt();
        for (int i = 0; i < N; i++) {
            String s = sc.next();
            String[] arr = s.split(",");
            fee.put(arr[0], Integer.parseInt(arr[1]));
        }

        for (String[] arr : input) {
            StringBuilder sb = new StringBuilder();
            sb.append(arr[0]).append(",").append(arr[1]).append(",").append(arr[2]);
            if (used.contains(sb.toString())) continue;
            used.add(sb.toString());
            //System.out.println(Arrays.toString(arr));
            String client = arr[1];
            String factor = arr[2];
            int cost = fee.getOrDefault(factor, 0);
            int time = Integer.parseInt(arr[3]);
            if(time < 0 || time > 100) time = 0;
            int sum = cost * time;
            ans.put(client, sum + ans.getOrDefault(client, 0));
        }

        for (Map.Entry<String, Integer> entry : ans.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }
    }
}
