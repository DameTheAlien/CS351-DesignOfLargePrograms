/*
 * Name: Damian Franco
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Human Benchmark (Lab 3)
 * Version: 5
 */
package benchmark;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class VerbalTest {
    /* Width and height of window */
    private double width  = 1200.0, height = 700.0;
    /* List of all the words being read in */
    private ArrayList<String> allWords = new ArrayList<String>();
    /* List of all the words that have been seen */
    private ArrayList<String> seenWords = new ArrayList<String>();
    /* Count of lives left and correct guesses */
    private int lives = 3, guessesRight = 0;
    /* A string of the current word in play */
    String currWord;
    
    /*
     * Constructor for VerbalTest object, this will call the GUI for
     * this class to be invoked and show and start the game play area.
     */
    public VerbalTest() {
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
        readFile();
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
     * Game hud will show the number of level you are at and
     * the number of lives you have left in the game. This shows
     * progression and automatically updates as the game 
     * continues.
     * 
     * @param to add components to
     */
    public void gameHUD(Pane root) {
        Group g = new Group();
        Font hudFont = Font.font("TimesRoman", FontWeight.BOLD, 30);
        Text livesText = new Text();
        livesText.setText("Lives left: " + lives);
        livesText.setFont(hudFont);
        livesText.setLayoutX(10);
        livesText.setLayoutY(250);
        
        int yuh = guessesRight + 1;
        Text scoreText = new Text();
        scoreText.setText("Score: " + Integer.toString(yuh));
        scoreText.setFont(hudFont);
        scoreText.setLayoutX(10);
        scoreText.setLayoutY(200);
        
        g.getChildren().add(livesText);
        g.getChildren().add(scoreText);
        root.getChildren().add(g);
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
        
        startGame.setOnAction(event -> {
            handleEvent(root);
        });
        
        g.getChildren().add(startGame);
        root.getChildren().add(g);
    }
    
    /*
     * This method will update the GUI by calling all the methods that will 
     * update/add things to the GUI when the event is called.
     * 
     * @param pane to add componenets to
     */
    public void handleEvent(Pane root) {
        root.getChildren().removeIf(node -> node instanceof Group);
        gameHUD(root);
        showWord(root);
        showButtons(root);
    }
    
    /*
     * This method will set up the two buttons during the game play. The
     * buttons will be added as a key component of the game. The new button
     * will be clicked when the word is presumed to be a new/unseen word. 
     * The seen button will be clicked when the word is presumed to be seen
     * already during the game. Both will call various methods and check if
     * the button was the correct choice. The methods will handle the logic
     * behind this game.
     * 
     * @param pane to add new and old buttons to
     */
    public void showButtons(Pane root) {
        Group g = new Group();
        Button newBtn = new Button();
        newBtn.setText("NEW");
        newBtn.setFont(Font.font("TimesRoman", FontWeight.SEMI_BOLD, 24));
        newBtn.setLayoutX(425);
        newBtn.setLayoutY(425);
        newBtn.setPrefSize(150, 75);
        newBtn.setOnAction(event -> {
            boolean seen = false;
            boolean check = checkIfSeen(root);
            if(check == seen) {
                addToSeen();
                handleEvent(root);
            }
            else {
                wrongChoice(root);
            }
        });
        
        Button seenBtn = new Button();
        seenBtn.setText("SEEN");
        seenBtn.setFont(Font.font("TimesRoman", FontWeight.SEMI_BOLD, 24));
        seenBtn.setLayoutX(625);
        seenBtn.setLayoutY(425);
        seenBtn.setPrefSize(150, 75);
        seenBtn.setOnAction(event -> {
            boolean seen = true;
            boolean check = checkIfSeen(root);
            if(check == seen) {
                guessesRight++;
                handleEvent(root);
            }
            else {
                wrongChoice(root);
            }
        });
        
        g.getChildren().add(newBtn);
        g.getChildren().add(seenBtn);
        root.getChildren().add(g);
    }
    
    /*
     * The wrongChoide method will subtract the number of lives the
     * player has and then will check to see if the game is over or
     * not. If the game is over, then the game over screen will show,
     * and if it is not over yet, then it will continue the game.
     * 
     * @param pane to add componenets to
     */
    public void wrongChoice(Pane root) {
        lives--;
        if(lives <= 0) {
            root.getChildren().removeIf(node -> node instanceof Group);
            gameOverShow(root);
        }
        else {
            handleEvent(root);
        }
    }
    
    /*
     * The add to seen method will be able to add the word to the seen
     * word list and will count the guess as a correct guess.
     */
    public void addToSeen() {
        guessesRight++;
        seenWords.add(currWord);
    }
    
    /*
     * This method will take in the current word in play and scan through
     * the list of seen words and if the word has been seen, then the method
     * will return true. If it has not been seen, then it will return false.
     * 
     * @param pane to add componenets to
     * @return boolean true or false if word is seen or not
     */
    public boolean checkIfSeen(Pane root) {
        if(seenWords.contains(currWord)) {
            return true;
        }
        else {
            return false;
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
        Group g = new Group();
        Font overFont = Font.font("TimesRoman", FontWeight.BOLD, 60);
        Text overText = new Text();
        overText.setText("GAME OVER");
        overText.setFont(overFont);
        overText.setLayoutX(400);
        overText.setLayoutY(300);
        overText.setFill(Color.FIREBRICK);
        
        int yuh = guessesRight + 1;
        Font overSmolFont = Font.font("TimesRoman", FontWeight.BOLD, 30);
        Text overSmolText = new Text();
        overSmolText.setText("Score achieved: " + yuh);
        overSmolText.setFont(overSmolFont);
        overSmolText.setLayoutX(450);
        overSmolText.setLayoutY(350);
        
        Font againFont = Font.font("TimesRoman", FontWeight.BOLD, FontPosture.ITALIC, 18);
        Button playAgain = new Button();
        playAgain.setText("Click to play again");
        playAgain.setLayoutX(435);
        playAgain.setLayoutY(400);
        playAgain.setPrefSize(275, 50);
        playAgain.setFont(againFont);
        
        playAgain.setOnAction(event -> {
            root.getChildren().removeIf(node -> node instanceof Group);
            guessesRight = 0;
            lives = 3;
            for(int i = 0; i < seenWords.size(); i++) {
                seenWords.remove(i);
            }
            handleEvent(root);
        });
        g.getChildren().add(overText);
        g.getChildren().add(overSmolText);
        g.getChildren().add(playAgain);
        
        root.getChildren().add(g);
    }
    
    /*
     * This method will get a word from the list of all words at random and
     * add that word to the GUI and show it to the player.
     * 
     * @param pane to add componenets to
     */
    public void showWord(Pane root) {
        currWord = allWords.get(ThreadLocalRandom.current().nextInt(0, allWords.size()));
        
        Group g = new Group();
        Font numFont = Font.font("TimesRoman", FontWeight.BOLD, 70);
        Text numText = new Text();
        numText.setText(currWord);
        numText.setFont(numFont);
        numText.setTextAlignment(TextAlignment.CENTER);
        numText.setLayoutX(445);
        numText.setLayoutY(385);
        numText.setFill(Color.WHITE);
        numText.setStroke(Color.BLACK);
       
        g.getChildren().add(numText);
        root.getChildren().add(g);
    }
    
    /*
     * This method will read in the wordBank.txt file which
     * contains all the words we will need to play the game. 
     * Then it will place every single word in the allWords
     * list.
     */
    public void readFile() {
        String path = "/wordBank.txt";
        InputStream is = getClass().getResourceAsStream(path);
        BufferedReader wordBank = new BufferedReader(new InputStreamReader(is));
        Scanner sc = null;
        sc = new Scanner(wordBank);
        while(sc.hasNext()) {
            allWords.add(sc.next());
        }
        sc.close();
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
        Label welLabel = new Label("Verbal Memory");        
        welLabel.setFont(titleFont);
        welLabel.setLayoutX(435);
        welLabel.setLayoutY(25);
        
        Font descFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label descLabel = new Label("Test on your short term memory when it comes to remembering words.");
        descLabel.setFont(descFont);
        descLabel.setLayoutX(305);
        descLabel.setLayoutY(100);
        
        Font chooseFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label inTitleLabel = new Label("Instructions:");
        inTitleLabel.setFont(chooseFont);
        inTitleLabel.setLayoutX(220);
        inTitleLabel.setLayoutY(600);
        
        Font instFont = Font.font("TimesRoman", FontWeight.NORMAL, 20);
        Label instLabel = new Label("Memorize the words above, select seen or new if the words are old/seen or new words shown.\n "
                + "                                          This test will get harder as you progress.");
        
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
