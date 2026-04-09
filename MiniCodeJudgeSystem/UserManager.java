package MiniCodeJudgeSystem;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserManager {

    public static List<Integer> getSolvedProblems(String username) {
        File file = new File("users.txt");
        if (!file.exists()) return new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", -1); 
                if (parts.length >= 3 && parts[0].equals(username)) {
                    if (parts[2].isEmpty()) return new ArrayList<>();
                    String[] ids = parts[2].split(",");
                    List<Integer> list = new ArrayList<>();
                    for (String id : ids) {
                        try { list.add(Integer.parseInt(id)); } catch(Exception ignore){}
                    }
                    return list;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    public static void addSolvedProblem(String username, int problemId, String difficulty) {
        File file = new File("users.txt");
        if (!file.exists()) return;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", -1);
                if (parts.length >= 3 && parts[0].equals(username)) {
                    List<String> ids = new ArrayList<>();
                    if (!parts[2].trim().isEmpty()) {
                        ids.addAll(Arrays.asList(parts[2].split(",")));
                    }
                    if (!ids.contains(String.valueOf(problemId))) {
                        ids.add(String.valueOf(problemId));
                        // Add XP based on difficulty
                        int xpToAdd = difficulty.equals("EASY") ? 50 : (difficulty.equals("MEDIUM") ? 150 : 500);
                        int currentXP = 0;
                        if (parts.length >= 4) {
                            try { currentXP = Integer.parseInt(parts[3]); } catch (Exception ignore) {}
                        }
                        int newXP = currentXP + xpToAdd;
                        
                        // Reconstruct line with XP
                        line = parts[0] + ":" + parts[1] + ":" + String.join(",", ids) + ":" + newXP;
                    } else {
                        // Just reconstruct
                        String xpPart = parts.length >= 4 ? parts[3] : "0";
                        line = parts[0] + ":" + parts[1] + ":" + parts[2] + ":" + xpPart;
                    }
                }
                sb.append(line).append(System.lineSeparator());
            }
        } catch (Exception e) { e.printStackTrace(); }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(sb.toString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static int getExperience(String username) {
        File file = new File("users.txt");
        if (!file.exists()) return 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", -1);
                if (parts.length >= 4 && parts[0].equals(username)) {
                    return Integer.parseInt(parts[3]);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public static int calculateLevel(int xp) {
        return (xp / 1000) + 1;
    }

    public static int getStreak(String username) {
        File file = new File("streak.txt");
        if (!file.exists()) return 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", -1);
                if (parts.length >= 3 && parts[0].equals(username)) {
                    return Integer.parseInt(parts[1]);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public static void updateStreak(String username) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File file = new File("streak.txt");
        
        StringBuilder fileContent = new StringBuilder();
        boolean foundUser = false;
        int currentStreak = 0;
        String lastDate = "";

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":", -1);
                    if (parts.length >= 3 && parts[0].equals(username)) {
                        foundUser = true;
                        currentStreak = Integer.parseInt(parts[1]);
                        lastDate = parts[2];
                    } else {
                        fileContent.append(line).append(System.lineSeparator());
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        if (foundUser) {
            if (!currentDate.equals(lastDate)) {
                try {
                    Date last = new SimpleDateFormat("yyyy-MM-dd").parse(lastDate);
                    Date now = new SimpleDateFormat("yyyy-MM-dd").parse(currentDate);
                    long diff = now.getTime() - last.getTime();
                    long days = diff / (1000 * 60 * 60 * 24);
                    
                    if (days == 1) {
                        currentStreak++;
                    } else if (days > 1) {
                        currentStreak = 1; // broken streak
                    }
                } catch (Exception e) {
                    currentStreak = 1;
                }
            }
        } else {
            currentStreak = 1;
        }

        fileContent.append(username).append(":").append(currentStreak).append(":").append(currentDate).append(System.lineSeparator());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(fileContent.toString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void addSubmission(String username, int problemId, String status) {
        File file = new File("submissions.txt");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(username + ":" + problemId + ":" + status + ":" + date);
            writer.newLine();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static List<String[]> getSubmissions(String username) {
        List<String[]> list = new ArrayList<>();
        File file = new File("submissions.txt");
        if (!file.exists()) return list;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", -1);
                if (parts.length >= 4 && parts[0].equals(username)) {
                    list.add(parts);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        Collections.reverse(list); // Newest first
        return list;
    }

    public static class UserStats implements Comparable<UserStats> {
        public String username;
        public int solvedCount;
        public int streak;
        public int experience;

        public UserStats(String username, int solvedCount, int streak, int experience) {
            this.username = username;
            this.solvedCount = solvedCount;
            this.streak = streak;
            this.experience = experience;
        }

        @Override
        public int compareTo(UserStats other) {
            if (this.experience != other.experience) return other.experience - this.experience;
            if (this.solvedCount != other.solvedCount) return other.solvedCount - this.solvedCount;
            return other.streak - this.streak;
        }
    }

    public static List<UserStats> getAllUsersStats() {
        List<UserStats> stats = new ArrayList<>();
        File file = new File("users.txt");
        if (!file.exists()) return stats;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", -1);
                if (parts.length >= 3) {
                    String user = parts[0];
                    int count = 0;
                    if (!parts[2].trim().isEmpty()) {
                        String[] ids = parts[2].split(",");
                        count = ids.length;
                    }
                    int streak = getStreak(user);
                    int xp = parts.length >= 4 ? Integer.parseInt(parts[3]) : 0;
                    stats.add(new UserStats(user, count, streak, xp));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        Collections.sort(stats);
        return stats;
    }
}
