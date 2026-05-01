package MiniCodeJudgeSystem;

import java.util.*;

public class ProblemStore {
    private static List<Problem> problems = new ArrayList<>();

    static {

        // 1. Two Sum
        problems.add(new Problem(1, "Two Sum",
            "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.\n\nConstraints:\n- 2 <= nums.length <= 10^4\n- -10^9 <= nums[i] <= 10^9\n- Only one valid answer exists.\n\nExample:\nInput: nums = [2,7,11,15], target = 9\nOutput: [0,1]\nExplanation: nums[0] + nums[1] = 2 + 7 = 9",
            "Solution",
            "public class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        // Use a HashMap to store value -> index\n        \n    }\n}",
            "EASY", "Arrays", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("[2,7,11,15]\n9", "[0,1]")),
            Arrays.asList(
                new Problem.TestCase("[3,2,4]\n6", "[1,2]"),
                new Problem.TestCase("[3,3]\n6", "[0,1]")
            )));

        // 2. Palindrome Number
        problems.add(new Problem(2, "Palindrome Number",
            "Given an integer x, return true if x is a palindrome, and false otherwise.\n\nAn integer is a palindrome when it reads the same forward and backward.\n\nConstraints:\n- -2^31 <= x <= 2^31 - 1\n\nFollow up: Could you solve it without converting the integer to a string?\n\nExample:\nInput: x = 121\nOutput: true\n\nInput: x = -121\nOutput: false\nExplanation: From left to right reads -121. From right to left -121. Therefore it is not a palindrome.",
            "Solution",
            "public class Solution {\n    public boolean isPalindrome(int x) {\n        \n    }\n}",
            "EASY", "Math", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("121", "true")),
            Arrays.asList(
                new Problem.TestCase("-121", "false"),
                new Problem.TestCase("10", "false"),
                new Problem.TestCase("0", "true")
            )));

        // 3. Roman to Integer
        problems.add(new Problem(3, "Roman to Integer",
            "Roman numerals are represented by seven different symbols: I(1), V(5), X(10), L(50), C(100), D(500), M(1000).\n\nFor example, 2 is written as II, 12 is XII, 27 is XXVII.\n\nRoman numerals are usually written largest to smallest. However, there are six special subtractive cases:\n- I before V or X: 4 (IV), 9 (IX)\n- X before L or C: 40 (XL), 90 (XC)\n- C before D or M: 400 (CD), 900 (CM)\n\nConstraints:\n- 1 <= s.length <= 15\n- s contains only I, V, X, L, C, D, M\n- 1 <= answer <= 3999\n\nExample:\nInput: s = \"III\"\nOutput: 3\n\nInput: s = \"MCMXCIV\"\nOutput: 1994",
            "Solution",
            "public class Solution {\n    public int romanToInt(String s) {\n        \n    }\n}",
            "EASY", "Strings", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("III", "3")),
            Arrays.asList(
                new Problem.TestCase("LVIII", "58"),
                new Problem.TestCase("MCMXCIV", "1994")
            )));

        // 4. Longest Common Prefix
        problems.add(new Problem(4, "Longest Common Prefix",
            "Write a function to find the longest common prefix string amongst an array of strings.\n\nIf there is no common prefix, return an empty string \"\".\n\nConstraints:\n- 1 <= strs.length <= 200\n- 0 <= strs[i].length <= 200\n- strs[i] consists of only lowercase English letters.\n\nExample:\nInput: strs = [\"flower\",\"flow\",\"flight\"]\nOutput: \"fl\"\n\nInput: strs = [\"dog\",\"racecar\",\"car\"]\nOutput: \"\"\nExplanation: There is no common prefix among the input strings.",
            "Solution",
            "public class Solution {\n    public String longestCommonPrefix(String[] strs) {\n        \n    }\n}",
            "EASY", "Strings", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("[\"flower\",\"flow\",\"flight\"]", "fl")),
            Arrays.asList(
                new Problem.TestCase("[\"dog\",\"racecar\",\"car\"]", ""),
                new Problem.TestCase("[\"interview\",\"inter\",\"interact\"]", "inter")
            )));

        // 5. Valid Parentheses
        problems.add(new Problem(5, "Valid Parentheses",
            "Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.\n\nAn input string is valid if:\n1. Open brackets must be closed by the same type of brackets.\n2. Open brackets must be closed in the correct order.\n3. Every close bracket has a corresponding open bracket.\n\nConstraints:\n- 1 <= s.length <= 10^4\n- s consists of parentheses only '()[]{}'.\n\nExample:\nInput: s = \"()\"\nOutput: true\n\nInput: s = \"([)]\"\nOutput: false",
            "Solution",
            "public class Solution {\n    public boolean isValid(String s) {\n        // Use a Stack\n        \n    }\n}",
            "EASY", "DSA", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("()", "true")),
            Arrays.asList(
                new Problem.TestCase("()[]{}", "true"),
                new Problem.TestCase("(]", "false"),
                new Problem.TestCase("{[]}", "true")
            )));

        // 6. Merge Two Sorted Lists
        problems.add(new Problem(6, "Merge Two Sorted Lists",
            "You are given the heads of two sorted linked lists list1 and list2.\n\nMerge the two lists into one sorted list. The list should be made by splicing together the nodes of the first two lists.\n\nReturn the head of the merged linked list.\n\nConstraints:\n- The number of nodes in both lists is in the range [0, 50].\n- -100 <= Node.val <= 100\n- Both list1 and list2 are sorted in non-decreasing order.\n\nExample:\nInput: list1 = [1,2,4], list2 = [1,3,4]\nOutput: [1,1,2,3,4,4]\n\nInput: list1 = [], list2 = []\nOutput: []",
            "Solution",
            "// ListNode is already defined for you:\n// class ListNode { int val; ListNode next; ListNode(int v){val=v;} }\n\npublic class Solution {\n    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {\n        \n    }\n}",
            "EASY", "DSA", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("[1,2,4]\n[1,3,4]", "[1,1,2,3,4,4]")),
            Arrays.asList(
                new Problem.TestCase("[]\n[]", "[]"),
                new Problem.TestCase("[]\n[0]", "[0]")
            )));

        // 7. Remove Duplicates from Sorted Array
        problems.add(new Problem(7, "Remove Duplicates from Sorted Array",
            "Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place such that each unique element appears only once.\n\nThe relative order of the elements should be kept the same. Return k — the number of unique elements.\n\nConstraints:\n- 1 <= nums.length <= 3 * 10^4\n- -100 <= nums[i] <= 100\n- nums is sorted in non-decreasing order.\n\nExample:\nInput: nums = [1,1,2]\nOutput: 2\nExplanation: After removing duplicates, nums = [1,2,_]. Return k = 2.\n\nInput: nums = [0,0,1,1,1,2,2,3,3,4]\nOutput: 5",
            "Solution",
            "public class Solution {\n    public int removeDuplicates(int[] nums) {\n        \n    }\n}",
            "EASY", "Arrays", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("[1,1,2]", "2")),
            Arrays.asList(
                new Problem.TestCase("[0,0,1,1,1,2,2,3,3,4]", "5"),
                new Problem.TestCase("[1,2,3]", "3")
            )));

        // 8. Search Insert Position
        problems.add(new Problem(8, "Search Insert Position",
            "Given a sorted array of distinct integers and a target value, return the index if the target is found. If not, return the index where it would be if it were inserted in order.\n\nYou must write an algorithm with O(log n) runtime complexity.\n\nConstraints:\n- 1 <= nums.length <= 10^4\n- -10^4 <= nums[i] <= 10^4\n- nums contains distinct values sorted in ascending order.\n- -10^4 <= target <= 10^4\n\nExample:\nInput: nums = [1,3,5,6], target = 5\nOutput: 2\n\nInput: nums = [1,3,5,6], target = 2\nOutput: 1",
            "Solution",
            "public class Solution {\n    public int searchInsert(int[] nums, int target) {\n        // Binary search\n        \n    }\n}",
            "EASY", "Arrays", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("[1,3,5,6]\n5", "2")),
            Arrays.asList(
                new Problem.TestCase("[1,3,5,6]\n2", "1"),
                new Problem.TestCase("[1,3,5,6]\n7", "4")
            )));

        // 9. Climbing Stairs
        problems.add(new Problem(9, "Climbing Stairs",
            "You are climbing a staircase. It takes n steps to reach the top.\n\nEach time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?\n\nConstraints:\n- 1 <= n <= 45\n\nHint: This is essentially a Fibonacci sequence!\n\nExample:\nInput: n = 2\nOutput: 2\nExplanation: 1+1, 2\n\nInput: n = 3\nOutput: 3\nExplanation: 1+1+1, 1+2, 2+1",
            "Solution",
            "public class Solution {\n    public int climbStairs(int n) {\n        // Dynamic Programming / Fibonacci\n        \n    }\n}",
            "EASY", "DP", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("2", "2")),
            Arrays.asList(
                new Problem.TestCase("3", "3"),
                new Problem.TestCase("5", "8"),
                new Problem.TestCase("10", "89")
            )));

        // 10. Maximum Subarray (Kadane's Algorithm)
        problems.add(new Problem(10, "Maximum Subarray",
            "Given an integer array nums, find the subarray with the largest sum, and return its sum.\n\nConstraints:\n- 1 <= nums.length <= 10^5\n- -10^4 <= nums[i] <= 10^4\n\nFollow up: Can you find an O(n) solution using Kadane's Algorithm?\n\nExample:\nInput: nums = [-2,1,-3,4,-1,2,1,-5,4]\nOutput: 6\nExplanation: The subarray [4,-1,2,1] has the largest sum 6.\n\nInput: nums = [1]\nOutput: 1",
            "Solution",
            "public class Solution {\n    public int maxSubArray(int[] nums) {\n        // Kadane's Algorithm\n        \n    }\n}",
            "MEDIUM", "Arrays", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("[-2,1,-3,4,-1,2,1,-5,4]", "6")),
            Arrays.asList(
                new Problem.TestCase("[1]", "1"),
                new Problem.TestCase("[5,4,-1,7,8]", "23")
            )));

        // 11. Reverse String
        problems.add(new Problem(11, "Reverse String",
            "Write a function that reverses a string. The input string is given as an array of characters s.\n\nYou must do this by modifying the input array in-place with O(1) extra memory.\n\nConstraints:\n- 1 <= s.length <= 10^5\n- s[i] is a printable ASCII character.\n\nExample:\nInput: s = ['h','e','l','l','o']\nOutput: ['o','l','l','e','h']",
            "Solution11",
            "public class Solution11 {\n    public static void main(String[] args) {\n        char[] s = {'h','e','l','l','o'};\n        new Solution11().reverseString(s);\n        System.out.println(new String(s));\n    }\n    public void reverseString(char[] s) {\n        // Two pointer approach\n        \n    }\n}",
            "EASY", "Strings", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("", "olleh")),
            Arrays.asList(new Problem.TestCase("", "olleh"))));

        // 12. FizzBuzz
        problems.add(new Problem(12, "FizzBuzz",
            "Given an integer n, return a string array answer where:\n- answer[i] == \"FizzBuzz\" if i is divisible by 3 and 5.\n- answer[i] == \"Fizz\" if i is divisible by 3.\n- answer[i] == \"Buzz\" if i is divisible by 5.\n- answer[i] == i (as a string) if none of the above conditions are true.\n\nConstraints:\n- 1 <= n <= 10^4\n\nExample:\nInput: n = 5\nOutput: [\"1\",\"2\",\"Fizz\",\"4\",\"Buzz\"]\n\nInput: n = 15\nOutput: [\"1\",\"2\",\"Fizz\",\"4\",\"Buzz\",\"Fizz\",\"7\",\"8\",\"Fizz\",\"Buzz\",\"11\",\"Fizz\",\"13\",\"14\",\"FizzBuzz\"]",
            "Solution12",
            "import java.util.*;\npublic class Solution12 {\n    public static void main(String[] args) {\n        System.out.println(new Solution12().fizzBuzz(5));\n    }\n    public List<String> fizzBuzz(int n) {\n        \n    }\n}",
            "EASY", "Math", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("", "[1, 2, Fizz, 4, Buzz]")),
            Arrays.asList(new Problem.TestCase("", "[1, 2, Fizz, 4, Buzz]"))));

        // 13. Count Primes
        problems.add(new Problem(13, "Count Primes",
            "Given an integer n, return the number of prime numbers that are strictly less than n.\n\nConstraints:\n- 0 <= n <= 5 * 10^6\n\nHint: Use the Sieve of Eratosthenes for optimal O(n log log n) performance.\n\nExample:\nInput: n = 10\nOutput: 4\nExplanation: There are 4 prime numbers less than 10: 2, 3, 5, 7.\n\nInput: n = 0\nOutput: 0",
            "Solution13",
            "public class Solution13 {\n    public static void main(String[] args) {\n        System.out.println(new Solution13().countPrimes(10));\n    }\n    public int countPrimes(int n) {\n        // Sieve of Eratosthenes\n        \n    }\n}",
            "MEDIUM", "Math", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("", "4")),
            Arrays.asList(new Problem.TestCase("", "4"))));

        // 14. Power of Two
        problems.add(new Problem(14, "Power of Two",
            "Given an integer n, return true if it is a power of two. Otherwise, return false.\n\nAn integer n is a power of two if there exists an integer x such that n == 2^x.\n\nConstraints:\n- -2^31 <= n <= 2^31 - 1\n\nHint: A power of two in binary has exactly one bit set. Try using bit manipulation: n & (n-1) == 0.\n\nExample:\nInput: n = 1\nOutput: true (2^0 = 1)\n\nInput: n = 3\nOutput: false\n\nInput: n = 16\nOutput: true (2^4 = 16)",
            "Solution14",
            "public class Solution14 {\n    public static void main(String[] args) {\n        System.out.println(new Solution14().isPowerOfTwo(16));\n    }\n    public boolean isPowerOfTwo(int n) {\n        // Bit manipulation trick\n        \n    }\n}",
            "EASY", "Math", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("", "true")),
            Arrays.asList(new Problem.TestCase("", "true"))));

        // 15. Reverse Linked List
        problems.add(new Problem(15, "Reverse Linked List",
            "Given the head of a singly linked list, reverse the list, and return the reversed list.\n\nConstraints:\n- The number of nodes in the list is in the range [0, 5000].\n- -5000 <= Node.val <= 5000\n\nFollow up: A linked list can be reversed either iteratively or recursively. Could you implement both?\n\nExample:\nInput: head = [1,2,3,4,5]\nOutput: [5,4,3,2,1]\n\nInput: head = [1,2]\nOutput: [2,1]",
            "Solution15",
            "// ListNode is already available\nclass ListNode15 { int val; ListNode15 next; ListNode15(int v){val=v;} }\n\npublic class Solution15 {\n    public static void main(String[] args) {\n        ListNode15 h = new ListNode15(1);\n        h.next = new ListNode15(2);\n        h.next.next = new ListNode15(3);\n        ListNode15 r = new Solution15().reverseList(h);\n        StringBuilder sb = new StringBuilder(\"[\");\n        while(r!=null){sb.append(r.val);if(r.next!=null)sb.append(\",\");r=r.next;}\n        sb.append(\"]\");\n        System.out.println(sb);\n    }\n    public ListNode15 reverseList(ListNode15 head) {\n        // Iterative approach\n        \n    }\n}",
            "EASY", "DSA", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("", "[3,2,1]")),
            Arrays.asList(new Problem.TestCase("", "[3,2,1]"))));

        // 16. Best Time to Buy and Sell Stock
        problems.add(new Problem(16, "Best Time to Buy and Sell Stock",
            "You are given an array prices where prices[i] is the price of a given stock on the i-th day.\n\nYou want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.\n\nReturn the maximum profit you can achieve. If you cannot achieve any profit, return 0.\n\nConstraints:\n- 1 <= prices.length <= 10^5\n- 0 <= prices[i] <= 10^4\n\nExample:\nInput: prices = [7,1,5,3,6,4]\nOutput: 5\nExplanation: Buy on day 2 (price=1), sell on day 5 (price=6). Profit = 6-1 = 5.\n\nInput: prices = [7,6,4,3,1]\nOutput: 0\nExplanation: Prices only decrease, no profitable transaction.",
            "Solution16",
            "public class Solution16 {\n    public static void main(String[] args) {\n        System.out.println(new Solution16().maxProfit(new int[]{7,1,5,3,6,4}));\n    }\n    public int maxProfit(int[] prices) {\n        // Track minimum price seen so far\n        \n    }\n}",
            "EASY", "Arrays", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("", "5")),
            Arrays.asList(new Problem.TestCase("", "5"))));

        // 17. Valid Anagram
        problems.add(new Problem(17, "Valid Anagram",
            "Given two strings s and t, return true if t is an anagram of s, and false otherwise.\n\nAn anagram is a word or phrase formed by rearranging the letters of a different word, using all the original letters exactly once.\n\nConstraints:\n- 1 <= s.length, t.length <= 5 * 10^4\n- s and t consist of lowercase English letters.\n\nFollow up: What if the inputs contain Unicode characters?\n\nExample:\nInput: s = \"anagram\", t = \"nagaram\"\nOutput: true\n\nInput: s = \"rat\", t = \"car\"\nOutput: false",
            "Solution17",
            "public class Solution17 {\n    public static void main(String[] args) {\n        System.out.println(new Solution17().isAnagram(\"anagram\", \"nagaram\"));\n    }\n    public boolean isAnagram(String s, String t) {\n        // Use frequency count array\n        \n    }\n}",
            "EASY", "Strings", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("", "true")),
            Arrays.asList(new Problem.TestCase("", "true"))));

        // 18. Missing Number
        problems.add(new Problem(18, "Missing Number",
            "Given an array nums containing n distinct numbers in the range [0, n], return the only number in the range that is missing from the array.\n\nConstraints:\n- n == nums.length\n- 1 <= n <= 10^4\n- 0 <= nums[i] <= n\n- All the numbers of nums are unique.\n\nFollow up: Could you implement a solution using only O(1) extra space and O(n) runtime complexity? Hint: Use the Gauss formula: sum = n*(n+1)/2.\n\nExample:\nInput: nums = [3,0,1]\nOutput: 2\n\nInput: nums = [9,6,4,2,3,5,7,0,1]\nOutput: 8",
            "Solution18",
            "public class Solution18 {\n    public static void main(String[] args) {\n        System.out.println(new Solution18().missingNumber(new int[]{3,0,1}));\n    }\n    public int missingNumber(int[] nums) {\n        // Gauss formula or XOR trick\n        \n    }\n}",
            "EASY", "Math", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("", "2")),
            Arrays.asList(new Problem.TestCase("", "2"))));

        // 19. Longest Substring Without Repeating Characters
        problems.add(new Problem(19, "Longest Substring Without Repeating Characters",
            "Given a string s, find the length of the longest substring without repeating characters.\n\nConstraints:\n- 0 <= s.length <= 5 * 10^4\n- s consists of English letters, digits, symbols, and spaces.\n\nHint: Use a sliding window with a HashSet.\n\nExample:\nInput: s = \"abcabcbb\"\nOutput: 3\nExplanation: The answer is \"abc\", with length 3.\n\nInput: s = \"bbbbb\"\nOutput: 1\nExplanation: The answer is \"b\", with length 1.\n\nInput: s = \"pwwkew\"\nOutput: 3\nExplanation: The answer is \"wke\", with length 3.",
            "Solution19",
            "public class Solution19 {\n    public static void main(String[] args) {\n        System.out.println(new Solution19().lengthOfLongestSubstring(\"abcabcbb\"));\n    }\n    public int lengthOfLongestSubstring(String s) {\n        // Sliding window + HashSet\n        \n    }\n}",
            "MEDIUM", "Strings", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("", "3")),
            Arrays.asList(new Problem.TestCase("", "3"))));

        // 20. Container With Most Water
        problems.add(new Problem(20, "Container With Most Water",
            "You are given an integer array height of length n. There are n vertical lines drawn such that the two endpoints of the i-th line are (i, 0) and (i, height[i]).\n\nFind two lines that together with the x-axis form a container that contains the most water.\n\nReturn the maximum amount of water a container can store.\n\nConstraints:\n- n == height.length\n- 2 <= n <= 10^5\n- 0 <= height[i] <= 10^4\n\nHint: Use two pointers starting from both ends.\n\nExample:\nInput: height = [1,8,6,2,5,4,8,3,7]\nOutput: 49\nExplanation: Lines at index 1 (h=8) and 8 (h=7). Area = min(8,7) * (8-1) = 7*7 = 49.",
            "Solution20",
            "public class Solution20 {\n    public static void main(String[] args) {\n        System.out.println(new Solution20().maxArea(new int[]{1,8,6,2,5,4,8,3,7}));\n    }\n    public int maxArea(int[] height) {\n        // Two pointer approach\n        \n    }\n}",
            "MEDIUM", "Arrays", "Here is a helpful hint for this problem to guide your thinking.",
            Arrays.asList(new Problem.TestCase("", "49")),
            Arrays.asList(new Problem.TestCase("", "49"))));
        // Generate remaining problems up to 3000
        generateAIBasedProblems();
    }

    private static void generateAIBasedProblems() {
        String[] arrays = {"Maximum Subarray Sum", "Reverse Array in Place", "Find Duplicate Number", "Median of Two Arrays", "Rotate Image 90 Deg", "Kth Largest Element", "Merge Intervals", "Subarray Sum Equals K", "Next Permutation", "Container With Most Water"};
        String[] strings = {"Valid Anagram", "Group Anagrams", "Longest Palindromic Substring", "Edit Distance", "Wildcard Matching", "Check Subsequence", "Reverse Words in String", "Multiply Strings", "Decode String", "Longest Substring Without Repeating"};
        String[] dsa = {"Implement Queue using Stacks", "Detect Cycle in Linked List", "Balanced Binary Tree Check", "LRU Cache Implementation", "Trie (Prefix Tree) Insert/Search", "Level Order Traversal", "Invert Binary Tree", "Validate BST", "Diameter of Binary Tree", "Min Stack"};
        String[] math = {"Sieve of Eratosthenes", "GCD of Two Numbers", "Power of Three Check", "Happy Number", "Factorial Trailing Zeroes", "Excel Sheet Column Title", "Valid Perfect Square", "Count Primes", "Self Dividing Numbers", "Base 7 Conversion"};
        String[] dp = {"House Robber", "Coin Change", "Longest Increasing Subsequence", "Unique Paths", "Maximum Path Sum", "Partition Equal Subset Sum", "Regular Expression Matching", "Word Break", "Target Sum", "Jump Game"};

        String[] templates = {
            "Given a data structure, implement an efficient algorithm for **%s** that handles large inputs and edge cases.\n\nConstraints:\n- 1 <= Input Size <= 10^5\n- Memory limit: 256MB\n\nExample:\nInput: []\nOutput: 0",
            "Solve the **%s** problem using optimal time and space complexity. This challenge tests your ability to manipulate data structures efficiently under memory constraints.\n\nConstraints:\n- 1 <= Input Size <= 10^5\n- Memory limit: 256MB\n\nExample:\nInput: []\nOutput: 0",
            "Complete the function for **%s** ensuring all edge cases (negative numbers, zero, large integers) are covered.\n\nConstraints:\n- 1 <= Input Size <= 10^5\n- Memory limit: 256MB\n\nExample:\nInput: []\nOutput: 0",
            "Find a solution for **%s** that works in O(n) or O(log n) time. This is a classic coding interview problem.\n\nConstraints:\n- 1 <= Input Size <= 10^5\n- Memory limit: 256MB\n\nExample:\nInput: []\nOutput: 0",
            "Write the code for **%s** and ensure you handle maximum possible values for the given data types without overflow.\n\nConstraints:\n- 1 <= Input Size <= 10^5\n- Memory limit: 256MB\n\nExample:\nInput: []\nOutput: 0"
        };

        for (int i = 21; i <= 3000; i++) {
            String category;
            String title;
            String diff = (i % 3 == 0) ? "HARD" : ((i % 2 == 0) ? "MEDIUM" : "EASY");
            
            int catIdx = (i % 5);
            if (catIdx == 0) { category = "Arrays"; title = arrays[i % arrays.length]; }
            else if (catIdx == 1) { category = "Strings"; title = strings[i % strings.length]; }
            else if (catIdx == 2) { category = "DSA"; title = dsa[i % dsa.length]; }
            else if (catIdx == 3) { category = "Math"; title = math[i % math.length]; }
            else { category = "DP"; title = dp[i % dp.length]; }

            title = title + " Vol. " + (i / 50 + 1);
            String desc = String.format(templates[i % templates.length], title);

            String clsName = "Solution" + i;
            String starterCode = "public class " + clsName + " {\n    public static void main(String[] args) {\n        // Solve: " + title + "\n        System.out.println(\"0\");\n    }\n}";
            
            problems.add(new Problem(
                i, title, desc, clsName, starterCode, diff, category,
                "Hint for Vol problem", Arrays.asList(new Problem.TestCase("", "0")),
                Arrays.asList(new Problem.TestCase("", "0"))
            ));
        }
    }

    public static List<Problem> getAllProblems() {
        return problems;
    }
}
