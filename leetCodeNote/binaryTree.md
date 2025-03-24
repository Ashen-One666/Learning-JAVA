## 二叉树遍历模板

## 模板
### 144. 二叉树的前序遍历
### 145. 二叉树的后序遍历
### 94. 二叉树的中序遍历

## 二叉树层序遍历

## 例题

### （模板题）102. 二叉树的层序遍历
- 思路：
  - 递归： 按深度，将深度为deep的节点放在ans的第 deep-1 个list中
  - 迭代： 使用队列queue，存储当前层的节点，每轮取出queue所有元素（当前层节点）并放入ans，然后把当前层的子节点再放入queue。
- 代码：
```java
class Solution {

    private List<List<Integer>> ans = new ArrayList<>();

    public List<List<Integer>> levelOrder(TreeNode root) {
        //levelSearch1(root, 1);
        levelSearch2(root);
        return ans;
    }

    // 递归
    private void levelSearch1(TreeNode cur, int deep){
        if(cur == null) return;
        // 首次到达新的一层时，为ans添加该层的list，此list大小为 2 ^ (deep - 1)
        if(ans.size() < deep)
            ans.add(new ArrayList<>());
        // 向这层的list中加入当前节点
        ans.get(deep - 1).add(cur.val);
        // 左孩子
        levelSearch1(cur.left, deep + 1);
        // 右孩子
        levelSearch1(cur.right, deep + 1);
    }

    // 非递归（队列）
    private void levelSearch2(TreeNode cur){
        if(cur == null) return;
        Queue<TreeNode> queue = new LinkedList<>(); // queue只存当前层的节点
        queue.offer(cur);
        while (!queue.isEmpty()) {
            List<Integer> curLevelNodes = new ArrayList<>();
            int len = queue.size(); // 记录当前queue大小，每轮只取当前层的节点
            while(len > 0){
                TreeNode tmp = queue.poll();
                curLevelNodes.add(tmp.val);
                if(tmp.left != null) queue.offer(tmp.left);
                if(tmp.right != null) queue.offer(tmp.right);
                len--;
            }
            ans.add(curLevelNodes);
        }
    }
}    
```

### 107. 二叉树的层序遍历 II
- 描述：返回自底向上的层序遍历
- 思路：在正序的层序遍历基础上将ans反转即可

### 199. 二叉树的右视图
- 描述：想象自己站在二叉树的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值
- 思路：在模板的基础上输出ans每层的最后一个元素

### 637.二叉树的层平均值
- 思路：在模板的基础上输出ans每层的平均值

### 116.填充每个节点的下一个右侧节点指针
### 117. 填充每个节点的下一个右侧节点指针 II

### 513. 找树左下角的值
- 思路：层序遍历后输出最后一层第一个元素

## 二叉树遍历

## 例题

### 101. 对称二叉树
- 思路：两个指针分别按 中-左-右 和 中-右-左 的顺序遍历，如果某步两个指针的值不相等则错误
- 代码：
```java
private boolean flag = true;
public boolean isSymmetric(TreeNode root) {
    dfs(root, root);
    return flag;
}

private void dfs(TreeNode cur1, TreeNode cur2){
    if((cur1 == null && cur2 != null) || (cur1 != null && cur2 == null)){
        flag = false;
        return;
    } 
    if(cur1 == null && cur2 == null) return;
    if(cur1.val != cur2.val){
        flag = false;
        return;
    }
    dfs(cur1.left, cur2.right);
    dfs(cur1.right, cur2.left);
}
```

### 257. 二叉树的所有路径
- 思路：前序遍历，到叶节点时将这条路径path上的所有结果存储到ans。回溯时取出path最后一个节点。
- 代码：
```java
List<String> ans = new ArrayList<>(); // 存储所有路径
List<Integer> path = new ArrayList<>(); // 记录每条路径的情况
public List<String> binaryTreePaths(TreeNode root) {
    dfs(root, path);
    return ans;
}

private void dfs(TreeNode cur, List<Integer> path){
    // 前序（放入节点时保证当前节点不为空）
    path.add(cur.val);
    // 到叶节点开始处理逻辑
    if(cur.left == null && cur.right == null){
        StringBuffer s = new StringBuffer();
        for(int i = 0; i < path.size() - 1; i++){
            s.append(path.get(i).toString()).append("->");
        }
        s.append(path.get(path.size() - 1).toString());    
        ans.add(s.toString());
        return;
    }
    if(cur.left != null){
        dfs(cur.left, path);
        path.remove(path.size() - 1); // 回溯
    }
    if(cur.right != null){
        dfs(cur.right, path);
        path.remove(path.size() - 1); // 回溯
    }
}
```

### 106. 从中序与后序遍历序列构造二叉树
- 思路：按后序数组最后一个元素作为切分点切分中序数组，再按中序数组的长度切分后序数组（每棵子树的中序后序数组长度一致）。当前节点的值是切分点。
       递归 + 切分实现构造过程。
- 代码：
```java
Map<Integer, Integer> map = new HashMap<>();

public TreeNode buildTree(int[] inorder, int[] postorder) {
    for(int i = 0; i < inorder.length; i++)
        map.put(inorder[i], i);
    TreeNode root = build(0, inorder.length - 1, 0, postorder.length - 1, postorder);
    return root;
}

private TreeNode build(int l1, int r1, int l2, int r2, int[] postorder){
    if(l1 > r1 || l2 > r2) return null;
    int curRoot = postorder[r2];
    int sep = map.get(curRoot);
    
    TreeNode cur = new TreeNode(curRoot);
    // 写左右边界时保证inorder和postorder长度一致就是对的，即 r2-l2 = r1-l1
    cur.left = build(l1, sep - 1, l2, l2 + sep - 1 - l1, postorder);
    cur.right = build(sep + 1, r1, r2 - r1 + sep, r2 - 1, postorder);// 这里postorder的右边界要-1，去掉curRoot那个节点
    return cur;
}
```

## 二叉树深度

### 104. 二叉树的最大深度
- 思路：dfs

### 111. 二叉树的最小深度
- 思路：与最大深度类似，不过是只在叶节点（左右孩子都为空）更新深度

## 二叉树高度
- 二叉树节点的深度：指从根节点到该节点的最长简单路径边的条数。
- 二叉树节点的高度：指从该节点到叶子节点的最长简单路径边的条数。

## 例题

### 110. 平衡二叉树
- 介绍：判断一个二叉树是否是平衡二叉树
- 思路：后序遍历（从底向上处理才能剪枝），当前树的高度 = max(左子树高度, 右子树高度) + 1。如果当前树深度超过2则不是平衡二叉树。
- 代码：
```java
public boolean isBalanced(TreeNode root) {
    if(getHeight(root) == -1) return false;
    return true;
}

// recur(TreeNode cur)：以cur为根的子树高度
// -1: 不是平衡二叉树
private int getHeight(TreeNode cur){
    if(cur == null) return 0; // 到底了，高度为0
    int left = getHeight(cur.left);
    if(left == -1) return -1;
    int right = getHeight(cur.right);
    if(right == -1) return -1;
    // 后序遍历
    if(Math.abs(left - right) < 2)
        return Math.max(left, right) + 1;
    else 
        return -1;
}
```

## 二叉搜索树
### 验证二叉搜索树
- 判断树是否是二叉搜索树，即左子树均小于当前节点，右子树均大于当前节点
- 思路： 中序遍历，得到的数组是递增的则满足

## 最近公共祖先
### 236. 二叉树的最近公共祖先
- 描述： 寻找两个点 p, q 在树上的最近公共祖先
- 思路： 后序遍历，用子树返回的结果判断根节点是否是最近公共祖先。 利用回溯从底向上搜索，遇到一个节点的左子树里有p，右子树里有q，那么当前节点就是最近公共祖先。
- 代码：
```java
class Solution {
    // 后序遍历，通过子树结果判断根节点是否是最近公共祖先
    // 递归方法返回值：以当前节点为根的子树中包含的是p还是q
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 当root为p或q其中一个时就返回，因为再深p或q就没有了
        if(root == null || root == p || root == q){
            return root;
        }
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if(left == null && right == null) return null; // p, q都没找到，那就没有
        if(left == null) return right; // 右子树中存在p或q
        if(right == null) return left; // 左子树中存在p或q
        return root; // 左右子树都存在，说明p, q分别位于左右子树上，此时root就是最近公共祖先
    }
}
```

### 235. 二叉搜索树的最近公共祖先
- 思路： 利用二叉搜索树的性质，从上到下搜索，如果当前节点cur值在 [p, q] 区间内则停止搜索，此节点就是最近公共祖先。
  如果cur > q，则向左搜，如果cur < p，则向右搜
- 代码：
```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        int l = Math.min(p.val, q.val), r = Math.max(p.val, q.val);
        if(root.val >= l && root.val <= r) return root;
        if(root.left != null && root.val > r) return lowestCommonAncestor(root.left, p, q);
        if(root.right != null && root.val < l) return lowestCommonAncestor(root.right, p, q);
        return null;
    }
}
```

## 二叉搜索树
### 450. 删除二叉搜索树指定节点
- 描述： 删除二叉搜索树指定节点（节点值为key的节点，保证所有节点不一样）
- 思路： 有返回值递归：方法返回值为修改后当前子树的根节点（通过 root.left/right = dfs() 来接值）
- 代码：
```java
class Solution {
    // 有参数递归，返回值为当前子树更新后的根节点
    public TreeNode deleteNode(TreeNode root, int key) {
        if(root == null) return null;
        // 通过 root.left/right = deleteNode() 来接值
        if(root.val < key) root.right = deleteNode(root.right, key);
        else if(root.val > key) root.left = deleteNode(root.left, key);
        else{
            // 当前节点是需要删除的节点
            // 返回值为删除操作之后当前子树的根节点
            if(root.left == null) return root.right;
            if(root.right == null) return root.left;
            findMaxNode(root.left).right = root.right;
            return root.left;
        }
        return root;
    }

    // 寻找当前子树中最右的节点
    private TreeNode findMaxNode(TreeNode cur){
        while(cur.right != null) cur = cur.right;
        return cur;
    }
}
```