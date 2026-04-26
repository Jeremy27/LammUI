package fr.courel.lammui.fx.component;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class LammHeaderFx extends HBox {

    public LammHeaderFx(String suffix) {
        getStyleClass().add("lamm-header");
        var bold = new Label("Lamm");
        bold.getStyleClass().add("lamm-header-title-bold");
        var light = new Label(suffix == null ? "" : suffix);
        light.getStyleClass().add("lamm-header-title-light");
        getChildren().addAll(bold, light);
    }
}
