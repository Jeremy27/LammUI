package fr.courel.lammui.fx.component;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Chrome custom pour fenêtre {@link javafx.stage.StageStyle#UNDECORATED}.
 *
 * <p>Fournit titre, slot d'actions (cog réglages, etc.) et les contrôles
 * standard min/max/close. La barre de titre est draggable, double-clic = maximize.
 * Les enfants ajoutés ensuite via {@link #getChildren()} se placent sous la barre.
 */
public class LammChromeFx extends VBox {

    private final HBox titleBar = new HBox(8);
    private final HBox actionsSlot = new HBox(4);
    private final Button btnMin;
    private final Button btnMax;
    private final Button btnClose;

    private double dragOffsetX;
    private double dragOffsetY;
    private Stage stage;

    public LammChromeFx(String suffix) {
        getStyleClass().add("lamm-chrome");
        titleBar.getStyleClass().add("lamm-chrome-titlebar");
        titleBar.setAlignment(Pos.CENTER_LEFT);

        var titleBold = new Label("Lamm");
        titleBold.getStyleClass().add("lamm-header-title-bold");
        var titleLight = new Label(suffix == null ? "" : suffix);
        titleLight.getStyleClass().add("lamm-header-title-light");

        actionsSlot.setAlignment(Pos.CENTER_RIGHT);

        btnMin = chromeButton(minIcon(), "lamm-chrome-button");
        btnMax = chromeButton(maxIcon(), "lamm-chrome-button");
        btnClose = chromeButton(closeIcon(), "lamm-chrome-button", "lamm-chrome-button-close");

        var spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBar.getChildren().addAll(titleBold, titleLight, spacer, actionsSlot, btnMin, btnMax, btnClose);

        getChildren().add(titleBar);
        installDragHandlers();
    }

    /** Wires window controls to the given stage. Must be called once. */
    public void attachTo(Stage stage) {
        this.stage = stage;
        btnClose.setOnAction(e -> stage.close());
        btnMin.setOnAction(e -> stage.setIconified(true));
        btnMax.setOnAction(e -> stage.setMaximized(!stage.isMaximized()));
        stage.maximizedProperty().addListener((obs, ov, nv) -> updateMaxIcon(nv));
    }

    /** Adds an app-specific action (e.g. settings cog) before the OS controls. */
    public void addAction(Node node) {
        actionsSlot.getChildren().add(node);
    }

    /** Convenience: creates a chrome-styled icon button for use with {@link #addAction}. */
    public static Button iconButton(Node icon, Runnable onClick) {
        var btn = new Button();
        btn.getStyleClass().add("lamm-chrome-button");
        btn.setFocusTraversable(false);
        btn.setGraphic(icon);
        btn.setOnAction(e -> onClick.run());
        return btn;
    }

    private static Button chromeButton(Node icon, String... styleClasses) {
        var btn = new Button();
        for (String c : styleClasses) btn.getStyleClass().add(c);
        btn.setFocusTraversable(false);
        btn.setGraphic(icon);
        return btn;
    }

    private void installDragHandlers() {
        titleBar.setOnMousePressed(e -> {
            if (stage == null) return;
            dragOffsetX = e.getScreenX() - stage.getX();
            dragOffsetY = e.getScreenY() - stage.getY();
        });
        titleBar.setOnMouseDragged(e -> {
            if (stage == null || stage.isMaximized()) return;
            stage.setX(e.getScreenX() - dragOffsetX);
            stage.setY(e.getScreenY() - dragOffsetY);
        });
        titleBar.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && stage != null) {
                stage.setMaximized(!stage.isMaximized());
            }
        });
    }

    private void updateMaxIcon(boolean maximized) {
        btnMax.setGraphic(maximized ? restoreIcon() : maxIcon());
    }

    /* ----- Icônes 12×12 dessinées en JavaFX ----- */

    private static Node minIcon() {
        var line = new Rectangle(0, 5, 12, 1);
        line.getStyleClass().add("lamm-chrome-icon");
        return line;
    }

    private static Node maxIcon() {
        var rect = new Rectangle(0, 0, 12, 12);
        rect.setFill(null);
        rect.setStrokeWidth(1.2);
        rect.getStyleClass().add("lamm-chrome-icon");
        return rect;
    }

    private static Node restoreIcon() {
        var back = new Rectangle(2, 0, 10, 10);
        back.setFill(null);
        back.setStrokeWidth(1.2);
        back.getStyleClass().add("lamm-chrome-icon");
        var front = new Rectangle(0, 2, 10, 10);
        front.setFill(null);
        front.setStrokeWidth(1.2);
        front.getStyleClass().add("lamm-chrome-icon");
        var pane = new StackPane(back, front);
        pane.setMinSize(12, 12);
        return pane;
    }

    private static Node closeIcon() {
        var l1 = new Line(0, 0, 12, 12);
        var l2 = new Line(0, 12, 12, 0);
        l1.setStrokeWidth(1.4);
        l2.setStrokeWidth(1.4);
        l1.getStyleClass().add("lamm-chrome-icon");
        l2.getStyleClass().add("lamm-chrome-icon");
        var pane = new StackPane(l1, l2);
        pane.setMinSize(12, 12);
        return pane;
    }

    /** Cog/gear icon (réglages) — utilisable via {@link #iconButton(Node, Runnable)}. */
    public static Node settingsIcon() {
        var ring = new javafx.scene.shape.Circle(7, 7, 5);
        ring.setFill(null);
        ring.setStrokeWidth(1.4);
        ring.getStyleClass().add("lamm-chrome-icon");
        var center = new javafx.scene.shape.Circle(7, 7, 1.6);
        center.getStyleClass().add("lamm-chrome-icon-fill");

        var pane = new javafx.scene.layout.Pane(ring, center);
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4.0;
            double cx = 7 + Math.cos(angle) * 6;
            double cy = 7 + Math.sin(angle) * 6;
            var tooth = new Path(
                new MoveTo(cx - 1.2, cy - 1.2),
                new LineTo(cx + 1.2, cy - 1.2),
                new LineTo(cx + 1.2, cy + 1.2),
                new LineTo(cx - 1.2, cy + 1.2),
                new ClosePath()
            );
            tooth.setStrokeWidth(0);
            tooth.getStyleClass().add("lamm-chrome-icon-fill");
            pane.getChildren().add(tooth);
        }
        pane.setMinSize(14, 14);
        return pane;
    }
}
