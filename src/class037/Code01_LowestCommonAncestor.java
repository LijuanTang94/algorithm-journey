package class037;

// 普通二叉树上寻找两个节点的最近公共祖先
// 测试链接 : https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
public class Code01_LowestCommonAncestor {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		if (root == null || root == p || root == q) {
			// 遇到空，或者p，或者q，直接返回
			return root;
		}
		TreeNode l = lowestCommonAncestor(root.left, p, q);
		TreeNode r = lowestCommonAncestor(root.right, p, q);
		if (l != null && r != null) {
			// 左树也搜到，右树也搜到，返回root
			return root;
		}
		// l和r一个为空，一个不为空
		// 返回不空的那个
		return l != null ? l : r;
	}

}

## Approach: Post-order Traversal

### Key Insight
- Use **post-order traversal** (left → right → root)
- If a node finds **p in left** and **q in right** (or vice versa) → it's the LCA
- If both found in one subtree → LCA is deeper, bubble up the result

### Algorithm Logic
1. **Base case**: If node is null or equals p/q → return node
2. **Recurse**: Search left and right subtrees
3. **Both non-null**: Current node is LCA (p and q in different subtrees)
4. **One non-null**: Return the non-null side (both p and q are there)
time complexity: O(N) where N is the number of nodes in the tree (each node visited once)
space complexity: O(H) where H is the height of the tree (due to recursion stack