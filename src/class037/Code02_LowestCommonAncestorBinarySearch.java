package class037;

// 搜索二叉树上寻找两个节点的最近公共祖先
// 测试链接 : https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-search-tree/
public class Code02_LowestCommonAncestorBinarySearch {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		// root从上到下
		// 如果先遇到了p，说明p是答案
		// 如果先遇到了q，说明q是答案
		// 如果root在p~q的值之间，不用管p和q谁大谁小，只要root在中间，那么此时的root就是答案
		// 如果root在p~q的值的左侧，那么root往右移动
		// 如果root在p~q的值的右侧，那么root往左移动
		while (root.val != p.val && root.val != q.val) {
			if (Math.min(p.val, q.val) < root.val && root.val < Math.max(p.val, q.val)) {
				break;
			}
			root = root.val < Math.min(p.val, q.val) ? root.right : root.left;
		}
		return root;
	}

	public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        while (root != null) {
            if (root.val > p.val && root.val > q.val) {
                root = root.left;
            } else if (root.val < p.val && root.val < q.val) {
                root = root.right;
            } else {
                return root;
            }
        }
        return null;
    }

}

## Approach: Leverage BST Property

### Key Insight
- BST property: Left < Root < Right
- If both p and q **< root** → LCA in left subtree
- If both p and q **> root** → LCA in right subtree
- Otherwise → root is LCA (split point or equals p/q)
time complexity: O(h) where h is the height of the tree
space complexity: O(1) for iterative, O(h) for recursive due to call stack

## Common Mistakes

### ❌ Mistake 1: Only check one node
```java
if (root.val < p.val) {  // ❌ Only checks p
    root = root.right;
}
```
✅ **Fix**: Check both p and q
```java
if (root.val < p.val && root.val < q.val) {
    root = root.right;
}
```

---

### ❌ Mistake 2: Assume p < q
```java
// ❌ Assumes p.val < q.val
if (root.val < p.val) {
    return lowestCommonAncestor(root.right, p, q);
}
```
✅ **Fix**: Always check both values independently

---

### ❌ Mistake 3: Use Binary Tree approach
```java
// ❌ Unnecessary post-order traversal
TreeNode left = lowestCommonAncestor(root.left, p, q);
TreeNode right = lowestCommonAncestor(root.right, p, q);
if (left != null && right != null) return root;
```
✅ **Fix**: BST allows value comparison, no need to search both sides

---

### ❌ Mistake 4: Wrong loop condition
```java
while (root != p || root != q) {  // ❌ Always true
```
✅ **Fix**: No special loop condition needed, just `while (root != null)`

---

### ❌ Mistake 5: Forget split case
```java
if (root.val > p.val && root.val > q.val) {
    root = root.left;
} else {
    root = root.right;  // ❌ Missing split point case
}
```
✅ **Fix**: Always include else to return root


