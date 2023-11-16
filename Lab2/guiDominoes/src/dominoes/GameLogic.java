/*
 * Name: Damian Franco
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Mexican Train Dominoes
 * Version: GUI V7
 */
package dominoes;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class GameLogic {
    /* Boneyard intialization */
    private ArrayList<Piece> boneyard;
    /* Player list intialization */
    private ArrayList<Player> players;
    /* Train list intialization */
    private ArrayList<Train> trains;
    /* Piece setup for the engine piece */
    private Piece engine;
    /* Max number in the boneyard */
    private final int BONE_MAX = 56;
    /* Variable setup for scores and num counts for rounds and player types */
    private int numPlayers, numAIPlayers, roundCount = 1, p1Score, p2Score, p3Score, p4Score;
    /* Flags to set off round, game, passed players and if we need to flip the piece on the GUI */
    private boolean roundOverFlag, gameOverFlag, passFlag, doFlipPiece = false;;
    /* Players holding the previous and current players */
    private Player currentPlayer, passedPlayer;
    /* Piece to hold current piece being played */
    private Piece currentPiece;
    /* String to hold the ID of the best move for AI */
    private String bestMoveTrain;
    
    /* 
     * Gets the best move for the AI.
     * @param current players hand
     * @param integer of right value of the last piece on the train
     * @return best piece to play from AI's hand
     */
    public Piece getBestMove(ArrayList<Piece> currHand, int t) {
        Piece bestMove = new Piece(100, 10, 11, null);;
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

    /* 
     * Handles the AI player's algorithm and turn.
     */
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
    
    /* 
     * Sets the free train of a player that has passed.
     */
    public void setFreeOffPassed() {
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
    }
    
    /* 
     * Checks if the current turn is the AI's turn.
     * @return boolean if AI's turn true or false
     */
    public boolean isAITurn() {
        if(numPlayers == 4) {
            if(numAIPlayers == 3) {
                if(currentPlayer.getID() == 1 || currentPlayer.getID() == 2 || currentPlayer.getID() == 3) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else if(numAIPlayers == 2) {
                if(currentPlayer.getID() == 2 || currentPlayer.getID() == 3) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else if(numAIPlayers == 1) {
                if(currentPlayer.getID() == 3) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        else if(numPlayers == 3) {
            if(numAIPlayers == 2) {
                if(currentPlayer.getID() == 1 || currentPlayer.getID() == 2) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else if(numAIPlayers == 1) {
                if(currentPlayer.getID() == 2) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        else if(numPlayers == 2) {
            if(numAIPlayers == 1) {
                if(currentPlayer.getID() == 1) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }
    
    /* 
     * Handles the AI players turn based on the number of players and AI players.
     */
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
            }
        }
       
    }
    
    /* 
     * Set the current playable/free trains according to the current player.
     * @param Player object of the current player
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
     * Handles the turn changing based off the current player.
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
     * Handles the play option when choosen.
     * @param current player 
     * @param current piece
     */
    public void optionPlay(Player currentP, Piece curr) {
        String t = "";
        currentPiece = curr;
        boolean check = isLegalMove(t, currentPiece);
        if(check) {
            currentP.getHand().remove(currentPiece);
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
     * Flips the current piece being played. Also does it on the GUI now.
     * @param current piece
     * @return current piece but flipped
     */
    public Piece flipPiece(Piece curr) {
        int right = curr.rightVal;
        int left = curr.leftVal;
        curr.rightVal = left;
        curr.leftVal = right;
        doFlipPiece = true;
        return curr;
    }
    
    /* 
     * Check to see if the move the player made is legal or not.
     * @param current train being played on
     * @param current piece being played
     * @return true if move is legal, false if not
     */
    public boolean isLegalMove(String currTrain, Piece currPiece) {
        doFlipPiece = false;
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
     * Generates all 55 pieces of 9x9 dominoes and places those pieces
     * into the boneyard list.
     */
    public void generateBoneyard() {
        boneyard = new ArrayList<Piece>();
        for(int i = 0; i < BONE_MAX; i++) {
            if(i <= 9) {
                boneyard.add(new Piece(i, 0, i, null));
            }
            else if(i > 9 && i <= 18) {
                boneyard.add(new Piece(i, 1, i - 9, null));
            }
            else if(i > 18 && i <= 26) {
                boneyard.add(new Piece(i, 2, i - 17, null));
            }
            else if(i > 26 && i <= 33) {
                boneyard.add(new Piece(i, 3, i - 24, null));
            }
            else if(i > 33 && i <= 39) {
                boneyard.add(new Piece(i, 4, i - 30, null));
            }
            else if(i > 39 && i <= 44) {
                boneyard.add(new Piece(i, 5, i - 35, null));
            }
            else if(i > 44 && i <= 48) {
                boneyard.add(new Piece(i, 6, i - 39, null));
            }
            else if(i > 48 && i <= 51) {
                boneyard.add(new Piece(i, 7, i - 42, null));
            }
            else if(i > 51 && i <= 53) {
                boneyard.add(new Piece(i, 8, i - 44, null));
            }
            else if(i > 53 && i <= 54) {
                boneyard.add(new Piece(i, 9, i - 45, null));
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
            players.add(new Player(i, 0, h));
        }
    }
}
