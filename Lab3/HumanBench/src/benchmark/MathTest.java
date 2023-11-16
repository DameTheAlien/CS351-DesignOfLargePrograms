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

public class MathTest extends Application {
    /* Width and height of window */
    private double width  = 1200.0, height = 700.0;
    /* Holds the time as a long property to display correctly on GUI */
    private LongProperty timeMS;
    /* Intialization of the text field to submit answer in */
    private TextField tf;
    /* Button to submit the answer when clicked */
    private Button submit;
    /* List to add the times between clicks to */
    private ArrayList<Long> times = new ArrayList<Long>();
    /* Array that holds the math operators */
    private char[] ops = {'+', '-'};
    /* Count of correct guesses, the answer, and the round count */
    private int rightGuesses, ans, roundCount = 1;
    /* Holds the nano seconds time when reaction time starts and ends */
    private long beginTime, startTime;
    
    /*
     * Constructor for MathTest object, this will call the GUI for
     * this class to be invoked and show and start the game play area.
     */
    public MathTest() {
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
     * This method will take a random number from 0 to 100 and then a
     * random math operator between - and +. I orginally had multiplication
     * and division but it was too hard for my small brain. Then It will
     * put the equation and display it on the screen. Under the equation
     * a text field will be intialized and will display on the GUI for 
     * the answer to be inserted by the user. When the button or the
     * key ENTER is pressed, then it will handle the input.
     * 
     * @param to add components to
     */
    public void showEquation(Pane root) {
        int firstVar = ThreadLocalRandom.current().nextInt(0, 100);
        int secondVar = ThreadLocalRandom.current().nextInt(0, 100);
        char currOp = ops[ThreadLocalRandom.current().nextInt(0, ops.length)]; 
        ans = 0;
        
        switch(currOp) {
            case '+':
                ans = firstVar + secondVar;
                break;
            case '-':
                ans = firstVar - secondVar;
                break;
            default:
                break;
        }
        
        Group g = new Group();
        Font overFont = Font.font("TimesRoman", FontWeight.BOLD, 60);
        String str = firstVar + " " + currOp + " " + secondVar + " = ?";
        Text equation = new Text();
        equation.setText(str);
        equation.setFont(overFont);
        equation.setLayoutX(440);
        equation.setLayoutY(350);
        equation.setFill(Color.WHITE);
        equation.setStroke(Color.BLACK);
        
        tf = new TextField();
        tf.setLayoutX(445);
        tf.setLayoutY(375);
        tf.setPrefSize(300, 75);
        tf.setPromptText("ENTER YOUR ANSWER HERE");
        tf.setFont(Font.font("TimesRoman", FontWeight.BOLD, 20));
        
        tf.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                handleAction(root);
            }
        });
       
        g.getChildren().add(equation);
        g.getChildren().add(tf);
        
        root.getChildren().add(g);
    }
    
    /*
     * This method will set up the submit button mthod for the player
     * to submit their answer. When the button is clicked the method
     * called will handle the submitting.
     * 
     * @param to add the submit button to
     */
    public void submitBtnSetup(Pane root) {
        submit = new Button();
        submit.setText("Click or press ENTER to submit");
        submit.setFont(Font.font("TimesRoman", FontWeight.SEMI_BOLD, 12));
        submit.setLayoutX(445);
        submit.setLayoutY(475);
        submit.setPrefSize(300, 35);
        root.getChildren().add(submit);
        submit.setOnAction(event -> {
            handleAction(root);
        });
    }
    
    /*
     * The handleAction method will get whatever is inserted in
     * the text field and compare it to the answer. If the answer
     * is the right answer, the level will move on and show a new 
     * equation. It will also check if the game is over by checking
     * if the level is greater or equal to 10. If the input is wrong
     * then it will show that it is not the right answer on the screen.
     * 
     * @param to add components to
     */
    public void handleAction(Pane root) {
        int input = Integer.parseInt(tf.getText());
        if(ans == input) {
            root.getChildren().removeIf(node -> node instanceof Group);
            root.getChildren().removeIf(node -> node instanceof Text);
            root.getChildren().remove(submit);
            startTime = System.nanoTime();
            times.add(startTime);
            rightGuesses++;
            if(rightGuesses >= 10) {
                averageTime();
                roundOver(root);
            }
            else {
                showEquation(root);
                submitBtnSetup(root);
            }
        }
        else {
            Text wrongAns = new Text();
            wrongAns.setText("Wrong answer, try again");
            wrongAns.setFont(Font.font("TimesRoman", FontWeight.SEMI_BOLD, 25));
            wrongAns.setLayoutX(800);
            wrongAns.setLayoutY(420);
            wrongAns.setFill(Color.RED);
            root.getChildren().add(wrongAns);
        }
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
        over.setText("Your average time per equation is:\n"
                + "                             ms");
        over.setLayoutX(360);
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
            rightGuesses = 0;
            for(int i = 0; i < times.size(); i++) {
                times.remove(i);
            }
            showEquation(root);
            submitBtnSetup(root);
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
        long avg = milliVal / 10;
        
        switch(roundCount) {
            case 1:
                timeMS.set(avg);
                break;
            case 2:
                avg -= 10000;
                timeMS.set(avg);
                break;
            case 3:
                avg -= 20000;
                timeMS.set(avg);
                break;
            case 4:
                avg -= 30000;
                timeMS.set(avg);
                break;
            case 5:
                avg -= 40000;
                timeMS.set(avg);
                break;
            default:
                break;
        }
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
        timeMS = new SimpleLongProperty(0);
        Button startGame = new Button();
        startGame.setText("Click to start");
        startGame.setLayoutX(445);
        startGame.setLayoutY(325);
        startGame.setPrefSize(300, 75);
        startGame.setFont(startFont);
        startGame.setOnAction(event -> {
            root.getChildren().removeIf(node -> node instanceof Group);
            beginTime = System.nanoTime();
            times.add(beginTime);
            showEquation(root);
            submitBtnSetup(root);
        });
        
        g.getChildren().add(startGame);
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
        Label welLabel = new Label("Quick Maths");        
        welLabel.setFont(titleFont);
        welLabel.setLayoutX(455);
        welLabel.setLayoutY(25);
        
        Font descFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label descLabel = new Label("Test to see how quick and accurate your simple math skills are.");
        descLabel.setFont(descFont);
        descLabel.setLayoutX(325);
        descLabel.setLayoutY(100);
        
        Font chooseFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label inTitleLabel = new Label("Instructions:");
        inTitleLabel.setFont(chooseFont);
        inTitleLabel.setLayoutX(220);
        inTitleLabel.setLayoutY(600);
        
        Font instFont = Font.font("TimesRoman", FontWeight.NORMAL, 20);
        Label instLabel = new Label("Do the simple math problems above with quickness, type your answer in the text box.\n "
                + "                        Your speed will display on the screen after you play.");
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
