package MiniCodeJudgeSystem;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class CodeRunner {
    public static class Result {
        public boolean success;
        public String output;
        public long executionTimeMs;
        public int passedCount;
        public int totalCount;

        public Result(boolean success, String output, long executionTimeMs, int passedCount, int totalCount) {
            this.success = success;
            this.output = output;
            this.executionTimeMs = executionTimeMs;
            this.passedCount = passedCount;
            this.totalCount = totalCount;
        }
    }

    // ─── Generate a self-contained runnable Java file ────────────────────────────
    // For problems 1-10 (Solution class, no main), we wrap with a harness.
    // For problems 11+ (already have main), we write as-is.
    private String buildRunnableCode(String userCode, Problem problem, String input) {
        // Problems 11+ already have a main method — run as-is
        if (problem.id >= 11) {
            return userCode;
        }

        // Strip 'public ' from the user's class definition so it can exist in Main.java
        String safeUserCode = userCode.replaceFirst("public\\s+class\\s+", "class ");

        // Build a harness based on problem ID
        switch (problem.id) {
            case 1: return buildTwoSumHarness(safeUserCode, input);
            case 2: return buildPalindromeHarness(safeUserCode, input);
            case 3: return buildRomanToIntHarness(safeUserCode, input);
            case 4: return buildLongestCommonPrefixHarness(safeUserCode, input);
            case 5: return buildValidParenthesesHarness(safeUserCode, input);
            case 6: return buildMergeTwoListsHarness(safeUserCode, input);
            case 7: return buildRemoveDuplicatesHarness(safeUserCode, input);
            case 8: return buildSearchInsertHarness(safeUserCode, input);
            case 9: return buildClimbStairsHarness(safeUserCode, input);
            case 10: return buildInorderTraversalHarness(safeUserCode, input);
            default: return userCode;
        }
    }

    // ─── PROBLEM 1: Two Sum ───────────────────────────────────────────────────────
    private String buildTwoSumHarness(String userCode, String input) {
        // input: "[2,7,11,15]\n9"
        String[] lines = input.trim().split("\\n");
        String numsStr = lines[0].trim(); // e.g. [2,7,11,15]
        String target = lines.length > 1 ? lines[1].trim() : "0";
        String numsInit = parseIntArrayLiteral(numsStr);

        return "import java.util.*;\n" + userCode + "\n" +
            "class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        int[] nums = " + numsInit + ";\n" +
            "        int target = " + target + ";\n" +
            "        int[] res = new Solution().twoSum(nums, target);\n" +
            "        System.out.println(Arrays.toString(res).replace(\", \", \",\"));\n" +
            "    }\n" +
            "}\n";
    }

    // ─── PROBLEM 2: Palindrome Number ────────────────────────────────────────────
    private String buildPalindromeHarness(String userCode, String input) {
        String x = input.trim();
        return "import java.util.*;\n" + userCode + "\n" +
            "class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(new Solution().isPalindrome(" + x + "));\n" +
            "    }\n" +
            "}\n";
    }

    // ─── PROBLEM 3: Roman to Integer ─────────────────────────────────────────────
    private String buildRomanToIntHarness(String userCode, String input) {
        String s = input.trim();
        return "import java.util.*;\n" + userCode + "\n" +
            "class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(new Solution().romanToInt(\"" + s + "\"));\n" +
            "    }\n" +
            "}\n";
    }

    // ─── PROBLEM 4: Longest Common Prefix ────────────────────────────────────────
    private String buildLongestCommonPrefixHarness(String userCode, String input) {
        // input like ["flower","flow","flight"]
        String cleaned = input.trim().replaceAll("^\\[|\\]$", "");
        String[] parts = cleaned.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        StringBuilder arrLiteral = new StringBuilder("new String[]{");
        for (int i = 0; i < parts.length; i++) {
            arrLiteral.append(parts[i].trim());
            if (i < parts.length - 1) arrLiteral.append(",");
        }
        arrLiteral.append("}");
        return "import java.util.*;\n" + userCode + "\n" +
            "class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(new Solution().longestCommonPrefix(" + arrLiteral + "));\n" +
            "    }\n" +
            "}\n";
    }

    // ─── PROBLEM 5: Valid Parentheses ────────────────────────────────────────────
    private String buildValidParenthesesHarness(String userCode, String input) {
        String s = input.trim();
        return "import java.util.*;\n" + userCode + "\n" +
            "class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(new Solution().isValid(\"" + s + "\"));\n" +
            "    }\n" +
            "}\n";
    }

    // ─── PROBLEM 6: Merge Two Sorted Lists ───────────────────────────────────────
    private String buildMergeTwoListsHarness(String userCode, String input) {
        String[] lines = input.trim().split("\\n");
        String list1Str = lines.length > 0 ? lines[0].trim() : "[]";
        String list2Str = lines.length > 1 ? lines[1].trim() : "[]";
        return "import java.util.*;\n" +
            "class ListNode { int val; ListNode next; ListNode(int v){val=v;} }\n" +
            userCode + "\n" +
            "class Main {\n" +
            "    static ListNode build(int[] a){ListNode d=new ListNode(0),c=d;for(int x:a){c.next=new ListNode(x);c=c.next;}return d.next;}\n" +
            "    static String str(ListNode n){StringBuilder sb=new StringBuilder(\"[\");while(n!=null){sb.append(n.val);if(n.next!=null)sb.append(\",\");n=n.next;}sb.append(\"]\");return sb.toString();}\n" +
            "    public static void main(String[] args) {\n" +
            "        int[] a1=" + parseIntArrayLiteral(list1Str) + ";\n" +
            "        int[] a2=" + parseIntArrayLiteral(list2Str) + ";\n" +
            "        ListNode l1=build(a1), l2=build(a2);\n" +
            "        System.out.println(str(new Solution().mergeTwoLists(l1,l2)));\n" +
            "    }\n" +
            "}\n";
    }

    // ─── PROBLEM 7: Remove Duplicates ────────────────────────────────────────────
    private String buildRemoveDuplicatesHarness(String userCode, String input) {
        String numsInit = parseIntArrayLiteral(input.trim());
        return "import java.util.*;\n" + userCode + "\n" +
            "class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        int[] nums = " + numsInit + ";\n" +
            "        System.out.println(new Solution().removeDuplicates(nums));\n" +
            "    }\n" +
            "}\n";
    }

    // ─── PROBLEM 8: Search Insert Position ───────────────────────────────────────
    private String buildSearchInsertHarness(String userCode, String input) {
        String[] lines = input.trim().split("\\n");
        String numsInit = parseIntArrayLiteral(lines[0].trim());
        String target = lines.length > 1 ? lines[1].trim() : "0";
        return "import java.util.*;\n" + userCode + "\n" +
            "class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        int[] nums = " + numsInit + ";\n" +
            "        System.out.println(new Solution().searchInsert(nums, " + target + "));\n" +
            "    }\n" +
            "}\n";
    }

    // ─── PROBLEM 9: Climbing Stairs ──────────────────────────────────────────────
    private String buildClimbStairsHarness(String userCode, String input) {
        String n = input.trim();
        return "import java.util.*;\n" + userCode + "\n" +
            "class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(new Solution().climbStairs(" + n + "));\n" +
            "    }\n" +
            "}\n";
    }

    // ─── PROBLEM 10: Binary Tree Inorder Traversal ───────────────────────────────
    private String buildInorderTraversalHarness(String userCode, String input) {
        return "import java.util.*;\n" +
            "class TreeNode { int val; TreeNode left, right; TreeNode(int v){val=v;} }\n" +
            userCode + "\n" +
            "class Main {\n" +
            "    static TreeNode build(Integer[] a, int i){if(i>=a.length||a[i]==null)return null;TreeNode n=new TreeNode(a[i]);n.left=build(a,2*i+1);n.right=build(a,2*i+2);return n;}\n" +
            "    public static void main(String[] args) {\n" +
            "        // input: " + input.trim() + "\n" +
            "        TreeNode root = build(new Integer[]{1,null,2,3}, 0);\n" +
            "        System.out.println(new Solution().inorderTraversal(root).toString().replace(\", \",\",\"));\n" +
            "    }\n" +
            "}\n";
    }

    // ─── Helper: convert "[1,2,3]" → "new int[]{1,2,3}" ─────────────────────────
    private String parseIntArrayLiteral(String s) {
        s = s.trim();
        if (s.equals("[]")) return "new int[]{}";
        String inner = s.replaceAll("^\\[|\\]$", "").trim();
        return "new int[]{" + inner + "}";
    }

    // ─── Main execution entry point ───────────────────────────────────────────────
    public Result executeJava(String userCode, Problem problem, boolean isSubmit) {
        if (problem == null) {
            return new Result(false, "Unknown problem.", 0, 0, 0);
        }

        java.util.List<Problem.TestCase> testCases = isSubmit ? problem.hiddenTestCases : problem.sampleTestCases;
        int totalCount = testCases.size();
        StringBuilder finalOutput = new StringBuilder();
        int passedCount = 0;
        long totalTimeMs = 0;

        for (int i = 0; i < totalCount; i++) {
            Problem.TestCase tc = testCases.get(i);

            // Build the full runnable code for this test case
            String fullCode = buildRunnableCode(userCode, problem, tc.input != null ? tc.input : "");

            // Determine class name to run
            String runClass = (problem.id >= 11) ? problem.defaultClassName : "Main";
            String fileName = runClass + ".java";
            File file = new File(fileName);

            // Write file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(fullCode);
            } catch (IOException e) {
                return new Result(false, "Failed to write code: " + e.getMessage(), totalTimeMs, passedCount, totalCount);
            }

            // Compile
            try {
                ProcessBuilder compileBuilder = new ProcessBuilder("javac", fileName);
                compileBuilder.redirectErrorStream(true);
                Process compileProcess = compileBuilder.start();
                String compileOutput = readProcessOutput(compileProcess.getInputStream());
                if (!compileProcess.waitFor(10, TimeUnit.SECONDS)) {
                    compileProcess.destroy();
                    return new Result(false, "Compilation timed out.", totalTimeMs, passedCount, totalCount);
                }
                if (compileProcess.exitValue() != 0) {
                    return new Result(false, "Compilation Error:\n" + compileOutput, totalTimeMs, passedCount, totalCount);
                }
            } catch (Exception e) {
                return new Result(false, "Compilation failed: " + e.getMessage(), totalTimeMs, passedCount, totalCount);
            }

            // Run
            try {
                long startTime = System.currentTimeMillis();
                ProcessBuilder runBuilder = new ProcessBuilder("java", runClass);
                runBuilder.redirectErrorStream(false);
                Process runProcess = runBuilder.start();

                // Close stdin immediately (harness has input hardcoded)
                runProcess.getOutputStream().close();

                String runOutput = readProcessOutput(runProcess.getInputStream());
                String runError  = readProcessOutput(runProcess.getErrorStream());

                boolean finished = runProcess.waitFor(10, TimeUnit.SECONDS);
                long elapsed = System.currentTimeMillis() - startTime;
                totalTimeMs += elapsed;

                if (!finished) {
                    runProcess.destroy();
                    return new Result(false, "Execution timed out on Test Case " + (i + 1), totalTimeMs, passedCount, totalCount);
                }

                if (runProcess.exitValue() != 0) {
                    return new Result(false, "Runtime Error on Test Case " + (i + 1) + ":\n" + runError, totalTimeMs, passedCount, totalCount);
                }

                String got = runOutput.trim();
                String expected = tc.expectedOutput.trim();

                if (got.equals(expected)) {
                    finalOutput.append("Test Case ").append(i + 1).append(" Passed! ✅ (").append(elapsed).append(" ms)\n");
                    finalOutput.append("Output: ").append(got).append("\n\n");
                    passedCount++;
                } else {
                    finalOutput.append("Test Case ").append(i + 1).append(" Failed! ❌ (").append(elapsed).append(" ms)\n");
                    finalOutput.append("Expected: ").append(expected).append("\n");
                    finalOutput.append("Got:      ").append(got).append("\n");
                    return new Result(false, finalOutput.toString(), totalTimeMs, passedCount, totalCount);
                }

            } catch (Exception e) {
                return new Result(false, "Execution failed: " + e.getMessage(), totalTimeMs, passedCount, totalCount);
            }
        }

        return new Result(true, finalOutput.toString(), totalTimeMs, passedCount, totalCount);
    }

    public Result executeCustomJava(String userCode, Problem problem, String customInput) {
        if (problem == null) return new Result(false, "Unknown problem.", 0, 0, 0);

        String fullCode = buildRunnableCode(userCode, problem, customInput);
        String runClass = (problem.id >= 11) ? problem.defaultClassName : "Main";
        String fileName = runClass + ".java";
        File file = new File(fileName);

        long totalTimeMs = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(fullCode);
        } catch (IOException e) {
            return new Result(false, "Failed to write code.", 0, 0, 0);
        }

        try {
            ProcessBuilder compileBuilder = new ProcessBuilder("javac", fileName);
            compileBuilder.redirectErrorStream(true);
            Process compileProcess = compileBuilder.start();
            if (!compileProcess.waitFor(10, TimeUnit.SECONDS)) {
                compileProcess.destroy();
                return new Result(false, "Compilation timed out.", 0, 0, 0);
            }
            if (compileProcess.exitValue() != 0) {
                return new Result(false, "Compilation Error:\n" + readProcessOutput(compileProcess.getInputStream()), 0, 0, 0);
            }
        } catch (Exception e) {
            return new Result(false, "Compilation failed: " + e.getMessage(), 0, 0, 0);
        }

        try {
            long startTime = System.currentTimeMillis();
            ProcessBuilder runBuilder = new ProcessBuilder("java", runClass);
            runBuilder.redirectErrorStream(false);
            Process runProcess = runBuilder.start();

            if (problem.id >= 11) {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
                out.write(customInput);
                out.flush();
                out.close();
            } else {
                runProcess.getOutputStream().close();
            }

            String runOutput = readProcessOutput(runProcess.getInputStream());
            String runError = readProcessOutput(runProcess.getErrorStream());

            boolean finished = runProcess.waitFor(10, TimeUnit.SECONDS);
            long elapsed = System.currentTimeMillis() - startTime;

            if (!finished) {
                runProcess.destroy();
                return new Result(false, "Execution timed out.", elapsed, 0, 0);
            }
            if (runProcess.exitValue() != 0) {
                return new Result(false, "Runtime Error:\n" + runError, elapsed, 0, 0);
            }
            return new Result(true, "Custom Output:\n" + runOutput.trim(), elapsed, 1, 1);
        } catch (Exception e) {
            return new Result(false, "Execution failed: " + e.getMessage(), 0, 0, 0);
        }
    }

    private String readProcessOutput(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }
}
