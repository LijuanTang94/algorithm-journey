package class035;

import java.util.ArrayList;
import java.util.HashMap;

// 最大频率栈
public class Code06_MaximumFrequencyStack {

	// 测试链接 : https://leetcode.cn/problems/maximum-frequency-stack/
	
	class FreqStack {
		private Map<Integer, Integer> valueCounts;  // value -> frequency
		private Map<Integer, Stack<Integer>> counts; // frequency -> stack
		private int maxFreq;

		public FreqStack() {
			valueCounts = new HashMap<>();
			counts = new HashMap<>();
			maxFreq = 0;
		}
		
		public void push(int val) {
			int freq = valueCounts.getOrDefault(val, 0) + 1;
			valueCounts.put(val, freq);
			maxFreq = Math.max(maxFreq, freq);
			counts.computeIfAbsent(freq, k -> new Stack<>()).push(val);
		}
		
		public int pop() {
			int val = counts.get(maxFreq).pop();
			valueCounts.put(val, valueCounts.get(val) - 1);
			if (counts.get(maxFreq).isEmpty()) {
				maxFreq--;
			}
			return val;
		}
	}
}

# Maximum Frequency Stack (LeetCode 895)

## Core Idea

Implement a stack that always pops the element with the **highest frequency**.
If tied, pop the **most recently pushed** one.

Use two hash maps:

1. **valueCounts**: value → frequency
2. **counts**: frequency → stack of values

Plus `maxFreq` to track the current maximum frequency.

---

## Operations

### push(val)
1. Increment frequency of `val`
2. Update `maxFreq` if needed
3. Push `val` into the stack for its new frequency

**Time: O(1)**

### pop()
1. Pop from the stack at `maxFreq`
2. Decrement its frequency in `valueCounts`
3. If `maxFreq` stack is empty, decrease `maxFreq`

**Time: O(1)**


## Common Pitfalls

### ❌ Wrong key in `getOrDefault`
```java
valueCounts.getOrDefault(0, 0)      // WRONG
valueCounts.getOrDefault(val, 0)    // CORRECT
```

### ❌ Misusing `add()` return value
```java
// add() returns boolean, not the list!
counts.put(freq, counts.getOrDefault(freq, new ArrayList<>()).add(val)); // WRONG
```

### ❌ Forgetting to decrement frequency in `pop()`
```java
public int pop() {
    int val = counts.get(maxFreq).pop();
    // Missing: valueCounts.put(val, valueCounts.get(val) - 1);
    return val;
}
```

### ❌ Not updating `maxFreq` when stack empties
```java
public int pop() {
    int val = counts.get(maxFreq).pop();
    valueCounts.put(val, valueCounts.get(val) - 1);
    // Missing check for empty stack
    return val;
}
```

---

## Key Invariants

- `valueCounts[val]` = current frequency of `val`
- `counts[f]` = all values currently at frequency `f` (ordered by recency)
- `maxFreq` = highest existing frequency
- Top of `counts[maxFreq]` = next element to pop