package class038;

import java.util.HashSet;

// 字符串的全部子序列
// 测试链接 : https://leetcode.com/problems/subsets/
class Solution {
    List<List<Integer>> res;
    List<Integer> path;
    
    public List<List<Integer>> subsets(int[] nums) {
        res = new ArrayList<>();
        path = new ArrayList<>();
        dfs(nums, 0);
        return res;
    }
    
    private void dfs(int[] nums, int start) {
        if (start == nums.length) {
            res.add(new ArrayList<>(path));  // ✅ 到达叶子节点，收集结果
            return;
        }
        
        // 选择1: 要当前元素
        path.add(nums[start]);
        dfs(nums, start + 1);
        path.remove(path.size() - 1);  // ✅ 回溯
        
        // 选择2: 不要当前元素
        dfs(nums, start + 1);
    }
}

## 🔍 执行过程演示
```
输入: nums = [1, 2, 3]

递归树:
                    []
                  /    \
                [1]     []
              /   \    /  \
           [1,2] [1] [2]  []
           / \   / \ / \  / \
        [1,2,3][1,2][1,3][1][2,3][2][3][]

每个叶子节点都是一个子集
```

### 具体执行步骤
```
start=0, path=[]
  ├─ add(1), path=[1]
  │   └─ start=1, path=[1]
  │       ├─ add(2), path=[1,2]
  │       │   └─ start=2, path=[1,2]
  │       │       ├─ add(3), path=[1,2,3]
  │       │       │   └─ start=3 → 收集 [1,2,3] ✅
  │       │       │   └─ remove(3), path=[1,2]
  │       │       └─ start=3 → 收集 [1,2] ✅
  │       │   └─ remove(2), path=[1]
  │       └─ start=2, path=[1]
  │           ├─ add(3), path=[1,3]
  │           │   └─ start=3 → 收集 [1,3] ✅
  │           │   └─ remove(3), path=[1]
  │           └─ start=3 → 收集 [1] ✅
  │   └─ remove(1), path=[]
  └─ start=1, path=[]
      ├─ add(2), path=[2]
      │   └─ start=2, path=[2]
      │       ├─ add(3), path=[2,3]
      │       │   └─ start=3 → 收集 [2,3] ✅
      │       │   └─ remove(3), path=[2]
      │       └─ start=3 → 收集 [2] ✅
      │   └─ remove(2), path=[]
      └─ start=2, path=[]
          ├─ add(3), path=[3]
          │   └─ start=3 → 收集 [3] ✅
          │   └─ remove(3), path=[]
          └─ start=3 → 收集 [] ✅

结果: [[], [1], [1,2], [1,2,3], [1,3], [2], [2,3], [3]]


另一种写法（在每个节点收集）
javaprivate void dfs(int[] nums, int start) {
    res.add(new ArrayList<>(path));  // 每个节点都是一个子集
    
    for (int i = start; i < nums.length; i++) {
        path.add(nums[i]);
        dfs(nums, i + 1);
        path.remove(path.size() - 1);
    }
}

优化2: 如果要按字典序输出（这题不要求）
java// 先"不选"，后"选"，结果会按字典序
private void dfs(int[] nums, int start) {
    if (start == nums.length) {
        res.add(new ArrayList<>(path));
        return;
    }
    
    // 不要（先执行）
    dfs(nums, start + 1);
    
    // 要（后执行）
    path.add(nums[start]);
    dfs(nums, start + 1);
    path.remove(path.size() - 1);
}

// 输出: [[], [3], [2], [2,3], [1], [1,3], [1,2], [1,2,3]]



90. Subsets II. https://leetcode.com/problems/subsets-ii/description/
Given an integer array nums that may contain duplicates, return all possible subsets (the power set).

The solution set must not contain duplicate subsets. Return the solution in any order.

class Solution {
    List<List<Integer>> res;
    List<Integer> path;
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        res = new ArrayList<>();
        path = new ArrayList<>();
        dfs(nums, 0);
        return res;
    }
    private void dfs(int[] nums, int start) {
        if (start == nums.length) {
            res.add(new ArrayList<>(path));
            return;
        }

        path.add(nums[start]);
        dfs(nums, start + 1);
        path.remove(path.size() - 1);

        int next = start + 1;
        while (next < nums.length && nums[next] == nums[start]) {
            next++;
        }
        dfs(nums, next);
    }
}

## 🤔 为什么"选"的时候不需要跳过？

### 关键理解：位置的区分作用
```
[1, 2₁, 2₂]

当我们"选 2₁"时:
  - 我们明确选的是位置1的元素
  - 接下来考虑位置2的元素（2₂）
  - 虽然值相同，但位置不同，可以继续处理

当我们"不选 2₁"时:
  - 如果不跳过，会在位置2选 2₂
  - 这会导致 [1, 2₁] 和 [1, 2₂] 重复
  - 所以必须跳过所有相同的！
```

---

## 📊 用集合理解

### 选择的本质
```
nums = [1, 2₁, 2₂]

所有可能的选择方案:
1. 不选任何 2 → []
2. 选 2₁ 不选 2₂ → [2₁]
3. 不选 2₁ 选 2₂ → [2₂]  ← 这个和方案2重复！
4. 选 2₁ 选 2₂ → [2₁,2₂]

去重规则：
- 如果不选 2₁，就不能选 2₂
- 这样方案3就被消除了
```

---

## 💡 另一种理解方式：统一处理相同元素
```
把连续相同的元素看作一个整体:

[1, 2, 2, 3]
    └──┘
   当作一个"单元"

对于这个"单元"，有3种选择:
1. 不选任何一个
2. 选1个（第一个）
3. 选2个（都选）

代码中的逻辑:
- "选"分支：处理选1个、选2个...
- "不选"分支：处理不选任何一个（跳过整个单元）
```

---

## 🎯 记忆技巧

### 口诀
```
选了递归正常走，
不选跳过所有相同。
```

### 为什么？
```
选: 我承诺用这个元素，后面的相同元素可以再选
不选: 我放弃这个元素，后面相同的也一并放弃

private void dfs(int[] nums, int start) {
    res.add(new ArrayList<>(path));
    
    for (int i = start; i < nums.length; i++) {
        // 同一层去重：跳过与前一个相同的元素
        if (i > start && nums[i] == nums[i-1]) {
            continue;  // ✅ 去重
        }
        
        path.add(nums[i]);
        dfs(nums, i + 1);
        path.remove(path.size() - 1);
    }
}

//千万记得要排序！不排序就无法正确去重！
```