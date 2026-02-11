package class049;

// 加油站
// 在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
// 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升
// 你从其中的一个加油站出发，开始时油箱为空。
// 给定两个整数数组 gas 和 cost ，如果你可以按顺序绕环路行驶一周
// 则返回出发时加油站的编号，否则返回 -1
// 如果存在解，则 保证 它是 唯一 的。
// 测试链接 : https://leetcode.cn/problems/gas-station/
public class Code04_GasStation {

	public static int canCompleteCircuit(int[] gas, int[] cost) {
		int n = gas.length;
		// 本来下标是0..n-1，但是扩充到0..2*n-1，i位置的余量信息在(r%n)位置
		// 窗口范围是[l, r)，左闭右开，也就是说窗口是[l..r-1]，r是到不了的位置
		for (int l = 0, r = 0, sum; l < n; l = r + 1, r = l) {
			sum = 0;
			while (sum + gas[r % n] - cost[r % n] >= 0) {
				// r位置即将右扩，窗口会变大
				if (r - l + 1 == n) { // 此时检查是否已经转了一圈
					return l;
				}
				// r位置进入窗口，累加和加上r位置的余量
				sum += gas[r % n] - cost[r % n];
				// r右扩，窗口变大了
				r++;
			}
		}
		return -1;
	}

}


# Gas Station — Idea (Brute Force Circular Simulation)

## Core Idea

Try every station as a starting point.

For each starting index `l`:
- Simulate traveling clockwise around the circular route.
- Maintain a running fuel balance:
  
  balance += gas[i] - cost[i]

- If the balance ever becomes negative, this starting point fails.
- If we successfully travel `n` stations, return `l`.

---

## Why `% n` is Needed

The gas stations form a circular route.

When the pointer `r` exceeds `n - 1`, we wrap around using:

    r % n

This simulates circular traversal on a linear array.

---

## Algorithm Flow

1. Enumerate each possible starting index `l`.
2. Expand pointer `r` forward.
3. Use `r % n` to access stations circularly.
4. Stop when:
   - Fuel becomes negative → move to next start
   - Or we travel `n` stations → return `l`
5. If no valid start exists → return -1

---

## Time Complexity

Worst case: O(n²)

Each starting position may simulate up to n stations.


class Solution {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int n = gas.length;
        int curSum = 0;
        int totalSum = 0;
        int start = 0;
        for(int i = 0; i < n; i++) {
            if (curSum < 0) {
                curSum = gas[i] - cost[i];
                start = i;
            }else {
                curSum += gas[i] - cost[i];
            }
            totalSum += gas[i] - cost[i];
        }
        return totalSum < 0 ? -1 : start;
    }
}


# Gas Station — Greedy Solution (O(n))

## Core Idea

If the total amount of gas is less than the total cost,
it is impossible to complete the circuit.

Otherwise, there must be exactly one valid starting station.

---

## Greedy Insight

While scanning the array:

- Maintain a running balance `curSum`
- Also maintain a global balance `totalSum`

If `curSum` becomes negative at index `i`:

- Any station between the previous `start` and `i`
  cannot be a valid starting point
- Reset `start = i + 1`
- Reset `curSum = 0`

Continue scanning.

---

## Why This Works

If starting from station `start` fails at `i`,
then any station between `start` and `i`
will have even less fuel when reaching `i`.

Therefore, all of them can be skipped.

This guarantees O(n) time complexity.

---

## Final Decision

- If `totalSum < 0` → return -1
- Otherwise → return `start`
