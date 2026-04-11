package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;

import javax.swing.*;
import java.awt.*;

/**
 * Header signature avec dégradé Lamm (bleu-teal -> violet).
 */
public class LammHeader extends JPanel {

    public LammHeader() {
        super(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(LammColors.lammGradient(0, 0, getWidth(), 0));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
