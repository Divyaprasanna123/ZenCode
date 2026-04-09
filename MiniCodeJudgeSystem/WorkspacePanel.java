package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WorkspacePanel extends JPanel {
    private String username;
    private Problem currentProblem = null;
    
    private JTextArea codeArea;
    private JTextArea outputArea;
    private JLabel titleLabel;
    private JTextArea descArea;
    private JTextField classNameField;
    private JComboBox<String> languageCombo;
    
    private JButton runButton;
    private JButton submitButton;
    private JButton resetButton;
    
    public WorkspacePanel(String username) {
        this.username = username;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setOpaque(true);
        setBorder(new EmptyBorder(20, 30, 20, 30));
        setupMainLayout();
    }

    public void setProblem(Problem p) {
        this.currentProblem = p;
        titleLabel.setText(p.id + ". " + p.title + " (" + p.difficulty + ")");
        
        StringBuilder sb = new StringBuilder();
        sb.append(p.description);
        
        if (p.sampleTestCases != null && !p.sampleTestCases.isEmpty()) {
            sb.append("\n\n--- Sample Test Cases ---\n");
            for (int i = 0; i < p.sampleTestCases.size(); i++) {
                Problem.TestCase tc = p.sampleTestCases.get(i);
                sb.append("Case ").append(i + 1).append(":\n");
                sb.append("Input:    ").append(tc.input.isEmpty() ? "None" : tc.input).append("\n");
                sb.append("Output:   ").append(tc.expectedOutput).append("\n\n");
            }
        }
        
        descArea.setText(sb.toString());
        classNameField.setText(p.defaultClassName);
        String savedCode = CodeStorage.loadCode(username, p.id);
        codeArea.setText(savedCode != null ? savedCode : p.starterCode);
        outputArea.setText("Ready to compile and run.");
    }

    private void setupMainLayout() {
        JPanel problemPanel = createGlassPanel();
        problemPanel.setLayout(new BorderLayout());
        problemPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        titleLabel = new JLabel("No Problem Selected");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        descArea = new JTextArea();
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descArea.setOpaque(true);
        descArea.setBackground(new Color(30, 30, 30));
        descArea.setForeground(new Color(200, 200, 200));

        problemPanel.add(titleLabel, BorderLayout.NORTH);
        problemPanel.add(new JScrollPane(descArea){{setOpaque(false); getViewport().setOpaque(false); setBorder(null);}}, BorderLayout.CENTER);

        JPanel editorPanel = createGlassPanel();
        editorPanel.setLayout(new BorderLayout());
        
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        toolbar.setOpaque(false);
        
        languageCombo = new JComboBox<>(new String[]{"Java"});
        classNameField = new JTextField("", 12);
        classNameField.setBackground(new Color(40, 40, 40));
        classNameField.setForeground(Color.WHITE);
        classNameField.setCaretColor(Color.WHITE);
        classNameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        toolbar.add(languageCombo);
        toolbar.add(new JLabel("Class: "){{setForeground(Color.GRAY);}});
        toolbar.add(classNameField);

        codeArea = new JTextArea();
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        codeArea.setBackground(new Color(20, 20, 20));
        codeArea.setForeground(new Color(220, 220, 220));
        codeArea.setCaretColor(Color.WHITE);
        
        editorPanel.add(toolbar, BorderLayout.NORTH);
        editorPanel.add(new JScrollPane(codeArea){{setBorder(null);}}, BorderLayout.CENTER);

        JPanel consolePanel = createGlassPanel();
        consolePanel.setLayout(new BorderLayout());
        
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(15, 15, 15));
        outputArea.setForeground(new Color(44, 187, 93));
        outputArea.setFont(new Font("Consolas", Font.BOLD, 14));
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.setOpaque(false);
        
        resetButton = createElevatedButton("Reset", new Color(200, 50, 50));
        runButton = createElevatedButton("Run Code", new Color(60, 60, 60));
        submitButton = createElevatedButton("Submit Code", new Color(44, 187, 93));

        resetButton.addActionListener(e -> { if(currentProblem!=null) codeArea.setText(currentProblem.starterCode); });
        runButton.addActionListener(e -> runAction(false));
        submitButton.addActionListener(e -> runAction(true));

        btnPanel.add(resetButton);
        btnPanel.add(runButton);
        btnPanel.add(submitButton);

        consolePanel.add(new JScrollPane(outputArea){{setBorder(null);}}, BorderLayout.CENTER);
        consolePanel.add(btnPanel, BorderLayout.SOUTH);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPanel, consolePanel);
        rightSplit.setDividerLocation(450);
        rightSplit.setOpaque(false);
        rightSplit.setBorder(null);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, problemPanel, rightSplit);
        mainSplit.setDividerLocation(400);
        mainSplit.setOpaque(false);
        mainSplit.setBorder(null);

        add(mainSplit, BorderLayout.CENTER);
    }

    private JPanel createGlassPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(255, 255, 255, 20));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
            }
        };
    }

    private JButton createElevatedButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void runAction(boolean isSubmit) {
        if (currentProblem == null) return;
        outputArea.setText(isSubmit ? "> Submitting..." : "> Running...");
        String code = codeArea.getText();
        SwingWorker<CodeRunner.Result, Void> worker = new SwingWorker<>() {
            @Override protected CodeRunner.Result doInBackground() {
                return new CodeRunner().executeJava(code, currentProblem, isSubmit);
            }
            @Override protected void done() {
                try {
                    CodeRunner.Result r = get();
                    outputArea.setText(r.output + "\n\n> Result: " + (r.success ? "PASSED" : "FAILED"));
                } catch (Exception e) {}
            }
        };
        worker.execute();
    }
}
