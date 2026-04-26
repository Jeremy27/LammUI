package fr.courel.lammui.fx.component;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

public class LammTextFieldFx extends StackPane {

    private static final Duration ANIM = Duration.millis(160);
    private static final double FLOATED_Y = -18;
    private static final double FLOATED_SCALE = 0.85;

    private final Label label;
    private final TextField field;
    private final Scale labelScale = new Scale(1.0, 1.0, 0, 0);
    private boolean floated = false;

    public LammTextFieldFx() {
        this("");
    }

    public LammTextFieldFx(String labelText) {
        getStyleClass().add("lamm-tf");

        field = new TextField();
        field.getStyleClass().add("lamm-tf-input");

        label = new Label(labelText);
        label.getStyleClass().add("lamm-tf-label");
        label.setMouseTransparent(true);
        label.getTransforms().add(labelScale);

        StackPane.setAlignment(field, Pos.CENTER_LEFT);
        StackPane.setAlignment(label, Pos.CENTER_LEFT);
        getChildren().addAll(field, label);

        applyFloated(false, false);

        field.focusedProperty().addListener((o, was, is) ->
            applyFloated(is || !field.getText().isEmpty(), true)
        );
        field.textProperty().addListener((o, was, is) -> {
            if (!field.isFocused()) {
                applyFloated(!is.isEmpty(), true);
            }
        });
    }

    private void applyFloated(boolean target, boolean animate) {
        if (target == floated && animate) return;
        floated = target;
        double y = target ? FLOATED_Y : 0;
        double s = target ? FLOATED_SCALE : 1.0;

        if (animate) {
            new Timeline(new KeyFrame(ANIM,
                new KeyValue(label.translateYProperty(), y, Interpolator.EASE_BOTH),
                new KeyValue(labelScale.xProperty(), s, Interpolator.EASE_BOTH),
                new KeyValue(labelScale.yProperty(), s, Interpolator.EASE_BOTH)
            )).play();
        } else {
            label.setTranslateY(y);
            labelScale.setX(s);
            labelScale.setY(s);
        }

        if (target) {
            if (!label.getStyleClass().contains("floated")) {
                label.getStyleClass().add("floated");
            }
        } else {
            label.getStyleClass().remove("floated");
        }
    }

    public TextField getField() {
        return field;
    }

    public StringProperty textProperty() {
        return field.textProperty();
    }

    public String getText() {
        return field.getText();
    }

    public void setText(String text) {
        field.setText(text);
    }
}
