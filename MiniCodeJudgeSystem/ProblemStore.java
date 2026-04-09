package MiniCodeJudgeSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProblemStore {
    private static List<Problem> problems = new ArrayList<>();

    static {
        // Initial set of real problems (Top 10)
        problems.add(new Problem(1, "Two Sum", "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.", "Solution", "public class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        \n    }\n}", "EASY", "Arrays", 
            Arrays.asList(new Problem.TestCase("[2,7,11,15]\n9", "[0,1]")), 
            Arrays.asList(new Problem.TestCase("[3,2,4]\n6", "[1,2]"))));
            
        problems.add(new Problem(2, "Palindrome Number", "Given an integer x, return true if x is a palindrome, and false otherwise.", "Solution", "public class Solution {\n    public boolean isPalindrome(int x) {\n        \n    }\n}", "EASY", "Math", 
            Arrays.asList(new Problem.TestCase("121", "true")), 
            Arrays.asList(new Problem.TestCase("-121", "false"))));

        problems.add(new Problem(3, "Roman to Integer", "Roman numerals are represented by seven different symbols: I, V, X, L, C, D and M.", "Solution", "public class Solution {\n    public int romanToInt(String s) {\n        \n    }\n}", "EASY", "Strings", 
            Arrays.asList(new Problem.TestCase("III", "3")), 
            Arrays.asList(new Problem.TestCase("LVIII", "58"))));

        problems.add(new Problem(4, "Longest Common Prefix", "Write a function to find the longest common prefix string amongst an array of strings.", "Solution", "public class Solution {\n    public String longestCommonPrefix(String[] strs) {\n        \n    }\n}", "EASY", "Strings", 
            Arrays.asList(new Problem.TestCase("[\"flower\",\"flow\",\"flight\"]", "fl")), 
            Arrays.asList(new Problem.TestCase("[\"dog\",\"racecar\",\"car\"]", ""))));

        problems.add(new Problem(5, "Valid Parentheses", "Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.", "Solution", "public class Solution {\n    public boolean isValid(String s) {\n        \n    }\n}", "EASY", "DSA", 
            Arrays.asList(new Problem.TestCase("()", "true")), 
            Arrays.asList(new Problem.TestCase("()[]{}", "true"))));

        problems.add(new Problem(6, "Merge Two Sorted Lists", "You are given the heads of two sorted linked lists list1 and list2. Merge the two lists in a one sorted list.", "Solution", "public class Solution {\n    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {\n        \n    }\n}", "EASY", "DSA", 
            Arrays.asList(new Problem.TestCase("[1,2,4]\n[1,3,4]", "[1,1,2,3,4,4]")), 
            Arrays.asList(new Problem.TestCase("[]\n[]", "[]"))));

        problems.add(new Problem(7, "Remove Duplicates", "Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place.", "Solution", "public class Solution {\n    public int removeDuplicates(int[] nums) {\n        \n    }\n}", "EASY", "Arrays", 
            Arrays.asList(new Problem.TestCase("[1,1,2]", "2")), 
            Arrays.asList(new Problem.TestCase("[0,0,1,1,1,2,2,3,3,4]", "5"))));

        problems.add(new Problem(8, "Search Insert Position", "Given a sorted array of distinct integers and a target value, return the index if the target is found.", "Solution", "public class Solution {\n    public int searchInsert(int[] nums, int target) {\n        \n    }\n}", "EASY", "Arrays", 
            Arrays.asList(new Problem.TestCase("[1,3,5,6]\n5", "2")), 
            Arrays.asList(new Problem.TestCase("[1,3,5,6]\n2", "1"))));

        problems.add(new Problem(9, "Climbing Stairs", "You are climbing a staircase. It takes n steps to reach the top. Each time you can either climb 1 or 2 steps.", "Solution", "public class Solution {\n    public int climbStairs(int n) {\n        \n    }\n}", "EASY", "DP", 
            Arrays.asList(new Problem.TestCase("2", "2")), 
            Arrays.asList(new Problem.TestCase("3", "3"))));

        problems.add(new Problem(10, "Binary Tree Inorder Traversal", "Given the root of a binary tree, return the inorder traversal of its nodes' values.", "Solution", "public class Solution {\n    public List<Integer> inorderTraversal(TreeNode root) {\n        \n    }\n}", "EASY", "DSA", 
            Arrays.asList(new Problem.TestCase("[1,null,2,3]", "[1,3,2]")), 
            Arrays.asList(new Problem.TestCase("[]", "[]"))));

        generateAIBasedProblems();
    }

    private static void generateAIBasedProblems() {
        String[] arrays = {"Maximum Subarray Sum", "Reverse Array in Place", "Find Duplicate Number", "Median of Two Arrays", "Rotate Image 90 Deg", "Kth Largest Element", "Merge Intervals", "Subarray Sum Equals K", "Next Permutation", "Container With Most Water"};
        String[] strings = {"Valid Anagram", "Group Anagrams", "Longest Palindromic Substring", "Edit Distance", "Wildcard Matching", "Check Subsequence", "Reverse Words in String", "Multiply Strings", "Decode String", "Longest Substring Without Repeating"};
        String[] dsa = {"Implement Queue using Stacks", "Detect Cycle in Linked List", "Balanced Binary Tree Check", "LRU Cache Implementation", "Trie (Prefix Tree) Insert/Search", "Level Order Traversal", "Invert Binary Tree", "Validate BST", "Diameter of Binary Tree", "Min Stack"};
        String[] math = {"Sieve of Eratosthenes", "GCD of Two Numbers", "Power of Three Check", "Happy Number", "Factorial Trailing Zeroes", "Excel Sheet Column Title", "Valid Perfect Square", "Count Primes", "Self Dividing Numbers", "Base 7 Conversion"};
        String[] dp = {"House Robber", "Coin Change", "Longest Increasing Subsequence", "Unique Paths", "Maximum Path Sum", "Partition Equal Subset Sum", "Regular Expression Matching", "Word Break", "Target Sum", "Jump Game"};

        String[] templates = {
            "### Problem Statement\nImplement an efficient algorithm for **%s** that handles large inputs and edge cases. Your solution should focus on optimal performance and clean code architecture.\n\n### Logic Requirements\n- Handle null or empty inputs gracefully.\n- Ensure the time complexity does not exceed O(n log n).\n- Provide clear variable names.",
            "### Challenge Overview\nSolve the **%s** problem using optimal time and space complexity. This challenge tests your ability to manipulate data structures efficiently under memory constraints.\n\n### Constraints\n- 1 <= Input Size <= 10^5\n- Memory limit: 256MB",
            "### Task\nComplete the function for **%s** ensuring all edge cases (negative numbers, zero, large integers) are covered. Focus on robustness and correct mathematical logic.",
            "### Algorithmic Goal\nFind a solution for **%s** that works in O(n) or O(log n) time. This is a classic coding interview problem that requires a deep understanding of standard algorithmic patterns.",
            "### Implementation Details\nWrite the code for **%s** and explain your approach in comments. Ensure you handle maximum possible values for the given data types without overflow."
        };

        for (int i = 11; i <= 5000; i++) {
            String category;
            String title;
            String diff = (i % 3 == 0) ? "HARD" : ((i % 2 == 0) ? "MEDIUM" : "EASY");
            
            int catIdx = (i % 5);
            if (catIdx == 0) { category = "Arrays"; title = arrays[i % arrays.length]; }
            else if (catIdx == 1) { category = "Strings"; title = strings[i % strings.length]; }
            else if (catIdx == 2) { category = "DSA"; title = dsa[i % dsa.length]; }
            else if (catIdx == 3) { category = "Math"; title = math[i % math.length]; }
            else { category = "DP"; title = dp[i % dp.length]; }

            title = title + " Vol. " + (i / 50);
            String desc = String.format(templates[i % templates.length], title);
            
            // Append Sample Test Cases to Description
            desc += "\n\n### Sample Test Cases\n**Input:** \"\"\n**Output:** \"0\"";

            String clsName = "Solution" + i;
            String starterCode = "public class " + clsName + " {\n    public static void main(String[] args) {\n        // Solve: " + title + "\n        System.out.println(\"0\");\n    }\n}";
            
            problems.add(new Problem(
                i, title, desc, clsName, starterCode, diff, category,
                Arrays.asList(new Problem.TestCase("", "0")),
                Arrays.asList(new Problem.TestCase("", "0"))
            ));
        }
    }

    public static List<Problem> getAllProblems() {
        return problems;
    }
}
