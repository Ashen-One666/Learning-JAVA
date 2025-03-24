## 字符串反转（双指针）

### 例题
#### 151. 反转字符串中的单词
- 思路：  
  双指针：  
  - 倒序遍历字符串 s ，记录单词左右索引边界 i , j。
  - 每确定一个单词的边界，则将其添加至单词列表 res 。
  - 最终，将单词列表拼接为字符串，并返回即可。
- 代码1：
```java
public String reverseWords(String s) {
    StringBuffer ans = new StringBuffer("");
    s = s.trim();
    // i指向单词第一个字母的前一个位置（空格），j指向单词最后一个字母
    int j = s.length() - 1, i = j;
    while(i >= 0 && j >= 0){
        while(i >= 0 && s.charAt(i) != ' ') i--;
        ans.append(s.substring(i + 1, j + 1));
        ans.append(" ");
        j = i - 1;
        while(j >= 0 && s.charAt(j) == ' ') j--; // 去掉单词间多的空格
        i = j;
    }
    ans.deleteCharAt(ans.length() - 1);
    return ans.toString();
}
```
- 代码2（使用库函数）：  
注意：以空格为分割符完成字符串分割后，若两单词间有 x>1 个空格，则在单词列表 strs 中，此两单词间会多出 x−1 个 “空单词”  
解决：遇到空单词 "" 时跳过即可
```java
    public String reverseWords(String s) {
        String[] strs = s.trim().split(" ");        // 删除首尾空格，分割字符串
        StringBuilder res = new StringBuilder();
        for (int i = strs.length - 1; i >= 0; i--) { // 倒序遍历单词列表
            if (strs[i].equals("")) continue;        // 遇到空单词则跳过
            res.append(strs[i] + " ");              // 将单词拼接至 StringBuilder
        }
        return res.toString().trim();               // 转化为字符串，删除尾部空格，并返回
    }
```

## KMP

### 模板
```java
    // 匹配（返回第一个匹配项的下标）
    public int firstIndex(String s, String t){
        if(t.isEmpty()) return 0;
        int[] next = new int[t.length()];
        getNext(next, t);
        int j = 0;
        for(int i = 0; i < s.length(); i++){
          while(j > 0 && t.charAt(i) != t.charAt(j))
              j = next[j - 1];
          if(t.charAt(i) == t.charAt(j))
              j++;
          if(j == t.length())
              return i - t.length() + 1;
        }
        return -1;
    }
    
    // 匹配（返回t在s中出现的次数）
    public int count(String s, String t){
        if(t.isEmpty()) return 0;
        int sum = 0;
        int[] next = new int[t.length()];
        getNext(next, t);
        int j = 0;
        for(int i = 0; i < s.length(); i++){
            while(j > 0 && t.charAt(i) != t.charAt(j))
                j = next[j - 1];
            if(t.charAt(i) == t.charAt(j))
                j++;
            if(j == t.length()){
                sum++;
                j = next[j - 1];
            }
        }
        return sum;
    }
    
    // 获取next数组
    public void getNext(int[] next, String t){
        next[0] = 0;
        int j = 0; // j指向前缀的末尾+1，同时j也代表当前字串的最长公共前后缀的长度
        for(int i = 1; i < t.length(); i++){
            while(j > 0 && t.charAt(i) != t.charAt(j))
                j = next[j - 1];
            if(t.charAt(i) == t.charAt(j))
                j++;
            next[i] = j; // 如果不相等，此时next[i] = j = 0
        }
    }
```

### 例题

#### 28. 找出字符串中第一个匹配项的下标
模板题，kmp能够解决匹配问题

#### 459. 重复的子字符串
- 说明：  
  判断字符串是否能由其某个字串构成
- 思路：  
  如果一个字符串满足题意，那么这个字串的形式为 "abcabcabc"，而kmp中 next[i] 表示以i结尾的字串最长公共前后缀。  
  如果这个字符串s是由重复子串组成，那么最长相等前后缀不包含的子串是字符串s的最小重复子串。
- 代码：
```java
public boolean repeatedSubstringPattern(String s) {
  int n = s.length();
  if(n == 0) return false;
  int[] next = new int[n];
  getNext(next, s);
  // maxPre: 最长相等前后缀
  int maxPre = next[n - 1];
  // n - maxPre: 最长相等前后缀不包含的子串(即s的最小重复子串)
  if(maxPre != 0 && n % (n - maxPre) == 0) return true;
  return false;
}

public void getNext(int[] next, String t){
  next[0] = 0;
  int j = 0; // j指向前缀的末尾+1，同时j也代表当前字串的最长公共前后缀的长度
  for(int i = 1; i < t.length(); i++){
    while(j > 0 && t.charAt(i) != t.charAt(j))
      j = next[j - 1];
    if(t.charAt(i) == t.charAt(j))
      j++;
    next[i] = j; // 如果不相等，此时next[i] = j = 0
  }
}    
```

