package fr.courel.lammui.theme;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 * Typographie de la suite Lamm.
 * Utilise Roboto si disponible, sinon fallback sur les fonts système.
 */
public final class LammFonts {

    private LammFonts() {}

    private static final String FAMILY = resolveFamily();

    public static final Font TITLE    = new Font(FAMILY, Font.BOLD, 20);
    public static final Font SUBTITLE = new Font(FAMILY, Font.BOLD, 16);
    public static final Font BODY     = new Font(FAMILY, Font.PLAIN, 14);
    public static final Font CAPTION  = new Font(FAMILY, Font.PLAIN, 12);
    public static final Font BUTTON   = new Font(FAMILY, Font.BOLD, 14);

    private static String resolveFamily() {
        var ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // Ordre de préférence : Inter > Poppins > Nunito > Roboto > Quicksand
        for (String preferred : new String[]{"Inter", "Poppins", "Nunito", "Roboto", "Quicksand", "Quicksand Medium"}) {
            for (String name : ge.getAvailableFontFamilyNames()) {
                if (name.equalsIgnoreCase(preferred)) return name;
            }
        }
        return Font.SANS_SERIF;
    }
}
