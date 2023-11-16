/*
 * Name: Damian Franco
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Human Benchmark (Lab 3)
 * Version: 5
 */
package benchmark;

import java.net.URISyntaxException;
import java.net.URL;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HearingTest extends Application {
    /* Width and height of window */
    private double width  = 1200.0, height = 700.0;
    /* Play area back drop */
    private Rectangle r;
    /* Media player to play the audio in the game */
    private MediaPlayer mp;
    /* Text components of the GUI during game progress */
    private Text playText, msLabel, timeDisplay;
    /* Flag to set if game is being played or not */
    private Boolean gameFlag = false;
    /* Holds the time as a long property to display correctly on GUI */
    private LongProperty time;
    /* Holds the nano seconds time when reaction time starts */
    private long startTime;
    
    /*
     * Constructor for HearingTest object, this will call the GUI for
     * this class to be invoked and show and start the game play area.
     */
    public HearingTest() {
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
        primaryStage.setTitle("Scuffed Human Benchmark");
        Canvas canvas = new Canvas(width, height);
        Pane root = new Pane(canvas);
        
        loadSound();
        gameAreaSetup(root);
        topPanelSetup(root);
        menuSetup(primaryStage, root);
        setPlayText(root);
        
        BackgroundFill bgFill = new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bgFill);
        root.setBackground(bg);
        
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
    
    /*
     * Loads the sound from the resource folder to be played during
     * the gameplay. Then it will put it in a new MediaPlayer object
     * that way it can be played when the game is being played.
     */
    public void loadSound() {
        String path = "/meep.mp3";
        URL fis = getClass().getResource(path);
        Media sound = null;
        try {
            sound = new Media(fis.toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mp = new MediaPlayer(sound);
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
        r = new Rectangle();
        r.setLayoutX(0);
        r.setLayoutY(165.5);
        r.setWidth(1200);
        r.setHeight(414.5);
        r.setFill(Color.FIREBRICK);        
        
        r.setOnMouseClicked(event -> {
            root.getChildren().removeIf(node -> node instanceof Group);
            handleGame();
            if(!gameFlag) {
                root.getChildren().removeIf(node -> node instanceof Text);
                r.setFill(Color.FIREBRICK);
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
       
        root.getChildren().add(r);
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
     * Does the same thing as it does in the ReactionGame class. But
     * in this version the method will instead play the meep meep audio.
     * This method will be invoked when the mouse clicks the play
     * area when there is a new game to be played. First this will
     * set our gameFlag to true because the game is currently in play.
     * Next, it will sleep the GUI from 1-5 seconds to keep the player
     * on their toes and to randomize the time to play the audio.
     * After it sleeps, it will play the roadrunner meep meep and assign our
     * start time to the nano seconds of the current time. 
     * 
     * @param mouse click event
     */
    public void startTimer(MouseEvent mouseEvent) throws InterruptedException {
        long rand = ThreadLocalRandom.current().nextLong(1000, 5000);
        gameFlag = true;
        TimeUnit.MILLISECONDS.sleep(rand);
        mp.play();
        startTime = System.nanoTime();
    }
    
    /*
     * Does the same thing as it does in the ReactionGame class.
     * This method will be invoked when the game is over. The method will first
     * get the time the player finshed the game, then it will find out the 
     * reaction time by dividing the finish time by the start time. It will then
     * convert it to milliseconds. This was shown to us in class by Professor Haugh.
     * Lastly it will remove the text components on the GUI and get ready to be 
     * played again, while also assiging the value of time to the reaction time,
     * and it will stop the audio clip that way it can be played again on other plays.
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
        mp.stop();
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
        Label welLabel = new Label("Hearing Test");        
        welLabel.setFont(titleFont);
        welLabel.setLayoutX(465);
        welLabel.setLayoutY(25);
        
        Font descFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label descLabel = new Label("Test to check how quick your reactions are according to your ear.");
        descLabel.setFont(descFont);
        descLabel.setLayoutX(325);
        descLabel.setLayoutY(100);
        
        Font chooseFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label inTitleLabel = new Label("Instructions:");
        inTitleLabel.setFont(chooseFont);
        inTitleLabel.setLayoutX(220);
        inTitleLabel.setLayoutY(600);
        
        Font instFont = Font.font("TimesRoman", FontWeight.NORMAL, 20);
        Label instLabel = new Label("A sound will play, click the screen as quickly as possible once you hear the sound.\n "
                + "                  Your speed will display on the screen after you play.");
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
