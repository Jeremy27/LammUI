package fr.courel.lammui.fx.component;

import javafx.scene.control.Button;

public class LammButtonFx extends Button {

    public LammButtonFx() {
        this("");
    }

    public LammButtonFx(String text) {
        super(text);
        getStyleClass().add("lamm-button");
    }

    public static LammButtonFx primary(String text) {
        var b = new LammButtonFx(text);
        b.getStyleClass().add("lamm-button-primary");
        return b;
    }

    public static LammButtonFx accent(String text) {
        var b = new LammButtonFx(text);
        b.getStyleClass().add("lamm-button-accent");
        return b;
    }
}
