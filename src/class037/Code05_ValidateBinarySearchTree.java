package class037;

// 验证搜索二叉树
// 测试链接 : https://leetcode.cn/problems/validate-binary-search-tree/
public class Code05_ValidateBinarySearchTree {
	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	# Validate BST - 两种方法对比

## 方法1: Top-Down (自顶向下) - 推荐 ⭐⭐⭐⭐⭐

### 思路
从根向下传递**约束范围** [min, max]，检查每个节点是否在范围内

## Approach: Top-Down with Range Validation

### Key Insight
- Pass valid range `[min, max]` down from parent to child
- Each node must satisfy: `min < node.val < max`
- Left subtree: tighten upper bound to `root.val`
- Right subtree: tighten lower bound to `root.val`

### Algorithm
1. Start with widest range: `[MIN_VALUE, MAX_VALUE]`
2. For each node, check if value is within range
3. Recurse left with `[min, root.val]`
4. Recurse right with `[root.val, max]`


### 实现
```java

    public boolean isValidBST(TreeNode root) {
        return dfs(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    
    private boolean dfs(TreeNode root, long min, long max) {
        if (root == null) return true;
        
        if (root.val <= min || root.val >= max) return false;
        
        return dfs(root.left, min, root.val)       // 左: 缩小上界
            && dfs(root.right, root.val, max);     // 右: 提高下界
    }
```
}
### 参数含义
- `min`: 当前节点必须 **> min** (下界约束)
- `max`: 当前节点必须 **< max** (上界约束)
- 初始: `[MIN_VALUE, MAX_VALUE]` - 根节点无约束

### 信息流向
```
        5 [MIN,MAX]
       / \
   [MIN,5] [5,MAX]
     2        8

信息: 根 → 叶 (传递约束)
```

---

## 方法2: Bottom-Up (自底向上)

### 思路
从叶向上收集**实际范围** [min, max]，检查 BST 性质

### 实现
```java
class Solution {
    long min, max;
    
    public boolean isValidBST(TreeNode head) {
        if (head == null) {
            min = Long.MAX_VALUE;  // 空树最小值 = 正无穷
            max = Long.MIN_VALUE;  // 空树最大值 = 负无穷
            return true;
        }
        
        boolean lok = isValidBST(head.left);
        long lmin = min, lmax = max;  // 保存左子树范围
        
        boolean rok = isValidBST(head.right);
        long rmin = min, rmax = max;  // 保存右子树范围
        
        // 更新当前子树范围
        min = Math.min(Math.min(lmin, rmin), head.val);
        max = Math.max(Math.max(lmax, rmax), head.val);
        
        // 检查 BST: 左子树最大值 < 根 < 右子树最小值
        return lok && rok && lmax < head.val && head.val < rmin;
    }
}
```

### 参数含义
- `min`: 当前子树的**实际最小值**
- `max`: 当前子树的**实际最大值**
- null: `[MAX_VALUE, MIN_VALUE]` - 单位元，不影响计算

### 信息流向
```
        5 [2,8]
       / \
   [2,2]   [8,8]
     2        8

信息: 叶 → 根 (返回实际值)
```

---

## 核心区别

| 对比项 | Top-Down (推荐) | Bottom-Up |
|--------|-----------------|-----------|
| **信息流** | 根 → 叶 | 叶 → 根 |
| **传递内容** | 约束范围 | 实际范围 |
| **MIN/MAX 含义** | 初始最宽松约束 | 空集合单位元 |
| **null 处理** | `return true` | `min=MAX, max=MIN` |
| **代码复杂度** | 简单 ⭐⭐⭐⭐⭐ | 复杂 ⭐⭐ |
| **理解难度** | 易 ⭐⭐⭐⭐⭐ | 难 ⭐⭐ |
| **全局变量** | 无 ✅ | 有 ❌ |
| **线程安全** | 是 ✅ | 否 ❌ |

---

## 为什么 null 返回不同？

### Top-Down: null 返回 true
```java
if (root == null) return true;  // 空树满足任何约束
```

### Bottom-Up: null 返回 MAX/MIN
```java
if (head == null) {
    min = Long.MAX_VALUE;  // 空树没有最小值 → 正无穷
    max = Long.MIN_VALUE;  // 空树没有最大值 → 负无穷
    return true;
}
```

**原因**: 作为单位元，不影响 `Math.min/max` 计算
```java
Math.min(任何值, MAX_VALUE) = 任何值  // 不影响结果
Math.max(任何值, MIN_VALUE) = 任何值  // 不影响结果
```

---

## 记忆口诀

### Top-Down
```
"约束从宽到严"
根: [MIN, MAX] - 最宽松
左: [MIN, root] - 收紧上界
右: [root, MAX] - 收紧下界
```

### Bottom-Up
```
"空集合是单位元"
null: [MAX, MIN] - 正无穷/负无穷
作用: 不影响 min/max 计算
```

---

## 推荐使用

**✅ 面试/工作**: Top-Down
- 代码简洁
- 逻辑清晰
- 无全局状态
- 业界标准

**Bottom-Up 适用场景**:
- 需要同时返回多个信息 (min, max, height 等)
- 特定复杂树问题

---

**Time**: O(N)  
**Space**: O(H)
