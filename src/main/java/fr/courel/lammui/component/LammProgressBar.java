package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Barre de progression avec animation et mode indéterminé.
 */
public class LammProgressBar extends JComponent {

    private static final int BAR_HEIGHT = 6;
    private static final int ARC = 3;

    private float value = 0f;
    private float displayValue = 0f;
    private boolean indeterminate = false;
    private float indeterminatePos = 0f;
    private Timer animTimer;
    private Timer indeterminateTimer;

    public LammProgressBar() {
        setOpaque(false);
    }

    public void setValue(float value) {
        this.value = Math.clamp(value, 0f, 1f);
        animateValue();
    }

    public float getValue() {
        return value;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
        if (indeterminate) {
            startIndeterminate();
        } else if (indeterminateTimer != null) {
            indeterminateTimer.stop();
        }
        repaint();
    }

    private void animateValue() {
        if (animTimer != null && animTimer.isRunning()) animTimer.stop();
        animTimer = new Timer(12, _ -> {
            float diff = value - displayValue;
            displayValue += diff * 0.12f;
            if (Math.abs(diff) < 0.005f) {
                displayValue = value;
                animTimer.stop();
            }
            repaint();
        });
        animTimer.start();
    }

    private void startIndeterminate() {
        if (indeterminateTimer != null && indeterminateTimer.isRunning()) return;
        indeterminateTimer = new Timer(16, _ -> {
            indeterminatePos += 0.015f;
            if (indeterminatePos > 1.3f) indeterminatePos = -0.3f;
            repaint();
        });
        indeterminateTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int y = (getHeight() - BAR_HEIGHT) / 2;

        // Track
        g2.setColor(LammColors.divider());
        g2.fill(new RoundRectangle2D.Float(0, y, w, BAR_HEIGHT, ARC, ARC));

        Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;

        if (indeterminate) {
            // Barre qui se déplace
            float barWidth = w * 0.3f;
            float barX = indeterminatePos * w;
            g2.setClip(new RoundRectangle2D.Float(0, y, w, BAR_HEIGHT, ARC, ARC));
            g2.setColor(accentColor);
            g2.fill(new RoundRectangle2D.Float(barX, y, barWidth, BAR_HEIGHT, ARC, ARC));
        } else {
            // Barre de progression
            float barWidth = w * displayValue;
            if (barWidth > 0) {
                g2.setColor(accentColor);
                g2.fill(new RoundRectangle2D.Float(0, y, barWidth, BAR_HEIGHT, ARC, ARC));
            }
        }

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, BAR_HEIGHT + 8);
    }
}
