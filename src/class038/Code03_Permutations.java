package class038;

import java.util.ArrayList;
import java.util.List;

// 没有重复项数字的全排列
// 测试链接 : https://leetcode.cn/problems/permutations/
全排列两种写法完整对比
核心区别
对比项方法1: 标记法 (Boolean Array)方法2: 交换法 (Swap)思维模型填空：每次选一个未使用的数填入排队：每次把后面的数拉到当前位置状态维护boolean[] used 标记已使用直接修改原数组path额外维护 List<Integer> path数组本身就是 path空间复杂度O(n) + O(n)O(n)代码直观性⭐⭐⭐⭐⭐ 非常直观⭐⭐⭐ 需要理解交换易错程度⭐ 不易出错⭐⭐⭐⭐ 容易写错递归参数推荐度⭐⭐⭐⭐⭐ 面试首选⭐⭐⭐ 了解即可

方法1: 标记法（推荐）
核心思路
想象填空题：[_] [_] [_]

每个位置可以填任何未使用的数字：
- 第1个位置: 选1 → 标记1已用 → 递归填第2个
- 第2个位置: 选2或3 (1已用) → 标记 → 递归
- 第3个位置: 选剩下的 → 标记 → 收集结果
- 回溯: 撤销标记

关键: boolean[] 记录哪些数字已经用过
完整代码
public class Code03_Permutations {
    List<List<Integer>> res = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    
    public List<List<Integer>> permute(int[] nums) {
        boolean[] used = new boolean[nums.length];
        dfs(nums, used);
        return res;
    }
    
    private void dfs(int[] nums, boolean[] used) {
        // 终止条件: path 填满了
        if (path.size() == nums.length) {
            res.add(new ArrayList<>(path));
            return;
        }
        
        // 尝试所有未使用的数字
        for (int i = 0; i < nums.length; i++) {
            if (!used[i]) {
                // 1. 选择
                used[i] = true;
                path.add(nums[i]);
                
                // 2. 递归
                dfs(nums, used);
                
                // 3. 撤销选择（回溯）
                path.remove(path.size() - 1);
                used[i] = false;
            }
        }
    }
}
```

### 执行过程（nums = [1,2,3]）
```
dfs(used=[F,F,F], path=[])
├─ 选1: used=[T,F,F], path=[1]
│  ├─ 选2: used=[T,T,F], path=[1,2]
│  │  └─ 选3: path=[1,2,3] → 收集 ✅
│  └─ 选3: used=[T,F,T], path=[1,3]
│     └─ 选2: path=[1,3,2] → 收集 ✅
├─ 选2: used=[F,T,F], path=[2]
│  ├─ 选1: path=[2,1,3] → 收集 ✅
│  └─ 选3: path=[2,3,1] → 收集 ✅
└─ 选3: used=[F,F,T], path=[3]
   ├─ 选1: path=[3,1,2] → 收集 ✅
   └─ 选2: path=[3,2,1] → 收集 ✅
```

### 优点
- ✅ **逻辑清晰**: "选一个 → 标记 → 递归 → 撤销" 思路直观
- ✅ **不易出错**: 每个操作都有明确对应的撤销
- ✅ **易于调试**: 可以清楚看到哪些数字被使用
- ✅ **易于扩展**: 添加剪枝条件很方便

### 缺点
- ❌ **空间开销**: 需要额外的 `boolean[]` 和 `path`

---

## 方法2: 交换法

### 核心思路
```
想象排队：[1] [2] [3]
           ↑
         start

每次把后面的人拉到 start 位置:
- start=0: 把位置 0,1,2 分别拉到位置0 → 递归 start=1
- start=1: 把位置 1,2 分别拉到位置1 → 递归 start=2
- start=2: 把位置 2 拉到位置2 → 收集结果

关键: 
1. 交换后数组本身就是当前排列
2. 递归参数是 start + 1 (不是 i + 1!)
完整代码
javaclass Solution {
    List<List<Integer>> res = new ArrayList<>();
    
    public List<List<Integer>> permute(int[] nums) {
        dfs(nums, 0);
        return res;
    }
    
    private void dfs(int[] nums, int start) {
        // 终止条件: 所有位置都确定了
        if (start == nums.length) {
            List<Integer> path = new ArrayList<>();
            for (int num : nums) {
                path.add(num);
            }
            res.add(path);
            return;
        }
        
        // 尝试把每个后续元素交换到 start 位置
        for (int i = start; i < nums.length; i++) {
            // 1. 交换（把 i 位置的元素拉到 start）
            swap(nums, i, start);
            
            // 2. 递归处理下一个位置
            dfs(nums, start + 1);  // ✅ 关键: start + 1 (不是 i + 1)
            
            // 3. 恢复（把元素送回原位）
            swap(nums, i, start);
        }
    }
    
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
```

### 执行过程（nums = [1,2,3]）
```
dfs(nums=[1,2,3], start=0)
├─ i=0: swap(0,0)=[1,2,3]
│  └─ dfs(nums=[1,2,3], start=1)
│     ├─ i=1: swap(1,1)=[1,2,3]
│     │  └─ dfs(start=2)
│     │     └─ i=2: swap(2,2)=[1,2,3] → 收集 ✅
│     └─ i=2: swap(2,1)=[1,3,2]
│        └─ dfs(start=2) → [1,3,2] → 收集 ✅
│
├─ i=1: swap(1,0)=[2,1,3]
│  └─ dfs(nums=[2,1,3], start=1)
│     ├─ i=1: [2,1,3] → 收集 ✅
│     └─ i=2: [2,3,1] → 收集 ✅
│
└─ i=2: swap(2,0)=[3,2,1]
   └─ dfs(nums=[3,2,1], start=1)
      ├─ i=1: [3,2,1] → 收集 ✅
      └─ i=2: [3,1,2] → 收集 ✅
优点

✅ 空间优化: 不需要额外的 boolean[] 和 path
✅ 代码简洁: 行数更少
✅ 原地操作: 直接修改数组

缺点

❌ 易错: 递归参数容易写成 i + 1 而不是 start + 1
❌ 不直观: 交换的逻辑需要理解
❌ 难调试: 数组在不断变化，状态难追踪


常见错误对比
❌ 方法1 常见错误
java// 错误1: 忘记回溯
path.add(nums[i]);
dfs(nums, used);
// ❌ 忘记 remove 和恢复 used

// 错误2: 复制 path 时忘记 new
res.add(path);  // ❌ 会随后续修改而变化

// 错误3: used 索引错误
if (!used[nums[i]]) {  // ❌ 应该是 used[i]
❌ 方法2 常见错误
java// 错误1: 递归参数错误 (最常见!)
dfs(nums, i + 1);  // ❌ 应该是 start + 1

// 错误2: swap 顺序前后不一致
swap(nums, i, start);
dfs(nums, start + 1);
swap(nums, start, i);  // ❌ 恢复时顺序应该一致

// 错误3: 忘记恢复交换
swap(nums, i, start);
dfs(nums, start + 1);
// ❌ 忘记再次 swap
```

---

## 复杂度分析

### 时间复杂度
```
两种方法都是: O(n! × n)

- n! 个排列
- 每个排列需要 O(n) 复制到结果
```

### 空间复杂度
```
方法1 (标记法):
  - boolean[] used: O(n)
  - List<Integer> path: O(n)
  - 递归栈: O(n)
  - 总空间: O(n)

方法2 (交换法):
  - 递归栈: O(n)
  - 临时 path: O(n) (收集结果时)
  - 总空间: O(n)

结论: 空间复杂度相同
```

---

## 适用场景

### 方法1 (标记法) 适用于
```
✅ 面试（首选）
✅ 需要按特定顺序选择
✅ 需要添加复杂剪枝条件
✅ 需要清晰的调试过程
✅ 团队协作代码（易读）
```

### 方法2 (交换法) 适用于
```
✅ 追求极致空间优化
✅ 熟练使用交换技巧
✅ 刷题追求简洁
✅ 性能关键场景（常数优化）