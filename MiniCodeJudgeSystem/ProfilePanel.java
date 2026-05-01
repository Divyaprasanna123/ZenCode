package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.List;

public class ProfilePanel extends JPanel {
    private String username;
    private JFrame parentFrame;

    // Left Column components
    private JLabel streakLabel, levelLabel, battleLabel;
    private JProgressBar xpBar;
    
    // Right Column components
    private RingChartPanel ringChart;
    private JLabel easyLabel, medLabel, hardLabel;
    private JProgressBar easyBar, medBar, hardBar;
    private DefaultTableModel testTableModel;

    private Font emojiFont;

    public ProfilePanel(String username, JFrame parentFrame) {
        this.username = username;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 18)); // LeetCode dark background
        setOpaque(true);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Use Segoe UI Emoji for proper rendering of emojis on Windows
        emojiFont = new Font("Segoe UI Emoji", Font.BOLD, 18);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setOpaque(false);
        mainSplit.setBorder(null);
        mainSplit.setDividerSize(20);
        mainSplit.setDividerLocation(300);

        // --- LEFT COLUMN: USER CARD ---
        JPanel leftPanel = createGlassPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel avatarPlaceholder = new JLabel(IconLoader.getIcon("profile"));
        avatarPlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nameLabel = new JLabel(username);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        levelLabel = new JLabel("Level 1");
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        levelLabel.setForeground(new Color(44, 187, 93));
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        xpBar = new JProgressBar(0, 1000);
        xpBar.setStringPainted(true);
        xpBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI());
        xpBar.setForeground(new Color(44, 187, 93));
        xpBar.setBackground(new Color(40, 40, 40));
        xpBar.setBorderPainted(false);
        xpBar.setMaximumSize(new Dimension(200, 15));
        xpBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        streakLabel = new JLabel("\uD83D\uDD25 0 Day Streak"); // 🔥
        streakLabel.setFont(emojiFont);
        streakLabel.setForeground(new Color(255, 161, 22));
        streakLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        battleLabel = new JLabel("\u2694\uFE0F Battles: 0W - 0L"); // ⚔️
        battleLabel.setFont(emojiFont);
        battleLabel.setForeground(new Color(138, 43, 226));
        battleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutBtn = new JButton("Logout") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? new Color(220, 60, 60) : new Color(200, 50, 50);
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(200, 40));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            parentFrame.dispose();
            new Auth().show();
        });

        leftPanel.add(avatarPlaceholder);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(nameLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(levelLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(xpBar);
        leftPanel.add(Box.createVerticalStrut(40));
        leftPanel.add(streakLabel);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(battleLabel);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(logoutBtn);

        // --- RIGHT COLUMN: STATS & HISTORY ---
        JPanel rightPanel = new JPanel(new BorderLayout(0, 20));
        rightPanel.setOpaque(false);

        // Top Section: Solved Stats
        JPanel statsPanel = createGlassPanel();
        statsPanel.setLayout(new BorderLayout());
        statsPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel statsTitle = new JLabel("Solved Problems");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statsTitle.setForeground(new Color(200, 200, 200));
        statsPanel.add(statsTitle, BorderLayout.NORTH);

        JPanel statsContent = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 10));
        statsContent.setOpaque(false);

        ringChart = new RingChartPanel();
        ringChart.setPreferredSize(new Dimension(120, 120));
        statsContent.add(ringChart);

        JPanel barsPanel = new JPanel();
        barsPanel.setLayout(new BoxLayout(barsPanel, BoxLayout.Y_AXIS));
        barsPanel.setOpaque(false);
        barsPanel.setPreferredSize(new Dimension(300, 100));

        easyLabel = new JLabel("Easy  0 / 0");
        easyLabel.setForeground(Color.WHITE);
        easyBar = createDiffBar(new Color(44, 187, 93));

        medLabel = new JLabel("Medium  0 / 0");
        medLabel.setForeground(Color.WHITE);
        medBar = createDiffBar(new Color(230, 160, 50));

        hardLabel = new JLabel("Hard  0 / 0");
        hardLabel.setForeground(Color.WHITE);
        hardBar = createDiffBar(new Color(200, 50, 50));

        barsPanel.add(createBarRow(easyLabel, easyBar));
        barsPanel.add(Box.createVerticalStrut(15));
        barsPanel.add(createBarRow(medLabel, medBar));
        barsPanel.add(Box.createVerticalStrut(15));
        barsPanel.add(createBarRow(hardLabel, hardBar));

        statsContent.add(barsPanel);
        statsPanel.add(statsContent, BorderLayout.CENTER);

        // Bottom Section: Mock Test History
        JPanel historyPanel = createGlassPanel();
        historyPanel.setLayout(new BorderLayout());
        historyPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel historyTitle = new JLabel("Mock Test History");
        historyTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        historyTitle.setForeground(new Color(200, 200, 200));
        historyTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] cols = {"Date", "Topic", "Score", "Percentage"};
        testTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable testTable = new JTable(testTableModel);
        testTable.setBackground(new Color(25, 25, 25));
        testTable.setForeground(Color.WHITE);
        testTable.setGridColor(new Color(50, 50, 50));
        testTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        testTable.setRowHeight(30);
        testTable.setShowVerticalLines(false);
        testTable.setOpaque(true);

        // Custom header renderer so Windows L&F cannot override to white
        testTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                lbl.setBackground(new Color(40, 40, 40));
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(60, 60, 60)));
                return lbl;
            }
        });

        // Body cell renderer: dark background, white text, centred
        DefaultTableCellRenderer bodyRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                lbl.setBackground(isSelected ? new Color(44, 100, 60) : new Color(25, 25, 25));
                lbl.setForeground(Color.WHITE);
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setOpaque(true);
                return lbl;
            }
        };
        for (int i = 0; i < testTable.getColumnCount(); i++) {
            testTable.getColumnModel().getColumn(i).setCellRenderer(bodyRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(testTable);
        scrollPane.getViewport().setBackground(new Color(25, 25, 25));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 40)));

        historyPanel.add(historyTitle, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        rightPanel.add(statsPanel, BorderLayout.NORTH);
        rightPanel.add(historyPanel, BorderLayout.CENTER);

        mainSplit.setLeftComponent(leftPanel);
        mainSplit.setRightComponent(rightPanel);

        add(mainSplit, BorderLayout.CENTER);
        refreshData();
    }

    private JPanel createBarRow(JLabel lbl, JProgressBar bar) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        p.add(lbl, BorderLayout.NORTH);
        p.add(bar, BorderLayout.CENTER);
        return p;
    }

    private JProgressBar createDiffBar(Color c) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setPreferredSize(new Dimension(300, 10));
        bar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI()); // bypasses Windows L&F override
        bar.setForeground(c);
        bar.setBackground(new Color(50, 50, 50));
        bar.setBorderPainted(false);
        bar.setStringPainted(false);
        return bar;
    }

    public void refreshData() {
        int streak = UserManager.getStreak(username);
        int xp = UserManager.getExperience(username);
        int level = UserManager.calculateLevel(xp);
        int[] battles = UserManager.getBattleStats(username);
        
        streakLabel.setText("\uD83D\uDD25 " + streak + " Day Streak");
        levelLabel.setText("Level " + level);
        xpBar.setValue(xp % 1000);
        xpBar.setString((xp % 1000) + " / 1000 XP");
        battleLabel.setText("\u2694\uFE0F Battles: " + battles[0] + "W - " + battles[1] + "L");

        // Calculate Problem Stats
        List<Problem> allProbs = ProblemStore.getAllProblems();
        List<Integer> solvedIds = UserManager.getSolvedProblems(username);

        int totalEasy = 0, totalMed = 0, totalHard = 0;
        int solvedEasy = 0, solvedMed = 0, solvedHard = 0;

        for (Problem p : allProbs) {
            if (p.difficulty.equals("EASY")) {
                totalEasy++;
                if (solvedIds.contains(p.id)) solvedEasy++;
            } else if (p.difficulty.equals("MEDIUM")) {
                totalMed++;
                if (solvedIds.contains(p.id)) solvedMed++;
            } else {
                totalHard++;
                if (solvedIds.contains(p.id)) solvedHard++;
            }
        }

        int totalSolved = solvedEasy + solvedMed + solvedHard;
        int totalProbs = allProbs.size();

        ringChart.setValues(totalSolved, totalProbs);
        
        easyLabel.setText(String.format("Easy       %d / %d", solvedEasy, totalEasy));
        easyBar.setMaximum(totalEasy == 0 ? 1 : totalEasy);
        easyBar.setValue(solvedEasy);

        medLabel.setText(String.format("Medium     %d / %d", solvedMed, totalMed));
        medBar.setMaximum(totalMed == 0 ? 1 : totalMed);
        medBar.setValue(solvedMed);

        hardLabel.setText(String.format("Hard       %d / %d", solvedHard, totalHard));
        hardBar.setMaximum(totalHard == 0 ? 1 : totalHard);
        hardBar.setValue(solvedHard);

        // Load Test History
        testTableModel.setRowCount(0);
        List<String[]> tests = UserManager.getMockTestResults(username);
        for (String[] t : tests) {
            if (t.length >= 5) {
                String topic = t[1];
                int score = Integer.parseInt(t[2]);
                int max = Integer.parseInt(t[3]);
                String date = t[4];
                String percent = (max > 0) ? (score * 100 / max) + "%" : "0%";
                testTableModel.addRow(new Object[]{date, topic, score + " / " + max, percent});
            }
        }
    }

    private JPanel createGlassPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 10)); // Slight white tint
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(255, 255, 255, 20));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2.dispose();
            }
        };
    }

    // Custom Component for LeetCode style Circular Chart
    class RingChartPanel extends JPanel {
        private int solved = 0;
        private int total = 1;

        public RingChartPanel() {
            setOpaque(false);
        }

        public void setValues(int solved, int total) {
            this.solved = solved;
            this.total = total > 0 ? total : 1;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight()) - 10;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            int strokeWidth = 8;

            // Background ring
            g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(50, 50, 50));
            g2.drawArc(x, y, size, size, 0, 360);

            // Progress ring
            double extent = (double) solved / total * 360.0;
            g2.setColor(new Color(230, 160, 50)); // Orange-ish LeetCode vibe
            g2.drawArc(x, y, size, size, 90, (int) -extent); // Draw clockwise from top

            // Text in center
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
            String topTxt = String.valueOf(solved);
            FontMetrics fmTop = g2.getFontMetrics();
            g2.drawString(topTxt, x + (size - fmTop.stringWidth(topTxt)) / 2, y + size / 2 + 5);

            g2.setColor(new Color(150, 150, 150));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            String botTxt = "Solved";
            FontMetrics fmBot = g2.getFontMetrics();
            g2.drawString(botTxt, x + (size - fmBot.stringWidth(botTxt)) / 2, y + size / 2 + 25);

            g2.dispose();
        }
    }
}
