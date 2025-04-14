import java.util.Arrays;
import java.util.Scanner;

public class Test6 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] factorCount = new int[n + 1]; // 存储每个数的质因子个数

        for (int i = 2; i <= n; i++) {
            if (factorCount[i] == 0) { // 说明 i 是质数
                for (int j = i; j <= n; j += i) {
                    factorCount[j]++; // 统计 i 是 j 的一个质因子
                }
            }
        }

        System.out.println(Arrays.toString(factorCount));
    }
}
