package fr.courel.lammui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

/**
 * Boutons minimiser / maximiser / fermer à intégrer dans un header
 * quand la fenêtre est en mode {@code setUndecorated(true)}.
 */
public class LammWindowControls extends JPanel {

    public LammWindowControls(JFrame frame) {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        add(new ControlButton(ControlButton.Kind.MINIMIZE, () ->
                frame.setExtendedState(frame.getExtendedState() | Frame.ICONIFIED)));
        add(new ControlButton(ControlButton.Kind.MAXIMIZE, () -> {
            int state = frame.getExtendedState();
            if ((state & Frame.MAXIMIZED_BOTH) != 0) {
                frame.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
            } else {
                frame.setMaximizedBounds(GraphicsEnvironment
                        .getLocalGraphicsEnvironment().getMaximumWindowBounds());
                frame.setExtendedState(state | Frame.MAXIMIZED_BOTH);
            }
        }));
        add(new ControlButton(ControlButton.Kind.CLOSE, () ->
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING))));
    }

    private static class ControlButton extends JButton {
        enum Kind { MINIMIZE, MAXIMIZE, CLOSE }

        private final Kind kind;
        private boolean hovered = false;

        ControlButton(Kind kind, Runnable onClick) {
            this.kind = kind;
            setPreferredSize(new Dimension(44, 30));
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addActionListener(_ -> onClick.run());
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            var g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (hovered) {
                g2.setColor(kind == Kind.CLOSE ? new Color(0xE81123) : new Color(255, 255, 255, 60));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;

            switch (kind) {
                case MINIMIZE -> g2.drawLine(cx - 5, cy + 3, cx + 5, cy + 3);
                case MAXIMIZE -> g2.drawRect(cx - 5, cy - 4, 10, 8);
                case CLOSE -> {
                    g2.drawLine(cx - 5, cy - 5, cx + 5, cy + 5);
                    g2.drawLine(cx + 5, cy - 5, cx - 5, cy + 5);
                }
            }
            g2.dispose();
        }
    }
}
