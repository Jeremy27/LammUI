package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Scrollbar fine stylisée au thème Lamm, s'épaissit au hover.
 * Applicable à tout JScrollBar via {@code scrollBar.setUI(new LammScrollBarUI())}.
 */
public class LammScrollBarUI extends BasicScrollBarUI {

    public static final int THUMB_WIDTH = 6;
    public static final int THUMB_WIDTH_HOVER = 8;
    private static final int THUMB_ARC = 6;

    private boolean hovered = false;

    @Override
    protected void installListeners() {
        super.installListeners();
        scrollbar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                scrollbar.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                scrollbar.repaint();
            }
        });
    }

    @Override
    protected void configureScrollBarColors() {}

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return invisibleButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return invisibleButton();
    }

    private static JButton invisibleButton() {
        var btn = new JButton();
        btn.setPreferredSize(new Dimension(0, 0));
        btn.setMinimumSize(new Dimension(0, 0));
        btn.setMaximumSize(new Dimension(0, 0));
        return btn;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {}

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;

        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (LammTheme.isDark()) {
            g2.setColor(hovered ? new Color(0x4B5563) : new Color(0x374151));
        } else {
            g2.setColor(LammColors.withAlpha(LammColors.PRIMARY, hovered ? 120 : 50));
        }

        int width = hovered ? THUMB_WIDTH_HOVER : THUMB_WIDTH;

        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            int x = thumbBounds.x + thumbBounds.width - width - 2;
            g2.fill(new RoundRectangle2D.Float(x, thumbBounds.y + 2,
                    width, thumbBounds.height - 4, THUMB_ARC, THUMB_ARC));
        } else {
            int y = thumbBounds.y + thumbBounds.height - width - 2;
            g2.fill(new RoundRectangle2D.Float(thumbBounds.x + 2, y,
                    thumbBounds.width - 4, width, THUMB_ARC, THUMB_ARC));
        }

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            return new Dimension(THUMB_WIDTH_HOVER + 4, super.getPreferredSize(c).height);
        } else {
            return new Dimension(super.getPreferredSize(c).width, THUMB_WIDTH_HOVER + 4);
        }
    }
}
