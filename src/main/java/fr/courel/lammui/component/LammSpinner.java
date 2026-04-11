package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Spinner style Materialize : ligne en bas, label flottant, boutons +/- discrets.
 */
public class LammSpinner extends JPanel {

    private final JSpinner spinner;
    private final String label;
    private boolean focused = false;
    private float focusAnimation = 0f;
    private Timer animTimer;

    public LammSpinner(String label, int value, int min, int max, int step) {
        this.label = label;
        setLayout(new BorderLayout(4, 0));
        setOpaque(false);

        spinner = new JSpinner(new SpinnerNumberModel(value, min, max, step));
        spinner.setFont(LammFonts.BODY);
        spinner.setOpaque(false);
        spinner.setBorder(null);

        // Retirer les boutons natifs
        for (Component c : spinner.getComponents()) {
            if (c instanceof JButton) {
                spinner.remove(c);
            }
        }

        // Styler l'éditeur
        var editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.setOpaque(false);
        var textField = editor.getTextField();
        textField.setFont(LammFonts.BODY);
        textField.setOpaque(false);
        textField.setBackground(new Color(0, 0, 0, 0));
        textField.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        textField.setHorizontalAlignment(JTextField.LEFT);
        textField.setColumns(3);

        textField.addFocusListener(new FocusAdapter() {
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

        // Boutons +/- custom
        var btnPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(createArrowButton(true));
        btnPanel.add(createArrowButton(false));

        // Wrapper spinner + flèches, sous le label
        var inputRow = new JPanel(new BorderLayout(2, 0));
        inputRow.setOpaque(false);
        inputRow.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        inputRow.add(spinner, BorderLayout.CENTER);
        inputRow.add(btnPanel, BorderLayout.EAST);

        add(inputRow, BorderLayout.CENTER);
    }

    private JComponent createArrowButton(boolean up) {
        var btn = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                var g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(LammColors.textSecondary());
                g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                if (up) {
                    g2.drawLine(cx - 3, cy + 1, cx, cy - 2);
                    g2.drawLine(cx, cy - 2, cx + 3, cy + 1);
                } else {
                    g2.drawLine(cx - 3, cy - 1, cx, cy + 2);
                    g2.drawLine(cx, cy + 2, cx + 3, cy - 1);
                }
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(16, 10);
            }
        };
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    spinner.setValue(up
                            ? spinner.getNextValue()
                            : spinner.getPreviousValue());
                } catch (Exception ignored) {}
            }
        });
        return btn;
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

        // Label
        Color labelColor = focused ? accentColor : LammColors.textSecondary();
        int style = focused ? Font.BOLD : Font.PLAIN;
        g2.setColor(labelColor);
        g2.setFont(LammFonts.CAPTION.deriveFont(style, 11f));
        g2.drawString(label, 4, 12);

        // Couleur du texte
        var editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setForeground(LammColors.textPrimary());
        editor.getTextField().setCaretColor(accentColor);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        var editor = (JSpinner.DefaultEditor) spinner.getEditor();
        int w = editor.getTextField().getPreferredSize().width + 24;
        return new Dimension(w, 40);
    }

    public int getValue() {
        return (int) spinner.getValue();
    }

    public void setValue(int value) {
        spinner.setValue(value);
    }

    public JSpinner getSpinner() {
        return spinner;
    }
}
