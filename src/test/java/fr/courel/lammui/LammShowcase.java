package fr.courel.lammui;

import fr.courel.lammui.component.*;
import fr.courel.lammui.component.LammLabel.Style;
import fr.courel.lammui.theme.LammColors;
import fr.courel.lammui.theme.LammFonts;
import fr.courel.lammui.theme.LammTheme;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * Showcase des composants LammUI.
 */
public class LammShowcase {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var frame = new LammFrame("LammUI Showcase");
            frame.setLayout(new BorderLayout(0, 0));

            // --- Header compact ---
            var header = new LammHeader();
            header.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

            var titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            titlePanel.setOpaque(false);
            titlePanel.add(new LammTitle("UI", 22f));
            header.add(titlePanel, BorderLayout.WEST);

            var themeSwitch = new LammSwitch("Dark");
            // Forcer le texte en blanc sur le header
            themeSwitch.addPropertyChangeListener("selected", e -> {
                LammTheme.toggle();
                themeSwitch.setLabel(LammTheme.isDark() ? "Light" : "Dark");
            });
            var switchWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            switchWrapper.setOpaque(false);
            switchWrapper.add(themeSwitch);
            header.add(switchWrapper, BorderLayout.EAST);

            frame.add(header, BorderLayout.NORTH);

            // --- Contenu ---
            var content = new JPanel();
            content.setOpaque(false);
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setBorder(BorderFactory.createEmptyBorder(20, 32, 20, 32));

            content.add(row(section("Boutons", buttonDemo()), section("Palette", paletteDemo())));
            content.add(Box.createVerticalStrut(12));
            content.add(row(section("Typographie", typoDemo()), section("Dialogs", dialogDemo(frame))));
            content.add(Box.createVerticalStrut(12));
            content.add(row(section("Switch", switchDemo()), section("Checkbox", checkboxDemo())));
            content.add(Box.createVerticalStrut(12));
            content.add(row(section("ComboBox", comboDemo()), section("Spinner", spinnerDemo())));
            content.add(Box.createVerticalStrut(12));
            content.add(row(section("Champs texte", inputDemo()), section("Zone de texte", textAreaDemo())));
            content.add(Box.createVerticalStrut(12));
            content.add(row(section("Progression", progressDemo()), section("Arborescence", treeDemo())));

            var scroll = new LammScrollPane(content);
            frame.add(scroll, BorderLayout.CENTER);

            frame.centerOnScreen(900, 700);
            frame.setVisible(true);
        });
    }

    private static JPanel row(JPanel... cards) {
        var panel = new JPanel(new GridLayout(1, cards.length, 12, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        for (var card : cards) {
            panel.add(card);
        }
        return panel;
    }

    private static JPanel section(String title, JPanel demo) {
        var card = new LammCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        card.setTitle(title);

        demo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(demo);

        return card;
    }

    private static JPanel buttonDemo() {
        var row = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        row.setOpaque(false);
        row.add(new LammButton("Primary"));
        row.add(LammButton.accent("Accent"));
        row.add(LammButton.flat("Flat Button"));
        return row;
    }

    private static JPanel paletteDemo() {
        var row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.add(colorSwatch("Primary", LammColors.PRIMARY));
        row.add(colorSwatch("Light", LammColors.PRIMARY_LIGHT));
        row.add(colorSwatch("Dark", LammColors.PRIMARY_DARK));
        row.add(colorSwatch("Emeraude", LammColors.LAMM_END));
        row.add(colorSwatch("Accent", LammColors.ACCENT));
        row.add(colorSwatch("Error", LammColors.ERROR));
        row.add(colorSwatch("Success", LammColors.SUCCESS));
        return row;
    }

    private static JPanel typoDemo() {
        var col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        for (var s : Style.values()) {
            var label = new LammLabel(
                    s.name().charAt(0) + s.name().substring(1).toLowerCase() + " — Suite Lamm", s);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            col.add(label);
            col.add(Box.createVerticalStrut(4));
        }
        return col;
    }

    private static JPanel inputDemo() {
        var row = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        row.setOpaque(false);
        row.add(new LammTextField("Nom du projet", 15));
        row.add(new LammTextField("Version", 8));
        return row;
    }

    private static JPanel switchDemo() {
        var row = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 0));
        row.setOpaque(false);
        row.add(new LammSwitch("Notifications"));
        row.add(new LammSwitch("Auto-save"));
        var on = new LammSwitch("Activé");
        on.setSelected(true);
        row.add(on);
        return row;
    }

    private static JPanel checkboxDemo() {
        var row = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 0));
        row.setOpaque(false);
        row.add(new LammCheckbox("Option A"));
        var checked = new LammCheckbox("Option B");
        checked.setSelected(true);
        row.add(checked);
        row.add(new LammCheckbox("Option C"));
        return row;
    }

    private static JPanel comboDemo() {
        var row = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        row.setOpaque(false);
        row.add(new LammComboBox<>("Projet", "Lammprimante", "Lammrelease", "Lammvocat"));
        row.add(new LammComboBox<>("Version", "1.0", "1.1", "2.0"));
        return row;
    }

    private static JPanel progressDemo() {
        var col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        // Barre à 65%
        var bar1 = new LammProgressBar();
        bar1.setValue(0.65f);
        bar1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        bar1.setAlignmentX(Component.LEFT_ALIGNMENT);
        col.add(bar1);
        col.add(Box.createVerticalStrut(12));

        // Barre indéterminée
        var bar2 = new LammProgressBar();
        bar2.setIndeterminate(true);
        bar2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        bar2.setAlignmentX(Component.LEFT_ALIGNMENT);
        col.add(bar2);

        return col;
    }

    private static JPanel spinnerDemo() {
        var row = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        row.setOpaque(false);
        row.add(new LammSpinner("Copies", 1, 1, 100, 1));
        row.add(new LammSpinner("Taille du lot", 10, 1, 500, 5));
        return row;
    }

    private static JPanel textAreaDemo() {
        var area = new LammTextArea("Journal", 3, 30);
        area.setText("Lammprimante v1.1 démarré\nChargement de 42 fichiers...\nImpression terminée.");
        area.setEditable(false);
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        area.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        return area;
    }

    private static JPanel dialogDemo(JFrame frame) {
        var row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        row.setOpaque(false);

        var infoBtn = new LammButton("Info");
        infoBtn.addActionListener(e ->
                LammDialog.info(frame, "Information", "L'opération s'est déroulée correctement."));
        row.add(infoBtn);

        var warnBtn = LammButton.accent("Warning");
        warnBtn.addActionListener(e ->
                LammDialog.warning(frame, "Attention", "Cette action est irréversible."));
        row.add(warnBtn);

        var errBtn = new LammButton("Erreur", LammColors.ERROR, Color.WHITE);
        errBtn.addActionListener(e ->
                LammDialog.error(frame, "Erreur", "Impossible de se connecter au serveur."));
        row.add(errBtn);

        var confirmBtn = LammButton.flat("Confirmer");
        confirmBtn.addActionListener(e -> {
            boolean ok = LammDialog.confirm(frame, "Confirmation", "Voulez-vous vraiment supprimer ce fichier ?");
            if (ok) LammDialog.success(frame, "Succès", "Fichier supprimé avec succès.");
        });
        row.add(confirmBtn);

        return row;
    }

    private static JPanel treeDemo() {
        var root = new DefaultMutableTreeNode("Projet");

        var src = new DefaultMutableTreeNode("src");
        var main = new DefaultMutableTreeNode("main");
        main.add(new DefaultMutableTreeNode("App.java"));
        main.add(new DefaultMutableTreeNode("Config.java"));
        src.add(main);
        var test = new DefaultMutableTreeNode("test");
        test.add(new DefaultMutableTreeNode("AppTest.java"));
        src.add(test);
        root.add(src);

        var resources = new DefaultMutableTreeNode("resources");
        resources.add(new DefaultMutableTreeNode("logo.png"));
        resources.add(new DefaultMutableTreeNode("styles.css"));
        root.add(resources);

        root.add(new DefaultMutableTreeNode("pom.xml"));

        var tree = new LammTree(root);
        tree.setRootVisible(true);
        tree.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Expand all
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

        var wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(tree, BorderLayout.CENTER);
        return wrapper;
    }

    private static JPanel colorSwatch(String name, Color color) {
        var panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                var g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(80, 64));

        var label = new JLabel(name, SwingConstants.CENTER);
        label.setFont(LammFonts.CAPTION);
        float brightness = (color.getRed() * 299 + color.getGreen() * 587 + color.getBlue() * 114) / 1000f;
        label.setForeground(brightness < 128 ? Color.WHITE : new Color(0x1A1A2E));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }
}
