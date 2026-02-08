package class037;

// 验证平衡二叉树
// 测试链接 : https://leetcode.cn/problems/balanced-binary-tree/
public class Code04_BalancedBinaryTree {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	public static boolean balance;

	public static boolean isBalanced(TreeNode root) {
		// balance是全局变量，所有调用过程共享
		// 所以每次判断开始时，设置为true
		balance = true;
		height(root);
		return balance;
	}

	// 一旦发现不平衡，返回什么高度已经不重要了
	public static int height(TreeNode cur) {
		if (!balance || cur == null) {
			return 0;
		}
		int lh = height(cur.left);
		int rh = height(cur.right);
		if (Math.abs(lh - rh) > 1) {
			balance = false;
		}
		return Math.max(lh, rh) + 1;
	}

	// 这个写法更好，不需要全局变量，直接用返回值来表示是否平衡，
	// 不会出现全局变量被污染的情况，同时比上一个写法早一层返回结果，效率更高
	public boolean isBalanced(TreeNode root) {
        return dfs(root) != -1;
    }
    
    private int dfs(TreeNode root) {
        if (root == null) return 0;
        
        int left = dfs(root.left);
        if (left == -1) return -1;
        
        int right = dfs(root.right);
        if (right == -1) return -1;
        
        if (Math.abs(left - right) > 1) return -1;
        
        return Math.max(left, right) + 1;
    }

}

## Approach: Post-Order Traversal with Early Exit

### Key Insight
- Use **post-order** (left → right → root): check children before parent
- **Early exit**: stop immediately when unbalanced detected
- Return **height** if balanced, **-1** (sentinel) if not

### Algorithm
1. Base case: null → return 0
2. Get left subtree height (exit if -1)
3. Get right subtree height (exit if -1)
4. Check difference: if > 1 → return -1
5. Return max(left, right) + 1
### Complexity
- Time: O(N) - visit each node once
- Space: O(H) - recursion stack, H = tree height (worst O(N))

