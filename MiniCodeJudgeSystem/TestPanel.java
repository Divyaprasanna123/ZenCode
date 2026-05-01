package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestPanel extends JPanel {
    private String username;
    private AppFrame parentFrame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // Setup UI
    private JComboBox<String> topicCombo;
    private JButton startBtn;

    // Active Test UI
    private JLabel timerLabel;
    private JLabel progressLabel;
    private JLabel titleLabel;
    private JTextArea descArea;
    private JTextArea codeArea;
    private JTextArea outputArea;
    private JButton submitBtn;
    private JButton nextBtn;

    // Test State
    private List<Problem> testProblems;
    private int currentProblemIndex = 0;
    private int score = 0;
    private Timer testTimer;
    private int secondsRemaining;

    public TestPanel(String username, AppFrame parentFrame) {
        this.username = username;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setOpaque(true);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        initSetupUI();
        initActiveTestUI();

        add(cardPanel, BorderLayout.CENTER);
    }

    private JButton createTestBtn(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setMaximumSize(new Dimension(250, 45));
        btn.setPreferredSize(new Dimension(250, 45));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void initSetupUI() {
        JPanel setupWrapper = new JPanel(new GridBagLayout());
        setupWrapper.setOpaque(false);

        JPanel glassPanel = createGlassPanel();
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel("Mock Test Center");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel info = new JLabel("<html><center>Test your knowledge on problems you have already practiced.<br>During the test, navigation is locked and time is strictly monitored.</center></html>");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setForeground(new Color(200, 200, 200));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);

        topicCombo = new JComboBox<>(new String[]{"Arrays", "Strings", "DSA", "Math", "DP"});
        topicCombo.setMaximumSize(new Dimension(200, 30));
        topicCombo.setAlignmentX(Component.CENTER_ALIGNMENT);

        startBtn = createTestBtn("Start Lockdown Test", new Color(200, 50, 50)); // Red
        startBtn.addActionListener(e -> startTest());

        glassPanel.add(title);
        glassPanel.add(Box.createVerticalStrut(20));
        glassPanel.add(info);
        glassPanel.add(Box.createVerticalStrut(30));
        glassPanel.add(topicCombo);
        glassPanel.add(Box.createVerticalStrut(30));
        glassPanel.add(startBtn);

        setupWrapper.add(glassPanel);
        cardPanel.add(setupWrapper, "SETUP");
    }

    private void initActiveTestUI() {
        JPanel activeWrapper = new JPanel(new BorderLayout());
        activeWrapper.setOpaque(false);
        activeWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        progressLabel = new JLabel("Problem 1 of X");
        progressLabel.setForeground(Color.WHITE);
        progressLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        timerLabel = new JLabel("30:00");
        timerLabel.setForeground(new Color(255, 100, 100));
        timerLabel.setFont(new Font("Consolas", Font.BOLD, 20));

        header.add(progressLabel, BorderLayout.WEST);
        header.add(timerLabel, BorderLayout.EAST);
        activeWrapper.add(header, BorderLayout.NORTH);

        // Problem Info (Left)
        JPanel leftPanel = createGlassPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        titleLabel = new JLabel("Title");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        descArea = new JTextArea();
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(new Color(20, 20, 20));
        descArea.setForeground(new Color(220, 220, 220));

        leftPanel.add(titleLabel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(descArea), BorderLayout.CENTER);

        // Editor Info (Right)
        JPanel rightPanel = createGlassPanel();
        rightPanel.setLayout(new BorderLayout());
        
        codeArea = new JTextArea();
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        codeArea.setBackground(new Color(30, 30, 30));
        codeArea.setForeground(Color.WHITE);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(15, 15, 15));
        outputArea.setForeground(new Color(44, 187, 93));

        JSplitPane codeSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(codeArea), new JScrollPane(outputArea));
        codeSplit.setDividerLocation(300);
        rightPanel.add(codeSplit, BorderLayout.CENTER);

        // Footer buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        submitBtn = createTestBtn("Run & Submit", new Color(44, 187, 93));   // Green
        submitBtn.setPreferredSize(new Dimension(160, 40));
        nextBtn = createTestBtn("Next Problem →", new Color(210, 160, 0));    // Yellow
        nextBtn.setPreferredSize(new Dimension(160, 40));
        nextBtn.setEnabled(false);

        submitBtn.addActionListener(e -> submitCurrentProblem());
        nextBtn.addActionListener(e -> loadNextProblem());

        footer.add(submitBtn);
        footer.add(nextBtn);
        rightPanel.add(footer, BorderLayout.SOUTH);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        mainSplit.setDividerLocation(350);
        mainSplit.setOpaque(false);
        activeWrapper.add(mainSplit, BorderLayout.CENTER);

        cardPanel.add(activeWrapper, "TEST");
    }

    private void startTest() {
        String topic = (String) topicCombo.getSelectedItem();
        List<Integer> solvedIds = UserManager.getSolvedProblems(username);
        
        testProblems = new ArrayList<>();
        for (Problem p : ProblemStore.getAllProblems()) {
            if (p.category.equalsIgnoreCase(topic) && solvedIds.contains(p.id)) {
                testProblems.add(p);
            }
        }

        if (testProblems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You haven't solved any problems in " + topic + " yet! Practice first.");
            return;
        }

        Collections.shuffle(testProblems);
        // Take up to 3 problems for the test
        if (testProblems.size() > 3) {
            testProblems = testProblems.subList(0, 3);
        }

        parentFrame.setLockdown(true);
        currentProblemIndex = 0;
        score = 0;
        secondsRemaining = testProblems.size() * 10 * 60; // 10 mins per problem

        testTimer = new Timer(1000, e -> {
            secondsRemaining--;
            int m = secondsRemaining / 60;
            int s = secondsRemaining % 60;
            timerLabel.setText(String.format("%02d:%02d", m, s));
            if (secondsRemaining <= 0) {
                endTest("Time's up!");
            }
        });
        testTimer.start();

        cardLayout.show(cardPanel, "TEST");
        loadProblemUI();
    }

    private void loadProblemUI() {
        Problem p = testProblems.get(currentProblemIndex);
        progressLabel.setText("Problem " + (currentProblemIndex + 1) + " of " + testProblems.size());
        titleLabel.setText(p.title);
        descArea.setText(p.description);
        codeArea.setText(p.starterCode);
        outputArea.setText("Ready.");
        submitBtn.setEnabled(true);
        nextBtn.setEnabled(false);
    }

    private void submitCurrentProblem() {
        submitBtn.setEnabled(false);
        Problem p = testProblems.get(currentProblemIndex);
        String code = codeArea.getText();
        outputArea.setText("Evaluating...");

        SwingWorker<CodeRunner.Result, Void> worker = new SwingWorker<>() {
            @Override protected CodeRunner.Result doInBackground() {
                return new CodeRunner().executeJava(code, p, true);
            }
            @Override protected void done() {
                try {
                    CodeRunner.Result r = get();
                    outputArea.setText(r.output);
                    if (r.success) {
                        score += 100;
                        outputArea.append("\n✅ Correct! +100 points.");
                    } else {
                        outputArea.append("\n❌ Incorrect.");
                    }
                    nextBtn.setEnabled(true);
                } catch (Exception e) {}
            }
        };
        worker.execute();
    }

    private void loadNextProblem() {
        currentProblemIndex++;
        if (currentProblemIndex >= testProblems.size()) {
            endTest("Test Complete!");
        } else {
            loadProblemUI();
        }
    }

    private void endTest(String message) {
        if (testTimer != null) testTimer.stop();
        parentFrame.setLockdown(false);
        int maxScore = testProblems.size() * 100;
        UserManager.addMockTestResult(username, (String) topicCombo.getSelectedItem(), score, maxScore);
        
        JOptionPane.showMessageDialog(this, message + "\nFinal Score: " + score + " / " + maxScore);
        cardLayout.show(cardPanel, "SETUP");
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
