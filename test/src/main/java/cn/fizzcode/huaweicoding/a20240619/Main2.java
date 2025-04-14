package cn.fizzcode.huaweicoding.a20240619;
import java.math.BigDecimal;
import java.util.*;

public class Main2 {
    public static void main(String[] args) {
        System.out.print(solve());
    }

    static BigDecimal solve() {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        int[] cnt = new int[128];
        int oddCnt = 0;

        for (char c : s.toCharArray()) {
            cnt[c]++;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            if ((cnt[c] & 1) == 1) oddCnt++;
        }
        if (oddCnt > 1) return new BigDecimal(0);

        int n = 0;
        for (char c = 'a'; c <= 'z'; c++) {
            cnt[c] /= 2;
            n += cnt[c];
        }

        // 预处理阶乘
        BigDecimal[] A = new BigDecimal[1002];
        A[0] = new BigDecimal(1);
        for (int i = 1; i <= 1001; i++) {
            A[i] = A[i - 1].multiply(new BigDecimal(i));
        }

        BigDecimal x = A[n];
        BigDecimal y = new BigDecimal(1);
        for (char c = 'a'; c <= 'z'; c++) {
            int k = cnt[c];
            if (k != 0) y = y.multiply(A[k]);
        }
        return x.divide(y, 2);
    }
}
