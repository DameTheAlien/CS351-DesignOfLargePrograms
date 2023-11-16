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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VisualTest extends Application {
    /* Width and height of window */
    private double width  = 1200.0, height = 700.0;
    /* Count of lives left, correct guesses, and tries the player has left */
    private int lives = 3, level = 1, tries;
    /* Timeline to show sequental events on the GUI */
    private Timeline tl;
    /* List of the blank rectangles GUI objects (has not pattern) */
    private ArrayList<Rectangle> blank = new ArrayList<Rectangle>();
    /* List of the pattern rectangles GUI objects (holds the pattern) */
    private ArrayList<Rectangle> pattern = new ArrayList<Rectangle>();
    /* List of the ID's for the right rectangles clicked */
    private ArrayList<String> rightIDs = new ArrayList<String>();
    /* Global text GUI components to remove from the GUI */
    private Text livesText, stageText, triesText;
    
    /*
     * Constructor for VisualTest object, this will call the GUI for
     * this class to be invoked and show and start the game play area.
     */
    public VisualTest() {
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
        
        setBackground(root);
        menuSetup(primaryStage, root);
        topPanelSetup(root);
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
     * Setup for the button that appears when first shown the program.
     * The button will start the game when ever invoked. On action, the
     * game play area will be set and all game features will be setup.
     * The button will then be added to pane. This will also set up the 
     * timeline to show the sequentally updating the GUI.
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
        
        tl = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            tries = 2;
            updateGUI(root);
        }));
        
        startGame.setOnAction(event -> {
            root.getChildren().removeIf(node -> node instanceof Group);
            gameHUD(root);
            blank = setupImages(root);
            pattern = setupImages(root);
            setupPattern(root);
            showPattern(root);
            tl.play();
        });
        
        g.getChildren().add(startGame);
        root.getChildren().add(g);
    }
    
    /*
     * This method will handle that next screen on the GUI when the game
     * is progressing. It will also remove the old texts and patterns on 
     * the GUI.
     * 
     * @param pane to add componenets to
     */
    public void nextScreen(Pane root) {
        root.getChildren().removeIf(node -> node instanceof Group);
        root.getChildren().removeIf(node -> node instanceof Rectangle);
        topPanelSetup(root);
        gameHUD(root);
    }
    
    /*
     * This method will update the GUI by calling all the methods that will 
     * update/add things to the GUI. It will also remove the old texts and
     * patterns on the GUI.
     * 
     * @param pane to add componenets to
     */
    public void updateGUI(Pane root) {
        root.getChildren().removeIf(node -> node instanceof Group);
        root.getChildren().removeIf(node -> node instanceof Rectangle);
        setBackground(root);
        topPanelSetup(root);
        gameHUD(root);
        setupBlank(root);
        showBlank(root);
    }
    
    /*
     * The setupBlank method will set up the images but with an all blank. This
     * will be able to show it on the GUI and allow the player to play on the
     * blank images. The blank will have the same dimensions as the pattern
     * but all the colors will be blank/white. This will also handle the action
     * when the rectangles are being clicked on. This is done by scanning 
     * through the list and setting all to blank. The action on the images
     * set the color to the right color(red) or the wrong color(grey), and
     * check the input and show the new pattern on the GUI.
     * 
     * @param pane to add componenets to
     */
    public void setupBlank(Pane root) {
        for(int i = 0; i < blank.size(); i++) {
            blank.get(i).setFill(Color.WHITESMOKE);
            final Integer play = i;
            blank.get(i).setOnMouseClicked(event -> {
                Rectangle curr = blank.get(play);
                checkInput(root, curr);
                root.getChildren().removeIf(node -> node instanceof Group);
                gameHUD(root);
            });
        }
    }
    
    /*
     * This method will clear all of the lists to nothing when the pattern
     * needs to be reset.
     */
    public void clearLists() {
        blank.removeAll(blank);
        pattern.removeAll(pattern);
        rightIDs.removeAll(rightIDs);
    }
    
    /*
     * This will scan through the list of choices that are inserted by the
     * player and see if the choices are either all right so that the game
     * could progress. First, it will scan through the list and get the IDs
     * of the pattern list. If the ID's are all equal to the right choices
     * array, then the pattern is solved and the game progresses. 
     * 
     * @param pane to add componenets to
     * @param list of right/wrong choice IDs
     */
    public void checkIfRight(Pane root, ArrayList<String> rightChoices) {
        ArrayList<String> patID = new ArrayList<String>();
        for(int i = 0; i < pattern.size(); i++) {
            if(pattern.get(i).getFill().equals(Color.FIREBRICK)) {
                patID.add(pattern.get(i).getId());
            }
        }
        if(rightChoices.containsAll(patID)) {
            root.getChildren().removeIf(node -> node instanceof Group);
            root.getChildren().removeIf(node -> node instanceof Rectangle);
            tl.stop();
            level++;
            clearLists();
            setBackground(root);
            topPanelSetup(root);
            gameHUD(root);
            blank = setupImages(root);
            pattern = setupImages(root);
            setupPattern(root);
            showPattern(root);
            tl.play();
        }
    }
    
    /*
     * This will check the current clicked image and get the ID of the
     * image, and then it will either change the color to red/firebrick
     * if the color is right, and it will progress the game or end if
     * the pattern is solved. If the rectangles ID was not a right choice
     * then it will change the color of the rectangle to gray. And
     * the number of tries will decrement. If the number of tries is
     * less than 0, then the lives will lose and it will check if the game
     * is over or not.
     * 
     * @param pane to add componenets to
     * @param rectangle of the current clicked image
     */
    public void checkInput(Pane root, Rectangle r) {
        String currentPlay = r.getId();
        int id = Integer.parseInt(currentPlay);
        if(pattern.get(id).getFill().equals(Color.FIREBRICK)) {
            blank.get(id).setFill(Color.FIREBRICK);
            rightIDs.add(currentPlay);
            checkIfRight(root, rightIDs);
        }
        else {
            blank.get(id).setFill(Color.GRAY);
            tries--;
            if(tries < 0) {
                lives--;
                checkGameOver(root);
            }
        }
    }
    
    /*
     * This will take the number of lives and see if it is
     * at 0 lives or less than 0 lives, and it will show the game
     * over screen if it is not. If there are still lives left, then
     * the game will progress with one life less. 
     * 
     * @param pane to add componenets to
     */
    public void checkGameOver(Pane root) {
        if(lives <= 0) {
            root.getChildren().removeIf(node -> node instanceof Group);
            root.getChildren().removeIf(node -> node instanceof Rectangle);
            lives = 0;
            tries = 0;
            setBackground(root);
            topPanelSetup(root);
            gameHUD(root);
            gameOverShow(root);
        }
        else {
            root.getChildren().removeIf(node -> node instanceof Group);
            root.getChildren().removeIf(node -> node instanceof Rectangle);
            tl.stop();
            tries = 0;
            clearLists();
            setBackground(root);
            topPanelSetup(root);
            gameHUD(root);
            blank = setupImages(root);
            pattern = setupImages(root);
            setupPattern(root);
            showPattern(root);
            tl.play();
        }
    }
    
    /*
     * This is the screen that shows when the game is over. All this method does
     * is sets up the GUI when the game is over to display the level in which
     * that player has achieved and a button for the option to play again. When
     * the play again button is pressed, all the game logic will reset to its
     * original state. 
     * 
     * @param pane to add componenets to
     */
    public void gameOverShow(Pane root) {
        Font overFont = Font.font("TimesRoman", FontWeight.BOLD, 60);
        Text overText = new Text();
        overText.setText("GAME OVER");
        overText.setFont(overFont);
        overText.setLayoutX(435);
        overText.setLayoutY(300);
        overText.setFill(Color.FIREBRICK);
        
        int yuh = level;
        Font overSmolFont = Font.font("TimesRoman", FontWeight.BOLD, 30);
        Text overSmolText = new Text();
        overSmolText.setText("Level achieved: " + yuh);
        overSmolText.setFont(overSmolFont);
        overSmolText.setLayoutX(485);
        overSmolText.setLayoutY(350);
        
        Font againFont = Font.font("TimesRoman", FontWeight.BOLD, FontPosture.ITALIC, 18);
        Button playAgain = new Button();
        playAgain.setText("Click to play again");
        playAgain.setLayoutX(465);
        playAgain.setLayoutY(400);
        playAgain.setPrefSize(275, 50);
        playAgain.setFont(againFont);
        
        playAgain.setOnAction(event -> {
            root.getChildren().removeIf(node -> node instanceof Group);
            root.getChildren().removeIf(node -> node instanceof Rectangle);
            tl.stop();
            tries = 0;
            level = 1;
            lives = 3;
            clearLists();
            setBackground(root);
            topPanelSetup(root);
            gameHUD(root);
            blank = setupImages(root);
            pattern = setupImages(root);
            setupPattern(root);
            showPattern(root);
            tl.play();
        });
        
        root.getChildren().add(overText);
        root.getChildren().add(overSmolText);
        root.getChildren().add(playAgain);
    }
    
    /*
     * This method will run through the current images and it will assign
     * random rectangles to the firebrick/red color. Those are the colors
     * that will create the pattern effect on the screen.
     * 
     * @param pane to add componenets to
     */
    public void setupPattern(Pane root) {
        if(level <= 3) {
            if(level == 1) {
                for(int i = 0; i < 3; i++) {
                    pattern.get(ThreadLocalRandom.current().nextInt(0, pattern.size())).setFill(Color.FIREBRICK);
                }
            }
            else {
                for(int i = 0; i < 4; i++) {
                    pattern.get(ThreadLocalRandom.current().nextInt(0, pattern.size())).setFill(Color.FIREBRICK);
                }
            }
        }
        else if(level > 3 && level < 8) {
            if(level < 5) {
                for(int i = 0; i < 6; i++) {
                    pattern.get(ThreadLocalRandom.current().nextInt(0, pattern.size())).setFill(Color.FIREBRICK);
                }
            }
            else {
                for(int i = 0; i < 10; i++) {
                    pattern.get(ThreadLocalRandom.current().nextInt(0, pattern.size())).setFill(Color.FIREBRICK);
                }
            }
        }
        else {
            if(level < 12) {
                for(int i = 0; i < 13; i++) {
                    pattern.get(ThreadLocalRandom.current().nextInt(0, pattern.size())).setFill(Color.FIREBRICK);
                }
            }
            else {
                for(int i = 0; i < 20; i++) {
                    pattern.get(ThreadLocalRandom.current().nextInt(0, pattern.size())).setFill(Color.FIREBRICK);
                }
            }
        }
    }
    
    /*
     * This will show the pattern on the screen so the player can
     * remember it and insert it.
     * 
     * @param pane to add the pattern images to
     */
    public void showPattern(Pane root) {
        for(int i = 0; i < pattern.size(); i++) {
            root.getChildren().add(pattern.get(i));
        }
    }
    
    /*
     * This will show the blank pattern on the screen so 
     * the player can play on it.
     * 
     * @param pane to add the pattern images to
     */
    public void showBlank(Pane root) {
        for(int i = 0; i < blank.size(); i++) {
            root.getChildren().add(blank.get(i));
        }
    }
    
    /*
     * This is set up the images in the game. It will assign how many
     * rectangles we have according to the level and it will set up the
     * way it is displayed on the GUI.
     * 
     * @param pane to add the pattern images to
     */
    public ArrayList<Rectangle> setupImages(Pane root) {
        double xMov = 465;
        ArrayList<Rectangle> imgs = new ArrayList<Rectangle>();
        if(level <= 3) {
            for(int i = 0; i < 9; i++) {
                Rectangle r = new Rectangle();
                r.setFill(Color.WHITE);
                r.setStroke(Color.BLACK);
                r.setWidth(75);
                r.setHeight(75);
                if(i < 3) {
                    r.setLayoutX(xMov);
                    r.setLayoutY(225);
                    xMov += 100;
                }
                else if(i >= 3 && i < 6) {
                    r.setLayoutX(xMov - 300);
                    r.setLayoutY(325);
                    xMov += 100;
                }
                else {
                    r.setLayoutX(xMov - 600);
                    r.setLayoutY(425);
                    xMov += 100;
                }
                r.setId("" + i);
                imgs.add(r);
            }
        }
        else if(level > 3 && level <= 8) {
            for(int i = 0; i < 16; i++) {
                Rectangle r = new Rectangle();
                r.setFill(Color.WHITE);
                r.setStroke(Color.BLACK);
                r.setWidth(75);
                r.setHeight(75);
                if(i < 4) {
                    r.setLayoutX(xMov - 50);
                    r.setLayoutY(185);
                    xMov += 100;
                }
                else if(i >= 4 && i < 8) {
                    r.setLayoutX(xMov - 450);
                    r.setLayoutY(285);
                    xMov += 100;
                }
                else if(i >= 8 && i < 12) {
                    r.setLayoutX(xMov - 850);
                    r.setLayoutY(385);
                    xMov += 100;
                }
                else {
                    r.setLayoutX(xMov - 1250);
                    r.setLayoutY(485);
                    xMov += 100;
                }
                r.setId("" + i);
                imgs.add(r);
            }
        }
        else {
            for(int i = 0; i < 25; i++) {
                Rectangle r = new Rectangle();
                r.setFill(Color.WHITE);
                r.setStroke(Color.BLACK);
                r.setWidth(65);
                r.setHeight(65);
                if(i < 5) {
                    r.setLayoutX(xMov - 50);
                    r.setLayoutY(190);
                    xMov += 75;
                }
                else if(i >= 5 && i < 10) {
                    r.setLayoutX(xMov - 425);
                    r.setLayoutY(265);
                    xMov += 75;
                }
                else if(i >= 10 && i < 15) {
                    r.setLayoutX(xMov - 800);
                    r.setLayoutY(340);
                    xMov += 75;
                }
                else if(i >= 15 && i < 20) {
                    r.setLayoutX(xMov - 1175);
                    r.setLayoutY(415);
                    xMov += 75;
                }
                else {
                    r.setLayoutX(xMov - 1550);
                    r.setLayoutY(490);
                    xMov += 75;
                }
                r.setId("" + i);
                imgs.add(r);
            }
        }
        return imgs;
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
        r.setFill(Color.DODGERBLUE);  
        
        Rectangle backR = new Rectangle();
        backR.setX(0);
        backR.setY(0);
        backR.setWidth(1200);
        backR.setHeight(165);
        backR.setFill(Color.AQUAMARINE);
        
        root.getChildren().add(backR);
        root.getChildren().add(r);
    }
    
    /*
     * Game hud will show the number of level you are at and
     * the number of lives and tries you have left in the game. 
     * This shows progression and automatically updates as the 
     * game continues.
     * 
     * @param to add components to
     */
    public void gameHUD(Pane root) {
        Group g = new Group();
        Font hudFont = Font.font("TimesRoman", FontWeight.BOLD, 30);
        livesText = new Text();
        livesText.setText("Lives left: " + lives);
        livesText.setFont(hudFont);
        livesText.setLayoutX(10);
        livesText.setLayoutY(250);
        livesText.setFill(Color.WHITESMOKE);
        
        int yuh = level;
        stageText = new Text();
        stageText.setText("Stage: " + Integer.toString(yuh));
        stageText.setFont(hudFont);
        stageText.setLayoutX(10);
        stageText.setLayoutY(200);
        stageText.setFill(Color.WHITESMOKE);
        
        int woah = tries;
        triesText = new Text();
        triesText.setText("Tries left: " + woah);
        triesText.setFont(hudFont);
        triesText.setLayoutX(850);
        triesText.setLayoutY(375);
        triesText.setFill(Color.WHITESMOKE);
        
        g.getChildren().add(livesText);
        g.getChildren().add(stageText);
        g.getChildren().add(triesText);
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
        Label welLabel = new Label("Visual Memory");    
        welLabel.setFont(titleFont);
        welLabel.setLayoutX(435);
        welLabel.setLayoutY(25);
        
        Font descFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label descLabel = new Label("Test on your short term memory when it comes to images.");
        descLabel.setFont(descFont);
        descLabel.setLayoutX(360);
        descLabel.setLayoutY(100);
        
        Font chooseFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label inTitleLabel = new Label("Instructions:");
        inTitleLabel.setFont(chooseFont);
        inTitleLabel.setLayoutX(220);
        inTitleLabel.setLayoutY(600);
        
        Font instFont = Font.font("TimesRoman", FontWeight.NORMAL, 20);
        Label instLabel = new Label("Memorize the square patterns above and click the same pattern onto the blank squares.\n "
                + "                                This test will get harder as you progress.");
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
        
        root.getChildren().add(welLabel);
        root.getChildren().add(descLabel);
        root.getChildren().add(inTitleLabel);
        root.getChildren().add(instLabel);
        root.getChildren().add(bottomLine);
        root.getChildren().add(titleLine);
    }
}



