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
public class Train {
    /* Name for ID purpose */
    private String name;
    /* Boolean to see if train if free/playable or not */
    private boolean free;
    /* List of pieces in the train */
    private ArrayList<Piece> pieces;

    /* Train constructor */
    public Train(String name, boolean free, ArrayList<Piece> pieces) {
        this.name = name;
        this.free = free;
        this.pieces = pieces;
    }
    
    /* Getter for name */
    public String getName() {
        return name;
    }
    
    /* Setter for name */
    public void setName(String name) {
        this.name = name;
    }
    
    /* Getter for the is free variable */
    public boolean getIsFree() {
        return free;
    }
    
    /* Setter for the is free variable */
    public void setIsFree(boolean free) {
        this.free = free;
    }
    
    /* Getter for the list of pieces in the train */
    public ArrayList<Piece> getPieces() {
        return pieces;
    }
    
    /* Getter for the last piece on the train */
    public Piece getLastPiece() {
        return pieces.get(pieces.size() - 1);
    }
    
    /* Setter for the list of pieces */
    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }
}
