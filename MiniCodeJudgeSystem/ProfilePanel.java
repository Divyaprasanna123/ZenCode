package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ProfilePanel extends JPanel {
    private String username;
    private JFrame parentFrame;
    private JLabel streakLabel, solvedLabel, levelLabel;
    private JProgressBar xpBar;

    public ProfilePanel(String username, JFrame parentFrame) {
        this.username = username;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setOpaque(true);
        setBorder(new EmptyBorder(50, 100, 50, 100));

        JPanel glassCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(new Color(255, 255, 255, 20));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
                g2.dispose();
            }
        };
        glassCard.setLayout(new BoxLayout(glassCard, BoxLayout.Y_AXIS));
        glassCard.setBorder(new EmptyBorder(40, 50, 40, 50));
        glassCard.setOpaque(false);

        JLabel title = new JLabel("Developer Profile");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        levelLabel = new JLabel("Level 1 Explorer");
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        levelLabel.setForeground(new Color(44, 187, 93));
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        xpBar = new JProgressBar(0, 100);
        xpBar.setValue(35);
        xpBar.setMaximumSize(new Dimension(400, 12));
        xpBar.setForeground(new Color(44, 187, 93));
        xpBar.setBackground(new Color(255, 255, 255, 20));
        xpBar.setBorderPainted(false);
        xpBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        streakLabel = new JLabel("🔥 3 Day Hot Streak");
        streakLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        streakLabel.setForeground(new Color(255, 161, 22));
        streakLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        solvedLabel = new JLabel("Problems Solved: 0");
        solvedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        solvedLabel.setForeground(Color.WHITE);
        solvedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logout = new JButton("Logout and Exit") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(200, 50, 50, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        logout.setMaximumSize(new Dimension(250, 45));
        logout.setForeground(Color.WHITE);
        logout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logout.setContentAreaFilled(false);
        logout.setBorderPainted(false);
        logout.setFocusPainted(false);
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);
        logout.addActionListener(e -> {
            parentFrame.dispose();
            new Auth().show();
        });

        glassCard.add(title);
        glassCard.add(Box.createRigidArea(new Dimension(0, 10)));
        glassCard.add(levelLabel);
        glassCard.add(Box.createRigidArea(new Dimension(0, 20)));
        glassCard.add(xpBar);
        glassCard.add(Box.createRigidArea(new Dimension(0, 40)));
        glassCard.add(streakLabel);
        glassCard.add(Box.createRigidArea(new Dimension(0, 20)));
        glassCard.add(solvedLabel);
        glassCard.add(Box.createRigidArea(new Dimension(0, 60)));
        glassCard.add(logout);

        add(glassCard, BorderLayout.NORTH);
        refreshData();
    }

    public void refreshData() {
        int streak = UserManager.getStreak(username);
        List<Integer> solved = UserManager.getSolvedProblems(username);
        streakLabel.setText("🔥 " + streak + " Day Streak");
        solvedLabel.setText("System Mastered: " + solved.size() + " challenges");
    }
}
