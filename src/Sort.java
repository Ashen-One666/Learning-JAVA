import java.util.*;

// 手写多种排序
public class Sort {
    // ----------------- 快排 -----------------
    static class QSort{
        static Random random = new Random();

        static public int[] qsort(int[] nums, int l, int r){
            if(nums == null || nums.length == 0) return nums;
            sort(nums, l, r);
            return nums;
        }

        static void sort(int[] nums, int l, int r){
            if(l > r) return;
            int p = random.nextInt(r - l) + l;
            swap(nums, p, r);
            int i = l;
            for(int j = l + 1; j < r; j++){
                if(nums[j] <= nums[r]){
                    swap(nums, i, j);
                    i++;
                }
            }
            swap(nums, i, r);

            sort(nums, l, p - 1);
            sort(nums, p + 1, r);
        }

        static void swap(int[] nums, int i, int j){
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }
    }

    // ----------------- 归并 -----------------
    static class MSort{
        static public int[] msort(int[] nums, int l, int r){
            if(nums == null || nums.length == 0) return nums;
            sort(nums, l, r);
            return nums;
        }

        static void sort(int[] nums, int l, int r){
            if(l > r) return;
            int mid = (r - l) / 2 + l;
            sort(nums, l, mid);
            sort(nums, mid + 1, r);
            merge(nums, l, mid, r);
        }

        static void merge(int[] nums, int l, int mid, int r){
            int[] tmp = new int[r - l + 1];
            int pos = 0;
            int i = l, j = mid + 1;
            while(i <= mid && j <= r){
                if(nums[i] <= nums[j])
                    tmp[pos++] = nums[i++];
                else
                    tmp[pos++] = nums[j++];
            }
            while(i <= mid) tmp[pos++] = nums[i++];
            while(j <= r) tmp[pos++] = nums[j++];
            System.arraycopy(tmp, 0, nums, l, r - l + 1);
        }
    }

    // ----------------- 冒泡 -----------------
    static class BSort{
        static int[] bsort(int[] nums, int l, int r){
            if(nums == null || nums.length == 0) return nums;
            sort(nums, l, r);
            return nums;
        }

        static void sort(int[] nums, int l, int r){
            int n = r - l + 1;
            // 外层i计次数，内层j交换
            for(int i = 0; i < n - 1; i++){
                boolean swapped = false;
                for(int j = l; j < r - i - 1; j++){
                    if(nums[j] > nums[j + 1]){
                        swap(nums, j, j + 1);
                        swapped = true;
                    }
                }
                if(!swapped) break;
            }
        }

        static void swap(int[] nums, int i, int j){
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }
    }

    // ----------------- 测试 -----------------
    public static void main(String[] args){
        int[] nums = {10, 5, 6, 23, 3, 1, 0};

    }
}
