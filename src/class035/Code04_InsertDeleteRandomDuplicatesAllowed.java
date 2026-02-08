package class035;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// 插入、删除和获取随机元素O(1)时间且允许有重复数字的结构
public class Code04_InsertDeleteRandomDuplicatesAllowed {

	// 测试链接 :
	// https://leetcode.cn/problems/insert-delete-getrandom-o1-duplicates-allowed/
	class RandomizedCollection {

		public HashMap<Integer, HashSet<Integer>> map;

		public ArrayList<Integer> arr;

		public RandomizedCollection() {
			map = new HashMap<>();
			arr = new ArrayList<>();
		}

		public boolean insert(int val) {
			arr.add(val);
			HashSet<Integer> set = map.getOrDefault(val, new HashSet<Integer>());
			set.add(arr.size() - 1);
			map.put(val, set);
			return set.size() == 1;
		}

		public boolean remove(int val) {
			if (!map.containsKey(val)) {
				return false;
			}
			HashSet<Integer> valSet = map.get(val);
			int valAnyIndex = valSet.iterator().next();
			int endValue = arr.get(arr.size() - 1);
			if (val == endValue) {
				valSet.remove(arr.size() - 1);
			} else {
				HashSet<Integer> endValueSet = map.get(endValue);
				endValueSet.add(valAnyIndex);
				arr.set(valAnyIndex, endValue);
				endValueSet.remove(arr.size() - 1);
				valSet.remove(valAnyIndex);
			}
			arr.remove(arr.size() - 1);
			if (valSet.isEmpty()) {
				map.remove(val);
			}
			return true;
		}

		public int getRandom() {
			return arr.get((int) (Math.random() * arr.size()));
		}
	}

}


# RandomizedCollection (O(1) with Duplicates Allowed)

## Solution Idea

To support `insert`, `remove`, and `getRandom` in **O(1)** time while allowing duplicates:

- Use an **ArrayList** to store all values (order does not matter)
- Use a **HashMap<Value, Set<Index>>** to track all positions of each value in the array

The key invariant is:
> **Every index stored in the map must correspond to a valid position in the array.**

---

### Insert
- Append the value to the end of the array
- Add its index to the corresponding set in the map
- Return `true` if the value did not exist before

---

### Remove
- Take any index of the value from its index set
- If the value is not the last element:
  - Move the last array element into the removed position
  - Update index sets for both values
- Remove the last element from the array
- Remove the key from the map if its index set becomes empty

---

### GetRandom
- Generate a random index in `[0, size - 1]`
- Return the element at that index

All operations run in **O(1)** time.

---

## Common Pitfalls

1. **Storing values instead of indices in the set**  
   → The set must store array indices, not values.

2. **Forgetting to update index sets after swapping**  
   → Any movement in the array must be reflected in the map.

3. **Incorrect handling of `val == lastValue`**  
   → Removing the last element does not require a swap.

4. **Leaving empty sets in the map**  
   → Always remove the key when its set becomes empty.

5. **Incorrect use of `Math.random()`**  
   → Cast after multiplication:  
   ```java
   (int)(Math.random() * size)
