package class048;

// 边框为1的最大正方形
// 给你一个由若干 0 和 1 组成的二维网格 grid
// 请你找出边界全部由 1 组成的最大 正方形 子网格
// 并返回该子网格中的元素数量。如果不存在，则返回 0。
// 测试链接 : https://leetcode.cn/problems/largest-1-bordered-square/
time complexity O(n * m * min(n,m))，额外空间复杂度O(1)
space complexity O(1)
public class Code02_LargestOneBorderedSquare {

	// 打败比例不高，但完全是常数时间的问题
	// 时间复杂度O(n * m * min(n,m))，额外空间复杂度O(1)
	// 复杂度指标上绝对是最优解
	public static int largest1BorderedSquare(int[][] g) {
		int n = g.length;
		int m = g[0].length;
		build(n, m, g);
		if (sum(g, 0, 0, n - 1, m - 1) == 0) {
			return 0;
		}
		// 找到的最大合法正方形的边长
		int ans = 1;
		for (int a = 0; a < n; a++) {
			for (int b = 0; b < m; b++) {
				// (a,b)所有左上角点
				//     (c,d)更大边长的右下角点，k是当前尝试的边长
				for (int c = a + ans, d = b + ans, k = ans + 1; c < n && d < m; c++, d++, k++) {
					if (sum(g, a, b, c, d) - sum(g, a + 1, b + 1, c - 1, d - 1) == (k - 1) << 2) {
						ans = k;
					}
				}
			}
		}
		return ans * ans;
	}

	// g : 原始二维数组
	// 把g变成原始二维数组的前缀和数组sum，复用自己
	// 不能补0行，0列，都是0
	public static void build(int n, int m, int[][] g) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				g[i][j] += get(g, i, j - 1) + get(g, i - 1, j) - get(g, i - 1, j - 1);
			}
		}
	}

	public static int sum(int[][] g, int a, int b, int c, int d) {
		return a > c ? 0 : (g[c][d] - get(g, c, b - 1) - get(g, a - 1, d) + get(g, a - 1, b - 1));
	}

	public static int get(int[][] g, int i, int j) {
		return (i < 0 || j < 0) ? 0 : g[i][j];
	}

}


Idea / Approach

1.Enumerate Squares

Fix the top-left corner (a, b) of the square.

Increase the side length k by expanding equally to the right and downward.

The enumeration itself guarantees that the shape is a square.

2.Prefix Sum Preprocessing

Build a 2D prefix sum array from the grid.

This allows querying the number of 1s in any submatrix in O(1) time.

3.Border Validation

The number of 1s on the border of a k × k square is computed as:

sum(outer square) − sum(inner square)


4.A valid square border must contain exactly 4 × (k − 1) cells.

If the computed value matches this number, the square’s border is entirely composed of 1s.

5.Update the Answer

Track and update the maximum valid side length found.

Core Insight (One Sentence)

Square enumeration ensures the shape, and prefix sums are only used to efficiently verify that the square’s border consists entirely of ones.