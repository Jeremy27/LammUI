package fr.courel.lammui.fx.component;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Toggle switch animé : track + thumb, click pour basculer.
 *
 * <p>Position du thumb : centerX 11 (off) → 33 (on), animé ~160ms.
 * Couleurs via CSS : track utilise {@code -lamm-border} en off,
 * {@code -lamm-primary} en on (pseudo-class {@code :on}).
 */
public class LammSwitchFx extends HBox {

    private static final double TRACK_W = 44;
    private static final double TRACK_H = 22;
    private static final double THUMB_R = 9;
    private static final double THUMB_OFF_X = 11;
    private static final double THUMB_ON_X = 33;
    private static final Duration ANIM = Duration.millis(160);
    private static final PseudoClass ON = PseudoClass.getPseudoClass("on");

    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final Circle thumb;

    public LammSwitchFx() {
        this(null);
    }

    public LammSwitchFx(String labelText) {
        getStyleClass().add("lamm-switch");
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        setCursor(Cursor.HAND);

        var track = new Rectangle(TRACK_W, TRACK_H);
        track.setArcWidth(TRACK_H);
        track.setArcHeight(TRACK_H);
        track.getStyleClass().add("lamm-switch-track");

        thumb = new Circle(THUMB_OFF_X, TRACK_H / 2.0, THUMB_R);
        thumb.getStyleClass().add("lamm-switch-thumb");

        var trackPane = new Pane(track, thumb);
        trackPane.setMinSize(TRACK_W, TRACK_H);
        trackPane.setPrefSize(TRACK_W, TRACK_H);
        trackPane.setMaxSize(TRACK_W, TRACK_H);

        if (labelText != null && !labelText.isEmpty()) {
            var label = new Label(labelText);
            label.getStyleClass().add("lamm-switch-label");
            getChildren().addAll(label, trackPane);
        } else {
            getChildren().add(trackPane);
        }

        setOnMouseClicked(e -> selected.set(!selected.get()));

        selected.addListener((obs, ov, nv) -> {
            pseudoClassStateChanged(ON, nv);
            new Timeline(new KeyFrame(ANIM,
                new KeyValue(thumb.centerXProperty(),
                    nv ? THUMB_ON_X : THUMB_OFF_X, Interpolator.EASE_BOTH)
            )).play();
        });
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean value) {
        selected.set(value);
    }
}
