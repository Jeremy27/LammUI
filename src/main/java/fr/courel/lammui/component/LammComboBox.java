package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * ComboBox style Materialize : ligne en bas, label flottant, caret discret.
 */
public class LammComboBox<E> extends JPanel {

    private final JComboBox<E> combo;
    private final String label;
    private boolean focused = false;
    private float focusAnimation = 0f;
    private Timer animTimer;

    @SafeVarargs
    public LammComboBox(String label, E... items) {
        this.label = label;
        setLayout(new BorderLayout());
        setOpaque(false);

        combo = new JComboBox<>(items);
        combo.setFont(LammFonts.BODY);
        combo.setOpaque(false);
        combo.setBorder(BorderFactory.createEmptyBorder(16, 4, 4, 4));
        combo.setUI(new LammComboBoxUI());

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(LammFonts.BODY);
                setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

                Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;
                if (index == -1) {
                    setOpaque(false);
                    setForeground(LammColors.textPrimary());
                } else if (isSelected) {
                    setOpaque(true);
                    setBackground(accentColor);
                    setForeground(Color.WHITE);
                } else {
                    setOpaque(true);
                    setBackground(LammColors.card());
                    setForeground(LammColors.textPrimary());
                }
                return this;
            }
        });

        combo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                focused = true;
                animate(true);
            }
            @Override
            public void focusLost(FocusEvent e) {
                focused = false;
                animate(false);
            }
        });

        add(combo, BorderLayout.CENTER);
    }

    private void animate(boolean in) {
        if (animTimer != null && animTimer.isRunning()) animTimer.stop();
        animTimer = new Timer(12, _ -> {
            float target = in ? 1f : 0f;
            float diff = target - focusAnimation;
            focusAnimation += diff * 0.15f;
            if (Math.abs(diff) < 0.01f) {
                focusAnimation = target;
                animTimer.stop();
            }
            repaint();
        });
        animTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int lineY = h - 2;

        Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;

        // Ligne de base grise
        g2.setColor(LammColors.border());
        g2.fillRect(4, lineY, w - 8, 1);

        // Ligne accent animée depuis le centre
        if (focusAnimation > 0.01f) {
            int lineWidth = w - 8;
            int center = 4 + lineWidth / 2;
            int halfWidth = (int) (lineWidth / 2 * focusAnimation);
            g2.setColor(accentColor);
            g2.fillRect(center - halfWidth, lineY, halfWidth * 2, 2);
        }

        // Label flottant
        float t = 1f; // toujours en haut car il y a toujours une valeur sélectionnée
        float labelSize = 11f;
        Color labelColor = focused ? accentColor : LammColors.textSecondary();

        g2.setColor(labelColor);
        int style = focused ? Font.BOLD : Font.PLAIN;
        g2.setFont(LammFonts.CAPTION.deriveFont(style, labelSize));
        g2.drawString(label, 4, 12);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = combo.getPreferredSize();
        return new Dimension(d.width, d.height + 6);
    }

    public JComboBox<E> getCombo() {
        return combo;
    }

    public E getSelectedItem() {
        return (E) combo.getSelectedItem();
    }

    private static class LammComboBoxUI extends BasicComboBoxUI {
        @Override
        protected ComboPopup createPopup() {
            return new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    var sp = new JScrollPane(list,
                            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    sp.setBorder(null);
                    sp.getVerticalScrollBar().setUI(new LammScrollBarUI());
                    sp.getVerticalScrollBar().setOpaque(false);
                    sp.getVerticalScrollBar().setUnitIncrement(16);
                    return sp;
                }
            };
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {}

        @Override
        public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
            var g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(comboBox.getFont());
            g2.setColor(LammColors.textPrimary());
            var fm = g2.getFontMetrics();
            Object item = comboBox.getSelectedItem();
            String text = item != null ? item.toString() : "";
            g2.drawString(text, bounds.x, bounds.y + (bounds.height + fm.getAscent() - fm.getDescent()) / 2);
        }

        @Override
        protected JButton createArrowButton() {
            var btn = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    var g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(LammColors.textSecondary());
                    int cx = getWidth() / 2;
                    int cy = getHeight() / 2;
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(cx - 4, cy - 2, cx, cy + 2);
                    g2.drawLine(cx, cy + 2, cx + 4, cy - 2);
                    g2.dispose();
                }
            };
            btn.setOpaque(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setPreferredSize(new Dimension(24, 24));
            return btn;
        }
    }
}
