package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;

import javax.swing.*;
import java.awt.*;

/**
 * Label qui s'adapte automatiquement au thème actif.
 */
public class LammLabel extends JLabel {

    public enum Style { TITLE, SUBTITLE, BODY, CAPTION }

    private final boolean secondary;

    public LammLabel(String text) {
        this(text, Style.BODY, false);
    }

    public LammLabel(String text, Style style) {
        this(text, style, false);
    }

    public LammLabel(String text, Style style, boolean secondary) {
        super(text);
        this.secondary = secondary;
        setFont(switch (style) {
            case TITLE -> LammFonts.TITLE;
            case SUBTITLE -> LammFonts.SUBTITLE;
            case BODY -> LammFonts.BODY;
            case CAPTION -> LammFonts.CAPTION;
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        setForeground(secondary ? LammColors.textSecondary() : LammColors.textPrimary());
        super.paintComponent(g);
    }
}
