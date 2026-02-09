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


