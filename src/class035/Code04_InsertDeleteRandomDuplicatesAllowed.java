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


To support insert, remove, and getRandom in O(1) time with duplicates allowed:

Use an ArrayList to store all values (duplicates included)

Use a HashMap<value, Set<index>> to track all positions of each value in the array

Insert

Append the value to the array

Record its index in the corresponding set

Return true if the value did not exist before

Remove

Remove any index of the value from its index set

If the value is not the last element:

Move the last array element to the removed position

Update index sets for both values

Remove the last element from the array

Delete the map entry if the index set becomes empty

GetRandom

Pick a random index from the array and return its value

All operations run in O(1) time.

Common Pitfalls

Storing values instead of indices in the set
→ The set must track array indices, not values.

Forgetting to update index sets after swapping
→ Any index change in the array must be reflected in the map.

Not handling val == lastValue correctly
→ Removing the last element requires no swap, only cleanup.

Leaving empty sets in the map
→ Always remove the key when its index set becomes empty.

Incorrect use of Math.random()
→ Cast after multiplication: (int)(Math.random() * size).

Key Takeaway

The array may change order,
but the map must always reflect the exact indices in the array.