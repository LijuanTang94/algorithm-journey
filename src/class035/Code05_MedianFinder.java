package class035;

import java.util.PriorityQueue;

// 快速获得数据流的中位数的结构
public class Code05_MedianFinder {

	// 测试链接 : https://leetcode.cn/problems/find-median-from-data-stream/
	class MedianFinder {

		private PriorityQueue<Integer> maxHeap;

		private PriorityQueue<Integer> minHeap;

		public MedianFinder() {
			maxHeap = new PriorityQueue<>((a, b) -> b - a);
			minHeap = new PriorityQueue<>((a, b) -> a - b);
		}

		public void addNum(int num) {
			if (maxHeap.isEmpty() || maxHeap.peek() >= num) {
				maxHeap.add(num);
			} else {
				minHeap.add(num);
			}
			balance();
		}

		public double findMedian() {
			if (maxHeap.size() == minHeap.size()) {
				return (double) (maxHeap.peek() + minHeap.peek()) / 2;
			} else {
				return maxHeap.size() > minHeap.size() ? maxHeap.peek() : minHeap.peek();
			}
		}

		private void balance() {
			if (Math.abs(maxHeap.size() - minHeap.size()) == 2) {
				if (maxHeap.size() > minHeap.size()) {
					minHeap.add(maxHeap.poll());
				} else {
					maxHeap.add(minHeap.poll());
				}
			}
		}

	}

}


# Find Median from Data Stream (LeetCode 295)

## Core Idea

To efficiently find the median from a stream of numbers, maintain **two heaps**:

- **Max Heap (`large`)**: stores the smaller half of the numbers
- **Min Heap (`small`)**: stores the larger half of the numbers

### Invariant
- The size difference between the two heaps is at most 1
- All elements in `large` ≤ all elements in `small`

---

## Operations

### addNum(num)
1. Insert `num` into:
   - `large` if it is smaller than or equal to `large.peek()`
   - otherwise into `small`
2. Rebalance the heaps if their size difference exceeds 1

Time Complexity: **O(log n)**

---

### findMedian()
- If both heaps have the same size:
  - Median is the average of `large.peek()` and `small.peek()`
- Otherwise:
  - Median is the top of the larger heap

Time Complexity: **O(1)**

---

## Common Pitfalls

1. **Using `poll()` instead of `peek()` in `findMedian()`**  
   - `poll()` removes elements and corrupts the data structure  
   - `findMedian()` must be a read-only operation

2. **Integer division when computing the average**
   ```java
   (a + b) / 2        // WRONG (integer division)
   (a + b) / 2.0      // CORRECT (floating-point division)
   ```
Comparator overflow

(a, b) -> (b - a)  // risky


Prefer:

Integer.compare(b, a)


Unstable balancing condition

Avoid size difference == 2

Prefer explicit checks: > 1

Follow-up Questions & Optimizations
Follow-up 1

If all numbers are in the range [0, 100], how can we optimize?

Solution: Counting Array (Bucket Counting)

Use an array count[0..100]

count[x] stores the frequency of number x

Maintain a total count of numbers

addNum:

Increment count[num] → O(1)

findMedian:

Scan from 0 to 100 using cumulative counts to locate the median

Fixed range → O(1)

class MedianFinder {
    // count[i] = how many times number i appears
    private int[] count = new int[101];
    private int total = 0;

    public MedianFinder() {}

    public void addNum(int num) {
        count[num]++;
        total++;
    }

    public double findMedian() {
        // positions of median
        int k1 = (total + 1) / 2;
        int k2 = (total % 2 == 0) ? k1 + 1 : k1;

        int prefix = 0;
        int first = -1, second = -1;

        // scan buckets from small to large
        for (int i = 0; i <= 100; i++) {
            prefix += count[i];

            // first time reaching k1
            if (first == -1 && prefix >= k1) {
                first = i;
            }
            // first time reaching k2
            if (prefix >= k2) {
                second = i;
                break;
            }
        }

        return (first + second) / 2.0;
    }
}


Follow-up 2

If 99% of the numbers are in the range [0, 100], how can we optimize?

Solution: Hybrid Approach (Buckets + Heaps)

Use count[0..100] for most numbers

Use:

Max heap for numbers < 0

Min heap for numbers > 100

Maintain total count

addNum:

Bucket insert for [0,100]

Heap insert for outliers

findMedian:

Determine whether the median lies in:

Left heap

Buckets

Right heap

This approach keeps operations efficient while handling rare outliers correctly.

Key Takeaway

When data distribution is known or bounded,
we can replace general data structures with simpler, faster ones.

No constraints → Two heaps

Small fixed range → Counting array

Mostly small range → Hybrid (Buckets + Heaps)