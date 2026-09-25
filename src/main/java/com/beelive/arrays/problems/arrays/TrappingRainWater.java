package com.beelive.arrays.problems.arrays;

/**
 * PROBLEM: Trapping Rain Water
 * ----------------------------
 * Given an array of non-negative integers representing an elevation map
 * where the width of each bar is 1, compute how much water it can trap
 * after raining.
 *
 * Example: height = [0,1,0,2,1,0,1,3,2,1,2,1]  -> Output: 6
 *
 * KEY INSIGHT:
 * Water trapped above index i = min(maxLeft[i], maxRight[i]) - height[i]
 * (only if this value is positive, otherwise 0 water is trapped there)
 *
 * -------------------------------------------------------------------
 * BRUTE FORCE STRATEGY (explained before coding):
 * For every index i, find the tallest bar to its left (maxLeft) and
 * the tallest bar to its right (maxRight) by scanning outward each time.
 * Water trapped at i = min(maxLeft, maxRight) - height[i].
 * This means for EVERY index we re-scan the whole array twice (left + right),
 * giving O(n^2) time. It works, but it's wasteful because maxLeft/maxRight
 * values get recomputed again and again for neighboring indices.
 *
 * OPTIMIZED STRATEGY:
 * Precompute maxLeft[] and maxRight[] arrays in O(n) each (two passes),
 * then do a single pass to sum up trapped water -> O(n) time, O(n) space.
 * Can be pushed further to O(1) space using a two-pointer technique,
 * which is what we implement below as the "efficient" solution.
 */
public class TrappingRainWater {

    // ---------- BRUTE FORCE: O(n^2) time, O(1) space ----------
    public int trapBruteForce(int[] height) {
        int n = height.length;
        int totalWater = 0;

        for (int i = 0; i < n; i++) {
            int maxLeft = 0;
            int maxRight = 0;

            // scan everything to the left of i (including i) for the tallest bar
            for (int left = 0; left <= i; left++) {
                maxLeft = Math.max(maxLeft, height[left]);
            }

            // scan everything to the right of i (including i) for the tallest bar
            for (int right = i; right < n; right++) {
                maxRight = Math.max(maxRight, height[right]);
            }

            // water trapped at this index is bounded by the shorter of the two walls
            int waterHere = Math.min(maxLeft, maxRight) - height[i];
            totalWater += waterHere; // waterHere is always >= 0 by definition of maxLeft/maxRight
        }

        return totalWater;
    }

    // ---------- OPTIMIZED: O(n) time, O(1) extra space (two pointers) ----------
    public int trap(int[] height) {
        if (height == null || height.length == 0) return 0;

        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int totalWater = 0;

        // Two pointers move inward. At each step we know FOR SURE the water level
        // on the side with the smaller "max so far", because the other side
        // already guarantees a wall at least as tall exists somewhere ahead.
        while (left < right) {
            if (height[left] < height[right]) {
                // left side is the limiting wall
                if (height[left] >= leftMax) {
                    // found a new tallest wall on the left, no water trapped here
                    leftMax = height[left];
                } else {
                    // current bar is shorter than tallest wall seen so far on the left
                    // -> water is trapped above it, bounded by leftMax
                    totalWater += leftMax - height[left];
                }
                left++;
            } else {
                // right side is the limiting wall (symmetric logic)
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    totalWater += rightMax - height[right];
                }
                right--;
            }
        }

        return totalWater;
    }

    public static void main(String[] args) {
        TrappingRainWater solver = new TrappingRainWater();
        int[] height = {0,1,0,2,1,0,1,3,2,1,2,1};

        System.out.println("Brute Force Result: " + solver.trapBruteForce(height)); // 6
        System.out.println("Optimized Result:   " + solver.trap(height));           // 6
    }
}
