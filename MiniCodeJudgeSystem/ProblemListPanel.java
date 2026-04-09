package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ProblemListPanel extends JPanel {
    private String username;
    private AppFrame frame;
    private JPanel gridPanel;
    private List<Problem> allProblems;

    public ProblemListPanel(String username, AppFrame frame) {
        this.username = username;
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setOpaque(true);
        setBorder(new EmptyBorder(30, 40, 30, 40));
        
        // Category Filter with glass style
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setOpaque(false);
        
        String[] categories = {"All", "Arrays", "Strings", "DSA", "Math", "DP"};
        for (String cat : categories) {
            JButton btn = createCategoryButton(cat);
            btn.addActionListener(e -> filterByCategory(cat));
            filterPanel.add(btn);
        }

        gridPanel = new JPanel();
        gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));
        gridPanel.setBackground(Color.BLACK);
        gridPanel.setOpaque(true);

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBackground(Color.BLACK);
        scroll.getViewport().setBackground(Color.BLACK);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(filterPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        refreshList();
    }

    private JButton createCategoryButton(String cat) {
        JButton btn = new JButton(cat) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void refreshList() {
        allProblems = ProblemStore.getAllProblems();
        updateGrid(allProblems);
    }

    private void updateGrid(List<Problem> problems) {
        gridPanel.removeAll();
        // Limit to first 100 for display performance in this demo
        int limit = Math.min(problems.size(), 100);
        for (int i = 0; i < limit; i++) {
            gridPanel.add(new ProblemCard(problems.get(i)));
            gridPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacing between lines
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void filterByCategory(String category) {
        if (category.equals("All")) {
            updateGrid(allProblems);
        } else {
            List<Problem> filtered = allProblems.stream()
                .filter(p -> p.category.equalsIgnoreCase(category))
                .toList();
            updateGrid(filtered);
        }
    }

    private class ProblemCard extends JPanel {
        private Problem p;
        private Color cardBg = new Color(255, 255, 255, 10);

        public ProblemCard(Problem p) {
            this.p = p;
            setMaximumSize(new Dimension(1200, 100)); // Full width, shorter height for list row
            setPreferredSize(new Dimension(1200, 100));
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(15, 30, 15, 30));

            JLabel title = new JLabel("<html><body style='width: 180px'>" + p.title + "</body></html>");
            title.setFont(new Font("Segoe UI", Font.BOLD, 18));
            title.setForeground(Color.WHITE);

            JLabel idLabel = new JLabel("#" + p.id);
            idLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            idLabel.setForeground(new Color(150, 150, 150));

            JPanel footer = new JPanel(new BorderLayout());
            footer.setOpaque(false);
            
            JLabel diffTag = new JLabel(" " + p.difficulty + " ");
            diffTag.setOpaque(true);
            diffTag.setFont(new Font("Segoe UI", Font.BOLD, 11));
            diffTag.setForeground(Color.WHITE);
            
            if (p.difficulty.equals("EASY")) diffTag.setBackground(new Color(44, 187, 93, 150));
            else if (p.difficulty.equals("MEDIUM")) diffTag.setBackground(new Color(230, 160, 50, 150));
            else diffTag.setBackground(new Color(200, 50, 50, 150));

            footer.add(idLabel, BorderLayout.WEST);
            footer.add(diffTag, BorderLayout.EAST);

            add(title, BorderLayout.NORTH);
            add(footer, BorderLayout.SOUTH);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    cardBg = new Color(255, 255, 255, 25);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    cardBg = new Color(255, 255, 255, 10);
                    repaint();
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    frame.switchToProblem(p);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Hover lift simulated by border glow
            g2.setColor(cardBg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            
            if (cardBg.getAlpha() > 20) {
                g2.setColor(new Color(44, 187, 93, 100)); // Green glow on hover
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
            } else {
                g2.setColor(new Color(255, 255, 255, 30));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
            }
            
            g2.dispose();
        }
    }
}
