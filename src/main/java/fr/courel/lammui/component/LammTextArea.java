package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Zone de texte stylisée avec label et bordure Lamm.
 */
public class LammTextArea extends JPanel {

    private static final int BORDER_WIDTH = 1;
    private static final int ARC = 8;

    private final JTextArea textArea;
    private final JScrollPane scrollPane;
    private final String label;

    public LammTextArea(String label) {
        this(label, 4, 20);
    }

    public LammTextArea(String label, int rows, int cols) {
        this.label = label;
        setLayout(new BorderLayout());
        setOpaque(false);

        textArea = new JTextArea(rows, cols);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setOpaque(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(22, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;
        boolean hasFocus = textArea.hasFocus();

        // Fond de la zone
        g2.setColor(LammColors.fieldBackground());
        g2.fillRoundRect(0, 20, w, h - 20, ARC, ARC);

        // Bordure toujours en gris subtil
        g2.setColor(LammColors.border());
        g2.setStroke(new BasicStroke(BORDER_WIDTH));
        g2.drawRoundRect(0, 20, w - 1, h - 21, ARC, ARC);

        // Label
        Color labelColor = hasFocus ? accentColor : LammColors.textSecondary();
        int style = hasFocus ? Font.BOLD : Font.PLAIN;
        g2.setColor(labelColor);
        g2.setFont(LammFonts.CAPTION.deriveFont(style, 11f));
        g2.drawString(label, 4, 14);

        // Couleur du texte
        textArea.setForeground(LammColors.textPrimary());
        textArea.setCaretColor(accentColor);

        g2.dispose();
        super.paintComponent(g);
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public void append(String text) {
        textArea.append(text);
    }

    public void setEditable(boolean editable) {
        textArea.setEditable(editable);
    }
}
