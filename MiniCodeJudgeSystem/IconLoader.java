package MiniCodeJudgeSystem;

import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class IconLoader {
    public static ImageIcon getIcon(String name) {
        String path = "/MiniCodeJudgeSystem/icons/" + name + ".png";
        java.net.URL imgURL = IconLoader.class.getResource(path);
        
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            // Fallback for development/local execution
            File file = new File("icons/" + name + ".png");
            if (file.exists()) {
                return new ImageIcon(file.getAbsolutePath());
            }
        }
        return new ImageIcon(generateDefaultIcon(name));
    }

    private static Image generateDefaultIcon(String name) {
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(200, 200, 200));

        switch (name.toLowerCase()) {
            case "play":
                g2.fillPolygon(new int[]{8, 24, 8}, new int[]{8, 16, 24}, 3);
                break;
            case "submit":
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(6, 16, 14, 24);
                g2.drawLine(14, 24, 28, 8);
                break;
            case "reset":
                g2.setStroke(new BasicStroke(2));
                g2.drawArc(6, 6, 20, 20, 0, 270);
                g2.fillPolygon(new int[]{20, 28, 24}, new int[]{6, 6, 12}, 3);
                break;
            case "refresh":
                g2.setStroke(new BasicStroke(2));
                g2.drawArc(6, 6, 20, 20, 0, 270);
                g2.fillPolygon(new int[]{20, 28, 24}, new int[]{6, 6, 12}, 3);
                break;
            case "logout":
                g2.drawRect(4, 4, 18, 24);
                g2.drawLine(16, 16, 28, 16);
                g2.fillPolygon(new int[]{24, 30, 24}, new int[]{10, 16, 22}, 3);
                break;
            case "login":
                g2.drawRect(10, 4, 18, 24);
                g2.drawLine(4, 16, 16, 16);
                g2.fillPolygon(new int[]{12, 18, 12}, new int[]{10, 16, 22}, 3);
                break;
            case "timer":
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(8, 8, 16, 16);
                g2.drawLine(16, 16, 16, 10);
                g2.drawLine(16, 16, 20, 16);
                break;
            case "leaderboard":
                g2.fillRect(6, 18, 6, 8);
                g2.fillRect(13, 10, 6, 16);
                g2.fillRect(20, 14, 6, 12);
                break;
            case "profile":
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(11, 7, 10, 10); // Head
                g2.drawArc(6, 18, 20, 20, 0, 180); // Shoulders
                break;
            default:
                g2.fillRect(8, 8, 16, 16);
                break;
        }
        g2.dispose();
        return img;
    }
}
