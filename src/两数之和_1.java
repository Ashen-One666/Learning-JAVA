import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class 两数之和_1 {
    public static class Solution {
        public int[] twoSum(int[] nums, int target) {
            Map<Integer, Integer> map = new HashMap<Integer, Integer>(nums.length);
            for (int i = 0; i < nums.length; i++){
                int x = nums[i];
                int y = target - nums[i];
                Integer exits = map.get(y);
                if(exits != null){
                    return new int[]{exits, i};
                }
                map.put(x, i);
            }
            throw new NullPointerException("No Solution");
        }

        public static void main(String[] args) {
            Solution sol = new Solution();
            int[] nums = {3, 2, 4};
            int target = 6;
            int[] ans = sol.twoSum(nums, target);
            System.out.print(Arrays.toString(ans));
        }
    }

}
