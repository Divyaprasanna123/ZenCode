package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.MessageDigest;

public class Auth {
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private static final String USERS_FILE = "users.txt";

    public Auth() {
        frame = new JFrame("ZenCode - Authentication");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        
        cardPanel.add(createLoginPanel(), "LOGIN");
        cardPanel.add(createSignupPanel(), "SIGNUP");
        
        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(new GridBagLayout());
        bg.add(cardPanel);
        
        frame.add(bg);
        frame.setLocationRelativeTo(null);
    }

    public void show() { frame.setVisible(true); }
    
    private JPanel createGlassCard(String subtitle) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 10)); // Subtle glass
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(new Color(255, 255, 255, 20)); // Thin edge
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(450, 580));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel logo = new JLabel("ZenCode");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(180, 180, 180));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(logo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(sub);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        return panel;
    }
    
    private void addInput(JPanel parent, String labelText, JComponent field) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 8));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(350, 75));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(200, 200, 200));
        
        field.setPreferredSize(new Dimension(350, 45));
        field.setBackground(new Color(35, 35, 35)); // Solid color to prevent artifacts
        field.setOpaque(true);
        field.setForeground(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        
        if (field instanceof JTextField) {
            ((JTextField)field).setCaretColor(Color.WHITE);
        }

        // Clean modern border
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1, true),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        wrapper.add(label, BorderLayout.NORTH);
        wrapper.add(field, BorderLayout.CENTER);
        parent.add(wrapper);
        parent.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private JPanel createLoginPanel() {
        JPanel card = createGlassCard("Sign in to your workspace");
        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        
        addInput(card, "Username", user);
        addInput(card, "Password", pass);
        
        JButton loginBtn = createBtn("Login", new Color(44, 187, 93));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.addActionListener(e -> {
            String u = user.getText().trim();
            String p = new String(pass.getPassword());
            if (authenticate(u, hashPassword(p))) {
                frame.dispose();
                new AppFrame(u).show();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials");
            }
        });
        
        JButton toSignup = createBtn("Create an Account", new Color(60, 60, 60));
        toSignup.setForeground(Color.WHITE);
        toSignup.addActionListener(e -> cardLayout.show(cardPanel, "SIGNUP"));
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(loginBtn);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(toSignup);
        return card;
    }

    private JPanel createSignupPanel() {
        JPanel card = createGlassCard("Start your journey");
        JTextField user = new JTextField();
        JPasswordField p1 = new JPasswordField();
        JPasswordField p2 = new JPasswordField();
        
        addInput(card, "Username", user);
        addInput(card, "Password", p1);
        addInput(card, "Confirm Password", p2);
        
        JButton regBtn = createBtn("Register Now", new Color(44, 187, 93));
        regBtn.setForeground(Color.WHITE);
        regBtn.addActionListener(e -> {
            String u = user.getText().trim();
            String pass1 = new String(p1.getPassword());
            if (pass1.equals(new String(p2.getPassword())) && registerUser(u, hashPassword(pass1))) {
                JOptionPane.showMessageDialog(frame, "Created!");
                cardLayout.show(cardPanel, "LOGIN");
            }
        });
        
        JButton backBtn = createBtn("Back to Login", new Color(60, 60, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));
        
        card.add(regBtn);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(backBtn);
        return card;
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(350, 50));
        btn.setPreferredSize(new Dimension(350, 50));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexS = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexS.append('0');
                hexS.append(hex);
            }
            return hexS.toString();
        } catch(Exception ex){ return ""; }
    }

    private boolean authenticate(String username, String hashedPass) {
        File f = new File(USERS_FILE);
        if (!f.exists()) return false;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = r.readLine()) != null) {
                String[] p = l.split(":", -1);
                if (p.length >= 2 && p[0].equals(username) && p[1].equals(hashedPass)) return true;
            }
        } catch (IOException e) {}
        return false;
    }

    private boolean registerUser(String username, String hashedPass) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            w.write(username + ":" + hashedPass + "::");
            w.newLine();
            return true;
        } catch (IOException e) { return false; }
    }
}
