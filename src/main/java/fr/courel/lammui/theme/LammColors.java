package fr.courel.lammui.theme;

import java.awt.*;

/**
 * Palette de couleurs de la suite Lamm.
 * Identité bleu-teal + touche violet + accent amber.
 */
public final class LammColors {

    private LammColors() {}

    // --- Primary : Bleu-marine pro ---
    public static final Color PRIMARY_LIGHT  = new Color(0x4A6A90);
    public static final Color PRIMARY        = new Color(0x2A4570);
    public static final Color PRIMARY_DARK   = new Color(0x1A3050);

    // --- Signature : dégradé Lamm ---
    public static final Color LAMM_START     = new Color(0x1F2937);
    public static final Color LAMM_END       = new Color(0x34D399);

    // --- Accent : Bronze doré ---
    public static final Color ACCENT_LIGHT   = new Color(0xD4A96A);
    public static final Color ACCENT         = new Color(0xB8860B);
    public static final Color ACCENT_DARK    = new Color(0x966F09);

    // --- Etats ---
    public static final Color ERROR          = new Color(0xD32F2F);
    public static final Color SUCCESS        = new Color(0x388E3C);

    // --- Couleurs dynamiques ---

    public static Color background()      { return LammTheme.current().background(); }
    public static Color surface()         { return LammTheme.current().surface(); }
    public static Color card()            { return LammTheme.current().card(); }
    public static Color textPrimary()     { return LammTheme.current().textPrimary(); }
    public static Color textSecondary()   { return LammTheme.current().textSecondary(); }
    public static Color textDisabled()    { return LammTheme.current().textDisabled(); }
    public static Color textOnPrimary()   { return LammTheme.current().textOnPrimary(); }
    public static Color textOnAccent()    { return LammTheme.current().textOnAccent(); }
    public static Color divider()         { return LammTheme.current().divider(); }
    public static Color border()          { return LammTheme.current().border(); }
    public static Color ripple()          { return LammTheme.current().ripple(); }
    public static Color fieldBackground() { return LammTheme.current().fieldBackground(); }

    /** Dégradé signature Lamm. */
    public static GradientPaint lammGradient(float x1, float y1, float x2, float y2) {
        return new GradientPaint(x1, y1, LAMM_START, x2, y2, LAMM_END);
    }

    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
