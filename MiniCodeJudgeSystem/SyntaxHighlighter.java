package MiniCodeJudgeSystem;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.*;

public class SyntaxHighlighter {
    
    // Define standard Java keywords
    private static final String[] KEYWORDS = {
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
        "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
        "for", "if", "goto", "implements", "import", "instanceof", "int", "interface", "long", "native",
        "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super",
        "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while",
        "true", "false", "null", "String", "System", "out", "println", "print"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String STRING_PATTERN = "\".*?\"";
    private static final String COMMENT_PATTERN = "//.*";

    // Styles
    private static final SimpleAttributeSet KEYWORD_STYLE = new SimpleAttributeSet();
    private static final SimpleAttributeSet STRING_STYLE = new SimpleAttributeSet();
    private static final SimpleAttributeSet COMMENT_STYLE = new SimpleAttributeSet();
    private static final SimpleAttributeSet NORMAL_STYLE = new SimpleAttributeSet();

    static {
        StyleConstants.setForeground(KEYWORD_STYLE, new Color(200, 80, 200)); // Purple
        StyleConstants.setBold(KEYWORD_STYLE, true);

        StyleConstants.setForeground(STRING_STYLE, new Color(100, 200, 100)); // Green
        
        StyleConstants.setForeground(COMMENT_STYLE, new Color(130, 130, 130)); // Grey
        StyleConstants.setItalic(COMMENT_STYLE, true);

        StyleConstants.setForeground(NORMAL_STYLE, Color.WHITE);
        StyleConstants.setFontFamily(NORMAL_STYLE, "Consolas");
        StyleConstants.setFontSize(NORMAL_STYLE, 16);
    }

    public static void apply(JTextPane textPane) {
        StyledDocument doc = textPane.getStyledDocument();
        
        // Remove old listener if exists to prevent duplicates (in case of re-apply)
        // For simplicity, we just add a document listener that parses on change
        doc.addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { highlight(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { highlight(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}

            private void highlight() {
                SwingUtilities.invokeLater(() -> {
                    try {
                        String text = doc.getText(0, doc.getLength());
                        // Reset to normal first
                        doc.setCharacterAttributes(0, text.length(), NORMAL_STYLE, true);

                        // Highlight Keywords
                        Matcher m = Pattern.compile(KEYWORD_PATTERN).matcher(text);
                        while (m.find()) {
                            doc.setCharacterAttributes(m.start(), m.end() - m.start(), KEYWORD_STYLE, false);
                        }

                        // Highlight Strings
                        m = Pattern.compile(STRING_PATTERN).matcher(text);
                        while (m.find()) {
                            doc.setCharacterAttributes(m.start(), m.end() - m.start(), STRING_STYLE, false);
                        }

                        // Highlight Comments
                        m = Pattern.compile(COMMENT_PATTERN).matcher(text);
                        while (m.find()) {
                            doc.setCharacterAttributes(m.start(), m.end() - m.start(), COMMENT_STYLE, false);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });
    }
}
