package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Toggle switch Material Design avec animation.
 */
public class LammSwitch extends JComponent {

    private static final int TRACK_WIDTH = 44;
    private static final int TRACK_HEIGHT = 22;
    private static final int THUMB_SIZE = 18;
    private static final int THUMB_PADDING = 2;

    private boolean selected = false;
    private float animation = 0f;
    private Timer animTimer;
    private String label;
    private boolean onGradient = false;

    public LammSwitch() {
        this(null);
    }

    public LammSwitch(String label) {
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

    public void setLabel(String label) {
        this.label = label;
        repaint();
    }

    /** Active un style contrasté pour un placement sur un fond coloré (ex. LammHeader). */
    public void setOnGradient(boolean onGradient) {
        this.onGradient = onGradient;
        repaint();
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

        int offsetX = 0;

        // Couleurs selon contexte (normal ou sur gradient coloré)
        Color labelColor;
        Color trackOff;
        Color trackOn;
        Color thumbOff;
        Color thumbOn;
        Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;

        if (onGradient) {
            labelColor = Color.WHITE;
            trackOff = new Color(255, 255, 255, 70);
            trackOn = new Color(255, 255, 255, 140);
            thumbOff = Color.WHITE;
            thumbOn = Color.WHITE;
        } else {
            labelColor = LammColors.textPrimary();
            trackOff = LammColors.border();
            trackOn = LammColors.withAlpha(accentColor, 120);
            thumbOff = LammColors.surface();
            thumbOn = accentColor;
        }

        // Label
        if (label != null) {
            g2.setFont(LammFonts.BODY);
            g2.setColor(labelColor);
            var fm = g2.getFontMetrics();
            g2.drawString(label, 0, (TRACK_HEIGHT + fm.getAscent() - fm.getDescent()) / 2);
            offsetX = fm.stringWidth(label) + 10;
        }

        int trackY = 0;

        // Track
        Color trackColor = interpolateColor(trackOff, trackOn, animation);
        g2.setColor(trackColor);
        g2.fill(new RoundRectangle2D.Float(offsetX, trackY, TRACK_WIDTH, TRACK_HEIGHT,
                TRACK_HEIGHT, TRACK_HEIGHT));

        // Thumb
        float thumbMinX = offsetX + THUMB_PADDING;
        float thumbMaxX = offsetX + TRACK_WIDTH - THUMB_SIZE - THUMB_PADDING;
        float thumbX = thumbMinX + (thumbMaxX - thumbMinX) * animation;
        float thumbY = trackY + THUMB_PADDING;

        // Ombre du thumb
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fill(new Ellipse2D.Float(thumbX, thumbY + 1, THUMB_SIZE, THUMB_SIZE));

        // Thumb
        Color thumbColor = animation > 0.5f ? thumbOn : thumbOff;
        g2.setColor(thumbColor);
        g2.fill(new Ellipse2D.Float(thumbX, thumbY, THUMB_SIZE, THUMB_SIZE));

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        int labelWidth = 0;
        if (label != null) {
            labelWidth = getFontMetrics(LammFonts.BODY).stringWidth(label) + 10;
        }
        return new Dimension(labelWidth + TRACK_WIDTH + 4, TRACK_HEIGHT + 4);
    }

    private static Color interpolateColor(Color from, Color to, float t) {
        int r = (int) (from.getRed() + (to.getRed() - from.getRed()) * t);
        int g = (int) (from.getGreen() + (to.getGreen() - from.getGreen()) * t);
        int b = (int) (from.getBlue() + (to.getBlue() - from.getBlue()) * t);
        int a = (int) (from.getAlpha() + (to.getAlpha() - from.getAlpha()) * t);
        return new Color(r, g, b, a);
    }
}
