package fr.courel.lammui.component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Groupe de {@link LammRadioButton} mutuellement exclusifs.
 * Les callbacks {@link #onSelectionChanged(Consumer)} sont notifiés
 * après la désélection des autres boutons.
 */
public final class LammRadioGroup {

    private final List<LammRadioButton> buttons = new ArrayList<>();
    private final List<Consumer<LammRadioButton>> listeners = new ArrayList<>();

    public LammRadioGroup(LammRadioButton... initial) {
        for (var b : initial) add(b);
    }

    public void add(LammRadioButton button) {
        buttons.add(button);
        button.addPropertyChangeListener("selected", evt -> {
            if (Boolean.TRUE.equals(evt.getNewValue())) {
                for (var other : buttons) {
                    if (other != button && other.isSelected()) {
                        other.setSelected(false);
                    }
                }
                for (var listener : listeners) listener.accept(button);
            }
        });
    }

    public void onSelectionChanged(Consumer<LammRadioButton> listener) {
        listeners.add(listener);
    }
}
