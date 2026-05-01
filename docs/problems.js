const PROBLEMS = [
  {
    id: 1,
    title: "Two Sum",
    difficulty: "Easy",
    category: "Arrays",
    description: "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.",
    starterCode: "import java.util.*;\n\npublic class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        // Your code here\n        return new int[]{};\n    }\n\n    public static void main(String[] args) {\n        Solution sol = new Solution();\n        int[] res = sol.twoSum(new int[]{2, 7, 11, 15}, 9);\n        System.out.println(Arrays.toString(res));\n    }\n}",
    hint: "Use a HashMap to store the numbers you've seen so far and their indices.",
    testCases: [
      { input: "2 7 11 15\n9", output: "[0, 1]" }
    ]
  },
  {
    id: 2,
    title: "Reverse String",
    difficulty: "Easy",
    category: "Strings",
    description: "Write a function that reverses a string. The input string is given as an array of characters s.\n\nYou must do this by modifying the input array in-place with O(1) extra memory.",
    starterCode: "import java.util.*;\n\npublic class Solution {\n    public void reverseString(char[] s) {\n        // Your code here\n    }\n\n    public static void main(String[] args) {\n        Solution sol = new Solution();\n        char[] s = {'h','e','l','l','o'};\n        sol.reverseString(s);\n        System.out.println(Arrays.toString(s));\n    }\n}",
    hint: "Use two pointers, one at the start and one at the end, and swap them.",
    testCases: [
      { input: "h e l l o", output: "[o, l, l, e, h]" }
    ]
  },
  {
    id: 3,
    title: "Palindrome Linked List",
    difficulty: "Medium",
    category: "DSA",
    description: "Given the head of a singly linked list, return true if it is a palindrome or false otherwise.",
    starterCode: "class ListNode {\n    int val; ListNode next; \n    ListNode(int x) { val = x; }\n}\n\npublic class Solution {\n    public boolean isPalindrome(ListNode head) {\n        // Your code here\n        return true;\n    }\n\n    public static void main(String[] args) {\n        System.out.println(\"Test with [1,2,2,1]\");\n        // Example logic\n    }\n}",
    hint: "Find the middle of the list, reverse the second half, and compare.",
    testCases: []
  },
  {
    id: 4,
    title: "Fibonacci Number",
    difficulty: "Easy",
    category: "Math",
    description: "The Fibonacci numbers, commonly denoted F(n) form a sequence, called the Fibonacci sequence, such that each number is the sum of the two preceding ones, starting from 0 and 1.",
    starterCode: "public class Solution {\n    public int fib(int n) {\n        // Your code here\n        return 0;\n    }\n\n    public static void main(String[] args) {\n        Solution sol = new Solution();\n        System.out.println(sol.fib(4));\n    }\n}",
    hint: "Use recursion or dynamic programming.",
    testCases: [
      { input: "4", output: "3" }
    ]
  },
  {
    id: 5,
    title: "Longest Increasing Subsequence",
    difficulty: "Hard",
    category: "DP",
    description: "Given an integer array nums, return the length of the longest strictly increasing subsequence.",
    starterCode: "public class Solution {\n    public int lengthOfLIS(int[] nums) {\n        // Your code here\n        return 0;\n    }\n\n    public static void main(String[] args) {\n        Solution sol = new Solution();\n        System.out.println(sol.fib(new int[]{10,9,2,5,3,7,101,18}));\n    }\n}",
    hint: "Use dynamic programming with O(n^2) or binary search with O(n log n).",
    testCases: [
      { input: "10 9 2 5 3 7 101 18", output: "4" }
    ]
  }
];
