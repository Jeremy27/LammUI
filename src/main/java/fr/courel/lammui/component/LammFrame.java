package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre de base pour les applications de la suite Lamm.
 * Réagit automatiquement aux changements de thème.
 */
public class LammFrame extends JFrame {

    public LammFrame(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(LammColors.background());

        LammTheme.onThemeChange(() -> {
            getContentPane().setBackground(LammColors.background());
            LammTheme.repaintAll(this);
        });
    }

    /**
     * Centre la fenêtre sur l'écran avec la taille donnée.
     */
    public void centerOnScreen(int width, int height) {
        setSize(width, height);
        setLocationRelativeTo(null);
    }
}
