package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Checkbox Material Design avec animation de la coche.
 */
public class LammCheckbox extends JComponent {

    private static final int BOX_SIZE = 20;
    private static final int ARC = 4;
    private static final int GAP = 10;

    private boolean selected = false;
    private float animation = 0f;
    private Timer animTimer;
    private String label;

    public LammCheckbox(String label) {
        this.label = label;
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelected(!selected);
            }
        });
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        animate(selected);
        firePropertyChange("selected", !selected, selected);
    }

    private void animate(boolean on) {
        if (animTimer != null && animTimer.isRunning()) animTimer.stop();
        animTimer = new Timer(12, _ -> {
            float target = on ? 1f : 0f;
            float diff = target - animation;
            animation += diff * 0.2f;
            if (Math.abs(diff) < 0.01f) {
                animation = target;
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

        Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;
        int boxY = (getHeight() - BOX_SIZE) / 2;

        // Boîte
        if (animation > 0.01f) {
            // Fond coloré animé
            Color fillColor = LammColors.withAlpha(accentColor, (int) (255 * animation));
            g2.setColor(fillColor);
            g2.fill(new RoundRectangle2D.Float(0, boxY, BOX_SIZE, BOX_SIZE, ARC, ARC));
        }

        // Bordure
        Color borderColor = animation > 0.5f ? accentColor : LammColors.border();
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Float(0.5f, boxY + 0.5f, BOX_SIZE - 1, BOX_SIZE - 1, ARC, ARC));

        // Coche
        if (animation > 0.2f) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            float progress = Math.min(1f, (animation - 0.2f) / 0.8f);
            int cx = BOX_SIZE / 2;
            int cy = boxY + BOX_SIZE / 2;

            // Première ligne de la coche (descente)
            int x1 = cx - 5, y1 = cy;
            int x2 = cx - 1, y2 = cy + 4;
            // Deuxième ligne (montée)
            int x3 = cx + 6, y3 = cy - 4;

            if (progress <= 0.5f) {
                float p = progress * 2;
                g2.drawLine(x1, y1, x1 + (int) ((x2 - x1) * p), y1 + (int) ((y2 - y1) * p));
            } else {
                g2.drawLine(x1, y1, x2, y2);
                float p = (progress - 0.5f) * 2;
                g2.drawLine(x2, y2, x2 + (int) ((x3 - x2) * p), y2 + (int) ((y3 - y2) * p));
            }
        }

        // Label
        if (label != null) {
            g2.setFont(LammFonts.BODY);
            g2.setColor(LammColors.textPrimary());
            var fm = g2.getFontMetrics();
            g2.drawString(label, BOX_SIZE + GAP, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
        }

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        int labelWidth = 0;
        if (label != null) {
            labelWidth = getFontMetrics(LammFonts.BODY).stringWidth(label) + GAP;
        }
        return new Dimension(BOX_SIZE + labelWidth + 4, BOX_SIZE + 8);
    }
}
