package class037;

import java.util.ArrayList;
import java.util.List;

// 收集累加和等于aim的所有路径
// 测试链接 : https://leetcode.cn/problems/path-sum-ii/
public class Code03_PathSumII {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	public static List<List<Integer>> pathSum(TreeNode root, int aim) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root != null) {
			List<Integer> path = new ArrayList<>();
			f(root, aim, 0, path, ans);
		}
		return ans;
	}

	public static void f(TreeNode cur, int aim, int sum, List<Integer> path, List<List<Integer>> ans) {
		if (cur.left == null && cur.right == null) {
			// 叶节点
			if (cur.val + sum == aim) {
				path.add(cur.val);
				copy(path, ans);
				path.remove(path.size() - 1);
			}
		} else {
			// 不是叶节点
			path.add(cur.val);
			if (cur.left != null) {
				f(cur.left, aim, sum + cur.val, path, ans);
			}
			if (cur.right != null) {
				f(cur.right, aim, sum + cur.val, path, ans);
			}
			path.remove(path.size() - 1);
		}
	}

	public static void copy(List<Integer> path, List<List<Integer>> ans) {
		List<Integer> copy = new ArrayList<>();
		for (Integer num : path) {
			copy.add(num);
		}
		ans.add(copy);
	}

}

## 记忆口诀

### "加减查递回" (Add-Subtract-Check-Recurse-Backtrack)

1. **加** - `path.add(root.val)`
2. **减** - `target -= root.val`
3. **查** - `if (is_leaf && target == 0)`
4. **递** - `dfs(left/right, target)`
5. **回** - `path.remove(last)`

### 关键：第2步"减"必须在第3步"查"之前！

---

## 常见错误对比

### ❌ 错误1: 不更新就检查
```java
path.add(root.val);
if (is_leaf && target == 0) {  // ❌ target 还是原值！
```

### ✅ 正确: 先更新再检查
```java
path.add(root.val);
target -= root.val;            // 立即更新
if (is_leaf && target == 0) {  // ✅ 现在检查 0
```

---

### ❌ 错误2: 检查 target == root.val
```java
path.add(root.val);
if (is_leaf && target == root.val) {  // ❌ 容易忘记这个逻辑
```

### ✅ 正确: 先减再查0 (更简单)
```java
path.add(root.val);
target -= root.val;
if (is_leaf && target == 0) {  // ✅ 统一查 0，不会错
```

---

### ❌ 错误3: Early return
```java
if (is_leaf && target == 0) {
    res.add(new ArrayList<>(path));
    return;  // ❌ 跳过回溯！
}
path.remove(...);  // 永远不会执行
```

### ✅ 正确: 不要 return
```java
if (is_leaf && target == 0) {
    res.add(new ArrayList<>(path));
    // 不 return，继续执行
}
dfs(left, target);
dfs(right, target);
path.remove(...);  // ✅ 总是执行
```

---

### ❌ 错误4: 递归时重复减
```java
target -= root.val;
dfs(root.left, target - root.val);  // ❌ 减了两次！
```

### ✅ 正确: 只减一次
```java
target -= root.val;
dfs(root.left, target);  // ✅ 已经减过了
```

---

```java
class Solution {
    List<List<Integer>> res = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        dfs(root, targetSum);
        return res;
    }
    
    private void dfs(TreeNode root, int target) {
        if (root == null) return;
        
        path.add(root.val);       // 1. 加
        target -= root.val;       // 2. 减
        
        if (root.left == null && root.right == null && target == 0) {  // 3. 查
            res.add(new ArrayList<>(path));  // 记得 copy!
        }
        
        dfs(root.left, target);   // 4. 递
        dfs(root.right, target);
        
        path.remove(path.size() - 1);  // 5. 回
    }
}
```

---

## 5 个检查点 (写完自查)

写完代码后，检查这 5 点：

### ✅ Checklist

1. [ ] **加入节点在最前面**: `path.add(root.val)` 在递归前
2. [ ] **立即更新 target**: `target -= root.val` 紧跟在 add 后面
3. [ ] **检查 target == 0**: 不是检查 `target == root.val`
4. [ ] **没有 early return**: if 语句内没有 `return`
5. [ ] **回溯在最后**: `path.remove(...)` 是函数最后一行

---

## 记忆技巧

### 想象成"进房间-出房间"模型
```java
// 进房间
path.add(root.val);      // 进门：写名字在访客簿
target -= root.val;      // 交钱：减去房费

// 在房间里
if (is_leaf && target == 0) {  // 检查：钱够不够
    res.add(...);               // 记录：拍照留念
}

// 访问子房间
dfs(left);
dfs(right);

// 出房间
path.remove(...);        // 出门：擦掉访客簿上的名字
```

每次**进门必出门**，不能中途跑路 (early return)!