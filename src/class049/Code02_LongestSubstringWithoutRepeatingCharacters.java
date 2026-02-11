package class049;

import java.util.Arrays;

// 无重复字符的最长子串
// 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
// 测试链接 : https://leetcode.cn/problems/longest-substring-without-repeating-characters/

public class Code02_LongestSubstringWithoutRepeatingCharacters {

	public static int lengthOfLongestSubstring(String str) {
		char[] s = str.toCharArray();
		int n = s.length;
		// char -> int -> 0 ~ 255
		// 每一种字符上次出现的位置
		int[] last = new int[256];
		// 所有字符都没有上次出现的位置
		Arrays.fill(last, -1);
		// 不含有重复字符的 最长子串 的长度
		int ans = 0;
		for (int l = 0, r = 0; r < n; r++) {
			l = Math.max(l, last[s[r]] + 1);
			ans = Math.max(ans, r - l + 1);
			// 更新当前字符上一次出现的位置
			last[s[r]] = r;
		}
		return ans;
	}

}


Commonly used tables are:

int[26] for Letters 'a' - 'z' or 'A' - 'Z'
int[128] for ASCII
int[256] for Extended ASCII


# Longest Substring Without Repeating Characters

## Core Idea

Use a **sliding window** with two pointers to maintain a substring that contains **no repeated characters**.

The right pointer expands the window, and the left pointer moves only when the window becomes invalid.

---

## Approach 1: Last Occurrence Index (Recommended)

### Idea
- Maintain an array `last[c]` that stores the **last index** where character `c` appeared.
- When a repeated character is found, **jump the left pointer directly** to the next valid position.
- This avoids shrinking the window step by step.

### Key Steps
1. Initialize `last[]` with `-1`.
2. For each position `r`:
   - Update `l = max(l, last[s.charAt(r)] + 1)`
   - Update `last[s.charAt(r)] = r`
3. Update the answer with `r - l + 1`.

### Time Complexity
- O(n), each character is processed once.

### Why It’s Better
- Smaller constant factor.
- Cleaner logic.
- Best choice when each character is allowed **at most once**.

---

## Approach 2: Frequency Count Sliding Window (General)

### Idea
- Maintain a frequency array `count[]` for characters inside the window.
- Expand the window by moving `r`.
- If a character appears more than once, **shrink the window** by moving `l`.

### Key Steps
1. Increase `count[s.charAt(r)]`.
2. While the count of the current character is greater than 1:
   - Decrease `count[s.charAt(l)]`
   - Move `l`
3. Update the answer.

### Time Complexity
- O(n), but with larger constant due to repeated shrinking.

### When to Use
- Problems with frequency constraints:
  - “at most K occurrences”
  - “anagrams”
  - “minimum window substring”
