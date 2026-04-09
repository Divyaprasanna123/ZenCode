package MiniCodeJudgeSystem;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    public BackgroundPanel() {
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Premium Linear Gradient (Black to Deep Charcoal at the bottom)
        GradientPaint gp = new GradientPaint(
            0, 0, new Color(10, 10, 10),
            0, getHeight(), new Color(35, 35, 35)
        );
        
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        g2.dispose();
    }
}
