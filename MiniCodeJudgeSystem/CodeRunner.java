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

    public Result executeJava(String code, Problem problem, boolean isSubmit) {
        if (problem == null) {
            return new Result(false, "Unknown problem.", 0, 0, 0);
        }
        
        String className = problem.defaultClassName;
        String fileName = className + ".java";
        File file = new File(fileName);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(code);
        } catch (IOException e) {
            return new Result(false, "Failed to write code to file: " + e.getMessage(), 0, 0, 0);
        }

        // Compile
        try {
            ProcessBuilder compileBuilder = new ProcessBuilder("javac", fileName);
            compileBuilder.redirectErrorStream(true);
            Process compileProcess = compileBuilder.start();
            
            String compileOutput = readProcessOutput(compileProcess.getInputStream());
            if (!compileProcess.waitFor(5, TimeUnit.SECONDS)) {
                compileProcess.destroy();
                return new Result(false, "Compilation timed out.", 0, 0, 0);
            }
            
            if (compileProcess.exitValue() != 0) {
                return new Result(false, "Compilation Error:\n" + compileOutput, 0, 0, 0);
            }
        } catch (Exception e) {
            return new Result(false, "Compilation execution failed: " + e.getMessage(), 0, 0, 0);
        }

        // Run Test Cases
        StringBuilder finalOutput = new StringBuilder();
        int passedCount = 0;
        long totalTimeMs = 0;
        
        java.util.List<Problem.TestCase> testCases = isSubmit ? problem.hiddenTestCases : problem.sampleTestCases;
        int totalCount = testCases.size();

        for (int i = 0; i < totalCount; i++) {
            Problem.TestCase tc = testCases.get(i);
            try {
                long startTime = System.currentTimeMillis();
                
                ProcessBuilder runBuilder = new ProcessBuilder("java", className);
                Process runProcess = runBuilder.start();
                
                if (tc.input != null && !tc.input.isEmpty()) {
                    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
                        writer.write(tc.input);
                        writer.newLine(); // simulate enter
                    }
                }
                
                String runOutput = readProcessOutput(runProcess.getInputStream());
                String runError = readProcessOutput(runProcess.getErrorStream());
                
                boolean finished = runProcess.waitFor(5, TimeUnit.SECONDS);
                long endTime = System.currentTimeMillis();
                long currentRunTime = endTime - startTime;
                totalTimeMs += currentRunTime;

                if (!finished) {
                    runProcess.destroy();
                    return new Result(false, "Execution timed out on Test Case " + (i+1), totalTimeMs, passedCount, totalCount);
                }

                if (runProcess.exitValue() != 0) {
                    return new Result(false, "Runtime Error on Test Case " + (i+1) + ":\n" + runError, totalTimeMs, passedCount, totalCount);
                }

                if (runOutput.trim().equals(tc.expectedOutput.trim())) {
                    finalOutput.append("Test Case ").append(i+1).append(" Passed! \u2705 (" + currentRunTime + " ms)\n");
                    finalOutput.append("Output: ").append(runOutput.trim()).append("\n\n");
                    passedCount++;
                } else {
                    finalOutput.append("Test Case ").append(i+1).append(" Failed! \u274C (" + currentRunTime + " ms)\n");
                    finalOutput.append("Expected: ").append(tc.expectedOutput.trim()).append("\n");
                    finalOutput.append("Got:      ").append(runOutput.trim()).append("\n");
                    return new Result(false, finalOutput.toString(), totalTimeMs, passedCount, totalCount);
                }

            } catch (Exception e) {
                return new Result(false, "Execution failed: " + e.getMessage(), totalTimeMs, passedCount, totalCount);
            }
        }
        
        return new Result(true, finalOutput.toString(), totalTimeMs, passedCount, totalCount);
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
