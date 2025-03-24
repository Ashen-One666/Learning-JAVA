import java.util.*;
public class Test3 {

    public static void main(String[] args) {
        int n;
        int[] nums;
        int target;
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = sc.nextInt();
        }
        target = sc.nextInt();

        int div = finDiv(0, n - 1, nums, target);
        System.out.println("div = " + div);
        int ans1 = binSearch(0, div - 1, nums, target);
        System.out.println("ans1 = " + ans1);
        int ans2 = binSearch(div, n - 1, nums, target);
        System.out.println("ans2 = " + ans2);

        if(ans1 == -1) System.out.println("ans = " + ans2);
        else System.out.println("ans = " + ans1);

    }

    // 寻找分界点
    public static int finDiv(int l, int r, int[] nums, int target) {
        int n = nums.length;
        while(l <= r) {
            int mid = l + (r - l) / 2;
            // 中值大于尾部
            if(nums[mid] > nums[n - 1]){
                l = mid + 1;
            }
            // 中值小于等于尾部
            else{
                // mid为首部或在分界点
                if(mid == 0 || nums[mid - 1] > nums[mid]){
                    return mid;
                }
                r = mid - 1;
            }
        }
        return -1; // 不会到达此处
    }

    // 二分
    public static int binSearch(int l, int r, int[] nums, int target) {
        int n = nums.length;
        while(l <= r) {
            int mid = l + (r - l) / 2;
            if(nums[mid] == target){
                return mid;
            }
            else if(nums[mid] < target){
                l = mid + 1;
            }
            else{r = mid - 1;}
        }
        return -1;
    }
}
