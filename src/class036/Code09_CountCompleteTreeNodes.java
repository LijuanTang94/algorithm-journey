package class036;

// 求完全二叉树的节点个数
// 测试链接 : https://leetcode.cn/problems/count-complete-tree-nodes/
public class Code09_CountCompleteTreeNodes {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	public int countNodes(TreeNode root) {
    if (root == null) return 0;
    
    int leftDepth = getDepth(root.left);
    int rightDepth = getDepth(root.right);
    
    if (leftDepth == rightDepth) {
        // 左子树是满树，直接算 + 递归右子树
        return (1 << leftDepth) + countNodes(root.right);
    } else {
        // 右子树是满树，直接算 + 递归左子树
        return (1 << rightDepth) + countNodes(root.left);
    }
}

	private int getDepth(TreeNode node) {
		int depth = 0;
		while (node != null) {
			node = node.left;  // 只走左边
			depth++;
		}
		return depth;
	}
// 只访问 O(log²n) 个节点！
```
## Approach: Leverage Complete Binary Tree Property

### Key Insight
In a complete binary tree:
- **At least one subtree is perfect** (full binary tree)
- Perfect tree nodes = 2^h - 1 (use formula, no traversal needed)
- Only recurse into the imperfect subtree

### Algorithm
1. Calculate left boundary depth (go left only)
2. Calculate right boundary depth (go right only)
3. If depths equal → perfect tree → use formula: 2^h - 1
4. Otherwise → recurse into both subtrees

---

## 具体例子
```
完全二叉树，15个节点：

            1
          /   \
         2     3
        / \   / \
       4   5 6   7
      / \ / \
     8  9 10 11

你的算法访问的节点：
1. 根节点 1 → 计算深度，递归
2. 节点 2 → 计算深度，递归
3. 节点 4 → 计算深度，左右相等 → 返回 2^2-1=3
4. 节点 5 → 计算深度，左右相等 → 返回 2^1-1=1
5. 节点 3 → 计算深度，左右相等 → 返回 2^2-1=3

访问节点数：5个（不是15个！）
深度计算：每次 O(log n)
总复杂度：O(log²n)

暴力解法会访问所有15个节点 → O(n)

}
