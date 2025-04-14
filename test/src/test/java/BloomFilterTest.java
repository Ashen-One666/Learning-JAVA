import java.util.*;

public class BloomFilterTest {
    private static final int DEFAULT_CAPACITY = 1 << 24;
    private static final int[] HASH_SEEDS = {3, 7, 11, 13, 31, 37, 61};

    private BitSet bitset = new BitSet(DEFAULT_CAPACITY);
    private SimpleHash[] hashes;

    BloomFilterTest() {
        hashes = new SimpleHash[HASH_SEEDS.length];
        for (int i = 0; i < HASH_SEEDS.length; i++) {
            hashes[i] = new SimpleHash(DEFAULT_CAPACITY, HASH_SEEDS[i]);
        }
    }

    // 添加元素
    private void add(String value){
        for (int i = 0; i < HASH_SEEDS.length; i++) {
            bitset.set(hashes[i].hash(value), true);
        }
    }

    // 判断元素是否可能存在
    private boolean mightContains(String value){
        for (int i = 0; i < HASH_SEEDS.length; i++) {
            if (bitset.get(hashes[i].hash(value))) {
                return true;
            }
        }
        return false;
    }

    private static class SimpleHash {
        private final int capacity;
        private final int seed;
        private SimpleHash(int capacity, int seed) {
            this.capacity = capacity;
            this.seed = seed;
        }

        private int hash(String value) {
            int result = 0;
            for (int i = 0; i < value.length(); i++) {
                result = seed * result + value.charAt(i);
            }
            return result & (capacity - 1);
        }
    }



    public static void main(String[] args) {
        BloomFilterTest bloomFilterTest = new BloomFilterTest();
        String value1 = "hello";
        String value2 = "world";
        String value3 = "java";

        bloomFilterTest.add(value1);
        bloomFilterTest.add(value2);
        bloomFilterTest.add(value3);

        System.out.println(bloomFilterTest.mightContains(value1));
        System.out.println(bloomFilterTest.mightContains(value2));
        System.out.println(bloomFilterTest.mightContains(value3));
        System.out.println(bloomFilterTest.mightContains("python"));
    }
}
