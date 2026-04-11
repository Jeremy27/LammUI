package fr.courel.lammui.theme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire de thème Lamm (light/dark).
 * Permet de switcher à chaud et notifie les composants.
 */
public sealed interface LammTheme permits LammTheme.Light, LammTheme.Dark {

    Color background();
    Color surface();
    Color card();
    Color textPrimary();
    Color textSecondary();
    Color textDisabled();
    Color textOnPrimary();
    Color textOnAccent();
    Color divider();
    Color border();
    Color ripple();
    Color fieldBackground();

    // --- Singleton et switch ---

    List<Runnable> listeners = new ArrayList<>();
    LammTheme[] holder = { new Light() };

    static LammTheme current() {
        return holder[0];
    }

    static boolean isDark() {
        return holder[0] instanceof Dark;
    }

    static void setTheme(LammTheme theme) {
        holder[0] = theme;
        applyDefaults();
        listeners.forEach(Runnable::run);
    }

    static void toggle() {
        setTheme(isDark() ? new Light() : new Dark());
    }

    static void onThemeChange(Runnable listener) {
        listeners.add(listener);
    }

    private static void applyDefaults() {
        var theme = current();
        UIManager.put("Panel.background", theme.background());
        UIManager.put("Label.font", LammFonts.BODY);
        UIManager.put("TextField.font", LammFonts.BODY);
        UIManager.put("ComboBox.font", LammFonts.BODY);
    }

    /**
     * Repeint récursivement tous les composants d'une fenêtre
     * en leur appliquant les couleurs du thème actif.
     */
    static void repaintAll(Window window) {
        applyThemeRecursive(window);
        window.repaint();
    }

    private static void applyThemeRecursive(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof Container c) {
                applyThemeRecursive(c);
            }
            comp.repaint();
        }
    }

    // --- Light ---

    record Light() implements LammTheme {
        @Override public Color background()      { return new Color(0xF0F1F3); }
        @Override public Color surface()          { return new Color(0xFFFFFF); }
        @Override public Color card()             { return new Color(0xFFFFFF); }
        @Override public Color textPrimary()      { return new Color(0x1A1A2E); }
        @Override public Color textSecondary()    { return new Color(0x6B7280); }
        @Override public Color textDisabled()     { return new Color(0xBDBDBD); }
        @Override public Color textOnPrimary()    { return Color.WHITE; }
        @Override public Color textOnAccent()     { return new Color(0x1A1A2E); }
        @Override public Color divider()          { return new Color(0xE5E7EB); }
        @Override public Color border()           { return new Color(0xD1D5DB); }
        @Override public Color ripple()           { return new Color(255, 255, 255, 80); }
        @Override public Color fieldBackground()  { return new Color(0xF9FAFB); }
    }

    // --- Dark ---

    record Dark() implements LammTheme {
        @Override public Color background()      { return new Color(0x0F172A); }
        @Override public Color surface()          { return new Color(0x1E293B); }
        @Override public Color card()             { return new Color(0x1E293B); }
        @Override public Color textPrimary()      { return new Color(0xF1F5F9); }
        @Override public Color textSecondary()    { return new Color(0x94A3B8); }
        @Override public Color textDisabled()     { return new Color(0x475569); }
        @Override public Color textOnPrimary()    { return Color.WHITE; }
        @Override public Color textOnAccent()     { return new Color(0x0F172A); }
        @Override public Color divider()          { return new Color(0x334155); }
        @Override public Color border()           { return new Color(0x475569); }
        @Override public Color ripple()           { return new Color(255, 255, 255, 30); }
        @Override public Color fieldBackground()  { return new Color(0x0F172A); }
    }
}
