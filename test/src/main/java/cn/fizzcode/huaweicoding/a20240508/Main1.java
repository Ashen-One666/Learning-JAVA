package cn.fizzcode.huaweicoding.a20240508;
import java.util.*;

public class Main1 {
    public static void main(String[] args) {
        solve();
    }

    static void solve() {
        Scanner sc = new Scanner(System.in);
        int[] a = new int[32];
        while (sc.hasNextLine()) {
            String[] input = sc.nextLine().split(" ");
            String op = input[0];
            String dst = input[1];
            int dstIndex = Integer.parseInt(dst.substring(1));

            // PRINT
            if (op.equals("PRINT")) {
                System.out.println(a[dstIndex]);
            }

            // MOV
            else if (op.equals("MOV")) {
                String src = input[2];
                if (src.charAt(0) == 'a') {
                    int srcIndex = Integer.parseInt(src.substring(1));
                    a[dstIndex] = a[srcIndex];
                }
                else {
                    int srcVal = Integer.parseInt(src);
                    a[dstIndex] = srcVal;
                }
                //System.out.println("MOV a[" + dstIndex + "] = " + a[dstIndex]);
            }

            // 运算符
            else {
                String src0 = input[2];
                String src1 = input[3];
                int val0 = 0;
                int val1 = 0;
                if (src0.charAt(0) == 'a') {
                    val0 = a[Integer.parseInt(src0.substring(1))];
                }
                else {
                    val0 = Integer.parseInt(src0);
                }
                if (src1.charAt(0) == 'a') {
                    val1 = a[Integer.parseInt(src1.substring(1))];
                }
                else {
                    val1 = Integer.parseInt(src1);
                }

                // ADD
                if (op.equals("ADD")) {
                    a[dstIndex] = val0 + val1;
                    //System.out.println("ADD a[" + dstIndex + "] = " + val0 + " + " + val1);
                }
                if (op.equals("SUB")) {
                    a[dstIndex] = val0 - val1;
                    //System.out.println("ADD a[" + dstIndex + "] = " + val0 + " - " + val1);
                }
                if (op.equals("MUL")) {
                    a[dstIndex] = val0 * val1;
                    //System.out.println("MUL a[" + dstIndex + "] = " + val0 + " * " + val1);
                }
                if (op.equals("DIV")) {
                    if ((val0 < 0 && val1 > 0) || (val0 > 0 && val1 < 0)) {
                        int mod = val0 % val1;
                        if (mod == 0) {a[dstIndex] = val0 / val1;}
                        else {a[dstIndex] = val0 / val1 - 1;}
                    }
                    else a[dstIndex] = val0 / val1;
                    //System.out.println("DIV a[" + dstIndex + "] = " + val0 + " / " + val1);
                }
            }
        }
    }
}
