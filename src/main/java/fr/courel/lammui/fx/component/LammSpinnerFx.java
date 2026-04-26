package fr.courel.lammui.fx.component;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;

/**
 * Spinner numérique style Materialize : label en sur-titre,
 * bordure inférieure animée au focus, flèches +/- intégrées.
 *
 * <p>Le visuel est défini dans {@code lamm.css} via {@code .lamm-spinner-input}
 * et {@code .lamm-spinner-label}.
 */
public class LammSpinnerFx extends VBox {

    private final Label label;
    private final Spinner<Integer> spinner;

    public LammSpinnerFx(String labelText, int min, int max, int initial) {
        this(labelText, min, max, initial, 1);
    }

    public LammSpinnerFx(String labelText, int min, int max, int initial, int step) {
        getStyleClass().add("lamm-spinner");
        setSpacing(2);

        label = new Label(labelText);
        label.getStyleClass().add("lamm-spinner-label");

        spinner = new Spinner<>(min, max, initial, step);
        spinner.setEditable(true);
        spinner.getStyleClass().add("lamm-spinner-input");

        getChildren().addAll(label, spinner);

        spinner.getEditor().focusedProperty().addListener((obs, was, is) -> {
            if (is) {
                if (!label.getStyleClass().contains("focused")) {
                    label.getStyleClass().add("focused");
                }
            } else {
                label.getStyleClass().remove("focused");
            }
        });
    }

    public Spinner<Integer> getSpinner() {
        return spinner;
    }

    public ObjectProperty<Integer> valueProperty() {
        return spinner.getValueFactory().valueProperty();
    }

    public int getValue() {
        return spinner.getValue();
    }

    public void setValue(int value) {
        spinner.getValueFactory().setValue(value);
    }
}
