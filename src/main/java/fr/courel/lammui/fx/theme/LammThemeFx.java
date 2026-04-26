package fr.courel.lammui.fx.theme;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Objects;

public final class LammThemeFx {

    public enum Mode { LIGHT, DARK }

    private static final String STYLESHEET = "/fr/courel/lammui/fx/lamm.css";
    private static final String DARK_CLASS = "dark";

    private static Mode mode = Mode.LIGHT;

    private LammThemeFx() {}

    public static void install(Scene scene) {
        var url = Objects.requireNonNull(
            LammThemeFx.class.getResource(STYLESHEET),
            "Stylesheet Lamm introuvable : " + STYLESHEET
        ).toExternalForm();
        if (!scene.getStylesheets().contains(url)) {
            scene.getStylesheets().add(url);
        }
        applyMode(scene.getRoot());
    }

    public static Mode current() {
        return mode;
    }

    public static boolean isDark() {
        return mode == Mode.DARK;
    }

    public static void toggle(Scene scene) {
        setMode(scene, isDark() ? Mode.LIGHT : Mode.DARK);
    }

    public static void setMode(Scene scene, Mode m) {
        mode = m;
        applyMode(scene.getRoot());
    }

    private static void applyMode(Parent root) {
        root.getStyleClass().remove(DARK_CLASS);
        if (mode == Mode.DARK) {
            root.getStyleClass().add(DARK_CLASS);
        }
    }
}
