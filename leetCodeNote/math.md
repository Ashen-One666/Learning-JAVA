# 质因数
## 求解数组中每个数的质因数个数
### 方法
埃式筛法预处理
### 代码
```java
public static int[] countPrimeFactorsSieve(int n) {
    int[] factorCount = new int[n + 1]; // 存储每个数的质因子个数

    for (int i = 2; i <= n; i++) {
        if (factorCount[i] == 0) { // 说明 i 是质数
            for (int j = i; j <= n; j += i) {
                factorCount[j]++; // 统计 i 是 j 的一个质因子
            }
        }
    }
    return factorCount;
}
```

## 求解数组中每个数是否为质数
### 方法
埃式筛法预处理
### 代码
```java
public static int countPrimes(int n) {
    if (n < 2) return 0;
    boolean[] isPrime = new boolean[n];
    Arrays.fill(isPrime, true);
    isPrime[0] = isPrime[1] = false;
    
    for (int i = 2; i * i < n; i++) {
        if (isPrime[i]) {
            for (int j = i * i; j < n; j += i) {
                isPrime[j] = false;
            }
        }
    }

    int count = 0;
    for (boolean prime : isPrime) {
        if (prime) count++;
    }
    return count;
}
```

