package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Champ texte style Materialize : ligne en bas + label flottant.
 */
public class LammTextField extends JPanel {

    private static final int PADDING = 4;
    private static final int LINE_NORMAL = 1;
    private static final int LINE_FOCUS = 2;

    private final JTextField field;
    private final String placeholder;
    private boolean focused = false;
    private float focusAnimation = 0f;
    private Timer animTimer;

    public LammTextField(String placeholder) {
        this(placeholder, 20);
    }

    public LammTextField(String placeholder, int columns) {
        this.placeholder = placeholder;

        setLayout(new BorderLayout());
        setOpaque(false);

        field = new JTextField(columns);
        field.setFont(LammFonts.BODY);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(16, PADDING, 6, PADDING));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                focused = true;
                animate(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                focused = false;
                animate(false);
            }
        });

        add(field, BorderLayout.CENTER);
    }

    private void animate(boolean in) {
        if (animTimer != null && animTimer.isRunning()) animTimer.stop();
        animTimer = new Timer(12, _ -> {
            float target = in ? 1f : 0f;
            float diff = target - focusAnimation;
            focusAnimation += diff * 0.15f;
            if (Math.abs(diff) < 0.01f) {
                focusAnimation = target;
                animTimer.stop();
            }
            repaint();
        });
        animTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int lineY = h - LINE_FOCUS;

        Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;

        // Ligne de base grise
        g2.setColor(LammColors.border());
        g2.fillRect(PADDING, lineY, w - PADDING * 2, LINE_NORMAL);

        // Ligne accent animée depuis le centre
        if (focusAnimation > 0.01f) {
            int lineWidth = w - PADDING * 2;
            int center = PADDING + lineWidth / 2;
            int halfWidth = (int) (lineWidth / 2 * focusAnimation);
            g2.setColor(accentColor);
            g2.fillRect(center - halfWidth, lineY, halfWidth * 2, LINE_FOCUS);
        }

        // Label flottant
        boolean hasText = !field.getText().isEmpty();
        float t = hasText ? 1f : focusAnimation;

        float labelYDown = 30f;
        float labelYUp = 12f;
        float labelY = labelYDown + (labelYUp - labelYDown) * t;
        float labelSize = 14f + (11f - 14f) * t;
        Color labelColor = interpolateColor(LammColors.textSecondary(), accentColor, t);

        g2.setColor(labelColor);
        int style = focused ? Font.BOLD : Font.PLAIN;
        g2.setFont(LammFonts.CAPTION.deriveFont(style, labelSize));
        g2.drawString(placeholder, PADDING, labelY);

        // Couleur du texte et caret
        field.setForeground(LammColors.textPrimary());
        field.setCaretColor(accentColor);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = field.getPreferredSize();
        return new Dimension(d.width, d.height + 6);
    }

    public String getText() {
        return field.getText();
    }

    public void setText(String text) {
        field.setText(text);
        repaint();
    }

    public JTextField getField() {
        return field;
    }

    private static Color interpolateColor(Color from, Color to, float t) {
        int r = (int) (from.getRed() + (to.getRed() - from.getRed()) * t);
        int g = (int) (from.getGreen() + (to.getGreen() - from.getGreen()) * t);
        int b = (int) (from.getBlue() + (to.getBlue() - from.getBlue()) * t);
        return new Color(r, g, b);
    }
}
