package class036;

import java.util.ArrayList;
import java.util.List;

// 二叉树的锯齿形层序遍历
// 测试链接 : https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/
public class Code02_ZigzagLevelOrderTraversal {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交以下的方法
	// 用每次处理一层的优化bfs就非常容易实现
	// 如果测试数据量变大了就修改这个值
	public static int MAXN = 2001;

	public static TreeNode[] queue = new TreeNode[MAXN];

	public static int l, r;

	public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root != null) {
			l = r = 0;
			queue[r++] = root;
			// false 代表从左往右
			// true 代表从右往左
			boolean reverse = false; 
			while (l < r) {
				int size = r - l;
				ArrayList<Integer> list = new ArrayList<Integer>();
				// reverse == false, 左 -> 右， l....r-1, 收集size个
				// reverse == true,  右 -> 左， r-1....l, 收集size个
				// 左 -> 右, i = i + 1
				// 右 -> 左, i = i - 1
				for (int i = reverse ? r - 1 : l, j = reverse ? -1 : 1, k = 0; k < size; i += j, k++) {
					TreeNode cur = queue[i];
					list.add(cur.val);
				}
				for (int i = 0; i < size; i++) {
					TreeNode cur = queue[l++];
					if (cur.left != null) {
						queue[r++] = cur.left;
					}
					if (cur.right != null) {
						queue[r++] = cur.right;
					}
				}
				ans.add(list);
				reverse = !reverse;
			}
		}
		return ans;
	}

	public List<List<Integer>> zigzagLevelOrder2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        boolean flag = true;
        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> level = new ArrayList<>();
            for(int i = 0; i < size; i++) {
                TreeNode cur = q.poll();
                level.add(cur.val);
                if (cur.left != null) {
                    q.offer(cur.left);
                }
                if (cur.right != null) {
                    q.offer(cur.right);
                }
            }
            if (!flag) {
                Collections.reverse(level);
            }
            res.add(new ArrayList<>(level));
            flag = !flag;
        }
        return res;
    }

}

# Binary Tree Zigzag Level Order Traversal (LeetCode 103)

## Core Idea

Perform a **level-order traversal (BFS)** of the binary tree using a queue.

For each level:
- Traverse nodes from left to right as usual.
- Alternate the order of values on every level to achieve the zigzag pattern.

A boolean flag is used to indicate the current direction:
- `true`  → left to right
- `false` → right to left

---

## Algorithm

1. If the root is `null`, return an empty result.
2. Use a queue to perform BFS.
3. For each level:
   - Record the number of nodes at the current level.
   - Collect all node values into a list.
   - Add children to the queue for the next level.
4. If the direction flag indicates right-to-left:
   - Reverse the list of values.
5. Add the level list to the result.
6. Toggle the direction flag and continue.

---

## Time and Space Complexity

- **Time Complexity:** `O(n)`
- **Space Complexity:** `O(n)`

Where `n` is the number of nodes in the tree.

---

## Common Pitfalls

1. **Using a non-existent `reverse()` method on List**
   ```java
   level.reverse();   // WRONG
   Collections.reverse(level); // CORRECT
   ```