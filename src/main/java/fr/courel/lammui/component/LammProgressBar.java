package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Barre de progression avec animation et mode indéterminé.
 * Supporte un texte affiché au centre (setString / setStringPainted).
 */
public class LammProgressBar extends JComponent {

    private static final int BAR_HEIGHT_THIN = 6;
    private static final int BAR_HEIGHT_WITH_TEXT = 20;
    private static final int ARC = 4;

    private float value = 0f;
    private float displayValue = 0f;
    private boolean indeterminate = false;
    private float indeterminatePos = 0f;
    private Timer animTimer;
    private Timer indeterminateTimer;

    private String text = "";
    private boolean stringPainted = false;

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

    /** Texte affiché au centre de la barre (nécessite setStringPainted(true)). */
    public void setString(String text) {
        this.text = text != null ? text : "";
        revalidate();
        repaint();
    }

    public String getString() {
        return text;
    }

    /** Active l'affichage du texte au centre de la barre. */
    public void setStringPainted(boolean painted) {
        this.stringPainted = painted;
        revalidate();
        repaint();
    }

    public boolean isStringPainted() {
        return stringPainted;
    }

    private boolean hasText() {
        return stringPainted && !text.isEmpty();
    }

    private int barHeight() {
        return hasText() ? BAR_HEIGHT_WITH_TEXT : BAR_HEIGHT_THIN;
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
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = barHeight();
        int y = (getHeight() - h) / 2;

        g2.setColor(LammColors.divider());
        g2.fill(new RoundRectangle2D.Float(0, y, w, h, ARC, ARC));

        Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;

        float filledWidth;
        if (indeterminate) {
            float barWidth = w * 0.3f;
            float barX = indeterminatePos * w;
            g2.setClip(new RoundRectangle2D.Float(0, y, w, h, ARC, ARC));
            g2.setColor(accentColor);
            g2.fill(new RoundRectangle2D.Float(barX, y, barWidth, h, ARC, ARC));
            g2.setClip(null);
            filledWidth = 0;
        } else {
            filledWidth = w * displayValue;
            if (filledWidth > 0) {
                g2.setColor(accentColor);
                g2.fill(new RoundRectangle2D.Float(0, y, filledWidth, h, ARC, ARC));
            }
        }

        if (hasText()) {
            g2.setFont(LammFonts.CAPTION.deriveFont(11f));
            var fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textX = (w - textWidth) / 2;
            int textY = y + (h + fm.getAscent() - fm.getDescent()) / 2 - 1;

            // Texte sur fond empty
            g2.setColor(LammColors.textPrimary());
            g2.drawString(text, textX, textY);

            // Texte sur fond rempli (blanc, clippé sur la partie remplie)
            if (filledWidth > 0 || indeterminate) {
                var clip = indeterminate
                        ? new RoundRectangle2D.Float(0, y, w, h, ARC, ARC)
                        : new RoundRectangle2D.Float(0, y, filledWidth, h, ARC, ARC);
                g2.setClip(clip);
                g2.setColor(Color.WHITE);
                g2.drawString(text, textX, textY);
                g2.setClip(null);
            }
        }

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, barHeight() + 8);
    }
}
