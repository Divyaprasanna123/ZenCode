package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class WorkspacePanel extends JPanel {
    private String username;
    private Problem currentProblem = null;
    
    private JTextPane codeArea;
    private JTextArea outputArea;
    private JLabel titleLabel;
    private JTextArea descArea;
    private JTextField classNameField;
    private JComboBox<String> languageCombo;
    
    private JButton runButton;
    private JButton submitButton;
    private JButton resetButton;
    
    // Collaborative Features
    private JTextField roomField;
    private JButton joinRoomBtn;
    private JButton battleBtn;
    private WebSocket webSocket;
    private boolean isProgrammaticUpdate = false;
    private boolean isBattleMode = false;
    
    // Zen Timer & Whiteboard
    private WhiteboardPanel whiteboardPanel;
    private JLabel timerLabel;
    private Timer zenTimer;
    private int zenSecondsLeft = 0;
    
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
        
        updateCodeAreaQuietly(savedCode != null ? savedCode : p.starterCode);
        outputArea.setText("Ready to compile and run.");
    }

    private void updateCodeAreaQuietly(String text) {
        isProgrammaticUpdate = true;
        codeArea.setText(text);
        isProgrammaticUpdate = false;
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
        // Custom renderer so Windows L&F can't override the dropdown colors
        languageCombo.setBackground(new Color(40, 40, 40));
        languageCombo.setForeground(Color.WHITE);
        languageCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        languageCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                lbl.setBackground(isSelected ? new Color(60, 60, 60) : new Color(40, 40, 40));
                lbl.setForeground(Color.WHITE);
                lbl.setOpaque(true);
                return lbl;
            }
        });
        classNameField = new JTextField("", 12);
        styleTextField(classNameField);

        roomField = new JTextField("", 8);
        styleTextField(roomField);
        roomField.setToolTipText("Enter Room ID to collaborate");
        
        joinRoomBtn = createElevatedButton("Join Live", new Color(138, 43, 226)); // Purple for magic/live feature
        joinRoomBtn.setPreferredSize(new Dimension(100, 30));
        joinRoomBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        joinRoomBtn.addActionListener(e -> toggleCollabRoom());

        battleBtn = createElevatedButton("Battle", new Color(220, 80, 50));
        battleBtn.setPreferredSize(new Dimension(80, 30));
        battleBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        battleBtn.addActionListener(e -> toggleBattleMode());

        timerLabel = new JLabel("");
        timerLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        timerLabel.setForeground(new Color(230, 160, 50));

        JButton timerBtn = createElevatedButton("⏱️ Timer", new Color(0, 168, 168)); // Teal
        timerBtn.setPreferredSize(new Dimension(100, 30));
        timerBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        timerBtn.addActionListener(e -> startZenTimer());

        JButton hintBtn = createElevatedButton("💡 Hint (-50 XP)", new Color(230, 130, 30)); // Orange
        hintBtn.setPreferredSize(new Dimension(145, 30));
        hintBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        hintBtn.addActionListener(e -> showHint());

        toolbar.add(languageCombo);
        toolbar.add(new JLabel("Class: "){{setForeground(Color.GRAY);}});
        toolbar.add(classNameField);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(timerBtn);
        toolbar.add(timerLabel);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(hintBtn);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(new JLabel("Room: "){{setForeground(new Color(138, 43, 226));}});
        toolbar.add(roomField);
        toolbar.add(joinRoomBtn);
        toolbar.add(battleBtn);

        codeArea = new JTextPane();
        SyntaxHighlighter.apply(codeArea);
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        codeArea.setBackground(new Color(20, 20, 20));
        codeArea.setForeground(new Color(220, 220, 220));
        codeArea.setCaretColor(Color.WHITE);
        
        // Broadcast Code Changes!
        codeArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { broadcastCode(); }
            @Override public void removeUpdate(DocumentEvent e) { broadcastCode(); }
            @Override public void changedUpdate(DocumentEvent e) { broadcastCode(); }
            
            private void broadcastCode() {
                if (!isProgrammaticUpdate && webSocket != null) {
                    String code = codeArea.getText().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
                    String json = "{\"type\":\"code_update\",\"roomId\":\"" + roomField.getText().trim() + "\",\"code\":\"" + code + "\"}";
                    webSocket.sendText(json, true);
                }
            }
        });

        editorPanel.add(toolbar, BorderLayout.NORTH);
        editorPanel.add(new JScrollPane(codeArea){{setBorder(null);}}, BorderLayout.CENTER);

        JPanel consolePanel = createGlassPanel();
        consolePanel.setLayout(new BorderLayout());
        
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(15, 15, 15));
        outputArea.setForeground(new Color(44, 187, 93));
        outputArea.setFont(new Font("Consolas", Font.BOLD, 14));

        JTextArea customInputArea = new JTextArea();
        customInputArea.setBackground(new Color(15, 15, 15));
        customInputArea.setForeground(Color.WHITE);
        customInputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        customInputArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(40, 40, 40)), "Custom Input (Optional)", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12), Color.GRAY));
        
        JPanel consoleWrapper = new JPanel(new BorderLayout());
        consoleWrapper.setOpaque(false);
        consoleWrapper.add(new JScrollPane(outputArea){{setBorder(null);}}, BorderLayout.CENTER);
        consoleWrapper.add(customInputArea, BorderLayout.SOUTH);

        DefaultListModel<String> submissionsModel = new DefaultListModel<>();
        JList<String> submissionsList = new JList<>(submissionsModel);
        submissionsList.setBackground(new Color(15, 15, 15));
        submissionsList.setForeground(Color.WHITE);
        submissionsList.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        whiteboardPanel = new WhiteboardPanel();

        JTabbedPane rightBottomTabs = new JTabbedPane();
        rightBottomTabs.addTab("Console",     consoleWrapper);
        rightBottomTabs.addTab("Whiteboard",  whiteboardPanel);
        rightBottomTabs.addTab("Submissions", new JScrollPane(submissionsList));
        rightBottomTabs.setBackground(new Color(30, 30, 30));
        rightBottomTabs.setForeground(Color.WHITE);
        rightBottomTabs.setOpaque(true);

        // Custom tab label components — Windows L&F cannot override these
        String[] tabTitles = {"  Console  ", "  Whiteboard  ", "  Submissions  "};
        for (int i = 0; i < tabTitles.length; i++) {
            JLabel tabLbl = new JLabel(tabTitles[i]);
            tabLbl.setForeground(Color.WHITE);
            tabLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            tabLbl.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            rightBottomTabs.setTabComponentAt(i, tabLbl);
        }
        
        // Listen for problem changes to update submissions
        rightBottomTabs.addChangeListener(e -> {
            if (rightBottomTabs.getTitleAt(rightBottomTabs.getSelectedIndex()).equals("Submissions")) {
                submissionsModel.clear();
                if (currentProblem != null) {
                    for (String[] sub : UserManager.getSubmissions(username)) {
                        if (sub.length >= 4 && sub[1].equals(String.valueOf(currentProblem.id))) {
                            submissionsModel.addElement(sub[3] + " - " + sub[2]);
                        }
                    }
                }
            }
        });
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.setOpaque(false);
        
        resetButton = createElevatedButton("Reset", new Color(200, 50, 50));       // Red
        runButton = createElevatedButton("Run Code", new Color(210, 160, 0));         // Yellow
        submitButton = createElevatedButton("Submit Code", new Color(44, 187, 93));  // Green

        resetButton.addActionListener(e -> { if(currentProblem!=null) updateCodeAreaQuietly(currentProblem.starterCode); });
        runButton.addActionListener(e -> runAction(false, customInputArea.getText().trim()));
        submitButton.addActionListener(e -> runAction(true, ""));

        btnPanel.add(resetButton);
        btnPanel.add(runButton);
        btnPanel.add(submitButton);

        consolePanel.add(rightBottomTabs, BorderLayout.CENTER);
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
    
    private void styleTextField(JTextField field) {
        field.setBackground(new Color(40, 40, 40));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void toggleCollabRoom() {
        if (webSocket == null) {
            String roomId = roomField.getText().trim();
            if (roomId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a Room ID first to collaborate.");
                return;
            }
            try {
                HttpClient client = HttpClient.newHttpClient();
                WebSocket.Builder builder = client.newWebSocketBuilder();
                webSocket = builder.buildAsync(URI.create(Config.WS_URL + "?roomId=" + roomId + "&username=" + username), 
                        new WebSocketListener()).join();
                outputArea.append("System: Joined room " + roomId + "\n");
                
                joinRoomBtn.setText("Leave Live");
                roomField.setEditable(false);
                outputArea.setText("> Connected to Collab Room: " + roomId);
            } catch (Exception e) {
                outputArea.append("System: Failed to connect to server for live collab.\n");
            }
        } else {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Leaving");
            webSocket = null;
            joinRoomBtn.setText("Join Live");
            roomField.setEditable(true);
            outputArea.setText("> Disconnected from Collab Room.");
        }
    }

    private class WebSocketListener implements WebSocket.Listener {
        StringBuilder textBuilder = new StringBuilder();

        @Override
        public void onOpen(WebSocket webSocket) {
            WebSocket.Listener.super.onOpen(webSocket);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            textBuilder.append(data);
            if (last) {
                String fullMessage = textBuilder.toString();
                textBuilder.setLength(0); // reset
                
                SwingUtilities.invokeLater(() -> {
                    // Primitive JSON parse for simplicity
                    if (fullMessage.contains("\"type\":\"code_update\"")) {
                        try {
                            int codeIndex = fullMessage.indexOf("\"code\":\"") + 8;
                            String codeContent = fullMessage.substring(codeIndex, fullMessage.lastIndexOf("\""));
                            codeContent = codeContent.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
                            
                            // Only update if it's different to prevent loops and cursor jumping
                            if (!codeArea.getText().equals(codeContent)) {
                                int caret = codeArea.getCaretPosition();
                                updateCodeAreaQuietly(codeContent);
                                codeArea.setCaretPosition(Math.min(caret, codeContent.length()));
                            }
                        } catch (Exception e) { e.printStackTrace(); }
                    } else if (fullMessage.contains("\"type\":\"system\"")) {
                        int contentIdx = fullMessage.indexOf("\"content\":\"") + 11;
                        String content = fullMessage.substring(contentIdx, fullMessage.lastIndexOf("\""));
                        outputArea.setText("> " + content + "\n" + outputArea.getText());
                    } else if (fullMessage.contains("\"type\":\"battle_update\"")) {
                        int progIdx = fullMessage.indexOf("\"progress\":") + 11;
                        int progress = Integer.parseInt(fullMessage.substring(progIdx, fullMessage.indexOf("}", progIdx)));
                        outputArea.setText("> Opponent Progress: " + progress + "%\n" + outputArea.getText());
                    } else if (fullMessage.contains("\"type\":\"battle_win\"")) {
                        int nameIdx = fullMessage.indexOf("\"username\":\"") + 12;
                        String winnerName = fullMessage.substring(nameIdx, fullMessage.indexOf("\"", nameIdx));
                        if (!winnerName.equals(username)) {
                            UserManager.addBattleResult(username, false);
                            JOptionPane.showMessageDialog(WorkspacePanel.this, "You lost! " + winnerName + " finished first.");
                            isBattleMode = false;
                            battleBtn.setText("Battle");
                        }
                    }
                });
            }
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }
        
        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            SwingUtilities.invokeLater(() -> {
                WorkspacePanel.this.webSocket = null;
                joinRoomBtn.setText("Join Live");
                roomField.setEditable(true);
            });
            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
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

    private void runAction(boolean isSubmit, String customInput) {
        if (currentProblem == null) return;
        outputArea.setText(isSubmit ? "> Submitting..." : "> Running...");
        String code = codeArea.getText();
        
        // Save local progress if it was just run
        CodeStorage.saveCode(username, currentProblem.id, code);

        SwingWorker<CodeRunner.Result, Void> worker = new SwingWorker<>() {
            @Override protected CodeRunner.Result doInBackground() {
                if (!customInput.isEmpty() && !isSubmit) {
                    return new CodeRunner().executeCustomJava(code, currentProblem, customInput);
                }
                return new CodeRunner().executeJava(code, currentProblem, isSubmit);
            }
            @Override protected void done() {
                try {
                    CodeRunner.Result r = get();
                    outputArea.setText(r.output + "\n\n> Result: " + (r.success ? "PASSED" : "FAILED"));
                    if(isSubmit) {
                        UserManager.addSubmission(username, currentProblem.id, r.success ? "PASSED" : "FAILED");
                        if (r.success) {
                            UserManager.addSolvedProblem(username, currentProblem.id, currentProblem.difficulty);
                        }
                    }
                    if (webSocket != null && isBattleMode) {
                        int passPercent = (r.totalCount == 0) ? 0 : (r.passedCount * 100 / r.totalCount);
                        webSocket.sendText("{\"type\":\"battle_update\",\"roomId\":\"" + roomField.getText().trim() + "\",\"progress\":" + passPercent + "}", true);
                        if (isSubmit && r.success) {
                            webSocket.sendText("{\"type\":\"battle_win\",\"roomId\":\"" + roomField.getText().trim() + "\",\"username\":\"" + username + "\"}", true);
                            UserManager.addBattleResult(username, true);
                            JOptionPane.showMessageDialog(WorkspacePanel.this, "🏆 You won the battle!");
                            isBattleMode = false;
                            battleBtn.setText("Battle");
                        }
                    }
                } catch (Exception e) {}
            }
        };
        worker.execute();
    }

    private void toggleBattleMode() {
        if (webSocket == null) {
            JOptionPane.showMessageDialog(this, "Join a room first to start a battle!");
            return;
        }
        isBattleMode = !isBattleMode;
        if (isBattleMode) {
            battleBtn.setText("End Battle");
            outputArea.setText("> BATTLE MODE ACTIVATED! First to submit 100% passing code wins!\n" + outputArea.getText());
        } else {
            battleBtn.setText("Battle");
        }
    }

    private void startZenTimer() {
        if (currentProblem == null) return;
        if (zenTimer != null && zenTimer.isRunning()) {
            zenTimer.stop();
            timerLabel.setText("");
            return;
        }
        
        zenSecondsLeft = currentProblem.difficulty.equals("EASY") ? 15 * 60 : 
                        (currentProblem.difficulty.equals("MEDIUM") ? 30 * 60 : 45 * 60);
        
        zenTimer = new Timer(1000, e -> {
            zenSecondsLeft--;
            int m = zenSecondsLeft / 60;
            int s = zenSecondsLeft % 60;
            timerLabel.setText(String.format("%02d:%02d", m, s));
            if (zenSecondsLeft <= 0) {
                zenTimer.stop();
                JOptionPane.showMessageDialog(this, "Time is up! Interview Failed.");
            }
        });
        zenTimer.start();
        JOptionPane.showMessageDialog(this, "Zen Interview Timer started! Good luck.");
    }

    private void showHint() {
        if (currentProblem == null) return;
        if (currentProblem.hint == null || currentProblem.hint.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hint available for this problem.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Unlocking this hint will cost 50 XP. Proceed?", "Confirm Hint", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (UserManager.deductExperience(username, 50)) {
                JOptionPane.showMessageDialog(this, "💡 Hint: " + currentProblem.hint);
            } else {
                JOptionPane.showMessageDialog(this, "Not enough XP! Solve more problems to earn XP.");
            }
        }
    }
}
