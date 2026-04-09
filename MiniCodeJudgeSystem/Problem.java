package MiniCodeJudgeSystem;

import java.util.List;

public class Problem {
    public int id;
    public String title;
    public String description;
    public String defaultClassName;
    public String starterCode;
    public String difficulty; // EASY, MEDIUM, HARD
    public String category;   // Arrays, Strings, DSA, Math, DP
    public List<TestCase> sampleTestCases;
    public List<TestCase> hiddenTestCases;

    public static class TestCase {
        public String input;
        public String expectedOutput;

        public TestCase(String input, String expectedOutput) {
            this.input = input;
            this.expectedOutput = expectedOutput;
        }
    }

    public Problem(int id, String title, String description, String defaultClassName,
                   String starterCode, String difficulty, String category,
                   List<TestCase> sampleTestCases, List<TestCase> hiddenTestCases) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.defaultClassName = defaultClassName;
        this.starterCode = starterCode;
        this.difficulty = difficulty;
        this.category = category;
        this.sampleTestCases = sampleTestCases;
        this.hiddenTestCases = hiddenTestCases;
    }
}
