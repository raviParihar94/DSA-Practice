package com.beelive.arrays.problems.arrays;

/**
 * PROBLEM: Median of Two Sorted Arrays
 * -------------------------------------
 * Given two sorted arrays nums1 and nums2 of size m and n, return the
 * median of the two sorted arrays combined. Required time complexity: O(log(m+n)).
 *
 * Example: nums1 = [1,3], nums2 = [2] -> median = 2.0
 *
 * -------------------------------------------------------------------
 * BRUTE FORCE STRATEGY (explained before coding):
 * Merge both arrays into one big sorted array (like the merge step of merge sort),
 * then pick the middle element (or average of two middle elements if even length).
 * This is O(m+n) time and O(m+n) space -- correct, but does not meet the
 * O(log(m+n)) requirement that this problem is famous for.
 *
 * OPTIMIZED STRATEGY:
 * Binary search on the SMALLER array to find a "partition point" that splits
 * the combined array into a left half and right half of equal size (or
 * left half one element bigger if total length is odd), such that every
 * element in the left half <= every element in the right half.
 * Once such a partition is found, the median can be read off directly
 * from the 4 border elements around the partition -- O(log(min(m,n))) time.
 */
public class MedianOfTwoSortedArrays {

    // ---------- BRUTE FORCE: O(m+n) time, O(m+n) space ----------
    public double findMedianBruteForce(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int[] merged = new int[m + n];

        // standard merge-two-sorted-arrays logic
        int i = 0, j = 0, k = 0;
        while (i < m && j < n) {
            merged[k++] = (nums1[i] <= nums2[j]) ? nums1[i++] : nums2[j++];
        }
        while (i < m) merged[k++] = nums1[i++];
        while (j < n) merged[k++] = nums2[j++];

        int total = merged.length;
        if (total % 2 == 1) {
            return merged[total / 2]; // odd length: exact middle element
        } else {
            // even length: average of the two middle elements
            return (merged[total / 2 - 1] + merged[total / 2]) / 2.0;
        }
    }

    // ---------- OPTIMIZED: O(log(min(m,n))) time, O(1) space (binary search on partitions) ----------
    public double findMedian(int[] nums1, int[] nums2) {
        // Always binary search on the smaller array to keep the search space minimal
        if (nums1.length > nums2.length) {
            return findMedian(nums2, nums1);
        }

        int m = nums1.length, n = nums2.length;
        int low = 0, high = m; // we binary search over how many elements of nums1 go into the "left half"

        while (low <= high) {
            // partitionX = elements of nums1 in the left half
            int partitionX = (low + high) / 2;
            // partitionY = elements of nums2 in the left half, chosen so total left size is correct
            int partitionY = (m + n + 1) / 2 - partitionX;

            // Determine the 4 border values around the cut. Use +/- infinity for out-of-range
            // so the comparisons still work correctly at the array edges.
            int maxLeftX = (partitionX == 0) ? Integer.MIN_VALUE : nums1[partitionX - 1];
            int minRightX = (partitionX == m) ? Integer.MAX_VALUE : nums1[partitionX];

            int maxLeftY = (partitionY == 0) ? Integer.MIN_VALUE : nums2[partitionY - 1];
            int minRightY = (partitionY == n) ? Integer.MAX_VALUE : nums2[partitionY];

            // Correct partition found when every left element <= every right element
            if (maxLeftX <= minRightY && maxLeftY <= minRightX) {
                if ((m + n) % 2 == 0) {
                    // even total length: median is avg of the two max-left and min-right values
                    return (Math.max(maxLeftX, maxLeftY) + Math.min(minRightX, minRightY)) / 2.0;
                } else {
                    // odd total length: median is the max of the left half
                    return Math.max(maxLeftX, maxLeftY);
                }
            } else if (maxLeftX > minRightY) {
                // partitionX took too many/large elements -> move it left
                high = partitionX - 1;
            } else {
                // partitionX took too few elements -> move it right
                low = partitionX + 1;
            }
        }

        throw new IllegalArgumentException("Input arrays are not sorted or invalid");
    }

    public static void main(String[] args) {
        MedianOfTwoSortedArrays solver = new MedianOfTwoSortedArrays();

        int[] nums1 = {1, 3};
        int[] nums2 = {2};
        System.out.println("Brute Force: " + solver.findMedianBruteForce(nums1, nums2)); // 2.0
        System.out.println("Optimized:   " + solver.findMedian(nums1, nums2));           // 2.0

        int[] nums3 = {1, 2,3,5,2};
        int[] nums4 = {3, 4,8,9};
        System.out.println("Brute Force: " + solver.findMedianBruteForce(nums3, nums4)); // 2.5
        System.out.println("Optimized:   " + solver.findMedian(nums3, nums4));           // 2.5
    }
}
