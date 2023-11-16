/*
 * Name: Damian Franco
 *       101789677
 *       CS 351 - 004
 * 
 * Project: Mexican Train Dominoes
 * Version: GUI V7
 */
package dominoes;

import javafx.scene.image.Image;

public class Piece {
    /* ID value for piece */
    private int ID;
    /* Left value integer */
    private int leftVal;
    /* Right value integer */
    private int rightVal;
    /* Image variable to assign an the image to the piece */
    private Image i;
    
    /* Constructor for the Piece object */
    public Piece(int ID, int leftVal, int rightVal, Image i) {
        this.ID = ID;
        this.leftVal = leftVal;
        this.rightVal = rightVal;
        this.i = i;
    }
    
    /* Getter for the Image corresponding to the dominoe */
    public Image getImage() {
        return i;
    }
    
    /* Setter for the Image corresponding the dominoe */
    public void setImage(Image i) {
        this.i = i;
    }
    
    /* Getter for the left value of the dominoe */
    public int getLeftVal() {
        return leftVal;
    }
    
    /* Getter for the right value of the dominoe */
    public int getRightVal() {
        return rightVal;
    }
    
    /* Getter for the ID value of the dominoe */
    public int getID() {
        return ID;
    }
    
    /* Setter for the ID value of the dominoe */
    public void setID(int ID) {
        this.ID = ID;
    }
    
    /* Setter for the left value */
    public void setLeftVal(int leftVal) {
        this.leftVal = leftVal;
    }
    
    /* Setter for the right value */
    public void setRightVal(int rightVal) {
        this.rightVal = rightVal;
    }
    
    /* String representation of the dominoe */
    public String toString() {
        return "[" + leftVal +", " + rightVal + "]";
    }
}
