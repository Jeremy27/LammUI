package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Titre signature : "Lamm" en bold + suffixe en light.
 * S'adapte au thème (couleur primary en light, émeraude en dark).
 */
public class LammTitle extends JComponent {

    private final String suffix;
    private final float fontSize;

    public LammTitle(String suffix) {
        this(suffix, 26f);
    }

    public LammTitle(String suffix, float fontSize) {
        this.suffix = suffix;
        this.fontSize = fontSize;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String prefix = "Lamm";
        Font boldFont = LammFonts.TITLE.deriveFont(Font.BOLD, fontSize);
        Font lightFont = LammFonts.TITLE.deriveFont(Font.PLAIN, fontSize);

        var fmBold = g2.getFontMetrics(boldFont);
        int prefixWidth = fmBold.stringWidth(prefix);
        int y = fmBold.getAscent() + 2;

        Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;

        // "Lamm" en bold accent
        g2.setFont(boldFont);
        g2.setColor(accentColor);
        g2.drawString(prefix, 0, y);

        // Suffixe en texte secondaire
        g2.setFont(lightFont);
        g2.setColor(LammColors.textSecondary());
        g2.drawString(suffix, prefixWidth, y);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        var fm = getFontMetrics(LammFonts.TITLE.deriveFont(Font.BOLD, fontSize));
        return new Dimension(
                fm.stringWidth("Lamm" + suffix) + 4,
                fm.getHeight() + 4);
    }
}
