package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Metal (cross-platform) L&F respects setBackground/setForeground
                // on ALL components; Windows L&F silently overrides them.
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ── Global dark overrides (counters Windows L&F white bleed) ──
            Color dark      = new Color(30, 30, 30);
            Color darker    = new Color(20, 20, 20);
            Color border    = new Color(50, 50, 50);
            Color textWhite = Color.WHITE;
            Color textGray  = new Color(180, 180, 180);

            // Text fields & areas
            UIManager.put("TextField.background",         new Color(40, 40, 40));
            UIManager.put("TextField.foreground",         textWhite);
            UIManager.put("TextField.caretForeground",    textWhite);
            UIManager.put("TextField.border",             BorderFactory.createEmptyBorder(5,10,5,10));
            UIManager.put("PasswordField.background",     new Color(35, 35, 35));
            UIManager.put("PasswordField.foreground",     textWhite);
            UIManager.put("PasswordField.caretForeground",textWhite);
            UIManager.put("TextArea.background",          darker);
            UIManager.put("TextArea.foreground",          textWhite);
            UIManager.put("TextArea.caretForeground",     textWhite);
            UIManager.put("TextPane.background",          darker);
            UIManager.put("TextPane.foreground",          textWhite);
            UIManager.put("TextPane.caretForeground",     textWhite);

            // Progress bars
            UIManager.put("ProgressBar.background",       border);
            UIManager.put("ProgressBar.foreground",       new Color(44, 187, 93));
            UIManager.put("ProgressBar.selectionBackground", textWhite);
            UIManager.put("ProgressBar.selectionForeground", textWhite);
            UIManager.put("ProgressBar.border",           BorderFactory.createEmptyBorder());

            // Tabbed pane
            UIManager.put("TabbedPane.background",        dark);
            UIManager.put("TabbedPane.foreground",        textWhite);
            UIManager.put("TabbedPane.selected",          new Color(50, 50, 50));
            UIManager.put("TabbedPane.contentAreaColor",  dark);
            UIManager.put("TabbedPane.tabsOpaque",        Boolean.FALSE);
            UIManager.put("TabbedPane.shadow",            dark);
            UIManager.put("TabbedPane.darkShadow",        dark);
            UIManager.put("TabbedPane.light",             new Color(50, 50, 50));
            UIManager.put("TabbedPane.highlight",         new Color(50, 50, 50));
            UIManager.put("TabbedPane.focus",             new Color(50, 50, 50));
            UIManager.put("TabbedPane.unselectedBackground", new Color(35, 35, 35));
            UIManager.put("TabbedPane.borderHightlightColor", border);

            // Tables
            UIManager.put("Table.background",             new Color(25, 25, 25));
            UIManager.put("Table.foreground",             textWhite);
            UIManager.put("Table.gridColor",              border);
            UIManager.put("Table.selectionBackground",    new Color(44, 100, 60));
            UIManager.put("Table.selectionForeground",    textWhite);
            UIManager.put("TableHeader.background",       new Color(35, 35, 35));
            UIManager.put("TableHeader.foreground",       textWhite);
            UIManager.put("TableHeader.cellBorder",       BorderFactory.createLineBorder(border));

            // Scroll bars
            UIManager.put("ScrollBar.background",         dark);
            UIManager.put("ScrollBar.thumb",              border);
            UIManager.put("ScrollBar.track",              darker);
            UIManager.put("ScrollBar.thumbDarkShadow",    dark);
            UIManager.put("ScrollBar.thumbHighlight",     border);
            UIManager.put("ScrollBar.thumbShadow",        dark);

            // ComboBox
            UIManager.put("ComboBox.background",          new Color(40, 40, 40));
            UIManager.put("ComboBox.foreground",          textWhite);
            UIManager.put("ComboBox.selectionBackground", new Color(60, 60, 60));
            UIManager.put("ComboBox.selectionForeground", textWhite);
            UIManager.put("ComboBox.disabledBackground",  dark);
            UIManager.put("ComboBox.disabledForeground",  textGray);

            // General panel / option pane
            UIManager.put("Panel.background",             dark);
            UIManager.put("OptionPane.background",        dark);
            UIManager.put("OptionPane.messageForeground", textWhite);

            // CheckBox
            UIManager.put("CheckBox.background",          Color.BLACK);
            UIManager.put("CheckBox.foreground",          textWhite);

            new Auth().show();
        });
    }
}
