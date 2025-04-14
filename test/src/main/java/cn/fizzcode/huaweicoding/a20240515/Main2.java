package cn.fizzcode.huaweicoding.a20240515;
import java.util.*;

/**
 * 模拟栈 + KMP
 */
public class Main2 {
    public static void main(String[] args) {
        solve();
    }

    static void solve() {
        Scanner in = new Scanner(System.in);
        String s0 = in.nextLine();
        String text = in.nextLine();
        Deque<Character> st = new LinkedList<>();
        for (char c : s0.toCharArray()) {
            if (c != ')') {
                st.addLast(c);
            }
            else {
                Deque<Character> q = new LinkedList<>();
                // 获取字符
                while (!st.isEmpty() && st.peekLast() != '(') {
                    q.addFirst(st.removeLast());
                }
                // 去掉'('
                st.removeLast();
                // 获取计数
                StringBuilder sb = new StringBuilder();
                while (!st.isEmpty() && Character.isDigit(st.peekLast())) {
                    sb.insert(0, st.removeLast());
                }
                int cnt = Integer.parseInt(sb.toString());

                // 重新向stack中添加字符
                for (int i = 0; i < cnt; i++) {
                    for (char c1 : q) {
                        st.addLast(c1);
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (char c : st) sb.append(c);
        String pattern = sb.toString();

        int start = kmp(text, pattern);
        if (start == -1) System.out.print("!");
        else System.out.print(text.substring(start, start + pattern.length()));
    }

    // 原串 模式串
    static boolean isMatched(char c1, char c2) {
        //System.out.println("c1 = " + c1 + ", c2 = " + c2);
        if (c2 == 'A' && Character.isAlphabetic(c1)) return true;
        if (c2 == 'N' && Character.isDigit(c1)) return true;
        return false;
    }

    // ------------- KMP -------------
    /**
     * KMP
     * @param text 原串
     * @param pattern 模式串
     * @return 返回首次匹配到的的字串的起始位置，没有匹配到则为-1
     */
    static int kmp(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        int[] next = new int[pattern.length()];
        getNext(next, pattern);
        //System.out.println(Arrays.toString(next));
        //System.out.println("text = " + text + ", pattern = " + pattern);
        int j = 0; // 指向模式串，当前正在匹配的位置
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && !isMatched(text.charAt(i), pattern.charAt(j)))
                j = next[j - 1];
            if (isMatched(text.charAt(i), pattern.charAt(j)))
                j++;
            if (j == pattern.length()) return i - pattern.length() + 1;
        }
        return -1;
    }


    /**
     * 获取next数组
     *  next[i]含义： 字串[0,i]最长的公共前后缀长度
     *  （如"abbab"，next={0,0,0,1,2}）
     */
    static void getNext(int[] next, String pattern) {
        next[0] = 0;
        // j含义：指向最长前缀的末尾 + 1，也代表当前最长公共前后缀的长度
        int j = 0;
        for (int i = 1; i < pattern.length(); i++) {
            while (j > 0 && pattern.charAt(j) != pattern.charAt(i))
                j = next[j - 1]; // 由于j是指向 最长前缀的末尾 + 1 的位置，因此需要移到next[j-1]处，而不是next[j]处
            if (pattern.charAt(j) == pattern.charAt(i))
                j++;
            next[i] = j;
        }
    }
}
