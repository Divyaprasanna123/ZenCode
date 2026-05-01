package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    private DefaultTableModel tableModel;

    public LeaderboardPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setOpaque(true);
        setBorder(new EmptyBorder(40, 50, 40, 50));

        JPanel headerPanel = createGlassPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("\ud83c\udfc6 Global Leaderboard");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 32));
        title.setForeground(new Color(230, 160, 50)); // Gold
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Rankings based on Experience (XP), Solved Problems, and Streaks");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 200));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(title);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(subtitle);

        add(headerPanel, BorderLayout.NORTH);
        add(Box.createVerticalStrut(30), BorderLayout.CENTER);

        // Leaderboard Table
        String[] cols = {"Rank", "User", "Level", "XP", "Solved", "Streak", "Battles (W-L)"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        JTable table = new JTable(tableModel);
        table.setBackground(new Color(25, 25, 25));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(40, 40, 40));
        table.getTableHeader().setBackground(new Color(35, 35, 35));
        table.getTableHeader().setForeground(new Color(230, 160, 50));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        table.setRowHeight(40);
        table.setShowVerticalLines(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(20, 20, 20));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 40)));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);
        tableContainer.setBorder(new EmptyBorder(30, 0, 0, 0));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<UserManager.UserStats> statsList = UserManager.getAllUsersStats();
        
        int rank = 1;
        for (UserManager.UserStats s : statsList) {
            String rankStr = String.valueOf(rank);
            if (rank == 1) rankStr = "\ud83e\udd47 " + rank; // Gold medal
            else if (rank == 2) rankStr = "\ud83e\udd48 " + rank; // Silver medal
            else if (rank == 3) rankStr = "\ud83e\udd49 " + rank; // Bronze medal
            
            int level = UserManager.calculateLevel(s.experience);
            String battleStats = s.battleWins + "W - " + s.battleLosses + "L";
            String streakStr = "\ud83d\udd25 " + s.streak;

            tableModel.addRow(new Object[]{rankStr, s.username, level, s.experience, s.solvedCount, streakStr, battleStats});
            rank++;
        }
    }

    private JPanel createGlassPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
    }
}
