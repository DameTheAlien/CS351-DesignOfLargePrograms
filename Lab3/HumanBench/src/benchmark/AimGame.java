/*
 * Name: Damian Franco
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Human Benchmark (Lab 3)
 * Version: 5
 */
package benchmark;

import java.util.ArrayList;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AimGame extends Application {
    /* Width and height of window */
    private double width  = 1200.0, height = 700.0;
    /* Holds the time as a long property to display correctly on GUI */
    private LongProperty timeMS;
    /* List to add the times between clicks to */
    private ArrayList<Long> times = new ArrayList<Long>();
    /* Count of targets hit/level and round count */
    private int targetsHit, roundCount = 1;
    /* Position of the circles x and y positions */
    private double xPos, yPos;
    /* Holds the nano seconds time when reaction time starts and ends */
    private long startTime, beginTime;
    
    /*
     * Constructor for AimGame object, this will call the GUI for
     * this class to be invoked and show and start the game play area.
     */
    public AimGame() {
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
        
        topPanelSetup(root);
        menuSetup(primaryStage, root);
        setBackground(root);
        startBtnSetup(root);
        
        BackgroundFill bgFill = new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bgFill);
        root.setBackground(bg);
        
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
    
    /*
     * Sets up the GUI labels and the play again button on the
     * pane when the game is over. This also will show the
     * average time in miliseconds on the screen. The method
     * also handles the action on the button, when the button 
     * is clicked the game is fully reset and the game area is 
     * set up to be played again.
     * 
     * @param pane to add componenets to
     */
    public void roundOver(Pane root) {
        root.getChildren().removeIf(node -> node instanceof Circle);
        Font againFont = Font.font("TimesRoman", FontWeight.BOLD, 18);
        Group g = new Group();
        Button playAgain = new Button();
        playAgain.setText("Click to play again");
        playAgain.setLayoutX(445);
        playAgain.setLayoutY(400);
        playAgain.setPrefSize(275, 50);
        playAgain.setFont(againFont);
        
        Font overFont = Font.font("TimesRoman", FontWeight.BOLD, 30);
        Label over = new Label();
        over.setText("Your average time per target is:\n"
                + "                           ms");
        over.setLayoutX(370);
        over.setLayoutY(300);
        over.setFont(overFont);
        
        Label avgMS = new Label();
        avgMS.textProperty().bind(timeMS.asString());
        avgMS.setLayoutX(525);
        avgMS.setLayoutY(345);
        avgMS.setFont(overFont);
        
        playAgain.setOnAction(event -> {
            roundCount++;
            root.getChildren().removeIf(node -> node instanceof Group);
            targetsHit = 0;
            for(int i = 0; i < times.size(); i++) {
                times.remove(i);
            }
            times.add(beginTime);
            drawTarget(root);
        });
        
        g.getChildren().add(playAgain);
        g.getChildren().add(over);
        g.getChildren().add(avgMS);
        root.getChildren().add(g);
    }
    
    /*
     * This method will get all of the times calculated during the game
     * play and take all of those times and convert them to milliseconds.
     * Then the average will be taken by dividing the times by the number
     * of targets. Then we will get the time in milliseconds by subtracting
     * by different numbers based on the round count. This will set our label
     * to show the average time in milliseconds on the screen after the game
     * is over. 
     */
    public void averageTime() {
        long reactNano =  times.get(times.size() - 1) - times.get(0);
        long milliVal = TimeUnit.NANOSECONDS.toMillis(reactNano);
        long avg = milliVal / 30;
        
        switch(roundCount) {
            case 1:
                timeMS.set(avg);
                break;
            case 2:
                avg -= 1000;
                timeMS.set(avg);
                break;
            case 3:
                avg -= 2000;
                timeMS.set(avg);
                break;
            case 4:
                avg -= 3000;
                timeMS.set(avg);
                break;
            case 5:
                avg -= 4000;
                timeMS.set(avg);
                break;
            default:
                break;
        }
    }
    
    /*
     * The drawTarget method will draw the circles on the play area at random. 
     * The method will first get a random double withing the play area to draw 
     * the new target/circle to and then it will handle the event when clicked.
     * When the circle is clicked, the game will progress the game by incrementing
     * the targets hit and will get the time for each time the targets are hit to
     * keep track of average time between hits. Lastly, the clicks will check
     * if the game is over by seeing if the number of hit targets are more than
     * 300, then it'll either play the game end it.
     * 
     * @param pane to add circles
     */
    public void drawTarget(Pane root) {        
        xPos = ThreadLocalRandom.current().nextDouble(15, 1150);
        yPos = ThreadLocalRandom.current().nextDouble(190, 550);
        Circle c = new Circle();
        c.setRadius(25);
        c.setFill(Color.RED);
        c.setLayoutX(xPos);
        c.setLayoutY(yPos);
        
        c.setOnMouseClicked(event -> {
            root.getChildren().removeIf(node -> node instanceof Circle);
            startTime = System.nanoTime();
            times.add(startTime);
            targetsHit++;
            if(targetsHit >= 30) {
                averageTime();
                roundOver(root);
            }
            else {
                drawTarget(root);
            }
            
        });
        root.getChildren().add(c);
    }
    
    /*
     * Background/backdrop setup for game area to make the GUI
     * look, at least, semi decent. 
     * 
     * @param pane to add backdrop to
     */
    public void setBackground(Pane root) {
        Rectangle r = new Rectangle();
        r.setLayoutX(0);
        r.setLayoutY(165.5);
        r.setWidth(1200);
        r.setHeight(414.5);
        r.setFill(Color.LIGHTSLATEGRAY);   
        
        root.getChildren().add(r);
    }
    
    /*
     * Setup for the button that appears when first shown the program.
     * The button will start the game when ever invoked. On action, the
     * game play area will be set and all game features will be setup.
     * The button will then be added to pane. 
     * 
     * @param pane to add the start button to
     */
    public void startBtnSetup(Pane root) {
        Font startFont = Font.font("TimesRoman", FontWeight.BOLD, 28);
        Group g = new Group();
        Button startGame = new Button();
        startGame.setText("Click to start");
        startGame.setLayoutX(445);
        startGame.setLayoutY(325);
        startGame.setPrefSize(300, 75);
        startGame.setFont(startFont);
        
        timeMS = new SimpleLongProperty(0);
        
        startGame.setOnAction(event -> {
            root.getChildren().removeIf(node -> node instanceof Group);
            beginTime = System.nanoTime();
            times.add(beginTime);
            drawTarget(root);
        });
        
        g.getChildren().add(startGame);
        root.getChildren().add(g);
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
        Label welLabel = new Label("Aim Trainer");        
        welLabel.setFont(titleFont);
        welLabel.setLayoutX(465);
        welLabel.setLayoutY(25);
        
        Font descFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label descLabel = new Label("Test how quickly and efficently you can hit targets.");
        descLabel.setFont(descFont);
        descLabel.setLayoutX(385);
        descLabel.setLayoutY(100);
        
        Font chooseFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label inTitleLabel = new Label("Instructions:");
        inTitleLabel.setFont(chooseFont);
        inTitleLabel.setLayoutX(220);
        inTitleLabel.setLayoutY(600);
        
        Font instFont = Font.font("TimesRoman", FontWeight.NORMAL, 20);
        Label instLabel = new Label("Click/hit the targets that appear above as quickly and accurately as possible.\n "
                + "              Your speed will display on the screen after you play.");
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
