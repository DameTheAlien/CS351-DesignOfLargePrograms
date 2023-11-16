package main.backend;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import main.Loader;

public class Home {
    private static final String directory = "resources/";

    private final String homePage;
    private final String reactionTimePage;

    private Scene scene;

    public Home() {
        homePage = directory + "home.fxml";
        reactionTimePage = directory + "reactionTime.fxml";
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void initialize() {

    }

    public void reactionTimeClicked(ActionEvent actionEvent) {
        Pane newRoot = Loader.loadFxmlFile(reactionTimePage);
        scene.setRoot(newRoot);
    }
}
