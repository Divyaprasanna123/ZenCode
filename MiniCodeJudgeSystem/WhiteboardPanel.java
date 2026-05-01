package MiniCodeJudgeSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class WhiteboardPanel extends JPanel {
    private BufferedImage canvasImage;
    private Graphics2D g2;
    private int prevX, prevY;

    public WhiteboardPanel() {
        setBackground(Color.BLACK);
        
        // Ensure image buffer is ready when panel is displayed
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (canvasImage == null) {
                    canvasImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                    g2 = canvasImage.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    clear();
                } else {
                    // Resize logic could go here, but simple implementation is fine
                    BufferedImage newImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D newG2 = newImg.createGraphics();
                    newG2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    newG2.setColor(new Color(25, 25, 25));
                    newG2.fillRect(0, 0, getWidth(), getHeight());
                    newG2.drawImage(canvasImage, 0, 0, null);
                    canvasImage = newImg;
                    g2 = newG2;
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (g2 != null) {
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(prevX, prevY, e.getX(), e.getY());
                    prevX = e.getX();
                    prevY = e.getY();
                    repaint();
                }
            }
        });
    }

    public void clear() {
        if (g2 != null) {
            g2.setPaint(new Color(25, 25, 25)); // Dark grey background
            g2.fillRect(0, 0, getSize().width, getSize().height);
            g2.setPaint(Color.WHITE);
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (canvasImage != null) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }
}
