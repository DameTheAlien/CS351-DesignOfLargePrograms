/*
 * Name: Damian Franco
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Human Benchmark (Lab 3)
 * Version: 5
 */
package benchmark;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ReactionGame extends Application {
    /* Width and height of window */
    private double width  = 1200.0, height = 700.0;
    /* Holds the time as a long property to display correctly on GUI */
    private LongProperty time;
    /* Text GUI components for the game hub and display text */
    private Text timeDisplay, playText, msLabel;
    /* Holds the nano seconds time when reaction time starts */
    private long startTime;
    /* Flag to display when the game is in play */
    private boolean gameFlag = false;
    /* Backdrop of play area to modify based on play */
    private Rectangle backDrop;
    
    /*
     * Constructor for ReactionGame object, this will call the GUI for
     * this class to be invoked and show and start the game play area.
     */
    public ReactionGame() {
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
        gameAreaSetup(root);
        topPanelSetup(root);
        menuSetup(primaryStage, root);
        setPlayText(root);
        
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
     * Set the text to imply the user where to to click to begin the game.
     * 
     * @param pane to add components to
     */
    public void setPlayText(Pane root) {
        Group g = new Group();
        Font playFont = Font.font("TimesRoman", FontWeight.BOLD, 36);
        playText = new Text();
        playText.setText("Click anywhere in the play area to begin");
        playText.setX(265);
        playText.setY(375);
        playText.setFont(playFont);
        playText.setFill(Color.WHITE);
        g.getChildren().add(playText);
        
        root.getChildren().add(g);
    }
    
    /*
     * Setup of the game area by setting the game play are to the
     * rectangle where the player needs to click to begin the game. 
     * When the rectangle is clicked, the action is handled by calling
     * the handleGame() method and the either playing or ending the game
     * based on the game flag. 
     * 
     * @param pane to add components to
     */
    public void gameAreaSetup(Pane root) {
        backDrop = new Rectangle();
        backDrop.setLayoutX(0);
        backDrop.setLayoutY(165.5);
        backDrop.setWidth(1200);
        backDrop.setHeight(414.5);
        backDrop.setFill(Color.FIREBRICK); 
        
        backDrop.setOnMouseClicked(event -> {
            root.getChildren().removeIf(node -> node instanceof Group);
            handleGame();
            if(!gameFlag) {
                root.getChildren().removeIf(node -> node instanceof Text);
                try {
                    startTimer(event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                finishTimer(root, event);
                root.getChildren().add(timeDisplay);
                root.getChildren().add(msLabel);
            }
            
        });
        root.getChildren().add(backDrop);
    }
    
    /*
     * The handle game method will first set up a new text that will show after
     * the game has ended. It will also get the reaction time of the game 
     * previously played and bind it to the text display object timeDisplay.
     */
    public void handleGame() {
        Font playFont = Font.font("TimesRoman", FontWeight.BOLD, 36);
        msLabel = new Text();
        msLabel.setText("                  ms\nClick to play again");
        msLabel.setFont(playFont);
        msLabel.setX(415);
        msLabel.setY(375);
        msLabel.setFill(Color.WHITE);
        
        time = new SimpleLongProperty(0);
        timeDisplay = new Text();
        timeDisplay.setX(510);
        timeDisplay.setY(376);
        timeDisplay.setFont(playFont);
        timeDisplay.setFill(Color.WHITE);
        timeDisplay.textProperty().bind(time.asString());
    }
    
    /*
     * This method will be invoked when the mouse clicks the play
     * area when there is a new game to be played. First this will
     * set our gameFlag to true because the game is currently in play.
     * Next, it will sleep the GUI from 1-5 seconds to keep the player
     * on their toes and to randomize the time to show the green screen.
     * After it sleeps, it will turn the play area green and assign our
     * start time to the nano seconds of the current time. 
     * 
     * @param mouse click event
     */
    public void startTimer(MouseEvent mouseEvent) throws InterruptedException {
        long rand = ThreadLocalRandom.current().nextLong(1000, 5000);
        gameFlag = true;
        TimeUnit.MILLISECONDS.sleep(rand);
        backDrop.setFill(Color.FORESTGREEN);
        startTime = System.nanoTime();
    }
    
    /*
     * This method will be invoked when the game is over. The method will first
     * get the time the player finshed the game, then it will find out the 
     * reaction time by dividing the finish time by the start time. It will then
     * convert it to milliseconds. This was shown to us in class by Professor Haugh.
     * Lastly it will remove the text components on the GUI and get ready to be 
     * played again, while also assiging the value of time to the reaction time.
     * 
     * @param pane to add components to
     * @param mouse click event
     */
    public void finishTimer(Pane root, MouseEvent mouseEvent) {
        long finishTime = System.nanoTime();
        long reactionTimeNano = finishTime - startTime;
        long milliValue = TimeUnit.NANOSECONDS.toMillis(reactionTimeNano);
        root.getChildren().removeIf(node -> node instanceof Text);
        gameFlag = false;
        backDrop.setFill(Color.FIREBRICK);
        time.setValue(milliValue);
    }
    
    /*
     * This method will setup the main menu button seen throughout
     * the program on the bottom left. This button will take the 
     * user back to the main menu selection screen. This will also 
     * close the current game GUI window open.
     * 
     * @param main stage to close on click
     * @param pane to add components to
     */
    public void menuSetup(Stage primaryStage, Pane root) {
        Button menuBtn = new Button();
        Font menuFont = Font.font("TimesRoman", FontPosture.ITALIC, 15);
        menuBtn.setText("Main Menu");
        menuBtn.setLayoutX(10);
        menuBtn.setLayoutY(600);
        menuBtn.setPrefSize(150, 35);
        menuBtn.setFont(menuFont);
        root.getChildren().add(menuBtn);
        
        menuBtn.setOnAction(event -> {
            MainMenu mm = new MainMenu();
            mm.restart();
            primaryStage.close();
        });
    }
    
    /*
     * The top half-panel setup throughout the whole program to
     * keep consistancy, style, and to, of course, look at least
     * a tid bit less scuffed. This appears in every class to 
     * set up the layout of the game around the same layout.
     * 
     * @param pane to add components to
     */
    public void topPanelSetup(Pane root) {
        Font titleFont = Font.font("TimesRoman", FontWeight.BOLD, 50);
        Label welLabel = new Label("Reaction Time");        
        welLabel.setFont(titleFont);
        welLabel.setLayoutX(435);
        welLabel.setLayoutY(25);
        
        Font descFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label descLabel = new Label("Test to see how fast your reaction time is.");
        descLabel.setFont(descFont);
        descLabel.setLayoutX(425);
        descLabel.setLayoutY(100);
        
        Font chooseFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label inTitleLabel = new Label("Instructions:");
        inTitleLabel.setFont(chooseFont);
        inTitleLabel.setLayoutX(220);
        inTitleLabel.setLayoutY(600);
        
        Font instFont = Font.font("TimesRoman", FontWeight.NORMAL, 20);
        Label instLabel = new Label("Wait for the play area to turn green and click the area as quickly as possible.\n "
                + "           Your reaction speed will display on the screen after you play.");
        instLabel.setFont(instFont);
        instLabel.setLayoutX(350);
        instLabel.setLayoutY(600);
        
        Line titleLine = new Line();
        titleLine.setStartX(0);
        titleLine.setStartY(165);
        titleLine.setEndX(1200);
        titleLine.setEndY(165);
        
        Line bottomLine = new Line();
        bottomLine.setStartX(0);
        bottomLine.setStartY(580);
        bottomLine.setEndX(1200);
        bottomLine.setEndY(580);
        
        Rectangle backR = new Rectangle();
        backR.setX(0);
        backR.setY(0);
        backR.setWidth(1200);
        backR.setHeight(165);
        backR.setFill(Color.AQUAMARINE);
        
        root.getChildren().add(backR);
        root.getChildren().add(welLabel);
        root.getChildren().add(descLabel);
        root.getChildren().add(inTitleLabel);
        root.getChildren().add(instLabel);
        root.getChildren().add(bottomLine);
        root.getChildren().add(titleLine);
    }

}
