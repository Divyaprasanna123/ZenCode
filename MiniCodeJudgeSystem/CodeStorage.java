package MiniCodeJudgeSystem;

import java.io.*;

public class CodeStorage {
    private static final String STORAGE_DIR = "codes";

    public static void saveCode(String username, int problemId, String code) {
        File dir = new File(STORAGE_DIR + "/" + username);
        if (!dir.exists()) dir.mkdirs();
        
        File file = new File(dir, problemId + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(code);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static String loadCode(String username, int problemId) {
        File file = new File(STORAGE_DIR + "/" + username + "/" + problemId + ".txt");
        if (!file.exists()) return null;
        
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) { e.printStackTrace(); }
        return sb.toString();
    }
}
