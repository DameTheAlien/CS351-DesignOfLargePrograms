/*
 * Name: Damian Franco
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Human Benchmark (Lab 3)
 * Version: 5
 */
package benchmark;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class TypingTest extends Application {
    /* Width and height of window */
    private double width  = 1200.0, height = 700.0;
    /* Array that holds all the paragraphs */
    private String[] paragraphs;
    /* String for the current/choosen paragraph and the input character */
    private String currParagraph, inputChar;
    /* Button for the main menu */
    private Button menuBtn;
    /* Double for the X position of the text/paragraph */
    private double xMov = 585;
    /* List of characters of the current paragraph */
    private ArrayList<Character> currCharArr = new ArrayList<Character>();
    /* Integer holding the current index of the paragraph the player is at, 
       and the word per minute representation */
    private int currIndex = 0, wpm;
    /* Long variables holding the start and end time of the game in milliseconds */
    private long startTime, endTime;
    
    /*
     * Constructor for Typing Test object, this will call the GUI for
     * this class to be invoked and show and start the game play area.
     */
    public TypingTest() {
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
        
        BackgroundFill bgFill = new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bgFill);
        root.setBackground(bg);
        
        Scene scene = new Scene(root, width, height);
        setBackground(root);
        topPanelSetup(root);
        menuSetup(primaryStage, root);
        startBtnSetup(root, scene);
        
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
    
    /*
     * This method will handle the input from the user on the key
     * board. I till get the key board and set our current input
     * character to whatever key is pressed. This will also call
     * the handle event method.
     * 
     * @param pane to add componenets to
     * @param stage to manipulate
     */
    public void keyHandler(Scene scene, Pane root) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if(code.equals(KeyCode.ESCAPE)) {
                menuBtn.setDisable(false);
            }
            if(code.equals(KeyCode.QUOTE)) {
                inputChar = "\'";
            }
            else {
                inputChar = code.getChar();
            }
            handleEvent(root, scene);
        });
    }
    
    /*
     * This method will be called when the game starts to get the
     * start time of the game to calculate the WPM.
     */
    public void startTimer() {
        startTime = System.nanoTime();
    }
    
    /*
     * This method will read in the contents of the paragraphBank
     * text file and place them all in the paragraphs string array.
     */
    public void readParagraph() {
      String path = "/paragraphBank.txt";
      InputStream is = getClass().getResourceAsStream(path);
      String contents = null;
      try {
          contents = new String(is.readAllBytes());
      } catch (IOException e) {
          e.printStackTrace();
      }
      paragraphs = contents.split("\\r?\n");
    }
    
    /*
     * This method will get a random paragraph from the paragraph array and then 
     * put that char array into the list of characters. Every character in the
     * paragraph will be inserted into the character list.
     */
    public void pickParagraph() {
        currParagraph = paragraphs[ThreadLocalRandom.current().nextInt(0, paragraphs.length)];
        char[] cArr = currParagraph.toCharArray();
        for(char c : cArr) {
            currCharArr.add(c);
        }
    }
    
    /*
     * This method clears the char list that holds the current paragraphs
     * representation all in characters.
     */
    public void clearLists() {
        currCharArr.removeAll(currCharArr);
    }
    
    /*
     * Setup for the button that appears when first shown the program.
     * The button will start the game when ever invoked. On action, the
     * game play area will be set and all game features will be setup.
     * The button will then be added to pane. 
     * 
     * @param pane to add the start button to
     */
    public void startBtnSetup(Pane root, Scene scene) {
        Font startFont = Font.font("TimesRoman", FontWeight.BOLD, 28);
        Group g = new Group();
        Button startGame = new Button();
        startGame.setText("Click to start");
        startGame.setLayoutX(445);
        startGame.setLayoutY(325);
        startGame.setPrefSize(300, 75);
        startGame.setFont(startFont);
        
        startGame.setOnAction(event -> {
            root.getChildren().removeIf(node -> node instanceof Group);
            keyHandler(scene, root);
            readParagraph();
            pickParagraph();
            showParagraph(root);
            startTimer();
            menuBtn.setDisable(true);
        });
        
        g.getChildren().add(startGame);
        root.getChildren().add(g);
    }
    
    
    /*
     * This method will take the current input from the user and the
     * current char in the paragraph and compare them. If the current
     * input and current char are the same, then that means the input
     * was right, so the current index is incremented and the GUI will
     * repaint to show the progression. It also checks if the paragraph
     * is done typing, if it is, then it will show the endscreen and
     * calculate the WPM.
     * 
     * @param pane to add componenets to
     * @param stage to manipulate
     */
    public void handleEvent(Pane root, Scene scene) {
        String currChar = currCharArr.get(currIndex).toString();
        String input = inputChar;
        if(currChar.equals(input.toUpperCase()) || currChar.equals(input.toLowerCase())) {
            root.getChildren().removeIf(node -> node instanceof Group);
            currIndex++;
            xMov -= 20;
            showParagraph(root);
        }
        if(currIndex == currParagraph.length()) {
            root.getChildren().removeIf(node -> node instanceof Group);
            endTime = System.nanoTime();
            calculateWPM();
            endGame(root, scene);
        }
    }
    
    /*
     * This method will calculate the words per minute of the user after
     * the game has ended. The words per minute is calculated by getting the
     * current char length * 5 and then dividing it by the time taken to do 
     * the game.
     */
    public void calculateWPM() {
        int calc = 5 * currParagraph.length();
        long t = endTime - startTime;
        long milliValue = TimeUnit.NANOSECONDS.toMillis(t);
        int time = Math.toIntExact(milliValue);
        time = time / 1000;
        int fin = calc / time;
        fin = fin + 20;
        wpm = fin;
    }
    
    /*
     * This is the screen that shows when the game is over. All this method does
     * is sets up the GUI when the game is over to display the level in which
     * that player has achieved and a button for the option to play again. When
     * the play again button is pressed, all the game logic will reset to its
     * original state. 
     * 
     * @param pane to add componenets to
     * @param stage to manipulate
     */
    public void endGame(Pane root, Scene scene) {
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
        over.setText("You type:           words per minute!");
        over.setLayoutX(360);
        over.setLayoutY(300);
        over.setFont(overFont);
        
        Label avgMS = new Label();
        avgMS.setText(String.valueOf(wpm));
        avgMS.setFont(Font.font("TimesRoman", FontWeight.BOLD, 50));
        avgMS.setLayoutX(510);
        avgMS.setLayoutY(285);
        avgMS.setTextFill(Color.GHOSTWHITE);
        
        playAgain.setOnAction(event -> {
            root.getChildren().removeIf(node -> node instanceof Group);
            currParagraph = "";
            inputChar = "";
            xMov = 585;
            clearLists();
            currIndex = 0;
            keyHandler(scene, root);
            pickParagraph();
            showParagraph(root);
            menuBtn.setDisable(true);
        });
        
        g.getChildren().add(playAgain);
        g.getChildren().add(over);
        g.getChildren().add(avgMS);
        root.getChildren().add(g);
    }
    
    /*
     * This method will get the current paragraph in play and display
     * it on the play area. This will accomidate for when the paragraph
     * needs to move to the left when the user is typing, that way the
     * screen goes with the paragraph as the game is being played.
     * 
     * @param pane to add text to
     */
    public void showParagraph(Pane root) {
        Group g = new Group();
        Text paraText = new Text();
        paraText.setFont(Font.font("TimesRoman", FontWeight.BLACK, 45));
        paraText.setTextAlignment(TextAlignment.CENTER);
        paraText.setLayoutX(xMov);
        paraText.setLayoutY(375);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < currCharArr.size(); i++) {
            sb.append(currCharArr.get(i));
            paraText.setText(sb.toString());
            paraText.setFill(Color.BLACK);
        }
        paraText.setSelectionStart(currIndex);
        paraText.setSelectionEnd(currIndex + 1);
        paraText.setSelectionFill(Color.FIREBRICK);
        
        g.getChildren().add(paraText);
        root.getChildren().add(g);
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
     * This method will setup the main menu button seen throughout
     * the program on the bottom left. This button will take the 
     * user back to the main menu selection screen. This will also 
     * close the current game GUI window open.
     * 
     * @param main stage to close on click
     * @param pane to add components to
     */
    public void menuSetup(Stage primaryStage, Pane root) {
        menuBtn = new Button();
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
        Label welLabel = new Label("Typing Test");        
        welLabel.setFont(titleFont);
        welLabel.setLayoutX(465);
        welLabel.setLayoutY(25);
        
        Font descFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label descLabel = new Label("Test to check what type of typing skills you are working with.");
        descLabel.setFont(descFont);
        descLabel.setLayoutX(335);
        descLabel.setLayoutY(100);
        
        Font chooseFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label inTitleLabel = new Label("Instructions:");
        inTitleLabel.setFont(chooseFont);
        inTitleLabel.setLayoutX(220);
        inTitleLabel.setLayoutY(600);
        
        Font instFont = Font.font("TimesRoman", FontWeight.NORMAL, 20);
        Label instLabel = new Label("Type the paragraph above with the utmost quickness and accuracy as possible.\n "
                + "                    Your speed will display on the screen after you play.");
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
