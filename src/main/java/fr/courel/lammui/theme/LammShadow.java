package fr.courel.lammui.theme;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

/**
 * Ombres et élévations style Material Design.
 */
public final class LammShadow {

    private LammShadow() {}

    /** Pas d'élévation. */
    public static Border flat() {
        return new EmptyBorder(0, 0, 0, 0);
    }

    /** Elevation légère (cards au repos). */
    public static Border elevation1() {
        return shadow(new Color(0, 0, 0, 30), 2);
    }

    /** Elevation moyenne (hover, boutons). */
    public static Border elevation2() {
        return shadow(new Color(0, 0, 0, 40), 4);
    }

    /** Elevation forte (dialogs, menus). */
    public static Border elevation3() {
        return shadow(new Color(0, 0, 0, 50), 6);
    }

    private static Border shadow(Color color, int size) {
        return new CompoundBorder(
                new LineBorder(color, 1, true),
                new EmptyBorder(size, size, size, size)
        );
    }
}
