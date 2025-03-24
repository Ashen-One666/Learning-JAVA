import org.junit.jupiter.api.Test;

class LowerBound {
    public static int findLowerBound(int[] arr, int x) {
        int left = 0, right = arr.length - 1;
        int result = -1; // 如果找不到lower_bound，返回-1

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] <= x) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }
}


public class Test1 {
    @Test
    public void test(){
        int[] arr = {1, 2, 5, 7, 9};
        int x = 10;

        int lowerBoundIndex = LowerBound.findLowerBound(arr, x);
        if (lowerBoundIndex != -1) {
            System.out.println("Lower bound index: " + lowerBoundIndex);
            System.out.println("Lower bound value: " + arr[lowerBoundIndex]);
        } else {
            System.out.println("No lower bound found for x = " + x);
        }
    }
}
