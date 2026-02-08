package class036;

// 二叉树先序序列化和反序列化
// 测试链接 : https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/
public class Code05_PreorderSerializeAndDeserialize {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		public TreeNode(int v) {
			val = v;
		}
	}

    // 二叉树可以通过先序、后序或者按层遍历的方式序列化和反序列化
    // 但是，二叉树无法通过中序遍历的方式实现序列化和反序列化
    // 因为不同的两棵树，可能得到同样的中序序列，即便补了空位置也可能一样。
    // 比如如下两棵树
    //         __2
    //        /
    //       1
    //       和
    //       1__
    //          \
    //           2
    // 补足空位置的中序遍历结果都是{ null, 1, null, 2, null}
	// 提交这个类
	public class Codec {

		public String serialize(TreeNode root) {
			StringBuilder builder = new StringBuilder();
			f(root, builder);
			return builder.toString();
		}

		void f(TreeNode root, StringBuilder builder) {
			if (root == null) {
				builder.append("#,");
			} else {
				builder.append(root.val + ",");
				f(root.left, builder);
				f(root.right, builder);
			}
		}

		public TreeNode deserialize(String data) {
			String[] vals = data.split(",");
			cnt = 0;
			return g(vals);
		}

		// 当前数组消费到哪了
		public static int cnt;

		TreeNode g(String[] vals) {
			String cur = vals[cnt++];
			if (cur.equals("#")) {
				return null;
			} else {
				TreeNode head = new TreeNode(Integer.valueOf(cur));
				head.left = g(vals);
				head.right = g(vals);
				return head;
			}
		}

	}

}


## Approach: Preorder Traversal with Delimiter

### Key Idea
- **Serialize**: Use preorder traversal (root → left → right), mark null nodes as `#`
- **Deserialize**: Reconstruct tree using the same preorder sequence
- Use `,` as delimiter to separate values

### Why Preorder?
- Can reconstruct tree **without ambiguity**
- Process root first, then recursively build left and right subtrees
- Natural fit for recursive deserialization

java# Serialize and Deserialize Binary Tree - 速记卡

## 7大易错点

| # | 错误 | 正确 |
|---|------|------|
| 1 | `append("val")` | `append(root.val)` |
| 2 | `== "#"` | `.equals("#")` |
| 3 | `new TreeNode(str)` | `Integer.parseInt(str)` |
| 4 | `nodes[idx++]...nodes[idx++]` | `val = nodes[idx++]` 只用一次 |
| 5 | 递归内 `split()` | 外层 split 一次 |
| 6 | 不重置 idx | 每次 `idx = 0` |
| 7 | `int idx` 参数传递 | 类变量 / 数组 / Queue |

---

## 末尾逗号说明
```java
"1,2,#,#,".split(",")  // → [1, 2, #, #]

// 末尾空串被舍去，但没关系！
// 因为递归正好结束，不会再访问 nodes[idx]
```

**结论**：不需要处理末尾空串，递归天然正确！