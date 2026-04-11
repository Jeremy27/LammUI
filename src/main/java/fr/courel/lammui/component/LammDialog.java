package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Dialog modal stylisé avec le thème Lamm.
 * Fond overlay assombri, card centrée avec coins arrondis.
 */
public class LammDialog extends JDialog {

    private static final int ARC = 12;
    private static final int MIN_WIDTH = 340;

    public enum Type { INFO, WARNING, ERROR, SUCCESS }

    private final Type type;
    private final String message;

    private LammDialog(Window owner, String title, String message, Type type) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        this.type = type;
        this.message = message;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(owner.getWidth(), owner.getHeight());
        setLocationRelativeTo(owner);

        setContentPane(new OverlayPanel(captureOwner(owner)));
    }

    /** Affiche un dialog info. */
    public static void info(Window owner, String title, String message) {
        show(owner, title, message, Type.INFO);
    }

    /** Affiche un dialog warning. */
    public static void warning(Window owner, String title, String message) {
        show(owner, title, message, Type.WARNING);
    }

    /** Affiche un dialog erreur. */
    public static void error(Window owner, String title, String message) {
        show(owner, title, message, Type.ERROR);
    }

    /** Affiche un dialog succès. */
    public static void success(Window owner, String title, String message) {
        show(owner, title, message, Type.SUCCESS);
    }

    /** Affiche un dialog de confirmation. Retourne true si confirmé. */
    public static boolean confirm(Window owner, String title, String message) {
        var dialog = new LammConfirmDialog(owner, title, message);
        dialog.setVisible(true);
        return dialog.confirmed;
    }

    private static void show(Window owner, String title, String message, Type type) {
        var dialog = new LammDialog(owner, title, message, type);
        dialog.setVisible(true);
    }

    private Color typeColor() {
        return switch (type) {
            case INFO -> LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;
            case WARNING -> LammColors.ACCENT;
            case ERROR -> LammColors.ERROR;
            case SUCCESS -> LammColors.SUCCESS;
        };
    }

    private class OverlayPanel extends JPanel {

        private final BufferedImage snapshot;

        OverlayPanel(BufferedImage snapshot) {
            this.snapshot = snapshot;
            setOpaque(true);
            setLayout(new GridBagLayout());

            var card = new CardPanel();
            add(card, new GridBagConstraints());

            // Clic sur l'overlay (hors card) ferme le dialog
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (!card.getBounds().contains(e.getPoint())) {
                        dispose();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Fond = screenshot de la fenêtre parent + overlay sombre
            g.drawImage(snapshot, 0, 0, null);
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private class CardPanel extends JPanel {

        CardPanel() {
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(24, 28, 20, 28));

            // Titre
            var titleLabel = new JLabel(getTitle());
            titleLabel.setFont(LammFonts.SUBTITLE);
            titleLabel.setAlignmentX(LEFT_ALIGNMENT);
            add(titleLabel);
            add(Box.createVerticalStrut(12));

            // Message
            var msgLabel = new JLabel("<html><body style='width: 260px'>" + message + "</body></html>");
            msgLabel.setFont(LammFonts.BODY);
            msgLabel.setAlignmentX(LEFT_ALIGNMENT);
            add(msgLabel);
            add(Box.createVerticalStrut(20));

            // Bouton OK
            var btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            btnPanel.setOpaque(false);
            btnPanel.setAlignmentX(LEFT_ALIGNMENT);
            var okBtn = new LammButton("OK", typeColor(), Color.WHITE);
            okBtn.addActionListener(e -> dispose());
            btnPanel.add(okBtn);
            add(btnPanel);
        }

        @Override
        protected void paintComponent(Graphics g) {
            var g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            var shape = dialogShape(w, h);

            // Ombre
            g2.translate(2, 4);
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fill(dialogShape(w, h));
            g2.translate(-2, -4);

            // Fond
            g2.setColor(LammColors.card());
            g2.fill(shape);

            // Barre accent en haut
            g2.setClip(shape);
            g2.setColor(typeColor());
            g2.fillRect(0, 0, w, 3);
            g2.setClip(null);

            g2.dispose();

            // Couleurs du texte
            for (Component c : getComponents()) {
                if (c instanceof JLabel label) {
                    label.setForeground(LammColors.textPrimary());
                }
            }

            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(Math.max(d.width, MIN_WIDTH), d.height);
        }
    }

    /** Capture le contenu de la fenêtre parent en image. */
    private static BufferedImage captureOwner(Window owner) {
        var img = new BufferedImage(owner.getWidth(), owner.getHeight(), BufferedImage.TYPE_INT_ARGB);
        owner.paint(img.getGraphics());
        return img;
    }

    /** Shape avec coins arrondis en bas, droits en haut. */
    private static Shape dialogShape(int w, int h) {
        var shape = new Path2D.Float();
        shape.moveTo(0, 0);
        shape.lineTo(w, 0);
        shape.lineTo(w, h - ARC);
        shape.quadTo(w, h, w - ARC, h);
        shape.lineTo(ARC, h);
        shape.quadTo(0, h, 0, h - ARC);
        shape.closePath();
        return shape;
    }

    /** Variante confirm avec 2 boutons. */
    private static class LammConfirmDialog extends JDialog {

        boolean confirmed = false;

        LammConfirmDialog(Window owner, String title, String message) {
            super(owner, title, ModalityType.APPLICATION_MODAL);
            setUndecorated(true);
            setBackground(new Color(0, 0, 0, 0));
            setSize(owner.getWidth(), owner.getHeight());
            setLocationRelativeTo(owner);

            var snapshot = captureOwner(owner);
            var overlay = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    g.drawImage(snapshot, 0, 0, null);
                    g.setColor(new Color(0, 0, 0, 180));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            overlay.setOpaque(true);
            overlay.setLayout(new GridBagLayout());

            var card = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    var g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int w = getWidth(), h = getHeight();
                    var shape = dialogShape(w, h);
                    g2.translate(2, 4);
                    g2.setColor(new Color(0, 0, 0, 40));
                    g2.fill(dialogShape(w, h));
                    g2.translate(-2, -4);
                    g2.setColor(LammColors.card());
                    g2.fill(shape);
                    Color accent = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;
                    g2.setClip(shape);
                    g2.setColor(accent);
                    g2.fillRect(0, 0, w, 3);
                    g2.dispose();
                    super.paintComponent(g);
                }

                @Override
                public Dimension getPreferredSize() {
                    Dimension d = super.getPreferredSize();
                    return new Dimension(Math.max(d.width, MIN_WIDTH), d.height);
                }
            };
            card.setOpaque(false);
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(24, 28, 20, 28));

            var titleLabel = new JLabel(title);
            titleLabel.setFont(LammFonts.SUBTITLE);
            titleLabel.setForeground(LammColors.textPrimary());
            titleLabel.setAlignmentX(LEFT_ALIGNMENT);
            card.add(titleLabel);
            card.add(Box.createVerticalStrut(12));

            var msgLabel = new JLabel("<html><body style='width: 260px'>" + message + "</body></html>");
            msgLabel.setFont(LammFonts.BODY);
            msgLabel.setForeground(LammColors.textPrimary());
            msgLabel.setAlignmentX(LEFT_ALIGNMENT);
            card.add(msgLabel);
            card.add(Box.createVerticalStrut(20));

            var btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            btnPanel.setOpaque(false);
            btnPanel.setAlignmentX(LEFT_ALIGNMENT);

            var cancelBtn = LammButton.flat("Annuler");
            cancelBtn.addActionListener(e -> dispose());
            btnPanel.add(cancelBtn);

            Color accent = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;
            var confirmBtn = new LammButton("Confirmer", accent, Color.WHITE);
            confirmBtn.addActionListener(e -> {
                confirmed = true;
                dispose();
            });
            btnPanel.add(confirmBtn);
            card.add(btnPanel);

            overlay.add(card, new GridBagConstraints());

            overlay.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (!card.getBounds().contains(e.getPoint())) {
                        dispose();
                    }
                }
            });

            setContentPane(overlay);
        }
    }
}
