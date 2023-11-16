/*
 * Name: Damian Franco
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Mexican Train Dominoes
 * Version: GUI V7
 */
package dominoes;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameGUI extends Application {
    /* New game logic object to handle all that logic */
    private GameLogic gl = new GameLogic();
    /* AI turn flag to set when AI needs to play */
    private boolean AIturn;
    /* Button setup for the GUI and human player */
    private Button playBtn, passBtn, drawBtn;
    /* List of ImageView objects of the many dominoe piece images */
    private ArrayList<ImageView> handImageList = new ArrayList<ImageView>();

    /*
     * Start of the program and set up on the GUI
     * @param Stage to setup the GUI on
     */ 
    public void start(Stage primaryStage) throws Exception {
        /* GUI Window and mainframe setup */
        double width  = 1300.0;
        double height = 700.0;
        primaryStage.setTitle("Scuffed Dominoes");
        Canvas canvas = new Canvas(width, height);
        Pane root = new Pane(canvas);
        
        /* Font setup for better looking labels */
        Font roundFont = Font.font("TimesRoman", FontWeight.EXTRA_BOLD, 30);
        Font titleFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        
        /* Draw everything we need on the GUI and starts the game logic */
        openingTabs();
        GUISetup(root, roundFont, titleFont);
        buttonSetup(root);
        setupOnPlayer(gl.numPlayers, root);
        startGameLogic(root);
        showHand(root);
        setBonePanel(root);
        showEnginePiece(root);
        setHandLabel(root);

        /* Sets and shows the GUI */
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
    
    /*
     * Clears the entire GUI to redraw on it
     * @param pane that needs to be clear
     */
    public void clearGUI(Pane root) {
        root.getChildren().removeIf(node -> node instanceof ImageView);
        root.getChildren().removeIf(node -> node instanceof Group);
        root.getChildren().removeIf(node -> node instanceof Text);
        root.getChildren().removeIf(node -> node instanceof VBox);
        root.getChildren().removeIf(node -> node instanceof HBox);
    }
    
    /*
     * Sets up the engine piece an shows it on the GUI.
     * @param pane to put image on
     */
    public void showEnginePiece(Pane root) {
        ImageView engineImage = new ImageView(gl.engine.getImage());
        engineImage.setX(1170);
        engineImage.setY(340);
        engineImage.setFitHeight(120);
        engineImage.setPreserveRatio(true);
        engineImage.setRotate(90.0);
        root.getChildren().add(engineImage);
    }
    
    /*
     * Sets up the player hand text label accorrding to which
     * players hand is being shown.
     * @param pane to put label on
     */
    public void setHandLabel(Pane root) {
        Font titleFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        int playID = gl.currentPlayer.getID() + 1;
        Text yourHandLabel = new Text("Player " + playID + "'s Hand:");
        
        yourHandLabel.setFont(titleFont);
        yourHandLabel.setLayoutX(20);
        yourHandLabel.setLayoutY(480);
        root.getChildren().add(yourHandLabel);
    }
    
    /*
     * Handles the AIs move by calling the various methods it needs to take
     * to handle the AIs move.
     * @param main pane to modify
     */
    public void playAI(Pane root) {
        /* Handles AI move in the game logic */
        gl.AIMove();
        /* Does not AI's hand on the GUI */
        showHand(root);
        /* Shows the updated train on the GUI */
        showTrains(root);
        /* Handles the turn after AI is done playing */
        handleTurnLogic(root);
        /* Checks if the round is over at any point */
        roundOver(root);
        /* Checks if the game is over at any point */
        gameOver();
    }
    
    /*
     * Shows the current players hand/pieces on the GUI.
     * @param main pane put modify
     */
    public void showHand(Pane root) {
        handImageList.removeAll(handImageList);
        root.getChildren().removeIf(node -> node instanceof Text);
        setHandLabel(root);
        setBonePanel(root);
        int xMoveR1 = 50, xMoveR2 = 50;
        /* Parases through the current players hand and prints it to the GUI */
        for(int i = 0; i < gl.currentPlayer.getHand().size(); i++) {
                ImageView iV = new ImageView(gl.currentPlayer.getHand().get(i).getImage());
                handImageList.add(iV);
                iV.setX(xMoveR1);
                if(xMoveR1 <= 850) {
                    iV.setY(480);
                    xMoveR1 += 120;
                }
                else {
                    iV.setFitHeight(75);
                    iV.setX(xMoveR2);
                    iV.setY(580);
                    xMoveR2 += 110;
                }
                iV.setFitHeight(100);
                iV.setPreserveRatio(true);
                iV.setRotate(270.0);
                root.getChildren().add(iV);
        }
    }
    
    /*
     * Sets the text/label of the boneyard according to how many
     * pieces are left in the boneyard.
     * @param main pane to modify
     */
    public void setBonePanel(Pane root) {
        root.getChildren().removeIf(node -> node instanceof Text);
        Font roundFont = Font.font("TimesRoman", FontWeight.SEMI_BOLD, 30);
        String str = "Boneyard pieces:  " + gl.boneyard.size();
        Text boneLabel = new Text();
        boneLabel.setText("");
        boneLabel.setText(str);
        boneLabel.setFont(roundFont);
        boneLabel.setLayoutX(915);
        boneLabel.setLayoutY(315);
        root.getChildren().add(boneLabel);
    }
    
    /*
     * Sets and shows all the scores of each and every player
     * after every single round and does it based off how many
     * number of players are playing.
     * @param number of players
     * @param main pane to modify
     */
    public void setScorePanel(int numP, Pane root) {
        Font titleFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        VBox v = new VBox();
        if(numP == 2) {
            Label scoreOneLabel = new Label("Player 1 Score:  "  + gl.p1Score);
            scoreOneLabel.setFont(titleFont);
            scoreOneLabel.setLayoutX(915);
            scoreOneLabel.setLayoutY(65);
            
            Label scoreTwoLabel = new Label("Player 2 Score:  " + gl.p2Score);
            scoreTwoLabel.setFont(titleFont);
            scoreTwoLabel.setLayoutX(915);
            scoreTwoLabel.setLayoutY(115);
            
            v.getChildren().add(scoreOneLabel);
            v.getChildren().add(scoreTwoLabel);
        }
        else if(numP == 3) {
            Label scoreOneLabel = new Label("Player 1 Score:  " + gl.p1Score);
            scoreOneLabel.setFont(titleFont);
            scoreOneLabel.setLayoutX(915);
            scoreOneLabel.setLayoutY(65);
            
            Label scoreTwoLabel = new Label("Player 2 Score:  " + gl.p2Score);
            scoreTwoLabel.setFont(titleFont);
            scoreTwoLabel.setLayoutX(915);
            scoreTwoLabel.setLayoutY(115);
            
            Label scoreThreeLabel = new Label("Player 3 Score:  " + gl.p3Score);
            scoreThreeLabel.setFont(titleFont);
            scoreThreeLabel.setLayoutX(915);
            scoreThreeLabel.setLayoutY(165);
            
            v.getChildren().add(scoreOneLabel);
            v.getChildren().add(scoreTwoLabel);
            v.getChildren().add(scoreThreeLabel);
        }
        else if(numP == 4) {
            Label scoreOneLabel = new Label("Player 1 Score:  " + gl.p1Score);
            scoreOneLabel.setFont(titleFont);
            scoreOneLabel.setLayoutX(915);
            scoreOneLabel.setLayoutY(65);
            
            Label scoreTwoLabel = new Label("Player 2 Score:  " + gl.p2Score);
            scoreTwoLabel.setFont(titleFont);
            scoreTwoLabel.setLayoutX(915);
            scoreTwoLabel.setLayoutY(115);
            
            Label scoreThreeLabel = new Label("Player 3 Score:  " + gl.p3Score);
            scoreThreeLabel.setFont(titleFont);
            scoreThreeLabel.setLayoutX(915);
            scoreThreeLabel.setLayoutY(165);
            
            Label scoreFourLabel = new Label("Player 4 Score:  " + gl.p4Score);
            scoreFourLabel.setFont(titleFont);
            scoreFourLabel.setLayoutX(915);
            scoreFourLabel.setLayoutY(215);
            
            v.getChildren().add(scoreOneLabel);
            v.getChildren().add(scoreTwoLabel);
            v.getChildren().add(scoreThreeLabel);
            v.getChildren().add(scoreFourLabel);
        }
        v.setLayoutX(915);
        v.setLayoutY(65);
        v.setSpacing(15);
        root.getChildren().add(v);
    }
    
    /*
     * Starts and handles all the main game logic when
     * playing the game on the GUI.
     * @param main pane to modify
     */
    public void startGameLogic(Pane root) {
        /* Clears the GUI of previous nodes */
        clearGUI(root);

        /* Handle of the main game logic */
        gl.generateBoneyard();
        loadImages(root);
        gl.engine = gl.setEnginePiece();
        gl.setPlayers(gl.numPlayers);
        gl.setTrains(gl.numPlayers);
        gl.currentPiece = new Piece(100, 10, 11, null);
        gl.currentPlayer = gl.players.get(0);
        gl.trains.get(0).setIsFree(true);

        /* Sets up the GUI based on the current player*/
        setHandLabel(root);
        setRoundLabel(root);
        setScorePanel(gl.numPlayers, root);
        showHand(root);
        setBonePanel(root);
        showEnginePiece(root);
    }
    
    
    /*
     * Handles all the parts of when the game needs to change turns
     * and updates it on the GUI.
     * @param main pane to modify
     */
    public void handleTurnLogic(Pane root) {
        /* Redraw of the GUI based on the current player */
        drawBtn.setDisable(true);
        gl.changeTurn(gl.currentPlayer);
        root.getChildren().removeIf(node -> node instanceof Group);
        root.getChildren().removeIf(node -> node instanceof ImageView);
        showEnginePiece(root);
        showHand(root);
        showTrains(root);
        gl.setFreeOffPassed();
        gl.setFreeTrains(gl.currentPlayer);
        
        /* Check if it is currently the AI's turn*/
        Boolean b = gl.isAITurn();
        if(b) {
            AIturn = true;
            
        }
        else {
            AIturn = false;
        }

        /* Handles the AI's turn if the flag is set to true */
        if(AIturn) {
            playBtn.setDisable(true);
            passBtn.setDisable(true);
            drawBtn.setDisable(true);
            playAI(root);
        }
        else {
            playBtn.setDisable(false);
            passBtn.setDisable(false);
            drawBtn.setDisable(false);
        }
        /* Check if round or game is over */
        roundOver(root);
        gameOver();
    }
    
    /*
     * Loads all the dominoe piece images I made from the
     * resource folder in the build path of the project.
     * @param main pane to modify
     */
    public void loadImages(Pane root) {
        Image p = null;
        for(int i = 0; i < gl.boneyard.size(); i++) {
            String str = "/" + gl.boneyard.get(i).getID() + ".png";
            InputStream fis = getClass().getResourceAsStream(str);
            p = new Image(fis);
            gl.boneyard.get(i).setImage(p);
        }
    }
    
    /*
     * Shows the winner on a new alert pop up and then
     * exits the game after the player clicks the OK button.
     */
    public void showWinner() {
        Alert a = new Alert(AlertType.CONFIRMATION);
        int id = gl.getWinner().ID + 1;
        a.setTitle("GAME OVER");
        a.setHeaderText("GAME IS NOW OVER!!!");
        a.setContentText("PLAYER " + id + " WINS!!!!");
        Optional<ButtonType> result = a.showAndWait();
        if(result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }
    
    /*
     * Checks if the game is over or not
     */
    public void gameOver() {
        Boolean b = gl.checkGameOver();
        if(b) {
            showWinner();
        }
    }
    
    /*
     * Checks if teh round is over or not and also if the round
     * is over then it will alert the player by popping up an alert
     * window and after clicking OK on the alert, it will go to the
     * next round.
     * @param main pane to modify
     */
    public void roundOver(Pane root) {
        Boolean b  = gl.checkRoundOver();
        if(b) {
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("ROUND OVER");
            a.setHeaderText("ROUND " + gl.roundCount + " OVER!");
            a.setContentText("Please click OK to start next round.");
            a.show();
            gl.roundCount++;
            gl.roundOver();
            gl.boneyard.removeAll(gl.boneyard);
            gameOver();
            startGameLogic(root);
        }
        
    }
    
    /*
     * Set up and handling of all the train buttons on the GUI.
     * @param main pane to modify
     * @param train to print out a button to
     */
    public void trainButtonSetup(Pane root, Train train) {
        /* Font of the button text */
        Font btnFont = Font.font("ComicSans", FontWeight.BLACK, 12);

        /* Mexican train setup */
        Button mexTrainBtn = new Button();
        mexTrainBtn.setText("Play Piece");
        mexTrainBtn.setLayoutX(160);
        mexTrainBtn.setLayoutY(8);
        mexTrainBtn.setPrefSize(100, 25);
        mexTrainBtn.setFont(btnFont);
        
        /* Player 1 train setup */
        Button oneTrainBtn = new Button();
        oneTrainBtn.setText("Play Piece");
        oneTrainBtn.setLayoutX(160);
        oneTrainBtn.setLayoutY(92);
        oneTrainBtn.setPrefSize(100, 25);
        oneTrainBtn.setFont(btnFont);
        
        /* Player 2 train setup */
        Button twoTrainBtn = new Button();
        twoTrainBtn.setText("Play Piece");
        twoTrainBtn.setLayoutX(160);
        twoTrainBtn.setLayoutY(178);
        twoTrainBtn.setPrefSize(100, 25);
        twoTrainBtn.setFont(btnFont);
        
        /* Player 3 train setup */
        Button threeTrainBtn = new Button();
        threeTrainBtn.setText("Play Piece");
        threeTrainBtn.setLayoutX(160);
        threeTrainBtn.setLayoutY(265);
        threeTrainBtn.setPrefSize(100, 25);
        threeTrainBtn.setFont(btnFont);
        
        /* Player 3 train setup */
        Button fourTrainBtn = new Button();
        fourTrainBtn.setText("Play Piece");
        fourTrainBtn.setLayoutX(160);
        fourTrainBtn.setLayoutY(348);
        fourTrainBtn.setPrefSize(100, 25);
        fourTrainBtn.setFont(btnFont);
        
        /* Mexican train action handling */
        mexTrainBtn.setOnAction(event -> {
            /* Checks if the move is a legal one that removes the piece 
               and handles the turn and removal of that piece */
            Boolean b = gl.isLegalMove("M", gl.currentPiece);
            if(b) {
                gl.currentPlayer.getHand().remove(gl.currentPiece);
                handleTurnLogic(root);
            }
            /* Updates the GUI with new information */
            root.getChildren().removeIf(node -> node instanceof ImageView);
            showEnginePiece(root);
            showHand(root);
            showTrains(root);
            setHandLabel(root);
            root.getChildren().removeIf(node -> node instanceof Group);
            /* Checks if the game or round is over */
            gameOver();
            roundOver(root);
        });
        
        /* Player 1 train action handling */
        oneTrainBtn.setOnAction(event -> {
          /* Checks if the move is a legal one that removes the piece 
             and handles the turn and removal of that piece */
          Boolean b = gl.isLegalMove("1", gl.currentPiece);
          if(b) {
              gl.currentPlayer.getHand().remove(gl.currentPiece);
              handleTurnLogic(root);
          }
          /* Updates the GUI with new information */
          root.getChildren().removeIf(node -> node instanceof ImageView);
          showEnginePiece(root);
          showHand(root);
          showTrains(root);
          setHandLabel(root);
          root.getChildren().removeIf(node -> node instanceof Group);
          /* Checks if the game or round is over */
          gameOver();
          roundOver(root);
        });
        
        /* Player 2 train action handling */
        twoTrainBtn.setOnAction(event -> {
          /* Checks if the move is a legal one that removes the piece 
             and handles the turn and removal of that piece */
          Boolean b = gl.isLegalMove("2", gl.currentPiece);
          if(b) {
              gl.currentPlayer.getHand().remove(gl.currentPiece);
              handleTurnLogic(root);
          }
          root.getChildren().removeIf(node -> node instanceof ImageView);
          showEnginePiece(root);
          showHand(root);
          showTrains(root);
          setHandLabel(root);
          /* Updates the GUI with new information */
          root.getChildren().removeIf(node -> node instanceof Group);
          /* Checks if the game or round is over */
          roundOver(root);
          gameOver();
        });
        
        /* Player 3 train action handling */
        threeTrainBtn.setOnAction(event -> {
          /* Checks if the move is a legal one that removes the piece 
             and handles the turn and removal of that piece */
          Boolean b = gl.isLegalMove("3", gl.currentPiece);
          if(b) {
              gl.currentPlayer.getHand().remove(gl.currentPiece);
              handleTurnLogic(root);
          }
          root.getChildren().removeIf(node -> node instanceof ImageView);
          showEnginePiece(root);
          showHand(root);
          showTrains(root);
          setHandLabel(root);
          /* Updates the GUI with new information */
          root.getChildren().removeIf(node -> node instanceof Group);
          /* Checks if the game or round is over */
          roundOver(root);
          gameOver();
        });
        
        /* Player 4 train action handling */
        fourTrainBtn.setOnAction(event -> {
          /* Checks if the move is a legal one that removes the piece 
             and handles the turn and removal of that piece */
          Boolean b = gl.isLegalMove("4", gl.currentPiece);
          if(b) {
              gl.currentPlayer.getHand().remove(gl.currentPiece);
              handleTurnLogic(root);
          }
          /* Updates the GUI with new information */
          root.getChildren().removeIf(node -> node instanceof ImageView);
          showEnginePiece(root);
          showHand(root);
          showTrains(root);
          setHandLabel(root);
          root.getChildren().removeIf(node -> node instanceof Group);
          /* Checks if the game or round is over */
          roundOver(root);
          gameOver();
        });
        
        /* Adds buttons to the group if the train is playable and shows them on the GUI */
        Group g = new Group();
        if(train.getName().equals("0")) {
            g.getChildren().add(mexTrainBtn);
        }
        else if(train.getName().equals("1")) {
            g.getChildren().add(oneTrainBtn);
        }
        else if(train.getName().equals("2")) {
            g.getChildren().add(twoTrainBtn);
        }
        else if(train.getName().equals("3")) {
            g.getChildren().add(threeTrainBtn);
        }
        else if(train.getName().equals("4")) {
            g.getChildren().add(fourTrainBtn);
        }
        root.getChildren().add(g);
    }
    
    /*
     * Play, pass and draw button setup and action handling.
     * @param main pane to modify
     */
    public void buttonSetup(Pane root) {
        /* Play button intialization and setup */
        Font btnFont = Font.font("ComicSans", FontWeight.BLACK, 20);
        playBtn = new Button();
        playBtn.setText("Play");
        playBtn.setLayoutX(950);
        playBtn.setLayoutY(500);
        playBtn.setPrefSize(150, 70);
        playBtn.setFont(btnFont);
        
        /* Handles the action when the mouse is clicked */
        playBtn.setOnAction(event -> {
            /* Shows instructions and sets the free trains based on the current player */
            Font playFont = Font.font("ComicSans", FontWeight.SEMI_BOLD, 16);
            Label play = new Label("Please click the piece you would like to play and then click the button of the train you would like to play on.");
            play.setFont(playFont);
            play.setLayoutX(300);
            play.setLayoutY(675);
            root.getChildren().add(play);
            gl.setFreeTrains(gl.currentPlayer);

            /* This will get all the images of the playable dominoes in the players hand and allow
               them all to be clicked and selected to play when clicked on */
            for(int i = 0; i < handImageList.size(); i++) {
                handImageList.get(i).setId("hand" + i);
                /* Handles action when the image is clicked on */
                handImageList.get(i).setOnMouseClicked(nextEvent -> {
                    /* Label that shows which piece is selected setup */
                    Group g = new Group();
                    Label choosenPiece = new Label();
                    choosenPiece.setText("");
                    root.getChildren().removeIf(node -> node instanceof Group);
                    String st = nextEvent.getSource().toString();
                    String id = "";
                    /* Gets the ID of the piece selected and assigns it to current piece */
                    for(int y = 0; y < st.length(); y++) {
                        if(st.charAt(y) == '0' || st.charAt(y) == '1' || st.charAt(y) == '2' || st.charAt(y) == '3' || st.charAt(y) == '4' ||
                                st.charAt(y) =='5' ||st.charAt(y) == '6' ||st.charAt(y) == '7' ||st.charAt(y) == '8' ||st.charAt(y) == '9') {
                            id += Character.toString(st.charAt(y));
                        }
                    }
                    /* Layout and showing of selected piece text handling */
                    int q = Integer.parseInt(id);
                    String l = gl.currentPlayer.getHand().get(q).toString();
                    choosenPiece.setText("Choosen Piece: " + l);
                    choosenPiece.setFont(playFont);
                    choosenPiece.setLayoutX(550);
                    choosenPiece.setLayoutY(450);
                    g.getChildren().add(choosenPiece);
                    g.getChildren().add(play);
                    root.getChildren().add(g);

                    /* Updates the Boneyard and Hand panel */
                    setBonePanel(root);
                    setHandLabel(root);

                    /* Sets current piece to selected piece */
                    gl.currentPiece = gl.currentPlayer.getHand().get(q);

                    /* Flips the piece if double clicked */
                    if(nextEvent.getClickCount() == 2) {
                        handImageList.get(q).setRotate(270);
                    }

                    /* Show playable trains based on selected piece */
                    for(int j = 0; j < gl.trains.size(); j++) {
                        if(gl.trains.get(j).getIsFree()) {
                            trainButtonSetup(root, gl.trains.get(j));
                        }
                    }
                    playBtn.setOnMouseClicked(null);
                });
            }
        });

        /* Draw button intialization and setup */
        drawBtn = new Button();
        drawBtn.setText("Draw");
        drawBtn.setLayoutX(1125);
        drawBtn.setLayoutY(500);
        drawBtn.setPrefSize(150, 70);
        drawBtn.setFont(btnFont);
        
        /* Draw button on click event handling */
        drawBtn.setOnAction(event -> {
            /* Disable the draw button after drawing once */
            drawBtn.setDisable(true);
            /* Updates GUI and draws piece and adds it to players hand */
            gl.optionDraw();
            showHand(root);
            setBonePanel(root);
            setHandLabel(root);
            /* Checks if the game or round is over */
            gameOver();
            roundOver(root);
        });
        
        /* Pass button intialization and setup */
        passBtn = new Button();
        passBtn.setText("Pass");
        passBtn.setLayoutX(1035);
        passBtn.setLayoutY(580);
        passBtn.setPrefSize(150, 70);
        passBtn.setFont(btnFont);   
        
        /* Pass button event handling */
        passBtn.setOnAction(event -> {
            /* Change player, set flags and updates GUI */
            handleTurnLogic(root);
            setHandLabel(root);
            gl.passedPlayer = gl.currentPlayer; 
            gl.passFlag = true;
            /* Checks if the game or round is over */
            roundOver(root);
            gameOver();
        });
        
        /* Add the buttons to the GUI */
        root.getChildren().add(playBtn);
        root.getChildren().add(drawBtn);
        root.getChildren().add(passBtn);
    }
    
    /*
     * Handles the tabs that pop up when the GUI is first opened..
     */
    public void openingTabs() {
        /* Setup and handling of the how many players choice window */
        ArrayList<Integer> choicesOne = new ArrayList<>();
        choicesOne.add(2);
        choicesOne.add(3);
        choicesOne.add(4);
        ChoiceDialog<Integer> introDialog = new ChoiceDialog<Integer>(choicesOne.get(0), choicesOne);
        introDialog.setTitle("WELCOME!");
        introDialog.setHeaderText("Welcome to Mexican Train Dominoes");
        introDialog.setContentText("Choose how many players you would like to play with:");
        Optional<Integer> numPlayersResult = introDialog.showAndWait();
        
        /* Setup and handling of the how many AI players choice window */
        ArrayList<Integer> choicesAI = new ArrayList<>();
        choicesAI.add(0);
        choicesAI.add(1);
        choicesAI.add(2);
        choicesAI.add(3);
        ChoiceDialog<Integer> AIDialog = new ChoiceDialog<Integer>(choicesAI.get(0), choicesAI);
        AIDialog.setTitle("WELCOME!");
        AIDialog.setHeaderText("Thank you!");
        AIDialog.setContentText("Now choose how many AI players you would like to play with:");
        Optional<Integer> AIresult = AIDialog.showAndWait();
        
        /* Sets both number of players and AI players off the user input of the starting windows */
        gl.numPlayers = numPlayersResult.get();
        gl.numAIPlayers = AIresult.get();
    }
    
    /*
     * Set and handling of the round label on the rop right of the GUI.
     * @param main pane to modify
     */
    public void setRoundLabel(Pane root) {
        HBox h = new HBox();
        Font roundFont = Font.font("TimesRoman", FontWeight.EXTRA_BOLD, 30);
        Text roundLabel = new Text("Round " + gl.roundCount);
        roundLabel.setFont(roundFont);
        roundLabel.setLayoutX(760);
        roundLabel.setLayoutY(5);
        
        h.getChildren().add(roundLabel);
        h.setLayoutX(760);
        h.setLayoutY(5);
        root.getChildren().add(h);
    }
    
    /*
     * Main GUI setup of lines, labels and overall layout of the GUI
     * @param main pane to modify
     * @param font used for round labels
     * @param font used for title labels
     */
    public void GUISetup(Pane root, Font roundFont, Font titleFont) {
        /* Line/dividers initialization and setup */
        Line centerLine = new Line();
        centerLine.setStartX(0);
        centerLine.setStartY(450);
        centerLine.setEndX(1300);
        centerLine.setEndY(450);
        centerLine.setFill(Color.BLACK);
        
        Line topLine = new Line();
        topLine.setStartX(900);
        topLine.setStartY(0);
        topLine.setEndX(900);
        topLine.setEndY(450);
        topLine.setFill(Color.BLACK);
        
        Line roundLine1 = new Line();
        roundLine1.setStartX(745);
        roundLine1.setStartY(0);
        roundLine1.setEndX(745);
        roundLine1.setEndY(50);
        roundLine1.setFill(Color.BLACK);

        Line roundLine2 = new Line();
        roundLine2.setStartX(745);
        roundLine2.setStartY(50);
        roundLine2.setEndX(900);
        roundLine2.setEndY(50);
        roundLine2.setFill(Color.BLACK);
        
        Line engineLine = new Line();
        engineLine.setStartX(900);
        engineLine.setStartY(350);
        engineLine.setEndX(1300);
        engineLine.setEndY(350);
        engineLine.setFill(Color.BLACK);
        
        Line boneLine = new Line();
        boneLine.setStartX(900);
        boneLine.setStartY(260);
        boneLine.setEndX(1300);
        boneLine.setEndY(260);
        boneLine.setFill(Color.BLACK);
        
        /* Label that do not need updating/stationary labels setup */
        Label mTrainLabel = new Label("Mexican Train:");
        mTrainLabel.setFont(titleFont);
        mTrainLabel.setLayoutX(20);
        mTrainLabel.setLayoutY(5);
        
        Label engineLabel = new Label("Engine Piece:");
        engineLabel.setFont(roundFont);
        engineLabel.setLayoutX(915);
        engineLabel.setLayoutY(375);
        
        Label scoreLabel = new Label("Scoreboard:");
        scoreLabel.setFont(roundFont);
        scoreLabel.setLayoutX(915);
        scoreLabel.setLayoutY(5);
        
        /* Add all to the GUI */
        root.getChildren().add(mTrainLabel);
        root.getChildren().add(engineLabel);
        root.getChildren().add(scoreLabel);
        root.getChildren().add(centerLine);
        root.getChildren().add(topLine);
        root.getChildren().add(roundLine1);
        root.getChildren().add(roundLine2);
        root.getChildren().add(engineLine);
        root.getChildren().add(boneLine);
        
        /* Setting a new background color */
        BackgroundFill bgFill = new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bgFill);
        root.setBackground(bg);
    }
    
    /*
     * Set up of the train labels based on how many players are currently
     * in the game.
     * @param number of players
     * @param main pane to modify
     */
    public void setupOnPlayer(int numP, Pane root) {
        Font titleFont = Font.font("TimesRoman", FontPosture.ITALIC, 20);
        /* Only show and intialize 2 trains if 2 players */
        if(numP == 2) {
            Label oneTrainLabel = new Label("Player 1 Train:");
            oneTrainLabel.setFont(titleFont);
            oneTrainLabel.setLayoutX(20);
            oneTrainLabel.setLayoutY(90);
            
            Label twoTrainLabel = new Label("Player 2 Train:");
            twoTrainLabel.setFont(titleFont);
            twoTrainLabel.setLayoutX(20);
            twoTrainLabel.setLayoutY(175);
            
            root.getChildren().add(oneTrainLabel);
            root.getChildren().add(twoTrainLabel);
        }
        /* Only show and intialize 3 trains if 3 players */
        else if(numP == 3) {
            Label oneTrainLabel = new Label("Player 1 Train:");
            oneTrainLabel.setFont(titleFont);
            oneTrainLabel.setLayoutX(20);
            oneTrainLabel.setLayoutY(90);
            
            Label twoTrainLabel = new Label("Player 2 Train:");
            twoTrainLabel.setFont(titleFont);
            twoTrainLabel.setLayoutX(20);
            twoTrainLabel.setLayoutY(175);
            
            Label threeTrainLabel = new Label("Player 3 Train:");
            threeTrainLabel.setFont(titleFont);
            threeTrainLabel.setLayoutX(20);
            threeTrainLabel.setLayoutY(260);
            
            root.getChildren().add(oneTrainLabel);
            root.getChildren().add(twoTrainLabel);
            root.getChildren().add(threeTrainLabel);
        }
        /* Only show and intialize 4 trains if 4 players */
        else if(numP == 4) {
            Label oneTrainLabel = new Label("Player 1 Train:");
            oneTrainLabel.setFont(titleFont);
            oneTrainLabel.setLayoutX(20);
            oneTrainLabel.setLayoutY(90);
            
            Label twoTrainLabel = new Label("Player 2 Train:");
            twoTrainLabel.setFont(titleFont);
            twoTrainLabel.setLayoutX(20);
            twoTrainLabel.setLayoutY(175);
            
            Label threeTrainLabel = new Label("Player 3 Train:");
            threeTrainLabel.setFont(titleFont);
            threeTrainLabel.setLayoutX(20);
            threeTrainLabel.setLayoutY(260);
            
            Label fourTrainLabel = new Label("Player 4 Train:");
            fourTrainLabel.setFont(titleFont);
            fourTrainLabel.setLayoutX(20);
            fourTrainLabel.setLayoutY(345);
            
            root.getChildren().add(oneTrainLabel);
            root.getChildren().add(twoTrainLabel);
            root.getChildren().add(threeTrainLabel);
            root.getChildren().add(fourTrainLabel);
        }
    }
    
    /*
     * Set up and handling of all the train pieces on the GUI.
     * @param main pane to modify
     */
    public void showTrains(Pane root) {
        /* These hold the spacing between pieces being placed on the board */
        int xMoveT1 = 50, xMoveT2 = 50, xMoveT3 = 50, xMoveT4 = 50, xMoveT5 = 50;
        /* Scan through all the trains */
        for(int i = 0; i < gl.trains.size(); i++) {
            /* If the train is not empty */
            if(gl.trains.get(i).getPieces().size() != 0) {
                /* Scan through all the pieces that are in the non-empty trains */
                for(int j = 0; j < gl.trains.get(i).getPieces().size(); j++) {
                    Image img = null;
                    ImageView view = new ImageView();
                    ArrayList<ImageView> iV = new ArrayList<ImageView>();
                    /* Handles mexican train dominoe train images on the GUI */
                    if(gl.trains.get(i).getName().equals("0")) {
                        img = gl.trains.get(0).getPieces().get(j).getImage();
                        view.setImage(img);
                        view.setX(xMoveT1);
                        view.setY(10);
                        /* Spacing handling */
                        if(xMoveT1 <= 660) {
                            xMoveT1 += 120;
                        }
                        else {
                            view.setX(xMoveT1 - 120);
                        }
                        view.setFitHeight(100);
                        view.setPreserveRatio(true);
                        /* Rotate to match the values of the played dominoe */
                        if(gl.currentPiece.leftVal < gl.currentPiece.rightVal) {
                            view.setRotate(270.0);
                        }
                        else {
                            view.setRotate(90.0);
                        }
                     
                        /* Ad all the piece images to the GUI */
                        iV.add(view);
                        for(int x = 0; x < iV.size(); x++) {
                            root.getChildren().add(iV.get(x));
                        }
                        
                    }
                    /* Handles train 1 dominoe train images on the GUI */
                    else if(gl.trains.get(i).getName().equals("1")) {
                        img = gl.trains.get(1).getPieces().get(j).getImage();
                        view.setImage(img);
                        view.setX(xMoveT2);
                        view.setY(100);
                        /* Spacing handling */
                        if(xMoveT2 <= 850) {
                            xMoveT2 += 120;
                        }
                        else {
                            view.setX(xMoveT2 - 120);
                        }
                        view.setFitHeight(100);
                        view.setPreserveRatio(true);
                        /* Rotate to match the values of the played dominoe */
                        if(gl.currentPiece.leftVal < gl.currentPiece.rightVal) {
                            view.setRotate(270.0);
                        }
                        else {
                            view.setRotate(90.0);
                        }
                       
                        /* Ad all the piece images to the GUI */
                        iV.add(view);
                        for(int x = 0; x < iV.size(); x++) {
                            root.getChildren().add(iV.get(x));
                        }
                        
                    }
                    /* Handles train 2 dominoe train images on the GUI */
                    else if(gl.trains.get(i).getName().equals("2")) {
                        img = gl.trains.get(2).getPieces().get(j).getImage();
                        view.setImage(img);
                        view.setX(xMoveT3);
                        view.setY(185);
                        /* Spacing handling */
                        if(xMoveT3 <= 850) {
                            xMoveT3 += 120;
                        }
                        else {
                            view.setX(xMoveT3 - 120);
                        }
                        view.setFitHeight(100);
                        view.setPreserveRatio(true);
                        /* Rotate to match the values of the played dominoe */
                        if(gl.currentPiece.leftVal < gl.currentPiece.rightVal) {
                            view.setRotate(270.0);
                        }
                        else {
                            view.setRotate(90.0);
                        }
                     
                        /* Ad all the piece images to the GUI */
                        iV.add(view);
                        for(int x = 0; x < iV.size(); x++) {
                            root.getChildren().add(iV.get(x));
                        }
                    }
                    /* Handles train 3 dominoe train images on the GUI */
                    else if(gl.trains.get(i).getName().equals("3")) {
                        img = gl.trains.get(3).getPieces().get(j).getImage();
                        view.setImage(img);
                        view.setX(xMoveT4);
                        view.setY(265);
                        /* Spacing handling */
                        if(xMoveT4 <= 850) {
                            xMoveT4 += 120;
                        }
                        else {
                            view.setX(xMoveT4 - 120);
                        }
                        view.setFitHeight(100);
                        view.setPreserveRatio(true);
                        /* Rotate to match the values of the played dominoe */
                        if(gl.currentPiece.leftVal < gl.currentPiece.rightVal) {
                            view.setRotate(270.0);
                        }
                        else {
                            view.setRotate(90.0);
                        }
                     
                        /* Ad all the piece images to the GUI */
                        iV.add(view);
                        for(int x = 0; x < iV.size(); x++) {
                            root.getChildren().add(iV.get(x));
                        }
                    }
                    /* Handles train 4 dominoe train images on the GUI */
                    else if(gl.trains.get(i).getName().equals("4")) {
                        img = gl.trains.get(4).getPieces().get(j).getImage();
                        view.setImage(img);
                        view.setX(xMoveT5);
                        view.setY(350);
                        /* Spacing handling */
                        if(xMoveT5 <= 850) {
                            xMoveT5 += 120;
                        }
                        else {
                            view.setX(xMoveT5 - 120);
                        }
                        view.setFitHeight(100);
                        view.setPreserveRatio(true);
                        /* Rotate to match the values of the played dominoe */
                        if(gl.currentPiece.leftVal < gl.currentPiece.rightVal) {
                            view.setRotate(270.0);
                        }
                        else {
                            view.setRotate(90.0);
                        }
                     
                        /* Ad all the piece images to the GUI */
                        iV.add(view);
                        for(int x = 0; x < iV.size(); x++) {
                            root.getChildren().add(iV.get(x));
                        }
                    }
                }
            }
        }
    }
    
    /*
     * Main method used to call and run the GUI and game logic.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
