package fr.courel.lammui.component;

import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Tree stylisé avec le thème Lamm : fond transparent, sélection accent,
 * icônes géométriques et typographie cohérente.
 */
public class LammTree extends JTree {

    private static final int ICON_SIZE = 14;

    public LammTree(TreeModel model) {
        super(model);
        init();
    }

    public LammTree(TreeNode root) {
        super(root);
        init();
    }

    private void init() {
        setOpaque(false);
        setFont(LammFonts.BODY);
        setRowHeight(28);
        setShowsRootHandles(true);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setCellRenderer(new LammTreeCellRenderer());
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Peindre les lignes de sélection sur toute la largeur
        var g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int[] selectedRows = getSelectionRows();
        if (selectedRows != null) {
            Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;
            g2.setColor(LammColors.withAlpha(accentColor, 30));
            for (int row : selectedRows) {
                Rectangle bounds = getRowBounds(row);
                if (bounds != null) {
                    g2.fillRoundRect(0, bounds.y, getWidth(), bounds.height, 6, 6);
                }
            }
        }
        g2.dispose();

        super.paintComponent(g);
    }

    private static class LammTreeCellRenderer extends DefaultTreeCellRenderer {

        LammTreeCellRenderer() {
            setOpaque(false);
            setBorderSelectionColor(null);
            setBackgroundSelectionColor(new Color(0, 0, 0, 0));
            setBackgroundNonSelectionColor(new Color(0, 0, 0, 0));
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            setFont(LammFonts.BODY);

            Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;
            setForeground(sel ? accentColor : LammColors.textPrimary());

            if (leaf) {
                setIcon(new FileIcon());
            } else {
                setIcon(expanded ? new FolderOpenIcon() : new FolderIcon());
            }

            return this;
        }
    }

    /** Icône fichier : petit document stylisé. */
    private static class FileIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            var g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = ICON_SIZE, h = ICON_SIZE;
            int fold = 4;

            var shape = new Path2D.Float();
            shape.moveTo(x + 1, y);
            shape.lineTo(x + w - fold, y);
            shape.lineTo(x + w - 1, y + fold);
            shape.lineTo(x + w - 1, y + h);
            shape.lineTo(x + 1, y + h);
            shape.closePath();

            g2.setColor(LammColors.textSecondary());
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(shape);

            // Lignes de texte
            g2.setStroke(new BasicStroke(0.8f));
            for (int ly = y + 5; ly < y + h - 2; ly += 3) {
                g2.drawLine(x + 4, ly, x + w - 4, ly);
            }

            g2.dispose();
        }

        @Override public int getIconWidth() { return ICON_SIZE; }
        @Override public int getIconHeight() { return ICON_SIZE; }
    }

    /** Icône dossier fermé. */
    private static class FolderIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            var g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;
            int w = ICON_SIZE, h = ICON_SIZE;

            // Tab du dossier
            var shape = new Path2D.Float();
            shape.moveTo(x + 1, y + 2);
            shape.lineTo(x + 5, y + 2);
            shape.lineTo(x + 7, y + 4);
            shape.lineTo(x + w - 1, y + 4);
            shape.lineTo(x + w - 1, y + h);
            shape.lineTo(x + 1, y + h);
            shape.closePath();

            g2.setColor(LammColors.withAlpha(accentColor, 40));
            g2.fill(shape);
            g2.setColor(accentColor);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(shape);

            g2.dispose();
        }

        @Override public int getIconWidth() { return ICON_SIZE; }
        @Override public int getIconHeight() { return ICON_SIZE; }
    }

    /** Icône dossier ouvert. */
    private static class FolderOpenIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            var g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color accentColor = LammTheme.isDark() ? LammColors.LAMM_END : LammColors.PRIMARY;
            int w = ICON_SIZE, h = ICON_SIZE;

            // Tab du dossier
            var tab = new Path2D.Float();
            tab.moveTo(x + 1, y + 2);
            tab.lineTo(x + 5, y + 2);
            tab.lineTo(x + 7, y + 4);
            tab.lineTo(x + w - 1, y + 4);
            tab.lineTo(x + w - 1, y + 7);
            tab.lineTo(x + 1, y + 7);
            tab.closePath();

            g2.setColor(LammColors.withAlpha(accentColor, 60));
            g2.fill(tab);

            // Corps ouvert (décalé)
            var body = new Path2D.Float();
            body.moveTo(x + 3, y + 7);
            body.lineTo(x + w + 1, y + 7);
            body.lineTo(x + w - 1, y + h);
            body.lineTo(x + 1, y + h);
            body.closePath();

            g2.setColor(LammColors.withAlpha(accentColor, 40));
            g2.fill(body);
            g2.setColor(accentColor);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(tab);
            g2.draw(body);

            g2.dispose();
        }

        @Override public int getIconWidth() { return ICON_SIZE; }
        @Override public int getIconHeight() { return ICON_SIZE; }
    }
}
