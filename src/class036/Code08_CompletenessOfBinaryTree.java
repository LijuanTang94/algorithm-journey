package class036;

// 验证完全二叉树
// 测试链接 : https://leetcode.cn/problems/check-completeness-of-a-binary-tree/
public class Code08_CompletenessOfBinaryTree {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交以下的方法
	// 不把空节点加进去，但是一旦看到空节点了，后面就不能再有非空节点了
	public boolean isCompleteTree1(TreeNode root) {
        if (root == null) return true;
        Queue<TreeNode> q = new LinkedList<>();
        boolean seenNull = false;
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            if (cur.left == null) {
                seenNull = true;
            } else {
                if (seenNull) return false;
                q.offer(cur.left);
            }
            if (cur.right == null) {
                seenNull = true;
            } else {
                if (seenNull) return false;
                q.offer(cur.right);
            }
        }
        return true;

    }

	// 把空节点也加进去，一旦看到空节点了，后面就不能再有非空节点了
	public boolean isCompleteTree2(TreeNode root) {
        if (root == null) return true;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        boolean seenNull = false;
        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            if (cur == null) {
                seenNull = true;
            } else {
                if (seenNull) {
                    return false;
                }
                q.offer(cur.left);
                q.offer(cur.right);
            }
        }
        return true;
    }

}



# Check Completeness of a Binary Tree (LeetCode 958)

## Core Idea

A binary tree is **complete** if:
- All levels except possibly the last are completely filled
- All nodes in the last level are as far left as possible

To check this, we use **level-order traversal (BFS)**.

Key observation:
> **Once a `null` node is seen during BFS, all following nodes must also be `null`.  
If a non-null node appears after that, the tree is NOT complete.**

---

## Correct Algorithm (BFS with a Flag)

1. Perform BFS using a queue.
2. Maintain a boolean flag `seenNull`:
   - `false` initially
3. For each node dequeued:
   - If the node is `null`, set `seenNull = true`
   - If the node is **not null**:
     - If `seenNull` is already `true`, return `false`
     - Otherwise, enqueue **both left and right children (even if they are null)**
4. If BFS finishes without violation, return `true`.

---

## ❌ What Is Wrong in This Implementation

### 1. You never enqueue `null` nodes

```java
if (cur.left != null) {
    q.offer(cur.left);
}
if (cur.right != null) {
    q.offer(cur.right);
}
