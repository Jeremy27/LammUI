package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Card panel avec barre accent et titre.
 */
public class LammCard extends JPanel {

    private static final int ARC = 8;
    private static final int ACCENT_WIDTH = 2;

    private String title;

    public LammCard() {
        this(new BorderLayout());
    }

    public LammCard(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        updateBorder();
    }

    public void setTitle(String title) {
        this.title = title;
        updateBorder();
        repaint();
    }

    private void updateBorder() {
        int top = title != null ? 48 : 16;
        setBorder(BorderFactory.createEmptyBorder(top, 16 + ACCENT_WIDTH + 8, 16, 16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Fond arrondi à droite, bord gauche droit pour la barre accent
        var shape = new Path2D.Float();
        shape.moveTo(0, 0);
        shape.lineTo(w - ARC, 0);
        shape.quadTo(w, 0, w, ARC);
        shape.lineTo(w, h - ARC);
        shape.quadTo(w, h, w - ARC, h);
        shape.lineTo(0, h);
        shape.closePath();

        g2.setColor(LammColors.surface());
        g2.fill(shape);

        // Barre accent : bleu en light, émeraude en dark
        g2.setColor(LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY);
        g2.fillRect(0, 0, ACCENT_WIDTH, h);

        // Titre + séparateur
        if (title != null) {
            int textX = ACCENT_WIDTH + 12;
            int textY = 24;
            int sepY = 34;

            g2.setFont(LammFonts.SUBTITLE);
            g2.setColor(LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY);
            g2.drawString(title, textX, textY);

            g2.setColor(LammColors.divider());
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(ACCENT_WIDTH, sepY, w - 16, sepY);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
