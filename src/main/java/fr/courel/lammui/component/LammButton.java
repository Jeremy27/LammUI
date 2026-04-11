package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;
import fr.courel.lammui.util.RipplePainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Bouton Material Design avec effet ripple et coins arrondis.
 */
public class LammButton extends JButton {

    private static final int ARC = 8;
    private static final int PADDING_H = 28;
    private static final int PADDING_V = 12;

    private Color baseColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color textColor;
    private boolean flat = false;
    private final RipplePainter ripple = new RipplePainter(this);

    private boolean useThemePrimary = false;

    public LammButton(String text) {
        this(text, LammColors.PRIMARY, Color.WHITE);
        this.useThemePrimary = true;
    }

    public LammButton(String text, Color background, Color foreground) {
        super(text);
        this.baseColor = background;
        this.hoverColor = brighter(background, 0.15f);
        this.pressedColor = darker(background, 0.15f);
        this.textColor = foreground;

        setFont(LammFonts.BUTTON);
        setForeground(textColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(PADDING_V, PADDING_H, PADDING_V, PADDING_H));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ripple.start(e.getPoint());
            }
        });
    }

    /** Bouton flat (transparent, texte primary). */
    public static LammButton flat(String text) {
        var btn = new LammButton(text, new Color(0, 0, 0, 0), LammColors.PRIMARY);
        btn.flat = true;
        btn.hoverColor = LammColors.withAlpha(LammColors.PRIMARY, 20);
        btn.pressedColor = LammColors.withAlpha(LammColors.PRIMARY, 40);
        return btn;
    }

    /** Bouton accent (fond amber). */
    public static LammButton accent(String text) {
        return new LammButton(text, LammColors.ACCENT, LammColors.textOnAccent());
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (useThemePrimary) {
            Color primary = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;
            baseColor = primary;
            hoverColor = brighter(primary, 0.15f);
            pressedColor = darker(primary, 0.15f);
        }

        Color bg = getModel().isPressed() ? pressedColor
                 : getModel().isRollover() ? hoverColor
                 : baseColor;

        var shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), ARC, ARC);

        if (bg.getAlpha() > 0) {
            // Ombre légère pour les boutons raised
            if (!flat) {
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fill(new RoundRectangle2D.Float(0, 2, getWidth(), getHeight(), ARC, ARC));
            }
            g2.setColor(bg);
            g2.fill(shape);
        }

        ripple.paint(g2, getWidth(), getHeight(), ARC);
        g2.dispose();

        // Mettre à jour la couleur du texte pour le flat
        if (flat) {
            setForeground(LammColors.PRIMARY);
        }
        super.paintComponent(g);
    }

    private static Color brighter(Color c, float factor) {
        int r = Math.min(255, (int) (c.getRed() + 255 * factor));
        int g = Math.min(255, (int) (c.getGreen() + 255 * factor));
        int b = Math.min(255, (int) (c.getBlue() + 255 * factor));
        return new Color(r, g, b, c.getAlpha());
    }

    private static Color darker(Color c, float factor) {
        int r = Math.max(0, (int) (c.getRed() * (1 - factor)));
        int g = Math.max(0, (int) (c.getGreen() * (1 - factor)));
        int b = Math.max(0, (int) (c.getBlue() * (1 - factor)));
        return new Color(r, g, b, c.getAlpha());
    }
}
