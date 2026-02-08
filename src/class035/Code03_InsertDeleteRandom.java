package class035;

import java.util.ArrayList;
import java.util.HashMap;

// 插入、删除和获取随机元素O(1)时间的结构
public class Code03_InsertDeleteRandom {

	// 测试链接 : https://leetcode.cn/problems/insert-delete-getrandom-o1/
	class RandomizedSet {

		public HashMap<Integer, Integer> map;

		public ArrayList<Integer> arr;

		public RandomizedSet() {
			map = new HashMap<>();
			arr = new ArrayList<>();
		}

		public boolean insert(int val) {
			if (map.containsKey(val)) {
				return false;
			}
			map.put(val, arr.size());
			arr.add(val);
			return true;
		}

		public boolean remove(int val) {
			if (!map.containsKey(val)) {
				return false;
			}
			int valIndex = map.get(val);
			int endValue = arr.get(arr.size() - 1);
			map.put(endValue, valIndex);
			arr.set(valIndex, endValue);
			map.remove(val);
			arr.remove(arr.size() - 1);
			return true;
		}

		public int getRandom() {
			return arr.get((int) (Math.random() * arr.size()));
		}

	}

}

## RandomizedSet — Solution Idea

To support `insert`, `remove`, and `getRandom` in **O(1)** time,  
we combine two data structures:

- An `ArrayList` to store values
- A `HashMap` to map each value to its index in the array

### Key Design
- `ArrayList` allows O(1) random access
- `HashMap` allows O(1) lookup of an element’s index
- The array does **not** need to preserve order

### Operations
- **Insert**
  - Append the value to the end of the array
  - Record its index in the map

- **Remove**
  - Get the index of the value from the map
  - Swap it with the last element in the array
  - Update the swapped element’s index in the map
  - Remove the last element

- **GetRandom**
  - Pick a random index from `[0, size - 1]`
  - Return the element at that index

All operations run in **O(1)** time.

---

## Common Pitfalls

### 1. Forgetting to update the index after swap
When removing an element, the last element is moved to the removed position.

If its index in the map is not updated,  
future operations will access the wrong position.

---

### 2. Incorrect use of `Math.random()`
```java
(int)Math.random() * size   // WRONG
(int)(Math.random() * size) // CORRECT
