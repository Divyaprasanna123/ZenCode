package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    private JPanel leaderboardContent;
    private String username;

    public LeaderboardPanel(String username) {
        this.username = username;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("Global Ranking (by XP)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        leaderboardContent = new JPanel();
        leaderboardContent.setLayout(new BoxLayout(leaderboardContent, BoxLayout.Y_AXIS));
        leaderboardContent.setBackground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(leaderboardContent);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        leaderboardContent.removeAll();
        List<UserManager.UserStats> users = UserManager.getAllUsersStats();
        int rank = 1;
        for (UserManager.UserStats u : users) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(u.username.equals(username) ? new Color(0, 255, 200, 40) : new Color(25, 25, 25));
            row.setBorder(new EmptyBorder(15, 25, 15, 25));
            row.setMaximumSize(new Dimension(800, 60));

            JLabel rankLabel = new JLabel("#" + rank);
            rankLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            rankLabel.setForeground(rank <= 3 ? new Color(255, 215, 0) : Color.WHITE); // Gold for top 3

            JLabel userLabel = new JLabel(u.username);
            userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            userLabel.setForeground(Color.WHITE);

            JPanel statsGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
            statsGroup.setOpaque(false);

            JLabel xpLabel = new JLabel(u.experience + " XP");
            xpLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            xpLabel.setForeground(new Color(0, 255, 200));

            JLabel solvedLabel = new JLabel("Solved: " + u.solvedCount);
            solvedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            solvedLabel.setForeground(new Color(200, 200, 200));

            statsGroup.add(xpLabel);
            statsGroup.add(solvedLabel);

            JPanel leftGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
            leftGroup.setOpaque(false);
            leftGroup.add(rankLabel);
            leftGroup.add(userLabel);

            row.add(leftGroup, BorderLayout.WEST);
            row.add(statsGroup, BorderLayout.EAST);

            leaderboardContent.add(row);
            leaderboardContent.add(Box.createRigidArea(new Dimension(0, 10)));
            rank++;
        }
        leaderboardContent.revalidate();
        leaderboardContent.repaint();
    }
}
