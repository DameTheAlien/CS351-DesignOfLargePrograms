/*
 * Name: Damian Franco
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Mexican Train Dominoes
 * Version: Console V4
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class GameLogic {
    /* Boneyard intialization */
    public ArrayList<Piece> boneyard;
    /* Player list intialization */
    public ArrayList<Player> players;
    /* Train list intialization */
    ArrayList<Train> trains;
    /* Piece setup for the engine piece */
    Piece engine;
    /* Max number in the boneyard */
    public final int BONE_MAX = 56;
    /* Variable setup for scores and num counts for rounds and player types */
    public int numPlayers, numAIPlayers, roundCount = 1, p1Score, p2Score, p3Score, p4Score;
    /* Flags to set off round, game and passed players */
    public boolean roundOverFlag, gameOverFlag, passFlag;
    /* Players holding the previous and current players */
    public Player currentPlayer, passedPlayer;
    /* Piece to hold current piece being played */
    public Piece currentPiece;
    /* String to hold the ID of the best move for AI */
    public String bestMoveTrain;
    
    /* Constructor starting the game */
    public GameLogic() {
        System.out.print("Hello!! Welcome to my dominoes game! \nPlease enter the answers in the prompt below each question: \n\n");
        init();
    }
    
    /* Intitiaon of the game and setting up the base of the game */
    public void init() {
        System.out.print("How many players would you like to play with? (2, 3, or 4 Players only)\n");
        Scanner sc = new Scanner(System.in);
        numPlayers = sc.nextInt();
        
        if(numPlayers == 2 || numPlayers == 3 || numPlayers == 4) {
            System.out.print("How many AI players would you like to play with? \n 1, 2 or 3 AI Players only for 4 players\n"
                    + " 1 or 2 AI Players for 3 players\n 1 AI player for 2 players\n 0 for all human game \n");
            numAIPlayers = sc.nextInt();
            if(numAIPlayers == 0 || numAIPlayers == 1 || numAIPlayers == 2 || numAIPlayers == 3) {
                System.out.print("Welcome! Lets get started!\n");
                startGame();
            }
            else {
                System.out.println("That ain't the right number of AI players \nTry again from the top dawg\n");
                init();
            }
        }
        else {
            System.out.println("That ain't 2, 3, or 4 players \nTry again dawg\n");
            init();
        }
    }
    
    /* Gets the best move for the AI by scanning all playable areas */
    public Piece getBestMove(ArrayList<Piece> currHand, int t) {
        Piece bestMove = new Piece(10, 11);;
        if(t != 0) {
            for(int i = 0; i < trains.size(); i++) {
                if(trains.get(i).getIsFree()) {
                    if(trains.get(i).getPieces().size() == 0) {
                        t = engine.rightVal;
                        bestMoveTrain = trains.get(i).getName();
                    }
                    else {
                        t = trains.get(i).getLastPiece().rightVal;
                        bestMoveTrain = trains.get(i).getName();
                    }
                }
            }
        }
        else {
            bestMoveTrain = trains.get(0).getName();
            if(trains.get(0).getPieces().size() == 0) {
                t = engine.rightVal;
            }
            else {
                t = trains.get(0).getLastPiece().rightVal;
            }
        }
  
        for(int i = 0; i < currHand.size(); i++) {
            if(currHand.get(i).leftVal == t || currHand.get(i).rightVal == t) {
                if(currHand.get(i).leftVal == currHand.get(i).rightVal) {
                    bestMove = currHand.get(i); 
                }
                else {
                    bestMove = currHand.get(i); 
                }
            }
        }
        return bestMove;
    }
    
    /* Handles the AI move */
    public void AIMove() {
        Piece best = getBestMove(currentPlayer.getHand(), 1);
        currentPiece = best;
        if(best == null) {
            optionDraw();
        }
        else {
            boolean b = isLegalMove(bestMoveTrain, currentPiece);
            if(b) {
                currentPlayer.getHand().remove(currentPiece);
            }
            else {
                bestMoveTrain = "M";
                best = getBestMove(currentPlayer.getHand(), 0);
                currentPiece = best;
                b = isLegalMove(bestMoveTrain, currentPiece);
                if(b) {
                    currentPlayer.getHand().remove(currentPiece);
                }
                else {
                    optionDraw(); 
                }
            }
        }
    }
    
    /* Starts the game and generates all game necessities */
    public void startGame() {
        generateBoneyard();
        engine = setEnginePiece();
        setPlayers(numPlayers);
        setTrains(numPlayers);
        trains.get(0).setIsFree(true);
        currentPiece = new Piece(10, 11);
        
        System.out.println("\nRound " + roundCount);
        currentPlayer = players.get(0);
        while(!gameOverFlag) {
            if(checkGameOver()) {
                gameOverFlag = true;
                gameOver();
                break;
            }
            setFreeTrains(currentPlayer);
            if(passFlag) {
                switch(passedPlayer.ID) {
                    case 0:
                        trains.get(1).setIsFree(true);
                        break;
                    case 1:
                        trains.get(2).setIsFree(true);
                        break;
                    case 2:
                        trains.get(3).setIsFree(true);
                        break;
                    case 3:
                        trains.get(4).setIsFree(true);
                        break;
                }
            }
            consoleUpdate();
            if(numAIPlayers > 0) {
                AIPlayerHandle();
            }
            else {
                start();
            }
            
            changeTurn(currentPlayer);
            if(checkRoundOver()) {
                roundCount++;
                roundOver();
            }
            
        }
    }
    
    /* Handles the AI players turn based on the number of players and AI players */
    public void AIPlayerHandle() {
        if(numPlayers == 4) {
            if(numAIPlayers == 3) {
                if(currentPlayer.getID() == 1 || currentPlayer.getID() == 2 || currentPlayer.getID() == 3) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AIMove();
                }
                else {
                    start();
                }
            }
            else if(numAIPlayers == 2) {
                if(currentPlayer.getID() == 2 || currentPlayer.getID() == 3) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AIMove();
                }
                else {
                    start();
                }
            }
            else if(numAIPlayers == 1) {
                if(currentPlayer.getID() == 3) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AIMove();
                }
                else {
                    start();
                }
            }
        }
        
        else if(numPlayers == 3) {
            if(numAIPlayers == 2) {
                if(currentPlayer.getID() == 1 || currentPlayer.getID() == 2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AIMove();
                }
                else {
                    start();
                }
            }
            else if(numAIPlayers == 1) {
                if(currentPlayer.getID() == 2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AIMove();
                }
                else {
                    start();
                }
            }
        }
        else if(numPlayers == 2) {
            if(numAIPlayers == 1) {
                if(currentPlayer.getID() == 1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AIMove();
                }
                else {
                    start();
                }
            }
        }
       
    }
    
    /* 
     * Set all the free variables for the trains based on plausable moves 
     * @param current player
     */
    public void setFreeTrains(Player curr) {
        for(int i = 1; i < trains.size(); i++) {
            trains.get(i).setIsFree(false);
        }
        if(curr.ID == 0) {
            trains.get(1).setIsFree(true);
        }
        else if(curr.ID == 1) {
            trains.get(2).setIsFree(true);
        }
        else if(curr.ID == 2) {
            trains.get(3).setIsFree(true);
        }
        else if(curr.ID == 3) {
            trains.get(4).setIsFree(true);
        }
    }
    
    /* 
     * Checks to see if the round is over.
     * @return boolean
     */
    public boolean checkRoundOver() {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).getHand().size() == 0 || boneyard.size() == 0) {
                return true;
            }
        }
        return false;
        
    }

    /* 
     * Does all the handling of when the round is over by 
     * displaying scores and checking if the game is over.
     */
    public void roundOver() {
        if(numPlayers == 2) {
            p1Score += players.get(0).getPipCount();
            p2Score += players.get(1).getPipCount();
        }
        else if(numPlayers == 3) {
            p1Score += players.get(0).getPipCount();
            p2Score += players.get(1).getPipCount();
            p3Score += players.get(2).getPipCount();
        }
        else if(numPlayers == 4) {
            p1Score += players.get(0).getPipCount();
            p2Score += players.get(1).getPipCount();
            p3Score += players.get(2).getPipCount();
            p4Score += players.get(3).getPipCount();
        }
        if(roundCount <= 10) {
            String s = "";
            roundOverPrint();
            Scanner sc = new Scanner(System.in);
            s = sc.nextLine();
            if(s != null) {
                boneyard.removeAll(boneyard);
                startGame();
            }
        }
    }
    
    /* 
     * Prints out the round over screen to the console
     */
    public void roundOverPrint() {
        System.out.println("\nROUND OVER!");
        if(numPlayers == 2) {
            System.out.println("Player 1 Score: " + p1Score);
            System.out.println("Player 2 Score: " + p2Score);
        }
        else if(numPlayers == 3) {
            System.out.println("Player 1 Score: " + p1Score);
            System.out.println("Player 2 Score: " + p2Score);
            System.out.println("Player 3 Score: " + p3Score);
        }
        else if(numPlayers == 4) {
            System.out.println("Player 1 Score: " + p1Score);
            System.out.println("Player 2 Score: " + p2Score);
            System.out.println("Player 3 Score: " + p3Score);
            System.out.println("Player 4 Score: " + p4Score);
        }
        System.out.println("Please enter any key to play the next round:");
    }
    
    /* 
     * Prints out the game over screen to the console
     */
    public void gameOver() {
        System.out.println("\nGAME OVER!!!");
        if(numPlayers == 2) {
            System.out.println("Player 1 Score: " + p1Score);
            System.out.println("Player 2 Score: " + p2Score);
            Player winner = getWinner();
            int id = winner.ID + 1;
            System.out.println("PLAYER " + id +" WINS!");
        }
        else if(numPlayers == 3) {
            System.out.println("Player 1 Score: " + p1Score);
            System.out.println("Player 2 Score: " + p2Score);
            System.out.println("Player 3 Score: " + p3Score);
            Player winner = getWinner();
            int id = winner.ID + 1;
            System.out.println("PLAYER " + id +" WINS!");
        }
        else if(numPlayers == 4) {
            System.out.println("Player 1 Score: " + p1Score);
            System.out.println("Player 2 Score: " + p2Score);
            System.out.println("Player 3 Score: " + p3Score);
            System.out.println("Player 4 Score: " + p4Score);
            Player winner = getWinner();
            int id = winner.ID + 1;
            System.out.println("\nPLAYER " + id +" WINS!");
        }
    }
   
    /* 
     * Checks which player won the game.
     * @return winning player
     */
    public Player getWinner() {
        Player winner = null;
        if(p1Score < p2Score && p1Score < p3Score && p1Score < p4Score) {
            winner = players.get(0);
        }
        else if(p2Score < p1Score && p2Score < p3Score && p2Score < p4Score) {
            winner = players.get(1);
        }
        else if(p3Score < p1Score && p3Score < p2Score && p3Score < p4Score) {
            winner = players.get(2);
        }
        else if(p4Score < p1Score && p4Score < p2Score && p4Score < p1Score) {
            winner = players.get(3);
        }
        return winner;
    }
    
    /* 
     * Handles the changing of turns.
     * @param current player
     */
    public void changeTurn(Player p) {
        if(numPlayers == 2) {
            if(currentPiece.leftVal != currentPiece.rightVal || currentPiece == null) {
                if(p.ID == 0) {
                    currentPlayer = players.get(1);
                }
                else if(p.ID == 1) {
                    currentPlayer = players.get(0);
                }
            }
        }
        else if(numPlayers == 3) {
            if(currentPiece.leftVal != currentPiece.rightVal || currentPiece == null) {
                if(p.ID == 0) {
                    currentPlayer = players.get(1);
                }
                else if(p.ID == 1) {
                    currentPlayer = players.get(2);
                }
                else if(p.ID == 2) {
                    currentPlayer = players.get(0);
                }
            }
        }
        else if(numPlayers == 4) {
            if(currentPiece.leftVal != currentPiece.rightVal || currentPiece == null) {
                if(p.ID == 0) {
                    currentPlayer = players.get(1);
                }
                else if(p.ID == 1) {
                    currentPlayer = players.get(2);
                }
                else if(p.ID == 2) {
                    currentPlayer = players.get(3);
                }
                else if(p.ID == 3) {
                    currentPlayer = players.get(0);
                }
            }
        }
    }

    /* 
     * Creates trains based of the number of players
     * @param number of trains to setup
     */
    public void setTrains(int trainNum) {
        trains = new ArrayList<Train>();
        for(int i = 0; i < trainNum + 1; i++) {
            trains.add(i, new Train(Integer.toString(i), false, new ArrayList<Piece>()));
        }
    }
    
    /* 
     * Starts current players turn by asking what they 
     * would like to do, and handles what they do.
     */
    public void start() {
        String t = "";
        Scanner sc = new Scanner(System.in);
        System.out.println("\nWhat would you like to do? (Enter: Play, Draw, Pass)");
        
        t = sc.nextLine();
        
        switch(t) {
            case "play":
            case "Play":
                optionPlay(currentPlayer);
                passFlag = false;
                break;
            case "d":
            case "draw":
            case "Draw":
                //Don't skip directly on draw
                optionDraw();
                System.out.println();
                handPrint(currentPlayer);
                start();
                break;
            case "pass":
            case "Pass":
                passedPlayer = currentPlayer; 
                passFlag = true;
                break;
        }
      
    }
    
    /* 
     * Handles the play option when choosen
     * @param current player
     */
    public void optionPlay(Player currentP) {
        String t = "";
        int p = 0;
        System.out.println("\nWhich train? (Enter: M, 1, 2, 3 or 4)");
        Scanner sc = new Scanner(System.in);
        t = sc.nextLine();
        System.out.println("\nWhich piece? (Enter index of your piece. Ex: Enter 0 below for you first piece)");
        p = sc.nextInt();
        currentPiece = currentP.getHand().get(p);
        boolean check = isLegalMove(t, currentPiece);
        if(check) {
            currentP.getHand().remove(currentPiece);
        }
        else {
            wrongChoiceLoop(currentP);
        }
    }
    
    /* 
     * Recurrsion loop if wrong choice was made
     * @param current player
     */
    public void wrongChoiceLoop(Player currentP) {
        System.out.println("\nCannot play that piece, try again.");
        String t2 = "";
        int p2 = 0;
        if(numPlayers == 2) {
            System.out.println("\nWhich train? (Enter: M, 1 or 2) or Pass (Enter: Pass)");
        }
        else if(numPlayers == 3) {
            System.out.println("\nWhich train? (Enter: M, 1, 2, or 3) or Pass (Enter: Pass)");
        }
        else if(numPlayers == 4) {
            System.out.println("\nWhich train? (Enter: M, 1, 2, 3 or 4) or Pass (Enter: Pass)");
        }
        
        Scanner s2 = new Scanner(System.in);
        t2 = s2.nextLine();
       
        if(t2 == "Pass" || t2 == "pass") {
            System.out.println("\nPlayer Passed");
        }
        else {
            System.out.println("\nWhich piece? (Enter index of your piece. Ex: Enter 0 below for you first piece)");
            p2 = s2.nextInt();
            currentPiece = currentP.getHand().get(p2);
            boolean c2 = isLegalMove(t2, currentPiece);
            
            if(c2) {
                currentP.getHand().remove(currentPiece);
            }
            else {
                wrongChoiceLoop(currentP);
            }
        }
    }
    
    /* 
     * Handles the draw option and adds a piece to players hand
     */
    public void optionDraw() {
        Piece p = boneyard.get(ThreadLocalRandom.current().nextInt(0, boneyard.size()));
        currentPlayer.getHand().add(p);
        boneyard.remove(p);
    }
    
    /* 
     * Flips the current piece.
     * @param current piece
     * @return current piece but flipped
     */
    public Piece flipPiece(Piece curr) {
        int right = curr.rightVal;
        int left = curr.leftVal;
        curr.rightVal = left;
        curr.leftVal = right;
        return curr;
    }
    
    /* 
     * Check to see if the move the player made is legal or not.
     * @param current train being played on
     * @param current piece being played
     * @return true if move is legal, false if not
     */
    public boolean isLegalMove(String currTrain, Piece currPiece) {
        ArrayList<Piece> curr = new ArrayList<Piece>();
        switch(currTrain) {
            case "0":
            case "M":
                curr = trains.get(0).getPieces();
                if(trains.get(0).getPieces().size() == 0) {
                    if(engine.leftVal == currPiece.leftVal) {
                        trains.get(0).getPieces().add(currPiece);
                        return true;
                    }
                    else if(engine.leftVal == currPiece.rightVal) {
                        currPiece = flipPiece(currPiece);
                        trains.get(0).getPieces().add(currPiece);
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                if(curr.get(curr.size() - 1).rightVal == currPiece.leftVal) {
                    trains.get(0).getPieces().add(currPiece);
                    return true;
                }
                else if(curr.get(curr.size() - 1).rightVal == currPiece.rightVal) {
                    currPiece = flipPiece(currPiece);
                    trains.get(0).getPieces().add(currPiece);
                    return true;
                }
                break;
            case "1":
                curr = trains.get(1).getPieces();
                if(trains.get(1).getPieces().size() == 0) {
                    if(trains.get(1).getIsFree() == true) {
                        if(engine.leftVal == currPiece.leftVal) {
                            trains.get(1).getPieces().add(currPiece);
                            return true;
                        }
                        else if(engine.leftVal  == currPiece.rightVal) {
                            currPiece = flipPiece(currPiece);
                            trains.get(1).getPieces().add(currPiece);
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                }
                
                if(trains.get(1).getIsFree() == true) {
                    if(curr.get(curr.size() - 1).rightVal == currPiece.leftVal) {
                        trains.get(1).getPieces().add(currPiece);
                        return true;
                    }
                    else if(curr.get(curr.size() - 1).rightVal == currPiece.rightVal) {
                        currPiece = flipPiece(currPiece);
                        trains.get(1).getPieces().add(currPiece);
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                break;
            case "2":
                curr = trains.get(2).getPieces();
                if(trains.get(2).getPieces().size() == 0) {
                    if(trains.get(2).getIsFree() == true) {
                        if(engine.leftVal == currPiece.leftVal) {
                            trains.get(2).getPieces().add(currPiece);
                            return true;
                        }
                        else if(engine.leftVal == currPiece.rightVal) {
                            currPiece = flipPiece(currPiece);
                            trains.get(2).getPieces().add(currPiece);
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                }
                if(trains.get(2).getIsFree() == true) {
                    if(curr.get(curr.size() - 1).rightVal == currPiece.leftVal) {
                        trains.get(2).getPieces().add(currPiece);
                        return true;
                    }
                    else if(curr.get(curr.size() - 1).rightVal == currPiece.rightVal) {
                        currPiece = flipPiece(currPiece);
                        trains.get(2).getPieces().add(currPiece);
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                break;
            case "3":
                curr = trains.get(3).getPieces();
                if(trains.get(3).getPieces().size() == 0) {
                    if(trains.get(3).getIsFree() == true) {
                        if(engine.leftVal == currPiece.leftVal) {
                            trains.get(3).getPieces().add(currPiece);
                            return true;
                        }
                        else if(engine.leftVal == currPiece.rightVal) {
                            currPiece = flipPiece(currPiece);
                            trains.get(3).getPieces().add(currPiece);
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                }
                
                if(trains.get(3).getIsFree() == true) {
                    if(curr.get(curr.size() - 1).rightVal == currPiece.leftVal) {
                        trains.get(3).getPieces().add(currPiece);
                        return true;
                    }
                    else if(curr.get(curr.size() - 1).rightVal == currPiece.rightVal) {
                        currPiece = flipPiece(currPiece);
                        trains.get(3).getPieces().add(currPiece);
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                break;
            case "4":
                curr = trains.get(4).getPieces();
                if(trains.get(4).getPieces().size() == 0) {
                    if(trains.get(4).getIsFree() == true) {
                        if(engine.leftVal == currPiece.leftVal) {
                            trains.get(4).getPieces().add(currPiece);
                            return true;
                        }
                        else if(engine.leftVal == currPiece.rightVal) {
                            currPiece = flipPiece(currPiece);
                            trains.get(4).getPieces().add(currPiece);
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                }
                if(trains.get(4).getIsFree() == true) {
                    if(curr.get(curr.size() - 1).rightVal == currPiece.leftVal) {
                        trains.get(4).getPieces().add(currPiece);
                        return true;
                    }
                    else if(curr.get(curr.size() - 1).rightVal == currPiece.rightVal) {
                        currPiece = flipPiece(currPiece);
                        trains.get(4).getPieces().add(currPiece);
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                break;
            default:
                if(numPlayers == 2) {
                    System.out.println("\nNot a valid train. Please choose a different one.(M, 1 or 2)");
                    
                }
                else if(numPlayers == 3) {
                    System.out.println("\nNot a valid train. Please choose a different one.(M, 1, 2 or 3)");
                }
                else if(numPlayers == 4) {
                    System.out.println("\nNot a valid train. Please choose a different one.(M, 1, 2, 3 or 4)");
                }
                break;
        }
        return false;
    }
    
    /* 
     * Checks to see if the whole game is over.
     * @return true if game is over, false if not
     */
    public boolean checkGameOver() {
        if(roundCount > 10) {
            return true;
        }
        return false;
    }
    
    /* 
     * Set up engine pieces based of the round
     * @return engine piece
     */
    public Piece setEnginePiece() {
        Piece p = null;
        switch(roundCount) {
            case 1:
                p = boneyard.get(54);
                boneyard.remove(p);
                break;
            case 2:
                p = boneyard.get(52);
                boneyard.remove(p);
                break;
            case 3:
                p = boneyard.get(49);
                boneyard.remove(p);
                break;
            case 4:
                p = boneyard.get(45);
                boneyard.remove(p);
                break;
            case 5:
                p = boneyard.get(40);
                boneyard.remove(p);
                break;
            case 6:
                p = boneyard.get(34);
                boneyard.remove(p);
                break;
            case 7:
                p = boneyard.get(27);
                boneyard.remove(p);
                break;
            case 8:
                p = boneyard.get(19);
                boneyard.remove(p);
                break;
            case 9:
                p = boneyard.get(10);
                boneyard.remove(p);
                break;
            case 10:
                p = boneyard.get(0);
                boneyard.remove(p);
                break;
            default:
                System.out.print("Game Over");
                break;
        }
        return p;
    }
    
    /* 
     * Updates the console every round with the game state.
     */
    public void consoleUpdate() {
        System.out.println("\nEngine Piece:");
        System.out.println(engine.toString());
    
        System.out.println("(" + trains.get(0).getIsFree() + ") Mexican Train(M): ");
        for(int i = 0; i < trains.get(0).getPieces().size(); i++) {
            System.out.print(trains.get(0).getPieces().get(i).toString() + "  ");
        }
        System.out.println();
        if(numPlayers == 2) {
            trainPrint(2);
        }
        else if(numPlayers == 3) {
            trainPrint(3);
        }
        else if(numPlayers == 4) {
            trainPrint(4);
        }
       
        System.out.println("\n");
        handPrint(currentPlayer);
        System.out.println("\nBoneyard:");
        for(int i = 0; i < boneyard.size(); i++) {
            System.out.print(boneyard.get(i).toString() + "  ");
        }
        System.out.println();
    }
    
    /* 
     * Prints to the console the trains based of the number of players
     * @param number of players set
     */
    public void trainPrint(int numOfPlayers) {
        if(numOfPlayers == 2) {
            System.out.println("(" + trains.get(1).getIsFree() + ") Player 1 Train(1): ");
            for(int i = 0; i < trains.get(1).getPieces().size(); i++) {
                System.out.print(trains.get(1).getPieces().get(i).toString() + "  ");
            }
            System.out.println("\n(" + trains.get(2).getIsFree() + ") Player 2 Train(2): ");
            for(int i = 0; i < trains.get(2).getPieces().size(); i++) {
                System.out.print(trains.get(2).getPieces().get(i).toString() + "  ");
            }
        }
        else if(numOfPlayers == 3) {
            System.out.println("(" + trains.get(1).getIsFree() + ") Player 1 Train(1): ");
            for(int i = 0; i < trains.get(1).getPieces().size(); i++) {
                System.out.print(trains.get(1).getPieces().get(i).toString() + "  ");
            }
            System.out.println("\n(" + trains.get(2).getIsFree() + ") Player 2 Train(2): ");
            for(int i = 0; i < trains.get(2).getPieces().size(); i++) {
                System.out.print(trains.get(2).getPieces().get(i).toString() + "  ");
            }
            System.out.println("\n(" + trains.get(3).getIsFree() + ") Player 3 Train(3): ");
            for(int i = 0; i < trains.get(3).getPieces().size(); i++) {
                System.out.print(trains.get(3).getPieces().get(i).toString() + "  ");
            }
        }
        else if(numOfPlayers == 4) {
            System.out.println("(" + trains.get(1).getIsFree() + ") Player 1 Train(1): ");
            for(int i = 0; i < trains.get(1).getPieces().size(); i++) {
                System.out.print(trains.get(1).getPieces().get(i).toString() + "  ");
            }
            System.out.println("\n(" + trains.get(2).getIsFree() + ") Player 2 Train(2): ");
            for(int i = 0; i < trains.get(2).getPieces().size(); i++) {
                System.out.print(trains.get(2).getPieces().get(i).toString() + "  ");
            }
            System.out.println("\n(" + trains.get(3).getIsFree() + ") Player 3 Train(3): ");
            for(int i = 0; i < trains.get(3).getPieces().size(); i++) {
                System.out.print(trains.get(3).getPieces().get(i).toString() + "  ");
            }
            System.out.println("\n(" + trains.get(4).getIsFree() + ") Player 4 Train(4): ");
            for(int i = 0; i < trains.get(4).getPieces().size(); i++) {
                System.out.print(trains.get(4).getPieces().get(i).toString() + "  ");
            }
        }
    }
    
    /* 
     * Prints to the console the current players hand.
     * @param current player
     */
    public void handPrint(Player p) {
        int id = p.ID + 1;
        System.out.println("Player " + id + "'s hand:");
        for(int i = 0; i < p.getHand().size(); i++) {
            System.out.print(p.getHand().get(i).toString() + "  ");
        }
    }
    
    /* 
     * Generates all 55 pieces of 9x9 dominoes and places those pieces
     * into the boneyard list.
     */
    public void generateBoneyard() {
        boneyard = new ArrayList<Piece>();
        for(int i = 0; i < BONE_MAX; i++) {
            if(i <= 9) {
                boneyard.add(new Piece(0, i));
            }
            else if(i > 9 && i <= 18) {
                boneyard.add(new Piece(1, i - 9));
            }
            else if(i > 18 && i < 27) {
                boneyard.add(new Piece(2, i - 17));
            }
            else if(i > 27 && i <= 34) {
                boneyard.add(new Piece(3, i - 25));
            }
            else if(i > 34 && i <= 40) {
                boneyard.add(new Piece(4, i - 31));
            }
            else if(i > 40 && i <= 45) {
                boneyard.add(new Piece(5, i - 36));
            }
            else if(i > 45 && i <= 49) {
                boneyard.add(new Piece(6, i - 40));
            }
            else if(i > 49 && i <= 52) {
                boneyard.add(new Piece(7, i - 43));
            }
            else if(i > 52 && i <= 54) {
                boneyard.add(new Piece(8, i - 45));
            }
            else if(i > 54 && i <= 55) {
                boneyard.add(new Piece(9, i - 46));
            }
        }
    }
    
    /* 
     * Sets up each players starting hand based on the number of players.
     * @param hand size based on number of players
     * @return list of pieces set in players hand.
     */
    public ArrayList<Piece> setPlayerHand(int startHandSize) {
        ArrayList<Piece> hand = new ArrayList<Piece>();
        for(int i = 0; i < startHandSize; i++) {
            Piece rand = boneyard.get(ThreadLocalRandom.current().nextInt(0, boneyard.size()));
            hand.add(rand);
            boneyard.remove(rand);
        }
        return hand;
    }
    
    /* 
     * Sets up the number of players to the list and
     * also assigns the hand size based off how many players
     * are playing.
     * @param number of players
     */
    public void setPlayers(int numOfPlayers) {
        players = new ArrayList<Player>();
        int handSize = 0;
        if(numOfPlayers == 2) {
            handSize = 15;
        }
        else if(numOfPlayers == 3) {
            handSize = 13;
        }
        else if(numOfPlayers == 4) {
            handSize = 10;
        }
        for(int i = 0; i < numOfPlayers; i++) {
            ArrayList<Piece> h = setPlayerHand(handSize);
            players.add(new Player(i, h));
        }
    }
}
