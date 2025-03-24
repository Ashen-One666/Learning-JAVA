import java.util.Objects;

public class 最长公共前缀_14 {
    public static class Solution {
        public String longestCommonPrefix(String[] strs) {
            if(strs[0].isEmpty())
                return "";
            StringBuffer ans = new StringBuffer();
            boolean flag = false;
            for(int i = 0; i < 200; i++){
                if(flag) break;
                if(i > strs[0].length() - 1) break;
                char s0_x = strs[0].charAt(i);
                // 遍历每个str的i号元素
                for (String s : strs) {
                    if(s.isEmpty())
                        return "";
                    if (i > s.length() - 1 || s.charAt(i) != s0_x) {
                        flag = true;
                        break;
                    }
                }
                if(!flag)
                    ans.append(s0_x);
            }

            return ans.toString();
        }
    }

    public static void main(String[] args){
        Solution sol = new Solution();
        String[] strs = {"a"};
        String ans = sol.longestCommonPrefix(strs);
        System.out.print(ans);
    }
}
