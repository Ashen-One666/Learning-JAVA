package org.fizz;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DisjointSetTest {

    private int[] a;
    private DisjointSet ds;

    @Test
    public void test() {
        a = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int size = a.length;
        boolean flag = false;
        ds = new DisjointSet(size);

        ds.union(0, 5);
        ds.union(1, 6);
        ds.union(2, 7);
        ds.union(3, 8);
        ds.union(4, 9);

        flag = ds.isConnected(0,5);
        System.out.println("0, 5 ?" + flag);
        flag = ds.isConnected(0,6);
        System.out.println("0, 6 ?" + flag);

        ds.union(5, 6);
        flag = ds.isConnected(0,6);
        System.out.println("0, 6 ?" + flag);

    }

    public static int search(int[]nums, int l, int r, int target){
        int mid = 0;
        while (l <= r) {
            mid = l + (r - l) / 2;
            if(nums[mid] > target){
                r = mid - 1;
            }
            else if(nums[mid] < target){
                l = mid + 1;
            }
            else{
                return mid;
            }
        }
        return -1;
    }
}
