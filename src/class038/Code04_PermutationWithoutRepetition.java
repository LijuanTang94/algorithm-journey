package class038;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// 有重复项数组的去重全排列
// 测试链接 : https://leetcode.cn/problems/permutations-ii/

全排列 II（含重复元素）两种写法完整对比
核心区别
对比项方法1: 标记法 + 剪枝方法2: 交换法 + 同层去重去重策略排序 + 跳过"前一个相同且未用"的元素排序 + HashSet 同层去重去重时机选择前判断每层循环开始判断去重逻辑nums[i] == nums[i-1] && !used[i-1]used.contains(nums[i])额外空间boolean[]HashSet (每层)理解难度⭐⭐⭐⭐⭐⭐⭐代码复杂度简单中等推荐度⭐⭐⭐⭐⭐ 强烈推荐⭐⭐⭐ 了解即可

方法1: 标记法 + 剪枝（推荐）
核心思路
去重关键: 相同的数字必须按顺序使用

例如 [1, 2', 2'']:
✅ 可以: 先用 2'，再用 2''
❌ 不可: 跳过 2'，直接用 2'' (会产生重复)

判断条件:
if (nums[i] == nums[i-1] && !used[i-1]) {
    跳过  // 前一个相同数字还没用，不能用当前的
}
完整代码
、、、
public class Code04_PermutationWithoutRepetition {
    List<List<Integer>> res = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    
    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);  // ✅ 关键: 必须先排序
        boolean[] used = new boolean[nums.length];
        dfs(nums, used);
        return res;
    }
    
    private void dfs(int[] nums, boolean[] used) {
        // 终止条件
        if (path.size() == nums.length) {
            res.add(new ArrayList<>(path));
            return;
        }
        
        // 尝试所有未使用的数字
        for (int i = 0; i < nums.length; i++) {
            // 剪枝1: 已经使用过
            if (used[i]) {
                continue;
            }
            
            // 剪枝2: 去重核心
            // 如果当前数字和前一个相同，且前一个还没用过，跳过
            if (i > 0 && nums[i] == nums[i-1] && !used[i-1]) {
                continue;
            }
            
            // 选择
            used[i] = true;
            path.add(nums[i]);
            
            // 递归
            dfs(nums, used);
            
            // 回溯
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }
}
```

### 执行过程（nums = [1, 2, 2]）
```
排序后: [1, 2', 2'']

dfs(path=[], used=[F,F,F])
├─ 选1: path=[1], used=[T,F,F]
│  ├─ 跳过2': i=1, used[1]=F, nums[1]==nums[0]? No → 可以选
│  │  └─ 选2': path=[1,2'], used=[T,T,F]
│  │     ├─ 跳过2'': i=2, used[2]=F, nums[2]==nums[1] && !used[1]? No (used[1]=T)
│  │     │  → 可以选
│  │     └─ 选2'': path=[1,2',2''] → 收集 ✅
│  └─ 检查2'': i=2, used[2]=F, nums[2]==nums[1] && !used[1]? Yes
│     → 跳过 (防止重复: 不能跳过2'直接选2'')
│
├─ 选2': path=[2'], used=[F,T,F]
│  ├─ 选1: path=[2',1,2''] → 收集 ✅
│  └─ 选2'': nums[2]==nums[1] && !used[1]? No → 可以选
│     └─ path=[2',2'',1] → 收集 ✅
│
└─ 检查2'': i=2, nums[2]==nums[1] && !used[1]? Yes
   → 跳过 ❌ (防止产生重复的 [2'',1,2'] 等)

结果: [[1,2',2''], [2',1,2''], [2',2'',1]]
没有重复！
```

### 为什么这样去重？
```
对于 [1, 2', 2'']:

情况1: 先选2' → [2', ...]
  ✅ 允许，因为是第一个2

情况2: 不选2'，直接选2'' → [2'', ...]
  ❌ 不允许！因为会产生重复
  
  为什么重复？
  - [2', 1, 2''] 已经被收集
  - [2'', 1, 2'] 和上面本质相同（两个2是一样的）
  
去重规则:
  如果 nums[i] == nums[i-1] && !used[i-1]
  → 说明跳过了前一个相同的数字
  → 会产生重复排列
  → 必须剪枝
```

### 优点
- ✅ **逻辑清晰**: 一个条件解决所有去重
- ✅ **代码简洁**: 只加了一行判断
- ✅ **易于理解**: "必须按顺序使用相同数字"
- ✅ **不易出错**: 条件明确

### 缺点
- ❌ **必须排序**: 不排序无法去重
- ❌ **理解成本**: 需要理解 `!used[i-1]` 的含义

---

## 方法2: 交换法 + 同层去重

### 核心思路
```
去重关键: 同一层不能交换相同的数字到 start 位置

例如在 start=0 时:
[2', 1, 2'']
 ↑      ↑
如果已经把2'交换到位置0，就不能再把2''交换到位置0

用 HashSet 记录每层已经交换过的值
完整代码
public class Code04_PermutationWithoutRepetition {
    List<List<Integer>> res = new ArrayList<>();
    
    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);  // ✅ 排序（可选，但建议保留）
        dfs(nums, 0);
        return res;
    }
    
    private void dfs(int[] nums, int start) {
        // 终止条件
        if (start == nums.length) {
            List<Integer> path = new ArrayList<>();
            for (int num : nums) {
                path.add(num);
            }
            res.add(path);
            return;
        }
        
        // ✅ 关键: 用 Set 记录当前层已经用过的值
        Set<Integer> used = new HashSet<>();
        
        // 尝试把每个后续元素交换到 start 位置
        for (int i = start; i < nums.length; i++) {
            // 去重: 如果这个值在当前层已经用过，跳过
            if (used.contains(nums[i])) {
                continue;
            }
            
            // 记录使用
            used.add(nums[i]);
            
            // 交换
            swap(nums, i, start);
            
            // 递归
            dfs(nums, start + 1);
            
            // 恢复
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

### 执行过程（nums = [1, 2, 2]）
```
排序后: [1, 2', 2'']

dfs(nums=[1,2',2''], start=0, used={})
├─ i=0: nums[0]=1, used={} → 没用过
│  └─ used={1}, swap(0,0)=[1,2',2'']
│     └─ dfs(start=1, used={})
│        ├─ i=1: nums[1]=2', used={} → 没用过
│        │  └─ used={2}, swap(1,1)=[1,2',2'']
│        │     └─ dfs(start=2, used={})
│        │        └─ i=2: nums[2]=2'', used={} → 没用过
│        │           └─ used={2}, [1,2',2''] → 收集 ✅
│        └─ i=2: nums[2]=2'', used={2} → 已用过
│           → 跳过 ❌ (防止重复: [1,2'',2'])
│
├─ i=1: nums[1]=2', used={1} → 没用过
│  └─ used={1,2}, swap(1,0)=[2',1,2'']
│     └─ dfs(start=1, used={})
│        ├─ i=1: nums[1]=1
│        │  └─ [2',1,2''] → 收集 ✅
│        └─ i=2: nums[2]=2''
│           └─ [2',2'',1] → 收集 ✅
│
└─ i=2: nums[2]=2'', used={1,2} → 2已用过
   → 跳过 ❌ (防止重复: [2'',1,2'])

结果: [[1,2',2''], [2',1,2''], [2',2'',1]]
没有重复！
```

### 为什么用 HashSet？
```
在每一层（每个 start 位置）:

start=0 时:
  可以把 1, 2', 2'' 交换到位置0
  但2'和2''值相同，只能选一个
  用 HashSet 记录: {1, 2}
  
  第一次遇到2: 加入 set，允许交换
  第二次遇到2: 发现在 set 中，跳过

start=1 时:
  新的一层，清空 used (每层独立)
  重新判断
优点

✅ 空间复用: 不需要全局 boolean[]
✅ 每层独立: HashSet 在每层重新创建
✅ 逻辑独立: 去重逻辑和交换逻辑分离

缺点

❌ 额外开销: 每层创建 HashSet
❌ 不够直观: 需要理解"同层"概念
❌ 易混淆: 容易忘记每层要重新创建 Set


方法1 易错点
❌ 错误1: 忘记排序
java// ❌ 不排序，去重失效
public List<List<Integer>> permuteUnique(int[] nums) {
    // Arrays.sort(nums);  // 忘记排序
    dfs(nums, new boolean[nums.length]);
}
❌ 错误2: 判断条件写反
java// ❌ 错误: used[i-1] 应该是 !used[i-1]
if (i > 0 && nums[i] == nums[i-1] && used[i-1]) {
    continue;
}
❌ 错误3: 没有检查 i > 0
java// ❌ i=0 时会越界
if (nums[i] == nums[i-1] && !used[i-1]) {
    continue;
}

方法2 易错点
❌ 错误1: Set 位置错误
java// ❌ Set 应该在每层重新创建
Set<Integer> used = new HashSet<>();  // 放在类成员变量

private void dfs(int[] nums, int start) {
    // 所有层共用一个 Set，去重失败
}
❌ 错误2: 忘记添加到 Set
javafor (int i = start; i < nums.length; i++) {
    if (used.contains(nums[i])) continue;
    // ❌ 忘记 used.add(nums[i])
    swap(nums, i, start);
    dfs(nums, start + 1);
    swap(nums, i, start);
}
❌ 错误3: 递归参数错误
javadfs(nums, i + 1);  // ❌ 应该是 start + 1
```

---

## 性能对比

### 时间复杂度
```
两种方法都是: O(n! × n)
- n! 个排列
- 每个排列复制 O(n)
```

### 空间复杂度
```
方法1: O(n)
  - boolean[]: O(n)
  - 递归栈: O(n)
  
方法2: O(n) 到 O(n²)
  - 递归栈: O(n)
  - 每层 HashSet: O(1) 到 O(n)
  - 最坏情况: O(n²)
```

**结论**: 方法1 空间更稳定