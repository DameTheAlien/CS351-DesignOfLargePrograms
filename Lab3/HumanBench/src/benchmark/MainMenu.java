/*
 * Name: Damian Franco
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Human Benchmark (Lab 3)
 * Version: 5
 */
package benchmark;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainMenu extends Application {
    /* Width and height of window */
    private double width  = 1200.0, height = 700.0;
    /* Button for the reaction time game */
    private Button reactionTime;
    /* Button for the aim trainer game */
    private Button aimTrainer;
    /* Button for the chimp test game */
    private Button chimpTest;
    /* Button for the visual memory game */
    private Button visualMemory;
    /* Button for the hearing test game */
    private Button hearing;
    /* Button for the typing test game */
    private Button typing;
    /* Button for the number memory game */
    private Button numberMemory;
    /* Button for the verbal memory game */
    private Button verbalMemory;
    /* Button for the quick maths game */
    private Button quickMath;
    
    /*
     * The restart method is a method to call back to the main menu when the 
     * main menu button is clicked anytime. This method brings the application
     * back to the main menu by setting the current stage to the main menu.
     */
    public void restart() {
        Stage primary = new Stage();
        try {
            start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
     * The main stage start method sets up the GUI and by calling various
     * methods that will allow the canvas of the GUI to look nicer and
     * to set up buttons, like the menu and start button. This will also
     * invoke the show method to show the GUI.
     * 
     * @param primary stage to display GUI on
     */
    public void start(Stage primaryStage) throws Exception {
        //Canvas and pane intialization
        primaryStage.setTitle("Scuffed Human Benchmark");
        Canvas canvas = new Canvas(width, height);
        Pane root = new Pane(canvas);
        
        //Methods that setup GUI for play.
        mainMenuSetup(root);
        mainBtnsSetup(root);
        btnActionSetup(primaryStage);
        
        //Background fill for the whole pane
        BackgroundFill bgFill = new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bgFill);
        root.setBackground(bg);
        
        //Scene intialization and show GUI invoked
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
    
    /*
     * A setup of the title GUI's features like the labels and background.
     * 
     * @param pane to add components to
     */
    public void mainMenuSetup(Pane root) {
        Font titleFont = Font.font("TimesRoman", FontWeight.BOLD, 50);
        Label welLabel = new Label("The Human Benchmark");        
        welLabel.setFont(titleFont);
        welLabel.setLayoutX(345);
        welLabel.setLayoutY(25);
        
        Font descFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label descLabel = new Label("Welcome to my knockoff version of the human benchmark website.");
        descLabel.setFont(descFont);
        descLabel.setLayoutX(335);
        descLabel.setLayoutY(100);
        
        Font chooseFont = Font.font("TimesRoman", FontWeight.BOLD, 20);
        Label chooseLabel = new Label("Please choose which game you would like to play:");
        chooseLabel.setFont(chooseFont);
        chooseLabel.setLayoutX(370);
        chooseLabel.setLayoutY(178);
        
        Line titleLine = new Line();
        titleLine.setStartX(0);
        titleLine.setStartY(165);
        titleLine.setEndX(1200);
        titleLine.setEndY(165);
        
        Rectangle backR = new Rectangle();
        backR.setX(0);
        backR.setY(0);
        backR.setWidth(1200);
        backR.setHeight(165);
        backR.setFill(Color.AQUAMARINE);
        
        root.getChildren().add(backR);
        root.getChildren().add(welLabel);
        root.getChildren().add(descLabel);
        root.getChildren().add(titleLine);
        root.getChildren().add(chooseLabel);
    }
    
    /*
     * Setup of all the buttons on the main menu. This does not 
     * handle the action when pressed, but it lays it out on the
     * GUI to make it look less scuffed.
     * 
     * @param pane to add components to
     */
    public void mainBtnsSetup(Pane root) {
        Font btnFont = Font.font("TimesRoman", FontPosture.ITALIC, 25);
        
        //Row 1
        reactionTime = new Button();
        reactionTime.setText("Reaction\n         Time");
        reactionTime.setLayoutX(50);
        reactionTime.setLayoutY(230);
        reactionTime.setPrefSize(300, 100);
        reactionTime.setFont(btnFont);
        
        aimTrainer = new Button();
        aimTrainer.setText("Aim\n  Trainer");
        aimTrainer.setLayoutX(450);
        aimTrainer.setLayoutY(230);
        aimTrainer.setPrefSize(300, 100);
        aimTrainer.setFont(btnFont);
        
        chimpTest = new Button();
        chimpTest.setText("Chimp\n      Test");
        chimpTest.setLayoutX(850);
        chimpTest.setLayoutY(230);
        chimpTest.setPrefSize(300, 100);
        chimpTest.setFont(btnFont);
        
        //Row 2
        hearing = new Button();
        hearing.setText("Hearing\n        Test");
        hearing.setLayoutX(50);
        hearing.setLayoutY(380);
        hearing.setPrefSize(300, 100);
        hearing.setFont(btnFont);
        
        quickMath = new Button();
        quickMath.setText("Quick\n    Maths");
        quickMath.setLayoutX(450);
        quickMath.setLayoutY(380);
        quickMath.setPrefSize(300, 100);
        quickMath.setFont(btnFont);
        
        typing = new Button();
        typing.setText("Typing\n      Test");
        typing.setLayoutX(850);
        typing.setLayoutY(380);
        typing.setPrefSize(300, 100);
        typing.setFont(btnFont);
        
        //Row 3
        numberMemory = new Button();
        numberMemory.setText("Number\n        Memory");
        numberMemory.setLayoutX(50);
        numberMemory.setLayoutY(530);
        numberMemory.setPrefSize(300, 100);
        numberMemory.setFont(btnFont);
        
        visualMemory = new Button();
        visualMemory.setText("Visual\n      Memory");
        visualMemory.setLayoutX(450);
        visualMemory.setLayoutY(530);
        visualMemory.setPrefSize(300, 100);
        visualMemory.setFont(btnFont);
        
        verbalMemory = new Button();
        verbalMemory.setText("Verbal\n      Memory");
        verbalMemory.setLayoutX(850);
        verbalMemory.setLayoutY(530);
        verbalMemory.setPrefSize(300, 100);
        verbalMemory.setFont(btnFont);
        
        root.getChildren().add(reactionTime);
        root.getChildren().add(aimTrainer);
        root.getChildren().add(chimpTest);
        
        root.getChildren().add(hearing);
        root.getChildren().add(typing);
        root.getChildren().add(quickMath);
        
        root.getChildren().add(visualMemory);
        root.getChildren().add(numberMemory);
        root.getChildren().add(verbalMemory);
    }
    
    /*
     * Sets up new objects of each game and assigns them to whenever
     * the button is clicked. Then it will close the main menu GUI
     * on the button click and show the game selected. 
     * 
     * @param main stage to close on click
     */
    public void btnActionSetup(Stage primaryStage) {
        reactionTime.setOnAction(event -> {
            ReactionGame rg = new ReactionGame();
            primaryStage.close();
        });
        
        aimTrainer.setOnAction(event -> {
            AimGame ag = new AimGame();
            primaryStage.close();
        });
        
        chimpTest.setOnAction(event -> {
            ChimpTest ct = new ChimpTest();
            primaryStage.close();
        });
        
        hearing.setOnAction(event -> {
            HearingTest ht = new HearingTest();
            primaryStage.close();
        });
        
        typing.setOnAction(event -> {
            TypingTest tt = new TypingTest();
            primaryStage.close();
        });
        
        numberMemory.setOnAction(event -> {
            NumberTest nt = new NumberTest();
            primaryStage.close();
        });
        
        visualMemory.setOnAction(event -> {
            VisualTest visT = new VisualTest();
            primaryStage.close();
        });
        
        verbalMemory.setOnAction(event -> {
            VerbalTest verbt = new VerbalTest();
            primaryStage.close();
        });
        
        quickMath.setOnAction(event -> {
            MathTest mt = new MathTest();
            primaryStage.close();
        });
    }
    
    /*
     * Main method that brings the user to the main menu when ran.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
