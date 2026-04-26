package fr.courel.lammui.fx.component;

import javafx.scene.control.ProgressBar;

public class LammProgressBarFx extends ProgressBar {

    public LammProgressBarFx() {
        super();
        getStyleClass().add("lamm-progress");
    }

    public LammProgressBarFx(double progress) {
        super(progress);
        getStyleClass().add("lamm-progress");
    }
}
