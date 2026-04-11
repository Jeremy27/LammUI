package fr.courel.lammui.util;

import fr.courel.lammui.theme.LammColors;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Effet ripple Material Design pour les composants interactifs.
 */
public class RipplePainter {

    private final JComponent target;
    private Point origin;
    private float progress = 0f;
    private float alpha = 0f;
    private Timer timer;

    public RipplePainter(JComponent target) {
        this.target = target;
    }

    public void start(Point point) {
        this.origin = point;
        this.progress = 0f;
        this.alpha = 1f;

        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        timer = new Timer(16, _ -> {
            progress += 0.08f;
            alpha = Math.max(0f, 1f - progress);

            if (progress >= 1f) {
                timer.stop();
                progress = 0f;
                alpha = 0f;
            }
            target.repaint();
        });
        timer.start();
    }

    public void paint(Graphics2D g2, int width, int height, int arc) {
        if (origin == null || alpha <= 0f) return;

        float maxRadius = (float) Math.hypot(width, height);
        float radius = maxRadius * progress;

        var clip = new RoundRectangle2D.Float(0, 0, width, height, arc, arc);
        Shape oldClip = g2.getClip();
        g2.clip(clip);

        g2.setColor(LammColors.withAlpha(LammColors.ripple(), (int) (80 * alpha)));
        g2.fill(new Ellipse2D.Float(origin.x - radius, origin.y - radius, radius * 2, radius * 2));

        g2.setClip(oldClip);
    }
}
