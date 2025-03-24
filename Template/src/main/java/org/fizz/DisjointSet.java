package org.fizz;

// 并查集
public class DisjointSet {

    private int[] parent;
    private int[] rank;

    // 构造方法（初始化）
    public DisjointSet(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    // 查找（路径压缩）
    public int find(int p) {
        if (p < 0 || p >= parent.length) {
            throw new IllegalArgumentException("p is out of bound");
        }
        while (p != parent[p]) {
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    // 合并（按秩合并）
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if(rootP == rootQ) {
            return;
        }
        if (rank[rootP] > rank[rootQ]) {
            parent[rootP] = rootQ;
        }
        else if (rank[rootP] < rank[rootQ]) {
            parent[rootQ] = rootP;
        }
        else {
            parent[rootQ] = rootP;
            rank[rootP]++;
        }
    }

    // 判断是否属于同一集合
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

}
