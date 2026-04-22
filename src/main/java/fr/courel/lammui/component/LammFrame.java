package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Fenêtre de base pour les applications de la suite Lamm.
 * Réagit automatiquement aux changements de thème.
 *
 * <p>Pour retirer la barre de titre Windows native et fournir une titlebar
 * custom : appeler {@link #useCustomTitleBar()} dans le constructeur de la
 * sous-classe <em>avant</em> d'ajouter du contenu, puis placer un
 * {@link LammWindowControls} dans le header et appeler {@link #makeDraggable}
 * sur le composant qui sert de zone de drag.</p>
 */
public class LammFrame extends JFrame {

    private static final int RESIZE_BORDER = 6;

    public LammFrame(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(LammColors.background());

        LammTheme.onThemeChange(() -> {
            getContentPane().setBackground(LammColors.background());
            LammTheme.repaintAll(this);
        });
    }

    public void centerOnScreen(int width, int height) {
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    /**
     * Retire la décoration Windows/Linux et installe un glassPane qui
     * capte uniquement les quelques pixels de bord pour le resize,
     * laissant le contenu affiché edge-to-edge.
     */
    public void useCustomTitleBar() {
        setUndecorated(true);
        var glass = new ResizeGlassPane();
        setGlassPane(glass);
        glass.setVisible(true);
    }

    /**
     * Rend un composant (typiquement le header) draggable pour déplacer la fenêtre.
     */
    public void makeDraggable(Component source) {
        var adapter = new MouseAdapter() {
            Point pressScreen;
            Point frameOrigin;

            @Override
            public void mousePressed(MouseEvent e) {
                pressScreen = new Point(e.getPoint());
                SwingUtilities.convertPointToScreen(pressScreen, source);
                frameOrigin = getLocation();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (pressScreen == null) return;
                if ((getExtendedState() & MAXIMIZED_BOTH) != 0) return;
                Point p = new Point(e.getPoint());
                SwingUtilities.convertPointToScreen(p, source);
                setLocation(frameOrigin.x + p.x - pressScreen.x,
                        frameOrigin.y + p.y - pressScreen.y);
            }
        };
        source.addMouseListener(adapter);
        source.addMouseMotionListener(adapter);
    }

    private class ResizeGlassPane extends JComponent {
        ResizeGlassPane() {
            setOpaque(false);
            var handler = new ResizeHandler();
            addMouseListener(handler);
            addMouseMotionListener(handler);
        }

        @Override
        public boolean contains(int x, int y) {
            if ((getExtendedState() & MAXIMIZED_BOTH) != 0) return false;
            return x < RESIZE_BORDER || x > getWidth() - RESIZE_BORDER
                    || y < RESIZE_BORDER || y > getHeight() - RESIZE_BORDER;
        }
    }

    private class ResizeHandler extends MouseAdapter {
        private static final int N = 1, S = 2, W = 4, E = 8;

        private Rectangle startBounds;
        private Point pressScreen;
        private int activeEdge = 0;

        private int edgeAt(Point p, Dimension size) {
            int edge = 0;
            if (p.y <= RESIZE_BORDER) edge |= N;
            if (p.y >= size.height - RESIZE_BORDER) edge |= S;
            if (p.x <= RESIZE_BORDER) edge |= W;
            if (p.x >= size.width - RESIZE_BORDER) edge |= E;
            return edge;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Component src = e.getComponent();
            src.setCursor(cursorFor(edgeAt(e.getPoint(), src.getSize())));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (activeEdge == 0) e.getComponent().setCursor(Cursor.getDefaultCursor());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            activeEdge = edgeAt(e.getPoint(), e.getComponent().getSize());
            if (activeEdge == 0) return;
            startBounds = getBounds();
            pressScreen = new Point(e.getPoint());
            SwingUtilities.convertPointToScreen(pressScreen, e.getComponent());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (activeEdge == 0) return;
            Point p = new Point(e.getPoint());
            SwingUtilities.convertPointToScreen(p, e.getComponent());
            int dx = p.x - pressScreen.x;
            int dy = p.y - pressScreen.y;

            int x = startBounds.x, y = startBounds.y;
            int w = startBounds.width, h = startBounds.height;
            Dimension min = getMinimumSize();

            if ((activeEdge & N) != 0) {
                int newH = startBounds.height - dy;
                if (newH >= min.height) { y = startBounds.y + dy; h = newH; }
            }
            if ((activeEdge & S) != 0) {
                h = Math.max(min.height, startBounds.height + dy);
            }
            if ((activeEdge & W) != 0) {
                int newW = startBounds.width - dx;
                if (newW >= min.width) { x = startBounds.x + dx; w = newW; }
            }
            if ((activeEdge & E) != 0) {
                w = Math.max(min.width, startBounds.width + dx);
            }
            setBounds(x, y, w, h);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            activeEdge = 0;
        }

        private Cursor cursorFor(int edge) {
            return switch (edge) {
                case N -> Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
                case S -> Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
                case W -> Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
                case E -> Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
                case N | W -> Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
                case N | E -> Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
                case S | W -> Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
                case S | E -> Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
                default -> Cursor.getDefaultCursor();
            };
        }
    }
}
