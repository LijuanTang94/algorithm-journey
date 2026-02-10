package class048;

// 二维差分模版(洛谷)
// 测试链接 : https://www.luogu.com.cn/problem/P3397
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过
//LeetCode 2536 – Increment Submatrices by One// 题目链接 : https://leetcode.com/problems/increment-submatrices-by-one/
//本题使用n+2的差分数组，避免了边界检查，四周都加了1行1列的padding，差分数组的有效范围是[1, n]，这样在更新时就不需要担心越界问题了。
//差分数组更新后，直接通过前缀和计算出最终的结果矩阵，最后再将结果矩阵中的有效部分（[1, n]）复制到返回的结果数组中。
class Solution {
    public int[][] rangeAddQueries(int n, int[][] queries) {
        int[][] diff = new int[n + 2][n + 2];
        for(int[] q: queries) {
            int a = q[0], b = q[1], c = q[2], d = q[3];
            diff[a + 1][b + 1] += 1;
            diff[a + 1][d + 2] -= 1;
            diff[c + 2][b + 1] -= 1;
            diff[c + 2][d + 2] += 1;
        }
        for(int i = 1; i <= n; i++) {
            for(int j = 1; j <= n; j++) {
                diff[i][j] += diff[i - 1][j] + diff[i][j - 1] - diff[i - 1][j - 1];
            }
        }
        int[][] res = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                res[i][j] = diff[i + 1][j + 1];
            }
        }
        return res;
    }
    
}

2D Difference Array Technique
Core Idea:
1.Instead of updating every cell in the range directly (which is slow), we use a 2D difference array to mark only the boundaries of each range update. Then we reconstruct the final result in one pass.
Algorithm Steps:

2.Create a difference array diff[n+2][n+2] with padding to avoid boundary checks
Mark range boundaries for each query [row1, col1, row2, col2]:

Add +1 at top-left corner: diff[row1+1][col1+1]
Add -1 at top-right boundary: diff[row1+1][col2+2]
Add -1 at bottom-left boundary: diff[row2+2][col1+1]
Add +1 at bottom-right corner: diff[row2+2][col2+2]

This creates a "delta" that affects only the rectangular region when we compute prefix sums.
Compute 2D prefix sum to reconstruct the actual values:

   diff[i][j] += diff[i-1][j] + diff[i][j-1] - diff[i-1][j-1]
This propagates the boundary markers to fill in the entire grid.

3.Extract result by copying the relevant portion (indices 1 to n) back to the result array.

4.Time Complexity: O(q + n²) where q is the number of queries
5.Space Complexity: O(n²)
This technique efficiently handles multiple overlapping range updates by deferring the actual computation until the end.