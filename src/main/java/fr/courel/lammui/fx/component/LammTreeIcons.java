package fr.courel.lammui.fx.component;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/** Fabriques d'icônes vectorielles 14×14 utilisées par LammTreeFx. */
public final class LammTreeIcons {

    private LammTreeIcons() {}

    public static Node file() {
        var outline = new Path(
            new MoveTo(1, 0), new LineTo(10, 0), new LineTo(13, 4),
            new LineTo(13, 14), new LineTo(1, 14), new ClosePath()
        );
        outline.getStyleClass().add("lamm-tree-icon-file-outline");
        var l1 = new Line(4, 5, 10, 5);
        var l2 = new Line(4, 8, 10, 8);
        var l3 = new Line(4, 11, 10, 11);
        l1.getStyleClass().add("lamm-tree-icon-file-line");
        l2.getStyleClass().add("lamm-tree-icon-file-line");
        l3.getStyleClass().add("lamm-tree-icon-file-line");
        return new Group(outline, l1, l2, l3);
    }

    public static Node folder() {
        var p = new Path(
            new MoveTo(1, 2), new LineTo(5, 2), new LineTo(7, 4),
            new LineTo(13, 4), new LineTo(13, 14), new LineTo(1, 14),
            new ClosePath()
        );
        p.getStyleClass().add("lamm-tree-icon-folder-fill");
        return new Group(p);
    }

    public static Node folderOpen() {
        var tab = new Path(
            new MoveTo(1, 2), new LineTo(5, 2), new LineTo(7, 4),
            new LineTo(13, 4), new LineTo(13, 7), new LineTo(1, 7),
            new ClosePath()
        );
        tab.getStyleClass().add("lamm-tree-icon-folder-open-tab");
        var body = new Path(
            new MoveTo(3, 7), new LineTo(15, 7), new LineTo(13, 14),
            new LineTo(1, 14), new ClosePath()
        );
        body.getStyleClass().add("lamm-tree-icon-folder-open-body");
        return new Group(tab, body);
    }
}
