package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.backend.Home;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Human Benchmark");

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("resources/home.fxml"));

        GridPane root = loader.load();

        Scene scene = new Scene(root);

        Home home = loader.getController();
        home.setScene(scene);

//        loader = new FXMLLoader(getClass().getResource("resources" +
//                "/reactionTime.fxml"));
//
//        Pane newRoot = loader.load();
//        scene.setRoot(newRoot);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
