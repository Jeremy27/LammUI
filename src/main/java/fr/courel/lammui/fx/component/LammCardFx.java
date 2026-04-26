package fr.courel.lammui.fx.component;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LammCardFx extends VBox {

    public LammCardFx() {
        getStyleClass().add("lamm-card");
        setSpacing(8);
    }

    public LammCardFx(String title) {
        this();
        var titleLabel = new Label(title);
        titleLabel.getStyleClass().add("lamm-card-title");
        var sep = new Region();
        sep.getStyleClass().add("lamm-card-separator");
        getChildren().addAll(titleLabel, sep);
    }
}
