package dominoes;
import java.util.ArrayList;
/*
 * Name: Damian Franco
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Mexican Train Dominoes
 * Version: GUI V7
 */
public class Player {
    /* Integer for the ID of the player */
    private int ID;
    /* Integer to hold players score */
    private int score;
    /* List of the pieces in the players hand */
    private ArrayList<Piece> hand;
    
    /* Constructor for player */
    public Player(int ID, int score, ArrayList<Piece> hand) {
        this.ID = ID;
        this.hand = hand;
    }
    
    /* Getter for the players ID */
    public int getID() {
        return ID;
    }
    
    /* Getter for the players score */
    public int getScore() {
        return score;
    }
    
    /* Setter for the players score */
    public void setScore(int score) {
        this.score = score;
    }
    
    /* Getter for the pieces in the players hand */
    public ArrayList<Piece> getHand() {
        return hand;
    }
    
    /* Setter for the players ID */
    public void setID(int ID) {
        this.ID = ID;
    }
    
    /* Setter for the players hand */
    public void setHand(ArrayList<Piece> hand) {
        this.hand = hand;
    }
    
    /* Getter for the pip count to get score */
    public int getPipCount() {
        int x = 0;
        for(int i = 0; i < hand.size(); i++) {
            x += hand.get(i).leftVal;
            x += hand.get(i).rightVal;
        }
        return x;
    }
    
    /* String representation of the players hand */
    public String toString() {
        return hand.toString();
    }
}
