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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ChimpTest extends Application {
    /* Width and height of window */
    private double width  = 1200.0, height = 700.0;
    /* Count of level at, lives left, current number, current image ID, and end number */
    private int level = 1, lives = 3, numberUp = 4, currID = 1, endInt;
    /* List of all the images/rectangles in the level */
    private ArrayList<Rectangle> numberRects = new ArrayList<Rectangle>();
    /* Current rectangle being pressed by the player */
    private Rectangle currPiece;
    
    /*
     * Constructor for ChimpTest object, this will call the GUI for
     * this class to be invoked and show and start the game play area.
     */
    public ChimpTest() {
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
        topPanelSetup(root);
        menuSetup(primaryStage, root);
        startBtnSetup(root, primaryStage);
        
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
     * Setup for the button that appears when first shown the program.
     * The button will start the game when ever invoked. On action, the
     * game play area will be set and all game features will be setup.
     * The button will then be added to pane. 
     * 
     * @param pane to add the start button to
     */
    public void startBtnSetup(Pane root, Stage primary) {
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
            numberRects = setupImages(root, primary);
            showNumbers(root);
            setupText(root);
            setEndInt();
        });
        
        g.getChildren().add(startGame);
        root.getChildren().add(g);
    }
    
    /*
     * This method will get our numberRects rectangles and add them to 
     * the root so they can be displayed to the player.
     * 
     * @param pane to add componenets to
     */
    public void showNumbers(Pane root) {
        for(int i = 0; i < numberRects.size(); i++) {
            root.getChildren().add(numberRects.get(i));
        }
    }
    
    /*
     * This method will get the rectangles on screen and put the correct
     * ID number displayed on top of that. It is achieved by making new
     * text objects for each rectangle according to their ID and setting
     * them to a proper X and Y postion according to the rectangles
     * current postition.
     * 
     * @param pane to add the number overlay to
     */
    public void setupText(Pane root) {
        ArrayList<Text> texts = new ArrayList<Text>();
        Group g = new Group();
        for(int i = 0; i < numberRects.size(); i++) {
            Text idRect = new Text();
            Font idFont = Font.font("TimesRoman", FontWeight.BOLD, 50);
            idRect.setText(numberRects.get(i).getId());
            idRect.setFont(idFont);
            idRect.setTextAlignment(TextAlignment.CENTER);
            if(i >= 10) {
                idRect.setLayoutX(numberRects.get(i).getLayoutX() + 10);
                idRect.setLayoutY(numberRects.get(i).getLayoutY() + 54);
            }
            else {
                idRect.setLayoutX(numberRects.get(i).getLayoutX() + 23);
                idRect.setLayoutY(numberRects.get(i).getLayoutY() + 54);
            }
            idRect.setFill(Color.BLACK);
            
            g.getChildren().add(idRect);
            texts.add(idRect);
        }
        root.getChildren().add(g);
    } 
    
    /*
     * This is the screen that shows when the round is over. All this method does
     * is sets up the GUI when the round is over to display the numbers in which
     * that player has achieved and a button for the option to continue, and
     * text that will display the number of lives left. When the continue button is 
     * pressed, all the some logic will be reset but the level will increment.
     * 
     * @param pane to add componenets to
     */
    public void endRound(Pane root, Stage primary) {
        Font overFont = Font.font("TimesRoman", FontWeight.BOLD, 50);
        Text overText = new Text();
        overText.setText("NUMBERS: " + numberUp);
        overText.setFont(overFont);
        overText.setLayoutX(445);
        overText.setLayoutY(300);
        overText.setFill(Color.WHITE);
        overText.setStroke(Color.BLACK);
        
        Font overSmolFont = Font.font("TimesRoman", FontWeight.BOLD, 30);
        Text overSmolText = new Text();
        overSmolText.setText("Lives Left: " + lives);
        overSmolText.setFont(overSmolFont);
        overSmolText.setLayoutX(505);
        overSmolText.setLayoutY(350);
        overSmolText.setFill(Color.WHITE);
        overSmolText.setStroke(Color.BLACK);
        
        Font againFont = Font.font("TimesRoman", FontWeight.BOLD, FontPosture.ITALIC, 18);
        Button contBtn = new Button();
        contBtn.setText("Continue");
        contBtn.setLayoutX(455);
        contBtn.setLayoutY(400);
        contBtn.setPrefSize(275, 50);
        contBtn.setFont(againFont);
        
        contBtn.setOnAction(event -> {
            root.getChildren().removeIf(node -> node instanceof Group);
            root.getChildren().removeIf(node -> node instanceof Rectangle);
            clearList();
            currPiece = null;
            setBackground(root);
            topPanelSetup(root);
            numberRects = setupImages(root, primary);
            showNumbers(root);
            setupText(root);
            setEndInt();
            currID = 1;
        });
        
        root.getChildren().add(overText);
        root.getChildren().add(overSmolText);
        root.getChildren().add(contBtn);
    }
    
    /*
     * This method will clear the whole list of showing rectangles
     * to either continue the game, or completely reset it.
     */
    public void clearList() {
        numberRects.removeAll(numberRects); 
    }
    
    /*
     * This will get the largest number of the current stage in order
     * to tell where the level is supposed to end.
     */
    public void setEndInt() {
        endInt = Integer.parseInt(numberRects.get(numberRects.size() - 1).getId());
    }
    
    /*
     * This method will take the current input from the player by getting the ID
     * of the clicked rectangle and comparing it to the current ID that is needed
     * to progress. If the ID is the correct ID, then the game will continue and
     * progress. If the ID is wrong, then the player will lose a life. Within the
     * if and else statements, they both check to see if the round and game is over
     * based on if the endInt and the number of lives.
     * 
     * @param pane to add componenets to
     * @param string of the rectangle ID
     * @param stage to manipulate
     */
    public void checkInput(Pane root, String id, Stage primary) {
        int bigInt = Integer.parseInt(id);
        if(bigInt == currID) {
            root.getChildren().removeIf(node -> node instanceof Group);
            root.getChildren().removeIf(node -> node instanceof Rectangle);
            setBackground(root);
            topPanelSetup(root);
            if(bigInt == endInt) {
                level++;
                numberUp++;
                endRound(root, primary);
            }
            currID++;
            numberRects.remove(currPiece);
            showNumbers(root);
            if(level == 1) {
                setupText(root);
            }
        }
        else {
            root.getChildren().removeIf(node -> node instanceof Group);
            root.getChildren().removeIf(node -> node instanceof Rectangle);
            setBackground(root);
            topPanelSetup(root);
            lives--;
            if(lives < 0) {
                endGame(root, primary);
            }
            else {
                endRound(root, primary);
            }
        }
    }
    
    /*
     * This method is used to handle the setup of the images on the screen.
     * It will first create the number of rectangle objects according to the
     * number in which what level the player is at. The rectangle will then
     * be set up at a random X and Y location. Then the images will be added
     * to the list of images and returned.
     * 
     * @param pane to add componenets to
     * @param stage to manipulate
     * @return array list of set up rectangle images
     */
    public ArrayList<Rectangle> setupImages(Pane root, Stage primary) {
        ArrayList<Double> allXs = new ArrayList<Double>();
        ArrayList<Double> allYs = new ArrayList<Double>();
        ArrayList<Rectangle> imgs = new ArrayList<Rectangle>();
        for(int i = 0; i < numberUp; i++) {
            Rectangle r = new Rectangle();
            r.setFill(Color.WHITE);
            r.setStroke(Color.BLACK);
            r.setWidth(75);
            r.setHeight(75);
            double xVal = ThreadLocalRandom.current().nextDouble(75, 1100);
            double yVal = ThreadLocalRandom.current().nextDouble(190, 500);
            allXs.add(xVal);
            allYs.add(yVal);
               
            r.setLayoutX(xVal);
            r.setLayoutY(yVal);
            int id = i + 1;
            r.setId("" + id);
            
            r.setOnMouseClicked(event -> {
                currPiece = r;
                checkInput(root, r.getId(), primary);
            });
            
            imgs.add(r);
        }
        return imgs;
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
    public void endGame(Pane root, Stage primary) {
        Font overFont = Font.font("TimesRoman", FontWeight.BOLD, 60);
        Text overText = new Text();
        overText.setText("GAME OVER");
        overText.setFont(overFont);
        overText.setLayoutX(435);
        overText.setLayoutY(300);
        overText.setFill(Color.FIREBRICK);
        
        Font overSmolFont = Font.font("TimesRoman", FontWeight.BOLD, 30);
        Text overSmolText = new Text();
        overSmolText.setText("Number achieved:  " + numberUp);
        overSmolText.setFont(overSmolFont);
        overSmolText.setLayoutX(465);
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
            root.getChildren().removeIf(node -> node instanceof Text);
            root.getChildren().removeIf(node -> node instanceof Button);
            level = 1;
            lives = 3;
            numberUp = 4;
            clearList();
            currPiece = null;
            setBackground(root);
            topPanelSetup(root);
            menuSetup(primary, root);
            numberRects = setupImages(root, primary);
            showNumbers(root);
            setupText(root);
            setEndInt();
            currID = 1;
        });
        root.getChildren().add(overText);
        root.getChildren().add(overSmolText);
        root.getChildren().add(playAgain);
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
        Label welLabel = new Label("Chimp Test");        
        welLabel.setFont(titleFont);
        welLabel.setLayoutX(465);
        welLabel.setLayoutY(25);
        
        Font descFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label descLabel = new Label("Test to see if you are smarter than a chimpanzee.");
        descLabel.setFont(descFont);
        descLabel.setLayoutX(385);
        
        descLabel.setLayoutY(100);
        
        Font chooseFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        Label inTitleLabel = new Label("Instructions:");
        inTitleLabel.setFont(chooseFont);
        inTitleLabel.setLayoutX(220);
        inTitleLabel.setLayoutY(600);
        
        Font instFont = Font.font("TimesRoman", FontWeight.NORMAL, 20);
        Label instLabel = new Label("Click the squares above in order from least to highest according to their numbers.\n "
                + "                              This test will get harder as you progress.");
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
