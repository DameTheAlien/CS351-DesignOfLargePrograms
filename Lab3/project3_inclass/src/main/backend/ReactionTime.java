package main.backend;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ReactionTime {
    @FXML
    private Pane root;
    @FXML
    private Label reactionTime;

    private long startTime;

    private LongProperty reactionTimeValue;

    public ReactionTime() {
        reactionTimeValue = new SimpleLongProperty(0);
    }

    public void initialize() {
        reactionTime.textProperty().bind(reactionTimeValue.asString());
    }

    public void startTimer(ActionEvent actionEvent) throws InterruptedException {
        long rand = ThreadLocalRandom.current().nextLong(1000, 5000);
        TimeUnit.MILLISECONDS.sleep(rand);
        root.setStyle("-fx-background-color: green");
        startTime = System.nanoTime();
    }

    public void finishTimer(ActionEvent actionEvent) {
        long finishTime = System.nanoTime();
        long reactionTimeNano = finishTime - startTime;
        long milliValue = TimeUnit.NANOSECONDS.toMillis(reactionTimeNano);
        reactionTimeValue.setValue(milliValue);
        //reactionTime.setText(Long.toString(milliValue) + " ms");
    }
}
