package class049;

// 最小覆盖子串
// 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串
// 如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
// 测试链接 : https://leetcode.cn/problems/minimum-window-substring/
public class Code03_MinimumWindowSubstring {

	public static String minWindow(String str, String tar) {
		char[] s = str.toCharArray();
		char[] t = tar.toCharArray();
		// 每种字符的欠债情况
		// cnts[i] = 负数，代表字符i有负债
		// cnts[i] = 正数，代表字符i有盈余
		int[] cnts = new int[256];
		for (char cha : t) {
			cnts[cha]--;
		}
		// 最小覆盖子串的长度
		int len = Integer.MAX_VALUE;
		// 从哪个位置开头，发现的最小覆盖子串
		int start = 0;
		// 总债务
		int debt = t.length;
		for (int l = 0, r = 0; r < s.length; r++) {
			// 窗口右边界向右，给出字符
			if (cnts[s[r]]++ < 0) {
				debt--;
			}
			if (debt == 0) {
				// 窗口左边界向右，拿回字符
				while (cnts[s[l]] > 0) {
					cnts[s[l++]]--;
				}
				// 以r位置结尾的达标窗口，更新答案
				if (r - l + 1 < len) {
					len = r - l + 1;
					start = l;
				}
			}
		}
		return len == Integer.MAX_VALUE ? "" : str.substring(start, start + len);
	}

}



# Minimum Window Substring — Two Sliding Window Patterns

This problem (LeetCode 76) can be solved using two slightly different sliding window structures.

---

## Pattern A — Remove Only Redundant Characters

### Core Idea
When the window becomes valid (`need == 0`):
- Remove only **extra (redundant)** characters
- Stop when the leftmost character becomes necessary

### Structure

```java
if (need == 0) {
    while (count[s.charAt(l)] < 0) {
        count[s.charAt(l)]++;
        l++;
    }
    updateAnswer();
}
Characteristics
Never removes required characters

Never breaks window validity

No need to restore need

Cleaner logic

Slightly more problem-specific

Mental Model
Expand → Valid → Remove redundant → Record answer
Pattern B — Standard Template (Shrink Until Invalid)
Core Idea
When the window becomes valid:

Update answer

Keep shrinking until the window becomes invalid

Restore need when necessary

Structure
while (need == 0) {
    updateAnswer();

    if (++count[s.charAt(l)] > 0) {
        need++;
    }
    l++;
}
Characteristics
May remove required characters

Intentionally breaks window validity

Requires restoring need

More general template

Works for many window problems

Mental Model
Expand → Valid → Record → Shrink → Until invalid
Comparison
Feature	Pattern A	Pattern B
Removes required chars	❌ No	✅ Yes
Needs to restore need	❌ No	✅ Yes
Simpler for this problem	✅ Yes	⚠ Slightly more verbose
More general template	⚠ Medium	⭐ Strong
Why Both Work Here
Minimum Window has a key property:

Once all redundant characters are removed, the window is already minimal for the current right boundary.

Because of this:

Pattern A finds the minimal valid window directly.

Pattern B finds it and then breaks it intentionally.

Both lead to correct answers.

When Pattern A Fails
Pattern A may fail for:

"At most K occurrences"

Dynamic frequency constraints

Complex window validity conditions

Pattern B is safer for general sliding window problems.

