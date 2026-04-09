package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AppFrame {
    private String username;
    private JFrame frame;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    private ProblemListPanel problemListPanel;
    private WorkspacePanel workspacePanel;
    private ProfilePanel profilePanel;

    public AppFrame(String username) {
        this.username = username;
        frame = new JFrame("ZenCode - " + username);
        frame.setSize(1280, 850);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        BackgroundPanel mainContainer = new BackgroundPanel();
        mainContainer.setLayout(new BorderLayout());

        setupNavigationBar(mainContainer);
        setupTabButtons(mainContainer);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);
        
        problemListPanel = new ProblemListPanel(username, this);
        workspacePanel = new WorkspacePanel(username);
        profilePanel = new ProfilePanel(username, frame);

        contentPanel.add(problemListPanel, "PROBLEMS");
        contentPanel.add(workspacePanel, "WORKSPACE");
        contentPanel.add(profilePanel, "PROFILE");

        JPanel topHeader = new JPanel();
        topHeader.setLayout(new BoxLayout(topHeader, BoxLayout.Y_AXIS));
        topHeader.setOpaque(false);
        
        setupNavigationBar(topHeader);
        setupTabButtons(topHeader);

        mainContainer.add(topHeader, BorderLayout.NORTH);
        mainContainer.add(contentPanel, BorderLayout.CENTER);
        frame.add(mainContainer);
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        frame.setVisible(true);
    }

    public void switchToProblem(Problem p) {
        workspacePanel.setProblem(p);
        cardLayout.show(contentPanel, "WORKSPACE");
    }

    private void setupNavigationBar(JPanel container) {
        JPanel navBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        navBar.setPreferredSize(new Dimension(0, 70));
        navBar.setBorder(new EmptyBorder(10, 30, 10, 30));
        navBar.setOpaque(false);

        JLabel logo = new JLabel("</> ZenCode");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        logo.setForeground(new Color(44, 187, 93));
        
        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightNav.setOpaque(false);

        // Fixed User Icon (using graphic instead of emoji to fix [])
        JLabel userLabel = new JLabel("User: " + username);
        userLabel.setIcon(IconLoader.getIcon("profile"));
        userLabel.setIconTextGap(10);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        userLabel.setForeground(Color.WHITE);

        rightNav.add(userLabel);
        navBar.add(logo, BorderLayout.WEST);
        navBar.add(rightNav, BorderLayout.EAST);

        container.add(navBar, BorderLayout.NORTH);
    }

    private void setupTabButtons(JPanel container) {
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnBar.setOpaque(false);
        btnBar.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton probBtn = createNavBtn(" Problems ", new Color(200, 50, 50)); // Red
        JButton workBtn = createNavBtn(" Workspace ", new Color(44, 187, 93)); // Green
        JButton profBtn = createNavBtn(" Profile ", new Color(200, 50, 50)); // Red

        probBtn.addActionListener(e -> cardLayout.show(contentPanel, "PROBLEMS"));
        workBtn.addActionListener(e -> cardLayout.show(contentPanel, "WORKSPACE"));
        profBtn.addActionListener(e -> cardLayout.show(contentPanel, "PROFILE"));

        btnBar.add(probBtn);
        btnBar.add(workBtn);
        btnBar.add(profBtn);

        container.add(btnBar);
    }

    private JButton createNavBtn(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
