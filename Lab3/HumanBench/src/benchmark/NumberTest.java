package benchmark;

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
import javafx.scene.control.TextField;
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
import javafx.stage.Stage;
import javafx.util.Duration;

public class NumberTest extends Application {
    /* Width and height of window */
    private double width  = 1200.0, height = 700.0;
    /* Intialization of the text field to submit answer in */
    private TextField tf;
    /* Button to submit the answer when clicked */
    private Button submit;
    /* Timeline to show sequental events on the GUI */
    private Timeline tl;
    /* Count of lives left, correct guesses, and x position for GUI layout */
    private int lives = 3, guessesRight = 0, xMov = 550;
    /* A string to hold the right answer in a string */
    private String rightAns;
    
    /*
     * Constructor for NumberTest object, this will call the GUI for
     * this class to be invoked and show and start the game play area.
     */
    public NumberTest() {
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
     * invoke the show method to show the GUI. This will also set up the 
     * timeline to show the sequentally updating the GUI.
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
        
        tl = new Timeline(new KeyFrame(Duration.seconds(6), event -> {
            root.getChildren().removeIf(node -> node instanceof Group);
            gameHUD(root);
            showTextBox(root);
        }));
        
        startGame.setOnAction(event -> {
            gameHUD(root);
            handleEvent(root);
        });
        
        g.getChildren().add(startGame);
        root.getChildren().add(g);
    }
    
    /*
     * Handles the method calling that will set up the game GUI
     * and play logic as it is invoked.
     * 
     * @param pane to add componenets to
     */
    public void handleEvent(Pane root) {
        root.getChildren().removeIf(node -> node instanceof Group);
        gameHUD(root);
        showNumber(root);
        tl.play();
    }
    
    /*
     * Sets up the GUI labels and the play again button on the
     * pane when the game is over. This also will show the
     * level/ round count achieved on screen. The method
     * also handles the action on the button, when the button 
     * is clicked the game is fully reset and the game area is 
     * set up to be played again.
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
        overSmolText.setText("Stage achieved: " + yuh);
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
            handleEvent(root);
        });
        
        g.getChildren().add(overText);
        g.getChildren().add(overSmolText);
        g.getChildren().add(playAgain);
        
        root.getChildren().add(g);
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
        Text stageText = new Text();
        stageText.setText("Stage: " + Integer.toString(yuh));
        stageText.setFont(hudFont);
        stageText.setLayoutX(10);
        stageText.setLayoutY(200);
        
        g.getChildren().add(livesText);
        g.getChildren().add(stageText);
        root.getChildren().add(g);
    }
    
    /*
     * This method handles when the submit button is pressed. It will first
     * stop the timeline and get the text in the text field and compare it
     * to the right answer. If the answer is right, then it will count the
     * answers as right and continue the game. If the answer is wrong, then
     * it will subtract/lose a life and it will check if there are no lives
     * left. If there are no lives, then the game will end, if there are not
     * it will continue the game.
     * 
     * @param to add components to
     */
    public void handleSubmit(Pane root) {
        tl.stop();
        if(tf.getText().equals(rightAns)) {
            root.getChildren().removeIf(node -> node instanceof Group);
            guessesRight++;
            handleEvent(root);
        }
        else {
            lives--;
            if(lives == 0) {
                root.getChildren().removeIf(node -> node instanceof Group);
                gameOverShow(root);
            }
            else {
                handleEvent(root);
            }
        }
    }
    
    /*
     * This method will generate a number based on the level and will
     * get sequentally harder as the game increases. Then it will add
     * the number and show it on the GUI and place it in the right
     * answer string.
     * 
     * @param to add the number text to
     */
    public void showNumber(Pane root) {
        int num = 0, numExtend = 0;
        xMov = 550;
        switch(guessesRight) {
            case 0:
                num = ThreadLocalRandom.current().nextInt(0, 9);
                rightAns = String.valueOf(num);
                break;
            case 1:
                num = ThreadLocalRandom.current().nextInt(10, 99);
                rightAns = String.valueOf(num);
                xMov -= 10;
                break;
            case 2:
                num = ThreadLocalRandom.current().nextInt(100, 999);
                rightAns = String.valueOf(num);
                xMov -= 30;
                break;
            case 3:
                num = ThreadLocalRandom.current().nextInt(1000, 9999);
                rightAns = String.valueOf(num);
                xMov -= 55;
                break;
            case 4:
                num = ThreadLocalRandom.current().nextInt(10_000, 99_999);
                rightAns = String.valueOf(num);
                xMov -= 95;
                break;
            case 5:
                num = ThreadLocalRandom.current().nextInt(100_000, 999_999);
                rightAns = String.valueOf(num);
                xMov -= 125;
                break;
            case 6:
                num = ThreadLocalRandom.current().nextInt(1_000_000, 9_999_999);
                rightAns = String.valueOf(num);
                xMov -= 155;
                break;
            case 7:
                num = ThreadLocalRandom.current().nextInt(10_000_000, 99_999_999);
                rightAns = String.valueOf(num);
                xMov -= 185;
                break;
            case 8:
                num = ThreadLocalRandom.current().nextInt(100_000_000, 999_999_999);
                rightAns = String.valueOf(num);
                xMov -= 205;
                break;
            case 9:
                num = ThreadLocalRandom.current().nextInt(1_000_000_000, 2_000_000_000);
                numExtend = ThreadLocalRandom.current().nextInt(0, 9);
                xMov -= 265;
                rightAns = String.valueOf(num) + String.valueOf(numExtend);
                break;
            case 10:
                num = ThreadLocalRandom.current().nextInt(1_000_000_000, 2_000_000_000);
                numExtend = ThreadLocalRandom.current().nextInt(10, 99);
                rightAns = String.valueOf(num) + String.valueOf(numExtend);
                xMov -= 305;
                break;
            default:
                num = ThreadLocalRandom.current().nextInt(1_000_000_000, 2_000_000_000);
                numExtend = ThreadLocalRandom.current().nextInt(10_000, 99_999);
                rightAns = String.valueOf(num) + String.valueOf(numExtend);
                xMov -= 375;
                break;  
        }
        Group g = new Group();
        Font numFont = Font.font("TimesRoman", FontWeight.BOLD, 100);
        Text numText = new Text();
        numText.setText(rightAns);
        numText.setFont(numFont);
        numText.setLayoutX(xMov);
        numText.setLayoutY(400);
        numText.setFill(Color.WHITE);
        numText.setStroke(Color.BLACK);
       
        g.getChildren().add(numText);
        root.getChildren().add(g);
    }
    
    /*
     * This method will add the aesthetics around the GUI
     * like various text labels. And it will add the text
     * field to submit the answer in.
     * 
     * @param to add components to
     */
    public void showTextBox(Pane root) {
        Group g = new Group();
        Text numText = new Text();
        numText.setText("Enter your answer below:");
        numText.setFont(Font.font("TimesRoman", FontWeight.BOLD, 40));
        numText.setLayoutX(365);
        numText.setLayoutY(300);
        numText.setFill(Color.BLACK);
        
        submit = new Button();
        submit.setText("Click or press ENTER to submit");
        submit.setFont(Font.font("TimesRoman", FontWeight.SEMI_BOLD, 12));
        submit.setLayoutX(445);
        submit.setLayoutY(425);
        submit.setPrefSize(300, 35);
        root.getChildren().add(submit);
        submit.setOnAction(event -> {
            handleSubmit(root);
        });
        
        tf = new TextField();
        tf.setLayoutX(445);
        tf.setLayoutY(325);
        tf.setPrefSize(300, 75);
        tf.setPromptText("ENTER YOUR ANSWER HERE");
        tf.setFont(Font.font("TimesRoman", FontWeight.BOLD, 20));
        
        tf.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                handleSubmit(root);
            }
        });
        
        g.getChildren().add(numText);
        g.getChildren().add(tf);
        g.getChildren().add(submit);
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
        Label welLabel = new Label("Number Memory");        
        welLabel.setFont(titleFont);
        welLabel.setLayoutX(405);
        welLabel.setLayoutY(25);
        
        Font descFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label descLabel = new Label("Test on your short term memory when it comes to numbers.");
        descLabel.setFont(descFont);
        descLabel.setLayoutX(345);
        descLabel.setLayoutY(100);
        
        Font chooseFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label inTitleLabel = new Label("Instructions:");
        inTitleLabel.setFont(chooseFont);
        inTitleLabel.setLayoutX(220);
        inTitleLabel.setLayoutY(600);
        
        Font instFont = Font.font("TimesRoman", FontWeight.NORMAL, 20);
        Label instLabel = new Label("Remember the number when it shows, then type that number in the text box when it appear.\n "
                + "                                       This test will get harder as you progress.");
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
