package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

/**
 * Radio button Material Design avec animation du point central.
 * Utiliser avec {@link LammRadioGroup} pour l'exclusivité.
 */
public class LammRadioButton extends JComponent {

    private static final int BOX_SIZE = 20;
    private static final int GAP = 10;

    private boolean selected;
    private float animation;
    private Timer animTimer;
    private final String label;

    public LammRadioButton(String label) {
        this(label, false);
    }

    public LammRadioButton(String label, boolean selected) {
        this.label = label;
        this.selected = selected;
        this.animation = selected ? 1f : 0f;
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!LammRadioButton.this.selected) setSelected(true);
            }
        });
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (this.selected == selected) return;
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

        // Bordure
        Color borderColor = animation > 0.5f ? accentColor : LammColors.border();
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new Ellipse2D.Float(0.5f, boxY + 0.5f, BOX_SIZE - 1, BOX_SIZE - 1));

        // Point central animé
        if (animation > 0.01f) {
            float innerSize = (BOX_SIZE - 8) * animation;
            float innerX = (BOX_SIZE - innerSize) / 2f;
            float innerY = boxY + (BOX_SIZE - innerSize) / 2f;
            g2.setColor(accentColor);
            g2.fill(new Ellipse2D.Float(innerX, innerY, innerSize, innerSize));
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
