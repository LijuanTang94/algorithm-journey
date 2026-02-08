package class037;

// 二叉树打家劫舍问题
// 测试链接 : https://leetcode.cn/problems/house-robber-iii/
public class Code07_HouseRobberIII {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	time complexity: O(n) - each node visited once
	space complexity: O(h) - recursion stack, where h is tree height
	public static int rob(TreeNode root) {
		f(root);
		return Math.max(yes, no);
	}

	// 全局变量，完成了X子树的遍历，返回之后
	// yes变成，X子树在偷头节点的情况下，最大的收益
	public static int yes;

	// 全局变量，完成了X子树的遍历，返回之后
	// no变成，X子树在不偷头节点的情况下，最大的收益
	public static int no;

	public static void f(TreeNode root) {
		if (root == null) {
			yes = 0;
			no = 0;
		} else {
			int y = root.val;
			int n = 0;
			f(root.left);
			y += no;
			n += Math.max(yes, no);
			f(root.right);
			y += no;
			n += Math.max(yes, no);
			yes = y;
			no = n;
		}
	}

	---

## 方法2: 返回数组 (推荐 ⭐⭐⭐⭐⭐)
## Approach: Tree DP with Post-Order Traversal

### Key Insight
At each node, we have two choices:
1. **Rob current node**: Cannot rob children
2. **Skip current node**: Can rob or skip children (choose max)

Return both values as `[skip, rob]` and let parent decide.

time complexity: O(n) - each node visited once
space complexity: O(h) - recursion stack, where h is tree height

```java
public int rob(TreeNode root) {
    int[] result = dfs(root);
    return Math.max(result[0], result[1]);
}

// 返回 [不偷当前节点的最大值, 偷当前节点的最大值]
private int[] dfs(TreeNode root) {
    if (root == null) return new int[]{0, 0};
    
    int[] left = dfs(root.left);
    int[] right = dfs(root.right);
    
    // 不偷当前: 左右子树可偷可不偷，取最大
    int no = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
    
    // 偷当前: 左右子树都不能偷
    int yes = root.val + left[0] + right[0];
    
    return new int[]{no, yes};
}
```

---

## 核心对比

| 对比项 | 全局变量 | 返回数组 (推荐) |
|--------|---------|----------------|
| **清晰度** | ⭐⭐ 难追踪状态 | ⭐⭐⭐⭐⭐ 一目了然 |
| **线程安全** | ❌ 不安全 | ✅ 安全 |
| **易错性** | 高 (状态混淆) | 低 (逻辑独立) |
| **维护性** | 差 | 好 |
| **性能** | 稍快 (~8%) | 稍慢 (可忽略) |
| **推荐度** | ⭐⭐ | ⭐⭐⭐⭐⭐ |

---

## 全局变量的问题

### 1. 状态追踪困难
```java
f(root.left);   // yes 和 no 被修改了
y += no;        // 这里的 no 是左子树的 no
f(root.right);  // yes 和 no 又被修改了
y += no;        // 这里的 no 是右子树的 no ← 容易混淆!
```

### 2. 线程不安全
```java
// ❌ 多线程会出错
Thread t1 = new Thread(() -> rob(tree1));
Thread t2 = new Thread(() -> rob(tree2));
// yes 和 no 被共享，结果错误
```

### 3. 难以测试
```java
// ❌ 每次测试前需要重置
yes = 0;
no = 0;
rob(tree1);

yes = 0;  // 必须重置!
no = 0;
rob(tree2);
```

---

## 返回数组的优势

### 1. 代码清晰
```java
int[] left = dfs(root.left);   // 明确返回左子树结果
int[] right = dfs(root.right); // 明确返回右子树结果
int no = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
int yes = root.val + left[0] + right[0];
// ✅ 逻辑清晰，不会混淆
```

### 2. 线程安全
```java
// ✅ 多线程无问题
Thread t1 = new Thread(() -> rob(tree1));
Thread t2 = new Thread(() -> rob(tree2));
// 每次调用独立，互不影响
```

### 3. 易于扩展
```java
// 如果需要返回更多信息，直接扩展数组
return new int[]{no, yes, count, maxPath};
```

---

## 推荐使用: 返回数组

**理由**:
1. ✅ 代码清晰易懂
2. ✅ 线程安全
3. ✅ 易于维护和调试
4. ✅ 不易出错
5. ✅ 业界标准 (90% LeetCode 解法)
6. ✅ 性能差异可忽略 (~8%)

**全局变量仅适用于**: 极端性能要求 + 单线程 + 完全理解逻辑


}


