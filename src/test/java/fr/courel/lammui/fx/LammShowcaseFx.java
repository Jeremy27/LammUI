package fr.courel.lammui.fx;

import fr.courel.lammui.fx.component.LammButtonFx;
import fr.courel.lammui.fx.component.LammCardFx;
import fr.courel.lammui.fx.component.LammHeaderFx;
import fr.courel.lammui.fx.component.LammTextFieldFx;
import fr.courel.lammui.fx.theme.LammThemeFx;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LammShowcaseFx extends Application {

    @Override
    public void start(Stage stage) {
        var header = new LammHeaderFx("UI — JavaFX showcase");

        var nom = new LammTextFieldFx("Nom du dossier");
        var ref = new LammTextFieldFx("Référence");

        var btnDefault = new LammButtonFx("Annuler");
        var btnPrimary = LammButtonFx.primary("Valider");
        var btnAccent  = LammButtonFx.accent("Archiver");
        var actions = new HBox(12, btnDefault, btnPrimary, btnAccent);
        actions.setAlignment(Pos.CENTER_LEFT);

        var card = new LammCardFx("Nouveau dossier");
        card.setSpacing(12);
        card.getChildren().addAll(nom, ref, actions);

        var btnToggle = LammButtonFx.primary("Toggle dark mode");
        var spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        var footer = new HBox(spacer, btnToggle);
        footer.setPadding(new Insets(12, 20, 20, 20));

        var content = new VBox(20, card);
        content.setPadding(new Insets(20));

        var root = new VBox(header, content, footer);
        VBox.setVgrow(content, Priority.ALWAYS);

        var scene = new Scene(root, 560, 420);
        LammThemeFx.install(scene);

        btnToggle.setOnAction(e -> LammThemeFx.toggle(scene));

        stage.setTitle("LammUI FX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
